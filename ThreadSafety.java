package com.fusong.thread;

/**
 * Created by Administrator on 2018/3/31.
 */
public class ThreadSafety {
    public static void main(String[] args) {
        Outputor outputor = new Outputor();//必须是同一个对象，不同的对象没有意义

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    outputor.output("fu feng song ");
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Outputor.outputName("liu  de  hua");
                    //outputor.outputStr("liu  de  hua");
                }
            }
        }).start();
    }

    static class Outputor {

        public void output(String str) {
            int len = str.length();
            synchronized (this) {
                for (int i = 0; i < len; i++) {
                    System.out.print(str.charAt(i));
                }
                System.out.println();
            }
        }
       /*synchronized 修饰静态方法，直接在静态方法上加synchronized*/
        public static synchronized void outputName(String str) {
            int len = str.length();

                for (int i = 0; i < len; i++) {
                    System.out.print(str.charAt(i));
                }
                System.out.println();


        }

        public synchronized void outputStr(String str) {
            int len = str.length();
            for (int i = 0; i < len; i++) {
                System.out.print(str.charAt(i));
            }
            System.out.println();
        }

    }
}


