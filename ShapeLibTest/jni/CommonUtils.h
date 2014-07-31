/*
 * CommonUtils.h
 *
 *  Created on: 2014年7月30日
 *      Author: dmd
 */

#ifndef COMMONUTILS_H_
#define COMMONUTILS_H_
#include <jni.h>
#include <android/log.h>

#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO, "native-ShapLibTest", __VA_ARGS__))
#define LOGW(...) ((void)__android_log_print(ANDROID_LOG_WARN, "native-ShapLibTest", __VA_ARGS__))
#define LOGE(...) ((void)__android_log_print(ANDROID_LOG_ERROR, "native-ShapLibTest", __VA_ARGS__))

class CommonUtils {
};
#endif /* COMMONUTILS_H_ */
