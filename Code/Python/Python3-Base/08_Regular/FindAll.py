#########################
#  正则表达式
#########################


# re为正则表达式模块
import re
import pkg.Tools as Tools

string1 = "1C|2C++|3Java|4C#|5Python|6JavaScript"
string2 = "100000001"
string3 = "AC35B9V2L$40K43ll6af52hf7"
string4 = "life is short, i use python, i love python"

# 找到Python
Tools.print_divider("sample")
python = re.findall("Python", string1)  # 返回包含结果的list
print(python)

# 找到所有数字
Tools.print_divider("找到所有数字")
allDigit = re.findall('(\d+)', string3)
acDigit = re.findall('AC(\d+)B', string3)  # 找到组中规定的内容
print(allDigit)
print(acDigit)

# 贪婪与非贪婪，Python默认倾向于贪婪匹配规则
Tools.print_divider("贪婪与非贪婪")
print(re.findall("[A-z]{3,6}", string1))  # 贪婪模式 ['Java', 'Python', 'JavaSc', 'ript']
print(re.findall("[A-z]{3,6}?", string1))  # 非贪婪模式 ['Jav', 'Pyt', 'hon', 'Jav', 'aSc', 'rip']

# 边界匹配
Tools.print_divider("边界匹配")
print(re.findall("\d{4,8}", string2))  # ['10000000']
print(re.findall("^\d{4,8}$", string2))  # [] ，要求完整的4位到8位字符串
print(re.findall("^000", string2))  # [] 以000开始
print(re.findall("000$", string2))  # [] 以000结尾

# 模式：re.I表示忽略大小写
# 模式：re.S表示"."符号匹配所有的字符，"."默认匹配除换行符之外的所有字符
Tools.print_divider("模式")
print(re.findall("python", string1, re.I))
print(re.findall("python.{1,3}", string1, re.I | re.S))

