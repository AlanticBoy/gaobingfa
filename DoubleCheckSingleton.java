package com.fusong.thread;

/**
 * @Author:
 * @Description:单例和多线程
 * @Date:Created in  19:59 2018/4/13
 * @ModefiedBy:
 */
public class DoubleCheckSingleton {

    private static DoubleCheckSingleton  singleton;
    /**
    　　* @Description: 得到DoubleCheckSingleton对象，双检查锁机制
    　　* @param ${tags}
    　　* @return ${return_type}
    　　* @throws
    　　* @author 付风松
    　　* @date 2018/4/13 20:03
    　　*/
    public static DoubleCheckSingleton getSingleton(){
        if (singleton==null){
            /*休眠三秒*/
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (DoubleCheckSingleton.class){
                /*这边为什么要再次判断呢？如果不判断的话，那么下面输出的hashCode码是不一致的
                *
                * */
                if (singleton==null){
                    singleton=new DoubleCheckSingleton();
                }
            }
        }
        return singleton;
    }

    public static void main(String[] args) {
        for (int i=0;i<10;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(" hashCode  "+getSingleton().hashCode());
                }
            }).start();
        }
    }


}
