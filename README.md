# feiyu sdk android demo

#### 1. 开发环境要求:

> * Android SDK API Level >= 16
> * Android Studio 2.0 或以上版本
> * 支持语音功能的真机


#### 2. 将jar包和so复制到对应的位置

![github](https://github.com/FeiyuCloud/android-sdk-demo/master/img/fycloud-androidstudio.png "github")

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

#### 4. 编译时java版本

使用java7及以上版本编译

```groovy
compileOptions {
sourceCompatibility JavaVersion.VERSION_1_7
targetCompatibility JavaVersion.VERSION_1_7
}
```


#### 5. 混淆

```
-keep class com.feiyucloud.** { *; }
```

#### 6. 多人语音
```java
FYRtcEngine engine = FYRtcEngine.create(mContext, appId, apptoken, mRtcEventHandler);
engine.joinChannel(channelId, mUid, option);
```


<br/>
