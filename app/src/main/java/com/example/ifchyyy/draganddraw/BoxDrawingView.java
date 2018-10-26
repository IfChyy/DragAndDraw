package com.example.ifchyyy.draganddraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivo Georgiev(IfChyy)
 * custom view class for creating a custom view with touch events
 * if touch and move events are true then draw a rectangle on the screen
 */

public class BoxDrawingView extends View {
    private static final String TAG = "BoxDrawingView";
    private static final String SAVE_BOXES = "boxes_list";


    //init box
    private Box currentBox;
    //init list of boxes
    private List<Box> boxes = new ArrayList<>();

    //nit box Paint background)
    private Paint boxPaint, backgroundPaint;

    //init our view //used when craeting view in code
    public BoxDrawingView(Context context) {
        super(context);
    }

    //used when creating view in XML
    public BoxDrawingView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        //Paint the boxes transperent red
        boxPaint = new Paint();
        boxPaint.setColor(0x22ff0000);

        //Paint the boxes background off-white
        backgroundPaint = new Paint();
        backgroundPaint.setColor(0xFFF8EFE0);

        Log.d(TAG, "BoxDrawingView: " + boxes.size());
    }

    private static final int INVALID_POINTER_ID = -1;
    private int mActivePointerId = INVALID_POINTER_ID;

    //creates an on touch event
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //first pointer on the screen
        PointF current = new PointF(event.getX(), event.getY());
        //second one for rotating the rectangle
        PointF second;

        String action = "";
        //init registered action touch event
        final int actionRegistered = event.getAction();
        //get index of pointer
        mActivePointerId = event.getActionIndex();

        switch (actionRegistered & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                action = "ACTION_DOWN";
                current = new PointF(event.getX(), event.getY());
                //resset the drawing state;
                //create a new box with the firstly touched coordinates
                currentBox = new Box(current);
                boxes.add(currentBox);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                action = "ACTION_POINTER_DOWN";
                //get the continuing finger inputs
                second = new PointF(event.getX(1), event.getY(1));

                Log.d(TAG, mActivePointerId + " ACTION Pointer DOWN " + second.x + "   " + second.y);

                break;
            case MotionEvent.ACTION_MOVE:
                action = "ACTION_MOVE";
                //check if box not null, and set the current position(pointF);
                if (currentBox != null) {
                    currentBox.setCurrent(current);
                    //then invalidate boxDrawingView
                    invalidate();
                }
                //check if current input is equal to 2 - we want only two fingers
                if (event.getPointerCount() == 2) {
                    //get the new coordiantes
                    second = new PointF(event.getX(1), event.getY(1));
                    //calculate the angle
                    currentBox.setAngle(getDegrees(current, second));
                    Log.d(TAG, mActivePointerId + " ACTION Pointer MOVE " +currentBox.getAngle());
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                  action = "ACTION_UP";
                currentBox = null;
                break;
            case MotionEvent.ACTION_POINTER_UP:

                break;
            case MotionEvent.ACTION_CANCEL:
                  action = "ACTION_CANCEL";
                currentBox = null;
                break;
        }
           Log.d(TAG, action + " at x=" + current.x + ", y=" + current.y);
        return true;
    }

    //init ondraw to draw our rectangles (boxes)

    @Override
    protected void onDraw(Canvas canvas) {
        //Fill the background
        canvas.drawPaint(backgroundPaint);

        for (Box box : boxes) {
            Float left = Math.min(box.getOrigin().x, box.getCurrent().x);
            Float right = Math.max(box.getOrigin().x, box.getCurrent().x);
            Float top = Math.min(box.getOrigin().y, box.getCurrent().y);
            Float bottom = Math.max(box.getOrigin().y, box.getCurrent().y);


            // Calculate the center of the rectangle
            float centerX = (box.getCurrent().x + box.getOrigin().x) / 2;
            float centerY = (box.getCurrent().y + box.getOrigin().y) / 2;

            //rotate before creating the rect
            canvas.rotate(box.getAngle(), centerX, centerY);
            //create rect
            canvas.drawRect(left, top, right, bottom, boxPaint);
            //rotate minus the degrees rotateted before creating rect to keep
            //position of next rect angle to 0
            canvas.rotate(-1 * box.getAngle(), centerX, centerY);
        }
    }

    //method to get the angle rotation from the second finger input on the screen
    public float getDegrees(PointF first, PointF second) {
        //convert rect coordinates to polar
        float angle = (float) Math.atan2(second.y - first.y, second.x  - first.x);
        //add mathPi/2 ;
        angle += Math.PI/2;
        //translate to degrees
        angle = (float) Math.toDegrees(angle);

        return angle;
    }


    //-------------CHALLENGE SAVING STATE
    //save the instance of the boxes array List to json and parse it as abunndle
    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        //init bundle
        Bundle bundle = new Bundle();
        //init the parcelable with key and the saveInstanceState()
        bundle.putParcelable("boxesSavedInstance", super.onSaveInstanceState());
        //init gson
        Gson gson = new Gson();
        //string arrayList boxes to json
        String boxesToJson = gson.toJson(boxes);
        //put the json into the bundle
        bundle.putString(SAVE_BOXES, boxesToJson);
        //return the budnle
        return bundle;
    }

    //restore the instance state after rotating screan
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        //check if state is a bundle else restore
        if (state instanceof Bundle) // implicit null check
        {   //init budnle
            Bundle bundle = (Bundle) state;
            //get the json string from our bundle with our key
            String boxesFromJson = bundle.getString(SAVE_BOXES);
            //init gson
            Gson gson = new Gson();
            // create the boxes array list equal to the old arraylist from gson json array
            this.boxes = gson.fromJson(boxesFromJson, new TypeToken<List<Box>>() {
            }.getType());
            //restore the instacne with bundle parcelable and our key
            super.onRestoreInstanceState(bundle.getParcelable("boxesSavedInstance"));
            return;
        }
        super.onRestoreInstanceState(state);
    }
}

