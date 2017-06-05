import $ from 'jquery';

export function toggleProbe(probeNumber) {
    return (
        {
            type:"TOGGLE_PROBE",
            payload: probeNumber
        }
    )
}


export function toggleIncludeProbe(probeNumber) {

}