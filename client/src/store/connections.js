import axios from 'axios'


export const newAxios = (basepath, access_token) => {
  const a = axios.create({
    baseURL: basepath
  })
  a.defaults.headers.common['Content-Type'] = 'application/json; charset=UTF-8'
  if (access_token !== undefined) {
    a.defaults.headers.common['access-token'] = access_token
  }
  return a
}

export const newAuth = () => {
  return newAxios(process.env.VUE_APP_AUTH_BASE_URL)
}

export const newAPI = (access_token) => {
  return newAxios(process.env.VUE_APP_API_BASE_URL, access_token)
}
