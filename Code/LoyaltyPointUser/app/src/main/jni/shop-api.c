#include <string.h>
#include <jni.h>

jstring Java_com_thesis_dont_loyaltypointuser_models_ShopModel_getGetAllShop(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.216.164/web_service/shop/select_all_shop.php");
}

jstring Java_com_thesis_dont_loyaltypointuser_models_ShopModel_getFollowShop(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.216.164/web_service/customer_shop/add_customer_shop.php");
}
