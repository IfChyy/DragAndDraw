package com.example.ifchyyy.draganddraw;

import android.graphics.PointF;

/**
 * Created by Ivo Georgiev(IfChyy)
 * A box class representing a box with coordinates origin and current
 * which is going to be represented on the screen when the user draws the box
 */

public class Box {
    private PointF origin, current;
    private Float angle;

    public Box(PointF origin) {
        this.origin = origin;
        this.current = origin;
        this.angle = 0.0f;
    }


    //------------------------------------GETTERS AND SETTERS
    public PointF getOrigin() {
        return origin;
    }


    public PointF getCurrent() {
        return current;
    }

    public void setCurrent(PointF current) {
        this.current = current;
    }

    public Float getAngle() {
        return angle;
    }

    public void setAngle(Float angle) {

        this.angle = angle;
    }
}
