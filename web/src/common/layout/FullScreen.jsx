import React, {Component} from 'react';
import {hasClass} from "../utils/Dom";
import {Button} from "antd";

export default class FullScreen extends Component {

    handleToggle() {
        if (!hasClass(document.body, 'full-screen')) {
            document.body.classList.add("full-screen");
            if (document.documentElement.requestFullscreen) {
                document.documentElement.requestFullscreen();
            } else if (document.documentElement.mozRequestFullScreen) {
                document.documentElement.mozRequestFullScreen();
            } else if (document.documentElement.webkitRequestFullscreen) {
                document.documentElement.webkitRequestFullscreen();
            } else if (document.documentElement.msRequestFullscreen) {
                document.documentElement.msRequestFullscreen();
            }
        } else {
            document.body.classList.remove("full-screen");
            if (document.exitFullscreen) {
                document.exitFullscreen();
            } else if (document.mozCancelFullScreen) {
                document.mozCancelFullScreen();
            } else if (document.webkitExitFullscreen) {
                document.webkitExitFullscreen();
            }
        }
    }

    render() {
        return (
            <div className={this.props.className}>
                <Button htmlType="button" title="Full Screen" onClick={this.handleToggle}
                        icon={!hasClass(document.body, 'full-screen') ? "fullscreen" : "fullscreen-exit"}/>
            </div>
        );
    }
}
