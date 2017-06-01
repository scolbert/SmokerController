const planReducer = (state = {
                        plans:[{id:1, name:'Test Smoking Plan 1'}, {id:2, name:'Test Smoking Plan 3'}],
                        selectedPlan: '1'
                     },
                     action) => {
    switch(action.type) {
        case "GET_PLANS":
            state = Object.assign({}, state, {plans: action.payload})
            break;
        case "SET_SELECTED_PLAN":
            state = Object.assign({}, state, {selectedPlan: action.payload})
            break;
    }
    return state;

};

export default planReducer;