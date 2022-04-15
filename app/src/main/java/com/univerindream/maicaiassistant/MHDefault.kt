package com.univerindream.maicaiassistant

object MHDefault {

    val defaultMHSolutions = arrayListOf(
        MHSolution(
            name = "每日优鲜购买",
            steps = arrayListOf(
                MCStep(
                    "打开每日优鲜",
                    arrayListOf(
                        MCCond(
                            EMCCond.APP_IS_BACKGROUND,
                            MCNode(EMCSearch.PACKAGE_NAME, packageName = "cn.missfresh.application"),
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.LAUNCH,
                        delay = 500,
                        node = MCNode(EMCSearch.PACKAGE_NAME, packageName = "cn.missfresh.application")
                    ),

                    ),
                MCStep(
                    "跳过开屏广告",
                    condList = arrayListOf(
                        MCCond(
                            EMCCond.EQ_CLASS_NAME,
                            MCNode(EMCSearch.CLASSNAME, className = "cn.missfresh.module.main.view.SplashActivity"),
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 500,
                        node = MCNode(EMCSearch.TXT, "跳过")
                    )

                ),
                MCStep(
                    "点击购物车",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCSearch.TXT, "购物车", className = "android.widget.TextView")
                        ),
                        MCCond(
                            EMCCond.NODE_NOT_SELECTED,
                            MCNode(EMCSearch.TXT, "购物车", className = "android.widget.TextView")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 500,
                        node =
                        MCNode(EMCSearch.TXT, "购物车", className = "android.widget.TextView")

                    )
                ),
                MCStep(
                    "去结算",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCSearch.TXT, "去结算")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 100,
                        node = MCNode(EMCSearch.TXT, "去结算")
                    ),
                ),
            )
        ),
        MHSolution(
            name = "美团抢购 - 自动选择送达时间 - 支付宝",
            steps = arrayListOf(
                MCStep(
                    "打开 app",
                    condList = arrayListOf(
                        MCCond(
                            EMCCond.APP_IS_BACKGROUND,
                            MCNode(EMCSearch.PACKAGE_NAME, packageName = "com.meituan.retail.v.android")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.LAUNCH,
                        delay = 500,
                        node =
                        MCNode(EMCSearch.PACKAGE_NAME, packageName = "com.meituan.retail.v.android"),

                        )
                ),
                MCStep(
                    "点击跳过",
                    condList = arrayListOf(
                        MCCond(
                            EMCCond.EQ_CLASS_NAME,
                            MCNode(
                                EMCSearch.CLASSNAME,
                                className = "com.meituan.retail.c.android.splash.SplashActivity"
                            )
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 500,
                        node = MCNode(EMCSearch.TXT, "跳过")
                    )
                ),
                MCStep(
                    "点击购物车",
                    condList = arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCSearch.ID, "com.meituan.retail.v.android:id/img_shopping_cart")
                        ),
                        MCCond(
                            EMCCond.NODE_NOT_SELECTED,
                            MCNode(EMCSearch.ID, "com.meituan.retail.v.android:id/img_shopping_cart")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 500,
                        node = MCNode(EMCSearch.ID, "com.meituan.retail.v.android:id/img_shopping_cart")
                    )
                ),
                MCStep(
                    "点击我知道了",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCSearch.TXT, "我知道了")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node =
                        MCNode(EMCSearch.TXT, "我知道了"),

                        )
                ),
                MCStep(
                    "点击返回购物车",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCSearch.TXT, "返回购物车")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node =
                        MCNode(EMCSearch.TXT, "返回购物车"),

                        )
                ),
                MCStep(
                    "点击结算",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCSearch.TXT, "结算")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node = MCNode(EMCSearch.TXT, "结算")
                    ),
                ),
                MCStep(
                    "立即支付",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCSearch.TXT, "立即支付")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 100,
                        node = MCNode(EMCSearch.TXT, "立即支付")
                    ),
                ),
                MCStep(
                    "自动选择送达时间",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCSearch.TXT, "选择送达时间")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_RANDOM_NODE,
                        node =
                        MCNode(EMCSearch.TXT, "-"),

                        ),
                ),
                MCStep(
                    "确认支付",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCSearch.TXT, "提交订单")
                        ),
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCSearch.TXT, "立即支付")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 100,
                        node = MCNode(EMCSearch.TXT, "确认支付")
                    ),
                ),
                MCStep(
                    "展开更多支付方式",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCSearch.TXT, "支付订单")
                        ),
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCSearch.TXT, "展开更多支付方式")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 100,
                        node = MCNode(EMCSearch.TXT, "展开更多支付方式")
                    ),
                ),
                MCStep(
                    "选择支付宝支付",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCSearch.TXT, "支付订单")
                        ),
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCSearch.TXT, "支付宝支付")
                        ),
                        MCCond(
                            EMCCond.NODE_NOT_CHECKED,
                            MCNode(EMCSearch.TXT, "支付宝支付")
                        ),
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 100,
                        node = MCNode(EMCSearch.TXT, "支付宝支付")
                    ),
                ),
                MCStep(
                    "确认支付",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCSearch.TXT, "支付订单")
                        ),
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCSearch.TXT, "确认支付")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 100,
                        node = MCNode(EMCSearch.TXT, "确认支付")
                    ),
                    isAlarm = true,
                    isManual = true
                ),
            )
        ),
        MHSolution(
            name = "美团抢购 - 自动选择送达时间 - 微信",
            steps = arrayListOf(
                MCStep(
                    "打开 app",
                    condList = arrayListOf(
                        MCCond(
                            EMCCond.APP_IS_BACKGROUND,
                            MCNode(EMCSearch.PACKAGE_NAME, packageName = "com.meituan.retail.v.android")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.LAUNCH,
                        delay = 500,
                        node =
                        MCNode(EMCSearch.PACKAGE_NAME, packageName = "com.meituan.retail.v.android"),

                        )
                ),
                MCStep(
                    "点击跳过",
                    condList = arrayListOf(
                        MCCond(
                            EMCCond.EQ_CLASS_NAME,
                            MCNode(
                                EMCSearch.CLASSNAME,
                                className = "com.meituan.retail.c.android.splash.SplashActivity"
                            )
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 500,
                        node = MCNode(EMCSearch.TXT, "跳过")
                    )
                ),
                MCStep(
                    "点击购物车",
                    condList = arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCSearch.ID, "com.meituan.retail.v.android:id/img_shopping_cart")
                        ),
                        MCCond(
                            EMCCond.NODE_NOT_SELECTED,
                            MCNode(EMCSearch.ID, "com.meituan.retail.v.android:id/img_shopping_cart")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 500,
                        node = MCNode(EMCSearch.ID, "com.meituan.retail.v.android:id/img_shopping_cart")
                    )
                ),
                MCStep(
                    "点击我知道了",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCSearch.TXT, "我知道了")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node =
                        MCNode(EMCSearch.TXT, "我知道了"),

                        )
                ),
                MCStep(
                    "点击返回购物车",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCSearch.TXT, "返回购物车")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node =
                        MCNode(EMCSearch.TXT, "返回购物车"),

                        )
                ),
                MCStep(
                    "点击结算",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCSearch.TXT, "结算")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node = MCNode(EMCSearch.TXT, "结算")
                    ),
                ),
                MCStep(
                    "立即支付",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCSearch.TXT, "立即支付")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 100,
                        node = MCNode(EMCSearch.TXT, "立即支付")
                    ),
                ),
                MCStep(
                    "自动选择送达时间",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCSearch.TXT, "选择送达时间")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_RANDOM_NODE,
                        node =
                        MCNode(EMCSearch.TXT, "-"),

                        ),
                ),
                MCStep(
                    "确认支付",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCSearch.TXT, "提交订单")
                        ),
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCSearch.TXT, "立即支付")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 100,
                        node = MCNode(EMCSearch.TXT, "确认支付")
                    ),
                ),
                MCStep(
                    "展开更多支付方式",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCSearch.TXT, "支付订单")
                        ),
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCSearch.TXT, "展开更多支付方式")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 100,
                        node = MCNode(EMCSearch.TXT, "展开更多支付方式")
                    ),
                ),
                MCStep(
                    "选择微信支付",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCSearch.TXT, "支付订单")
                        ),
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCSearch.TXT, "微信支付")
                        ),
                        MCCond(
                            EMCCond.NODE_NOT_CHECKED,
                            MCNode(EMCSearch.TXT, "微信支付")
                        ),
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 100,
                        node = MCNode(EMCSearch.TXT, "微信支付")
                    ),
                ),
                MCStep(
                    "确认支付",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCSearch.TXT, "支付订单")
                        ),
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCSearch.TXT, "确认支付")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 100,
                        node = MCNode(EMCSearch.TXT, "确认支付")
                    ),
                    isAlarm = true,
                    isManual = true
                ),
            )
        ),
        MHSolution(
            name = "美团抢购 - 人工选择送达时间",
            steps = arrayListOf(
                MCStep(
                    "打开 app",
                    condList = arrayListOf(
                        MCCond(
                            EMCCond.APP_IS_BACKGROUND,
                            MCNode(EMCSearch.PACKAGE_NAME, packageName = "com.meituan.retail.v.android")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.LAUNCH,
                        delay = 500,
                        node =
                        MCNode(EMCSearch.PACKAGE_NAME, packageName = "com.meituan.retail.v.android"),

                        )
                ),
                MCStep(
                    "点击跳过",
                    condList = arrayListOf(
                        MCCond(
                            EMCCond.EQ_CLASS_NAME,
                            MCNode(
                                EMCSearch.CLASSNAME,
                                className = "com.meituan.retail.c.android.splash.SplashActivity"
                            )
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 500,
                        node = MCNode(EMCSearch.TXT, "跳过")
                    )
                ),
                MCStep(
                    "点击购物车",
                    condList = arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCSearch.ID, "com.meituan.retail.v.android:id/img_shopping_cart")
                        ),
                        MCCond(
                            EMCCond.NODE_NOT_SELECTED,
                            MCNode(EMCSearch.ID, "com.meituan.retail.v.android:id/img_shopping_cart")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 500,
                        node = MCNode(EMCSearch.ID, "com.meituan.retail.v.android:id/img_shopping_cart")
                    )
                ),
                MCStep(
                    "点击我知道了",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCSearch.TXT, "我知道了")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node =
                        MCNode(EMCSearch.TXT, "我知道了"),

                        )
                ),
                MCStep(
                    "点击返回购物车",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCSearch.TXT, "返回购物车")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node =
                        MCNode(EMCSearch.TXT, "返回购物车"),

                        )
                ),
                MCStep(
                    "点击结算",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCSearch.TXT, "结算")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node = MCNode(EMCSearch.TXT, "结算")
                    ),
                ),
                MCStep(
                    "立即支付",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCSearch.TXT, "立即支付")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 100,
                        node = MCNode(EMCSearch.TXT, "立即支付")
                    ),
                ),
                MCStep(
                    "选择送达时间",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCSearch.TXT, "选择送达时间")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.NONE,
                        node = MCNode(EMCSearch.PACKAGE_NAME)
                    ),
                    isAlarm = true,
                    isManual = true
                ),
                MCStep(
                    "确认支付",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCSearch.TXT, "提交订单")
                        ),
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCSearch.TXT, "立即支付")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 100,
                        node = MCNode(EMCSearch.TXT, "确认支付")
                    ),
                    isAlarm = true,
                    isManual = true
                ),
            )
        ),
        MHSolution(
            name = "叮咚抢购",
            steps = arrayListOf(
                MCStep(
                    "打开 app",
                    arrayListOf(
                        MCCond(
                            EMCCond.APP_IS_BACKGROUND,
                            MCNode(EMCSearch.PACKAGE_NAME, packageName = "com.yaya.zone")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.LAUNCH,
                        delay = 500,
                        node = MCNode(EMCSearch.PACKAGE_NAME, packageName = "com.yaya.zone")
                    )
                ),
                MCStep(
                    "跳过广告",
                    condList = arrayListOf(
                        MCCond(
                            EMCCond.EQ_CLASS_NAME,
                            MCNode(EMCSearch.CLASSNAME, className = "cn.me.android.splash.activity.SplashActivity")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 500,
                        node = MCNode(EMCSearch.TXT, "跳过")
                    )
                ),
                MCStep(
                    "点击购物车",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCSearch.TXT, "购物车", className = "android.widget.TextView")
                        ),
                        MCCond(
                            EMCCond.NODE_NOT_SELECTED,
                            MCNode(EMCSearch.TXT, "购物车", className = "android.widget.TextView")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 500,
                        node =
                        MCNode(EMCSearch.TXT, "购物车", className = "android.widget.TextView")

                    )
                ),
                MCStep(
                    "去结算",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCSearch.TXT, "去结算")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 100,
                        node = MCNode(EMCSearch.TXT, "去结算")
                    ),
                ),
                MCStep(
                    "点击重新加载",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCSearch.TXT, "重新加载")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node = MCNode(EMCSearch.TXT, "重新加载")
                    ),
                ),
                MCStep(
                    "自动选择送达时间",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCSearch.TXT, "选择送达时间")
                        ),
                        MCCond(
                            EMCCond.NODE_NOT_CLICK,
                            MCNode(EMCSearch.TXT, "选择送达时间")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_RANDOM_NODE,
                        node =
                        MCNode(EMCSearch.TXT, "-"),

                        )
                ),
                MCStep(
                    "立即支付",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCSearch.TXT, "立即支付")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 100,
                        node = MCNode(EMCSearch.TXT, "立即支付")
                    ),
                    isAlarm = true,
                    isManual = true
                ),
            )
        ),
        MHSolution(
            name = "美团正常购买 - 支付宝",
            steps = arrayListOf(
                MCStep(
                    "打开 app",
                    condList = arrayListOf(
                        MCCond(
                            EMCCond.APP_IS_BACKGROUND,
                            MCNode(EMCSearch.PACKAGE_NAME, packageName = "com.meituan.retail.v.android")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.LAUNCH,
                        delay = 500,
                        node =
                        MCNode(EMCSearch.PACKAGE_NAME, packageName = "com.meituan.retail.v.android"),

                        )
                ),
                MCStep(
                    "点击跳过",
                    condList = arrayListOf(
                        MCCond(
                            EMCCond.EQ_CLASS_NAME,
                            MCNode(
                                EMCSearch.CLASSNAME,
                                className = "com.meituan.retail.c.android.splash.SplashActivity"
                            )
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 500,
                        node = MCNode(EMCSearch.TXT, "跳过")
                    )
                ),
                MCStep(
                    "点击购物车",
                    condList = arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCSearch.ID, "com.meituan.retail.v.android:id/img_shopping_cart")
                        ),
                        MCCond(
                            EMCCond.NODE_NOT_SELECTED,
                            MCNode(EMCSearch.ID, "com.meituan.retail.v.android:id/img_shopping_cart")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 500,
                        node = MCNode(EMCSearch.ID, "com.meituan.retail.v.android:id/img_shopping_cart")
                    )
                ),
                MCStep(
                    "点击结算",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCSearch.TXT, "结算")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node = MCNode(EMCSearch.TXT, "结算")
                    ),
                ),
                MCStep(
                    "立即支付",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCSearch.TXT, "立即支付")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 100,
                        node = MCNode(EMCSearch.TXT, "立即支付")
                    ),
                ),
                MCStep(
                    "选择送达时间",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCSearch.TXT, "选择送达时间")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_RANDOM_NODE,
                        node =
                        MCNode(EMCSearch.TXT, "-"),

                        )
                ),
                MCStep(
                    "确认支付",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCSearch.TXT, "提交订单")
                        ),
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCSearch.TXT, "立即支付")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 100,
                        node = MCNode(EMCSearch.TXT, "确认支付")
                    ),
                ),
                MCStep(
                    "展开更多支付方式",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCSearch.TXT, "支付订单")
                        ),
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCSearch.TXT, "展开更多支付方式")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 100,
                        node = MCNode(EMCSearch.TXT, "展开更多支付方式")
                    ),
                ),
                MCStep(
                    "选择支付宝支付",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCSearch.TXT, "支付订单")
                        ),
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCSearch.TXT, "支付宝支付")
                        ),
                        MCCond(
                            EMCCond.NODE_NOT_CHECKED,
                            MCNode(EMCSearch.TXT, "支付宝支付")
                        ),
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 100,
                        node = MCNode(EMCSearch.TXT, "支付宝支付")
                    ),
                ),
                MCStep(
                    "确认支付",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCSearch.TXT, "支付订单")
                        ),
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCSearch.TXT, "确认支付")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 100,
                        node = MCNode(EMCSearch.TXT, "确认支付")
                    ),
                    isAlarm = true,
                    isManual = true
                ),
            )
        ),
        MHSolution(
            name = "美团正常购买 - 微信",
            steps = arrayListOf(
                MCStep(
                    "打开 app",
                    condList = arrayListOf(
                        MCCond(
                            EMCCond.APP_IS_BACKGROUND,
                            MCNode(EMCSearch.PACKAGE_NAME, packageName = "com.meituan.retail.v.android")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.LAUNCH,
                        delay = 500,
                        node =
                        MCNode(EMCSearch.PACKAGE_NAME, packageName = "com.meituan.retail.v.android"),

                        )
                ),
                MCStep(
                    "点击跳过",
                    condList = arrayListOf(
                        MCCond(
                            EMCCond.EQ_CLASS_NAME,
                            MCNode(
                                EMCSearch.CLASSNAME,
                                className = "com.meituan.retail.c.android.splash.SplashActivity"
                            )
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 500,
                        node = MCNode(EMCSearch.TXT, "跳过")
                    )
                ),
                MCStep(
                    "点击购物车",
                    condList = arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCSearch.ID, "com.meituan.retail.v.android:id/img_shopping_cart")
                        ),
                        MCCond(
                            EMCCond.NODE_NOT_SELECTED,
                            MCNode(EMCSearch.ID, "com.meituan.retail.v.android:id/img_shopping_cart")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 500,
                        node = MCNode(EMCSearch.ID, "com.meituan.retail.v.android:id/img_shopping_cart")
                    )
                ),
                MCStep(
                    "点击结算",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCSearch.TXT, "结算")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node = MCNode(EMCSearch.TXT, "结算")
                    ),
                ),
                MCStep(
                    "立即支付",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCSearch.TXT, "立即支付")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 100,
                        node = MCNode(EMCSearch.TXT, "立即支付")
                    ),
                ),
                MCStep(
                    "选择送达时间",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCSearch.TXT, "选择送达时间")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_RANDOM_NODE,
                        node =
                        MCNode(EMCSearch.TXT, "-"),

                        )
                ),
                MCStep(
                    "确认支付",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCSearch.TXT, "提交订单")
                        ),
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCSearch.TXT, "立即支付")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 100,
                        node = MCNode(EMCSearch.TXT, "确认支付")
                    ),
                ),
                MCStep(
                    "展开更多支付方式",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCSearch.TXT, "支付订单")
                        ),
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCSearch.TXT, "展开更多支付方式")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 100,
                        node = MCNode(EMCSearch.TXT, "展开更多支付方式")
                    ),
                ),
                MCStep(
                    "选择微信支付",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCSearch.TXT, "支付订单")
                        ),
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCSearch.TXT, "微信支付")
                        ),
                        MCCond(
                            EMCCond.NODE_NOT_CHECKED,
                            MCNode(EMCSearch.TXT, "微信支付")
                        ),
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 100,
                        node = MCNode(EMCSearch.TXT, "微信支付")
                    ),
                ),
                MCStep(
                    "确认支付",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCSearch.TXT, "支付订单")
                        ),
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCSearch.TXT, "确认支付")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 100,
                        node = MCNode(EMCSearch.TXT, "确认支付")
                    ),
                    isAlarm = true,
                    isManual = true
                ),
            )
        ),
        MHSolution(
            name = "叮咚正常购买",
            steps = arrayListOf(
                MCStep(
                    "打开 app",
                    arrayListOf(
                        MCCond(
                            EMCCond.APP_IS_BACKGROUND,
                            MCNode(EMCSearch.PACKAGE_NAME, packageName = "com.yaya.zone")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.LAUNCH,
                        delay = 500,
                        node = MCNode(EMCSearch.PACKAGE_NAME, packageName = "com.yaya.zone")
                    )
                ),
                MCStep(
                    "跳过广告",
                    condList = arrayListOf(
                        MCCond(
                            EMCCond.EQ_CLASS_NAME,
                            MCNode(EMCSearch.CLASSNAME, className = "cn.me.android.splash.activity.SplashActivity")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 500,
                        node = MCNode(EMCSearch.TXT, "跳过")
                    )
                ),
                MCStep(
                    "点击购物车",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCSearch.TXT, "购物车", className = "android.widget.TextView")
                        ),
                        MCCond(
                            EMCCond.NODE_NOT_SELECTED,
                            MCNode(EMCSearch.TXT, "购物车", className = "android.widget.TextView")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 500,
                        node =
                        MCNode(EMCSearch.TXT, "购物车", className = "android.widget.TextView")

                    )
                ),
                MCStep(
                    "去结算",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCSearch.TXT, "去结算")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 100,
                        node = MCNode(EMCSearch.TXT, "去结算")
                    ),
                ),
                MCStep(
                    "自动选择送达时间",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCSearch.TXT, "选择送达时间")
                        ),
                        MCCond(
                            EMCCond.NODE_NOT_CLICK,
                            MCNode(EMCSearch.TXT, "选择送达时间")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_RANDOM_NODE,
                        node =
                        MCNode(EMCSearch.TXT, "-"),

                        )
                ),
                MCStep(
                    "立即支付",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCSearch.TXT, "立即支付")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 100,
                        node = MCNode(EMCSearch.TXT, "立即支付")
                    ),
                    isAlarm = true,
                    isManual = true
                ),
            )
        ),
        MHSolution(
            name = "盒马正常购买",
            steps = arrayListOf(
                MCStep(
                    "打开 app",
                    arrayListOf(
                        MCCond(
                            EMCCond.APP_IS_BACKGROUND,
                            MCNode(search = EMCSearch.PACKAGE_NAME, packageName = "com.wudaokou.hippo")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.LAUNCH,
                        delay = 500,
                        node = MCNode(search = EMCSearch.PACKAGE_NAME, packageName = "com.wudaokou.hippo")
                    )
                ),
                MCStep(
                    "点击购物车",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCSearch.TXT, "购物车", className = "android.widget.TextView")
                        ),
                        MCCond(
                            EMCCond.NODE_NOT_SELECTED,
                            MCNode(EMCSearch.TXT, "购物车", className = "android.widget.TextView")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 500,
                        node =
                        MCNode(EMCSearch.TXT, "购物车", className = "android.widget.TextView")

                    )
                ),
                MCStep(
                    "结算",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCSearch.TXT, "结算")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 100,
                        node = MCNode(EMCSearch.TXT, "结算")
                    ),
                ),
                MCStep(
                    "确认选择时间",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCSearch.TXT, "选择时间", className = "android.widget.LinearLayout")
                        ),
                        MCCond(
                            EMCCond.NODE_VISIBLE,
                            MCNode(EMCSearch.TXT, "选择时间", className = "android.widget.LinearLayout")
                        ),
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node =
                        MCNode(EMCSearch.TXT, "确认"),

                        )
                ),
                MCStep(
                    "点击选择时间",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCSearch.TXT, "选择时间", className = "android.widget.TextView")
                        ),
                        MCCond(
                            EMCCond.NODE_VISIBLE,
                            MCNode(EMCSearch.TXT, "选择时间", className = "android.widget.TextView")
                        ),
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        node =
                        MCNode(EMCSearch.TXT, "选择时间"),

                        )
                ),
                MCStep(
                    "提交订单",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCSearch.TXT, "提交订单")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 100,
                        node = MCNode(EMCSearch.TXT, "提交订单")
                    ),
                    isAlarm = true,
                    isManual = true
                ),
            )
        ),
    )

}