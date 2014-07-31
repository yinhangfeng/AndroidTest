#include <jni.h>
#include <android/log.h>
#include <string>
#include "com_example_shapelibtest_JniTest.h"

using namespace std;

#ifdef __cplusplus
extern "C" {
#endif
JNIEXPORT void JNICALL Java_com_example_shapelibtest_JniTest_helloWorld(
		JNIEnv *env, jclass clazz, jstring jstr) {
	const char *str = env->GetStringUTFChars(jstr, 0);
	if (str == NULL)
		__android_log_print(ANDROID_LOG_ERROR, "TAG", "Error....");
	__android_log_print(ANDROID_LOG_INFO, "TAG", "string From Java To C : %s",
			str);
	env->ReleaseStringUTFChars(jstr, str);
}
#ifdef __cplusplus
}
#endif
