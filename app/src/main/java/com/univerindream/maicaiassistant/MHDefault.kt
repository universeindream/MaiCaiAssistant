package com.univerindream.maicaiassistant

object MHDefault {

    val githubSolutions = arrayListOf<MCSolution>()

    val defaultMCSolutions = arrayListOf(
        MCSolution(
            name = "美团抢购 - 支付宝 - 自动送达时间",
            steps = arrayListOf(
                MCStep(
                    "打开 app",
                    condList = arrayListOf(
                        MCCond(
                            EMCCond.APP_IS_BACKGROUND,
                            MCNode(EMCNodeType.PACKAGE_NAME, packageName = "com.meituan.retail.v.android")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.LAUNCH,
                        node = MCNode(EMCNodeType.PACKAGE_NAME, packageName = "com.meituan.retail.v.android"),
                        delayRunAfter = 500,
                    )
                ),
                MCStep(
                    "点击跳过",
                    condList = arrayListOf(
                        MCCond(
                            EMCCond.EQ_CLASS_NAME,
                            MCNode(
                                EMCNodeType.CLASSNAME,
                                className = "com.meituan.retail.c.android.splash.SplashActivity"
                            )
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node = MCNode(EMCNodeType.TXT, "跳过"),
                        delayRunAfter = 500,
                    )
                ),
                MCStep(
                    "点击购物车",
                    condList = arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCNodeType.ID, "com.meituan.retail.v.android:id/img_shopping_cart")
                        ),
                        MCCond(
                            EMCCond.NODE_NOT_SELECTED,
                            MCNode(EMCNodeType.ID, "com.meituan.retail.v.android:id/img_shopping_cart")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node = MCNode(EMCNodeType.ID, "com.meituan.retail.v.android:id/img_shopping_cart"),
                        delayRunAfter = 500,
                    )
                ),
                MCStep(
                    "点击我知道了",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCNodeType.TXT, "我知道了")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node = MCNode(EMCNodeType.TXT, "我知道了"),
                    )
                ),
                MCStep(
                    "点击返回购物车",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCNodeType.TXT, "返回购物车")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node = MCNode(EMCNodeType.TXT, "返回购物车"),
                    )
                ),
                MCStep(
                    "点击结算",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCNodeType.TXT, "结算")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node = MCNode(EMCNodeType.TXT, "结算"),
                        delayRunBefore = 500,
                    ),
                ),
                MCStep(
                    "立即支付",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCNodeType.TXT, "立即支付")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node = MCNode(EMCNodeType.TXT, "立即支付"),
                        delayRunBefore = 500,
                    ),
                ),
                MCStep(
                    "自动选择送达时间",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCNodeType.TXT, "选择送达时间")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_RANDOM_NODE,
                        node = MCNode(EMCNodeType.TXT, "-"),
                    ),
                ),
                MCStep(
                    "确认支付",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCNodeType.TXT, "提交订单")
                        ),
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCNodeType.TXT, "立即支付")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node = MCNode(EMCNodeType.TXT, "确认支付"),
                        delayRunAfter = 100,
                    ),
                ),
                MCStep(
                    "展开更多支付方式",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCNodeType.TXT, "支付订单")
                        ),
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCNodeType.TXT, "展开更多支付方式")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node = MCNode(EMCNodeType.TXT, "展开更多支付方式"),
                        delayRunAfter = 100,
                    ),
                ),
                MCStep(
                    "选择支付宝支付",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCNodeType.TXT, "支付订单")
                        ),
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCNodeType.TXT, "支付宝支付")
                        ),
                        MCCond(
                            EMCCond.NODE_NOT_CHECKED,
                            MCNode(EMCNodeType.TXT, "支付宝支付")
                        ),
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node = MCNode(EMCNodeType.TXT, "支付宝支付"),
                        delayRunAfter = 100,
                    ),
                ),
                MCStep(
                    "确认支付",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCNodeType.TXT, "支付订单")
                        ),
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCNodeType.TXT, "确认支付")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node = MCNode(EMCNodeType.TXT, "确认支付"),
                        delayRunAfter = 100,
                    ),
                    isAlarm = true,
                    isManual = true
                ),
            )
        ),
        MCSolution(
            name = "美团抢购 - 微信 - 自动送达时间",
            steps = arrayListOf(
                MCStep(
                    "打开 app",
                    condList = arrayListOf(
                        MCCond(
                            EMCCond.APP_IS_BACKGROUND,
                            MCNode(EMCNodeType.PACKAGE_NAME, packageName = "com.meituan.retail.v.android")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.LAUNCH,
                        node = MCNode(EMCNodeType.PACKAGE_NAME, packageName = "com.meituan.retail.v.android"),
                        delayRunAfter = 500,
                    )
                ),
                MCStep(
                    "点击跳过",
                    condList = arrayListOf(
                        MCCond(
                            EMCCond.EQ_CLASS_NAME,
                            MCNode(
                                EMCNodeType.CLASSNAME,
                                className = "com.meituan.retail.c.android.splash.SplashActivity"
                            )
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node = MCNode(EMCNodeType.TXT, "跳过"),
                        delayRunAfter = 500,
                    )
                ),
                MCStep(
                    "点击购物车",
                    condList = arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCNodeType.ID, "com.meituan.retail.v.android:id/img_shopping_cart")
                        ),
                        MCCond(
                            EMCCond.NODE_NOT_SELECTED,
                            MCNode(EMCNodeType.ID, "com.meituan.retail.v.android:id/img_shopping_cart")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node = MCNode(EMCNodeType.ID, "com.meituan.retail.v.android:id/img_shopping_cart"),
                        delayRunAfter = 500,
                    )
                ),
                MCStep(
                    "点击我知道了",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCNodeType.TXT, "我知道了")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node = MCNode(EMCNodeType.TXT, "我知道了"),
                    )
                ),
                MCStep(
                    "点击返回购物车",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCNodeType.TXT, "返回购物车")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node = MCNode(EMCNodeType.TXT, "返回购物车"),
                    )
                ),
                MCStep(
                    "点击结算",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCNodeType.TXT, "结算")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node = MCNode(EMCNodeType.TXT, "结算"),
                        delayRunBefore = 500,
                    ),
                ),
                MCStep(
                    "立即支付",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCNodeType.TXT, "立即支付")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node = MCNode(EMCNodeType.TXT, "立即支付"),
                        delayRunBefore = 500,
                    ),
                ),
                MCStep(
                    "自动选择送达时间",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCNodeType.TXT, "选择送达时间")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_RANDOM_NODE,
                        node = MCNode(EMCNodeType.TXT, "-"),
                    ),
                ),
                MCStep(
                    "确认支付",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCNodeType.TXT, "提交订单")
                        ),
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCNodeType.TXT, "立即支付")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node = MCNode(EMCNodeType.TXT, "确认支付"),
                        delayRunAfter = 100,
                    ),
                ),
                MCStep(
                    "展开更多支付方式",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCNodeType.TXT, "支付订单")
                        ),
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCNodeType.TXT, "展开更多支付方式")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node = MCNode(EMCNodeType.TXT, "展开更多支付方式"),
                        delayRunAfter = 100,
                    ),
                ),
                MCStep(
                    "选择微信支付",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCNodeType.TXT, "支付订单")
                        ),
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCNodeType.TXT, "微信支付")
                        ),
                        MCCond(
                            EMCCond.NODE_NOT_CHECKED,
                            MCNode(EMCNodeType.TXT, "微信支付")
                        ),
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node = MCNode(EMCNodeType.TXT, "微信支付"),
                        delayRunAfter = 100,
                    ),
                ),
                MCStep(
                    "确认支付",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCNodeType.TXT, "支付订单")
                        ),
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCNodeType.TXT, "确认支付")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node = MCNode(EMCNodeType.TXT, "确认支付"),
                        delayRunAfter = 100,
                    ),
                    isAlarm = true,
                    isManual = true
                ),
            )
        ),
        MCSolution(
            name = "叮咚抢购 - 支付宝 - 自动送达时间",
            steps = arrayListOf(
                MCStep(
                    "打开 app",
                    arrayListOf(
                        MCCond(
                            EMCCond.APP_IS_BACKGROUND,
                            MCNode(EMCNodeType.PACKAGE_NAME, packageName = "com.yaya.zone")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.LAUNCH,
                        node = MCNode(EMCNodeType.PACKAGE_NAME, packageName = "com.yaya.zone"),
                        delayRunAfter = 500,
                    )
                ),
                MCStep(
                    "跳过广告",
                    condList = arrayListOf(
                        MCCond(
                            EMCCond.EQ_CLASS_NAME,
                            MCNode(EMCNodeType.CLASSNAME, className = "cn.me.android.splash.activity.SplashActivity")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node = MCNode(EMCNodeType.TXT, "跳过"),
                        delayRunAfter = 500,
                    )
                ),
                MCStep(
                    "点击购物车",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCNodeType.TXT, "购物车", className = "android.widget.TextView")
                        ),
                        MCCond(
                            EMCCond.NODE_NOT_SELECTED,
                            MCNode(EMCNodeType.TXT, "购物车", className = "android.widget.TextView")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node = MCNode(EMCNodeType.TXT, "购物车", className = "android.widget.TextView"),
                        delayRunAfter = 500,
                    )
                ),
                MCStep(
                    "去结算",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCNodeType.TXT, "去结算")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node = MCNode(EMCNodeType.TXT, "去结算"),
                        delayRunBefore = 500,
                        delayRunAfter = 100,
                    ),
                ),
                MCStep(
                    "点击重新加载",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCNodeType.TXT, "重新加载")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node = MCNode(EMCNodeType.TXT, "重新加载")
                    ),
                ),
                MCStep(
                    "关闭送达时间失效提示",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCNodeType.TXT, "选择送达时间")
                        ),
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCNodeType.TXT, "提示")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node = MCNode(EMCNodeType.TXT, "选择送达时间")
                    ),
                ),
                MCStep(
                    "自动选择送达时间",
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
                        type = EMCHandle.CLICK_RANDOM_NODE,
                        node = MCNode(EMCNodeType.TXT, "-"),
                    )
                ),
                MCStep(
                    "选择支付宝支付",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCNodeType.TXT, "确认订单")
                        ),
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCNodeType.TXT, "支付宝支付")
                        ),
                        MCCond(
                            EMCCond.NODE_NOT_CHECKED,
                            MCNode(EMCNodeType.TXT, "支付宝支付")
                        ),
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node = MCNode(EMCNodeType.TXT, "支付宝支付"),
                        delayRunAfter = 100,
                    ),
                ),
                MCStep(
                    "立即支付",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCNodeType.TXT, "立即支付")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node = MCNode(EMCNodeType.TXT, "立即支付"),
                        delayRunAfter = 100,
                    ),
                    isAlarm = true,
                    isManual = true
                ),
            )
        ),
        MCSolution(
            name = "叮咚抢购 - 微信 - 自动送达时间",
            steps = arrayListOf(
                MCStep(
                    "打开 app",
                    arrayListOf(
                        MCCond(
                            EMCCond.APP_IS_BACKGROUND,
                            MCNode(EMCNodeType.PACKAGE_NAME, packageName = "com.yaya.zone")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.LAUNCH,
                        node = MCNode(EMCNodeType.PACKAGE_NAME, packageName = "com.yaya.zone"),
                        delayRunAfter = 500,
                    )
                ),
                MCStep(
                    "跳过广告",
                    condList = arrayListOf(
                        MCCond(
                            EMCCond.EQ_CLASS_NAME,
                            MCNode(EMCNodeType.CLASSNAME, className = "cn.me.android.splash.activity.SplashActivity")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node = MCNode(EMCNodeType.TXT, "跳过"),
                        delayRunAfter = 500,
                    )
                ),
                MCStep(
                    "点击购物车",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCNodeType.TXT, "购物车", className = "android.widget.TextView")
                        ),
                        MCCond(
                            EMCCond.NODE_NOT_SELECTED,
                            MCNode(EMCNodeType.TXT, "购物车", className = "android.widget.TextView")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node = MCNode(EMCNodeType.TXT, "购物车", className = "android.widget.TextView"),
                        delayRunAfter = 500,
                    )
                ),
                MCStep(
                    "去结算",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCNodeType.TXT, "去结算")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node = MCNode(EMCNodeType.TXT, "去结算"),
                        delayRunBefore = 500,
                        delayRunAfter = 100,
                    ),
                ),
                MCStep(
                    "点击重新加载",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCNodeType.TXT, "重新加载")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node = MCNode(EMCNodeType.TXT, "重新加载")
                    ),
                ),
                MCStep(
                    "关闭送达时间失效提示",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCNodeType.TXT, "选择送达时间")
                        ),
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCNodeType.TXT, "提示")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node = MCNode(EMCNodeType.TXT, "选择送达时间")
                    ),
                ),
                MCStep(
                    "自动选择送达时间",
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
                        type = EMCHandle.CLICK_RANDOM_NODE,
                        node = MCNode(EMCNodeType.TXT, "-"),
                    )
                ),
                MCStep(
                    "选择微信支付",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCNodeType.TXT, "确认订单")
                        ),
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCNodeType.TXT, "微信支付")
                        ),
                        MCCond(
                            EMCCond.NODE_NOT_CHECKED,
                            MCNode(EMCNodeType.TXT, "微信支付")
                        ),
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node = MCNode(EMCNodeType.TXT, "微信支付"),
                        delayRunAfter = 100,
                    ),
                ),
                MCStep(
                    "立即支付",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCNodeType.TXT, "立即支付")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node = MCNode(EMCNodeType.TXT, "立即支付"),
                        delayRunAfter = 100,
                    ),
                    isAlarm = true,
                    isManual = true
                ),
            )
        ),
        MCSolution(
            name = "盒马购买",
            steps = arrayListOf(
                MCStep(
                    "打开 app",
                    arrayListOf(
                        MCCond(
                            EMCCond.APP_IS_BACKGROUND,
                            MCNode(nodeType = EMCNodeType.PACKAGE_NAME, packageName = "com.wudaokou.hippo")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.LAUNCH,
                        node = MCNode(nodeType = EMCNodeType.PACKAGE_NAME, packageName = "com.wudaokou.hippo"),
                        delayRunAfter = 500,
                    )
                ),
                MCStep(
                    "点击购物车",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCNodeType.TXT, "购物车", className = "android.widget.TextView")
                        ),
                        MCCond(
                            EMCCond.NODE_NOT_SELECTED,
                            MCNode(EMCNodeType.TXT, "购物车", className = "android.widget.TextView")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node = MCNode(EMCNodeType.TXT, "购物车", className = "android.widget.TextView"),
                        delayRunAfter = 500,
                    )
                ),
                MCStep(
                    "结算",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCNodeType.TXT, "结算")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node = MCNode(EMCNodeType.TXT, "结算"),
                        delayRunBefore = 500,
                        delayRunAfter = 100,
                    ),
                ),
                MCStep(
                    "唤起选择时间",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCNodeType.TXT, "选择时间", className = "android.widget.TextView")
                        ),
                        MCCond(
                            EMCCond.NODE_NO_EXIST,
                            MCNode(EMCNodeType.TXT, "送达")
                        ),
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE_JUST_SELF,
                        node = MCNode(EMCNodeType.TXT, "选择时间", className = "android.widget.LinearLayout"),
                    ),
                ),
                MCStep(
                    "确认选择时间",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCNodeType.TXT, "选择时间", className = "android.widget.TextView")
                        ),
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCNodeType.TXT, "送达")
                        ),
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCNodeType.TXT, "确认")
                        ),
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node = MCNode(EMCNodeType.TXT, "确认", nodeIndex = 1),
                    ),
                ),
                MCStep(
                    "提交订单",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCNodeType.TXT, "提交订单")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node = MCNode(EMCNodeType.TXT, "提交订单"),
                        delayRunAfter = 100,
                    ),
                    isAlarm = true,
                    isManual = true
                ),
            )
        ),
    )

}