import React, {Component} from 'react';
import {Menu, Dropdown, Avatar, Badge, Icon} from 'antd';
import {Link} from "react-router-dom";
const menu = (
    <Menu  >
        <Menu.Item key="0">
            <Icon type="setting" />
            Setting
        </Menu.Item>
        <Menu.Item key="Profile">
            <Icon type="profile" />
            Profile
        </Menu.Item>
        <Menu.Divider />
        <Menu.Item key="logout" ><Icon type="logout" />
        Logout</Menu.Item>
    </Menu>
);
class Profile extends Component {

    render() {
        return (
            <div className={"cfs_profile "+(this.props.className?this.props.className:"")}>
                <Dropdown overlay={menu} trigger={['click']} >
                    <Badge dot>
                    <Avatar shape="square" src="/img/male.png"/>
                    </Badge>
                </Dropdown>
            </div>
        );
    }
}


export default Profile;
