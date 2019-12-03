import unittest
import requests as r
import time
import json


root_url = 'http://127.0.0.1:8000'


def gen_uniq(s):
    return f'{s}_{time.time()}'


class TestCreateUser(unittest.TestCase):

    def test_correct_post_v1_users_should_return_status_201(self):
        url = root_url + '/v1/users'
        headers = {}
        body = {
            'username': gen_uniq('user'),
            'password': 'password'
        }
        res = r.post(url=url, data=body, headers=headers)
        self.assertEqual(res.status_code, 201)

    def test_no_username_post_v1_users_should_return_status_400(self):
        url = root_url + '/v1/users'
        headers = {}
        body = {
            'password': 'password'
        }
        res = r.post(url=url, data=body, headers=headers)
        self.assertEqual(res.status_code, 400)

    def test_no_password_post_v1_users_should_return_status_400(self):
        url = root_url + '/v1/users'
        headers = {}
        body = {
            'username': gen_uniq('user')
        }
        res = r.post(url=url, data=body, headers=headers)
        self.assertEqual(res.status_code, 400)

    def test_empty_body_post_v1_users_should_return_status_400(self):
        url = root_url + '/v1/users'
        headers = {}
        body = {}
        res = r.post(url=url, data=body, headers=headers)
        self.assertEqual(res.status_code, 400)


class TestCorrectUserLogin(unittest.TestCase):

    def setUp(self):
        url = root_url + '/v1/users'
        headers = {}
        body = {
            'username': gen_uniq('user'),
            'password': 'password'
        }
        r.post(url=url, data=body, headers=headers)

        login_url = root_url + '/v1/login'
        headers = {}
        self.res = r.post(url=login_url, data=body, headers=headers)
        self.res_body = self.res.json()

    def test_correct_post_v1_login_should_return_status_200(self):
        self.assertEqual(self.res.status_code, 200)

    def test_correct_post_v1_login_should_return_access_token(self):
        self.assertTrue('access_token' in self.res_body)
        self.assertTrue(self.res_body['access_token'] is not None)
        self.assertTrue(self.res_body['access_token'] != '')

    def test_correct_post_v1_login_should_return_refresh_token(self):
        self.assertTrue('refresh_token' in self.res_body)
        self.assertTrue(self.res_body['refresh_token'] is not None)
        self.assertTrue(self.res_body['refresh_token'] != '')


class TestIncorrectUserLogin(unittest.TestCase):

    def setUp(self):
        url = root_url + '/v1/users'
        headers = {}
        self.body = {
            'username': gen_uniq('user'),
            'password': 'password'
        }
        r.post(url=url, data=self.body, headers=headers)

    def test_no_username_post_v1_login_should_return_status_400(self):
        login_url = root_url + '/v1/login'
        headers = {}
        body = {
            'password': 'password'
        }
        res = r.post(url=login_url, data=body, headers=headers)
        self.assertEqual(res.status_code, 400)

    def test_no_password_post_v1_login_should_return_status_400(self):
        login_url = root_url + '/v1/login'
        headers = {}
        body = self.body.copy()
        body.pop('password')
        res = r.post(url=login_url, data=body, headers=headers)
        self.assertEqual(res.status_code, 400)

    def test_empty_body_post_v1_login_should_return_status_400(self):
        login_url = root_url + '/v1/login'
        headers = {}
        body = {}
        res = r.post(url=login_url, data=body, headers=headers)
        self.assertEqual(res.status_code, 400)

    def test_incorrect_username_post_v1_login_should_return_status_401(self):
        login_url = root_url + '/v1/login'
        headers = {}
        body = self.body.copy()
        body['username'] = 'incorrect_user'
        res = r.post(url=login_url, data=body, headers=headers)
        self.assertEqual(res.status_code, 401)

    def test_incorrect_password_post_v1_login_should_return_status_401(self):
        login_url = root_url + '/v1/login'
        headers = {}
        body = self.body.copy()
        body['password'] = 'bad_pass'
        res = r.post(url=login_url, data=body, headers=headers)
        self.assertEqual(res.status_code, 401)

    def test_incorrect_username_and_password_post_v1_login_should_return_status_401(self):
        login_url = root_url + '/v1/login'
        headers = {}
        body = {
            'username': gen_uniq('bad_user'),
            'password': 'bad_password'
        }
        res = r.post(url=login_url, data=body, headers=headers)
        self.assertEqual(res.status_code, 401)


