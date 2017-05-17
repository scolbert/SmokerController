import React, { Component } from 'react';
import ReactDOM from 'react-dom';

export class TestComponent extends Component {
    render() {
        return (
            <div>
                <label>test label</label>
            </div>
        );
    }
}

ReactDOM.render(<TestComponent />, document.getElementById('root'));

