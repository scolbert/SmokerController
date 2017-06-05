import $ from 'jquery';
import store from '../store.js';

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


export function submit() {
    return dispatch => {
         const criteriaState = store.getState().criteriaState;
        // const selectedPlanDetails = store.getState().planState.plans[convertSelectIndexToArrayIndex(selectedPlan)];

        $.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            url: 'http://localhost:8080/api/v1/TurnOffCriteria',
            data: JSON.stringify({
                id: 1,
                probeList: criteriaState.probes,
                probes:criteriaState.probes.toString(),
                targetTemperature: {temp: criteriaState.temperature}
            }),
            method: 'POST',
            success: (response) => {
                dispatch ({
                    type: "SUCCESSFUL_SUBMIT",
                    payload: response
                })},
            dataType: 'application/json'
        });
    }
}