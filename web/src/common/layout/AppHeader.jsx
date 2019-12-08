import React, { Component } from 'react';
import {
    Link,
    withRouter
} from 'react-router-dom';
import './AppHeader.css';
import { Layout, Menu, Dropdown, Icon } from 'antd';
import PropTypes from 'prop-types'
import FullScreen from "./FullScreen";

const Header = Layout.Header;
class AppHeader extends Component {
    constructor(props) {
        super(props);
        this.handleMenuClick = this.handleMenuClick.bind(this);
    }

    handleMenuClick({ key }) {
        if(key === "logout") {
            this.props.onLogout();
        }
    }

    render() {
        let menuItems;
        if(this.props.currentUser) {
            menuItems = [
                <Menu.Item key="/">
                    <Link to="/">
                        <Icon type="home" className="nav-icon" />
                    </Link>
                </Menu.Item>,
                <Menu.Item key="/home" className="profile-menu">
                    <ProfileDropdownMenu
                        currentUser={this.props.currentUser}
                        handleMenuClick={this.handleMenuClick}/>
                </Menu.Item>
            ];
        } else {
            menuItems = [
                <Menu.Item key="/login">
                    <Link to="/login">Login</Link>
                </Menu.Item>,
            ];
        }

        return (
            <Header id='header' className="app-header">
                    <div className="app-title" >
                        <span>Cloud File Server</span>
                    </div>


                <div className="pull-right">
                    <div className="app-menu">
                        <Menu
                        mode="horizontal"
                        selectedKeys={[this.props.location.pathname]}
                    >
                        {menuItems}
                    </Menu>
                    </div>
                    <FullScreen className="pull-right" />
                </div>
            </Header>
        );
    }
}

function ProfileDropdownMenu(props) {
    const dropdownMenu = (
        <Menu onClick={props.handleMenuClick} className="profile-dropdown-menu">
            <Menu.Item key="user-info" className="dropdown-item" disabled>
                <div className="user-full-name-info">
                    {props.currentUser.login}
                </div>
                <div className="username-info">
                    @{props.currentUser.firstName} {props.currentUser.lastName}
                </div>
            </Menu.Item>
            <Menu.Divider />
            <Menu.Item key="profile" className="dropdown-item">
                <Link to={`/users/${props.currentUser.login}`}>Profile</Link>
            </Menu.Item>
            <Menu.Item key="logout" className="dropdown-item">
                Logout
            </Menu.Item>
        </Menu>
    );

    return (
        <Dropdown
            overlay={dropdownMenu}
            trigger={['click']}
            getPopupContainer = { () => document.getElementsByClassName('profile-menu')[0]}>
            <a className="ant-dropdown-link" href={"#detail"}>
                <Icon type="user" className="nav-icon" style={{marginRight: 0}} /> <Icon type="down" />
            </a>
        </Dropdown>
    );
}


export default withRouter(AppHeader);

AppHeader.propTypes = {
    currentUser: PropTypes.shape({
        login: PropTypes.string,
        firstName: PropTypes.string,
        lastName: PropTypes.string,
        langKey: PropTypes.string,
        email:PropTypes.string,
        activated: PropTypes.bool,
        createdBy: PropTypes.string,
        createdDate: PropTypes.instanceOf(Date),
        imageUrl:PropTypes.string,
        lastModifiedBy: PropTypes.string,
        lastModifiedDate: PropTypes.instanceOf(Date),
        authorities:PropTypes.arrayOf(PropTypes.string)
    }),
};
ProfileDropdownMenu.propTypes = {
    handleMenuClick: PropTypes.func,
};
