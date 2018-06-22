//
// Created by sky on 2018/6/22.
//

#ifndef CUSTOMVIEWSTUDY_NATIVE_LIB_H
#define CUSTOMVIEWSTUDY_NATIVE_LIB_H

#endif //CUSTOMVIEWSTUDY_NATIVE_LIB_H

#include <sys/select.h>
#include <pthread.h>
#include <signal.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <sys/socket.h>
#include <sys/ioctl.h>
#include <stdlib.h>
#include <netdb.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <android/log.h>
#include <sys/un.h>
#include <errno.h>
#include <linux/signal.h>


#define LOG_TAG "tuch"
#define LOGE(...) _android_log_print(ANDROID_LOG_ERROR,LOG_TAG,_VA_ARGS_)


