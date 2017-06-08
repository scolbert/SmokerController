import $ from 'jquery';
import store from '../store.js';

export function startSession() {
    return dispatch => {
        const selectedPlan = store.getState().planState.selectedPlan;
        console.log("selected plan is ", selectedPlan);
        // search plans for the one with id of selected plan
        const plans = store.getState().planState.plans;
        const selectedPlanDetails = plans.filter((plan) => {
            if(plan.id === parseInt(selectedPlan)){
                return true;
            }
            return false;
        });
        console.log("selectedPlanDetails is ", selectedPlanDetails);
        $.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            url: 'http://localhost:8080/api/v1/smoke_session',
            data: JSON.stringify({
                id:1,
                description: selectedPlanDetails.description,
                meat:'meat not specified',
                referenceThermometer: 1,
                temperatureTimingId: selectedPlanDetails.id}),
            method: 'POST',
            success: (response) => {
                dispatch ({
                    type: "START_SESSION",
                    payload: response
                })},
            dataType: 'application/json'
        });
    }
}

function convertSelectIndexToArrayIndex(index){
    return index -1;
}

export function stopSession() {
    return dispatch => {
        $.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            url: 'http://localhost:8080/api/v1/smoke_session/1/STOP',
            success: (response) => {
                dispatch ({
                    type: "STOP_SESSION",
                    payload: response
                })},
            dataType: 'application/json'
        });
    }
}