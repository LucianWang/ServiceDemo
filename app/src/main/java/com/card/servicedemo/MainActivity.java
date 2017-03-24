package com.card.servicedemo;

import android.content.ComponentName;
import android.content.Context;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "KeycokeService";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //系统应用才可以自动开启
        initService();

        Button btn = (Button) findViewById(R.id.btn);
        btn.requestFocus();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "试试", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.btn_is).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (! isAccessibilitySettingsOn(getApplicationContext())) {
                    Toast.makeText(MainActivity.this, "没有开", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this, "开了", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initService() {
        Log.i(TAG, "initService: ");
        //注意 这里可能为空（也就是如果当前没有任何一个无障碍服务被授权的时候 就为空了  感谢评论里面指出bug的同学）
        String enabledServicesSetting = Settings.Secure.getString( getContentResolver(),Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);

        ComponentName selfComponentName = new ComponentName(getPackageName(),"com.card.servicedemo.KeycodeService");
        String flattenToString = selfComponentName.flattenToString();
        if (enabledServicesSetting==null||
                !enabledServicesSetting.contains(flattenToString)) {
            enabledServicesSetting += flattenToString;
        }
        Settings.Secure.putString(getContentResolver(),
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES,
                enabledServicesSetting);
        Settings.Secure.putInt(getContentResolver(),
                Settings.Secure.ACCESSIBILITY_ENABLED, 1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "onTouchEvent: ");
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i(TAG, "dispatchTouchEvent: ");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.i(TAG, "dispatchKeyEvent: ");
        return super.dispatchKeyEvent(event);
    }


    //是否开启
    private boolean isAccessibilitySettingsOn(Context mContext) {
        int accessibilityEnabled = 0;
        final String service = "com.card.servicedemo/com.card.servicedemo.KeycodeService";
        boolean accessibilityFound = false;
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    mContext.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
            Log.v(TAG, "accessibilityEnabled = " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG, "Error finding setting, default accessibility to not found: "
                    + e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            Log.v(TAG, "***ACCESSIBILIY IS ENABLED*** -----------------");
            String settingValue = Settings.Secure.getString(
                    mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                TextUtils.SimpleStringSplitter splitter = mStringColonSplitter;
                splitter.setString(settingValue);
                while (splitter.hasNext()) {
                    String accessabilityService = splitter.next();

                    Log.v(TAG, "-------------- > accessabilityService :: " + accessabilityService);
                    if (accessabilityService.equalsIgnoreCase(service)) {
                        Log.v(TAG, "We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }
        } else {
            Log.v(TAG, "***ACCESSIBILIY IS DISABLED***");
        }

        return accessibilityFound;
    }
}
