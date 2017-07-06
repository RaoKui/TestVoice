package com.example.a20151203.testvoice;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import java.io.File;

import cn.wps.moffice.service.OfficeService;
import cn.wps.moffice.service.doc.Document;

/**
 * Created by 20151203 on 2017/7/5.
 */

public class PPTActivity extends Activity {

    private boolean mIsBound;
    private Document mDoc;
    private String path;
    private static final String TAG = "PPTActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ppt);
//        doBindService();
//        Intent intent = new Intent();
//        Bundle bundle = new Bundle();
////        bundle.putBoolean(Define.BACK_KEY_DOWN, true);//监听Back键事件,对外发送广播
//        intent.putExtras(bundle);
        findViewById(R.id.btn_chose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
            }
        });


//        String path = Environment.getExternalStorageDirectory().getPath() + "/testppt.pptx";
//        Log.i(TAG, "onCreate: " + path);
//        try {
//            mDoc = mService.openDocument(path, "", intent);//mService为绑定文档后返回的对象
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//        Intent intent = new Intent();
//        Bundle bundle = new Bundle();
//        bundle.putString("OpenMode", "Normal");
//        openFile(path);


    }

    private String OPEN_MODE = "OpenMode";
    private String SEND_CLOSE_BROAD = "SendCloseBroad";
    private String CLEAR_TRACE = "ClearTrace";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                Uri uri = data.getData();
                path = uri.getPath().toString();
                openFile(path);
//                Toast.makeText(this, "文件路径："+uri.getPath().toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    boolean openFile(String path) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(OPEN_MODE, "Normal");
        //打开模式
        bundle.putBoolean(SEND_CLOSE_BROAD, true);
        //关闭时是否发送广播
        bundle.putString("com.example.a20151203.testvoice", "com.example.a20151203.testvoice");
        //第三方应用的包名，用于对改应用合法性的验证
        bundle.putBoolean(CLEAR_TRACE, true);
        //清除打开记录
        //bundle.putBoolean(CLEAR_FILE, true);
        //关闭后删除打开文件
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setClassName("cn.wps.moffice_eng", "cn.wps.moffice.documentmanager.PreStartActivity2");

        File file = new File(path);
        if (file == null || !file.exists()) {
            Log.i(TAG, "openFile: 不存在");
            return false;
        }

        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        intent.putExtras(bundle);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static final String OFFICE_SERVICE_ACTION = "cn.wps.moffice.service.OfficeService";
    public static final String PRO_OFFICE_SERVICE_ACTION = "cn.wps.moffice.service.ProOfficeService";

    void doBindService() {
        Intent intentOfficeService = new
                Intent(PRO_OFFICE_SERVICE_ACTION);
// PRO_OFFICE_SERVICE_ACTION是绑定service的IntentFilter，之前版本提供的OFFICE_SERVICE_ACTION也可以使用，只不过为了避免和个人版的冲突，添加了一个新参数
        intentOfficeService.putExtra("DisplayView", true);
        bindService(intentOfficeService, connection, BIND_AUTO_CREATE);
    }

    void doUnbindService() {
        if (mIsBound) {
            unbindService(connection);
            mIsBound = false;
        }
    }

    private OfficeService mService;
    //获取连接实例
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = OfficeService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
    };
}
