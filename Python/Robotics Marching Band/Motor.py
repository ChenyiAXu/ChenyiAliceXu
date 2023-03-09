from utils.brick import Motor, TouchSensor, wait_ready_sensors
import time

motor = Motor("C")
touch_sensor = TouchSensor(2)
wait_ready_sensors(True)


try:
    
        motor.set_power(0)
        time.sleep(2)
        while True:
            if touch_sensor.get_raw_value() == 1:
                for x in range(10000):
                    motor.set_position_relative(-55) 
                    time.sleep(1)
                    motor.set_position_relative(70)
                    time.sleep(1)

        
except SensorError as error:
    exit()
                


