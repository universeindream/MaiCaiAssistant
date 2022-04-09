package com.univerindream.maicaiassistant

import com.blankj.utilcode.util.ResourceUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object MHConfig {

    const val MALL_HELP_CHANNEL_ID = "mallhelp_1"

    const val DD_PACKAGE_NAME = "com.yaya.zone"
    const val DD_CLASS_HOME = "com.yaya.zone.home.HomeActivity"
    const val DD_CLASS_PAY = "cn.me.android.cart.activity.WriteOrderActivity"
    const val DD_Tab_Home = ""
    const val DD_Tab_Cart = ""

    const val MT_PACKAGE_NAME = "com.meituan.retail.v.android"
    const val MT_CLASS_HOME = "com.meituan.retail.c.android.newhome.newmain.NewMainActivity"
    const val MT_CLASS_PAY = "com.meituan.retail.c.android.mrn.mrn.MallMrnActivity"
    const val MT_Tab_Home = "com.meituan.retail.v.android:id/img_home"
    const val MT_Tab_Cart = "com.meituan.retail.v.android:id/img_shopping_cart"
    const val MT_Tab_Cookbook = "com.meituan.retail.v.android:id/img_cookbook"

    var mtSteps: List<MCStep> = Gson().fromJson<List<MCStep>>(
        MHData.stepsJsonMeiTuan,
        object : TypeToken<ArrayList<MCStep>>() {}.type
    )

    var ddSteps: List<MCStep> = Gson().fromJson<List<MCStep>>(
        MHData.stepsJsonDingDong,
        object : TypeToken<ArrayList<MCStep>>() {}.type
    )

    fun getSteps(): List<MCStep> = when (MHData.buyPlatform) {
        1 -> mtSteps
        else -> ddSteps
    }

    fun setDefaultStepsData() {
        when (MHData.buyPlatform) {
            1 -> MHData.stepsJsonMeiTuan = ResourceUtils.readRaw2String(R.raw.mt_steps)
            else -> MHData.stepsJsonDingDong = ResourceUtils.readRaw2String(R.raw.dd_steps)
        }
    }

/*    val MeiTuanSteps = arrayListOf(
        MCStep(
            "openApp",
            condList = arrayListOf(
                MCCond(
                    EMCCond.APP_IS_BACKGROUND,
                    MCNode(packageName = "com.meituan.retail.v.android")
                )
            ),
            handle = MCHandle(
                type = EMCHandle.LAUNCH,
                delay = 500,
                nodes = arrayListOf(
                    MCNode(packageName = "com.meituan.retail.v.android"),
                )
            )
        ),
        MCStep(
            "clickSkip",
            condList = arrayListOf(
                MCCond(
                    EMCCond.EQ_CLASS_NAME,
                    MCNode(className = "com.meituan.retail.c.android.splash.SplashActivity")
                )
            ),
            handle = MCHandle(
                type = EMCHandle.CLICK_NODE,
                delay = 500,
                nodes = arrayListOf(MCNode(EMCNodeType.TXT, "跳过"))
            )
        ),
        MCStep(
            "clickTabCar",
            condList = arrayListOf(
                MCCond(
                    EMCCond.NODE_IS_UNSELECTED,
                    MCNode(EMCNodeType.ID, "com.meituan.retail.v.android:id/img_shopping_cart")
                )
            ),
            handle = MCHandle(
                type = EMCHandle.CLICK_NODE,
                delay = 500,
                nodes = arrayListOf(MCNode(EMCNodeType.ID, "com.meituan.retail.v.android:id/img_shopping_cart"))
            )
        ),
        MCStep(
            "clickBtnIKnow",
            arrayListOf(
                MCCond(
                    EMCCond.NODE_CAN_CLICK,
                    MCNode(EMCNodeType.TXT, "我知道了")
                )
            ),
            handle = MCHandle(
                type = EMCHandle.CLICK_NODE,
                nodes = arrayListOf(
                    MCNode(EMCNodeType.TXT, "我知道了"),
                )
            )
        ),
        MCStep(
            "clickBtnBackShoppingCar",
            arrayListOf(
                MCCond(
                    EMCCond.NODE_CAN_CLICK,
                    MCNode(EMCNodeType.TXT, "返回购物车")
                )
            ),
            handle = MCHandle(
                type = EMCHandle.CLICK_NODE,
                nodes = arrayListOf(
                    MCNode(EMCNodeType.TXT, "返回购物车"),
                )
            )
        ),
        MCStep(
            "clickBtnSettle",
            arrayListOf(
                MCCond(
                    EMCCond.NODE_CAN_CLICK,
                    MCNode(EMCNodeType.TXT, "结算")
                )
            ),
            handle = MCHandle(
                type = EMCHandle.CLICK_NODE,
                nodes = arrayListOf(MCNode(EMCNodeType.TXT, "结算"))
            ),
        ),
        MCStep(
            "chooseDeliverTime",
            arrayListOf(
                MCCond(
                    EMCCond.NODE_EXIST,
                    MCNode(EMCNodeType.TXT, "选择送达时间")
                )
            ),
            handle = MCHandle(
                type = EMCHandle.CLICK_SCOPE_RANDOM_NODE,
                nodes = arrayListOf(
                    MCNode(EMCNodeType.TXT, "09:35-10:30"),
                    MCNode(EMCNodeType.TXT, "10:30-14:30"),
                    MCNode(EMCNodeType.TXT, "14:30-18:30"),
                    MCNode(EMCNodeType.TXT, "18:30-22:30")
                )
            )
        ),
        MCStep(
            "clickBtnPay",
            arrayListOf(
                MCCond(
                    EMCCond.NODE_EXIST,
                    MCNode(EMCNodeType.TXT, "支付")
                ),
                MCCond(
                    EMCCond.NODE_CAN_CLICK,
                    MCNode(EMCNodeType.TXT, "支付")
                )
            ),
            handle = MCHandle(
                type = EMCHandle.CLICK_NODE,
                delay = 100,
                nodes = arrayListOf(MCNode(EMCNodeType.TXT, "支付"))
            ),
        ),
    )

    val DingDongSteps = arrayListOf(
        MCStep(
            "openApp",
            arrayListOf(
                MCCond(
                    EMCCond.APP_IS_BACKGROUND,
                    MCNode(packageName = "com.yaya.zone")
                )
            ),
            handle = MCHandle(
                type = EMCHandle.LAUNCH,
                delay = 500,
                nodes = arrayListOf(MCNode(packageName = "com.yaya.zone"))
            )
        ),
        MCStep(
            "clickSkip",
            condList = arrayListOf(
                MCCond(
                    EMCCond.EQ_CLASS_NAME,
                    MCNode(className = "cn.me.android.splash.activity.SplashActivity")
                )
            ),
            handle = MCHandle(
                type = EMCHandle.CLICK_NODE,
                delay = 500,
                nodes = arrayListOf(MCNode(EMCNodeType.TXT, "跳过"))
            )
        ),
        MCStep(
            "clickTabCar",
            arrayListOf(
                MCCond(
                    EMCCond.EQ_CLASS_NAME,
                    MCNode(className = "com.yaya.zone.home.HomeActivity")
                ),
                MCCond(
                    EMCCond.NODE_IS_UNSELECTED,
                    MCNode(EMCNodeType.TXT, "购物车")
                )
            ),
            handle = MCHandle(
                type = EMCHandle.CLICK_NODE,
                delay = 500,
                nodes = arrayListOf(MCNode(EMCNodeType.TXT, "购物车"))
            )
        ),
        MCStep(
            "clickBtnSettle",
            arrayListOf(
                MCCond(
                    EMCCond.NODE_CAN_CLICK,
                    MCNode(EMCNodeType.TXT, "去结算")
                )
            ),
            handle = MCHandle(
                type = EMCHandle.CLICK_NODE,
                delay = 100,
                nodes = arrayListOf(MCNode(EMCNodeType.TXT, "去结算"))
            ),
        ),
        MCStep(
            "clickBtnPay",
            arrayListOf(
                MCCond(
                    EMCCond.NODE_CAN_CLICK,
                    MCNode(EMCNodeType.TXT, "立即支付")
                )
            ),
            handle = MCHandle(
                type = EMCHandle.CLICK_NODE,
                delay = 100,
                nodes = arrayListOf(MCNode(EMCNodeType.TXT, "立即支付"))
            ),
        ),
        MCStep(
            "clickBtnReload",
            arrayListOf(
                MCCond(
                    EMCCond.NODE_CAN_CLICK,
                    MCNode(EMCNodeType.TXT, "重新加载", null)
                )
            ),
            handle = MCHandle(
                type = EMCHandle.CLICK_NODE,
                nodes = arrayListOf(MCNode(EMCNodeType.TXT, "重新加载", null))
            ),
        ),
        MCStep(
            "chooseDeliverTime",
            arrayListOf(
                MCCond(
                    EMCCond.NODE_EXIST,
                    MCNode(EMCNodeType.TXT, "选择送达时间")
                ),
                MCCond(
                    EMCCond.NODE_NOT_CLICK,
                    MCNode(EMCNodeType.TXT, "选择送达时间")
                )
            ),
            handle = MCHandle(
                type = EMCHandle.CLICK_SCOPE_RANDOM_NODE,
                nodes = arrayListOf(
                    MCNode(EMCNodeType.TXT, "06:30-10:30"),
                    MCNode(EMCNodeType.TXT, "10:30-14:30"),
                    MCNode(EMCNodeType.TXT, "14:30-18:30"),
                    MCNode(EMCNodeType.TXT, "18:30-22:30")
                )
            )
        ),
    )*/

}