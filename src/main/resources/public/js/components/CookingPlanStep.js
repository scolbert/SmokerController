import React from 'react';
import Selector from './Selector.js';

const CookingPlanStep = (props) =>{
    return (
        <tr>
            <td>
                <input type="textBox" id="timeBox"/>
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
                <button>X</button>
            </td>
        </tr>
    )
}


export default CookingPlanStep;