//#include <conio.h>
#include <stdlib.h>
#include "CFunction.h"

JNIEXPORT void JNICALL Java_CFunction_clearScreen(JNIEnv *env, jclass c1)
{
    system("cls");
} 

JNIEXPORT jchar JNICALL Java_CFunction_Cgetch(JNIEnv *env, jclass c1)
{
    return (jchar) getch();
} 
