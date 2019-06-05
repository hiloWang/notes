package ztiany.chapter5


//--------------------
//多行字符串
//--------------------
memo = '''
            你好啊
            我在学习Groovy
'''
println memo
val1  = 234000
//"""同样支持求值
memo1 = """

                        我在学习Groovy
                        GString挺不错的
              \$ ${val1}

"""
println memo1


//处理xml

langs = ['c++':'Stroustrup', 'java':'Gosling']

content = ''
langs.each {
    key,value->
        fragment = """
            <language name="$key">
                <author>$value</author>
            </language>
        """
        content+=fragment

}
println content

