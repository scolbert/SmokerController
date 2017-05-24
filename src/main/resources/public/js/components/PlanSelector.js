import React from 'react';

const PlanSelector = (props) => {
    const plans = [{id:1, name:'Test Smoking Plan 1'}, {id:2, name:'Test Smoking Plan 2'}];
    const plansAsOptions = plans.map((item) => {
        return (
            <option value={item.id} > {item.name}</option>
        );
    })
    return(
        <div>
            {plansAsOptions}
        </div>
    )
}

export default PlanSelector;