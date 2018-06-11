package com.bignerdranch.android.draganddraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import static android.view.MotionEvent.ACTION_CANCEL;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_POINTER_DOWN;
import static android.view.MotionEvent.ACTION_UP;



public class BoxDrawingView extends View {
    private static final String TAG = "BoxDrawingView";
    private box mCurrentBox;
    private List<box> mBoxen = new ArrayList<>();
    private Paint mBoxPaint;
    private Paint mBackgroundPaint;

    private static final int INVALID_POINTER_ID = -1;
    private float fX, fY, sX, sY, focalX, focalY;
    private int ptrID1, ptrID2;
    private float mAngle = 0;
    private boolean firstTouch;


    // Used when creating the view in code
    public BoxDrawingView(Context context) {
        this(context, null);
    }

    //in box
    public float getAngle(){
        return mAngle;
    }
    @Override protected void onDraw(Canvas canvas) {
        // Fill the background
        canvas.drawPaint(mBackgroundPaint);
        for (box box : mBoxen) {
            float left = Math.min(box.getOrigin().x, box.getCurrent().x);
            float right = Math.max(box.getOrigin().x, box.getCurrent().x);
            float top = Math.min(box.getOrigin().y, box.getCurrent().y);
            float bottom = Math.max(box.getOrigin().y, box.getCurrent().y);

            //canvas.rotate(box.angle);

            canvas.drawRect(left, top, right, bottom, mBoxPaint);
            //canvas.rotate(-box.angle);

        }
    }

    // Used when inflating the view from XML
    public BoxDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Paint the boxes a nice semitransparent red (ARGB)
        mBoxPaint = new Paint();
        mBoxPaint.setColor(0x22ff0000);
        // Paint the background off-white
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xfff8efe0);

        ptrID1 = INVALID_POINTER_ID;
        ptrID2 = INVALID_POINTER_ID;

    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getPointerCount() > 1) {
            execMultiTouchEvent(event);
        } else {
            execSingleTouchEvent(event);
        }

        return true;
    }

    private void execMultiTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int pointerCount = event.getPointerCount();
        int primaryPointerId = event.getPointerId(0);
        int secondaryPointerId;
        boolean shouldRotate = false;
        PointF startPointForNonPrimary = new PointF();
        PointF tempPointForNonPrimary = new PointF();
        PointF postRotationOrigin, postRotationCurrent;
        float sX,sY,fX,fY;
        for(int i = 1; i < pointerCount; i++) {
            secondaryPointerId = event.getPointerId(i);
            switch (action) {
                case ACTION_POINTER_DOWN:
                    Log.d(TAG, "action is multi-touch: New Pointer detected " + actionToString
                            (action));
                    sX = event.getX(i);
                    sY = event.getY(i);
                    startPointForNonPrimary = new PointF(sX, sY);
                    Log.d(TAG, "start point for non-primary pointer" + i + ": x =" + sX +
                            ", y = " + sY );
                    if(isPointInRectangle(startPointForNonPrimary, mCurrentBox.getOrigin(),
                            mCurrentBox.getCurrent())) {
                        shouldRotate = true;
                    }
                    break;
                case ACTION_MOVE:
                    Log.d(TAG, "action is multi-touch: " + actionToString(action));
                    tempPointForNonPrimary = new PointF(event.getX(i), event.getY(i));
                    Log.d(TAG, "moving point for non-primary pointer" + i + ": x =" + event.getX(i) +
                            ", y = " + event.getY(i) );
                    if(shouldRotate) {
                        double rotationAngle = findRotationAngle(startPointForNonPrimary,
                                tempPointForNonPrimary);
                        Canvas canvas = new Canvas();
                        execRotation(canvas, rotationAngle);
                    }
                    invalidate();
                    break;
                case ACTION_UP:

            }
        }    }
    public void execSingleTouchEvent(MotionEvent event) {
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "action is single-touch: " + actionToString(action));
                PointF pOrigin = new PointF(event.getX(), event.getY());
                box box = new box(pOrigin);
                mBoxen.add(box);
                mCurrentBox = box;
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "action is single-touch: " + actionToString(action));
                if(mCurrentBox != null) {
                    PointF pCurrent = new PointF(event.getX(), event.getY());
                    mCurrentBox.setCurrent(pCurrent);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "action is single-touch: " + actionToString(action));
                mCurrentBox = null;
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG, "action is single-touch: " + actionToString(action));
                mCurrentBox = null;
                break;
        }
    }
    private static String actionToString(int action) {
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                return "Down";
            case MotionEvent.ACTION_MOVE:
                return "Move";
            case MotionEvent.ACTION_POINTER_DOWN:
                return "Pointer Down";
            case MotionEvent.ACTION_UP:
                return "Up";
            case MotionEvent.ACTION_POINTER_UP:
                return "Pointer Up";
            case MotionEvent.ACTION_OUTSIDE:
                return "Outside";
            case MotionEvent.ACTION_CANCEL:
                return "Cancel";
        }
        return "";
    }
    private static boolean isPointInRectangle(PointF givenPoint, PointF origin, PointF current) {
        float x, x1, x2, y, y1, y2;
        x = givenPoint.x;
        y = givenPoint.y;
        x1 = origin.x;
        y1 = origin.y;
        x2 = current.x;
        y2 = current.y;

        if((( x >= x1 && x <= x2) || (x >= x2 && x <= x1)) || (( y >= y1 && y <= y2) || (y >= y2
                && y <= y1))) {
            return true;
        } else {
            return false;
        }
    }
    private static void findPointAfterRotation(PointF point) {

    }

    private double findRotationAngle(PointF startPoint, PointF endPoint){
        float x1,x2,y1,y2;
        x1 = startPoint.x;
        x2 = endPoint.x;
        y1 = startPoint.y;
        y2 = endPoint.y;

        return Math.atan2(x2 - x1, y2 - y1);
    }

    private void execRotation(Canvas canvas, double rotationAngle) {
        canvas.rotate((float) rotationAngle);
    }
}


