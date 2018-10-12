package com.annotion.ruiyi.transparentwindow;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.annotion.ruiyi.transparentwindow.service.TransparentService;
import com.annotion.ruiyi.transparentwindow.utils.PermissionUtils;


public class MainActivity extends AppCompatActivity {

    private TransparentService myService = null;
    private ServiceConnection connection;
    private View view;
    private WindowManager windowManager;
    private MyWindow myWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("--------:", "MainActivity进程ID："+android.os.Process.myPid());
//        bindService();
        beginService();
        myWindow = MyWindow.getInstence(this);
        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (PermissionUtils.checkFloatPermission(MainActivity.this)){
                    myWindow.openWindow();
                }else {
                    startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:"+getPackageName())),1);
                }
            }
        });

    }

    private void beginService() {
        startService(new Intent(this,TransparentService.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("--------:","回调结果:"+resultCode);
        if (requestCode == 1 && resultCode == RESULT_OK){
            if (PermissionUtils.checkFloatPermission(MainActivity.this)){
                myWindow.openWindow();
            }
        }
    }

    public void bindService(){
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                myService = ((TransparentService.MyBinder) service).getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };
        Intent i = new Intent(this, TransparentService.class);
        startService(i);
        bindService(i, connection, BIND_AUTO_CREATE);

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("--------:","onStop");
        if (PermissionUtils.checkFloatPermission(MainActivity.this)){
            Log.e("--------:","onStop-openWindow");
//            myService.openWindow(this);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("--------:","onDestroy");
//        unbindService(connection);
//        unregisterReceiver(receiver);
    }

    public void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();

    }
}
