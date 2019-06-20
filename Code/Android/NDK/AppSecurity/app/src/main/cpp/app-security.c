#include <jni.h>
#include <string.h>
#include <android/log.h>

#define LOG_TAG "C-Log"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

static char *SIGN = "308203433082022ba00302010202045bb1af15300d06092a864886f70d01010b05003052310b300906035504061"
        "3023836310b3009060355040813026764310b300906035504071302737a310b3009060355040a1302686d310b3009060355040b"
        "1302686d310f300d060355040313065a7469616e79301e170d3137313132343039323132335a170d3432313131383039323132335a3"
        "052310b3009060355040613023836310b3009060355040813026764310b300906035504071302737a310b3009060355040a13026"
        "86d310b3009060355040b1302686d310f300d060355040313065a7469616e7930820122300d06092a864886f70d010101050003820"
        "10f003082010a0282010100cefe07bd7057df45df25c772fab23c50ae39216b1b0bd56a65707d38c4603193b690634864290f541c824e"
        "fb3d3f620e4c62ead123e8e1d76de6942c557b40fcb9c8111648b3ed8afaf0100c5e873b8b2ef8c94e17a0f41b4821bc6b575d4529ae9e5"
        "1bec120a94dd991e4bbc6286878bd58f75585f598d363b08f680fce439a4684b7ed7977a229fbfcdb83b9eb103ec1306c55b7f2f7dcbd"
        "1732d0b495b4d91445dfa65e248a3b9767d2586e1ad7300c6327400890cf32da80b80a41465765dee76561df451df6d70fae01a92e6ba"
        "64ace5d486213357af55bd70ff86e93b635b3ec7f4edaaff519722735965acff0bd158f9b8df1a3ad3a23de4d0a076ca50203010001a3213"
        "01f301d0603551d0e04160414778b6d191ae71b46df9a869b815235251aa7e794300d06092a864886f70d01010b050003820101009d7d2d"
        "99e1bd16aebd1f469c9588d84d01bc1ed7a2e9f806ddca962def4cb4a5c6bc838bba2bb6516707380b87bb158eae0fb08fd35b0386076a8"
        "7ccf813f13d0ffff34df4de0715e3f33e870076588040fdd0d5fad1e7eedfeae37c788366a6f31ec1ad681dff0630ab90bcfaeb0d291cfffdd33"
        "f4b282bead6e1f11b8329d84f491610e8f49df400f2a25782231c2988587a7b05b0d908f18edb457faf78d6c737b7bce82037342fdd3b42b"
        "3d454a54b65f1f4e11476ac68155b2d3e99e13c76eacf4d57850319285b7ebe078044eecec9e92534620937c41e39c481b3123f30d5fc12878"
        "834629ba89f761d74e9e0c7f29c2ed79f23cbcf4f256eac7be239";

static jint GET_SIGNATURES = 64;

/**
 * 获取java层的Application对象
 */
static jobject getApplication(JNIEnv *env) {

    jobject application = NULL;
    jclass activity_thread_clz = (*env)->FindClass(env, "android/app/ActivityThread");

    if (activity_thread_clz != NULL) {

        jmethodID currentApplication = (*env)->GetStaticMethodID(
                env,
                activity_thread_clz,
                "currentApplication",
                "()Landroid/app/Application;");

        if (currentApplication != NULL) {
            application = (*env)->CallStaticObjectMethod(
                    env,
                    activity_thread_clz,
                    currentApplication);
        } else {
            LOGE("Cannot find method: currentApplication() in ActivityThread.");
        }
        (*env)->DeleteLocalRef(env, activity_thread_clz);
    } else {
        LOGE("Cannot find class: android.app.ActivityThread");
    }
    return application;
}


static int verifySign(JNIEnv *env) {
    // Application object
    jobject application = getApplication(env);
    if (application == NULL) {
        return JNI_ERR;
    }
    // Context(ContextWrapper) class
    jclass context_clz = (*env)->GetObjectClass(env, application);
    // getPackageManager()
    jmethodID getPackageManager = (*env)->GetMethodID(
            env,
            context_clz,
            "getPackageManager",
            "()Landroid/content/pm/PackageManager;");

    // android.content.pm.PackageManager object
    jobject package_manager = (*env)->CallObjectMethod(env, application, getPackageManager);
    // PackageManager class
    jclass package_manager_clz = (*env)->GetObjectClass(env, package_manager);
    // getPackageInfo()
    jmethodID getPackageInfo = (*env)->GetMethodID(env,
                                                   package_manager_clz,
                                                   "getPackageInfo",
                                                   "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");
    // context.getPackageName()
    jmethodID getPackageName = (*env)->GetMethodID(env,
                                                   context_clz,
                                                   "getPackageName",
                                                   "()Ljava/lang/String;");

    // call getPackageName() and cast from jobject to jstring
    jstring package_name = (jstring) ((*env)->CallObjectMethod(env, application, getPackageName));

    // PackageInfo object
    jobject package_info = (*env)->CallObjectMethod(
            env,
            package_manager,
            getPackageInfo,
            package_name,
            GET_SIGNATURES);

    // class PackageInfo
    jclass package_info_clz = (*env)->GetObjectClass(env, package_info);

    // field signatures
    jfieldID signatures_field = (*env)->GetFieldID(
            env,
            package_info_clz,
            "signatures",
            "[Landroid/content/pm/Signature;");

    jobject signatures = (*env)->GetObjectField(env, package_info, signatures_field);
    jobjectArray signatures_array = (jobjectArray) signatures;
    jobject signature0 = (*env)->GetObjectArrayElement(env, signatures_array, 0);
    jclass signature_clz = (*env)->GetObjectClass(env, signature0);

    jmethodID toCharsString = (*env)->GetMethodID(
            env,
            signature_clz,
            "toCharsString",
            "()Ljava/lang/String;");

    // call toCharsString()
    jstring signature_str = (jstring) (*env)->CallObjectMethod(env, signature0, toCharsString);

    // release
    (*env)->DeleteLocalRef(env, application);
    (*env)->DeleteLocalRef(env, context_clz);
    (*env)->DeleteLocalRef(env, package_manager);
    (*env)->DeleteLocalRef(env, package_manager_clz);
    (*env)->DeleteLocalRef(env, package_name);
    (*env)->DeleteLocalRef(env, package_info);
    (*env)->DeleteLocalRef(env, package_info_clz);
    (*env)->DeleteLocalRef(env, signatures);
    (*env)->DeleteLocalRef(env, signature0);
    (*env)->DeleteLocalRef(env, signature_clz);

    const char *sign = (*env)->GetStringUTFChars(env, signature_str, NULL);
    if (sign == NULL) {
        LOGE("分配内存失败");
        return JNI_ERR;
    }

    LOGI("应用中读取到的签名为：%s", sign);
    LOGI("native中预置的签名为：%s", SIGN);

    int result = strcmp(sign, SIGN);
    // 使用之后要释放这段内存
    (*env)->ReleaseStringUTFChars(env, signature_str, sign);
    (*env)->DeleteLocalRef(env, signature_str);
    if (result == 0) { // 签名一致
        return JNI_OK;
    }
    return JNI_ERR;
}

jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    LOGI("验证APP签名");
    JNIEnv *env = NULL;
    if ((*vm)->GetEnv(vm, (void **) &env, JNI_VERSION_1_4) != JNI_OK) {
        return JNI_ERR;
    }
    if (verifySign(env) == JNI_OK) {
        return JNI_VERSION_1_4;
    }
    LOGE("签名不一致!");
    return JNI_ERR;
}
