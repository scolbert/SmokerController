const helper =  require('../helpers/ArrayHelper.js');

const TurnOffCriteriaReducer = (state, action ) => {
    // console.log("state passed into reducer is " , state);
    console.log("initial action.payload value is ", action.payload);
    if(state === undefined) {
        // console.log("Setting up initial state for TurnOffCritiera");
        state = {temperature: 0, probes:[]};
    }
    let newProbeArray = [];
    let newState = Object.assign({}, state);
    // console.log("before switch newProbeArray is " + newProbeArray);
    // console.log("before switch newState is " , newState);
    switch(action.type) {
        case "TOGGLE_PROBE":
            // console.log("before if state.probes is " + state.probes);
            if(helper.arrayContainsNumber(state.probes, action.payload)) {
                // console.log("in reducer where array contains number - turn off");
                // console.log("inside of if, state.probes is " + state.probes);
                newProbeArray = helper.removeNumberFromArray(state.probes, action.payload);
            } else {
                // console.log("in reducer where array doesnt contain number - turn on");
                // console.log("inside of else, state.probes is " + state.probes);
                console.log("before adding, state.probes is ", state.probes);
                console.log("before adding, action.payload is ", action.payload);
                console.log("result of helper is " + helper.addNumberToArray(state.probes, action.payload));
                newProbeArray = helper.addNumberToArray(state.probes, action.payload);
            }
            // newProbeArray = [1,2,3];
             newState = Object.assign({}, state, {probes: newProbeArray});
            break;
        default:
            // console.log("inside of default");
            newState = state;
    }
    return newState;
};

export default TurnOffCriteriaReducer;