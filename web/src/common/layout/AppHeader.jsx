import React, {Component} from 'react';
import {Link, withRouter} from 'react-router-dom';
import './AppHeader.css';
import {Col, Dropdown, Icon, Input, Layout, Menu, Row} from 'antd';
import PropTypes from 'prop-types'
import FullScreen from "./FullScreen";
import Profile from "./Profile";

const {Search} = Input;
const Header = Layout.Header;

class AppHeader extends Component {
    constructor(props) {
        super(props);
        this.handleMenuClick = this.handleMenuClick.bind(this);
    }

    handleMenuClick({key}) {
        if (key === "logout") {
            this.props.onLogout();
        }
    }

    render() {
        let menuItems;
        if (this.props.currentUser) {
            menuItems = [
                <Menu.Item key="/">
                    <Link to="/">
                        <Icon type="home" className="nav-icon"/>
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
            <Header className="app-header">
                <Row type="flex" justify="space-between">
                    <Col className="app-title">
                        <span>Cloud File Server</span>
                    </Col>

                    <Col>
                        {/*     <div className="app-menu">
                    <Menu
                        mode="horizontal"
                        selectedKeys={[this.props.location.pathname]}
                    >
                        {menuItems}
                    </Menu>
                </div>*/}
                    </Col>
                    <Row gutter={[6,6]} type="flex" justify="end" align="middle">
                        <Col>
                            <FullScreen/>
                        </Col>
                        <Col>
                            <Search className="header-search" placeholder="input search text" onSearch={value => console.log(value)}/>
                        </Col>
                        <Col>
                            <Profile/>
                        </Col>
                    </Row>
                </Row>
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
            <Menu.Divider/>
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
            getPopupContainer={() => document.getElementsByClassName('profile-menu')[0]}>
            <a className="ant-dropdown-link" href={"#detail"}>
                <Icon type="user" className="nav-icon" style={{marginRight: 0}}/> <Icon type="down"/>
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
        email: PropTypes.string,
        activated: PropTypes.bool,
        createdBy: PropTypes.string,
        createdDate: PropTypes.instanceOf(Date),
        imageUrl: PropTypes.string,
        lastModifiedBy: PropTypes.string,
        lastModifiedDate: PropTypes.instanceOf(Date),
        authorities: PropTypes.arrayOf(PropTypes.string)
    }),
};
ProfileDropdownMenu.propTypes = {
    handleMenuClick: PropTypes.func,
};
