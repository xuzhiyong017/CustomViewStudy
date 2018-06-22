#include <jni.h>
#include <string>
#include "native-lib.h"

const char *PATH;
extern "C" JNIEXPORT jstring

JNICALL
Java_com_sky_customviewstudy_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from B";
    return env->NewStringUTF(hello.c_str());
}


JNICALL void child_do_work() {

}

void child_listen_msg(){

}

void child_createSocket(){
    int listens = socket(AF_LOCAL,SOCK_STREAM,0);
    unlink(PATH);
    struct sockaddr_un addr;
    memset(&addr,0, sizeof(sockaddr_un));

}




void
Java_com_sky_customviewstudy_damon_Wathcer_createSocketServer(
        JNIEnv *env, jobject /* this */,jstring userId_) {

    const char * userId = env->GetStringUTFChars(userId_,0);

    pid_t  pid = fork();
    if(pid == 0) {
        //创建子进程成功
        child_do_work();
    }

    env->ReleaseStringUTFChars(userId_,userId);
}
