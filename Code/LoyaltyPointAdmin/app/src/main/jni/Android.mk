LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := services
LOCAL_SRC_FILES := user-api.c

include $(BUILD_SHARED_LIBRARY)