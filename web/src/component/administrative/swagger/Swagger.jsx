import React, {Component} from 'react';
import SwaggerUI from "swagger-ui-react"
import "swagger-ui-react/swagger-ui.css"
import AuthService from "../../../service/AuthService";
//
class DocApi extends Component {


    render() {
        return (
             <SwaggerUI url="/api/v2/api-docs" requestInterceptor={this.requestInterceptor}/>
        )
    }
    requestInterceptor(request) {
        console.log(request);
        request.headers.Authorization = "Bearer " + AuthService.getAuthToken();
        return request;
    }
}

export default DocApi;
