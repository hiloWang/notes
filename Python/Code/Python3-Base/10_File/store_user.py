import os
import json

file_name = "user.json"


def get_user_name():
    if os.path.exists(file_name) and os.path.isfile(file_name):
        with open(file_name, 'r') as user_file:
            return json.load(user_file)


def store_user_name(user_name):
    with open(file_name, 'w') as user_file:
        json.dump(user_name, user_file)


def get_new_name():
    user_name = ""
    while not user_name:
        user_name = input("请输入姓名")
    return user_name


def main():
    user_name = get_user_name()
    if user_name:
        print("welcome back, %s" % user_name)
    else:
        user_name = get_new_name()
        print("i will remember your name")
        store_user_name(user_name)


main()
