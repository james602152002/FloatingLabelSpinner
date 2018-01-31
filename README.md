# Android浮动字体下拉框-FloatingLabelSpinner

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Floating%20Label%20Spinner-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/6635)
[![License](https://img.shields.io/badge/License%20-Apache%202-337ab7.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![MinSDK](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=14)
[![](https://jitpack.io/v/james602152002/FloatingLabelSpinner.svg)](https://jitpack.io/#james602152002/FloatingLabelSpinner)
[![Build Status](https://travis-ci.org/james602152002/FloatingLabelSpinner.svg?branch=master)](https://travis-ci.org/james602152002/FloatingLabelSpinner)
[![codecov](https://codecov.io/gh/james602152002/FloatingLabelSpinner/branch/master/graph/badge.svg)](https://codecov.io/gh/james602152002/FloatingLabelSpinner)

## [English](README_EN.md) | 中文

多数的浮动下拉框控件碍于多数的适配方式必须由xml实现，故无法在页面上显示流体布局浮动提示文字。设计上通常欠缺考虑，无法设置ForegroundColorSpan文字以满足个人需求，故开发此控件。

## 特点功能:

 - 支持代码更改字体大小(包含浮动文字、提示文字、错误文字)
 - 支持代码设置提示、分割线以及错误状态下的颜色 
 - 支持自定义弹出框dropDownHintView(弹出框头部)
 - 错误文字的字数太多会以跑马灯动画展示
 - 浮动文字显示字数过多以ellipsize展示
 - 浮动文字支持ForegroundColorSpan
 
## Demo
 - [下载 APK-Demo](art/demo.apk)
 - [示例网址](https://github.com/james602152002/FloatingLabelSpinnerDemo)

## 项目演示

|浮动效果|错误效果|
|:---:|:---:|
|![](art/float_ch.gif)|![](art/error_ch.gif)|

[更多效果](common_md/DEMONSTRATION_CH.md)

## 依赖:

```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

```
dependencies {
	compile 'com.github.james602152002:FloatingLabelSpinner:1.2.6'
}
```

## 属性

```xml
<com.james602152002.floatinglabelspinner.FloatingLabelSpinner
          //浮动文字颜色
          app:j_fls_textColorHint="@android:color/holo_orange_light" 
          //分割线颜色
          app:j_fls_colorHighlight="#0000FF" 
          //错误提示颜色
          app:j_fls_colorError="#0000FF"
          //浮动文字
          app:j_fls_hint="label"
          //分割线厚度
          app:j_fls_thickness="2dp"
          //浮动文字水平偏移量
          app:j_fls_label_horizontal_margin="2dp"
          //浮动文字垂直偏移量
          app:j_fls_label_vertical_margin="2dp"
          //错误文字水平偏移量
          app:j_fls_error_horizontal_margin="2dp"
          //错误文字垂直偏移量
          app:j_fls_error_vertical_margin="2dp"
          //浮动文字字体大小
          app:j_fls_label_textSize="14sp"
          //提示文字字体大小
          app:j_fls_hint_textSize="20sp"
          //错误文字字体大小
          app:j_fls_error_textSize="14sp"
          //设置drop down view头部
          app:j_fls_dropDownHintView="@layout/header"
          //浮动文字动画时间(单位：毫秒)
          app:j_fls_float_anim_duration="800"
          //错误状态下跑马灯动画时间(单位：毫秒)
          app:j_fls_error_anim_duration="8000"
          //开启递归模式(如果传false为关闭)
          app:j_fls_recursive="true"/>
          
```

## 方法

```java

	//浮动文字字体大小
	setLabel_text_size(float label_text_size);
	//提示文字字体大小
	setHint_text_size(float hint_text_size);
	//错误文字字体大小
	setError_text_size(float error_text_size);
	//分割线厚度
	setThickness(int thickness);
	//分割线颜色
	setHighlight_color(int color);
	//提示文字颜色
	setHint_text_color(int color);
	//错误状态颜色
	setError_color(int color);
	//设置错误文字水平垂直偏移量
	setErrorMargin(int horizontal_margin, int vertical_margin);
	//设置浮动及提示文字(支持ForegroundColorSpan)
	spinner.setHint(CharSequence hint);
	//设置drop down view头部
	spinner.setDropDownHintView(View view);
	//设置错误文字(传空就不显示)
	setError(String error);
	//设置浮动文字动画时间(单位：毫秒）
	setAnimDuration(short duration)
	//设置错误状态跑马灯动画时间(单位：毫秒）
	setErrorAnimDuration(short duration)
	//设置递归模式，如果下拉框有二级页面可使用
	setRecursive_mode(boolean recursive_mode)
	//递归模式下手动关闭弹窗
	dismiss()
	//递归模式下更新适配器
	notifyDataSetChanged()
    
```

## 混淆

无需混淆代码。

## 赞赏

如果觉得效果写得不错，欢迎赏小弟一口蛋白粉 :)

|微信|支付宝|
|:---:|:---:|
|![](art/weixin_green.jpg)|![](art/zhifubao_blue.jpg)|

## 建议

```
您必须设置浮动字体提示文字以及适配器才能正常使用。
```

```
此控件于设置适配器后才会正常测量高度，如果里面的数据用到网络请求
我建议：

1. 获取数据后再设置适配器。
2. 用延时加载的效果载入页面，获取数据后再更新视图。

以提高交互体验～～～
```

License
-------

    Copyright 2017 james602152002

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
