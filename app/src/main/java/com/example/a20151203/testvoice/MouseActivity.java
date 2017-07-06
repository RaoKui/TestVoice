package com.example.a20151203.testvoice;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;


/**
 * Created by 20151203 on 2017/7/6.
 */

public class MouseActivity extends Activity {

    private static float mx = 0; // 发送的鼠标移动的差值
    private static float my = 0;
    private static float lx; // 记录上次鼠标的位置
    private static float ly;
    private static float fx; // 手指第一次接触屏幕时的坐标
    private static float fy;
    private static float lbx = 0; // 鼠标左键移动初始化坐标
    private static float lby = 0;
    Handler handler = new Handler();
    Runnable leftButtonDown;
    Runnable leftButtonRealease;
    Runnable rightButtonDown;
    Runnable rightButtonRealease;

    private FrameLayout leftButton;
    private FrameLayout rightButton;

    private boolean flag = true;//屏蔽home键
    private DatagramSocket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.control);
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            Toast.makeText(MouseActivity.this, "shba", Toast.LENGTH_SHORT);
            e.printStackTrace();
        }
        initTouch();
    }

    @Override
    public void onAttachedToWindow() {
//        if (flag) {
//            this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
//        }
        super.onAttachedToWindow();
    }

    private void initTouch() {
        FrameLayout touch = (FrameLayout) this.findViewById(R.id.touch);

        // let's set up a touch listener
        touch.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent ev) {
                if (ev.getAction() == MotionEvent.ACTION_MOVE)
                    onMouseMove(ev);
                if (ev.getAction() == MotionEvent.ACTION_DOWN)
                    onMouseDown(ev);
                if (ev.getAction() == MotionEvent.ACTION_UP)
                    onMouseUp(ev);
                return true;
            }
        });

        leftButton = (FrameLayout) this.findViewById(R.id.leftButton);
        leftButton.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent ev) {
                if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                    onLeftButton("down");
                    handler.post(leftButtonDown);
                }
                if (ev.getAction() == MotionEvent.ACTION_UP) {
                    onLeftButton("release");
                    lbx = 0;
                    lby = 0;
                    handler.post(leftButtonRealease);
                }
                if (ev.getAction() == MotionEvent.ACTION_MOVE)
                    moveMouseWithSecondFinger(ev);
                return true;
            }
        });

        rightButton = (FrameLayout) this.findViewById(R.id.rightButton);
        rightButton.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent ev) {
                if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                    onRightButton("down");
                    handler.post(rightButtonDown);
                }
                if (ev.getAction() == MotionEvent.ACTION_UP) {
                    onRightButton("release");
                    handler.post(rightButtonRealease);
                }
                return true;
            }
        });

        FrameLayout middleButton = (FrameLayout) this
                .findViewById(R.id.middleButton);
        middleButton.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent ev) {
                if (ev.getAction() == MotionEvent.ACTION_DOWN)
                    onMiddleButtonDown(ev);
                if (ev.getAction() == MotionEvent.ACTION_MOVE)
                    onMiddleButtonMove(ev);
                return true;
            }

        });

        this.leftButtonDown = new Runnable() {
            public void run() {
                drawLeftButtonDown(leftButton);
            }

            private void drawLeftButtonDown(FrameLayout fl) {
//                fl.setBackgroundResource(R.drawable.zuoc);
            }
        };

        this.rightButtonDown = new Runnable() {
            public void run() {
                drawButtonDown(rightButton);
            }

            private void drawButtonDown(FrameLayout fl) {
//                fl.setBackgroundResource(R.drawable.youc);
            }
        };

        this.leftButtonRealease = new Runnable() {
            public void run() {
                drawLeftButtonRealease(leftButton);
            }

            private void drawLeftButtonRealease(FrameLayout fl) {
//                fl.setBackgroundResource(R.drawable.zuo);

            }
        };

        this.rightButtonRealease = new Runnable() {
            public void run() {
                drawButtonRealease(rightButton);
            }

            private void drawButtonRealease(FrameLayout fl) {
//                fl.setBackgroundResource(R.drawable.you);

            }
        };

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }



    private void moveMouseWithSecondFinger(MotionEvent event) {
        int count = event.getPointerCount();
        if (count == 2) {
            if (lbx == 0 && lby == 0) {
                lbx = event.getX(1);
                lby = event.getY(1);
                return;
            }
            float x = event.getX(1);
            float y = event.getY(1);
            sendMouseEvent("mouse", x - lbx, y - lby);
            lbx = x;
            lby = y;
        }
        if (count == 1) {
            lbx = 0;
            lby = 0;
        }

    }

    private void onMouseDown(MotionEvent ev) {
        lx = ev.getX(); // 当手机第一放入时 把当前坐标付给lx
        ly = ev.getY();
        fx = ev.getX();
        fy = ev.getY();
    }

    private void onMouseMove(MotionEvent ev) {
        float x = ev.getX();
        mx = x - lx; // 当前鼠标位置 - 上次鼠标的位置
        lx = x; // 把当前鼠标的位置付给lx 以备下次使用
        float y = ev.getY();
        my = y - ly;
        ly = y;
        if (mx != 0 && my != 0)
            this.sendMouseEvent("mouse", mx, my);

    }

    private void onMouseUp(MotionEvent ev) {
        if (fx == ev.getX() && fy == ev.getY()) {
            sendMessage("leftButton:down");
            sendMessage("leftButton:release");
        }

    }

    private void sendMouseEvent(String type, float x, float y) {
        String str = type + ":" + x + "," + y;
        sendMessage(str);
    }

    private void onLeftButton(String type) {
        String str = "leftButton" + ":" + type;
        sendMessage(str);

    }

    private void onRightButton(String type) {
        String str = "rightButton" + ":" + type;
        sendMessage(str);
    }

    private void sendMessage(final String str) {
        new Thread() {
            @Override
            public void run() {
                try {
                    // 首先创建一个DatagramSocket对象

                    // 创建一个InetAddree
                    InetAddress serverAddress = InetAddress.getByName(Settings.ipnum);
                    byte data[] = str.getBytes();
                    // 创建一个DatagramPacket对象，并指定要讲这个数据包发送到网络当中的哪个地址，以及端口号
                    DatagramPacket packet = new DatagramPacket(data, data.length,
                            serverAddress, Settings.scoketnum);
                    // 调用socket对象的send方法，发送数据

//            Toast.makeText(MouseActivity.this, Settings.ipnum + Settings.scoketnum, Toast.LENGTH_LONG).show();
                    socket.send(packet);
//                    // 确定接受反馈数据的缓冲存储器，即存储数据的字节数组
//                    byte[] getBuf = new byte[1024];
//
//                    // 创建接受类型的数据报
//                    DatagramPacket getPacket = new DatagramPacket(getBuf, getBuf.length);
//
//                    // 通过套接字接受数据
//                    socket.receive(getPacket);
//
//                    // 解析反馈的消息，并打印
//                    String backMes = new String(getBuf, 0, getPacket.getLength());
//                    System.out.println("接受方返回的消息：" + backMes);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();

    }

    private void onMiddleButtonDown(MotionEvent ev) {
        ly = ev.getY();

    }

    private void onMiddleButtonMove(MotionEvent ev) {
        // count++;

        float y = ev.getY();
        my = y - ly;
        ly = y;
        if (my > 3 || my < -3) { // 减少发送次数 滑轮移动慢点
            String str = "mousewheel" + ":" + my;
            sendMessage(str);
        }

    }


}
