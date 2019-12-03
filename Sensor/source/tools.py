import requests 
import psutil
import datetime
import time
import platform

class SensorTools:

    def __init__(self, metric, sensor_name, url, urlAS):
        self.set_variables(metric, sensor_name, url, urlAS)
    
    def set_variables(self, metric, sensor_name, url, urlAS):
        self.API_REGISTER_ENDPOINT             = url + "hosts"
        self.API_CPU_MEASUREMENTS_ENDPOINT     = url + "metrics/" + sensor_name + "_CPU_Host/measurements"
        self.API_BATTERY_MEASUREMENTS_ENDPOINT = url + "metrics/" + sensor_name + "_Battery_Host/measurements"
        self.API_AUTH_SERVICE_LOGIN_ENDPOINT   = urlAS + "login"
        self.API_AUTH_SERVICE_REFRESH_ENDPOINT = urlAS + "token"
        
        self.LOGIN_NAME     = "sensor"
        self.LOGIN_PASSWORD = "sensor"
        while self.log_in():
            pass
        print(self.ACCESS_TOKEN)
        print(self.REFRESH_TOKEN)
        self.metric = metric
        self.hostID = sensor_name
        self.os     = platform.system()
        if self.metric == "CPU":
            self.type           = "CPU"
            self.unit           = "%"
            self.metric_id      = sensor_name + "_CPU_Host"
            self.collected_data = []
            self.timestamp      = []
        elif self.metric == "Battery":
            self.type           = "Battery"
            self.unit           = "%"
            self.metric_id      = sensor_name + "_Battery_Host"
            self.collected_data = []
            self.timestamp      = []
        else:
            self.type1           = "CPU"
            self.unit1           = "%"
            self.metric_id1      = sensor_name + "_CPU_Host"
            self.type2           = "Battery"
            self.unit2           = "%"
            self.metric_id2      = sensor_name + "_Battery_Host"
            self.collected_data1 = []
            self.collected_data2 = []
            self.timestamp       = []
    
    def register_sensor(self):
        if self.metric == "Both":
            data = self.register_json_data_for_two_metrics()
        else:
            data = self.register_json_data_for_one_metric()
        
        while True:
            print(data)
            r = self.post(endpoint = self.API_REGISTER_ENDPOINT, data = data) 
            if r == True:
                break
            else:
                time.sleep(5)
    
    def register_json_data_for_one_metric(self):
        data            = {}
        data["host-id"] = self.hostID
        data["os"]      = self.os
    
        metrics              = {}
        metrics["type"]      = self.type
        metrics["unit"]      = self.unit
        metrics["metric-id"] = self.metric_id
    
        data["metrics"] = [metrics]
        
        return data
        
    def register_json_data_for_two_metrics(self):
        data            = {}
        data["host-id"] = self.hostID
        data["os"]      = self.os
    
        metrics1              = {}
        metrics1["type"]      = self.type1
        metrics1["unit"]      = self.unit1
        metrics1["metric-id"] = self.metric_id1
        
        metrics2              = {}
        metrics2["type"]      = self.type2
        metrics2["unit"]      = self.unit2
        metrics2["metric-id"] = self.metric_id2
        
        data["metrics"] = [metrics1, metrics2]
        
        return data

    def send_data(self):
        self.coppy_data()
        if self.metric == "Both":
            data = self.json_data_for_two_metrics()
            print(data[0])
            print(data[1])
            r = self.post(endpoint = self.API_CPU_MEASUREMENTS_ENDPOINT,     data = data[0])
            r = self.post(endpoint = self.API_BATTERY_MEASUREMENTS_ENDPOINT, data = data[1])
        elif self.metric == "CPU":
            data = self.json_data_for_one_metric()
            print(data)
            r = self.post(endpoint = self.API_CPU_MEASUREMENTS_ENDPOINT,     data = data)
        else:
            data = self.json_data_for_one_metric()
            print(data)
            r = self.post(endpoint = self.API_BATTERY_MEASUREMENTS_ENDPOINT, data = data)

    def json_data_for_one_metric(self):
        data = []
        for i in range(len(self.data_to_send)):
            temp        = {}
            temp["val"] = str(self.data_to_send[i])
            temp["ts"]  = self.data_timestamp_to_send[i]
            data.append(temp)
        
        return data
    
    def json_data_for_two_metrics(self):
        data1 = []
        data2 = []
        for i in range(len(self.data1_to_send)):
            temp1        = {}
            temp1["val"] = str(self.data1_to_send[i])
            temp1["ts"]  = self.data_timestamp_to_send[i]
            
            temp2        = {}
            temp2["val"] = str(self.data2_to_send[i])
            temp2["ts"]  = self.data_timestamp_to_send[i]
            
            data1.append(temp1)
            data2.append(temp2)
        
        return [data1, data2]    
        
    def colect_data(self):  
        if self.metric == "CPU":
            self.collected_data.append(psutil.cpu_percent(interval=1.0))
        elif self.metric == "Battery":
            if psutil.sensors_battery() is None:
                val = -1
            else: 
                val = psutil.sensors_battery().percent
            self.collected_data.append(val)
        else:
            self.collected_data1.append(psutil.cpu_percent(interval=1.0))
            if psutil.sensors_battery() is None:
                val = -1
            else: 
                val = psutil.sensors_battery().percent
            self.collected_data2.append(val)
        self.timestamp.append(datetime.datetime.fromtimestamp(time.time()).strftime('%d/%m/%Y %H:%M:%S'))
        
    def coppy_data(self):
        if self.metric == "Both":
            self.data1_to_send   = self.collected_data1
            self.data2_to_send   = self.collected_data2
            self.collected_data1 = []
            self.collected_data2 = []
        else:
            self.data_to_send    = self.collected_data
            self.collected_data  = []
        
        self.data_timestamp_to_send = self.timestamp
        self.timestamp = []
    
    def data_amount(self):
        return len(self.timestamp)
    
    def post(self, endpoint, data):
        result = False
        headers = {'content-type': 'application/json', 'access-token': self.ACCESS_TOKEN}
        
        if endpoint == self.API_REGISTER_ENDPOINT:
            try:
                r = requests.post(url = endpoint, json = data, headers=headers) 
                #print(r.json())
                print(r.status_code, r.reason)
                if r.status_code == 201:
                    print("Sensor registration complete")
                    result = True
                elif r.status_code == 409:
                    print('Looks like, sensor with name "{0}" already exist. Skipping registration...'.format(self.hostID))
                    result = True
                else:
                    print("Something went wrong during registration. HTTP code: {0}. After a 5 seconds, we will try again...".format(r.status_code))
                    result = False
            except requests.exceptions.ConnectionError:
                print("Can not find server. Try to connect again after 5 seconds...")
                result = False
            except requests.exceptions.RequestException as e:
                print("During registration error: {0} ocurre.".format(e))
                result = False
        else:
            try:
                r = requests.post(url = endpoint, json = data, headers=headers) 
                #print(r)
                print(r.status_code, r.reason)
                if r.status_code == 201:
                    result = True
            except requests.exceptions.RequestException as e:
                print("During data send error: {0} occurred.".format(e))
                result = False
        
        return result
    
    def login_json_data(self):
        data             = {}
        data["username"] = self.LOGIN_NAME
        data["password"] = self.LOGIN_PASSWORD    
        return data
    
    def log_in(self):
        try:
            r = requests.post(url = self.API_AUTH_SERVICE_LOGIN_ENDPOINT, json = self.login_json_data()) 
            if r.status_code == 200:
                self.ACCESS_TOKEN  = r.json()['access_token']
                self.REFRESH_TOKEN = r.json()['refresh_token']
                result = True
            print(r.status_code)
        except requests.exceptions.RequestException as e:
            print("During data send error: {0} occurred.".format(e))
            return False
     
    def refresh_token(self):
        data                  = {}
        data["refresh_token"] = self.REFRESH_TOKEN
        try:
            r = requests.post(self.API_AUTH_SERVICE_REFRESH_ENDPOINT, json = data) 
            if r.status_code == 200:
                self.ACCESS_TOKEN  = r.json()['access_token']
            print("New token")
            print(r.status_code)
            print("____________________")
        except requests.exceptions.RequestException as e:
            print("During data send error: {0} occurred.".format(e))
            return False
