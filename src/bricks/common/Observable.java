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

import java.util.ArrayList;

/**
 * Observable abstract definition
 * 
 * @author      Adrian Tilita <adrian@tilita.ro>
 * @version     1.0
 * @since       2016-03-28
 */
abstract public class Observable {
    /**
     * NotificationMessage holder
     */
    final private NotificationMessage notificationPrototype = new NotificationMessage();

    /**
     * Attached observers
     */
    final private ArrayList<Observer> observers = new ArrayList<>();

    /**
     * Inject observer
     * @param   observer
     * @return  Observable
     */
    public Observable addObserver(Observer observer) {
        // to avoid duplicates we remove it first if it is allready added
        this.removeObserver(observer);
        this.observers.add(observer);
        return this;
    }

    /**
     * Remove an observer
     * @param   observer 
     * @return  Observable
     */
    public Observable removeObserver(Observer observer) {
        if (this.observers.isEmpty() == true) {
            return this;
        }
        if (this.observers.contains(observer)) {
            this.observers.remove(observer);
        }
        return this;
    }

    /**
     * Remove all observers attached
     */
    public void removeObservers() {
        if (this.observers.isEmpty() == true) {
            return;
        }
        int observerCount = this.observers.size();
        for (int i = 0; i < observerCount; i++) {
            this.observers.remove(i);
        }
    }

    /**
     * Notify all injected observers about the current state change
     */
    private void notify(NotificationMessage event) {
        // stop notify if no injected observers present
        if (this.observers.isEmpty() == true) {
            return;
        }
        this.observers.stream().forEach((observer) -> {
            observer.catchNotification(event);
        });
    }

    /**
     * Builds a new notification message to be dispatched
     * @param   type
     * @param   message
     * @param   target
     */
    protected void dispatchMessage(String type, String message, Object target) {
        NotificationMessage notificationMessage = notificationPrototype.clone();
        notificationMessage.setNotificationMessagetData(type, message, target);
        this.dispatchMessage(notificationMessage);
    }

    /**
     * Dispatch an already builded notification message to be dispatched
     * @param   notificationMessage
     */
    protected void dispatchMessage(NotificationMessage notificationMessage) {
        this.notify(notificationMessage);
    }
}