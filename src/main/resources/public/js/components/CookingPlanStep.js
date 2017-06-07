import React from 'react';
import Selector from './Selector.js';

const CookingPlanStep = (props) =>{
    return (
        <tr>
            <td>
                <input type="textBox" id="hoursBox" onBlur={(e) => props.onHoursChanged(props.index, e)} />
            </td>
            <td>
                <input type="textBox" id="minutesBox" onBlur={(e) => props.onMinutesChanged(props.index, e)}/>
            </td>
            <td>
                <Selector id="probeSelector" data={props.probeArray} onMessageSelected={(e) => props.onProbeSelected(props.index, e)}></Selector>
            </td>
            <td>
                <input type="textBox" id="temperatureBox" onBlur={(e) => props.onTemperatureChanged(props.index, e)}/>
            </td>
            <td>
                <Selector id="criteriaSelector" data={props.criteriaList} onMessageSelected={(e) => props.onCriteriaSelected(props.index, e)}></Selector>
            </td>
            <td>
                <button onClick={() => props.onDelete(props.index)}>X</button>
            </td>
        </tr>
    )
}


export default CookingPlanStep;