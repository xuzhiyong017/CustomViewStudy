package com.sky.customviewstudy.damon;

/**
 * @author: xuzhiyong
 * @project: CustomViewStudy
 * @package: com.sky.customviewstudy.damon
 * @description: ${DESP}
 * @date: 2018/6/22
 * @time: 16:01
 * @Email: 18971269648@163.com
 */
public class Wathcer {


    static {
        System.loadLibrary("native-lib");
    }

    public native  void createSocketServer(String userId);
    public native  void connectServer();
}
