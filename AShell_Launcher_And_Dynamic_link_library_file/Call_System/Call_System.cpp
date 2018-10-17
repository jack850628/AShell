//#include <conio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include "AShell_CallSystem.h"

char* jstring_to_char(JNIEnv *env,jstring jstr){
		char* rtn = NULL;
		jclass clsstring = env->FindClass("java/lang/String");
		jstring strencode = env->NewStringUTF("Big5");//¤å¦r½s½X 
		jmethodID mid = env->GetMethodID(clsstring, "getBytes", "(Ljava/lang/String;)[B");
		jbyteArray barr= (jbyteArray)env->CallObjectMethod(jstr, mid, strencode);
		jsize alen = env->GetArrayLength(barr);
		jbyte* ba = env->GetByteArrayElements(barr, JNI_FALSE);
		if (alen > 0)
		{
		rtn = (char*)malloc(alen + 1);
		memcpy(rtn, ba, alen);
		rtn[alen] = 0;
		}
		env->ReleaseByteArrayElements(barr, ba, 0);
		return rtn;
}
JNIEXPORT jint JNICALL Java_AShell_CallSystem_System(JNIEnv *env, jclass c1, jstring arg)
{
	/*const char *str = (*env)->GetStringUTFChars(env, arg, 0);
    system(str);*/
    return (jint) system(jstring_to_char(env,arg));
} 

JNIEXPORT jint JNICALL Java_AShell_CallSystem_chdir(JNIEnv *env, jclass c1, jstring arg)
{
	/*const char *str = (*env)->GetStringUTFChars(env, arg, 0);
    chdir(str);*/
    return (jint) chdir(jstring_to_char(env,arg));
} 
