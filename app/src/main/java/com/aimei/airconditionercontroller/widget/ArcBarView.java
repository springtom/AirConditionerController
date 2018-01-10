package com.aimei.airconditionercontroller.widget;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xingchun on 17/11/30.
 */
public class ArcBarView extends android.support.v7.widget.AppCompatImageView {

    private static final String TAG = ArcBarView.class.getSimpleName();
    private int mTotalWidth, mTotalHeight;
    private int mCenterX, mCenterY;
    //底色画笔
    private Paint innerCirclePaint;

    private Paint outerCirclePaint;
    //进度条画笔
    private Paint mProgressPaint;

    private Paint textPaint;

    private Paint selectedText;

    //控制器画笔
    private Paint controllerPaint;
    private float[] pos;                // 当前点的实际位置
    private float[] tan;                // 当前点的tangent值,用于计算图片所需旋转的角度


    private int mCircleD = 120;


    private Context mContext;

    private int startAngle = 120;

    private int endAngle = 180;


    private int maxEndAngle = 420;

    private int strokeWidth = 3;

    private int interval = 3;

    private int startColor = Color.parseColor("#2B72E7");

    private int endColor = Color.parseColor("#BD0DE9");

    private SweepGradient sweepGradient;

    private List<String> textList = new ArrayList<>();

    private int lastIndex;

    private int index;


    private int mode;// 0-正常模式//    -1 - 不可操作模式

