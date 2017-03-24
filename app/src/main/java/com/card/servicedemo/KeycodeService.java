package com.card.servicedemo;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

public class KeycodeService extends AccessibilityService {

    private static final String TAG = "KeycodeService";

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        int key = event.getKeyCode();
        switch(key){
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                Log.i(TAG, "KEYCODE_VOLUME_DOWN");
                break;
            case KeyEvent.KEYCODE_VOLUME_UP:
                Log.i(TAG, "KEYCODE_VOLUME_UP");
                break;
        }
        Log.i(TAG, "onKeyEvent:"+key);
        return true;
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public void onCreate() {
        Log.i(TAG, "RobMoney::onCreate");
        super.onCreate();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // TODO Auto-generated method stub
        Log.i(TAG, "onAccessibilityEvent: "+event.getEventType());
    }

}
