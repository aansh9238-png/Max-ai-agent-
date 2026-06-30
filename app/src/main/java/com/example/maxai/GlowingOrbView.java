package com.example.maxai;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

public class GlowingOrbView extends View {

    private Paint paint;
    private float pulseRadius = 0.7f;
    private ValueAnimator animator;
    private int orbColor = 0xFF2196F3;
    private int glowColor = 0xFF64B5F6;

    public GlowingOrbView(Context context) {
        super(context);
        init();
    }

    public GlowingOrbView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        startPulseAnimation();
    }

    private void startPulseAnimation() {
        animator = ValueAnimator.ofFloat(0.6f, 0.85f);
        animator.setDuration(1500);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addUpdateListener(animation -> {
            pulseRadius = (float) animation.getAnimatedValue();
            invalidate();
        });
        animator.start();
    }

    public void setOrbColor(int color) {
        this.orbColor = color;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float cx = getWidth() / 2f;
        float cy = getHeight() / 2f;
        float maxRadius = Math.min(getWidth(), getHeight()) / 2f;
        float radius = maxRadius * pulseRadius;

        RadialGradient gradient = new RadialGradient(
                cx, cy, radius,
                glowColor, orbColor,
                Shader.TileMode.CLAMP);
        paint.setShader(gradient);
        canvas.drawCircle(cx, cy, radius, paint);

        paint.setShader(null);
        paint.setColor(0x33FFFFFF);
        canvas.drawCircle(cx, cy, radius * 0.5f, paint);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (animator != null) {
            animator.cancel();
        }
    }
}
