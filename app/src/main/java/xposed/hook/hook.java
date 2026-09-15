package xposed.hook;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class hook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedHelpers.findAndHookMethod("android.app.ApplicationPackageManager", lpparam.classLoader, "hasSystemFeature", String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                if (param.args[0].equals("cn.google.services") || param.args[0].equals("com.google.android.feature.services_updater"))
                    param.setResult(false);
            }
        });
        XposedHelpers.findAndHookMethod("android.app.ApplicationPackageManager", lpparam.classLoader, "hasSystemFeature", String.class, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                if (param.args[0].equals("cn.google.services") || param.args[0].equals("com.google.android.feature.services_updater"))
                    param.setResult(false);
            }
        });
        XposedHelpers.findAndHookMethod("android.app.ApplicationPackageManager", lpparam.classLoader, "getSystemAvailableFeatures", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                android.content.pm.FeatureInfo[] featureInfoList = (android.content.pm.FeatureInfo[]) param.getResult();
                java.util.List<android.content.pm.FeatureInfo> newFeatureInfoList = new java.util.ArrayList<>(featureInfoList.length);
                for (android.content.pm.FeatureInfo featureInfo : featureInfoList) {
                    if (featureInfo == null) continue;
                    if ("cn.google.services".equals(featureInfo.name)) continue;
                    if ("com.google.android.feature.services_updater".equals(featureInfo.name)) continue;
                    newFeatureInfoList.add(featureInfo);
                }
                param.setResult(newFeatureInfoList.toArray(new android.content.pm.FeatureInfo[0]));
            }
        });
    }
}
