1. Monitor
	Zbudowanie projektu:
	mvn install
	
	Uruchomienie Monitora:
	mvn spring-boot:run
	
	Dodatkowe ustawienia (application.properties (src/main/resources)):
	MONITORID=v1  
	API_VERSION=v1  
	API_GATEWAY_URL=http://127.0.0.1:8081/v1/monitors  
	API_ENDPOINT=http://127.0.0.1:8080/  
	AUTH_SERVICE_URL = http://127.0.0.1:8000/v1/
	
2. Auth service
	Przygotowanie środowiska:
	python install
	pip install -r requirements.txt

	Dodatkowe ustawienia:
	Unix Bash(Linux, Mac, etc.):
		export FLASK_APP=app.py
		export FLASK_RUN_PORT=8000
	
	Windows:
		set FLASK_APP=app.py
		set FLASK_RUN_PORT=8000
	
	Uruchomienie Serwisu uwierzytelniającego:
	flask run --host=127.0.0.1

3. Sensor 
	Przygotowanie środowiska:
	python install
	pip install -r requirements.txt
	
	Uruchomienie Sensora:
	python source\sensor.py
	
4. API Gateway
	Przygotowanie środowiska:
	python install
	pip install -r requirements.txt
	
	Uruchomienie API Gateway:
	python manage.py runserver

	Dodatkowe ustawienia (config.py):
	auth_url = 'http://127.0.0.1:8000/' 
	method = 'v1/protected'
	
5. Web client
	Zbudowanie projektu:
	npm install

	Uruchomienie Klienta webowego:
	npm run serve
	
	Wersja produkcyjna:
	npm run build
	npm install -g serve
	serve -l tcp://localhost:8082 -s dist
	
	Dodatkowe ustawienia (.env):
	VUE_APP_API_BASE_URL = 'http://127.0.0.1:8080' 
	VUE_APP_AUTH_BASE_URL = 'http://127.0.0.1:8000'




	
	
