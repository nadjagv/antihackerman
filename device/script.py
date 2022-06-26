import logging
import threading
import time
import json

class Device:

    def __init__(self,name,id,interval):
        self.name=name
        self.id=id
        self.interval=interval


def thread_function(device):
    logging.info("Thread %s: starting for device %s", device.id,device.name)
    while(True):
        print(device.name)
        time.sleep(device.interval)

if __name__ == "__main__":
    format = "%(asctime)s: %(message)s"
    logging.basicConfig(format=format, level=logging.INFO,
                        datefmt="%H:%M:%S")

    f=open("devices.json")
    data = json.load(f)
    f.close()

    devices=[]

    for device in data['devices']:
        devices.append(Device(device["name"],device["id"],device["interval"]))

    threads = list()
    for device in devices:
        logging.info("Main    : create and start thread %d.", device.id)
        x = threading.Thread(target=thread_function, args=(device,))
        threads.append(x)
        x.start()

    for index, thread in enumerate(threads):
        thread.join()
