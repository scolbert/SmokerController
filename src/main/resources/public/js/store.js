import { createStore, combineReducers, applyMiddleware } from 'redux';
import logger from 'redux-logger';

import planReducer from './reducers/PlanReducer.js';
import sessionReducer from './reducers/sessionReducer.js';

export default createStore(combineReducers({planState: planReducer, sessionState: sessionReducer}), {}, applyMiddleware(logger));