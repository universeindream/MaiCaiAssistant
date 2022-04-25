## 买菜助手

[![GitHub release](https://img.shields.io/github/v/release/universeindream/MaiCaiAssistant.svg)](https://github.com/universeindream/MaiCaiAssistant/releases) [![Total Downloads](https://img.shields.io/github/downloads/universeindream/MaiCaiAssistant/total.svg)](https://github.com/universeindream/MaiCaiAssistant/releases/latest/download/app-release.apk) ![Support Platforms](https://img.shields.io/badge/platform-android-blue) 

### 概述

[买菜助手](https://github.com/universeindream/MaiCaiAssistant) 是一款可以自动执行 `购买方案` 的 APP。 购买方案由多个步骤构成，而每个步骤由条件与操作两个模块组成，条件用来指定系统当前处于哪一步骤，操作用来执行当前步骤的目标任务。

> 截止 20220425，美团买菜 - 可用，叮咚 - 可用，盒马半自动 - 可用

### 特性

- 支持多种方案 - [美团买菜](https://github.com/universeindream/MaiCaiAssistant/wiki/%E7%BE%8E%E5%9B%A2%E4%B9%B0%E8%8F%9C)、[叮咚](https://github.com/universeindream/MaiCaiAssistant/wiki/%E5%8F%AE%E5%92%9A%E4%B9%B0%E8%8F%9C)、[盒马](https://github.com/universeindream/MaiCaiAssistant/wiki/%E7%9B%92%E9%A9%AC) 
   - 可以自定义其他购物 APP 
- 支持设置抢购时长 - 可以用于捡漏
- 支持设置定时执行 - 适用于抢到了再起床的人
   - 需设置屏幕常亮
- 支持设置异常响铃 - 防止错过了抢购的机会
- 支持手动修改方案 - 自定义适合自己的方案

### 使用

1. [安装 APK](https://github.com/universeindream/MaiCaiAssistant/releases/latest/download/app-release.apk)
1. 开启无障碍服务
1. 选择适合的方案 - 比如：美团买菜 - 支付宝
2. 添加需要的东西至购物车
3. 点击右侧悬浮窗的运行按钮(▶)

> 因每个人的手机性能和网络环境不同, 如果运行失败，请大家自行设置问题步骤的`延迟时间`

### 重要

1. 运行前：请确认已选方案是否适合你!!!
2. 定时前：请测试该功能是否正常工作!!!
3. 悬浮窗：默认开启屏幕常亮!!!
4. 运行慢：请降低问题步骤的延迟时间!!!

### 问题

- 如果闪退，记得点击 `ERROR DETAILS` 截图反馈 - [参考图](https://user-images.githubusercontent.com/20157750/163066496-df9dafe9-bbbb-4bfd-8acb-1f7254475147.jpg)
- 如果 xxx 步骤运行失败，请尝试增加 xxx 步骤的执行前延迟或执行后延迟时间 - [视频教程](https://user-images.githubusercontent.com/7286154/163680965-a64f13ea-9bd7-4033-9c2e-e8f69e288831.mp4)
- 如果无法打开目标软件，请删除该步骤，然后手动打开目标软件，点击运行
- 如果无法定时运行，请将买菜助手保持在前台
- 如果没有悬浮窗，请打开应用设置开启浮窗权限

### 互助

由于本人所有购物平台都停运了，所以合适的方案很难给到大家，为了方便大家讨论出合适的抢购方案，建个微信群，大家互相帮助！！！ 

| 微信群3 | 拉2群微信 | TG 频道 | TG 群 |
| ------------- | ------------- | ------------- | ------------- |
| <img width="150" src="https://user-images.githubusercontent.com/7286154/164889964-8ac6cf63-6e57-493d-acb1-48bfe92fce89.png"> | <img width="150" src="https://user-images.githubusercontent.com/7286154/164888899-16c76894-61c6-4e42-981b-48c0e16752a1.png"> | <img width="150" src="https://user-images.githubusercontent.com/7286154/163721131-3aa6da8d-44ab-48d3-98a5-3ccfc083fae0.png"> |     <img width="150" src="https://user-images.githubusercontent.com/7286154/163738276-3460fdd0-930b-493a-a899-d8aa8a4e03be.png">     |



**ps:**

- 如果有需要的话，加我这个微信号，我抽空帮忙新增或优化方案，当然还要靠大家的录屏测试反馈
- 望疫情早日结束，也祝大伙也早点结束抢菜的日子
- 最后点个 :star: , 支持下 :ghost:

### 预览

**图片**

| -  | - | - |
| ------------- | ------------- | ------------- |
| <img src="https://user-images.githubusercontent.com/7286154/164913288-7d4f3e8f-49a6-485f-9d41-5532b4bdecd6.png" width="200" >  | <img src="https://user-images.githubusercontent.com/7286154/164913292-799634ec-b5f9-4cd1-8f0c-3c20890c47c1.png" width="200" > | <img src="https://user-images.githubusercontent.com/7286154/164913296-c75fd54b-df3e-44bc-b7a0-c2677d6e4458.png" width="200" > |


**实战**

<img src="https://user-images.githubusercontent.com/7286154/163914983-4d1e7b58-4abe-4d1f-aa9d-af9d38093f32.gif" width="300">

### TODO

- [x] 图形编辑
   - [x] 编辑
   - [x] 新增
   - [x] 删除
- [x] 文件导出
- [ ] 方案管理
- [ ] 步骤重复
   - [ ] 设置重复时长
   - [ ] 设置重复时长，已满返回

### License

[GNU General Public License v3.0](https://github.com/universeindream/MaiCaiAssistant/blob/main/LICENSE)


### 贡献

如果你有好的意见或建议，欢迎提 issue 或 pull request。
