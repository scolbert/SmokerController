
// session:{id:1, description:'Test Description', meat:'ribs', referenceThermometer: 1, temperatureTimingId: 5}

const sessionReducer = (state = {sessionState: {startSession: () => {console.log("running uninitialized startSession")}}}, action) => {
    switch(action.type) {
        // HTTP POST to /api/v1/smoke_session
        case "START_SESSION":
            // state = {session:action.payload};
            state = Object.assign({}, state, {lastStartedSession:action.payload})
            break;
        case "STOP_SESSION":
            // state = {session:action.payload};
            state = Object.assign({}, state, {lastStoppedSession:action.payload})
            break;
    }
    return state;
};

export default sessionReducer;