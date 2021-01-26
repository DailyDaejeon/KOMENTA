import { loginUser } from '@/api/auth.js'
import jwtDecode from 'jwt-decode'

const state = {
    adminAuth: 0,
    token: sessionStorage.getItem('token'),
    isLogin: sessionStorage.getItem('token') === null ? false : true,
    isLoginError: false,
    userInfo: sessionStorage.getItem('token') === null ? {} : jwtDecode(sessionStorage.getItem('token')),
    isPasswordConfirmed: false,
};

const mutations = {
    setToken(state, token) {
        state.token = token
        sessionStorage.setItem('token', token)
        state.isLogin = true
        state.isLoginError = false
        state.userInfo = jwtDecode(token)
    },
    logout(state) {
        state.token = ''
        state.isLogin = false
        state.isLoginError = false
        sessionStorage.clear()
    },
    loginError(state) {
        state.isLoginError = true
    },
    confirmComplete(state) {
        state.isPasswordConfirmed = true
    },
    enteredAccount(state) {
        state.isPasswordConfirmed = false
    },
}

const actions = {
    async LOGIN({ commit }, userData) {
        const response = await loginUser(userData)
        if (response.headers['access-token']) {
            commit('setToken', response.headers['access-token'])
        } else {
            commit('loginError')
        }
        return response
    },
    async PASSWORDCONFIRM({ commit }, userData) {
        const result = await loginUser(userData)
        if (result.headers['access-token']) {
            commit('confirmComplete')
            commit('setToken', result.headers['access-token'])
        } else {
            commit('loginError')
        }
        return result
    }

};

export default {
    state,
    mutations,
    actions
}
