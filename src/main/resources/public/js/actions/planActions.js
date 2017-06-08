import $ from 'jquery';
import store from '../store.js';
import { convertFarenheitToKelvin } from '../helpers/TemperatureConverter.js';

export function getPlans() {
    return dispatch => {
        $.ajax({
            url: 'http://localhost:8080/api/v1/TempTiming',
            success: (data) => {
                dispatch ({
                    type: "GET_PLANS",
                    payload: data
                })},
            dataType: 'json'
        });
    }
}

export function handlePlanSelection(e) {
    e.persist();
    return dispatch => {
        dispatch({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            type: "SET_SELECTED_PLAN",
            payload: e.target.value
        })
    }
}

export function addStep() {
    return ({
        type: "ADD_STEP",
        payload: 2
    })
}

export function deleteStep(key) {
    return ({
        type: "DELETE_STEP",
        payload:key
    })
}

export function changeHours(index, text){
    return ({
        type: "UPDATE_HOURS",
        payload:{index: index, newHours: text}
    })
}

export function changeMinutes(index, text){
    return ({
        type: "UPDATE_MINUTES",
        payload:{index: index, newMinutes: text}
    })
}

export function changeTemperature(index, text){
    return({
        type: "UPDATE_TEMPERATURE",
        payload:{index: index, newTemperature: text}
    })
}

export function changeAmbientProbe(index, probe){
    console.log("in action probe is ", probe);
    return({
        type: "CHANGE_SELECTED_PROBE",
        payload:{index: index, newSelectedProbe: probe}
    })
}

export function changeSelectedCriteria(index, criteria){
    return({
        type: "CHANGE_SELECTED_CRITERIA",
        payload:{index: index, newSelectedCriteria:criteria}
    })
}

export function addPlan(){
    return dispatch => {
        $.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            url: 'http://localhost:8080/api/v1/TempTiming',
            method: 'POST',
            data: JSON.stringify(buildPlanJson()),
            success: (response) => {
                createPlanSteps(response.id)
                dispatch ({
                    type: "ADD_PLAN",
                    payload: {}
                })}
        });
    }
}

function createPlanSteps(planId){
    console.log("id is ", planId);
    const index = 0;
    callNextAction(index, planId)
}

function callNextAction(index, planId){
    const planStepsArray = store.getState().planState.activePlanSteps;
    console.log("steps array is", planStepsArray);
    console.log("inside of callNextAction index is ", index);
    if(index === planStepsArray.length) {
        return;
    }
    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        url: 'http://localhost:8080/api/v1/TempTiming/' + planId + '/details',
        method: 'POST',
        data: JSON.stringify({
            "id": 0,
            "minutesAtTemp": parseInt(planStepsArray[index].minutes) + parseInt(planStepsArray[index].hours * 60),
            // "order": planStepsArray[index].order, // NOTE THAT INCLUDING AN ORDER BREAKS THINGS ON THE SERVER
            // IF YOU INCLUDE AN ORDER, A BUG IN THE SYSTEM WILL ONLY ALLOW ONE DETAIL TO BE ADDED USING THE ENDPOINT
            // ALL OTHERS WILL JUST MYSTERIOUSLY BE NOT ADDED
            "temperatureTiming": {"id": planId},
            "temperature": {
                "temp": 0,
                "tempK": convertFarenheitToKelvin(planStepsArray[index].temperature)
            },
            "turnOffCriteria": {"id": planStepsArray[index].selectedCriteria}
        }),
        success: (response) => {
            console.log("preparing to call next action with index of ", index);
            console.log("and planId of ", planId);
            setTimeout(callNextAction(index + 1, planId), 1000)
        }
    })
}


// function addStepsToApi(planId)

export function addName(name){
    return({
        type: "ADD_NAME",
        payload:name
    })
}

export function addDescription(description){
    return({
        type: "ADD_DESCRIPTION",
        payload:description
})
}

function buildPlanJson(){
    const activePlanName = store.getState().planState.activePlanName;
    const activePlanDescription = store.getState().planState.activePlanDescription;
    return ({
        "description": activePlanDescription,
        "id": 0,
        "name": activePlanName
    })
}

function buildPlanStepJsonArray(){
    const activePlanSteps = store.getState().planState.activePlanSteps;
    return activePlanSteps.map((item) => buildSinglePlanStepJson(item));
}

function buildSinglePlanStepJson(step){
    console.log("Selected Criteira is ", step.selectedCriteria);
    // let turnOffCriteria = getCriteria(step.selectedCriteria);
    // console.log("turnoffcritiera is ", turnOffCriteria);
    return(
        {
            "id": 0,
            "minutesAtTemp": parseInt(step.minutes) + parseInt(step.hours * 60),
            "order": step.order,
            "temperatureTiming": {"id": step.id},
            "temperature": {
                "temp": 0,
                "tempK": convertFarenheitToKelvin(step.temperature)
            },
            // "turnOffCriteria": turnOffCriteria
            "turnOffCriteria": {"id": step.selectedCriteria}
        }
    )

}





