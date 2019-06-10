package com.github.weiss.core.thread;

public abstract class LoopThread extends Thread {

    volatile Thread blinker = this;

    abstract public void setup();

    abstract public void loop();

    abstract public void over();

    @Override
    public void run() {
        Thread thisThread = Thread.currentThread();
        setup();
        while (blinker == thisThread) {
            loop();
        }
        over();
    }

    public void breakLoop() {
        blinker = null;
    }

    public void shutdown() {
        breakLoop();
        try {
            if (this != Thread.currentThread()) {
                synchronized (this) {
                    this.notifyAll();
                }
                this.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

