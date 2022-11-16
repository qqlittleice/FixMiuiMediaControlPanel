package com.yuk.fixMiuiMediaControlPanel

import com.github.kyuubiran.ezxhelper.init.EzXHelperInit
import com.yuk.fixMiuiMediaControlPanel.ktx.getObjectField
import com.yuk.fixMiuiMediaControlPanel.ktx.setObjectField
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class XposedInit : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        when (lpparam.packageName) {
            "com.android.systemui" -> {
                try {
                    EzXHelperInit.initHandleLoadPackage(lpparam)
                    XposedHelpers.findAndHookMethod("com.android.systemui.statusbar.notification.mediacontrol.MiuiMediaControlPanel",
                        lpparam.classLoader,
                        "setArtwork",
                        XposedHelpers.findClass("com.android.systemui.media.MediaData", lpparam.classLoader),
                        object : XC_MethodHook() {
                            override fun afterHookedMethod(param: MethodHookParam) {
                                param.thisObject.setObjectField("mCurrentKey", "")
                                XposedBridge.log("FixMiuiMediaControlPanel: Hook SystemUI 成功")
                            }
                        })
                } catch (t: Throwable) {
                    XposedBridge.log(t)
                }
            }
            else -> return
        }
    }
}