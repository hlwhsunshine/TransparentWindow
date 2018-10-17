package com.annotion.ruiyi.transparentwindow.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.annotion.ruiyi.transparentwindow.FloatWindowManager;
import com.annotion.ruiyi.transparentwindow.MyWindow;

public class TransparentService extends Service {

    private TransparentService.TransparentReceiver receiver;

    public TransparentService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("--------:", "service-onStartCommand");


        return super.onStartCommand(intent, flags, startId);
    }

    public class MyBinder extends Binder {
        public TransparentService getService() {
            return TransparentService.this;
        }
    }

    public void openWindow(Context context) {
        MyWindow.getInstence(context).openWindow();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("--------:", "service-onDestroy");
        unregisterReceiver(receiver);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("--------:", "service-onCreate");
//        myWindow = MyWindow.getInstence(this);
        initRecevice();

    }

    private void initRecevice() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        receiver = new TransparentService.TransparentReceiver();
        this.registerReceiver(receiver, filter);
    }


    class TransparentReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_SCREEN_ON.equals(action)) {
                // 开屏
                Log.i("------", "onReceive: ACTION_SCREEN_ON");
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                // 锁屏
                Log.i("------", "onReceive: ACTION_SCREEN_OFF");
            } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                // 解锁
                Log.i("------", "onReceive: ACTION_USER_PRESENT");
                FloatWindowManager.getInstance().applyOrShowFloatWindow(TransparentService.this);

            }
        }
    }
}
