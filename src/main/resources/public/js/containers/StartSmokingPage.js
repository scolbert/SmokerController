import React from 'react';
import PlanSelector from '../components/PlanSelector';
import { connect } from 'react-redux';
import { setPlans } from '../actions/planActions.js';

const StartSmokingPage = (props) => (
    <div>
        <title>StartSmokingPage Page</title>
        <h1>Start Smoking Page</h1>
        <PlanSelector plans={props.planState.plans} />
    </div>
)

const mapStateToProps = (state) => {
    return {
        planState: state.planState
    }
}

const mapDispatchToProps = (dispatch) => {
    return {
        getPlans: () => {
            dispatch( getPlans() );
        }
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(StartSmokingPage);
