import urllib.request as request
import re

"""
步骤：
1. 明确目录，爬去哪些数据
2. 找到数据对应的网页
3. 分析网页数据，找到数据所在标签位置
"""


class Spider:
    URL = 'https://www.panda.tv/cate/lol'
    ROOT_PATTERN = '<div class="video-info">[\s\S]*?</div>'  # ?表示非贪婪模式
    NUMBER_PATTERN = '<span class="video-number">([\s\S]*?)</span>'
    TITLE_PATTERN = '</i>([\s\S]*?)</span>'

    def __fetch_content(self):
        request_result = request.urlopen(Spider.URL)
        print(type(request_result))  # <class 'http.client.HTTPResponse'>

        html = request_result.read().decode('utf-8')
        html = str(html)
        return html

    def __analysis(self, html):
        roots = re.findall(Spider.ROOT_PATTERN, html)
        analysis_result = list()

        for root in roots:
            numbers = re.findall(Spider.NUMBER_PATTERN, root)
            titles = re.findall(Spider.TITLE_PATTERN, root)
            analysis_result.append({"name": titles[0].strip(), "number": numbers[0]})

        analysis_result = sorted(analysis_result, key=self.__sorted_seed, reverse=True)  # 排序

        return analysis_result

    def __sorted_seed(self, anchor):
        """
        排序规则
        :param anchor:
        :return:排序的key
        """
        number = anchor['number']
        str_number = re.findall('\d*', number)
        float_number = float(str_number[0])
        if number.find('万') != -1:
            float_number *= 10000

        return float_number

    def go(self):
        html = self.__fetch_content()
        return self.__analysis(html)


spider = Spider()
result = spider.go()
for value in result:
    print(value)
