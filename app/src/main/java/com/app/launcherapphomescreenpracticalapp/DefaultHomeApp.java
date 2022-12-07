package com.app.launcherapphomescreenpracticalapp;

import static android.content.pm.PackageManager.GET_RESOLVED_FILTER;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import java.util.List;

public class DefaultHomeApp {
    public static final String LAUNCHER_CLASS = "com.app.launcherapphomescreenpracticalapp.MainActivity";
    Activity activity;

    enum HomeApplicationCurrentStatus {
        App_IS_DEFAULT,
        OTHER_LAUNCHER_IS_DEFAULT,
        NO_DEFAULT
    }

    public DefaultHomeApp(Activity activity) {
        this.activity = activity;
    }

    public void launchapplicationHomeOrClearDefaultsDialog() {
        HomeApplicationCurrentStatus homeApplicationCurrentStatus;
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        ResolveInfo resolveActivity = this.activity.getPackageManager().resolveActivity(intent, GET_RESOLVED_FILTER);
        if ("com.app.launcherapphomescreenpracticalapp".equals(resolveActivity.activityInfo.applicationInfo.packageName) && LAUNCHER_CLASS.equals(resolveActivity.activityInfo.name)) {
            homeApplicationCurrentStatus = HomeApplicationCurrentStatus.App_IS_DEFAULT;
        }
        else if (!inResolveInfoList(resolveActivity, this.activity.getPackageManager().queryIntentActivities(intent, GET_RESOLVED_FILTER))) {
            homeApplicationCurrentStatus = HomeApplicationCurrentStatus.NO_DEFAULT;
        }
        else {
            homeApplicationCurrentStatus = HomeApplicationCurrentStatus.OTHER_LAUNCHER_IS_DEFAULT;
        }
        int i = launcher.SetDefaultLauncherHomeApplicationStatus[homeApplicationCurrentStatus.ordinal()];
        if (i == 1 || i == 2) {
            Intent intent2 = new Intent("android.intent.action.MAIN");
            intent2.addCategory("android.intent.category.HOME");
            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.activity.startActivity(intent2);
            return;
        }
        showClearDefaultsDialog(resolveActivity);
    }

    static class launcher {
        static final int[] SetDefaultLauncherHomeApplicationStatus;

        static {
            int[] iArr = new int[HomeApplicationCurrentStatus.values().length];
            SetDefaultLauncherHomeApplicationStatus = iArr;
            try {
                iArr[HomeApplicationCurrentStatus.App_IS_DEFAULT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                SetDefaultLauncherHomeApplicationStatus[HomeApplicationCurrentStatus.NO_DEFAULT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    @SuppressLint("WrongConstant")
    private void showClearDefaultsDialog(ResolveInfo resolveInfo) {
        Intent intent;
        if (this.activity.getPackageManager().resolveActivity(new Intent("android.settings.HOME_SETTINGS"), 0) == null) {
            intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", Uri.fromParts("package", resolveInfo.activityInfo.packageName, null));
        } else {
            intent = new Intent("android.settings.HOME_SETTINGS");
        }
        try {
//            intent.setFlags(276856832);
            this.activity.startActivity(intent);
        } catch (Exception unused) {
            setDefLauncher(this.activity);
        }
    }

    private boolean inResolveInfoList(ResolveInfo resolveInfo, List<ResolveInfo> list) {
        for (ResolveInfo resolveInfo2 : list) {
            if (resolveInfo2.activityInfo.name.equals(resolveInfo.activityInfo.name) && resolveInfo2.activityInfo.packageName.equals(resolveInfo.activityInfo.packageName)) {
                return true;
            }
        }
        return false;
    }

    private void setDefLauncher(Context context) {
        PackageManager packageManager = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, MainActivity.class);
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        context.startActivity(intent);
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }
}