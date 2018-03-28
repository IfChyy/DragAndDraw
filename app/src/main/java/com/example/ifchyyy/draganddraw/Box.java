package com.example.ifchyyy.draganddraw;

import android.graphics.PointF;

/**
 * Created by Ivo Georgiev(IfChyy)
 * A box class representing a box with cordinates origin and current
 * which is going to be represented on the screen when the user draws the box
 */

public class Box {
    private PointF origin,current;
    private PointF originRotate,currentRotate;

    public Box(PointF origin){
        this.origin = origin;
        this.current = origin;
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
}
