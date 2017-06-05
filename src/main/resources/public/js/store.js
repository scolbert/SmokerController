import { createStore, combineReducers, applyMiddleware } from 'redux';
import logger from 'redux-logger';
import thunk from 'redux-thunk';

import planReducer from './reducers/PlanReducer.js';
import sessionReducer from './reducers/sessionReducer.js';
import criteriaReducer from './reducers/TurnOffCriteriaReducer';

export default createStore(
    combineReducers({planState: planReducer, sessionState: sessionReducer, criteriaState: criteriaReducer }),
    {},
    applyMiddleware(logger, thunk));