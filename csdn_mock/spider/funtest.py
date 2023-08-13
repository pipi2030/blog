from time import time
from time import sleep
start_time = time()
sleep(4)
print("hello")
end_time = time()
print(str((end_time - start_time)/60) + "分钟")

