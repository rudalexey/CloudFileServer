import React, { Component } from 'react';
import './NotFoundPage.css';
import { Link } from 'react-router-dom';
import { Button } from 'antd';

class NotFoundPage extends Component {
    render() {
        return (
            <div className="not-found-page">
                <h1 className="title">
                    404
                </h1>
                <div className="desc">
                    The Page you're looking for was not found.
                </div>
                <Link to="/"><Button className="go-back-btn" type="primary" size="large">Go Back</Button></Link>
            </div>
        );
    }
}
export default NotFoundPage;