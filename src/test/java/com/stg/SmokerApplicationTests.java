package com.stg;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.stg.SmokerApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SmokerApplication.class)
@ActiveProfiles(profiles = "test")
@Ignore

public class SmokerApplicationTests {

	@Test
	public void contextLoads() {
	}

}
