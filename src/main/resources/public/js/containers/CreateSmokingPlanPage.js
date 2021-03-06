import React from 'react';
import Selector from '../components/Selector.js';
import { connect } from 'react-redux';
import CookingPlanStep from '../components/CookingPlanStep.js';
import { addStep, deleteStep, changeHours, changeMinutes, changeTemperature, changeAmbientProbe, changeSelectedCriteria, addPlan, addName, addDescription } from '../actions/planActions.js';
import { getTurnOffCriteria } from '../actions/TurnOffCriteriaActions.js';
import { createName } from '../helpers/TurnOffCriteriaNamer';
import {convertKelvinToFarenheit} from '../helpers/TemperatureConverter.js';

class CreateSmokingPlanPage extends React.Component {
    constructor(props){
        super(props);
        this.render = this.render.bind(this);
    }

    componentWillMount(){
        this.props.getTurnOffCriteria();
    }

    addNameToTurnOffCriteria() {
        let criteria = this.props.turnOffCriteriaList;
        return criteria.map((item) => {
            return Object.assign({}, item, {name:createName(convertKelvinToFarenheit(item.targetTemperature.tempK), item.probeList)});
        })
    }

    buildStepArray(){
        let testArray = [{name:"nameOne", id:3}, {name:"namethree", id:5}];
        let testFunction = (e) => {console.log("testFunction Called", e)};

        let source = this.props.planSteps;
        let destination = [];

        for(let x = 0; x < source.length; x++) {
            destination.push(
                <CookingPlanStep
                    key={source[x].key}
                    index={source[x].key}
                    order={source[x].order}
                    probeArray={this.props.probeList}
                    onProbeSelected={this.props.onProbeSelected}
                    criteriaList={this.addNameToTurnOffCriteria(this.props.turnOffCriteriaList)}
                    onCriteriaSelected={this.props.onCriteriaSelected}
                    onDelete={this.props.deleteStep}
                    onHoursChanged={this.props.onHoursChanged}
                    onMinutesChanged={this.props.onMinutesChanged}
                    onTemperatureChanged={this.props.onTemperatureChanged}
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
                <input type="textbox" onBlur={this.props.addName}></input><br />
                <label>Description </label>
                <input type="textBox" onBlur={this.props.addDescription}></input><br /><br />

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
                <button onClick={this.props.addPlan}>Submit</button>
            </div>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        planSteps: state.planState.activePlanSteps,
        probeList: state.planState.probeList,
        turnOffCriteriaList: state.criteriaState.turnOffCriteriaList
    }
}

const mapDispatchToProps = (dispatch) => {
    return {
        addStep: () => {
            dispatch(addStep());
        },
        deleteStep:(key) => {
            dispatch(deleteStep(key));
        },
        getTurnOffCriteria:() => {
            dispatch(getTurnOffCriteria());
        },
        onHoursChanged: (index, event) => {
            dispatch(changeHours(index, event.target.value));
        },
        onMinutesChanged: (index, event) => {
            dispatch(changeMinutes(index, event.target.value));
        },
        onTemperatureChanged: (index, event) => {
            dispatch(changeTemperature(index, event.target.value));
        },
        onProbeSelected: (index, event) => {
            dispatch(changeAmbientProbe(index, event.target.value));
        },
        onCriteriaSelected: (index, event) => {
            dispatch(changeSelectedCriteria(index, event.target.value));
        },
        addPlan: () => {
            dispatch(addPlan());
        },
        addName: (e) => {
            dispatch(addName(e.target.value));
        },
        addDescription: (e) => {
            dispatch(addDescription(e.target.value));
        }
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(CreateSmokingPlanPage);