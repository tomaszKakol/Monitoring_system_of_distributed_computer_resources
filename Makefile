#Create the appropriate branches in the project structure beforehand
docker_build:
	docker build --tag=all:auth-km3 AuthService/ 
	docker build --tag=all:monitor-km3 ./Monitor  
	docker build --tag=all:sensor-km3 ./Sensor
	docker build --tag=all:gateway-km3 ./APIGateway
	docker build --tag=all:client-km3 ./client
	docker build --tag=all:autoclient-km3 AutomaticClient/

docker_run:
	docker run -d --network host -it all:auth-km3
	docker run -d --network host -it all:gateway-km3
	docker run -d --network host -it all:monitor-km3
	docker run --network host -it all:sensor-km3
	
docker_run_no_d:
	docker run --network host -it all:auth-km3
	docker run --network host -it all:gateway-km3
	docker run --network host -it all:monitor-km3
	docker run --network host -it all:sensor-km3

docker_push:
		docker login -u all 
		docker push all:auth-km3
		docker push all:monitor-km3
		docker push all:gateway-km3
		docker push all:sensor-km3
		docker push all:client-km3
		docker push all:autoclient-km3