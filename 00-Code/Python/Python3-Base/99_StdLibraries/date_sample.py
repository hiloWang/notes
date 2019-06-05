import calendar  # 日历
import time  # 时间

#########################
# 演示Python中的日历
#########################

print(calendar.isleap(2016))  # 是不是闰年


#########################
# 演示Python中的时间
#########################

print(time.time())
print(time.localtime(time.time()))
print(time.asctime(time.localtime(time.time())))
