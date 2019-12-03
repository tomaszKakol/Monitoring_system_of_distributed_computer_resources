import unittest
from source.tools import SensorTools
import platform
from unittest.mock import patch

class TestSensorMethods(unittest.TestCase):

    def test_set_variables_CPU(self):
        metric = "CPU"
        name   = "testCPU"
        url    = "http://localhost:8080/v1/"
        tool = SensorTools(metric, name, url)
        
        self.assertEqual("http://localhost:8080/v1/hosts", tool.API_REGISTER_ENDPOINT)
        self.assertEqual("http://localhost:8080/v1/metrics/testCPU_CPU_Host/measurements", tool.API_CPU_MEASUREMENTS_ENDPOINT)
        self.assertEqual("CPU", tool.metric)
        self.assertEqual("testCPU", tool.hostID)
        self.assertEqual("CPU", tool.type)
        self.assertEqual("%", tool.unit)
        self.assertEqual("testCPU_CPU_Host", tool.metric_id)
        
    def test_set_variables_battery(self):
        metric = "Battery"
        name   = "testBattery"
        url    = "http://localhost:8080/v1/"
        tool = SensorTools(metric, name, url)
        
        self.assertEqual("http://localhost:8080/v1/hosts", tool.API_REGISTER_ENDPOINT)
        self.assertEqual("http://localhost:8080/v1/metrics/testBattery_Battery_Host/measurements", tool.API_BATTERY_MEASUREMENTS_ENDPOINT)
        self.assertEqual("Battery", tool.metric)
        self.assertEqual("testBattery", tool.hostID)
        self.assertEqual("Battery", tool.type)
        self.assertEqual("%", tool.unit)
        self.assertEqual("testBattery_Battery_Host", tool.metric_id)
        
    def test_set_variables_both(self):
        metric = "Both"
        name   = "testBoth"
        url    = "http://localhost:8080/v1/"
        tool = SensorTools(metric, name, url)
        
        self.assertEqual("http://localhost:8080/v1/hosts", tool.API_REGISTER_ENDPOINT)
        self.assertEqual("http://localhost:8080/v1/metrics/testBoth_CPU_Host/measurements", tool.API_CPU_MEASUREMENTS_ENDPOINT)
        self.assertEqual("http://localhost:8080/v1/metrics/testBoth_Battery_Host/measurements", tool.API_BATTERY_MEASUREMENTS_ENDPOINT)
        self.assertEqual("Both", tool.metric)
        self.assertEqual("testBoth", tool.hostID)
        self.assertEqual("CPU", tool.type1)
        self.assertEqual("%", tool.unit1)
        self.assertEqual("testBoth_CPU_Host", tool.metric_id1)
        self.assertEqual("Battery", tool.type2)
        self.assertEqual("%", tool.unit2)
        self.assertEqual("testBoth_Battery_Host", tool.metric_id2)
        
    def test_register_json_data_for_one_metric(self):
        right_data = {
                        'host-id': 'test', 
                        'os': str(platform.system()), 
                        'metrics': [{
                                       'type': 'CPU', 
                                       'unit': '%', 
                                       'metric-id': 'test_CPU_Host'
                                   }]
                     }
        
        metric = "CPU"
        name   = "test"
        url    = "http://localhost:8080/v1/"
        tool = SensorTools(metric, name, url)
        data = tool.register_json_data_for_one_metric()

        self.assertEqual(right_data, data)
        
    def test_register_json_data_for_two_metric(self):
        right_data = {
                        'host-id': 'test', 
                        'os': str(platform.system()), 
                        'metrics': [{
                                       'type': 'CPU', 
                                       'unit': '%', 
                                       'metric-id': 'test_CPU_Host'
                                    },
                                    {
                                       'type': 'Battery', 
                                       'unit': '%', 
                                       'metric-id': 'test_Battery_Host'
                                   }]
                     }
        
        metric = "Both"
        name   = "test"
        url    = "http://localhost:8080/v1/"
        tool = SensorTools(metric, name, url)
        data = tool.register_json_data_for_two_metrics()

        self.assertEqual(right_data, data)

    def test_json_data_for_one_metric(self):
        right_data = [{'val': '2', 'ts': 1.4}, {'val': '3', 'ts': 1.7}]
        
        metric = "CPU"
        name   = "test"
        url    = "http://localhost:8080/v1/"
        tool = SensorTools(metric, name, url)
        tool.coppy_data()
        tool.data_to_send = [2, 3]
        tool.data_timestamp_to_send = [1.4, 1.7]
        data = tool.json_data_for_one_metric()

        self.assertEqual(right_data, data)
        
    def test_json_data_for_two_metrics(self):
        right_data = [[{'val': '2', 'ts': 1.4}, {'val': '3', 'ts': 1.7}], [{'val': '5', 'ts': 1.4}, {'val': '6', 'ts': 1.7}]]
        
        metric = "Both"
        name   = "test"
        url    = "http://localhost:8080/v1/"
        tool = SensorTools(metric, name, url)
        tool.coppy_data()
        tool.data1_to_send = [2, 3]
        tool.data2_to_send = [5, 6]
        tool.data_timestamp_to_send = [1.4, 1.7]
        data = tool.json_data_for_two_metrics()

        self.assertEqual(right_data, data)
        
    def test_copy_data_both(self):
        metric = "Both"
        name   = "test"
        url    = "http://localhost:8080/v1/"
        tool = SensorTools(metric, name, url)
        tool.collected_data1 = [2, 3]
        tool.collected_data2 = [5, 6]
        tool.timestamp = [1.4, 1.7]
        tool.coppy_data()
        
        self.assertEqual([2, 3], tool.data1_to_send)
        self.assertEqual([5, 6], tool.data2_to_send)
        self.assertEqual([1.4, 1.7], tool.data_timestamp_to_send)
        self.assertEqual([], tool.collected_data1)
        self.assertEqual([], tool.collected_data2)
        self.assertEqual([], tool.timestamp)
      
    def test_copy_data_CPU(self):
        metric = "CPU"
        name   = "test"
        url    = "http://localhost:8080/v1/"
        tool = SensorTools(metric, name, url)
        tool.collected_data = [2, 3]
        tool.timestamp = [1.4, 1.7]
        tool.coppy_data()
        
        self.assertEqual([2, 3], tool.data_to_send)
        self.assertEqual([1.4, 1.7], tool.data_timestamp_to_send)
        self.assertEqual([], tool.collected_data)
        self.assertEqual([], tool.timestamp)
     
    def test_data_amount(self):
        metric = "CPU"
        name   = "test"
        url    = "http://localhost:8080/v1/"
        tool = SensorTools(metric, name, url)
        tool.timestamp = [1.4, 1.7, 2.2, 3.1, 5.8]
        
        self.assertEqual(5, tool.data_amount())

if __name__ == '__main__':
    unittest.main()