package com.rzierfiz.peerpaper.backend.ServerConnection;

import android.util.Log;

import java.util.Stack;

public class QueueWorker extends Thread {
    Stack<Runnable> runnables = new Stack<>();
    boolean isRunning = false;

    public void run() {
        while (isRunning) {
            synchronized (runnables) {
                if (runnables.isEmpty())
                    try {
                        runnables.wait();
                        if(runnables.isEmpty()) break;
                    } catch (Exception e) {
                        Log.d("Exp", e + " Stack");
                    }
                try {
                    runnables.pop().run();
                }catch (Exception e){}
            }
        }
    }

    public void addWork(Runnable runnable) {
        synchronized (runnables) {
            runnables.push(runnable);
            if (runnables.size() == 1)
                runnables.notify();
        }
    }

    public void startWorker() {
        if(isRunning) return;
        isRunning = true;
        start();
    }

    public void stopWorker() {
        if(!isRunning) return;
        isRunning = false;
        synchronized (runnables){
            runnables.notify();
        }
        if (isAlive()) try {
            this.join();
        } catch (Exception e) {
        }
    }

    protected void finalize() {
        try {
            stopWorker();
            super.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
