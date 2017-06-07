const planReducer = (state = {
                        plans:[{id:1, name:'Test Smoking Plan 1'}, {id:2, name:'Test Smoking Plan 3'}],
                        selectedPlan: '1',
                        nextStep: 1,
                        nextOrder: 1,
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
                        ],
                        probeList:[
                            {id: 1, name:"Probe 1"},
                            {id: 2, name:"Probe 2"},
                            {id: 3, name:"Probe 3"},
                            {id: 4, name:"Probe 4"}
                        ]
                     },
                     action) => {

    switch(action.type) {
        case "GET_PLANS":
            state = Object.assign({}, state, {plans: action.payload})
            break;
        case "SET_SELECTED_PLAN":
            state = Object.assign({}, state, {selectedPlan: action.payload})
            break;
        case "ADD_STEP":
            let newKey = state.nextStep;
            let newOrder = state.nextOrder;
            let newStep = {
                key:newKey,
                order:newOrder,
                hours:0,
                minutes:0,
                selectedProbe:1,
                temperature:0,
                selectedCriteria:0
            };
            let newActivePlanSteps = [...state.activePlanSteps, newStep];
            state = Object.assign({}, state, {nextStep:(newKey + 1), nextOrder:(newOrder +1), activePlanSteps: newActivePlanSteps});
            break;
        case "DELETE_STEP":
            let key = action.payload;
            let alteredArray = state.activePlanSteps.filter((item) => {
                if(key === item.key) return false;
                return true;
            })
            state = Object.assign({}, state, {activePlanSteps: alteredArray});
            break;
        case "UPDATE_HOURS":
            let activePlanStepsModifiedHours = state.activePlanSteps.map((item) => {
                if(item.key === action.payload.index){
                    return Object.assign({}, item, {hours:action.payload.newHours})
                } else {
                    return item;
                }
            })
            state = Object.assign({}, state, {activePlanSteps: activePlanStepsModifiedHours});
            break;
        case "UPDATE_MINUTES":
            let activePlanStepsModifiedMinutes = state.activePlanSteps.map((item) => {
                if(item.key === action.payload.index){
                    return Object.assign({}, item, {minutes:action.payload.newMinutes})
                } else {
                    return item;
                }
            })
            state = Object.assign({}, state, {activePlanSteps: activePlanStepsModifiedMinutes});
            break;
        case "UPDATE_TEMPERATURE":
            let activePlanStepsModifiedTemperature = state.activePlanSteps.map((item) => {
                if(item.key === action.payload.index){
                    return Object.assign({}, item, {temperature:action.payload.newTemperature})
                } else {
                    return item;
                }
            })
            state = Object.assign({}, state, {activePlanSteps: activePlanStepsModifiedTemperature});
            break;
        case "CHANGE_SELECTED_PROBE":
            console.log("in reducer Probe returned from action is " + action.payload.newSelectedProbe);
            let activePlanStepsModifiedSelectedProbe = state.activePlanSteps.map((item) => {
                if(item.key === action.payload.index){
                    return Object.assign({}, item, {selectedProbe:action.payload.newSelectedProbe})
                } else {
                    return item;
                }
            })
            state = Object.assign({}, state, {activePlanSteps: activePlanStepsModifiedSelectedProbe});
            break;
        case "CHANGE_SELECTED_CRITERIA":
            let activePlanStepsModifiedSelectedCriteria = state.activePlanSteps.map((item) => {
                if(item.key === action.payload.index){
                    return Object.assign({}, item, {selectedCriteria:action.payload.newSelectedCriteria})
                } else {
                    return item;
                }
            })
            state = Object.assign({}, state, {activePlanSteps: activePlanStepsModifiedSelectedCriteria});
            break;
        default:
            state = state;
    }
    return state;

};

export default planReducer;