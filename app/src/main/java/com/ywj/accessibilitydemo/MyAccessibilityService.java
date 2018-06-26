package com.ywj.accessibilitydemo;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import java.util.List;

/**
 * Created by weijing on 2018-06-25 14:31
 */
public class MyAccessibilityService extends AccessibilityService {
    private String TAG = MyAccessibilityService.class.getSimpleName();

    /*是否中文环境*/
    private boolean isChinese;
    /*包安装器名称*/
    private String[] installPackageNames = {"com.android.packageinstaller", "com.miui.packageinstaller",};
    private String[] keyWordsChinese = {"安装", "下一步", "确定", "打开",};
    private String[] keyWordsEnglish = {"Install", "Next", "OK", "Open",};


    @Override
    public void onCreate() {
        super.onCreate();
        String country = getResources().getConfiguration().locale.getCountry();
        if ("CN".equals(country)) {
            isChinese = true;
        }
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        ToastUtils.show(this, "onServiceConnected");
        Log.e(TAG, "onServiceConnected");

    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        String country = getResources().getConfiguration().locale.getCountry();
        if ("CN".equals(country)) {
            isChinese = true;
        }

        installApplication(event);
    }

    @Override
    public void onInterrupt() {

    }


    private void installApplication(AccessibilityEvent event) {
        if (event.getSource() != null) {
            /*验证是否是安卓包安装器*/
            if (isContains(installPackageNames, event.getPackageName().toString())) {
                /*判断语言*/
                for (String keyWord : isChinese ? keyWordsChinese : keyWordsEnglish) {
                    List<AccessibilityNodeInfo> nodeInfos = event.getSource().findAccessibilityNodeInfosByText(keyWord);
                    if (nodeInfos != null && !nodeInfos.isEmpty()) {
                        Log.e(TAG, "found Button:" + keyWord);
                        AccessibilityNodeInfo node;
                        for (int i = 0; i < nodeInfos.size(); i++) {
                            node = nodeInfos.get(i);
                            if ("android.widget.Button".equals(node.getClassName()) && node.isEnabled()) {
                                node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            }
                        }
                    }
                }


            }
        }

    }

    private boolean isContains(String[] values, String value) {
        for (String item : values) {
            if (value != null && value.equals(item)) {
                return true;
            }
        }
        return false;
    }

} 