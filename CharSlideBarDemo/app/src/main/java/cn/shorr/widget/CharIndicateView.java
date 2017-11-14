package cn.shorr.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import cn.shorr.demo.R;

/**
 * 字符指示View
 * Created by Shorr on 2016/12/7.
 */
public class CharIndicateView extends TextView {

    /*可自定义相关属性*/
    private int mIndicateTextSize = 50;  //字体大小（此处为默认值）
    private int mIndicateTextColor = Color.BLACK;  //字体颜色（此处为默认值）
    private int mBackgroundColor = Color.GRAY;  //背景色(此处为默认值)
    private int mBackgroundRadius = 10;  //矩形背景圆角半径(此处为默认值)

    public CharIndicateView(Context context) {
        this(context, null);
    }

    public CharIndicateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //初始化View相关自定义属性
        initFromAttributes(context, attrs);
        //初始化
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
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CharIndicateView, 0, 0);
        try {
            mIndicateTextSize = a.getDimensionPixelSize(R.styleable.CharIndicateView_indicateTextSize, mIndicateTextSize);
            mIndicateTextColor = a.getColor(R.styleable.CharIndicateView_indicateTextColor, mIndicateTextColor);
            mBackgroundColor = a.getColor(R.styleable.CharIndicateView_indicateBackground, mBackgroundColor);
            mBackgroundRadius = a.getDimensionPixelSize(R.styleable.CharIndicateView_indicateBackgroundRadius, mBackgroundRadius);
        } finally {
            //回收TypedArray
            a.recycle();
        }
    }

    /**
     * 初始化操作
     */
    private void init() {
        //设置圆角矩形背景
        //  float[] outerRadii = {10, 10, 10, 10, 10, 10, 10, 10};
        float[] outerRadii = new float[8];
        for (int i = 0; i < outerRadii.length; i++) {
            outerRadii[i] = mBackgroundRadius;
        }
        RoundRectShape shape = new RoundRectShape(outerRadii, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(shape);
        shapeDrawable.getPaint().setColor(mBackgroundColor);
        //将圆角矩形背景设置到当前View
        this.setBackgroundDrawable(shapeDrawable);
        //文本居中显示
        this.setGravity(Gravity.CENTER);
        //设置字体大小
        this.setTextSize(mIndicateTextSize);
        //设置字体颜色
        this.setTextColor(mIndicateTextColor);
        //默认不显示该布局
        this.setVisibility(View.GONE);
    }

    /**
     * 展示选中字符
     *
     * @param selectedChar 要显示的字符
     */
    public void showSelectedChar(String selectedChar) {
        this.setText(selectedChar);
    }

}
