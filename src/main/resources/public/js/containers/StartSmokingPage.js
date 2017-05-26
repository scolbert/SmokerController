import React from 'react';
import { createStore, combineReducers, applyMiddleware } from 'redux';
import PlanSelector from '../components/PlanSelector';
import logger from 'redux-logger';

const StartSmokingPage = (props) => (
    <div>
        <title>StartSmokingPage Page</title>
        <h1>Start Smoking Page</h1>
        <PlanSelector plans={this.props.plans} />
    </div>
)



const planReducer = (state = {plans:[{id:1, name:'Test Smoking Plan 1'}, {id:2, name:'Test Smoking Plan 2'}]},
                     action) => {
    switch(action.type) {
        // HTTP GET to /api/v1/TempTiming gets list of all temperature timing programs
        case "PLAN_GET":
            state = state;
            break;
            // post to /api/v1/TempTiming adds a temperature timing program
        case "PLAN_ADD":
            state.plans = state.plans.concat(action.payload);
            break;
    }
    return state;
};

const sessionReducer = (state = {
    // Start Session
      // description
      // id
      // meat
      // reference Thermometer
    // Stop Session
    // Get Current Session
    // getSessions
    session:{id:1, description:'Test Description', meat:'ribs', referenceThermometer: 1, }},
                     action) => {
    switch(action.type) {
        // HTTP POST to /api/v1/smoke_session
        case "START":
            state = state;
            break;
    }
    return state;
};

const store = createStore(combineReducers({planReducer, sessionReducer}), {}, applyMiddleware(logger));

store.subscribe(() => {
    // if(store.getState().plans[2] != undefined) console.log(store.getState().plans[2].name);
    // const state = store.getState();
    // console.log("Store updated! - " + store.getState().planReducer.plans[1].name);
    // console.log(store.getState().sessionReducer.session.description);
    // console.log("State is ", store.getState());
});

store.dispatch({
    type: "PLAN_GET"
});

store.dispatch({
    type: "PLAN_ADD",
    payload: {id:3, name:'Added Smoking Plan'}
})

// Delete Plan. HTTP DELETE to /api/v1/TempTiming/{id} deletes the plan
// Get a specific plan. HTTP GET to /api/v1/TempTiming/{id}
// Modify a specific plan. HTTP PUT to /api/v1/TempTiming/{id}

// create a turn off criteria (need to create the post for this)


export default StartSmokingPage;