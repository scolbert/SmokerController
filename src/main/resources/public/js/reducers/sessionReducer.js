
const sessionReducer = (state = {
                            // Start Session
                            // description
                            // id
                            // meat
                            // reference Thermometer
                            // Stop Session
                            // Get Current Session
                            // getSessions
                            session:{id:1, description:'Test Description', meat:'ribs', referenceThermometer: 1, }},
                        action) => {
    switch(action.type) {
        // HTTP POST to /api/v1/smoke_session
        case "START":
            state = state;
            break;
    }
    return state;
};

export default sessionReducer;