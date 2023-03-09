#!/usr/bin/env python3

"""
This test is used to collect data from the color sensor.
It must be run on the robot.
"""

# Add your imports here, if any
from utils.brick import EV3ColorSensor, wait_ready_sensors, TouchSensor, Color
import time

COLOR_SENSOR_DATA_FILE = "/home/pi/Desktop/Lab 2 - Sensors & Testing attached files Jan 31, 2023 131 PM/lab2-starter-code_PYR/lab2-starter-code_w23/data_analysis/color_sensor.csv"

# complete this based on your hardware setup
color_sensor_ = EV3ColorSensor(3)
touch_sensor_ = TouchSensor(2)
exit_ts = TouchSensor(4)

wait_ready_sensors(True) # Input True to see what the robot is trying to initialize! False to be silent.


def collect_color_sensor_data():
    "Collect color sensor data."
    color_sensor = EV3ColorSensor(3) # replace "..." with the appropriate port
    touch_sensor = TouchSensor(2) # replace "..." with the appropriate port
    exit_ts = TouchSensor(4)
    
    count = 0


    f = open(COLOR_SENSOR_DATA_FILE, "w")
    while True:
        if touch_sensor.get_raw_value() == 1:
            color_name = color_sensor_.get_color_name()
            if color_name == "Unknown":
                purple_min_rgb = (50,0,0)
                purple_max_rgb = (100,50,100)
                rgb = color_sensor_.get_rgb()
                #if rgb >= purple_min_rgb and rgb <= purple_max_rgb:
                if all(purple_min_rgb[i] <= rgb[i] <= purple_max_rgb[i] for i in range(3)):
                    
                    print("Purple")
            else:
                print(color_name)
            
        time.sleep(0.5)
        if exit_ts.get_raw_value() == 1:
            f.close()
            break




if __name__ == "__main__":
    collect_color_sensor_data()