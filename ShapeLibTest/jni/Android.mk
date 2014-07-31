LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE := ShapLibTest
LOCAL_SRC_FILES := com_example_shapelibtest_JniTest.cpp com_example_shapelibtest_ShapLibTest.cpp shapelib/dbfopen.c shapelib/shpopen.c shapelib/safileio.c shapelib/shptree.c
#LOCAL_C_INCLUDES := /Applications/ndk/sources/cxx-stl/stlport/stlport
LOCAL_LDLIBS+= -llog
include $(BUILD_SHARED_LIBRARY)