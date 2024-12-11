package com.xaye.diyview.view.track;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Path;

/**
 * Author xaye
 *
 * @date: 2024/12/11
 */
public class User {
    public Path path = new Path();  // 用户运动路径
    public Bitmap avatar;          // 用户头像
    private float currentDistance = 0; // 当前已运动距离
    private float speed;           // 当前速度 (m/s)
    private float acceleration;    // 加速度 (m/s^2)

    public float getCurrentDistance() {
        return currentDistance;
    }

    public void setCurrentDistance(float distance) {
        this.currentDistance = distance;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    /**
     * 根据当前速度和加速度更新距离
     */
    public void updateDistance() {
        speed += acceleration * 0.016f; // 加速度改变速度 (假设每帧 16ms)
        currentDistance += speed * 0.016f; // 距离更新
        if (currentDistance > 400f) {
            currentDistance -= 400f; // 超过一圈重置
        }
    }
}
