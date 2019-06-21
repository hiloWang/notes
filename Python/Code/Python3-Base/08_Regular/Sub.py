# re为正则表达式模块
import re

string1 = "1C|2C++|3Java|4C#|5Python|6JavaScript"
string2 = "100000001"
string3 = "AC35B9V2L$40K43ll6af52hf7"
string4 = "life is short, i use python, i love python"

print(re.sub("java", "JAVA", string1, 3, re.I))  # string1中匹配java的换成JAVA，3表示最多匹配3个


def sub_convert(value):
    matched = value.group()
    print(value)
    return "---" + matched + "---"


def convert_num(value):
    matched = value.group()
    num = int(matched)
    if num > 6:
        return str(1)
    else:
        return str(2)


# 找到string1中匹配的项后，将会调用sub_convert函数，并把匹配到的项传递给sub_convert函数，替换成sub_convert函数的返回值
print(re.sub("java", sub_convert, string1, 3, re.I))

print(re.sub("\d", convert_num, string3))  # 替换所有数字
