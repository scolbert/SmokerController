import $ from 'jquery';

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
    console.log("Inside of planAction.addStep");
    return ({
        type: "ADD_STEP",
        payload: 2
    })
}

export function deleteStep(key) {
    console.log("Inside of planAction.deleteStep");
    return ({
        type: "DELETE_STEP",
        payload:key
    })
}