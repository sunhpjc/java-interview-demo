package com.sunhp.interview;

import java.util.concurrent.TimeUnit;

class MyDate{
    int number = 0;

    public void addTo60(){
        this.number = 60;
    }
}

/**
 * 验证volatile的可见性
 * 1.1 假如 int number = 0 ，number之前没有被volatile关键字修饰------>启动之后发现没有打印“mission is over”，说明main线程没有感知到值的变化
 */
public class VolatileDemo {
    public static void main(String[] args) {
        MyDate myDate = new MyDate();//资源类

        //main线程读到的number应该是初始值0

        new Thread(()->{
            System.out.println(Thread.currentThread().getName() + "\t come in");
            //暂停一会儿线程
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            myDate.addTo60();
            System.out.println(Thread.currentThread().getName() + "\t update value" + myDate.number);
        },"AAA").start();

        //第二个线程是主线程
        while (myDate.number == 0){
            //main线程一直循环等待，直到number的值不再等于0
        }
        System.out.println(Thread.currentThread().getName() + "\t mission is over");

    }
}
