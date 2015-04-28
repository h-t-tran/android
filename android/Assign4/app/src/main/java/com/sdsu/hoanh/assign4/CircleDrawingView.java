package com.sdsu.hoanh.assign4;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CircleDrawingView extends View {
    /**
     * if the touch point is withing 25 pixel of the circle center, then we treat it as
     * user selects the circle.
     */
    private final static int NEARNESS_THRESHOLD_PIXEL = 25;

    private final static int MAX_CIRCLES_ALLOWED = 15;

    private final static int TIMER_EXPANSION_INTERVAL = 1000;  // 1 sec

    /**
     * how often do we want to update the motion after a fling of a circle.
     */
    private final static int TIMER_MOTION_INTERVAL = 200;
    private final static int CIRCLE_EXPANSION_AMOUNT = 10;

    /**
     * specify how much to decrease the motion of the fling circle.
     * ex. if value is 0.9, every TIMER_MOTION_INTERVAL ms, the speed will be decrease to 0.9
     * of the original value.
     */
    private final static double flingSpeedFactor = 0.9;

    private List<Circle> _circles = Collections.synchronizedList(new ArrayList<Circle>());
    private Circle _currCircle = null;
    private Paint _circlePaint;
    private Paint _selectionPaint;

    // use to expand the circle when user long press it
    private Timer _exapnsionTimer;

    // use to simulate circle motion after a swipe
    private Timer _motionTimer ;

    public CircleDrawingView(Context context) {
        this(context, null);
    }

    public CircleDrawingView(Context context, AttributeSet attributes) {
        super(context, attributes);

        _circlePaint = new Paint();
        _selectionPaint = new Paint();

        // create a timer and start it.
        _exapnsionTimer = new Timer();
        _exapnsionTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                CircleDrawingView.this.updateCircleExpansion();
            }

        }, 0, TIMER_EXPANSION_INTERVAL);


        _motionTimer = new Timer();
        _motionTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                CircleDrawingView.this.simulateContinuousMotion();
            }

        }, 0, TIMER_MOTION_INTERVAL);
    }

    /**
     * clear the existing circle collection
     */
    public void reset()
    {
        synchronized (_circles) {
            _circles.clear();
            invalidate();
        }

    }

    /**
     * tell each circle to move the specified speed
     * @param xSpeed - x component of the speed vector
     * @param ySpeed - y component of the speed vector
     */
    public void moveCircles(int xSpeed, int ySpeed) {
        synchronized (_circles) {
            for (Circle circle : _circles) {
                circle.setSpeed(xSpeed, ySpeed);
            }
        }

        invalidate();
    }

    public boolean onTouchEvent(MotionEvent event)
    {
        PointF currPoint = new PointF(event.getX(), event.getY());
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:

                Circle selectedCircle = getSelectedCircle(currPoint);
                if(selectedCircle == null) {
                    createNewCircle(currPoint);
                }
                else {
                    // else save the selected circle and redraw
                    _currCircle = selectedCircle;
                }
                // ensure the circle stops moving
                _currCircle.stop();

                invalidate();
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                _currCircle = null;
                invalidate();
                break;

            case MotionEvent.ACTION_MOVE:
                //Log.i(MainActivity.APP_NAME, "ACTION_MOVE: currPoint (" + currPoint.x + ", " + currPoint.y +")");
                _currCircle.setCenter(adjustCenter(_currCircle, currPoint));
                invalidate();
                break;
        }
        return true;
    }

    private void createNewCircle(PointF curPt) {
        synchronized (_circles) {
            // if we exceed the max, delete the older one
            if (_circles.size() >= MAX_CIRCLES_ALLOWED) {
                _circles.remove(0);
            }

            // create a new circle on new touch point and save it
            _currCircle = new Circle(curPt);
            _circles.add(_currCircle);

        }
    }

    /**
     * if the new center x +/- radius will cross the screen edge, leave the circle center as is
     * ditto for the center y.
     *
     * for the new center x +/- radius that does NOT cross the screen edge and same thing for the
     * center y, then allow the circle to accept new center
     *
     *
     * @param circle the current circle
     * @param newCenterCandiate a candidate for new circle
     * @return return new point for the center.
     */
    private PointF adjustCenter(Circle circle, PointF newCenterCandiate) {

        PointF newCenter = new PointF(circle.getCenter().x, circle.getCenter().y);

        // if new center x still allow the circle to fit in the screen, use it
        if(newCenterCandiate.x - circle.getRadius() > 0 &&
           newCenterCandiate.x + circle.getRadius() < this.getWidth()) {
            newCenter.x = newCenterCandiate.x;
        }
        if(newCenterCandiate.y - circle.getRadius() > 0 &&
                newCenterCandiate.y + circle.getRadius() < this.getHeight()) {
            newCenter.y = newCenterCandiate.y;
        }

        return newCenter;
    }

    private boolean hasReachedMaxWidth(Circle circle) {
        return (circle.getRadius() * 2) >= this.getWidth();
    }
    private boolean hasReachedMaxHeight(Circle circle) {
        return (circle.getRadius() * 2) >= this.getHeight();
    }

    private boolean canFitScreen(PointF newCenter, Circle circle) {

        // create a dummy circle with the new center and the input circle radius.
        // Then test to see if it can fit the screen
        Circle testCircle = new Circle(newCenter);
        testCircle.setRadius(circle.getRadius());

        // see if the test circle still fit the screen
        return canExpand(testCircle, 0);
    }
    /**
     * see if the input circle can be expanded without crossing the screen edge
     */
    private boolean canExpand(Circle circle, int expansionAmount)
    {
        int newRadius = circle.getRadius() + expansionAmount;
        return circle.getCenter().x + newRadius < this.getWidth() &&
                circle.getCenter().x - newRadius > 0 &&
                circle.getCenter().y - newRadius > 0 &&
                circle.getCenter().y + newRadius < this.getHeight();

    }

    /**
     * Determine the amount the circle can expand horizontally
     * before it reaches the edge of the screen
     */
    private int calculateHorzAllowableExpansionAmount(Circle circle) {
        int minDistanceFromEdge = (int)Math.min(this.getWidth() - circle.getCenter().x,
                                                circle.getCenter().x);
        return minDistanceFromEdge - circle.getRadius();

    }

    /**
     * Determine the amount the circle can expand vertically
     * before it reaches the edge of the screen
     */
    private int calculateVerticalAllowableExpansionAmount(Circle circle) {
        int minDistanceFromEdge = (int)Math.min(this.getHeight() - circle.getCenter().y,
                circle.getCenter().y);
        return minDistanceFromEdge - circle.getRadius();

    }

    /**
     * see if user wants to select a circle. By selection that means the location is "near" the
     * center of a circle.  The "near" is defined as within 15 pixel location.
     * We choose the circle closest to the input location and within 15 pixel.
     * @param location
     * @return
     */
    private Circle getSelectedCircle(PointF location) {

        Circle closestCircle = null;
        int shortestDistanceSoFar = Integer.MAX_VALUE;

        synchronized (_circles) {
            for (Circle circle : _circles) {

                // if this point inside the circle.
                if(circle.isInCircle(location)) {
                    int distance = Circle.calculateEuclideanDistance(circle.getCenter(), location);

                    // if found a closer circle, save it and adjust its distance
                    if (distance < shortestDistanceSoFar) {
                        closestCircle = circle;
                        shortestDistanceSoFar = distance;
                    }
                }
            }
        }

        return closestCircle;
    }

    /**
     * tell each circle if they are being fling, then continue to move it.
     */
    private void simulateContinuousMotion() {
        synchronized (_circles) {
            if (!_circles.isEmpty()) {
                for (Circle circle : _circles) {
                    circle.move(flingSpeedFactor, this.getWidth(), this.getHeight());
                }
                // must transfer to UI thread for redraw
                this.invalidateOnUiThread();
            }
        }
    }


    /**
     * expand the current circle
     */
    private void updateCircleExpansion()
    {
        // if there is a circle selected and user is not moving it around,
        // then increase its radius and redraw
        if(_currCircle != null && ! _currCircle.isInMotion()) {
            // expand the circle only if its edge does not go pass the screen edge
            if(canExpand(_currCircle, CIRCLE_EXPANSION_AMOUNT)) {
                //Log.i(MainActivity.APP_NAME, " Expanding circle");
                _currCircle.setRadius(_currCircle.getRadius() + CIRCLE_EXPANSION_AMOUNT);
                // must transfer to UI thread for redraw
                this.invalidateOnUiThread();
            }
            else {
                // if the circle cannot expand by the CIRCLE_EXPANSION_AMOUNT pixel, then
                // see if there is a smaller distance that we can expand.  We calculate the min
                // of both horizontal and vertical distance.
                int allowedExpansionDistance = Math.min(
                        calculateHorzAllowableExpansionAmount(_currCircle),
                        calculateVerticalAllowableExpansionAmount(_currCircle));
                if(allowedExpansionDistance > 0) {
                    _currCircle.setRadius(_currCircle.getRadius() + allowedExpansionDistance);
                    this.invalidateOnUiThread();
                }
            }

        }

    }

    private void invalidateOnUiThread()    {
        this.post(new Runnable() {
            @Override
            public void run() {
                CircleDrawingView.this.invalidate();
            }
        });
    }
    @Override
    protected void onDraw(Canvas canvas){

        synchronized (_circles) {
            for(Circle circle : _circles)
            {
                _circlePaint.setColor(circle.getColor());
                canvas.drawCircle(circle.getCenter().x, circle.getCenter().y, circle.getRadius(), _circlePaint);
            }

            // for the selected circle, we draw it on top
            if(_currCircle != null) {
                _selectionPaint.setColor(_currCircle.getColor());
                canvas.drawCircle(_currCircle.getCenter().x, _currCircle.getCenter().y,
                                        _currCircle.getRadius(), _selectionPaint);
            }
        }
    }


}