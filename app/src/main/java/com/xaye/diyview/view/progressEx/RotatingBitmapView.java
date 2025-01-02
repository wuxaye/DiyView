package com.xaye.diyview.view.progressEx;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.xaye.diyview.R;

/**
 * Author xaye
 *
 * @date: 2025/1/2
 */
public class RotatingBitmapView extends View {

    private Paint circlePaint;
    private float centerX, centerY;
    private float radius;
    private float angle = 0f; // 当前旋转角度
    private Bitmap bitmap;
    private float bitmapWidth; // Bitmap的宽度
    private float bitmapHeight; // Bitmap的高度

    public RotatingBitmapView(Context context) {
        super(context);
        init();
    }

    public RotatingBitmapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RotatingBitmapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(Color.GRAY); // 圆环颜色
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(5); // 圆环宽度

        // 加载Bitmap
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.point); // 替换为你的Bitmap资源
        bitmapWidth = bitmap.getWidth();
        bitmapHeight = bitmap.getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘制圆环轨迹
        canvas.drawCircle(centerX, centerY, radius, circlePaint);

        // 确保Bitmap的右边上的中心点在圆上
        float rightCenterX = centerX + radius * (float) Math.cos(Math.toRadians(angle));
        float rightCenterY = centerY + radius * (float) Math.sin(Math.toRadians(angle));

        // 计算Bitmap左上角的位置，使得右边上的中心点位于计算出的坐标
        float left = rightCenterX - bitmapWidth;
        float top = rightCenterY - bitmapHeight / 2;

        // 保存画布状态
        canvas.save();

        // 将画布旋转，使得Bitmap对齐到半径上
        canvas.rotate(angle, rightCenterX, rightCenterY);

        // 绘制Bitmap
        canvas.drawBitmap(bitmap, left, top, null);

        // 恢复画布状态
        canvas.restore();

        // 更新角度，控制旋转速度
        angle += 2; // 控制旋转的速度（2为每次旋转的角度增量）
        if (angle >= 360) {
            angle = 0;
        }

        // 再次绘制，形成动画
        //postInvalidateDelayed(30); // 控制刷新速度（30毫秒刷新一次）
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 计算中心点和半径
        centerX = w / 2f;
        centerY = h / 2f;
        radius = Math.min(centerX, centerY) - 20; // 给Bitmap的宽度留出空间
    }
}





