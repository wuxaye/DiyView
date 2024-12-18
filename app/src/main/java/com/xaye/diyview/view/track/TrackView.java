package com.xaye.diyview.view.track;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
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
    private static final float BUBBLE_HEIGHT = 30f; // 气泡的高度
    private static final float BUBBLE_PADDING = 10f; // 气泡的内边距
    private static final float BUBBLE_RADIUS = 20f; // 气泡的圆角半径
    private static final float BUBBLE_ARROW_HEIGHT = 6f; // 箭头的高度
    private static final float BUBBLE_OFFSET = 4f; // 气泡与头像的距离

    private User highlightedUser; // 当前高亮的用户
    private long highlightTime; // 记录高亮的时间戳
    private static final long HIGHLIGHT_DURATION = 3000; // 高亮气泡显示的时长（毫秒）

    private Paint trackPaint;       // 跑道画笔
    private Paint pathPaint;        // 运动路径画笔
    private Paint userPaint;        // 用户头像画笔
    private Paint bubblePaint; // 气泡画笔
    private Paint textPaint; // 文字画笔
    private Paint dashedLinePaint; // 虚线画笔
    private Path trackPath;         // 跑道路径
    private Path dashedPath; // 中间的虚线路径
    private RectF trackRectF;       // 跑道矩形边界
    private RectF innerTrackRectF;  // 跑道中间的虚线矩形
    private float trackWidth = 50f; // 跑道宽度
    private PathMeasure pathMeasure;// 用于测量路径长度

    // 复用的变量，减少内存分配
    private float[] position = new float[2];
    private RectF bubbleRectF = new RectF();
    private Paint.FontMetrics fontMetrics;
    private Path bubbleArrowPath = new Path();

    private List<User> users = new ArrayList<>(); // 存储所有用户

    private boolean isRunning = false; // 控制刷新状态

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

        bubblePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bubblePaint.setColor(Color.parseColor("#FF9800")); // 气泡颜色

        // 2. 初始化虚线画笔
        dashedLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dashedLinePaint.setStyle(Paint.Style.STROKE);
        dashedLinePaint.setColor(Color.WHITE); // 虚线的颜色为白色
        dashedLinePaint.setStrokeWidth(2f); // 虚线的宽度
        dashedLinePaint.setPathEffect(new DashPathEffect(new float[]{20, 15}, 0)); // 每段 20px 线，15px 空白


        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(14f);
        fontMetrics = textPaint.getFontMetrics();

        trackPath = new Path();
        dashedPath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //计算跑道的外边界矩形 (RectF)
        createTrackPath(w, h);
        createDashedPath();

        // 4. 生成虚线路径（跑道中心的虚线）
        dashedPath.reset();
        dashedPath.addRoundRect(trackRectF, 300, 300, Path.Direction.CW);
    }

    /**
     * 创建跑道路径
     */
    private void createTrackPath(int w, int h) {
        float left = trackWidth / 2f + 50;
        float top = trackWidth / 2f + 50;
        float right = w - trackWidth / 2f - 50;
        float bottom = h - trackWidth / 2f - 50;
        trackRectF = new RectF(left, top, right, bottom);
        trackPath.reset();//清空之前的路径，确保不会影响新的路径。

        //addRoundRect 将 trackRectF 矩形变成一个带有圆角的矩形路径。
        //300, 300 是矩形的水平和垂直的圆角半径，这使得操场的跑道具有平滑的弯曲效果。
        //Path.Direction.CW 表示顺时针绘制路径。
        trackPath.addRoundRect(trackRectF, 300, 300, Path.Direction.CW);

        //pathMeasure 用于测量路径的长度和位置。
        //trackPath 是路径，false 表示不闭合路径（但由于路径已经是一个完整的圆形，这里其实无关紧要）。
        pathMeasure = new PathMeasure(trackPath, false);//pathMeasure 用于计算用户在跑道上的实时运动轨迹，以便在跑道上显示用户的当前位置。
    }

    /**
     * 创建跑道中间的虚线路径
     */
    private void createDashedPath() {
        float offset = trackWidth / 4f; // 中间的偏移量
        innerTrackRectF = new RectF(
                trackRectF.left + offset,
                trackRectF.top + offset,
                trackRectF.right - offset,
                trackRectF.bottom - offset
        );

        dashedPath.reset();
        dashedPath.addRoundRect(innerTrackRectF, 300, 300, Path.Direction.CW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Log.i("TrackView", "onDraw start");
        // 1. 绘制跑道
        canvas.drawPath(trackPath, trackPaint);

        // 2. 绘制中间的虚线
        canvas.drawPath(dashedPath, dashedLinePaint);

        for (User user : users) {
            drawUserAvatar(canvas, user);
        }

        // 仅绘制被高亮的用户的气泡
        if (highlightedUser != null) {
            drawSpeedBubble(canvas, highlightedUser, String.format("%.2f m/s", highlightedUser.getSpeed()));
        }

        if (isRunning) {
            postInvalidateOnAnimation(); // 只有当 isRunning 为 true 时，才会自动刷新
        }
        //Log.i("TrackView", "onDraw end");
    }

    private void drawSpeedBubble(Canvas canvas, User user, String text) {
        float distanceOnPath = user.getCurrentDistance() / TOTAL_DISTANCE * pathMeasure.getLength();
        pathMeasure.getPosTan(distanceOnPath, position, null);

        float bubbleWidth = textPaint.measureText(text) + BUBBLE_PADDING * 2;
        float cx = position[0]; // 气泡的x中心点
        float avatarTopY = position[1] - trackWidth * 0.4f; // 头像顶部的Y坐标

        // 1. 计算气泡的边界
        float bubbleBottomY = avatarTopY - BUBBLE_ARROW_HEIGHT; // 气泡的底部Y坐标
        bubbleRectF.set(cx - bubbleWidth / 2, bubbleBottomY - BUBBLE_HEIGHT, cx + bubbleWidth / 2, bubbleBottomY);
        canvas.drawRoundRect(bubbleRectF, BUBBLE_RADIUS, BUBBLE_RADIUS, bubblePaint);

        float arrowCenterX = bubbleRectF.centerX();
        float arrowBottomY = bubbleRectF.bottom + BUBBLE_ARROW_HEIGHT;

        bubbleArrowPath.reset();
        bubbleArrowPath.moveTo(arrowCenterX, arrowBottomY);
        bubbleArrowPath.lineTo(arrowCenterX - BUBBLE_ARROW_HEIGHT, bubbleRectF.bottom);
        bubbleArrowPath.lineTo(arrowCenterX + BUBBLE_ARROW_HEIGHT, bubbleRectF.bottom);
        bubbleArrowPath.close();
        canvas.drawPath(bubbleArrowPath, bubblePaint);

        // 3. 绘制气泡中的文字
        float textX = bubbleRectF.centerX() - textPaint.measureText(text) / 2;
        float textY = bubbleRectF.centerY() - (fontMetrics.ascent + fontMetrics.descent) / 2;
        canvas.drawText(text, textX, textY, textPaint);
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
        float distanceOnPath = user.getCurrentDistance() / TOTAL_DISTANCE * pathMeasure.getLength();
        pathMeasure.getPosTan(distanceOnPath, position, null);

        float radius = trackWidth * 0.4f;
        canvas.drawBitmap(user.avatar, position[0] - radius, position[1] - radius, userPaint);
    }

    /**
     * 增加一个用户
     */
    public void addUser(Bitmap avatar) {
        User user = new User(createCircularBitmap(avatar, (int) (trackWidth * 0.8f)));
        users.add(user);
    }

    /**
     * 更新用户的运动状态
     */
    public void updateUserDistance(float[] newDistances) {
        for (int i = 0; i < users.size(); i++) {
            if (i < newDistances.length) {
                users.get(i).setTargetDistance(newDistances[i]);
            }
        }
    }

    /**
     * 启动刷新
     */
    public void start() {
        if (!isRunning) {
            isRunning = true;
            postInvalidateOnAnimation(); // 启动刷新
        }
    }

    /**
     * 停止刷新
     */
    public void stop() {
        isRunning = false; // 停止刷新
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float touchX = event.getX();
            float touchY = event.getY();

            for (User user : users) {
                float distanceOnPath = user.getCurrentDistance() / TOTAL_DISTANCE * pathMeasure.getLength();
                pathMeasure.getPosTan(distanceOnPath, position, null);

                float avatarRadius = trackWidth * 0.4f; // 头像的半径
                float dx = touchX - position[0];
                float dy = touchY - position[1];

                // 检测点击点是否在头像的圆形区域内
                if (Math.sqrt(dx * dx + dy * dy) <= avatarRadius) {
                    highlightedUser = user; // 设置高亮的用户
                    highlightTime = System.currentTimeMillis(); // 记录点击的时间
                    //postInvalidate(); // 触发重绘

                    // 3秒后隐藏高亮的气泡
                    postDelayed(() -> {
                        if (System.currentTimeMillis() - highlightTime >= HIGHLIGHT_DURATION) {
                            highlightedUser = null;
                            //postInvalidate(); // 重新绘制
                        }
                    }, HIGHLIGHT_DURATION);

                    break; // 只处理第一个被点击的头像
                }
            }
        }
        return true; // 返回 true 表示消费了触摸事件
    }


    /**
     * 将头像裁剪为圆形
     */
    private Bitmap createCircularBitmap(Bitmap bitmap, int size) {
        //1. 作为新的圆形 Bitmap 的画布，后面我们会在这张画布上“绘制”一个圆形的图片。
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
