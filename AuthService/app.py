from flask import Flask
from flask_restful import Api
from flask_sqlalchemy import SQLAlchemy
from flask_jwt_extended import JWTManager
from flask_cors import CORS

app = Flask(__name__)
CORS(app)
api = Api(app)

app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///app.db'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
app.config['SECRET_KEY'] = 'some-secret-string'

db = SQLAlchemy(app)


@app.before_first_request
def create_tables():
    db.create_all()


app.config['JWT_TOKEN_LOCATION'] = ('headers', 'json')
app.config['JWT_SECRET_KEY'] = 'jwt-secret-string'
app.config['PROPAGATE_EXCEPTIONS'] = True
app.config['JWT_BLACKLIST_ENABLED'] = True
app.config['JWT_BLACKLIST_TOKEN_CHECKS'] = ['access', 'refresh']
jwt = JWTManager(app)





@jwt.token_in_blacklist_loader
def check_if_token_in_blacklist(decrypted_token):
    jti = decrypted_token['jti']
    return models.RevokedTokenModel.is_jti_blacklisted(jti)


import models, resources



api.add_resource(resources.UserRegistration, '/v1/users')
api.add_resource(resources.UserLogin, '/v1/login')
api.add_resource(resources.TokenOperations, '/v1/token')
api.add_resource(resources.TokenVerification, '/v1/protected')

# Add 2 users:
if not models.UserModel.find_by_username('test'):
    new_user = models.UserModel(
                username='test',
                password=models.UserModel.generate_hash('test')
            )
    new_user.save_to_db()

if not models.UserModel.find_by_username('admin'):
    new_user = models.UserModel(
                username='admin',
                password=models.UserModel.generate_hash('admin')
            )
    new_user.save_to_db()

if not models.UserModel.find_by_username('sensor'):
    new_user = models.UserModel(
                username='sensor',
                password=models.UserModel.generate_hash('sensor')
            )
    new_user.save_to_db()

if not models.UserModel.find_by_username('monitor'):
    new_user = models.UserModel(
                username='monitor',
                password=models.UserModel.generate_hash('monitor')
            )
    new_user.save_to_db()
