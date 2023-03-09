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

SOUND = sound.Sound(duration=0.3, pitch="C4", volume=100)
SOUND2 = sound.Sound(duration=0.3, pitch="B3", volume=100)
SOUND3 = sound.Sound(duration=0.3, pitch="Bb3", volume=100)
SOUND4 = sound.Sound(duration=0.3, pitch="A3", volume=100)

wait_ready_sensors(True)  # Input True to see what the robot is trying to initialize! False to be silent.


def play_sound(sound):
    sound.play()
    sound.wait_done()


def collect_color_sensor_data():
    "Collect color sensor data."
    color_sensor_ = EV3ColorSensor(3)  # replace "..." with the appropriate port
    touch_sensor = TouchSensor(2)  # replace "..." with the appropriate port
    while True:
        if touch_sensor.get_raw_value() == 1:
            color_name = color_sensor_.get_color_name()
            if color_name == "Red":
                break
            if color_name == "Blue":
                play_sound(SOUND)
            if color_name == "Green":
                play_sound(SOUND2)
            if color_name == "Yellow":
                play_sound(SOUND3)

            if color_name == "Unknown":
                purple_min_rgb = (50, 0, 0)
                purple_max_rgb = (100, 50, 100)
                rgb = color_sensor_.get_rgb()
                if all(purple_min_rgb[i] <= rgb[i] <= purple_max_rgb[i] for i in range(3)):
                    play_sound(SOUND4)

            time.sleep(0.5)


if __name__ == "__main__":
    collect_color_sensor_data()
