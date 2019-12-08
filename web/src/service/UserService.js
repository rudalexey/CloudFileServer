import axios from 'axios';
import {BASE_API_URL} from "../common/constants";

class UserService {
    fetchUsers() {
        return axios.get(BASE_API_URL+'/users');
    }

    fetchUserById(userId) {
        return axios.get(BASE_API_URL+'/users/' + userId);
    }

    deleteUser(userId) {
        return axios.delete(BASE_API_URL+'/users/' + userId);
    }

    addUser(user) {
        return axios.post(BASE_API_URL+'/users', user);
    }

    editUser(user) {
        return axios.put(BASE_API_URL+'/users/' + user.id, user);
    }

}

export default new UserService();
