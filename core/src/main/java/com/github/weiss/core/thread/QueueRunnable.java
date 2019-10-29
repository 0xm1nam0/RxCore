package com.github.weiss.core.thread;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Runnable队列
 * author weiss
 * email kleinminamo@gmail.com
 * created 2017/7/7.
 */
public class QueueRunnable {

    protected final Queue<Runnable> runnableQueue;//默认队列
    protected final Queue<Runnable> runnableQueue4End;//结束队列

    public QueueRunnable() {
        runnableQueue = new LinkedList<Runnable>();
        runnableQueue4End = new LinkedList<Runnable>();
    }

    protected void queueEvent(final Runnable runnable) {
        synchronized (runnableQueue) {
            runnableQueue.add(runnable);
        }
    }

    protected void queueEvent4End(final Runnable runnable) {
        synchronized (runnableQueue) {
            runnableQueue.add(runnable);
        }
    }

    protected void runAllQueue() {
        synchronized (runnableQueue) {
            while (!runnableQueue.isEmpty()) {
                runnableQueue.poll().run();
            }
        }
    }

    protected void runAll(Queue<Runnable> queue) {
        synchronized (queue) {
            while (!queue.isEmpty()) {
                queue.poll().run();
            }
        }
    }

    protected void runAll4End(Queue<Runnable> queue) {
        synchronized (queue) {
            while (!queue.isEmpty()) {
                queue.poll().run();
            }
        }
    }
}
