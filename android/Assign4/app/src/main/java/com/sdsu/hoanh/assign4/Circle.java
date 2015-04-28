package com.sdsu.hoanh.assign4;

import android.graphics.PointF;
import android.util.Log;

import java.util.Random;


public class Circle {

    //the current center
    private PointF _center;

    // the current x,y velocity component in pixel per timer tick
    private int _velocityX;
    private int _velocityY;

    // initialize the circle to 3 pixels
    private int _radius = 30;
    private int _color;


    private SpeedData _speedData = new SpeedData();
    
    Circle(PointF center)
    {
        this._center = center;
        _velocityX = _velocityY = 0;

        // generate some random _color.
        Random random = new Random();
        int redComponent = (random.nextInt() % 0xFF) << 16;
        int greenComponent = (random.nextInt() % 0xFF) << 8;
        int blueComponent = (random.nextInt() % 0xFF);

        final int baseColor = redComponent | greenComponent | blueComponent;
        // more transparency for regular circle
        final int tittleTransparency = 0x22000000;
        this._color = tittleTransparency | baseColor;
    }

    /**
     * stop all movement of the circle.
     */
    public void stop() {
        //_velocityY = _velocityX = 0;
        _speedData.numOfPts = 0;
        _speedData.averageXSpeed = _speedData.averageYSpeed = 0;
    }
    /**
     * the circle is in motion if any of the velocity component is more than the threshold
     * @return
     */
    public boolean isInMotion() {
        final int movementThreshold = 3;
        boolean isInMotion = Math.abs(_velocityX) > movementThreshold ||
                             Math.abs(_velocityY) > movementThreshold;
         return isInMotion;

    }

    public static int calculateEuclideanDistance(PointF from, PointF to)
    {
        double deltaXSquare = Math.pow(from.x - to.x, 2.0);
        double deltaYSquare = Math.pow(from.y - to.y, 2.0);
        //Log.i(MainActivity.APP_NAME, "calculateEuclideanDistance: from (" + from.x + ", " + from.y +")");
        //Log.i(MainActivity.APP_NAME, "calculateEuclideanDistance: to (" + to.x + ", " + to.y +")");
        double distance = Math.sqrt(deltaXSquare + deltaYSquare);
        //Log.i(MainActivity.APP_NAME, "calculateEuclideanDistance: distance " + distance);
        return (int)distance;
     }

    /**
     * set new speed components.  If the new speed components is > than
     * current speed, then save them.  Else ignore the new slower speed since we want
     * the circle to continue to move at current speed.
     */
    public void setSpeed(int xSpeed, int ySpeed) {

        // save the speed only if new speed is greater than current
        if(Math.abs(xSpeed) > Math.abs(_velocityX))
        {
            _velocityX = (int)xSpeed;
        }

        if(Math.abs(ySpeed) > Math.abs(_velocityY))
        {
            _velocityY = (int)ySpeed;
        }

        // update the average speed based on new velocity.
        _speedData.updateAveSpeed(_velocityX, _velocityY);

    }

