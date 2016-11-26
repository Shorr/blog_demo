package cn.shorr.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * 自定义简易View
 * Created by Shorr on 2016/11/20.
 */

public class CustomView extends View {

    private int mContentWidth;  //默认宽度,单位px
    private int mContentHeight;  //默认高度,单位px
    private int mContentColor;  //View的颜色
    private int mGravity;  //View的Gravity属性

    private enum Measure {  //View测量宽、高的枚举
        WIDTH, HEIGHT
    }

    private Paint mPaint;  //定义一个画笔

    public CustomView(Context context) {
        this(context, null);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //初始化相关自定义属性
        initFromAttributes(context, attrs);
        //初始化View
        initView();
    }

    /**
     * 初始化View相关自定义属性
     *
     * @param context
     * @param attrs
     */
    private void initFromAttributes(Context context, AttributeSet attrs) {
        //获取相关View属性
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomView, 0, 0);

        try {
            mContentWidth = a.getDimensionPixelSize(R.styleable.CustomView_contentWidth, 0);
            mContentHeight = a.getDimensionPixelOffset(R.styleable.CustomView_contentHeight, 0);
            mContentColor = a.getColor(R.styleable.CustomView_contentColor, Color.TRANSPARENT);
            mGravity = a.getInteger(R.styleable.CustomView_gravity, -1);
        } finally {
            //回收TypedArray
            a.recycle();
        }
    }

    /**
     * 初始化View操作
     */
    private void initView() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //设置画笔颜色
        mPaint.setColor(mContentColor);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取宽高的尺寸
        int width = getMeasureSize(Measure.WIDTH, mContentWidth, widthMeasureSpec);
        int height = getMeasureSize(Measure.HEIGHT, mContentHeight, heightMeasureSpec);
        //设置测量尺寸
        setMeasuredDimension(width, height);
    }

    /**
     * 得到宽度测量的尺寸大小
     *
     * @param measure     测量的方向（宽高）
     * @param defalutSize 默认尺寸大小
     * @param measureSpec 测量的规则
     * @return 返回测量的尺寸
     */
    private int getMeasureSize(Measure measure, int defalutSize, int measureSpec) {
        int result = defalutSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:  //无限制大小
                result = defalutSize;
                break;
            case MeasureSpec.AT_MOST:  //对应wrap_content
                //如果设置了gravity属性，则忽略padding属性
                if (mGravity != -1) {
                    result = defalutSize;
                    break;
                }
                if (measure == Measure.WIDTH) {
                    //测量的宽
                    result = getPaddingLeft() + defalutSize + getPaddingRight();
                } else if (measure == Measure.HEIGHT) {
                    //测量的高
                    result = getPaddingTop() + defalutSize + getPaddingBottom();
                }
                break;
            case MeasureSpec.EXACTLY:  //对应match_parent or dp/px
                result = specSize;
                break;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //获取测量后的宽高
        int width = getWidth();
        int height = getHeight();
        //获取View的Padding
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        //设置ContentView的Rect
        Rect rect = null;
        if (mGravity == -1) {  //如果没有设置gravity属性，设置padding属性
            rect = new Rect(paddingLeft, paddingTop, width - paddingRight, height - paddingBottom);
        } else {  //如果设置gravity属性，不设置padding属性
            rect = getContentRect(width, height, mGravity);
        }
        //绘制ContentView
        canvas.drawRect(rect, mPaint);
    }

    /**
     * 获取ContentView的Rect
     *
     * @param width   View的Width
     * @param height  View的Height
     * @param gravity View的Gravity
     * @return
     */
    private Rect getContentRect(int width, int height, int gravity) {
        Rect rect = null;
        switch (gravity) {
            case 0:  //left
                rect = new Rect(0, 0,
                        width - (width - mContentWidth), height - (height - mContentHeight));
                break;
            case 1:  //right
                rect = new Rect(width - mContentWidth, 0,
                        height, height - (height - mContentHeight));
                break;
            case 2:  //center
                rect = new Rect((width - mContentWidth) / 2, (height - mContentHeight) / 2,
                        width - ((width - mContentWidth) / 2), height - ((height - mContentHeight) / 2));
                break;
            default:

                break;
        }

        return rect;
    }


}
