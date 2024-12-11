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

        // 1. 绘制跑道
        canvas.drawPath(trackPath, trackPaint);

        // 2. 遍历所有用户，绘制路径和头像
        for (User user : users) {
            //drawUserPath(canvas, user);
            drawUserAvatar(canvas, user);
        }
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
        Bitmap output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Rect rect = new Rect(0, 0, size, size);
        canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, null, rect, paint);
        return output;
    }
}