    public ArcBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public ArcBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initPaint();
        initData();
    }

    private void initData() {
        textList = new ArrayList<>();
        textList.add("17");
        textList.add("18");
        textList.add("19");
        textList.add("20");
        textList.add("21");
        textList.add("22");
        textList.add("23");
        textList.add("24");
        textList.add("25");
        textList.add("26");
        textList.add("27");
        textList.add("28");
        textList.add("29");
        textList.add("30");
    }


    private void initPaint() {
        pos = new float[2];
        tan = new float[2];

        innerCirclePaint = new Paint();
        outerCirclePaint = new Paint();
        textPaint = new Paint();
        selectedText = new Paint();
        mProgressPaint = new Paint();
        controllerPaint = new Paint();

        //抗锯齿
        innerCirclePaint.setAntiAlias(true);
        outerCirclePaint.setAntiAlias(true);
        textPaint.setAntiAlias(true);
        selectedText.setAntiAlias(true);
        mProgressPaint.setAntiAlias(true);
        controllerPaint.setAntiAlias(true);

        // 防抖动
        innerCirclePaint.setDither(true);
        outerCirclePaint.setDither(true);
        textPaint.setDither(true);
        selectedText.setDither(true);
        mProgressPaint.setDither(true);
        controllerPaint.setDither(true);

        // 开启图像过滤，对位图进行滤波处理。
        innerCirclePaint.setFilterBitmap(true);
        outerCirclePaint.setFilterBitmap(true);
        textPaint.setFilterBitmap(true);
        selectedText.setFilterBitmap(true);
        mProgressPaint.setFilterBitmap(true);
        controllerPaint.setFilterBitmap(true);

        innerCirclePaint.setColor(Color.parseColor("#000000"));
        outerCirclePaint.setColor(Color.parseColor("#4cffffff"));
        textPaint.setColor(Color.parseColor("#4cffffff"));
        mProgressPaint.setColor(Color.BLUE);
        selectedText.setColor(Color.parseColor("#ffffff"));
        controllerPaint.setColor(endColor);

        textPaint.setTextSize(50);
        selectedText.setTextSize(50);

        textPaint.setTextAlign(Paint.Align.CENTER);
        selectedText.setTextAlign(Paint.Align.CENTER);

        //空心圆
        innerCirclePaint.setStyle(Paint.Style.STROKE);
        outerCirclePaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setStyle(Paint.Style.STROKE);


        //圆半径
        innerCirclePaint.setStrokeWidth(mCircleD + 2 * interval);
        outerCirclePaint.setStrokeWidth(mCircleD + 2 * strokeWidth + 2 * interval);
        mProgressPaint.setStrokeWidth(mCircleD);

        //设置笔刷样式为圆形
        innerCirclePaint.setStrokeCap(Paint.Cap.ROUND);
        outerCirclePaint.setStrokeCap(Paint.Cap.ROUND);
        mProgressPaint.setStrokeCap(Paint.Cap.ROUND);

        //将绘制的内容显示在第一次绘制内容之上
        mProgressPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        controllerPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));

        controllerPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas去锯齿
        canvas.setDrawFilter(
                new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));

        //画外圆
        canvas.drawArc(
                new RectF(0 + mCircleD, 0 + mCircleD,
                        mTotalWidth - mCircleD, mTotalHeight - mCircleD),
                startAngle, maxEndAngle - startAngle, false, outerCirclePaint);

        //画内圆
        canvas.drawArc(
                new RectF(0 + mCircleD, 0 + mCircleD,
                        mTotalWidth - mCircleD, mTotalHeight - mCircleD),
                startAngle, maxEndAngle - startAngle, false, innerCirclePaint);

        //画文字
        drawText(canvas, textList, startAngle, maxEndAngle, textPaint);

        if (mode == 0) {
            //
            //画进度条
            int colorSweep[] = {startColor, (startColor + endColor) / 2, endColor};
            //设置渐变色
            sweepGradient = new SweepGradient(mCenterX, mCenterY, colorSweep, null);
            //按照圆心旋转
            Matrix matrix = new Matrix();
            int semicircleAngle = 0;
            if (mTotalWidth != 0 || mTotalHeight != 0) {
                semicircleAngle = (int) (180.0 * Math.asin((mCircleD) / (double) ((mTotalWidth + mTotalHeight) / 4)) / Math.PI);
            }
            if (endAngle <= startAngle) {
                endAngle = startAngle + 1;
            }
            matrix.setRotate(startAngle - semicircleAngle, mCenterX, mCenterY);
            sweepGradient.setLocalMatrix(matrix);
            mProgressPaint.setShader(sweepGradient);
            canvas.drawArc(
                    new RectF(0 + mCircleD, 0 + mCircleD,
                            mTotalWidth - mCircleD, mTotalHeight - mCircleD),
                    startAngle, endAngle - startAngle, false, mProgressPaint);


            //画控制器
            Path orbit = new Path();
            //通过Path类画一个内切圆弧路径
            orbit.addArc(
                    new RectF(0 + mCircleD, 0 + mCircleD,
                            mTotalWidth - mCircleD, mTotalHeight - mCircleD)
                    , startAngle, endAngle - startAngle);
            // 创建 PathMeasure
            PathMeasure measure = new PathMeasure(orbit, false);
            measure.getPosTan(measure.getLength() * 1, pos, tan);
            canvas.drawCircle(pos[0], pos[1], mCircleD / 2, controllerPaint);


            // 画选中刻度
            int r = (mTotalWidth + mTotalHeight) / 4 - mCircleD;
            float y = (float) (mCenterY - r + mCircleD / 4 - 10);
            float x = (float) (mCenterX);
            lastIndex = index;
            int perAngle;
            if (textList.size() > 1) {
                perAngle = (maxEndAngle - startAngle) / (textList.size() - 1);
            } else {
                perAngle = maxEndAngle - startAngle;
            }
            index = Math.round((endAngle - startAngle) / perAngle);
            String text = "";
            if (index >= 0 && index < textList.size()) {
                text = textList.get(index);
            }
            if (index != lastIndex && changeListener != null) {
                changeListener.changed(text);
            }
            drawText(canvas, text + "", x, y, selectedText, endAngle - 270);
        }

    }

    private void drawText(Canvas canvas, List<String> textList, int startAngle, int maxEndAngle, Paint textPaint) {
        if (textList == null)
            return;
        int perAngle;
        if (textList.size() > 1) {
            perAngle = (maxEndAngle - startAngle) / (textList.size() - 1);
        } else {
            perAngle = (maxEndAngle - startAngle);
        }

        int r = (mTotalWidth + mTotalHeight) / 4 - mCircleD;
        for (int i = 0; i < textList.size(); i++) {
            int angle = startAngle + perAngle * i;
            angle = angle % 360;
            float y = (float) (mCenterY - r + mCircleD / 4 - 10);
            float x = (float) (mCenterX);
            drawText(canvas, textList.get(i), x, y, textPaint, angle - 270);
        }
    }


    void drawText(Canvas canvas, String text, float x, float y, Paint paint, float angle) {
        if (angle != 0) {
            canvas.rotate(angle, mCenterX, mCenterY);
        }
        canvas.drawText(text, x, y, paint);
        if (angle != 0) {
            canvas.rotate(-angle, mCenterX, mCenterY);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTotalWidth = w;
        mTotalHeight = h;
        mCenterX = mTotalWidth / 2;
        mCenterY = mTotalHeight / 2;
    }

    float downX;
    float downY;
    float moveX;
    float moveY;
    boolean isTouchControll = false;
    int angle = endAngle;
    private int lastEndAngle;
    private int phase = 1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                isTouchControll = isTouchController(downX, downY);
                if (isTouchControll && mode == 0) {
                    angle = endAngle;
                    int initialPhasesAngle = angle % 360;
                    if (initialPhasesAngle > 0 && initialPhasesAngle <= 90) {
                        phase = 4;
                    } else if (initialPhasesAngle > 90 && initialPhasesAngle <= 180) {
                        phase = 3;
                    } else if (initialPhasesAngle > 180 && initialPhasesAngle <= 270) {
                        phase = 2;
                    } else {
                        phase = 1;
                    }

                }
                break;
            case MotionEvent.ACTION_MOVE:

                moveX = event.getX();
                moveY = event.getY();
                if (isTouchControll && mode == 0) {
                    lastEndAngle = angle;
                    int lastEndAngleRings = (int) Math.floor(lastEndAngle / 360f);
                    if (moveX < mCenterX && moveY > mCenterY) {//3象限
                        angle = lastEndAngleRings * 360 + 90 + (int) (Math.atan(Math.abs((mCenterX - moveX) / (mCenterY - moveY))) * 180 / Math.PI);
                        phase = 3;
                    } else if (moveX < mCenterX && moveY == mCenterY) {
                    } else if (moveX < mCenterX && moveY < mCenterY) {//2象限
                        angle = lastEndAngleRings * 360 + 180 + (int) (Math.atan(Math.abs((mCenterY - moveY) / (mCenterX - moveX))) * 180 / Math.PI);
                        phase = 2;
                    } else if (moveX == mCenterX && moveY < mCenterY) {
                    } else if (moveX > mCenterX && moveY < mCenterY) {//1象限
                        if (phase == 4) {
                            lastEndAngleRings--;
                        }
                        angle = lastEndAngleRings * 360 + 180 + 90 + (int) (Math.atan(Math.abs((mCenterX - moveX) / (mCenterY - moveY))) * 180 / Math.PI);
                        phase = 1;
                    } else if (moveX > mCenterX && moveY == mCenterY) {
                    } else if (moveX > mCenterX && moveY > mCenterY) {//4象限
                        if (phase == 1) {
                            lastEndAngleRings++;
                        }
                        angle = lastEndAngleRings * 360 + (int) (Math.atan(Math.abs((mCenterY - moveY) / (mCenterX - moveX))) * 180 / Math.PI);
                        phase = 4;
                    } else if (moveX == mCenterX && moveY > mCenterY) {
                    }
                    endAngle = angle;
                    if (endAngle <= startAngle) {
                        endAngle = startAngle + 1;
                    }
                    if (endAngle > maxEndAngle) {
                        endAngle = maxEndAngle;
                    }
                    Log.w(TAG, "endAngle=" + endAngle);
                    Log.w(TAG, "maxEndAngle=" + maxEndAngle);
                    Log.w(TAG, "startAngle=" + startAngle);
                    postInvalidate();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    private boolean isTouchController(float downX, float downY) {
        double d = Math.sqrt(Math.pow(downX - pos[0], 2) + Math.pow(downY - pos[1], 2));
        if (d <= mCircleD) {
            return true;
        }
        return false;
    }

    public void setChangeListener(ChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    private ChangeListener changeListener;

    public interface ChangeListener {
        void changed(String text);
    }

    public void setListData(List<String> textList) {
        this.textList = textList;
    }

    public void setText(String text) {
        if (!TextUtils.isEmpty(text)) {
            for (int i = 0; i < textList.size(); i++) {
                if (text.equals(textList.get(i))) {
                    int perAngle;
                    if (textList.size() > 1) {
                        perAngle = (maxEndAngle - startAngle) / (textList.size() - 1);
                    } else {
                        perAngle = maxEndAngle - startAngle;
                    }
                    endAngle = perAngle * i + startAngle;
                    postInvalidate();
                    break;
                }
            }
        }
    }

    public void setTextColor(int color) {
        if (textPaint != null) {
            textPaint.setColor(color);
        }
        postInvalidate();
    }

    public void setSelectedText(int color) {
        if (selectedText != null) {
            selectedText.setColor(color);
        }
        postInvalidate();
    }

    public void setTextSize(float size) {
        if (textPaint != null) {
            textPaint.setTextSize(size);
        }
        postInvalidate();
    }

    public void setSelectedTextSize(float size) {
        if (selectedText != null) {
            selectedText.setTextSize(size);
        }
        postInvalidate();
    }

    public void setBorderColor(int color) {
        if (outerCirclePaint != null) {
            outerCirclePaint.setColor(color);
        }
        postInvalidate();
    }

//    public void setBorderWidth(int width) {
//        strokeWidth = width;
//        if (outerCirclePaint != null) {
//            outerCirclePaint.setStrokeWidth(mCircleD + 2 * strokeWidth);
//        }
//    }

    public void setRingColor(int color) {
        if (innerCirclePaint != null) {
            innerCirclePaint.setColor(color);
        }
        postInvalidate();
    }

    public void setStartColor(int color) {
        startColor = color;
    }

    public void setEndColor(int color) {
        endColor = color;
    }

    public void setControllerColor(int color) {
        controllerPaint.setColor(color);
    }

    public void setMode(int mode) {
        this.mode = mode;
        postInvalidate();

    }


    public void setConfiguration(Configuration configuration) {
        mCircleD = configuration.getRingWidth();
        strokeWidth = configuration.getBorderWidth();
        this.maxEndAngle = configuration.getMaxAngle();
        this.startAngle = configuration.getStartAngle();
        this.interval = configuration.getInterval();
        if (maxEndAngle <= startAngle) {
            throw new IllegalStateException(
                    "The maxEndAngle must greater than the startAngle.");
        }
        if (maxEndAngle - startAngle > 360) {
            throw new IllegalStateException(
                    "the maxEndAngle can't be greater than startAngle, 360 degrees.");
        }
        if (outerCirclePaint != null) {
            outerCirclePaint.setStrokeWidth(mCircleD + 2 * strokeWidth + 2 * interval);
        }
        if (innerCirclePaint != null) {
            innerCirclePaint.setStrokeWidth(mCircleD + 2 * interval);
        }
        if (mProgressPaint != null) {
            mProgressPaint.setStrokeWidth(mCircleD);
        }
        postInvalidate();
    }

    public interface Configuration {
        int getRingWidth();

        int getBorderWidth();

        int getInterval();

        int getStartAngle();

        int getMaxAngle();
    }
}
