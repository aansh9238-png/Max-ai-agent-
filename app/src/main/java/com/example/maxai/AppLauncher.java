package com.example.maxai;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

public class AppLauncher {

    private final Context context;

    public AppLauncher(Context context) {
        this.context = context;
    }

    public boolean launchApp(String appName) {
        String packageName = getPackageName(appName.toLowerCase());
        if (packageName == null) {
            Toast.makeText(context, "App not found: " + appName, Toast.LENGTH_SHORT).show();
            return false;
        }
        try {
            PackageManager pm = context.getPackageManager();
            Intent intent = pm.getLaunchIntentForPackage(packageName);
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                return true;
            } else {
                Toast.makeText(context, appName + " is not installed!", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    private String getPackageName(String appName) {
        if (appName.contains("whatsapp")) return "com.whatsapp";
        if (appName.contains("youtube")) return "com.google.android.youtube";
        if (appName.contains("instagram")) return "com.instagram.android";
        if (appName.contains("chrome")) return "com.android.chrome";
        if (appName.contains("gmail")) return "com.google.android.gm";
        if (appName.contains("maps")) return "com.google.android.apps.maps";
        if (appName.contains("camera")) return "com.android.camera2";
        if (appName.contains("spotify")) return "com.spotify.music";
        if (appName.contains("telegram")) return "org.telegram.messenger";
        if (appName.contains("twitter") || appName.equals("x")) return "com.twitter.android";
        if (appName.contains("facebook")) return "com.facebook.katana";
        if (appName.contains("settings")) return "com.android.settings";
        if (appName.contains("calculator")) return "com.android.calculator2";
        return null;
    }
}
