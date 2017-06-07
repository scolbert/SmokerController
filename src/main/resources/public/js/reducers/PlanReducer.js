const planReducer = (state = {
                        plans:[{id:1, name:'Test Smoking Plan 1'}, {id:2, name:'Test Smoking Plan 3'}],
                        selectedPlan: '1',
                        nextStep: 1,
                        activePlanSteps:[
                            {
                                key:0,
                                order:0,
                                hours:0,
                                minutes:0,
                                selectedProbe:1,
                                temperature:0,
                                selectedCriteria:1
                            }
                        ]
                     },
                     action) => {

    console.log("action.type is " + action.type);
    switch(action.type) {
        case "GET_PLANS":
            state = Object.assign({}, state, {plans: action.payload})
            break;
        case "SET_SELECTED_PLAN":
            state = Object.assign({}, state, {selectedPlan: action.payload})
            break;
        case "ADD_STEP":
            let newKey = state.nextStep;
            let newStep = {
                key:newKey,
                order:1,
                hours:1,
                minutes:12,
                selectedProbe:4,
                temperature:300,
                selectedCriteria:3
            };
            let newActivePlanSteps = [...state.activePlanSteps, newStep];
            state = Object.assign({}, state, {nextStep:(newKey + 1), activePlanSteps: newActivePlanSteps});
            break;
        case "DELETE_STEP":
            let key = action.payload;
            let alteredArray = state.activePlanSteps.filter((item) => {
                if(key === item.key) return false;
                return true;
            })
            state = Object.assign({}, state, {activePlanSteps: alteredArray});
            break;
        default:
            state = state;
    }
    return state;

};

export default planReducer;