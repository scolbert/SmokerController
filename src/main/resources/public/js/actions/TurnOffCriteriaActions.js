import $ from 'jquery';
import store from '../store.js';
import convertFarenheitToKelvin from '../helpers/TemperatureConverter.js';

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
                targetTemperature: {tempK: criteriaState.temperature}
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

export function getTurnOffCriteria() {
    return dispatch => {
        $.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            url: 'http://localhost:8080/api/v1/TurnOffCriteria',
            method: 'GET',
            success: (response) => {
                console.log("Turn off criteria received from database is", response);
                dispatch ({
                    type: "GET_TURN_OFF_CRITERIA",
                    payload: response
                })
            },
            error: (response, textStatus, errorThrown) => {
                console.log("error response is ", response);
                console.log("textStatus is ", textStatus);
                console.log("error thrown is ", errorThrown);
            }
        })
    }
}