import React, {Component} from 'react';
import './App.css';
import {Route, Switch, withRouter} from 'react-router-dom';
import Loading from './common/Loading';
import PrivateRoute from './common/PrivateRoute';

import {Layout, notification} from 'antd';
import AppHeader from "./common/layout/AppHeader";
import NotFoundPage from './common/layout/NotFoundPage';
import auth, {ACCESS_TOKEN} from "./service/AuthService";
import HomePage from "./component/HomePage";
import LoginPage from "./component/auth/LoginPage";

const {Content} = Layout;

class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            currentUser: null,
            isAuthenticated: false,
            isLoading: false
        };
        this.handleLogout = this.handleLogout.bind(this);
        this.loadCurrentUser = this.loadCurrentUser.bind(this);
        this.handleLogin = this.handleLogin.bind(this);

        notification.config({
            placement: 'topRight',
            top: 70,
            duration: 3,
        });
    }

    loadCurrentUser() {
        this.setState({
            isLoading: true
        });
        auth.getUserInfo().then((rs) => {
            this.setState({
                currentUser: rs.data,
                isAuthenticated: true,
                isLoading: false
            });
        }).catch(error => {
            this.setState({
                isLoading: false
            });
        });
    }

    componentDidMount() {
        this.loadCurrentUser();
    }

    handleLogout(redirectTo = "/", notificationType = "success", description = "You're successfully logged out.") {
        localStorage.removeItem(ACCESS_TOKEN);

        this.setState({
            currentUser: null,
            isAuthenticated: false
        });

        this.props.history.push(redirectTo);

        notification[notificationType]({
            message: 'Cloud File Server',
            description: description,
        });
    }

    handleLogin() {
        notification.success({
            message: 'Cloud File Server',
            description: "You're successfully logged in.",
        });
        this.loadCurrentUser();
        this.props.history.push("/");
    }

    render() {
        if (this.state.isLoading) {
            return <Loading/>
        }
        return (
            <Layout className="app-container">
                <AppHeader isAuthenticated={this.state.isAuthenticated}
                           currentUser={this.state.currentUser}
                           onLogout={this.handleLogout}/>

                <Content className="app-content">
                    <div className="container">
                        <Switch>
                            <Route path="/login"
                                   render={(props) => <LoginPage onLogin={this.handleLogin} {...props} />}/>
                            {<PrivateRoute authenticated={this.state.isAuthenticated} path="/"
                                           component={HomePage} handleLogout={this.handleLogout}/>}
                            <Route component={NotFoundPage}/>
                        </Switch>
                    </div>
                </Content>
            </Layout>
        );
    }
}

export default withRouter(App);