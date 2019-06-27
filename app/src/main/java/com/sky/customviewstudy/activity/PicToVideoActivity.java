package com.sky.customviewstudy.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.sky.customviewstudy.R;
import com.sky.customviewstudy.utils.BitmapUtil;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import VideoHandle.EpEditor;
import VideoHandle.OnEditorListener;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PicToVideoActivity extends AppCompatActivity{

    VideoView video_view;
    private TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_to_video);
        video_view = findViewById(R.id.video_view);
        textView2 = findViewById(R.id.textView2);

        final String videoNewPath = "/storage/emulated/0/Android/data/com.smartcity/cache/video/encode" + ".mp4";
        File file = new File(videoNewPath);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        video_view.setVideoPath(videoNewPath);
        video_view.start();

//        EpEditor.execCmd(" -i "+videoNewPath,10,null);

        final String picListPath = "/storage/emulated/0/Android/data/com.smartcity/cache/video/tmpic/image%04d.jpg";
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Observable.create(new ObservableOnSubscribe<String>() {

                        @Override
                        public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                            EpEditor.pic2video(picListPath, videoNewPath, 540, 960, 1,new OnEditorListener() {
                                @Override
                                public void onSuccess() {
                                    emitter.onNext(videoNewPath);
                                }

                                @Override
                                public void onFailure() {
                                    emitter.onError(new Throwable("合成失败"));
                                }

                                @Override
                                public void onProgress(float v) {

                                }
                            });

//                            BitmapUtil.Image2Mp4(picListPath, videoNewPath, 540, 960, new OnEditorListener() {
//                                @Override
//                                public void onSuccess() {
//                                    emitter.onNext(videoNewPath);
//                                }
//
//                                @Override
//                                public void onFailure() {
//                                    emitter.onError(new Throwable("合成失败"));
//                                }
//
//                                @Override
//                                public void onProgress(float v) {
//
//                                }
//                            });
                        }
                    }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        Toast.makeText(PicToVideoActivity.this,"合成成功",Toast.LENGTH_LONG).show();
                        video_view.setVideoPath(s);
                        video_view.start();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(PicToVideoActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }
        });
    }
}
