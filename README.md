# feiyu sdk android demo

#### 1. 开发环境要求:

> * Android SDK API Level >= 16
> * 不支持模拟器编译运行
> * 准备好 AppID 和 AppToken


#### 2. 将jar包和so复制到对应的位置

![github](https://github.com/FeiyuCloud/android-sdk-demo/blob/master/img/fycloud-androidstudio.png "github")


#### 3. SDK所需权限:

```xml
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.VIBRATE"/>
<uses-permission android:name="android.permission.WAKE_LOCK"/>
<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
<uses-permission android:name="android.permission.RECORD_AUDIO"/>
<uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
```

当targetSdkVersion大于等于23的时候，请在调用SDK前申请好所需权限。


#### 4. java版本

```groovy
compileOptions {
    sourceCompatibility JavaVersion.VERSION_1_7
    targetCompatibility JavaVersion.VERSION_1_7
}
```


#### 5. 混淆

```
-keepattributes Signature
-keep class com.feiyucloud.** { *; }
```


#### 6. 使用

- 创建FYRtcEngine：

```java
public static FYRtcEngine create(Context context, String appId, String appToken, FYRtcEventHandler handler);
```

| 参数 | 是否必须 | 描述 |
| ---- | :---- | :---- |
| context | 是 | 应用程序上下文 |
| appId | 是 | 应用id |
| appToken | 是 | 应用token |
| handler | 是 | 一个提供了缺省实现的抽象类，SDK通过该抽象类向报告SDK运行时的各种事件 |

<br/>

- 加入频道：

```java
public void joinChannel(String channelId, String uid, FYOptionData option);
```

| 参数 | 是否必须 | 描述 |
| ---- | :---- | :---- |
| channelId | 是 | 频道id，字符串，数字，\_，长度不超过40位 |
| uid | 否 | 用户id，为空时sdk会生成一个uuid作为uid |
| option | 否 | 选项，可以配置最大时长，是否录音和偷传数据 |

<br/>

- 点到点语音：

```java
public void dialPeer(String calleeUid, String callerUid, FYOptionData option);
```

| 参数 | 是否必须 | 描述 |
| --- | --- | --- |
| calleeUid | 是 | 被叫用户id |
| callerUid | 否 | 主叫用户id，为空时sdk会生成一个uid |
| option | 否 | 呼叫选项，可配置最大时长，是否录音，透传数据 |

<br/>

- 准备接听点到点来电：

主叫呼叫被叫时，可以使用离线推送（如小米push）通知被叫，被叫调用`calleePrepare`准备接听来电，调用成功后一段时间内都可以接听到点到点语音来电。

注意：`calleePrepare`的参数需和`dialPeer`的calleeUid保持一致。

```java
public void calleePrepare(String callerUid);
```
| 参数 | 是否必须 | 描述 |
| --- | --- | --- |
| callerUid | 是 | 当前的uid |


<br/>

> channelId，uid格式：长度不超过40的字符串，支持的字符集范围: a-z,A-Z,0-9,_,-



#### 更多内容请参考：[http://gitbook.feiyucloud.com/](http://gitbook.feiyucloud.com/)

<br/>
