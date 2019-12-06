import axios from 'axios';

const USER_API_BASE_URL = 'http://localhost:8080/api/auth/';
export const ACCESS_TOKEN = "AuthAccessToken";

class AuthService {

    login(credentials) {
        return axios.post(USER_API_BASE_URL + "login", credentials);
    }

    getUserInfo() {
        if (!localStorage.getItem(ACCESS_TOKEN)) {
            return Promise.reject("No access token set.");
        }
        return axios.get(USER_API_BASE_URL + "userInfo", this.getAuthHeader());
    }

    getAuthHeader() {
        return {headers: {Authorization: 'Bearer ' + localStorage.getItem(ACCESS_TOKEN)}};
    }

    logOut() {
        localStorage.removeItem(ACCESS_TOKEN);
        return axios.post(USER_API_BASE_URL + 'logout', {}, this.getAuthHeader());
    }
}

export default new AuthService();