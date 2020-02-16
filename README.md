# 微信小程序消息代理 For LeanCloud

微信小程序消息和 LeanCloud 即时消息的打通。

## 本地运行

首先确认本机已经安装 [LeanCloud 命令行工具](https://www.leancloud.cn/docs/leanengine_cli.html)

安装依赖：

```
mvn package
```

登录账户：
```
lean login
```

关联项目：
```
lean checkout
```
根据列表提示，输入数字，依次按回车确认，以关联到你的应用。


启动项目：

```
lean up
```

应用即可启动运行：[localhost:3000](http://localhost:3000)

## 部署到 LeanEngine

部署到预备环境（若无预备环境则直接部署到生产环境）：
```
lean deploy
```

将预备环境的代码发布到生产环境：
```
lean publish
```

## 相关配置地址

- 小程序后台配置消息接收URL: <http://ip:port/wechat/message-receive>
- 微信支付异步接收URL：<http://ip:port/wechat/pay-result-notify>

## 极光推送接口

*参数说明*

- client_type 客户端类型
    - ANDROID 安卓客户端
    - IOS iOS客户端
- client_id APP登录LeanCloud消息服务使用的ClientId
- reg_id APP初始化极光推送成功后获取的 RegId

### App端初始化极光SDK成功后调用绑定接口

```
POST /user/push-bind

{
    "client_id": "norhor20181223+0001+002",
    "client_type": "ANDROID",
    "reg_id": "170976fa8aeb0034784"
}
```

### App端解除绑定接口

```
POST /user/push-unbind

{
    "client_id": "norhor20181223+0001+002"
}
```
