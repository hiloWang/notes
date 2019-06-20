#include "bionic-api.h"
#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <android/log.h>
#include <sys/system_properties.h>

/**
 * 执行shell命令
 * @param command
 */
void shell(const char *command) {
    //system没有接收子进程输出的通讯通道，命令执行前主进程一个等待
    int result = system(command);
    if (result == -1 || result == 127) {
        LOGI("system(%s) fail", command);
    } else {
        LOGI("system(%s) success", command);
    }
}

/**
 * 执行shell命令，并打印命令结果
 * @param command
 */
void shellPro(const char *command) {
    FILE *stream;
    stream = popen(command, "r");
    //默认情况下popen是完全缓存的，使用fflush刷新
    fflush(stream);
    if (NULL == stream) {
        LOGI("popen(%s) fail", command);
    } else {
        char buf[1024 * 5];
        int status;
        while (NULL != fgets(buf, 1024 * 5, stream)) {
            LOGI("read: %s", buf);
        }
        status = pclose(stream);
        LOGI("pclose result: %d", status);
    }
}

/**
 *
 * @param propertyName 属性名称，比如：ro.product.model
 * @param out  出参，该字符数组的最大长度不会超过 PROP_VALUE_MAX
 * @return
 */
int getSystemProperty(char *propertyName, char *out) {
    if (0 == __system_property_get(propertyName, out)) {
        LOGI("getSystemProperties not find  %s", propertyName);
        return 0;
    }
    LOGI("getSystemProperties find value = %s", out);
    return 1;
}

/**
 *
 * @param propertyName 属性名称，比如：ro.product.model
 * @param out  出参，该字符数组的最大长度不会超过 PROP_VALUE_MAX
 * @return 成功或失败
 */
int findSystemProperty(char *propertyName/**/, char *out) {
    //__system_property_find用于搜索系统属性，返回值在系统声明周期内有效
    const prop_info *result = __system_property_find(propertyName);
    if (NULL == result) {
        LOGI("findSystemProperties not find  %s", propertyName);
        return 0;
    } else {
        //使用__system_property_read读取prop_info中的值,name为可选出参，用于拷贝属性名
        if (0 == __system_property_read(result, NULL, out)) {
            LOGI("findSystemProperties find value = %s", out);
            return 1;
        } else {
            LOGI("findSystemProperties not find  %s", propertyName);
            return 0;
        }
    }
}

/**
 * 在Android系统中，app被当作用户看待
 */
int getUID() {

    //获取用户ID
    uid_t uid = getuid();

    //获取程序组ID
    gid_t gid = getgid();

    //获取用户名
    const char *login = getlogin();

    LOGI("uid = %d, gid = %d, login = $s", uid, gid, login);

    return uid;
}