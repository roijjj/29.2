package com.bignerdranch.android.draganddraw;

import android.graphics.PointF;

public class box {
    private PointF mOrigin;
    private PointF mCurrent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    private PointF mOrigin2;

    public PointF getCurrent2() {
        return mCurrent2;
    }

    public void setCurrent2(PointF current2) {
        mCurrent2 = current2;
    }

    private PointF mCurrent2;

    public int getId2() {
        return id2;
    }

    public void setId2(int id2) {
        this.id2 = id2;
    }

    private int id2;

    public PointF getOrigin2() {
        return mOrigin2;
    }

    public void setOrigin2(PointF origin2) {
        mOrigin2 = origin2;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    private float angle;

    public box(PointF origin) {
        mOrigin = origin;
        mCurrent = origin;
    }
        public PointF getCurrent() {
        return mCurrent;
    }
        public void setCurrent(PointF current) {
        mCurrent = current;
    }
        public PointF getOrigin() {
        return mOrigin;
    }
}
