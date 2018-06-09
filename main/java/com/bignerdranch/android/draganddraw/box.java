package com.bignerdranch.android.draganddraw;

import android.graphics.PointF;

public class box {
    private PointF mOrigin;
    private PointF mCurrent;

    public float getMangle() {
        return mangle;
    }

    public void setMangle(float mangle) {
        this.mangle = mangle;
    }

    private float mangle;

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
