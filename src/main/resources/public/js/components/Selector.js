// Data sent to this smoker component is required to have a name (which is displayed) and an id (which is the value returned)

import React from 'react';

const Selector = (props) => {
    const data = props.data;
    const test = (e) => {console.log("testing")}
    const plansAsOptions = data.map((item) => {
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

export default Selector;