    /**
     * tell the circle to move speedFactor-th distance between the previous and current center
     * ex. if speedFactor = 0.5, then we want center to move 1/2 the distance between previous and
     * current center
     * @param speedFactor - the factor of motion.
     * @param xLimit - how far can the circle travel to before it hit the right wall
     * @param yLimit - how far can the circle travel to before it hit the bottom wall
     */
    public void move(double speedFactor, int xLimit, int yLimit) {
        final float reductionOfSpeedFactor = 0.5f;

        if(isInMotion()) {

            // adjust new center
            this._center.x += _speedData.averageXSpeed * speedFactor;
            this._center.y += _speedData.averageYSpeed * speedFactor;

            //reduce the velocity magnitude
            _speedData.averageXSpeed *= speedFactor;
            _speedData.averageYSpeed *= speedFactor;

            // Ensure the circle still remain on the screen
            // if the outer edge of the circle goes pass the right edge of the screen, adjust the circle
            // so the edge is right on the right edge.
            if(this._center.x + _radius > xLimit) {
                //int readjustment =  (int)((this._center.x + _radius) - xLimit);
                //this._center.x -= readjustment;
                _center.x = xLimit - _radius;

                // negate the x velocity component so it will bounce back next timer tick
                // also reduce the magnitude by 1/2 to simulate reduction of speed when
                // bounce against the wall.
                _speedData.averageXSpeed = (int)(- (float)_speedData.averageXSpeed * reductionOfSpeedFactor);

            }
            // make sure it does not go beyond left edge
            else if(this._center.x - _radius < 0) {
                this._center.x = _radius;

                // negate the x velocity component so it will bounce back next timer tick
                // also reduce the magnitude by 1/2 to simulate reduction of speed when
                // bounce against the wall.
                _speedData.averageXSpeed = (int)(- (float)_speedData.averageXSpeed * reductionOfSpeedFactor);
            }

            // make sure it does not go beyond bottom edge. If it does adjust it so the edge touches
            // the bottom edge
            if(this._center.y + _radius > yLimit) {
                //int readjustment =  (int)((this._center.y + _radius) - yLimit);
                //this._center.y -= readjustment;
                _center.y = yLimit - _radius;

                // negate the y velocity component so it will bounce back next timer tick
                // also reduce the magnitude by 1/2 to simulate reduction of speed when
                // bounce against the wall.
                _speedData.averageYSpeed = (int)(- (float)_speedData.averageYSpeed * reductionOfSpeedFactor);
            }
            // make sure it does not go beyond top edge
            else if(this._center.y - _radius < 0) {
                this._center.y = _radius;

                // negate the y velocity component so it will bounce back next timer tick
                // also reduce the magnitude by 1/2 to simulate reduction of speed when
                // bounce against the wall.
                _speedData.averageYSpeed = (int)(- (float)_speedData.averageYSpeed * reductionOfSpeedFactor);
            }

        }
    }
    /**
     * see if the test point falls within the circle.  This is done by seeing how far it is from
     * the center.
     */
    public boolean isInCircle(PointF testPoint) {

        int distanceFromCenter = calculateEuclideanDistance(_center, testPoint);
        return distanceFromCenter < _radius;
    }

    public PointF getCenter() {
        return _center;
    }

    public void setCenter(PointF newCenter) {

        // determine the velocity component
        _velocityX = (int)(newCenter.x - _center.x);
        _velocityY = (int)(newCenter.y - _center.y);

        // update the average speed based on new velocity.
        _speedData.updateAveSpeed(_velocityX, _velocityY);

        // save new center
        this._center = new PointF(newCenter.x, newCenter.y);

    }

    public int getRadius() {
        return _radius;
    }

    public void setRadius(int radius) {
        this._radius = radius;
    }

    public int getColor() {
        return _color;
    }


    private static class SpeedData {
        // the calculation of average speed is based on all the points as the user drags the circle
        // the formula is New_AveSpeed = S + ( [ (n-1)/n ] * Curr_AveSpeed )
        //      where S is the speed of the n-1 to n point.
        //            n is the total points collected so far
        public int averageXSpeed = 0;
        public int averageYSpeed = 0;
        public int numOfPts = 0;

        public void reset()
        {
            averageXSpeed = 0;
            averageYSpeed = 0;
            numOfPts = 0;
        }
        public void updateAveSpeed(int newXVelocity, int newYVelocity) {
            this.numOfPts ++;
            this.averageXSpeed =
                    (int) (((double)(numOfPts - 1) / (double)numOfPts) * averageXSpeed) + newXVelocity;
            this.averageYSpeed =
                    (int) (((double)(numOfPts - 1) / (double)numOfPts) * averageYSpeed) + newYVelocity;
        }
    }

}
