## Project setup
```
python install
pip install -r requirements.txt

```

### Run server
#### Unix Bash(Linux, Mac, etc.):
```
export FLASK_APP=app.py
export FLASK_RUN_PORT=8000
flask run
```
#### Windows:
```
set FLASK_APP=app.py
set FLASK_RUN_PORT=8000
flask run
```

## Available endpoints

### v1/users
#### POST
```
Create new user.
Request parameters

Body:

{
   "username":"<username>",
   "password":"<password>"
}

Response

Success: 201 Failure: 400
```
### v1/login
#### POST
```
Returns JWT authentication token and refresh token in login process.
Request parameters

Body:

{
   "username":"<username>",
   "password":"<password>"
}

Response

Success: 200

{
   "access_token":"<access_token>",
   "refresh_token":"<refresh_token>"
}

Failure: 400, 401
```
### v1/token
#### POST
```
Returns new JWT access-token in token refresh process.
Token refresh:

Body:

{
   "refresh_token":"<refresh_token>"
}

Response

Success: 200

{
   "access_token":"<access_token>"
}

Failure: 400, 401, 422
```
#### DELETE
```
Invalidates refresh token.

Body:

{
   "refresh_token":"<refresh_token>"
}

Success: 200 Failure: 400, 401, 422
```

### v1/protected
#### GET
```
For testing purpose, returns current user name only when valid access-token
Token refresh:

Body:

{
   "access_token":"<access_token>"
}

Response

Success: 200 Failure: 401, 422

{ 
   "logged_in_as": "<username>"
}
```
