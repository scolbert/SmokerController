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
            console.log("Inside of Reducer.AddStep");
            let newStep = {
                key:1,
                order:1,
                hours:1,
                minutes:12,
                selectedProbe:4,
                temperature:300,
                selectedCriteria:3
            };
            let newActivePlanSteps = [...state.activePlanSteps, newStep];
            state = Object.assign({}, state, {activePlanSteps: newActivePlanSteps});
            break;
        default:
            state = state;
    }
    return state;

};

export default planReducer;