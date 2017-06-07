import React from 'react';
import Selector from './Selector.js';

const CookingPlanStep = (props) =>{
    return (
        <tr>
            <td>
                <input type="textBox" id="hoursBox" onBlur={(e) => props.onHoursChanged(props.index, e)} />
            </td>
            <td>
                <input type="textBox" id="minutesBox"/>
            </td>
            <td>
                <Selector id="probeSelector" data={props.probeArray} onMessageSelected={props.onProbeSelected}></Selector>
            </td>
            <td>
                <input type="textBox" id="temperatureBox"/>
            </td>
            <td>
                <Selector id="criteriaSelector" data={props.criteriaList} onMessageSelected={props.onCriteriaSelected}></Selector>
            </td>
            <td>
                <button onClick={() => props.onDelete(props.index)}>X</button>
            </td>
        </tr>
    )
}


export default CookingPlanStep;