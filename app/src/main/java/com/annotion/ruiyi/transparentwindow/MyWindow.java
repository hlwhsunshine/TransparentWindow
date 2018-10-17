package com.annotion.ruiyi.transparentwindow;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * Function:
 * Project:TransparentWindow
 * Date:2018/10/12
 * Created by xiaojun .
 */

public class MyWindow {
    private static MyWindow myWindow = null;
    private WindowManager windowManager;
    private View view;
    private static Context context;
    private final WindowManager.LayoutParams layoutParams;
    private boolean isShow = false;

    private MyWindow(){
        windowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = 800;
        layoutParams.height = 800;
//        layoutParams.flags =  WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        layoutParams.flags =  WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.format = PixelFormat.TRANSPARENT;
        layoutParams.alpha = 0.3f;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1 || Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }else{
            layoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        }

        view = View.inflate(context, R.layout.window_layout, null);
        view.findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeWindow();
            }
        });

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                Log.e("---------","keyCode:"+keyCode);
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });

    }
    public static MyWindow getInstence(Context appcontext){
        if (myWindow == null){
            context = appcontext;
            myWindow = new MyWindow();
        }
        return myWindow;

    }

    public void openWindow() {
        if (windowManager == null) {
            Log.e("----------","------windowManager is null!-----");
            return;
        }

        Log.e("----------","*********openWindow******");
        if (!isShow) {
            windowManager.addView(view, layoutParams);
            isShow = true;
        }

    }

    public void closeWindow(){
        if (windowManager == null)
            return;
        if (isShow) {
            windowManager.removeView(view);
            isShow = false;
        }
    }
}
