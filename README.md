# Android System hide api调用指北

## 概述

Android 系统隐藏 API 是指那些在 Android SDK 中未公开，但可以通过反射或其他技术手段访问的 API。这些 API 可能用于实现某些特定功能或绕过一些限制，例如直接修改系统设置、访问私有数据等。然而，使用隐藏 API 可能会导致应用与未来的 Android 版本不兼容，或者在 Google Play 上被拒绝上架。因此，在使用之前需要谨慎考虑其风险和后果。

## System 应用如何调用
`注意，System应用需要使用系统签名+android:sharedUserId="android.uid.system"声明`

System app-->SystemFunction-->HideApi
System app是我们的业务app

[SystemAPI封装](https://github.com/h4de5ing/SystemFunction)
[隐藏接口封装](https://github.com/h4de5ing/HideApi)

## 如何编译本仓库

下载对应SDK的classes-header.jar 基于aosp编译生成的文件
out_sys/target/common/obj/JAVA_LIBRARIES/framework_intermediates/classes-header.jar

解压classes-header.jar -> classes-header
下面的android.jar在解压或者替换前请备份一下，以免出错
解压\sdk\platforms\android-35\android.jar -> android

比如我需要替换ETH网络配置相关类
cp classes-header\android\net\IpConfiguration.class android\android\net\IpConfiguration.class
cp classes-header\android\net\IpConfiguration$Builder.class android\android\net\IpConfiguration$Builder.class
cp classes-header\android\net\IpConfiguration$IpAssignment.class android\android\net\IpConfiguration$IpAssignment.class
cp classes-header\android\net\IpConfiguration$ProxySettings.class android\android\net\IpConfiguration$ProxySettings.class

在命令行cd到android目录下，执行以下命令生成新的android.jar文件
`jar -cvfM android.jar .`

将新打包的android.jar 替换到sdk目录下\sdk\platforms\android-35\android.jar

## 在命令行中编译本仓库就可以得到hideapi的aar仓库了
