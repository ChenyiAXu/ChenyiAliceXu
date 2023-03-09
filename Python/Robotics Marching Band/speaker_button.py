#!/usr/bin/env python3

"""
Module to play sounds when the touch sensor is pressed.
This file must be run on the robot.
"""

from utils import sound
from utils.brick import TouchSensor, wait_ready_sensors

SOUND = sound.Sound(duration=0.3, pitch="C4", volume=100)
SOUND2 = sound.Sound(duration=0.3, pitch="B3", volume=100)
SOUND3 = sound.Sound(duration=0.3, pitch="Bb3", volume=100)
SOUND4 = sound.Sound(duration=0.3, pitch="A3", volume=100)
Tune = [SOUND, SOUND2, SOUND3, SOUND4, SOUND2, SOUND]

TOUCH_SENSOR = TouchSensor(1)

wait_ready_sensors()  # Note: Touch sensors actually have no initialization time


def play_sound(sound):
    sound.play()
    sound.wait_done()


def play_sound_on_button_press():
    "In an infinite loop, play a single note when the touch sensor is pressed."
    try:
        x = 0
        while True:
            if TOUCH_SENSOR.is_pressed():
                if x == 5:
                    x = 0
                else:
                    play_sound(Tune[x])
                    x = x + 1
    except BaseException:  # capture all exceptions including KeyboardInterrupt (Ctrl-C)"
        exit()


if __name__ == '__main__':
    play_sound_on_button_press()
