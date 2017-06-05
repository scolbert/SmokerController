const helper =  require('../helpers/ArrayHelper.js');

const TurnOffCriteriaReducer = (state, action ) => {
    if(state === undefined) {
        state = {temperature: 0, probes:[]};
    }

    let newProbeArray = [];
    let newState = Object.assign({}, state);

    switch(action.type) {
        case "TOGGLE_PROBE":
            if(helper.arrayContainsNumber(state.probes, action.payload)) {
                newProbeArray = helper.removeNumberFromArray(state.probes, action.payload);
            } else {
                newProbeArray = helper.addNumberToArray(state.probes, action.payload);
            }
             newState = Object.assign({}, state, {probes: newProbeArray});
            break;
        case "SET_TEMPERATURE":
            newState = Object.assign({}, state, {temperature: action.payload});
            break;
        default:
            newState = state;
    }
    return newState;
};

export default TurnOffCriteriaReducer;