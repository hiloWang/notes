# re为正则表达式模块
import re

string3 = "AC35B9V2L$40K43ll6af52hf7"
string4 = "life is short, i use python, i love python"

# search，匹配第一个
# group概念：group()默认获取第0组，第0组表示完整的字符串匹配
print(re.search("\d(.*)\d", string3))  #
print(re.search("\d(.*)\d", string3).group(1))  #

print(re.search("life(.*)python", string4))
print(re.search("life(.*)python(.*)python", string4).group(0, 1, 2))  # 1代表获取第一组的内容，即第一个()匹配到的
