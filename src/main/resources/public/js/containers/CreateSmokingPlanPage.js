import React from 'react';
import Selector from '../components/Selector.js';
import { connect } from 'react-redux';
import CookingPlanStep from '../components/CookingPlanStep.js';

class CreateSmokingPlanPage extends React.Component {
    constructor(props){
        super(props);
    }

    render() {

        let hourColumnWidth = 7;
        let probeColumnWidth = 6;
        let temperatureColumnWidth = 9;
        let criteriaColumnWidth = 8;
        let deleteColumnWidth = 6;
        let addColumnWidth = 4;
        let labelWidth = 9;

        let testArray = [{name:"nameOne", id:3}, {name:"nametwo", id:5}];
        let testFunction = (e) => {console.log("testFunction Called")};
        let cookingPlanStepArray = [
            <CookingPlanStep probeArray={testArray} onProbeSelected={testFunction} criteriaList={testArray} onCriteriaSelected={testFunction} />,
            <CookingPlanStep probeArray={testArray} onProbeSelected={testFunction} criteriaList={testArray} onCriteriaSelected={testFunction} />
        ];

        return (
            <div>
                <title>Create Smoking Plan Page</title>
                <h1>Create Smoking Plan Page</h1>

                <label>Name </label>
                <input type="textbox"></input><br />
                <label>Description </label>
                <input type="textBox"></input><br /><br />

                <div style={{border: '1px solid black'}}>
                    <table>
                        <thead>
                            <tr>
                                <th style={{width: hourColumnWidth + 'em'}}>Hours &<br />Minutes</th>
                                <th style={{width: probeColumnWidth + 'em'}}>Abient<br />Probe</th>
                                <th style={{width: temperatureColumnWidth + 'em'}}>Temperature</th>
                                <th style={{width: criteriaColumnWidth + 'em'}}>Turn Off<br />Criteria</th>
                                <th style={{width: deleteColumnWidth + 'em'}}>Delete</th>
                                <td style={{width: addColumnWidth + 'em'}}>
                                    <button>Add</button>
                                </td>
                            </tr>
                        </thead>
                        <tbody>
                        {cookingPlanStepArray}
                        </tbody>
                    </table>
                </div>
                <button>Submit</button>
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
        }
    }
}


export default connect(mapStateToProps, mapDispatchToProps)(CreateSmokingPlanPage);