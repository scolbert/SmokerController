import React from 'react';
import Selector from '../components/Selector';
import { connect } from 'react-redux';
import { getPlans, handlePlanSelection } from '../actions/planActions.js';
import { startSession, stopSession } from '../actions/sessionActions.js';
import MessageBox from '../components/MessageBox.js';

class StartSmokingPage extends React.Component {
    constructor(props) {
        super(props);

        this.state = {message:''};
    }

    componentWillMount() {
        this.props.getPlans();
    }

    render() {
        return (
            <div>
                <title>StartSmokingPage Page</title>
                <h1>Start Smoking Page</h1>
                <Selector plans={this.props.planState.plans} onMessageSelected={this.props.handlePlanSelection} /><br />
                <button onClick={this.props.startSession} >Start</button>
                <button onClick={this.props.stopSession}>Stop</button><br />
                <MessageBox message={this.state.message} />
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
            },
            handlePlanSelection: (e) => {
                console.log("inside of handlePlanSelection");
                dispatch( handlePlanSelection(e) );
            },
            startSession: () => {
                dispatch( startSession() );
            },
            stopSession: () => {
                dispatch( stopSession() );
            }
        }
    }


export default connect(mapStateToProps, mapDispatchToProps)(StartSmokingPage);
