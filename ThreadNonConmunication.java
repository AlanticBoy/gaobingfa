package com.fusong.thread;

/**
 * Created by Administrator on 2018/3/31.
 */
/*目标，主线程执行完，子线程执行，双方不通信*/
public class ThreadNonConmunication {
    public static void main(String[] args) {
        Bussness bussness=new Bussness();
        new Thread(new Runnable() {
            @Override
            public void run() {
                    for (int i=1;i<=50;i++){
                        bussness.sub(i);
                    }
                }
        }).start();
        for (int i=0;i<50;i++){
            bussness.main(i);
        }
    }

}
class Bussness{
    public synchronized void sub(int i){
        for (int j=0;j<10;j++){
            System.out.println(" sub thread curent loop j= "+j+" i= "+i);
        }
    }
    public synchronized void main(int i){
        for (int j=0;j<50;j++){
            System.out.println(" main curent loop j= "+j+" i= "+i);
        }
    }
}