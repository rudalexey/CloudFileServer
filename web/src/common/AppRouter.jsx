import React from "react";
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom'
import LoginComponent from "../component/auth/LoginComponent";

const AppRouter = () => {
    return (
        <Router>
            <Switch>
                <Route path="/" exact component={LoginComponent}/>
            </Switch>
        </Router>
    )
};

export default AppRouter;