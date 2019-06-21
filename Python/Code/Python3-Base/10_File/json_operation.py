#########################
#  JSON API
#########################

"""
Json和Python数据类型对应：
json python
object  dict
array list
string str
number int
number float
true True
false False
null None

 json有两类api：
 dump/dumps 序列化
 load/loads 反序列化

 如何存储：
 关系型数据库(MySQL)不要存储序列化的数据
 NOSQL(MongoDB)存储序列化的数据
"""

import json
import os

data = '{"age":27,"name":"Ztiany","job":"Developer"}'
file_name = "json.json"
json.dump(data, open(file_name, 'w'))

with open(file_name, 'r') as file_json:
    json_data = json.load(file_json)
    print(type(json_data))
    print(json_data)

json_data = json.loads(data)
print(type(json_data))
print(json_data)

# 删除文件
os.remove(file_name)
