import React from 'react';
import Selector from '../components/Selector.js';
import { connect } from 'react-redux';
import CookingPlanStep from '../components/CookingPlanStep.js';
import { addStep, deleteStep } from '../actions/planActions.js';
import { getTurnOffCriteria } from '../actions/TurnOffCriteriaActions.js';

class CreateSmokingPlanPage extends React.Component {
    constructor(props){
        super(props);
        this.render = this.render.bind(this);
    }

    componentWillMount(){
        console.log("inside of componentWillMount");
        this.props.getTurnOffCriteria();
    }

    buildStepArray(){
        let testArray = [{name:"nameOne", id:3}, {name:"namethree", id:5}];
        let testFunction = (e) => {console.log("testFunction Called")};

        let source = this.props.planSteps;
        let destination = [];

        for(let x = 0; x < source.length; x++) {
            destination.push(
                <CookingPlanStep
                    key={source[x].key}
                    index={source[x].key}
                    order={source[x].order}
                    probeArray={this.props.probeList}
                    onProbeSelected={testFunction}
                    criteriaList={testArray}
                    onCriteriaSelected={testFunction}
                    onDelete={this.props.deleteStep}
                />
            )
        }
        return destination;
    }



    render() {

        let hourColumnWidth = 4;
        let minuteColumnWidth = 4;
        let probeColumnWidth = 6;
        let temperatureColumnWidth = 9;
        let criteriaColumnWidth = 8;
        let deleteColumnWidth = 6;
        let addColumnWidth = 4;
        let labelWidth = 9;

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
                                <th style={{width: hourColumnWidth + 'em'}}>Hours</th>
                                <th style={{width: minuteColumnWidth + 'em'}}>Minutes</th>
                                <th style={{width: probeColumnWidth + 'em'}}>Abient<br />Probe</th>
                                <th style={{width: temperatureColumnWidth + 'em'}}>Temperature</th>
                                <th style={{width: criteriaColumnWidth + 'em'}}>Turn Off<br />Criteria</th>
                                <th style={{width: deleteColumnWidth + 'em'}}>Delete</th>
                                <td style={{width: addColumnWidth + 'em'}}>
                                    <button onClick={this.props.addStep}>Add</button>
                                </td>
                            </tr>
                        </thead>
                        <tbody>
                            {this.buildStepArray()}
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
        planSteps: state.planState.activePlanSteps,
        probeList: state.planState.probeList
    }
}

const mapDispatchToProps = (dispatch) => {
    return {
        addStep: () => {
            console.log("add clicked");
            dispatch(addStep());
        },
        deleteStep:(key) => {
            console.log("delete clicked");
            dispatch(deleteStep(key));
        },
        getTurnOffCriteria:() => {
            console.log("getting turn off criteria");
            dispatch(getTurnOffCriteria());
        }
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(CreateSmokingPlanPage);