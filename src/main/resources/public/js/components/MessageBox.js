import React from 'react';

const MessageBox = function(props) {
    return(
        <div style={{border: '1px solid black'}}>
            <label>{props.message}</label>
        </div>
    )
}

export default MessageBox;