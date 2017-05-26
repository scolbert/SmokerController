import React from 'react';
import ReactDOM from 'react-dom';
import App from './components/App';
import { BrowserRouter } from 'react-router-dom';
import {Provider} from 'react-redux';
import store from './store.js';




// Delete Plan. HTTP DELETE to /api/v1/TempTiming/{id} deletes the plan
// Get a specific plan. HTTP GET to /api/v1/TempTiming/{id}
// Modify a specific plan. HTTP PUT to /api/v1/TempTiming/{id}

// create a turn off criteria (need to create the post for this)

ReactDOM.render((
    <Provider store={store}>
        <BrowserRouter>
            <App />
        </BrowserRouter>
    </Provider>
    ), document.getElementById('root'));

