package com.xaye.diyview.view.track;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.xaye.diyview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Author xaye
 *
 * @date: 2024/12/11
 */
public class TrackView extends View {
    private static final float TOTAL_DISTANCE = 400f; // 跑道总长度 400 米

    private Paint trackPaint;       // 跑道画笔
    private Paint pathPaint;        // 运动路径画笔
    private Paint userPaint;        // 用户头像画笔
    private Path trackPath;         // 跑道路径
    private RectF trackRectF;       // 跑道矩形边界
    private float trackWidth = 50f; // 跑道宽度
    private PathMeasure pathMeasure;// 用于测量路径长度

    private List<User> users = new ArrayList<>(); // 存储所有用户

    public TrackView(Context context) {
        this(context, null);
    }

    public TrackView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 初始化画笔和路径
     */
    private void init() {
        trackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        trackPaint.setColor(Color.parseColor("#FF6347")); // 跑道颜色
        trackPaint.setStyle(Paint.Style.STROKE);
        trackPaint.setStrokeWidth(trackWidth);

        pathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pathPaint.setColor(Color.parseColor("#4CAF50")); // 用户运动路径颜色
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeWidth(8f);

        userPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        trackPath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float left = trackWidth / 2f + 20;
        float top = trackWidth / 2f + 20;
        float right = w - trackWidth / 2f - 20;
        float bottom = h - trackWidth / 2f - 20;
        trackRectF = new RectF(left, top, right, bottom);
        trackPath.reset();
        trackPath.addRoundRect(trackRectF, 300, 300, Path.Direction.CW);

        pathMeasure = new PathMeasure(trackPath, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.d("onDraw", "start >>>");
        // 1. 绘制跑道
        canvas.drawPath(trackPath, trackPaint);

        // 2. 遍历所有用户，绘制路径和头像
        for (User user : users) {
            //drawUserPath(canvas, user);
            drawUserAvatar(canvas, user);
        }
        Log.d("onDraw", "end >>>");
    }

    /**
     * 绘制用户的路径
     */
    private void drawUserPath(Canvas canvas, User user) {
        user.path.reset();
        float distanceOnPath = user.getCurrentDistance() / TOTAL_DISTANCE * pathMeasure.getLength();
        pathMeasure.getSegment(0, distanceOnPath, user.path, true);
        canvas.drawPath(user.path, pathPaint);
    }

    /**
     * 绘制用户的头像
     */
    private void drawUserAvatar(Canvas canvas, User user) {
        float[] pos = new float[2];
        float[] tan = new float[2];
        float distanceOnPath = user.getCurrentDistance() / TOTAL_DISTANCE * pathMeasure.getLength();
        pathMeasure.getPosTan(distanceOnPath, pos, tan);

        float radius = trackWidth * 0.4f;
        canvas.drawBitmap(
                user.avatar,
                pos[0] - radius,
                pos[1] - radius,
                userPaint
        );
    }

    /**
     * 增加一个用户
     */
    public void addUser(Bitmap avatar, float initialSpeed, float acceleration) {
        User user = new User();
        user.avatar = createCircularBitmap(avatar, (int) (trackWidth * 0.8f));
        user.setSpeed(initialSpeed);
        user.setAcceleration(acceleration);
        users.add(user);
    }

    /**
     * 更新用户的运动状态
     */
    public void updateUserDistance() {
        for (User user : users) {
            user.updateDistance();
        }
        invalidate();
    }

    /**
     * 将头像裁剪为圆形
     */
    private Bitmap createCircularBitmap(Bitmap bitmap, int size) {
        //1. 这将作为新的圆形 Bitmap 的画布，后面我们会在这张画布上“绘制”一个圆形的图片。
        Bitmap output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);//创建一个空白的 Bitmap

        //2. 画布 canvas 会将所有的绘图操作（如画圆、画图像等）直接绘制到 Bitmap 上。
        Canvas canvas = new Canvas(output);

        //3. 创建 Paint 画笔对象  Paint.ANTI_ALIAS_FLAG 是抗锯齿标志，可以让绘制的边缘更平滑（消除锯齿效果）
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        //4. 这个矩形表示图片的绘制区域，我们稍后会将图片填充到这个矩形中。
        Rect rect = new Rect(0, 0, size, size);

        //5. 在 output 中绘制一个圆（默认颜色是透明的，因为没有设置 paint.setColor）。
        canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint);

        //6. PorterDuffXfermode(PorterDuff.Mode.SRC_IN) 表示的意思是：
        //只保留源图像和目标图像的交集部分，并显示源图像的像素。
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        //7. 由于在第 6 步中设置了 PorterDuff.Mode.SRC_IN，只有前面圆的部分会显示，圆外的部分将被裁剪掉。
        //第一个参数 bitmap：要绘制的原始图片。
        //第二个参数 null：表示不裁剪原始 bitmap，使用整个图片。
        //第三个参数 rect：目标矩形，表示将整个图片缩放并填充到这个矩形中。
        //第四个参数 paint：前面设置了混合模式的画笔。
        canvas.drawBitmap(bitmap, null, rect, paint); //将原始的 bitmap 绘制到 rect 矩形区域中。
        return output;
    }
}
