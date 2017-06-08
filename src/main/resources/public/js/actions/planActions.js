import $ from 'jquery';
import store from '../store.js';

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
    return (
        [
            buildSinglePlanStepJson()
        ]
    )
}

function buildSinglePlanStepJson(){
    return(
        {
            "id": 0,
            "minutesAtTemp": 10,
            "order": 0,
            "temperature": {
                "temp": 0,
                "tempK": 500
            },
            "turnOffCriteria": getCriteria(0)
        }
    )

}

function getCriteria(id){
    return (
        {
            "id": 0,
            "probeList": [
                0
            ],
            "probes": "1,2,3",
            "targetTemperature": {
                "temp": 0,
                "tempK": 600
            }
        }
    )
}



