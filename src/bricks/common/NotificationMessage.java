/**
 * Copyright (c) 2016 Adrian Tilita
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom
 * the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
 * THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package bricks.common;

/**
 * A Prototype notification message passed on to the observers
 * 
 * @author      Adrian Tilita <adrian@tilita.ro>
 * @version     1.0
 * @since       2016-03-28
 */
public class NotificationMessage {
    /**
     * NotificationMessage type
     */
    private String type = null;

    /**
     * Object that triggered the event/the object affected
     */
    private Object target = null;

    /**
     * Any informational message that should be passed on
     */
    private String message = null;

    /**
     * The time when the event occurred
     */
    private long time = 0; 

    /**
     * Set the notification message data
     * @param   type
     * @param   message
     * @param   target
     * @return  NotificationMessage
     */
    public NotificationMessage setNotificationMessagetData(String type, String message, Object target) {
        this.type = type;
        this.message = message;
        this.target = target;
        this.time = System.nanoTime();
        return this;
    }

    /**
     * Clone the NotificationMessage
     * @return  NotificationMessage
     */
    @Override
    public NotificationMessage clone() {
        Object clone = null;
        try {
            clone = super.clone();
            ((NotificationMessage)clone).reset();
        } catch (CloneNotSupportedException e) {
            // if in some situatia the clone is not supported,
            // we instantiate a new event object
        } catch (Exception e) {
            // if in some situatia the clone is not supported,
            // we instantiate a new event object
        } finally {
            clone = new NotificationMessage();
        }
        return (NotificationMessage)clone;
    }

    /**
     * Reset the notification message details
     */
    public void reset() {
        this.type = null;
        this.target = null;
        this.message = null;
        this.time = System.nanoTime();
    }

    /**
     * Return the NotificationMessage Type
     * @return  String
     */
    public String getType() {
        return this.type;
    }

    /**
     * Return the notification Target
     * @return  Object
     */
    public Object getTarget() {
        return this.target;
    }

    /**
     * Return the time when the event occurred in nanoTime
     * @return  long
     */
    public long getTime() {
        return this.time;
    }

    /**
     * Get notification message
     * @return  String
     */
    public String getMessage() {
        return this.message;
    }
}