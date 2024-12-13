package com.xaye.diyview.view.track;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.util.Log;

/**
 * Author xaye
 *
 * @date: 2024/12/11
 */
public class User {
    public Path path = new Path(); // 用户的路径
    public Bitmap avatar; // 用户的头像
    private float currentDistance = 0; // 当前的累计运动距离
    private float lastDistance = 0; // 上一次的距离（用于动画起点）
    private float targetDistance = 0; // 目标距离（服务器返回的最新距离）
    private long lastUpdateTime; // 距离更新的时间戳
    private long updateDuration = 1000; // 服务器的更新间隔，1秒
    private float speed = 0f; // 缓存的速度，单位：m/s

    private static final float TOTAL_DISTANCE = 400f; // 跑道总长度

    public User(Bitmap avatar) {
        this.avatar = avatar;
    }

    /**
     * 服务器传入的目标距离
     */
    public void setTargetDistance(float distance) {
        if (distance < targetDistance) return; // 过滤回退的距离

        long now = System.currentTimeMillis();

        // 处理第一次调用
        if (lastUpdateTime == 0) {
            lastDistance = distance % TOTAL_DISTANCE;
            targetDistance = lastDistance;
            lastUpdateTime = now;
            return;
        }

        // 计算与上一次调用的时间间隔
        updateDuration = now - lastUpdateTime;
        if (updateDuration <= 0) updateDuration = 1;

        // 计算 lastDistance 和 targetDistance 的关系
        lastDistance = currentDistance % TOTAL_DISTANCE;
        float distanceChange = distance % TOTAL_DISTANCE - lastDistance;

        // 处理跨圈的情况
        if (distanceChange < 0) {
            distanceChange += TOTAL_DISTANCE;
        }

        // 计算速度（m/s）
        speed = distanceChange / (updateDuration / 1000f);

        // 更新 targetDistance，确保从 lastDistance 平滑增长到 targetDistance
        targetDistance = lastDistance + distanceChange;

        // 记录上次更新时间
        lastUpdateTime = now;
    }

    /**
     * 平滑更新用户的当前位置
     */
    public void updateCurrentDistance() {
        long now = System.currentTimeMillis();
        float elapsed = (now - lastUpdateTime) / (float) updateDuration; // 计算时间完成度 (0~1)
        elapsed = Math.min(1f, elapsed); // 确保在 0~1 之间, 表示在这 1s 内的完成度
        currentDistance = lastDistance + (targetDistance - lastDistance) * elapsed; // 线性插值
        // 0 + (30 - 0) * 0.1 - 0.2 - 0.3 ...
        // 300 + (330 - 300) * 0.1 - 0.2 - 0.3 ...
        // 390 + (420 - 390) * 0.1 - 0.2 - 0.3 ... // 最终：420

        // 使用模运算确保当前位置在 0~TOTAL_DISTANCE 范围内
        // 这里确保了 currentDistance 在达到 TOTAL_DISTANCE 后会回到 0，并递增
        currentDistance = currentDistance % TOTAL_DISTANCE;
    }

    /**
     * 获取当前的运动距离
     */
    public float getCurrentDistance() {
        updateCurrentDistance(); // 动态更新当前位置
        return currentDistance;
    }

    /**
     * 更新路径
     */
    public void updatePath(float posX, float posY) {
        if (path.isEmpty()) {
            path.moveTo(posX, posY);
        } else {
            path.lineTo(posX, posY);
        }
    }

    /**
     * 速度 = 距离变化量 / 时间间隔
     */
    public float getSpeed() {
        return speed;
    }
}
