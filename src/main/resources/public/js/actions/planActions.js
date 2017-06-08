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
                dispatch ({
                    type: "ADD_PLAN",
                    payload: {}
                })}
        });
    }
}

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
        "name": activePlanName,
        "tempDetails": buildPlanStepJsonArray()
    })
}

function buildPlanStepJsonArray(){
    const activePlanSteps = store.getState().planState.activePlanSteps;
    return activePlanSteps.map((item) => buildSinglePlanStepJson(item));
}

function buildSinglePlanStepJson(step){
    console.log("Selected Criteira is ", step.selectedCriteria);
    let turnOffCriteria = getCriteria(step.selectedCriteria);
    console.log("turnoffcritiera is ", turnOffCriteria);
    return(
        {
            "id": 0,
            "minutesAtTemp": parseInt(step.minutes) + parseInt(step.hours * 60),
            "order": step.order,
            "temperatureTimingId": step.id,
            "temperature": {
                "temp": 0,
                "tempK": convertFarenheitToKelvin(step.temperature)
            },
            "turnOffCriteria": turnOffCriteria
        }
    )

}

function getCriteria(id){
    const criteriaList = store.getState().criteriaState.turnOffCriteriaList;
    console.log("criteriaList is ", criteriaList);
    const filteredList = criteriaList.filter((item) => {
        console.log("comparing item.id to id " + item.id + " : " + id);
        if(item.id == id){
            console.log("found a match");
            return true;
        } else {
            console.log("does not match");
            return false;
        }
    });
    console.log("FilteredList is ", filteredList);
    return filteredList[0];

}



