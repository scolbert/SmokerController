import React from 'react';
import Selector from '../components/Selector';
import { connect } from 'react-redux';
import { getPlans, handlePlanSelection } from '../actions/planActions.js';
import { startSession, stopSession } from '../actions/sessionActions.js';
import MessageBox from '../components/MessageBox.js';

const CreateTurnOffCriteriaPage = (props) => {
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
                <input type='textBox' />
                <br />
                <br />
                <fieldset>
                    <legend>Probes to Include</legend>
                    <label>Probe 1 </label><input type="checkbox" name="Probe1" /><br />
                    <label>Probe 2 </label><input type="checkbox" name="Probe2" /><br />
                    <label>Probe 3 </label><input type="checkbox" name="Probe3" /><br />
                    <label>Probe 4 </label><input type="checkbox" name="Probe4" /><br />
                </fieldset>
            </div>
            <br />
            <button>Submit</button>
        </div>
    )
}



// const mapStateToProps = (state) => {
//     return {
//         criteriaState: state.criteriaState
//     }
// }
//
// const mapDispatchToProps = (dispatch) => {
//     return {
//         getCriteria: () => {
//             dispatch( getCriteria() );
//         }
//     }
// }
//
//
// export default connect(mapStateToProps, mapDispatchToProps)(CreateTurnOffCriteriaPage);

export default CreateTurnOffCriteriaPage;