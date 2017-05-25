import React from 'react';
import { Switch, Route } from 'react-router-dom';
import StartSmokingPage from '../containers/StartSmokingPage';
import CreateSmokingPlanPage from '../containers/CreateSmokingPlanPage';
import PreferencesPage from '../containers/PreferencesPage';

const Main = () => (
    <main>
        <Switch>
            <Route exact path="/" component={StartSmokingPage} />
            <Route path="/createSmokingPlan" component={CreateSmokingPlanPage} />
            <Route path="/preferences" component={PreferencesPage} />
        </Switch>
    </main>
)

export default Main;