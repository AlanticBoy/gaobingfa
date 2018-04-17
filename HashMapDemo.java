package com.fusong.thread;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/7.
 */
public class HashMapDemo {

    public static void main(String[] args) {

        Map<String,String> map=new HashMap<>();
     for (int i=0;i<5;i++){
         new Thread(new Runnable() {
             @Override
             public void run() {
              while (true){
                  try {
                      Thread.sleep(100);
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
                  map.put("name","fusong");
                  System.out.println(" has put data ");
              }
             }
         }).start();new Thread(new Runnable() {
             @Override
             public void run() {
                 try {
                     Thread.sleep(100);
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
                 String s=map.get("name");
                 System.out.println(" get data that   "+s);
             }
         }).start();
     }
    }


}
