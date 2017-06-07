const helper =  require('../helpers/ArrayHelper.js');
const tempConverter = require('../helpers/TemperatureConverter.js');

const TurnOffCriteriaReducer = (state, action ) => {
    if(state === undefined) {
        state = {temperature: 0, probes:[], turnOffCriteriaList:[]};
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
            newState = Object.assign({}, state, {temperature: tempConverter.convertFarenheitToKelvin(action.payload)});
            break;
        case "SUCCESSFUL_SUBMIT":
            newState = Object.assign({}, state, {probes:[], temperature: 0});
            break;
        case "GET_TURN_OFF_CRITERIA":
            console.log("Inside reducer.GET_TURN_OFF_CRITERIA that list is ", action.payload)
            newState = Object.assign({}, state, {turnOffCriteriaList:action.payload})
            break;
        default:
            newState = state;

    }
    return newState;
};

export default TurnOffCriteriaReducer;