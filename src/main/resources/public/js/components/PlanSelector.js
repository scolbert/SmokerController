import React from 'react';

const PlanSelector = (props) => {
    const plans = [{key:1, name:'Test Smoking Plan 1'}, {key:2, name:'Test Smoking Plan 2'}];
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