import $ from 'jquery';

export function toggleProbe(probeNumber) {
    return (
        {
            type:"TOGGLE_PROBE",
            payload: probeNumber
        }
    )
}

export function setTemperature(temperature) {
    return (
        {
            type:"SET_TEMPERATURE",
            payload: temperature
        }
    )
}


export function toggleIncludeProbe(probeNumber) {

}