package com.univerindream.maicaiassistant

object MHDefault {

    val defaultMHSolutions = arrayListOf(
        MHSolution(
            name = "美团 6 点购买方案",
            steps = arrayListOf(
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
                            EMCCond.NODE_NOT_SELECTED,
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
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCNodeType.TXT, "立即支付")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 100,
                        nodes = arrayListOf(MCNode(EMCNodeType.TXT, "立即支付"))
                    ),
                    isAlarm = true,
                ),
                MCStep(
                    "clickBtnSurePay",
                    arrayListOf(
                        MCCond(
                            EMCCond.NODE_EXIST,
                            MCNode(EMCNodeType.TXT, "支付订单")
                        ),
                        MCCond(
                            EMCCond.NODE_CAN_CLICK,
                            MCNode(EMCNodeType.TXT, "立即支付")
                        )
                    ),
                    handle = MCHandle(
                        type = EMCHandle.CLICK_NODE,
                        delay = 100,
                        nodes = arrayListOf(MCNode(EMCNodeType.TXT, "确认支付"))
                    ),
                    isAlarm = true,
                ),
            )
        ),
        MHSolution(
            name = "叮咚自动购买",
            steps = arrayListOf(
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
                            EMCCond.NODE_NOT_SELECTED,
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
            )
        ),
        MHSolution(
            name = "美团正常购买方案 - 支付宝",
            steps = arrayListOf(
                MCStep(
                    "打开 app",
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
                    "点击跳过",
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
                        nodes = arrayListOf(MCNode(EMCNodeType.ID, "com.meituan.retail.v.android:id/img_shopping_cart"))
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
                        nodes = arrayListOf(MCNode(EMCNodeType.TXT, "结算"))
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
                        nodes = arrayListOf(MCNode(EMCNodeType.TXT, "立即支付"))
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
                        type = EMCHandle.CLICK_SCOPE_RANDOM_NODE,
                        nodes = arrayListOf(
                            MCNode(EMCNodeType.TXT, "09:35-10:30"),
                            MCNode(EMCNodeType.TXT, "10:30-14:30"),
                            MCNode(EMCNodeType.TXT, "14:30-18:30"),
                            MCNode(EMCNodeType.TXT, "18:30-22:30")
                        )
                    ),
                    isAlarm = true,
                    isManual = true
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
                        nodes = arrayListOf(MCNode(EMCNodeType.TXT, "确认支付"))
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
                        nodes = arrayListOf(MCNode(EMCNodeType.TXT, "展开更多支付方式"))
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
                        nodes = arrayListOf(MCNode(EMCNodeType.TXT, "支付宝支付"))
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
                        nodes = arrayListOf(MCNode(EMCNodeType.TXT, "确认支付"))
                    ),
                    isAlarm = true,
                    isManual = true
                ),
            )
        ),
    )

}