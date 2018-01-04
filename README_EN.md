# Android-FloatingLabelSpinner

[![License](https://img.shields.io/badge/License%20-Apache%202-337ab7.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![MinSDK](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=14)
[![Methods](https://img.shields.io/badge/Methods%20%7C%20Size%20-%20148%20%7C%20107%20KB-d9534f.svg)](http://www.methodscount.com/?lib=com.github.james602152002%3AFloatingLabelSpinner%3A1.1.0)
[![](https://jitpack.io/v/james602152002/FloatingLabelSpinner.svg)](https://jitpack.io/#james602152002/FloatingLabelSpinner)

## English| [中文](README.md)

Most of floating label spinner always use xml to adjust your widget's size. Sometimes we wanna do it by code, and we want to style our floating label by ForegroundColorSpan. My widget will satisfy your demands.

## Feature:

 - Set text size by code. (Include floating label, hint text, and error text)
 - Set hint color, divider color and error color by code.
 - Customize DropDownHintView.
 - Scrolling text animation when your error character is too long.
 - Ellipsize when hint character is too long.
 - Label support ForegroundColorSpan now.
 
## APK Demo
[Download APK-Demo](art/demo_en.apk)

## Demonstration

|Floating Status|Error Status|
|:---:|:---:|
|![](art/float_en.gif)|![](art/error_en.gif)|

[More Demostration](common_md/DEMONSTRATION_EN.md)

## Dependency:

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
	compile 'com.github.james602152002:FloatingLabelSpinner:1.1.1'
}
```

## Property

```xml
<com.james602152002.floatinglabelspinner.FloatingLabelSpinner
          android:layout_width="match_parent"
          android:layout_height="wrap_content"
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
          app: j_fls_label_horizontal_margin="2dp"
          //浮动文字垂直偏移量
          app: j_fls_label_vertical_margin="2dp"
          //错误文字水平偏移量
          app: j_fls_error_horizontal_margin="2dp"
          //错误文字垂直偏移量
          app: j_fls_error_vertical_margin="2dp"
          //浮动文字字体大小
          app: j_fls_label_textSize="14sp"
          //提示文字字体大小
          app:j_fls_hint_textSize="20sp"
          //错误文字字体大小
          app:j_fls_error_textSize="14sp"
          //设置drop down view头部
          app:j_fls_dropDownHintView="@layout/header"
          //浮动文字动画时间(单位：毫秒)
          app:j_fls_float_anim_duration="800"
          //错误状态下跑马灯动画时间(单位：毫秒)
          app:j_fls_error_anim_duration="8000"/>
          
```

## Method

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

```

## Proguard

You don't need use proguard at all.

## Donate

If you like this widget,  you could praise me some protein powder below lol

|WeChat|AliPay|
|:---:|:---:|
|![](art/weixin_green.jpg)|![](art/zhifubao_blue.jpg)|

## Suggestion

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
