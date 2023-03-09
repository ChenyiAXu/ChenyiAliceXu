from utils.brick import Motor, TouchSensor, wait_ready_sensors, EV3ColorSensor, wait_ready_sensors, TouchSensor, Color
from utils import sound
from threading import Thread
import time


from utils import sound


# complete this based on your hardware setup

motor = Motor("C")
touch_sensor = TouchSensor(1)
emergency = TouchSensor(4)
color_sensor_ = EV3ColorSensor(3)  # replace "..." with the appropriate port
touch_sensor_ = TouchSensor(2)  # replace "..." with the appropriate port

wait_ready_sensors(True)

SOUND = sound.Sound(duration=0.1, pitch="C4", volume=150)
SOUND2 = sound.Sound(duration=0.1, pitch="D4", volume=150)
SOUND3 = sound.Sound(duration=0.1, pitch="E4", volume=150)
SOUND4 = sound.Sound(duration=0.1, pitch="F4", volume=150)


wait_ready_sensors(True)  # Input True to see what the robot is trying to initialize! False to be silent.



def play_sound(sound):
    sound.play()
    sound.wait_done()

def vibrations():
    motor.set_power(0)
    time.sleep(2)
    while True:
        if touch_sensor.get_raw_value() == 1:
            for x in range(10000):
                emergency1()
                motor.set_position_relative(-55) 
                time.sleep(1)
                motor.set_position_relative(70)
                time.sleep(1)
    
    
        
def emergency1():
    if emergency.get_raw_value() == 1:
        exit()
        
def collect_color_sensor_data():
    "Collect color sensor data."
    while True:
        emergency1()
        if touch_sensor_.get_raw_value() == 1:
                
                color_name = color_sensor_.get_color_name()
                if color_name == "Red":
                    play_sound(SOUND4)
                if color_name == "Blue":
                    play_sound(SOUND)
                if color_name == "Green":
                    play_sound(SOUND2)
                if color_name == "Yellow":
                    play_sound(SOUND3)
                
                    
        
                
        
if __name__=='__main__':
    #play the drumming mechanism
    Thread(target = collect_color_sensor_data).start()
    Thread(target = vibrations).start()
    
    
    #method for the color sensor
    
    