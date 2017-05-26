import React from 'react';

const PlanSelector = (props) => {
    const plans = props.plans;
    const plansAsOptions = plans.map((item) => {
        return (
            {key: item.key},
            <option value={item.key} > {item.name}</option>
        );
    })
    return(
        <div>
            <select>
                {plansAsOptions}
            </select>
        </div>
    )
}

export default PlanSelector;