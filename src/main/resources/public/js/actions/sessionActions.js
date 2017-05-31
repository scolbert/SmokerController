import $ from 'jquery';

export function startSession() {
    return dispatch => {
        $.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            url: 'http://localhost:8080/api/v1/smoke_session',
            data: JSON.stringify({id:1, description:'Test Description', meat:'ribs', referenceThermometer: 1, temperatureTimingId: 5}),
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