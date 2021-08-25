//#include <conio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>

#include <iostream>
#include <string>
using namespace std;

#include "StringRead.h"

jstring   CharTojstring(JNIEnv* env,const  char* str)  
{  
    jsize len=strlen(str);  
  
    jclass clsstring=env->FindClass("java/lang/String");  
    jstring strencode=env->NewStringUTF("Big5");  
  
    jmethodID mid=env->GetMethodID(clsstring,"<init>","([BLjava/lang/String;)V");  
    jbyteArray barr=env-> NewByteArray(len);  
  
    env-> SetByteArrayRegion(barr,0,len,(jbyte*)str);  
    return (jstring)env->NewObject(clsstring,mid,barr,strencode);  
}  
JNIEXPORT jstring JNICALL Java_StringRead_Read(JNIEnv *env, jclass c1)
{
	string s;
	getline(cin,s);
    return CharTojstring(env, s.c_str());
} 
