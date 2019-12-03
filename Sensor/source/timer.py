import threading
import time

class Timer():
    def __init__(self, interval, function):
        self.interval_time = time.time()
        self.done=False
        self.interval = interval
        self.function = function

    def run(self):
        self.function()
        self.interval_time+=self.interval
        if not self.done:
            threading.Timer(self.interval_time - time.time(), self.run).start()

    def start(self):
        self.done=False
        self.run()

    def stop(self):
        self.done=True