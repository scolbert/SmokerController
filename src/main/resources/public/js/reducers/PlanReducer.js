const planReducer = (state = {plans:[{id:1, name:'Test Smoking Plan 1'}, {id:2, name:'Test Smoking Plan 2'}]},
                     action) => {
    switch(action.type) {
        // HTTP GET to /api/v1/TempTiming gets list of all temperature timing programs
        case "GET_PLANS":
            state = state;
            break;
        // post to /api/v1/TempTiming adds a temperature timing program
        case "PLAN_ADD":
            state.plans = state.plans.concat(action.payload);
            break;
    }
    return state;
};

export default planReducer;