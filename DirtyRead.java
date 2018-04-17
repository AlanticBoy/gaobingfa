package com.fusong.thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author:
 * @Description:
 * @Date:Created in  17:14 2018/4/13
 * @ModefiedBy:
 */
public class DirtyRead {
    private String user = "liudan";
    private String pwd = "123456";
   private Lock lock=new ReentrantLock();
    private  void setUserValue(String user, String pwd) {
        lock.lock();
        this.user = user;
        try {
            /*写的时间为2秒*/
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            this.pwd = pwd;
            System.err.println("setValue 最终结果->：user = " + user + "\t" + "pwd = " + pwd);
            lock.unlock();
        }

    }
    private  void getUserValue() {
         lock.lock();
        try {
            System.err.println("getUserValue 设置值：user = " + this.user + "\t" + "pwd = " + this.pwd);
        } finally {
            lock.unlock();
        }
    }
    public  static  void main(String[] args) throws InterruptedException {
        final DirtyRead dr = new DirtyRead();

        Thread userThread = new Thread(new Runnable() {
            @Override
            public void run() {
                dr.setUserValue("testuser","111111");
            }
        });
        userThread.start();
        /*读的时间为1秒*/
        /*若是不同步，执行顺序则读在先，写在后*/
        Thread.sleep(1000);
        dr.getUserValue();
        System.out.println(" main thread execute first ");
    }

}
