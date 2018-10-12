package com.annotion.ruiyi.transparentwindow;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import static android.content.Context.WINDOW_SERVICE;

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
        windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = 800;
        layoutParams.height = 800;
        layoutParams.flags =  WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL ;
        layoutParams.format = PixelFormat.TRANSPARENT;
        layoutParams.alpha = 0.3f;
//        layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;

        view = View.inflate(context, R.layout.window_layout, null);
        view.findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeWindow();
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
        if (context == null)
            throw new RuntimeException("context is null!");
        if (!isShow) {
            windowManager.addView(view, layoutParams);
            isShow = true;
        }

    }

    public void closeWindow(){
        windowManager.removeView(view);
        isShow = false;
    }
}
