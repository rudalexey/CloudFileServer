import axios from 'axios';
import {BASE_API_URL} from "../common/constants";

const USER_API_BASE_URL = BASE_API_URL+'/auth';
export const ACCESS_TOKEN = "AuthAccessToken";

class AuthService {

    login(credentials) {
        return axios.post(USER_API_BASE_URL + "/login", credentials);
    }

    getUserInfo() {
        if (!localStorage.getItem(ACCESS_TOKEN)) {
            return Promise.reject("No access token set.");
        }
        return axios.get(USER_API_BASE_URL + "/userInfo", this.getAuthHeader());
    }

    getAuthHeader() {
        return {headers: {Authorization: 'Bearer ' + localStorage.getItem(ACCESS_TOKEN)}};
    }
    getAuthToken() {
        return localStorage.getItem(ACCESS_TOKEN);
    }
    logOut() {
        localStorage.removeItem(ACCESS_TOKEN);
        return axios.post(USER_API_BASE_URL + '/logout', {}, this.getAuthHeader());
    }
}

export default new AuthService();
