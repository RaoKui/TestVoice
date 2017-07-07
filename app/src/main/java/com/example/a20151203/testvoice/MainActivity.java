package com.example.a20151203.testvoice;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends Activity {

    private MediaPlayer mp;


    private Player player;

    private VideoView vv;

    private RelativeLayout rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vv = (VideoView) findViewById(R.id.vv);
        //设置视频控制器
        vv.setMediaController(new MediaController(this));
        rl = (RelativeLayout) findViewById(R.id.rl_vv);
        //播放完成回调
        vv.setOnCompletionListener(new MyPlayerOnCompletionListener());


//        Uri uri = Uri.parse("http://media.music.xunlei.com/resource/96/96ec5333172afedfc2dc7429f2a7ae0d.mp3");
//        mp = new MediaPlayer();
//        try {
//            mp.setDataSource("http://www.mobvcasting.com/android/audio/goodmorningandroid.mp3");
//            mp.prepare();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        mp =MediaPlayer.create(this, uri);
//        try {
////            mp.prepare();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        // 开始播放音乐
//        String url = "http://www.mobvcasting.com/android/audio/goodmorningandroid.mp3";
        String url = "http://mp3.haoduoge.com/s/2017-07-04/1499180571.mp3";
        SeekBar seekBar = (SeekBar) findViewById(R.id.sb);
        player = new Player(url, seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                this.progress = progress * player.mediaPlayer.getDuration()
                        / seekBar.getMax();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        findViewById(R.id.btn_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                player.play();
            }
        });

        findViewById(R.id.btn_pouse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mp.pause();
                player.pause();
            }
        });

        findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, RemoteControlActivity.class));
            }
        });

        findViewById(R.id.btn_ppt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PPTActivity.class));
            }
        });

        findViewById(R.id.btn_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, IJKDemoActivity.class));
                playVideo();
            }
        });

        findViewById(R.id.btn_file).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.btn_md).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,DrawerActivity.class));
            }
        });

    }

    private void playVideo() {

        rl.setVisibility(View.VISIBLE);
        Uri uri = Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");

        //设置视频路径
        vv.setVideoURI(uri);


        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //开始播放视频
                mp.start();

            }
        });

    }

    class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            Toast.makeText(MainActivity.this, "播放完成了", Toast.LENGTH_SHORT).show();
            rl.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (rl.getVisibility() == View.VISIBLE) {
            rl.setVisibility(View.GONE);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


}
