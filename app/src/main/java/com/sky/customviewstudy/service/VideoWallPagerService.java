package com.sky.customviewstudy.service;

import android.hardware.Camera;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import java.io.IOException;

/**
 * @author: xuzhiyong
 * @project: CustomViewStudy
 * @package: com.sky.customviewstudy.service
 * @description: ${DESP}
 * @date: 2018/6/13
 * @time: 10:23
 * @Email: 18971269648@163.com
 */
public class VideoWallPagerService extends WallpaperService {

    @Override
    public Engine onCreateEngine() {
        return new VideoEngine();
    }


    public class VideoEngine extends Engine{


        private boolean isInit = false;

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            initCamera();
            isInit = true;
        }




        @Override
        public void onSurfaceRedrawNeeded(SurfaceHolder holder) {
            super.onSurfaceRedrawNeeded(holder);

        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            isInit = false;
            destroyCamera();
        }


        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            if(visible){
                if(camera != null && isInit){
                    camera.startPreview();
                }
            }else{
                if(camera != null){
                    camera.stopPreview();
                }
            }
        }

        Camera camera;
        private void initCamera() {
            camera = Camera.open(0);
            Camera.Parameters parameters = camera.getParameters();
            parameters.set("orientation","portrait");
            camera.setDisplayOrientation(90);
//            camera.setParameters(parameters);
            try {
                camera.setPreviewDisplay(getSurfaceHolder());

                camera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void destroyCamera() {
            if(camera == null) return;
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }
}
