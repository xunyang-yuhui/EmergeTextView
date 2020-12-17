package com.yu.emergetextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 花式文字展示
 * 打字机式
 * 随机显示
 */

public class EmergeTextView extends View {
    private TextPaint mPaint;
    private int type;
    private String text;
    private int textSize;                              //字体大小
    private int textColor;                    //字体颜色
    private int during;                              //动画时间
    private int mWidth;                    //控件宽高
    private float sendMsgTime;                      //发送msg时间
    private List<CharConfig> charConfigList = new ArrayList<>();
    private UIThread uiThread;
    private int currentTime = 0;            //当前执行次数
    private int maxCircleTime = 16;         //最多循环次数
    private int finishCircleTime = 8;       //从透明到不透明循环次数
    private float textWidth;
    private Typeface typeface;

    public EmergeTextView(Context context) {
        this(context, null);
    }

    public EmergeTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmergeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EmergeTextView);
        type = typedArray.getInt(R.styleable.EmergeTextView_type, 0);
        text = typedArray.getString(R.styleable.EmergeTextView_text);
        textColor = typedArray.getInt(R.styleable.EmergeTextView_textColor, Color.BLACK);
        textSize = typedArray.getDimensionPixelSize(R.styleable.EmergeTextView_textSize, 10);
        during = typedArray.getInt(R.styleable.EmergeTextView_during, 2000);
        init();
        initCharConfig();

        typedArray.recycle();
    }

    private void init() {
        mPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(textSize);
        mPaint.setColor(textColor);
        if (typeface != null) {
            mPaint.setTypeface(typeface);
        }
    }

    //初始化字符串
    private void initCharConfig() {
        if (text != null && text.length() != 0) {
            char[] chars = text.toCharArray();
            for (char c : chars) {
                CharConfig config = new CharConfig();
                config.text = c;
                if ("\n".equals(String.valueOf(c))) {
                    config.textWidth = 0;
                    config.circleTime = Integer.MAX_VALUE;
                    charConfigList.add(config);
                    continue;
                }
                if (" ".equals(String.valueOf(c))) {
                    config.circleTime = Integer.MAX_VALUE;
                } else {
                    config.circleTime = (int) (Math.random() * maxCircleTime);
                }
                config.textWidth = mPaint.measureText(String.valueOf(c));
                charConfigList.add(config);
            }
        }

        if (type == EmergeType.typer) {
            sendMsgTime = (float) during / charConfigList.size();
        } else {
            sendMsgTime = (float) during / maxCircleTime;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasureHeight(heightMeasureSpec));
    }

    //获取控件高度
    private int getMeasureHeight(int heightMeasureSpec) {
        int height = (int) mPaint.getFontMetrics().bottom - (int) mPaint.getFontMetrics().top;       //一行字符显示的最大高度
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            height = size;
        } else {
            if (text == null || text.length() == 0) {
                return 0;
            }
            int showWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
            int textWidth = 0;
            for (CharConfig c : charConfigList) {
                if ("\n".equals(String.valueOf(c.text))) {
                    height = height + (int) mPaint.getFontMetrics().bottom - (int) mPaint.getFontMetrics().top;
                    textWidth = 0;
                    continue;
                }
                //加上当前字符的宽度，是否超过了可显示的宽度，若超过，则换行，重新计算宽度
                if (textWidth + c.textWidth > showWidth) {
                    height = height + (int) mPaint.getFontMetrics().bottom - (int) mPaint.getFontMetrics().top;
                    textWidth = 0;
                }
                textWidth += c.textWidth;
            }
            height = height + getPaddingRight() + getPaddingBottom();
        }
        return height;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float lineHeight = getPaddingTop() - mPaint.getFontMetrics().top;
        textWidth = getPaddingLeft();

        //打字机模式（文字逐个显示）
        if (type == EmergeType.typer) {
            for (int index = 0; index < currentTime; index++) {
                CharConfig config = charConfigList.get(index);
                if ("\n".equals(String.valueOf(config.text))) {
                    textWidth = getPaddingLeft();
                    lineHeight = lineHeight + mPaint.getFontMetrics().bottom - mPaint.getFontMetrics().top;
                    continue;
                }
                if (textWidth + config.textWidth > mWidth - getPaddingRight()) {
                    textWidth = getPaddingLeft();
                    lineHeight = lineHeight + mPaint.getFontMetrics().bottom - mPaint.getFontMetrics().top;
                }
                canvas.drawText(String.valueOf(config.text), textWidth, lineHeight, mPaint);
                textWidth = textWidth + config.textWidth;
            }
        } else {
            //随机显示
            for (CharConfig cc : charConfigList) {
                int alpha = 0;
                int delayTime = currentTime - cc.circleTime;
                if (delayTime > finishCircleTime) {
                    alpha = 255;
                } else if (delayTime > 0) {
                    alpha = 255 * delayTime / finishCircleTime;
                }
                if ("\n".equals(String.valueOf(cc.text))) {
                    textWidth = getPaddingLeft();
                    lineHeight = lineHeight + mPaint.getFontMetrics().bottom - mPaint.getFontMetrics().top;
                    continue;
                }
                if (textWidth + cc.textWidth > mWidth - getPaddingRight()) {
                    textWidth = getPaddingLeft();
                    //加上 文字显示的高度
                    lineHeight = lineHeight + mPaint.getFontMetrics().bottom - mPaint.getFontMetrics().top;

                }
                if (alpha > 0) {
                    canvas.drawText(String.valueOf(cc.text), textWidth, lineHeight, mPaint);
                }
                textWidth = textWidth + cc.textWidth;
            }
        }
        mPaint.setAlpha(255);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (charConfigList.size() != 0) {
            uiThread = new UIThread();
            uiThread.start();
        }
    }

    //设置文字
    public void setText(String string) {
        text = string;
    }

    //设置文字颜色
    public void setTextColor(int color) {
        textColor = color;
    }

    //设置文字大小
    public void setTextSize(int textSize) {
        this.textSize = sp2px(getContext(), textSize);
    }

    //设置类型
    public void setType(int emergeType) {
        type = emergeType;
    }

    //设置字体
    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    //设置动画时长
    public void setDuring(int during) {
        this.during = during;
    }

    public void start() {
        charConfigList.clear();
        currentTime = 0;

        init();
        initCharConfig();
        requestLayout();
        if (uiThread != null && uiThread.isAlive()) {
            uiThread.interrupt();
        }
        uiThread = new UIThread();
        uiThread.start();
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    class UIThread extends Thread {
        public UIThread() {
        }

        @Override
        public void run() {
            try {
                int sendTime = type == EmergeType.typer ? charConfigList.size() : maxCircleTime + finishCircleTime;
                while (currentTime < sendTime) {
                    sleep((long) sendMsgTime);
                    Message msg = Message.obtain();
                    handler.sendEmptyMessage(msg.what);
                    currentTime++;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            invalidate();
        }
    };

    class CharConfig {
        char text;              //文字
        int circleTime;              //循环次数
        float textWidth;          //文字宽度
    }

    //展示类型
    public interface EmergeType {
        int typer = 0;
        int randrom = 1;
    }
}