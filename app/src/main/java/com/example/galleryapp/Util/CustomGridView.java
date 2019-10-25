package com.example.galleryapp.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.example.galleryapp.R;

import java.util.ArrayList;
import java.util.Comparator;

import static android.view.MotionEvent.INVALID_POINTER_ID;

public class CustomGridView extends View {
    private ScaleGestureDetector mScaleDetector;
    int t;
    float mPosX,mPosY,x,y,dx,dy;
    float LEFTTOP_X, RIGHTBOTTOM_X, LEFTTOP_Y,RIGHTBOTTOM_Y;

    float[] distance = new float[4];
    float mLastTouchX,mLastTouchY;
    private int mBorderWidth = 300;
    private int mBorderHeight = 300;
    private Rect mTgRect = null;
    private PointF mDownPoint = null;
    SparseArray<PointF> mActivePointers;
    private final String TAG = this.getClass().getSimpleName();
    private int mActivePointerID = INVALID_POINTER_ID;
    Rect framingRect;
    public CustomGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mActivePointers = new SparseArray<>();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event){
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);
        switch (event.getAction() & MotionEvent.ACTION_MASK){

            case MotionEvent.ACTION_DOWN :
            case MotionEvent.ACTION_POINTER_DOWN :
                mDownPoint = new PointF(event.getX(pointerIndex), event.getY(pointerIndex));
                mActivePointers.put(pointerId, new PointF(event.getX(pointerIndex), event.getY(pointerIndex)));
                break;
            case MotionEvent.ACTION_MOVE: { // a pointer was moved
                for (int size = event.getPointerCount(), i = 0; i < size; i++) {
                    PointF point = mActivePointers.get(event.getPointerId(i));
                    if (point != null) {
                        point.x = event.getX(i);
                        point.y = event.getY(i);
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL: {
                mDownPoint = null;
                mActivePointers.remove(pointerId);
                break;
            }
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);

//        float Height = canvas.getHeight();
//        float Width = canvas.getWidth();
//
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.parseColor("#FF000000"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        // mPaint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(5);

        framingRect = getFramingRect(canvas);
        canvas.drawRect(framingRect, paint);
    }



    private Rect getFramingRect(Canvas canvas) {
        try {
            if (mTgRect == null)
                mTgRect = new Rect(canvas.getWidth() / 2 - mBorderWidth, canvas.getHeight() / 2 - mBorderHeight, canvas.getWidth() / 2 + mBorderWidth, canvas.getHeight() / 2 + mBorderHeight);
            else if (mActivePointers != null) {
                if (mActivePointers.size() == 0) {

                } else if (mActivePointers.size() == 1) {
                    // 1 finger
                    PointF p = mActivePointers.get(0);
                    // find nearest corner
                    CustomWindow cw = new CustomWindow(p, mTgRect);
                    switch (cw.getClosestDirection()) {
                        case LeftUp:
                            mTgRect = new Rect((int) p.x, (int) p.y, mTgRect.right, mTgRect.bottom);
                            break;
                        case RightUp:
                            mTgRect = new Rect(mTgRect.left, (int) p.y, (int) p.x, mTgRect.bottom);
                            break;
                        case LeftDown:
                            mTgRect = new Rect((int) p.x, mTgRect.top, mTgRect.right, (int) p.y);
                            break;
                        case RightDown:
                            mTgRect = new Rect(mTgRect.left, mTgRect.top, (int) p.x, (int) p.y);
                            break;
                        case Center: // move
                            if (mDownPoint != null) {   // == keep touch down
                                // 전체 이동
                                float mx = p.x - mDownPoint.x;
                                float my = p.y - mDownPoint.y;
                                mTgRect = new Rect((int) Math.max(0, mTgRect.left + mx)
                                        , (int) Math.max(0, mTgRect.top + my)
                                        , (int) Math.min(canvas.getWidth(), mTgRect.right + mx)
                                        , (int) Math.min(canvas.getHeight(), mTgRect.bottom + my));
                                mDownPoint = new PointF(p.x, p.y);
                            }
                            break;
                    }
                } else if (mActivePointers.size() == 2) {
                    // 2 fingure
                    PointF p1 = mActivePointers.get(0);
                    PointF p2 = mActivePointers.get(1);

                    mTgRect = new Rect(Math.min((int) p1.x, (int) p2.x), Math.min((int) p1.y, (int) p2.y)
                            , Math.max((int) p1.x, (int) p2.x), Math.max((int) p1.y, (int) p2.y));
                } else {
                    // disable upper 3 fingure
                }
            }
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        return mTgRect;
    }
    public class DirClass{
        public Direction dir;
        public double dist;
        public DirClass(Direction dir, double dist){
            this.dir = dir;
            this.dist = dist;
        }
    }
    public class CustomWindow{
        private PointF p;
        private Rect rect;

        public CustomWindow(PointF p, Rect rect){
            this.p = p;
            this.rect = rect;
        }

        private Direction getClosestDirection(){
            ArrayList<DirClass> dList = new ArrayList<DirClass>();
            dList.add(new DirClass(Direction.LeftUp, getDistance(p, rect.left, rect.top)));
            dList.add(new DirClass(Direction.RightUp, getDistance(p, rect.right, rect.top)));
            dList.add(new DirClass(Direction.LeftDown, getDistance(p, rect.left, rect.bottom)));
            dList.add(new DirClass(Direction.RightDown, getDistance(p, rect.right, rect.bottom)));
            dList.add(new DirClass(Direction.Center, getDistance(p, (rect.left + rect.right) / 2, (rect.top + rect.bottom) / 2)));
            return dList.stream().sorted(Comparator.comparing(s->s.dist)).findFirst().get().dir;
        }

        private double getDistance(PointF _p, int x, int y) {
            return Math.sqrt(Math.pow(Math.abs(_p.x - x), 2) + Math.pow(Math.abs(p.y - y), 2));
        }
    }

    public enum Direction { LeftUp, RightUp, LeftDown, RightDown, Center }

    public float min(float[] array) {
        // Validates input
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        }

        // Finds and returns min
        float min = array[0];
        for (int j = 1; j < array.length; j++) {
            if (Float.isNaN(array[j])) {
                return Float.NaN;
            }
            if (array[j] < min) {
                min = array[j];
                int t = j;
            }
        }

        return t;
    }
    double getDistance(int x, int y, float x1, float y1){
        return Math.sqrt(Math.pow(Math.abs(x1-x), 2) + Math.pow(Math.abs(y1-y), 2));
    }
}





















































        /*float X1 = Width / 3;
        float X2 = 2 * Width / 3;
        float Y1 = Height / 3;
        float Y2 = 2 * Height / 3;
        if ((x == 0 & y == 0) | (y>Height)){
            x1 = (int) X1;
            x2 = (int) X2;
            y1 = (int) Y1;
            y2 = (int) Y2;
        }
        else {
            width = x2 - x1;
            height = y2 - y1;
            if (x > x1 & y > y1 & x < x2 & y < y2) {
                a = 0;
            } else if (x < x1 & y < y1) {
                a = 1;
            } else if (x > x2 & y < y1) {
                a = 2;
            } else if (x < x1 & y > y2) {
                a = 3;
            } else if (x > x2 & y > y2) {
                a = 4;
            }
            switch (a) {
                case (0):
                    x1 = (int) (x - width / 2);
                    y1 = (int) (y - height / 2);
                    x2 = (int) (x + width / 2);
                    y2 = (int) (y + height / 2);
                    break;
                case (1):
                    x1 = x;
                    y1 = y;
                    break;
                case (2):
                    x2 = x;
                    y1 = y;
                    break;
                case (3):
                    x1 = x;
                    y2 = y;
                    break;
                case (4):
                    x2 = x;
                    y2 = y;
                    break;
            }
        }
        Rect dst = new Rect(Math.round(x1), Math.round(y1), Math.round(x2), Math.round(y2));
        canvas.drawRect(dst,paint);
        canvas.drawBitmap(bmp,null,dst,paint);
        *//*else {
            if (x < Width/2) {
                x1 = x;
                y1 = y;
            }else{
                    x2 = x;
                    y2 = y;
            }
        }
        distance[0] = (float) getDistance(x,y,x1,y1);
        distance[1] = (float) getDistance(x,y,x1,y2);
        distance[2] = (float) getDistance(x,y,x2,y1);
        distance[3] = (float) getDistance(x,y,x2,y2);
        t = (int) min(distance);
        switch(t){
            case 1:                            //LeftUp
                Rect A = new Rect(x,y,x2,y2);
                break;
            case 2:                            //LeftDown
                x2 = x;
                y1 = y;
                Rect B = new Rect(x,y1,x2,y);
                break;
            case 3:                           // RightUp
                x1 = x;
                y2 = y;
                Rect C = new Rect(x1,y,x,y2);
                break;
            case 4:                            //RightDown
                x2 = x;
                y2 = y;
                Rect D = new Rect(x1,y1,x,y);
                break;
        }*//*
 *//* q = (float) 50.0;
        canvas.drawCircle(x2-q/2,y1+q/2,q,myPaint);
        canvas.drawCircle(x1+q/2,y1+q/2, q,myPaint);*//*
 *//*canvas.drawLine(X1,0,X1, Height ,myPaint); // 세로 왼쪽줄
        canvas.drawLine(X2,0,X2, Height,myPaint); //세로 오른쪽줄
        canvas.drawLine(0,Y1,Width,Y1,myPaint); //가로 윗줄
        canvas.drawLine(0,Y2,Width,Y2,myPaint); //가로 아랫줄
*//*
        //Bitmap bmp = BitmapFactory.decodeResource(getResources(),R.drawable.golfer);
        //Rect dst = new Rect(Math.round(X1), Math.round(Y1), Math.round(X2), Math.round(Y2));
        //canvas.drawBitmap(bmp,null,dst,null);
    }
    double getDistance(int x, int y, float x1, float y1){
        return Math.sqrt(Math.pow(Math.abs(x1-x), 2) + Math.pow(Math.abs(y1-y), 2));
    }
    public static float min(float[] array) {
        // Validates input
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        }
        // Finds and returns min
        float min = array[0];
        for (int j = 1; j < array.length; j++) {
            if (Float.isNaN(array[j])) {
                return Float.NaN;
            }
            if (array[j] < min) {
                min = array[j];
                t = j;
            }
        }
        return t;
    }*/

