package com.fusong.thread;

/**
 * Created by Administrator on 2018/3/31.
 */
/*目标，子线程执行一部分，通知主线程。主线程执行一部分，通知子线程，双方通信*/

public class ThreadComunicationDemo01 {
    /*为什么不在用的方法里加synchronized，而放到线程资源类里？
    这是因为，放到线程资源类可以解决线程安全问题，同时使用方便，
    不然每个使用到的方法都得考虑加synchronized
    */
    public static void main(String[] args) {
        Bussness2 bussness = new Bussness2();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 50; i++) {
                    bussness.sub(i);
                }
            }
        }).start();
        for (int i = 0; i < 50; i++) {
            bussness.main(i);
        }
    }

}

class Bussness2 {
    private boolean isOK = true;

    public synchronized void sub(int i) {
        while (!isOK) {/*为什么这里使用while，而不是用IF？这是为了避免假唤醒，用while更显深度*/
            try {
                /*wait(),相当于睡眠中，在等待，需要被外界的notify()方法唤醒*/
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int j = 0; j < 10; j++) {
            System.out.println(" sub thread curent loop j= " + j + " i= " + i);
        }
        isOK = false;
        this.notify();
    }

    public synchronized void main(int i) {
        while (isOK) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int j = 0; j < 50; j++) {
            System.out.println(" main curent loop j= " + j + " i= " + i);
        }
        isOK = true;
        this.notify();
    }
}
