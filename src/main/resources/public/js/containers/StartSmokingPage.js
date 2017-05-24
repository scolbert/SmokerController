import React from 'react';
import { createStore } from 'redux';
import PlanSelector from '../components/PlanSelector';

const StartSmokingPage = (props) => {
    return(
        <div>
            <title>StartSmokingPage Page</title>
            <h1>Start Smoking Page</h1>
            <PlanSelector />
        </div>
    )
}
//
// function SmokingPlanReducer(state = {plans:[{id:1, name:'Test Smoking Plan 1'}, {id:2, name:'Test Smoking Plan 2'}]}, action) {
//     return state;
// }
//
// const store = createStore(SmokingPlanReducer);
//
// export default StartSmokingPage;