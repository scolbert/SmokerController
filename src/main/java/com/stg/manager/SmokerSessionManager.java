package com.stg.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.OptionalDouble;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.stg.io.HardwareInterface;
import com.stg.model.SmokeSession;
import com.stg.model.SmokeSessionDetail;
import com.stg.model.Temperature;
import com.stg.model.Temperature.Scale;
import com.stg.model.TemperatureTimingDetail;
import com.stg.model.TurnOffCriteria;
import com.stg.repository.SmokeSessionDetailRepository;
import com.stg.repository.SmokeSessionRepository;
import com.stg.repository.TemperatureTimingDetailRepository;
import com.stg.repository.TemperatureTimingRepository;

@Component
public class SmokerSessionManager {

	Log logger = LogFactory.getLog(getClass());

	@Autowired
	private HardwareInterface smoker;

	@Autowired
	private FanManager fan;

	@Autowired
	private SmokeSessionDetailRepository detailRepo;

	@Autowired
	private SmokeSessionRepository smokeSessionRepository;

	@Autowired
	private TemperatureTimingRepository tempTimingRepo;

	@Autowired
	private TemperatureTimingDetailRepository tempTimingDetailRepo;

	private SmokeSession currentSession = null;

	private SmokingMonitorThread smokeMonitor = null;

	private Thread smokingThread = null;

	@Value("${hardware.polling.interval}")
	private Integer refreshRate;

	@Value("${session.update.interval}")
	private Integer sessionUpdateInterval;

	/**
	 * This will the currently active SmokeSession or null if there isnt an
	 * active session
	 * 
	 * DO NOT MODIFY THIS WHILE THE SESSION IS IN PROGRESS
	 * 
	 * @return SmokeSession
	 */
	public SmokeSession getActiveSession() {
		return currentSession;
	}

	public synchronized Boolean isSessionInProgress() {
		if (currentSession == null) {
			return Boolean.FALSE; // session is already in progress
		} else {
			return Boolean.TRUE;
		}
	}

	public synchronized Boolean startSession(SmokeSession session) {
		if (currentSession != null) {
			return Boolean.FALSE; // session is already in progress
		}

		currentSession = session;

		smokeSessionRepository.saveAndFlush(currentSession);

		smokeMonitor = new SmokingMonitorThread();
		smokingThread = new Thread(smokeMonitor);
		smokingThread.setDaemon(true);
		smokingThread.start();

		return Boolean.TRUE;
	}

	public synchronized SmokeSession stopSession() {
		if (currentSession == null) {
			return null;
		}
		SmokeSession stoppedSession = currentSession;
		smokeMonitor.stop();
		smokingThread.interrupt();

		for (int loop = 0; loop < 5 && smokingThread.isAlive(); loop++) {
			try {
				if (logger.isDebugEnabled()) {
					logger.debug("Waiting for smoker thread to die.");
				}
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// Not likely to ever get here
				logger.error(e);
			}
		}

		if (smokingThread.isAlive()) {
			logger.error("Thread wont die.  Please restart the app");
		}

		currentSession = null;
		smokeMonitor = null;
		smokingThread = null;
		return stoppedSession;
	}

	private TemperatureTimingDetail getTargetTemp() {
		List<TemperatureTimingDetail> timings = tempTimingDetailRepo
				.findAllByTemperatureTiming(tempTimingRepo.getOne(currentSession.getTemperatureTimingId()));

		if (timings.size() == 1) {
			if (logger.isDebugEnabled()) {
				logger.debug("Using temp timing step: " + timings.get(0));
			}
			return timings.get(0);
		}

		Collections.sort(timings);
		long minutesFromStart = (System.currentTimeMillis() - currentSession.getStartDate().getTime()) / 1000 / 60;

		for (TemperatureTimingDetail detail : timings) {
			if (detail.getMinutesAtTemp() == null) {
				if (logger.isDebugEnabled()) {
					logger.debug("Using temp timing step: " + detail);
				}
				return detail;
			}
			if (minutesFromStart - detail.getMinutesAtTemp() < 0) {
				if (logger.isDebugEnabled()) {
					logger.debug("Using temp timing step: " + detail);
				}
				return detail;
			}
			minutesFromStart -= detail.getMinutesAtTemp();
		}
		return timings.get(timings.size() - 1);
	}

	private class SmokingMonitorThread implements Runnable {

		private volatile boolean run = true;

		private Integer average(List<Double> list) {
			OptionalDouble avg = list.stream().mapToDouble(a -> a).average();
			if (avg.isPresent()) {
				return new Double(Math.rint(avg.getAsDouble())).intValue();
			} else {
				return -1;
			}
		}

