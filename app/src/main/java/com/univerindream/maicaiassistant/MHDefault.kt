package com.univerindream.maicaiassistant

object MHDefault {

    val defaultMCSolutions = arrayListOf(
        MCSolution(
            name = "美团抢购 - 自动选择送达时间 - 支付宝",
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
                        delay = 500,
                        node =
                        MCNode(EMCNodeType.PACKAGE_NAME, packageName = "com.meituan.retail.v.android"),

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
                        delay = 500,
                        node = MCNode(EMCNodeType.TXT, "跳过")
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
                        delay = 500,
                        node = MCNode(EMCNodeType.ID, "com.meituan.retail.v.android:id/img_shopping_cart")
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
                        node =
                        MCNode(EMCNodeType.TXT, "我知道了"),

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
                        node =
                        MCNode(EMCNodeType.TXT, "返回购物车"),

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
                        node = MCNode(EMCNodeType.TXT, "结算")
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
                        delay = 100,
                        node = MCNode(EMCNodeType.TXT, "立即支付")
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
                        node =
                        MCNode(EMCNodeType.TXT, "-"),

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
                        delay = 100,
                        node = MCNode(EMCNodeType.TXT, "确认支付")
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
                        delay = 100,
                        node = MCNode(EMCNodeType.TXT, "展开更多支付方式")
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
                        delay = 100,
                        node = MCNode(EMCNodeType.TXT, "支付宝支付")
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
                        delay = 100,
                        node = MCNode(EMCNodeType.TXT, "确认支付")
                    ),
                    isAlarm = true,
                    isManual = true
                ),
            )
        ),
        MCSolution(
            name = "美团抢购 - 自动选择送达时间 - 微信",
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
                        delay = 500,
                        node =
                        MCNode(EMCNodeType.PACKAGE_NAME, packageName = "com.meituan.retail.v.android"),

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
                        delay = 500,
                        node = MCNode(EMCNodeType.TXT, "跳过")
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
                        delay = 500,
                        node = MCNode(EMCNodeType.ID, "com.meituan.retail.v.android:id/img_shopping_cart")
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
                        node =
                        MCNode(EMCNodeType.TXT, "我知道了"),

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
                        node =
                        MCNode(EMCNodeType.TXT, "返回购物车"),

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
                        node = MCNode(EMCNodeType.TXT, "结算")
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
                        delay = 100,
                        node = MCNode(EMCNodeType.TXT, "立即支付")
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
                        node =
                        MCNode(EMCNodeType.TXT, "-"),

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
                        delay = 100,
                        node = MCNode(EMCNodeType.TXT, "确认支付")
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
                        delay = 100,
                        node = MCNode(EMCNodeType.TXT, "展开更多支付方式")
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
                        delay = 100,
                        node = MCNode(EMCNodeType.TXT, "微信支付")
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
                        delay = 100,
                        node = MCNode(EMCNodeType.TXT, "确认支付")
                    ),
                    isAlarm = true,
                    isManual = true
                ),
            )
        ),
        MCSolution(
            name = "叮咚抢购",
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
                        delay = 500,
                        node = MCNode(EMCNodeType.PACKAGE_NAME, packageName = "com.yaya.zone")
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
                        delay = 500,
                        node = MCNode(EMCNodeType.TXT, "跳过")
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
                        delay = 500,
                        node =
                        MCNode(EMCNodeType.TXT, "购物车", className = "android.widget.TextView")

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
                        delay = 100,
                        node = MCNode(EMCNodeType.TXT, "去结算")
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
                        node =
                        MCNode(EMCNodeType.TXT, "-"),

                        )
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
                        delay = 100,
                        node = MCNode(EMCNodeType.TXT, "立即支付")
                    ),
                    isAlarm = true,
                    isManual = true
                ),
            )
        ),
        MCSolution(
            name = "美团正常购买 - 支付宝",
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
                        delay = 500,
                        node =
                        MCNode(EMCNodeType.PACKAGE_NAME, packageName = "com.meituan.retail.v.android"),

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
                        delay = 500,
                        node = MCNode(EMCNodeType.TXT, "跳过")
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
                        delay = 500,
                        node = MCNode(EMCNodeType.ID, "com.meituan.retail.v.android:id/img_shopping_cart")
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
                        node = MCNode(EMCNodeType.TXT, "结算")
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
                        delay = 100,
                        node = MCNode(EMCNodeType.TXT, "立即支付")
                    ),
                ),
                MCStep(
                    "选择送达时间",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCNodeType.TXT, "选择送达时间")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_RANDOM_NODE,
                        node =
                        MCNode(EMCNodeType.TXT, "-"),

                        )
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
                        delay = 100,
                        node = MCNode(EMCNodeType.TXT, "确认支付")
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
                        delay = 100,
                        node = MCNode(EMCNodeType.TXT, "展开更多支付方式")
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
                        delay = 100,
                        node = MCNode(EMCNodeType.TXT, "支付宝支付")
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
                        delay = 100,
                        node = MCNode(EMCNodeType.TXT, "确认支付")
                    ),
                    isAlarm = true,
                    isManual = true
                ),
            )
        ),
        MCSolution(
            name = "美团正常购买 - 微信",
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
                        delay = 500,
                        node =
                        MCNode(EMCNodeType.PACKAGE_NAME, packageName = "com.meituan.retail.v.android"),

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
                        delay = 500,
                        node = MCNode(EMCNodeType.TXT, "跳过")
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
                        delay = 500,
                        node = MCNode(EMCNodeType.ID, "com.meituan.retail.v.android:id/img_shopping_cart")
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
                        node = MCNode(EMCNodeType.TXT, "结算")
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
                        delay = 100,
                        node = MCNode(EMCNodeType.TXT, "立即支付")
                    ),
                ),
                MCStep(
                    "选择送达时间",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCNodeType.TXT, "选择送达时间")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_RANDOM_NODE,
                        node =
                        MCNode(EMCNodeType.TXT, "-"),

                        )
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
                        delay = 100,
                        node = MCNode(EMCNodeType.TXT, "确认支付")
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
                        delay = 100,
                        node = MCNode(EMCNodeType.TXT, "展开更多支付方式")
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
                        delay = 100,
                        node = MCNode(EMCNodeType.TXT, "微信支付")
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
                        delay = 100,
                        node = MCNode(EMCNodeType.TXT, "确认支付")
                    ),
                    isAlarm = true,
                    isManual = true
                ),
            )
        ),
        MCSolution(
            name = "叮咚正常购买",
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
                        delay = 500,
                        node = MCNode(EMCNodeType.PACKAGE_NAME, packageName = "com.yaya.zone")
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
                        delay = 500,
                        node = MCNode(EMCNodeType.TXT, "跳过")
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
                        delay = 500,
                        node =
                        MCNode(EMCNodeType.TXT, "购物车", className = "android.widget.TextView")

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
                        delay = 100,
                        node = MCNode(EMCNodeType.TXT, "去结算")
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
                        node =
                        MCNode(EMCNodeType.TXT, "-"),

                        )
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
                        delay = 100,
                        node = MCNode(EMCNodeType.TXT, "立即支付")
                    ),
                    isAlarm = true,
                    isManual = true
                ),
            )
        ),
        MCSolution(
            name = "盒马正常购买",
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
                        delay = 500,
                        node = MCNode(nodeType = EMCNodeType.PACKAGE_NAME, packageName = "com.wudaokou.hippo")
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
                        delay = 500,
                        node =
                        MCNode(EMCNodeType.TXT, "购物车", className = "android.widget.TextView")

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
                        delay = 100,
                        node = MCNode(EMCNodeType.TXT, "结算")
                    ),
                ),
                MCStep(
                    "确认选择时间",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCNodeType.TXT, "选择时间", className = "android.widget.LinearLayout")
                        ),
                        MCCond(
                            EMCCond.NODE_VISIBLE,
                            MCNode(EMCNodeType.TXT, "选择时间", className = "android.widget.LinearLayout")
                        ),
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node =
                        MCNode(EMCNodeType.TXT, "确认"),

                        )
                ),
                MCStep(
                    "点击选择时间",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCNodeType.TXT, "选择时间", className = "android.widget.TextView")
                        ),
                        MCCond(
                            EMCCond.NODE_VISIBLE,
                            MCNode(EMCNodeType.TXT, "选择时间", className = "android.widget.TextView")
                        ),
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node =
                        MCNode(EMCNodeType.TXT, "选择时间"),

                        )
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
                        delay = 100,
                        node = MCNode(EMCNodeType.TXT, "提交订单")
                    ),
                    isAlarm = true,
                    isManual = true
                ),
            )
        ),
    )

}