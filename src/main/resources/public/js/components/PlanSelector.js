import React from 'react';

const PlanSelector = (props) => {
    const plans = this.props.plans;
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