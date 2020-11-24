[![](https://jitpack.io/v/xunyang-yuhui/EmergeTextView.svg)](https://jitpack.io/#xunyang-yuhui/EmergeTextView)

# 效果图展示
https://s3.ax1x.com/2020/11/24/DNekeH.gif

思路参考了 https://github.com/xmuSistone/android-character-animation 

# 使用方法
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
