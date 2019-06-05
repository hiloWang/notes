#ifndef BIONICAPI_BIONIC_API_H
#define BIONICAPI_BIONIC_API_H

#ifdef __cplusplus
extern "C" {
#endif


void shell(const char *command);

void shellPro(const char *command);

int getSystemProperty(char *propertyName, char *out);

int getUID();

#define LOGE(format, ...) __android_log_print(ANDROID_LOG_ERROR, "NATIVE",  format, ##__VA_ARGS__)
#define LOGI(format, ...) __android_log_print(ANDROID_LOG_INFO, "NATIVE",  format, ##__VA_ARGS__)


#ifdef __cplusplus
}
#endif
#endif
