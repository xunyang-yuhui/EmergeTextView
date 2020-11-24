[![](https://jitpack.io/v/xunyang-yuhui/EmergeTextView.svg)](https://jitpack.io/#xunyang-yuhui/EmergeTextView)

一款文字展示的自定义View，含打字机模式（typer）和 随机模式（random）。打字机模式原型可参考游戏中的对话框文字出现的样式，随机模式原型可参考潮汐App

# 效果图展示
https://s3.ax1x.com/2020/11/24/DNekeH.gif

思路参考了 https://github.com/xmuSistone/android-character-animation 

# 使用方法
## xml添加
```
    <com.yu.emergetextview.EmergeTextView
            android:id="@+id/view_emerge_text"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:padding="10dp"
            android:background="@android:color/black"
            app:text="需要展示的文字"
            app:textColor="@android:color/white"
            app:textSize="20sp"
            app:during="2000"
            app:type="typer"
            app:layout_constraintTop_toTopOf="parent"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintRight_toRightOf="parent"/>
```
## 代码中动态设置
```
    emergeTextView.setType(EmergeTextView.EmergeType.typer)
    emergeTextView.setText(str)
    emergeTextView.setTextColor(color)
    emergeTextView.setTextSize(size)
    //调用启动方法
    emergeTextView.start()
```

# Dependency
Step 1: Add it in your root build.gradle at the end of repositories:
```
    allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Step 2. Add the dependency
```
    dependencies {
	        implementation 'com.github.xunyang-yuhui:EmergeTextView:v1.0.3'
	}
```
