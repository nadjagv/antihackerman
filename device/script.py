import logging
import threading
import time
import json
from abc import ABC, abstractmethod
import random
import time
from datetime import datetime
from os.path import exists


class Device(ABC):

    def __init__(self,name,id,filePath,realEstateId):
        self.name=name
        self.id=id
        self.filePath="../backend-app-myhouse/src/main/resources/devices/"+filePath
        self.realEstateId=realEstateId

    @abstractmethod
    def generate_output(self):
        pass


class BooleanDevice(Device):

    def __init__(self, name, id, filePath, realEstateId,activeTrueStr,activeFalseStr):
        Device.__init__(self, name, id, filePath, realEstateId)
        self.activeTrueStr=activeTrueStr
        self.activeFalseStr=activeFalseStr

    def generate_output(self):
        res=random.randrange(0,2)
        if(res==1):
            return (self.name+" id: "+str(self.id)+" Real Estate id: "
                    +str(self.realEstateId)+" : "+self.activeTrueStr,time.time(),res)
        else:
            return (self.name+" id: "+str(self.id)+" Real Estate id: "
                    +str(self.realEstateId)+" : "+self.activeFalseStr,time.time(),res)

class IntervalDevice(Device):

    def __init__(self, name, id, filePath, realEstateId,valueDefinition,minValue,maxValue):
        Device.__init__(self, name, id, filePath, realEstateId)
        self.valueDefinition=valueDefinition
        self.minValue=minValue
        self.maxValue=maxValue

    def generate_output(self):
        res=random.randrange(self.minValue,self.maxValue+1)
        return (self.name+" id: "+str(self.id)+" Real Estate id: "
                +str(self.realEstateId)+" : "+str(res)+" "+self.valueDefinition,time.time(),res)


def thread_function(device):
    logging.info("Thread %s: starting for device %s", device.id,device.name)
    while(True):

        (output,timestamp,value)=device.generate_output()
        print(output+" at "+str(datetime.fromtimestamp(timestamp)))

        if(not exists(device.filePath)):
            with open(device.filePath, "w+") as f:
                json.dump([], f)

        data=[]
        with open(device.filePath) as f:
            data = json.load(f)

        data.append({
          "message": output,
          "value": value,
          "timestamp": timestamp
        })

        with open(device.filePath,"w") as f:
            json.dump(data,f)

        time.sleep(random.randrange(2,11))

if __name__ == "__main__":
    format = "%(asctime)s: %(message)s"
    logging.basicConfig(format=format, level=logging.INFO,
                        datefmt="%H:%M:%S")

    f=open("../backend-app/src/main/resources/device_config.json")
    data = json.load(f)
    f.close()

    devices=[]

    for device in data:
        if(device["type"]=="INTERVAL_DEVICE"):
            devices.append(IntervalDevice(device["name"], device["id"], device["filePath"],device["realestateId"]
                                          ,device["valueDefinition"],device["minValue"],device["maxValue"]))
        else:
            devices.append(BooleanDevice(device["name"], device["id"], device["filePath"],device["realestateId"]
                                         ,device["activeTrueStr"],device["activeFalseStr"]))

    threads = list()
    for device in devices:
        logging.info("Main    : create and start thread %d.", device.id)
        x = threading.Thread(target=thread_function, args=(device,))
        threads.append(x)
        x.start()

    for index, thread in enumerate(threads):
        thread.join()
