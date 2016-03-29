package bricks.util;

/**
 * Utility time tracker
 * @author  Adrian Tilita
 * @version 1.0
 * @since   2016-02
 */
public class Timer {
    /**
     * Should contain the value of startTimer
     */
    private long timer = 0;

    /**
     * Start the timer
     */
    public void startTimer() {
        this.timer = System.currentTimeMillis();
    }

    /**
     * Interval: Now - Timer start -> milliseconds
     * @return
     */
    public long getTimer() {
        long currentTime = System.currentTimeMillis();
        return currentTime - this.timer;
    }

    /**
     * Interval: Now - Timer start -> seconds
     * @return 
     */
    public float getTimerInSeconds() {
        long now = this.getTimer();
        float seconds = (float)now / 1000;
        return seconds;
    }
}
