package cn.shorr.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import cn.shorr.demo.R;

/**
 * 字符索引栏
 * Created by Shorr on 2016/12/7.
 */
public class CharSlideBar extends View {

    /*可自定义相关属性*/
    private String mChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ#";  //要显示的所有字符(此处为默认值)
    private int mBackgroundColor = Color.GRAY;  //背景色(此处为默认值)
    private int mCharTextSize = 30;  //字体大小（此处为默认值）
    private int mCharTextColor = Color.BLACK;  //字体颜色（此处为默认值）

    private int mCanvasColor = Color.TRANSPARENT;  //画布颜色
    private int mLastSelectedPosition = -1;  //记录上次选中的位置

    private Paint mPaint;  //画笔
    private Paint.FontMetricsInt mFontMetricsInt;  //字体度量值
    private CharIndicateView mCharIndicateView;  //字符指示视图
    private OnSelectedListener mOnSelectedListener;  //字符选中的监听

    public CharSlideBar(Context context) {
        this(context, null);
    }

    public CharSlideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        //初始化View相关自定义属性
        initFromAttributes(context, attrs);
        //初始化相关操作
        init();
    }


    /**
     * 初始化View相关自定义属性
     *
     * @param context
     * @param attrs
     */
    private void initFromAttributes(Context context, AttributeSet attrs) {
        //获取相关View属性
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CharSlideBar, 0, 0);
        try {
            String chars = a.getString(R.styleable.CharSlideBar_barChars);
            mChars = chars == null ? mChars : chars;
            mCharTextSize = a.getDimensionPixelSize(R.styleable.CharSlideBar_barTextSize, mCharTextSize);
            mCharTextColor = a.getColor(R.styleable.CharSlideBar_barTextColor, mCharTextColor);
            mBackgroundColor = a.getColor(R.styleable.CharSlideBar_barBackground, mBackgroundColor);
        } finally {
            //回收TypedArray
            a.recycle();
        }
    }

    /**
     * 初始化操作
     */
    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //文字水平居中显示
        mPaint.setTextAlign(Paint.Align.CENTER);
        //设置字体大小
        mPaint.setTextSize(mCharTextSize);
        //设置字体颜色
        mPaint.setColor(mCharTextColor);
        //获取FontMetricsInt
        mFontMetricsInt = mPaint.getFontMetricsInt();
    }

    /**
     * 和字符指示View建立联系
     *
     * @param charIndicateView
     */
    public void setupWithIndicateView(CharIndicateView charIndicateView) {
        mCharIndicateView = charIndicateView;
    }

    /**
     * 设置监听事件
     *
     * @param onSelectedListener
     */
    public void setOnSelectedListener(OnSelectedListener onSelectedListener) {
        mOnSelectedListener = onSelectedListener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //设置索引栏背景色
        canvas.drawColor(mCanvasColor);
        //单个字符所占的高度
        float singleCharHeight = ((float) getHeight()) / mChars.length();
        //字符要显示的x值
        float charX = ((float) getWidth()) / 2;
        //计算出字体高度
        int fontHeight = mFontMetricsInt.descent - mFontMetricsInt.ascent;
        //计算出竖直方向居中时的偏移量
        float centerYOffset = singleCharHeight / 2 - (-mFontMetricsInt.ascent - fontHeight / 2);

        //根据x、y值画出所有字符
        for (int i = 0; i < mChars.length(); i++) {
            canvas.drawText(mChars.substring(i, i + 1), charX, singleCharHeight * (i + 1) - centerYOffset, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:  //手指按下
                //设置画布颜色
                mCanvasColor = mBackgroundColor;
                //重新绘制
                invalidate();
                //显示字符指示View
                if (mCharIndicateView != null) {
                    mCharIndicateView.setVisibility(View.VISIBLE);
                }
                //根据Y值得到选中的位置
                selectedPositionByY(event.getY());

                return true;
            case MotionEvent.ACTION_MOVE:  //手指滑动
                //根据Y值得到选中的位置
                selectedPositionByY(event.getY());

                return true;
            case MotionEvent.ACTION_UP:  //手指抬起
                //画布颜色设为透明
                mCanvasColor = Color.TRANSPARENT;
                //重新绘制
                invalidate();
                //隐藏字符指示View
                if (mCharIndicateView != null) {
                    mCharIndicateView.setVisibility(View.GONE);
                }
                //复位记录上次选中位置的值
                mLastSelectedPosition = -1;

                return true;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 根据View的Y值得到选中的字符位置
     *
     * @param y
     */
    private void selectedPositionByY(float y) {
        //若滑动范围超出索引栏的高度范围，不再计算位置
        if (y < 0 || y > getHeight()) {
            return;
        }
        //单个字符所占的高度
        float singleCharHeight = ((float) getHeight()) / mChars.length();
        //计算出当前选中的位置
        int position = (int) (y / singleCharHeight);
        //防止重复显示
        if (position != mLastSelectedPosition) {
            //根据选中位置，获取相应位置的字符
            String selectedChar = mChars.substring(position, position + 1);
            //展示选中的字符
            if (mCharIndicateView != null) {
                mCharIndicateView.showSelectedChar(selectedChar);
            }
            //设置监听的回调方法
            if (mOnSelectedListener != null) {
                mOnSelectedListener.onSelected(position, selectedChar);
            }
            //记录下当前位置
            mLastSelectedPosition = position;
        }

    }

    /**
     * 字符选中的监听事件
     */
    public interface OnSelectedListener {
        /**
         * 选中的回调方法
         *
         * @param position     选中的位置
         * @param selectedChar 选中的字符
         */
        public void onSelected(int position, String selectedChar);
    }
}
