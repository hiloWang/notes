package me.ztiany.groovy

import groovy.json.JsonSlurper

def response = getNetworkData('http://gank.io/api/xiandu/categories')

println response

static def getNetworkData(String url) {
    //发送http请求
    def connection = new URL(url).openConnection()
    connection.setRequestMethod('GET')
    connection.connect()
    def response = connection.content.text
    //将json转化为实体对象
    def jsonSluper = new JsonSlurper()
    return jsonSluper.parseText(response)
}


