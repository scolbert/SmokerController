import React from 'react';
import PlanSelector from '../components/PlanSelector';
import { connect } from 'react-redux';
import { getPlans } from '../actions/planActions.js';

class StartSmokingPage extends React.Component {
    constructor(props) {
        super(props);


    }

    componentWillMount() {
        this.props.getPlans();
    }

    render() {
        return (
            <div>
                <title>StartSmokingPage Page</title>
                <h1>Start Smoking Page</h1>
                <PlanSelector plans={this.props.planState.plans}/><br />
                <button>Start</button><button>Stop</button>
                <div style={{border: '1px solid black'}}>
                    Message Goes Here
                </div>
            </div>
        )
    }
}


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
