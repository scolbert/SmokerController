import React from 'react';
import Selector from '../components/Selector';
import { connect } from 'react-redux';
import { getPlans, handlePlanSelection } from '../actions/planActions.js';
import { startSession, stopSession } from '../actions/sessionActions.js';
import { toggleProbe, setTemperature, submit } from '../actions/TurnOffCriteriaActions.js';
import MessageBox from '../components/MessageBox.js';

class CreateTurnOffCriteriaPage extends React.Component {
    constructor(props){
        super(props);
    }

    render(){
        let criteriaList = [{name:'Test Critiera', id:1}, {name:'second test criteria', id:2}];
        criteriaList = [{name:'Add New Criteria', id:0}, ...criteriaList];
        return (
            <div>
                <title>Create Turn Off Criteria Page</title>
                <h1>Create Turn Off Criteria Page</h1>
                <br/>
                <div style={{border: '1px solid black'}}>
                    <h3>Turn Off Criteria</h3>
                    <label>Temperature  </label>
                    <input type='textBox' onBlur={this.props.setTemperature} />
                    <br />
                    <br />
                    <fieldset>
                        <legend>Probes to Include</legend>
                        <label name="probe1" >Probe 1 </label><input type="checkbox" value="1" onChange={this.props.toggleProbe} name="Probe1" /><br />
                        <label name="probe2" >Probe 2 </label><input type="checkbox" value="2" onChange={this.props.toggleProbe} name="Probe2" /><br />
                        <label name="probe3" >Probe 3 </label><input type="checkbox" value="3" onChange={this.props.toggleProbe} name="Probe3" /><br />
                        <label name="probe4" >Probe 4 </label><input type="checkbox" value="4" onChange={this.props.toggleProbe} name="Probe4" /><br />
                    </fieldset>
                </div>
                <br />
                <button onClick={this.props.submit}>Submit</button>
            </div>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        criteriaState: state.criteriaState
    }
}

const mapDispatchToProps = (dispatch) => {
    return {
        toggleProbe: (e) => {
            let probeNumber = e.target.value;
            dispatch( toggleProbe(probeNumber) );
        },
        setTemperature: (e) => {
            let temperature = e.target.value;
            dispatch( setTemperature(temperature));
        },
        submit: (e) => {
            dispatch( submit() );
        }
    }
}


export default connect(mapStateToProps, mapDispatchToProps)(CreateTurnOffCriteriaPage);