		@Override
		public void run() {

			List<List<Double>> probeReadings = new ArrayList<>();
			probeReadings.add(new ArrayList<>());
			probeReadings.add(new ArrayList<>());
			probeReadings.add(new ArrayList<>());
			probeReadings.add(new ArrayList<>());
			

			Long sessionUpdateTime = System.currentTimeMillis();
			while (run) {
				Long runStartTime = System.currentTimeMillis();
				for (int loop = 0; loop < 4 && run; loop++) {
					probeReadings.get(loop).add(smoker.getTemp(loop + 1));
				}

				if (sessionUpdateInterval * 1000 < (System.currentTimeMillis() - sessionUpdateTime)) {
					SmokeSessionDetail detail = saveDetails(probeReadings);

					if (isDoneCriteriaMet(detail) || detail.getTemperatureTimingDetail().getTemperature().getTemp(Scale.KELVIN) == 0) {
						SmokeSessionDetail finalDetail = new SmokeSessionDetail();
						BeanUtils.copyProperties(detail, finalDetail);
						finalDetail.setId(null);
						finalDetail.setFan(0);
						detailRepo.saveAndFlush(finalDetail);
						detail = finalDetail;
						run = false;
					}

					smoker.setFan(detail.getFan());
					// empty the lists for the next run
					for (List<Double> readings : probeReadings) {
						readings.clear();
					}
					sessionUpdateTime = System.currentTimeMillis();
				}

				try {
					Long sleepTime = refreshRate * 1000 - (System.currentTimeMillis() - runStartTime);
					if (sleepTime > 0) {
						Thread.sleep(sleepTime);
					} else {
						logger.info("Took longer to process than the refresh rate: " + sleepTime);
					}
				} catch (InterruptedException e) {
					logger.debug("Thread interupted.  Most likely stopping: " + run);
				}
			}
			currentSession = null;
		}

		private boolean isDoneCriteriaMet(SmokeSessionDetail detail) {
			boolean done;
			if (detail.getTemperatureTimingDetail().getTurnOffCriteria() != null) {
				done = true;
				TurnOffCriteria crit = detail.getTemperatureTimingDetail().getTurnOffCriteria();
				List<Integer> probeList = crit.getProbeList();
				if (!probeList.isEmpty()) {
					for (Integer probe : crit.getProbeList()) {
						Temperature probeTemp;
						switch (probe) {
						case 1:
							probeTemp = detail.getThermometer1();
							break;
						case 2:
							probeTemp = detail.getThermometer1();
							break;
						case 3:
							probeTemp = detail.getThermometer1();
							break;
						case 4:
							probeTemp = detail.getThermometer1();
							break;
						default:
							logger.error("Invalid probe: " + probe);
							continue;
						}
						if (probeTemp.compareTo(crit.getTargetTemperature()) < 0) {
							done = false;
							break;
						}
					}

				} else {
					done = false;
				}

			}else {
				done = false;
			}
			return done;
		}

		private SmokeSessionDetail saveDetails(List<List<Double>> probeReadings) {
			if (logger.isDebugEnabled()) {
				logger.debug("saving session details");
			}
			SmokeSessionDetail detail = new SmokeSessionDetail();
			detail.setTime(new Date());
			detail.setSession(currentSession);
			detail.setThermometer1(
					new Temperature(average(probeReadings.get(0)), Scale.valueOf(smoker.getTemperatureScale())));
			detail.setThermometer2(
					new Temperature(average(probeReadings.get(1)), Scale.valueOf(smoker.getTemperatureScale())));
			detail.setThermometer3(
					new Temperature(average(probeReadings.get(2)), Scale.valueOf(smoker.getTemperatureScale())));
			detail.setThermometer4(
					new Temperature(average(probeReadings.get(3)), Scale.valueOf(smoker.getTemperatureScale())));

			// get the current cooking step info
			TemperatureTimingDetail currentTargetTemp = getTargetTemp();
			detail.setTemperatureTimingDetail(currentTargetTemp);

			// calculate new fan setting
			Integer newFanSetting = fan.calculateFanValue(
					new Temperature(average(probeReadings.get(currentSession.getReferenceThermometer() - 1)),
							Scale.valueOf(smoker.getTemperatureScale())),
					currentTargetTemp.getTemperature(), smoker.getFanSetting());
			detail.setFan(newFanSetting.intValue());

			detailRepo.saveAndFlush(detail);

			if (logger.isDebugEnabled()) {
				logger.debug("Saved session details" + detail);
			}
			return detail;
		}

		public void stop() {
			run = false;
		}

	}
}
