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


public class BoxDrawingView extends View {
    private static final String TAG = "BoxDrawingView";
    private box mCurrentBox;
    private List<box> mBoxen = new ArrayList<>();
    private Paint mBoxPaint;
    private Paint mBackgroundPaint;
    float an;

    // Used when creating the view in code
    public BoxDrawingView(Context context) {
        this(context, null);
    }
    @Override protected void onDraw(Canvas canvas) {
        // Fill the background
        canvas.drawPaint(mBackgroundPaint);
        for (box box : mBoxen) {
            float left = Math.min(box.getOrigin().x, box.getCurrent().x);
            float right = Math.max(box.getOrigin().x, box.getCurrent().x);
            float top = Math.min(box.getOrigin().y, box.getCurrent().y);
            float bottom = Math.max(box.getOrigin().y, box.getCurrent().y);

            float px = ( box.getCurrent().x + box.getOrigin().x ) / 2;
            float py = ( box.getCurrent().y + box.getOrigin().y ) / 2;

            canvas.rotate(box.getAngle(),px,py);
            canvas.drawRect(left, top, right, bottom, mBoxPaint);
            canvas.rotate(-1*box.getAngle(),px,py);
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

    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PointF current = new PointF(event.getX(), event.getY());
        String action = "";
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                action = "ACTION_DOWN";
                // Reset drawing state
                mCurrentBox = new box(current);
                mCurrentBox.setId(event.getPointerId(0));
                mBoxen.add(mCurrentBox);
                break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    if ((event.getPointerCount()>=2)&&(mCurrentBox!= null)){
                        PointF current2 = new PointF(event.getX(1),event.getY(1));
                        mCurrentBox.setOrigin2(current2);
                        mCurrentBox.setId2(event.getPointerId(1));
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    action = "ACTION_MOVE";
                    if ((mCurrentBox != null)&& (mCurrentBox.getId() == event.getPointerId(0))) {

                        mCurrentBox.setCurrent(current);
                        if(event.getPointerCount()>=2){
                            if (mCurrentBox.getId2() == event.getPointerId(1)) {
                                PointF current2 = new PointF(event.getX(1),event.getY(1));
                                mCurrentBox.setCurrent(current2);
                                mCurrentBox.setAngle(calcangle(mCurrentBox.getOrigin2(),mCurrentBox.getCurrent2()));
                            }
                        }
                        invalidate();
                    }
                    break;
                    case MotionEvent.ACTION_UP:
                        action = "ACTION_UP";
                        mCurrentBox = null;
                        break;
            case MotionEvent.ACTION_POINTER_UP:
                if (mCurrentBox != null){
                Log.i(TAG, "Angle = " + mCurrentBox.getAngle());

            }
                        case MotionEvent.ACTION_CANCEL:
                            action = "ACTION_CANCEL";
                            mCurrentBox = null;
                            break;
        }
        Log.i(TAG, action + " at x=" + current.x + ", y=" + current.y);
        return true;
    }
    /*public float calcangle(PointF center,PointF target){
         an = (float) Math.atan2(target.y - center.y, target.x- center.x);

        an  += Math.PI/2.0;
        an = (float) Math.toDegrees(an);

        if(an< 0 ){
            an += 360;
        }
        return an;
    }*/
    public float calcangle(PointF center, PointF target) {
        float angle = (float) Math.atan2(target.y - center.y, target.x - center.x);
        angle += Math.PI/2.0;
        // Translate to degrees
        angle = (float) Math.toDegrees(angle);

        if(angle < 0){
            angle += 360;
        }
        return angle;
    }
}
