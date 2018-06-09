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

            canvas.rotate(box.getMangle(),box.getOrigin().x,box.getOrigin().y);
            canvas.drawRect(left, top, right, bottom, mBoxPaint);
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
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                action = "ACTION_DOWN";
                // Reset drawing state
                mCurrentBox = new box(current);
                mBoxen.add(mCurrentBox);
                break;
                case MotionEvent.ACTION_MOVE:
                    action = "ACTION_MOVE";
                    if (mCurrentBox != null) {
                        mCurrentBox.setCurrent(current);
                        if(event.getPointerCount()> 1){
                            float angle = (float) Math.toDegrees(Math.atan(event.getY(1)
                                    -mCurrentBox.getOrigin().y)/(event.getX(1) - mCurrentBox.getOrigin().x));
                            mCurrentBox.setMangle(angle);
                        }
                        invalidate();
                    }
                    break;
                    case MotionEvent.ACTION_UP:
                        action = "ACTION_UP";
                        mCurrentBox = null;
                        break;
                        case MotionEvent.ACTION_CANCEL:
                            action = "ACTION_CANCEL";
                            mCurrentBox = null;
                            break;
        }
        Log.i(TAG, action + " at x=" + current.x + ", y=" + current.y);
        return true;
    }
}
