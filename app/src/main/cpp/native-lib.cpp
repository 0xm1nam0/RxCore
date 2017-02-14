#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_github_weiss_example_ui_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Core from C++";
    return env->NewStringUTF(hello.c_str());
}
