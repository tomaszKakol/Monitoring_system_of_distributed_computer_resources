from flask_restful import Resource, reqparse
from flask_jwt_extended import JWTManager
from models import UserModel, RevokedTokenModel
from flask_jwt_extended import (create_access_token, create_refresh_token, jwt_required, jwt_refresh_token_required, get_jwt_identity, get_raw_jwt)

parser = reqparse.RequestParser()
parser.add_argument('username', help='This field cannot be blank', required=True)
parser.add_argument('password', help='This field cannot be blank', required=True)




class UserRegistration(Resource):
    def post(self):
        data = parser.parse_args()

        if UserModel.find_by_username(data['username']):
            return {'log': 'User {} already exists'.format(data['username'])}, 409

        new_user = UserModel(
            username=data['username'],
            password=UserModel.generate_hash(data['password'])
        )
        try:
            new_user.save_to_db()
            return {
                'log': 'User created'.format(data['username']),
                }, 201
        except:
            return {'log': 'Something went wrong'}, 500


class UserLogin(Resource):
    def post(self):
        data = parser.parse_args()
        current_user = UserModel.find_by_username(data['username'])

        if not current_user:
            return {'log': 'User {} doesn\'t exist'.format(data['username'])}, 401

        if UserModel.verify_hash(data['password'], current_user.password):
            access_token = create_access_token(identity=data['username'])
            refresh_token = create_refresh_token(identity=data['username'])
            return {
                'access_token': access_token,
                'refresh_token': refresh_token
                }, 200
        else:
            return {'log': 'Wrong credentials'}, 401


class TokenOperations(Resource):
    @jwt_refresh_token_required
    def post(self):
        try:
            current_user = get_jwt_identity()
            access_token = create_access_token(identity=current_user)
            return {'access_token': access_token}, 200
        except:
            return {'log': 'Something went wrong'}, 500

    @jwt_refresh_token_required
    def delete(self):
        try:
            jti = get_raw_jwt()['jti']
            revoked_token = RevokedTokenModel(jti=jti)
            revoked_token.add()
            return {"log": "Successfully deleted token"}, 200
        except:
            return {'log': 'Something went wrong'}, 500


class TokenVerification(Resource):
    @jwt_required
    def get(self):
        try:
            username = get_jwt_identity()
            return {'logged_in_as': username}, 200
        except:
            return {'log': 'Something went wrong'}, 500


