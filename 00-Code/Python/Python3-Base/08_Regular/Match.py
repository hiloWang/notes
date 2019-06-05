import re

string1 = "1C|2C++|3Java|4C#|5Python|6JavaScript"
string2 = "100000001"
string3 = "AC35B9V2L$40K43ll6af52hf7r"
string4 = "life is short, i use python, i love python"

# match，match尝试在字符串的首字母开始匹配，如果首字母没有匹配到，则返回None，匹配第一个
print(re.match("...", string1))
print(re.match("...", string1).group(0))
print(re.match("\d", string2))  # 1
print(re.match("\d", string2).group(0))  # 1
print(re.match("\w\w(\d\d)\w", string3).group(0))  # AC35B
print(re.match("\w\w(\d\d)\w", string3).group(1))  # 35
