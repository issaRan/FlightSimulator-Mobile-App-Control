package com.ISSAatOfek.EX4;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

// The Joystick Surface view.
public class JoyStickView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {
    // Middle X value.
    private float centerOfX;
    // Middle Y value.
    private float centerOfY;
    // The radius of the circle.
    private float raduisCircle;
    // The radius of the hat.
    private float hatRadius;
    // The Joystick listener.
    private JoystickListener JoystickCollable;
    // The ratio
    private final int rat = 5;

    // Joystick listener interface definition.
    public interface JoystickListener {
        void onJoystickMoved(float xPercent, float yPercent, int source);

    }


    // // Initialization of dimensional members.
    private void setupDim() {
        centerOfX = getWidth() / 2;
        centerOfY = getHeight() / 2;
        raduisCircle = Math.min(getWidth(), getHeight()) / 3;
        hatRadius = Math.min(getWidth(), getHeight()) / 5;
    }

    // Constructor.
    public JoyStickView(Context context) {
        super(context);
        setOnTouchListener(this);
        if (context instanceof JoystickListener) {
            JoystickCollable = (JoystickListener) context;
        }
    }

    // Constructor
    public JoyStickView(Context context, AttributeSet attributes, int style) {
        super(context, attributes, style);
        setOnTouchListener(this);
        if (context instanceof JoystickListener) {
            JoystickCollable = (JoystickListener) context;
        }
    }

    // Constructor.
    public JoyStickView(Context context, AttributeSet attributes) {
        super(context, attributes);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if (context instanceof JoystickListener) {
            JoystickCollable = (JoystickListener) context;
        }
    }

    // The function that draws the joystick.
    private void DrawJoystick(float newX, float newY) {
        // Check if the surface is available
        if (getHolder().getSurface().isValid()) {
            Canvas myCanvas = this.getHolder().lockCanvas();
            Paint colors = new Paint();
            myCanvas.drawRGB(250, 235, 215);

            colors.setARGB(255, 169, 169, 169);
            myCanvas.drawCircle(centerOfX, centerOfY, raduisCircle, colors);


            for (int i = 0; i <= (int) (hatRadius / rat); i++) {
                colors.setARGB(255, 65, 105, 225);
                myCanvas.drawCircle(newX, newY, hatRadius - (float) i * (rat) / 2, colors);
            }
            getHolder().unlockCanvasAndPost(myCanvas);


        }
    }

    // The function is called when a touch event occurs.
    public boolean onTouch(View v, MotionEvent e) {

        if (v.equals(this)) {
            if (v.equals(this)) {
                if (e.getAction() != e.ACTION_UP) {
                    float displacement = (float) Math.sqrt((Math.pow(e.getX() - centerOfX, 2)) +
                            Math.pow(e.getY() - centerOfY, 2));
                    if (displacement < raduisCircle) {
                        DrawJoystick(e.getX(), e.getY());
                        JoystickCollable.onJoystickMoved((e.getX() - centerOfX) / raduisCircle
                                , (e.getY() - centerOfY) / raduisCircle, getId());

                    } else {
                        float ratio = raduisCircle / displacement;
                        float constraiedX = centerOfX + (e.getX() - centerOfX) * ratio;
                        float constraiedY = centerOfY + (e.getY() - centerOfY) * ratio;
                        DrawJoystick(constraiedX, constraiedY);
                        JoystickCollable.onJoystickMoved((constraiedX - centerOfX) / raduisCircle
                                , (constraiedY - centerOfY) / raduisCircle, getId());
                    }

                } else {
                    DrawJoystick(centerOfX, centerOfY);
                    JoystickCollable.onJoystickMoved(0, 0, getId());
                }
            }
        } else {
            DrawJoystick(centerOfX, centerOfY);
        }
        return true;

    }


    // This function is called when the surface is created.
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        setupDim();
        DrawJoystick(centerOfX, centerOfY);
    }

    // This function is called when the surface is modified.
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int high) {

    }

    // This function is called when the surface is destroyed.
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}