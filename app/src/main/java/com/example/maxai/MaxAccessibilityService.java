package com.example.maxai;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class MaxAccessibilityService extends AccessibilityService {

    private static MaxAccessibilityService instance;
    private static String lastScreenText = "";

    public static MaxAccessibilityService getInstance() {
        return instance;
    }

    public static String getLastScreenText() {
        return lastScreenText;
    }

    @Override
    public void onServiceConnected() {
        instance = this;
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
                | AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        info.flags = AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS;
        info.notificationTimeout = 100;
        setServiceInfo(info);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo root = getRootInActiveWindow();
        if (root != null) {
            StringBuilder sb = new StringBuilder();
            readNode(root, sb);
            lastScreenText = sb.toString().trim();
        }
    }

    private void readNode(AccessibilityNodeInfo node, StringBuilder sb) {
        if (node == null) return;
        if (node.getText() != null && node.getText().length() > 0) {
            sb.append(node.getText()).append(" ");
        }
        for (int i = 0; i < node.getChildCount(); i++) {
            readNode(node.getChild(i), sb);
        }
    }

    @Override
    public void onInterrupt() {
        instance = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        instance = null;
    }
}
