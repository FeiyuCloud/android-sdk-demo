package com.feiyucloud.open.demo;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionUtil {
    private String[] permissions;
    private int requestCode;
    private Object object;

    private PermissionUtil(Object object) {
        this.object = object;
    }

    public static boolean isOverAndroidM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean hasPermission(Context context, String permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        return (ContextCompat.checkSelfPermission(context, permission) ==
                PackageManager.PERMISSION_GRANTED);
    }

    public static boolean hasPermission(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        for (String permission : permissions) {
            boolean hasPermission = (ContextCompat.checkSelfPermission(context, permission) ==
                    PackageManager.PERMISSION_GRANTED);
            if (!hasPermission) {
                return false;
            }
        }
        return true;
    }

    public static PermissionUtil with(Activity activity) {
        return new PermissionUtil(activity);
    }

    public static PermissionUtil with(android.support.v4.app.Fragment fragment) {
        return new PermissionUtil(fragment);
    }

    public static PermissionUtil with(android.app.Fragment fragment) {
        return new PermissionUtil(fragment);
    }

    public PermissionUtil requestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    public PermissionUtil permissions(String... permissions) {
        this.permissions = permissions;
        return this;
    }

    public void request() {
        if (isOverAndroidM()) {
            List<String> deniedPermissions = findDeniedPermissions(getActivity(object), permissions);
            if (deniedPermissions.size() > 0) {
                ActivityCompat.requestPermissions(getActivity(object), permissions, requestCode);
            }
        }
    }

    private static List<String> findDeniedPermissions(Activity activity, String... permission) {
        List<String> denyPermissions = new ArrayList<>();
        if (isOverAndroidM()) {
            for (String value : permission) {
                if (activity.checkSelfPermission(value) != PackageManager.PERMISSION_GRANTED) {
                    denyPermissions.add(value);
                }
            }
        }
        return denyPermissions;
    }

    private static Activity getActivity(Object obj) {
        if (obj instanceof Activity) {
            return (Activity) obj;
        } else if (obj instanceof android.support.v4.app.Fragment) {
            return ((android.support.v4.app.Fragment) obj).getActivity();
        } else if (obj instanceof android.app.Fragment) {
            return ((android.app.Fragment) obj).getActivity();
        } else {
            throw new IllegalArgumentException("The " + obj.getClass().getName() + " is not support.");
        }
    }

}

