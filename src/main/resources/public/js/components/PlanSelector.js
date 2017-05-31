import React from 'react';

const PlanSelector = (props) => {
    const plans = props.plans;
    const test = (e) => {console.log("testing")}
    const plansAsOptions = plans.map((item) => {
        return (
            <option key={item.id} value={item.id} >{item.name}</option>
        );
    })
    return(
        <div>
            <select onChange={props.onMessageSelected}>
                {plansAsOptions}
            </select>
        </div>
    )
}

export default PlanSelector;