class TestRefreshToken(unittest.TestCase):

    def setUp(self):
        url = root_url + '/v1/users'
        headers = {}
        body = {
            'username': gen_uniq('user'),
            'password': 'password'
        }
        r.post(url=url, data=body, headers=headers)

        login_url = root_url + '/v1/login'
        headers = {}
        self.res = r.post(url=login_url, data=body, headers=headers)
        self.res_body = self.res.json()

    def test_correct_post_v1_token_should_return_status_200(self):
        url = root_url + '/v1/token'
        headers = {}
        body = {
            'refresh_token': self.res_body['refresh_token']
        }
        res = r.post(url=url, data=body, headers=headers)
        self.assertEqual(res.status_code, 200)

    def test_correct_post_v1_token_should_return_access_token(self):
        url = root_url + '/v1/token'
        headers = {}
        body = {
            'refresh_token': self.res_body['refresh_token']
        }
        res = r.post(url=url, data=body, headers=headers)
        res_body = res.json()
        self.assertTrue('access_token' in res_body)
        self.assertTrue(res_body['refresh_token'] is not None)
        self.assertTrue(res_body['refresh_token'] != '')

    def test_access_token_post_v1_token_should_return_status_400(self):
        url = root_url + '/v1/token'
        headers = {}
        body = {
            'access_token': self.res_body['access_token']
        }
        res = r.post(url=url, data=body, headers=headers)
        self.assertEqual(res.status_code, 400)

    def test_access_token_as_refresh_token_post_v1_token_should_return_status_401(self):
        url = root_url + '/v1/token'
        headers = {}
        body = {
            'refresh_token': self.res_body['access_token']
        }
        res = r.post(url=url, data=body, headers=headers)
        self.assertEqual(res.status_code, 400)


class TestDeleteRefreshToken(unittest.TestCase):

    def setUp(self):
        url = root_url + '/v1/users'
        headers = {}
        body = {
            'username': gen_uniq('user'),
            'password': 'password'
        }
        r.post(url=url, data=body, headers=headers)

        login_url = root_url + '/v1/login'
        headers = {}
        self.res = r.post(url=login_url, data=body, headers=headers)
        self.res_body = self.res.json()

        url = root_url + '/v1/token'
        headers = {}
        body = {
            'refresh_token': self.res_body['refresh_token']
        }
        self.res_delete = r.delete(url=url, data=body, headers=headers)

    def test_correct_delete_v1_token_should_return_status_200(self):
        self.assertEqual(self.res_delete.status_code, 200)

    def test_deleted_refresh_token_post_should_return_status_401(self):
        url = root_url + '/v1/token'
        headers = {}
        body = {
            'refresh_token': self.res_body['refresh_token']
        }
        res = r.post(url=url, data=body, headers=headers)
        self.assertEqual(res.status_code, 401)


class TestProtected(unittest.TestCase):

    def setUp(self):
        url = root_url + '/v1/users'
        headers = {}
        self.body = {
            'username': gen_uniq('user'),
            'password': 'password'
        }
        r.post(url=url, data=self.body, headers=headers)

        login_url = root_url + '/v1/login'
        headers = {}
        self.res = r.post(url=login_url, data=self.body, headers=headers)
        self.res_body = self.res.json()

        url = root_url + '/v1/protected'
        headers = {}
        body = {
            'access_token': self.res_body['access_token']
        }
        self.res_protected = r.get(url=url, data=body, headers=headers)
        self.res_protected_body = self.res_protected.json()

    def test_correct_post_v1_protected_should_return_status_200(self):
        self.assertEqual(self.res_protected.status_code, 200)

    def test_correct_post_v1_protected_should_return_username(self):
        self.assertTrue('logged_in_as' in self.res_protected_body)
        self.assertTrue(
            self.res_protected_body['logged_in_as'] == self.body['username'])


if __name__ == '__main__':
    unittest.main()
