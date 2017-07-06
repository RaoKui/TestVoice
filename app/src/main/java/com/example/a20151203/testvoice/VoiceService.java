package com.example.a20151203.testvoice;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;

/**
 * Created by 20151203 on 2017/7/5.
 */

public class VoiceService extends Service {
    private MediaPlayer mp;

    @Override
    public void onCreate() {
        // 初始化音乐资源
        try {
            System.out.println("create player");
            // 创建MediaPlayer对象
            Uri uri = Uri.parse("http://media.music.xunlei.com/resource/96/96ec5333172afedfc2dc7429f2a7ae0d.mp3");
            mp = new MediaPlayer();
            mp = MediaPlayer.create(VoiceService.this, uri);
            // mp.prepare();
            // 开始播放音乐
            mp.start();
            // 音乐播放完毕的事件处理
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                public void onCompletion(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    // 循环播放
                    try {
                        mp.start();
                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
            // 播放音乐时发生错误的事件处理
            mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                public boolean onError(MediaPlayer mp, int what, int extra) {
                    // TODO Auto-generated method stub
                    // 释放资源
                    try {
                        mp.release();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return false;
                }
            });
        } catch (IllegalStateException e) {

            e.printStackTrace();
        }
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {

        // TODO Auto-generated method stub

        super.onStart(intent, startId);

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        // 服务停止时停止播放音乐并释放资源
        mp.stop();
        mp.release();
        super.onDestroy();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
