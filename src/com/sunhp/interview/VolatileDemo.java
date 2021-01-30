package com.sunhp.interview;

import java.util.concurrent.TimeUnit;

class MyDate{
    volatile int number = 0;

    public void addTo60(){
        this.number = 60;
    }

    //请注意，现在number是加了volatile关键字的
    public void addPlusPlus(){
        number ++;
    }
}

/**
 * 1.验证volatile的可见性
 *  1.1 假如 int number = 0 ，number之前没有被volatile关键字修饰------>启动之后发现没有打印“mission is over”，说明main线程没有感知到值的变化
 *  1.2 添加volatile可以保证可见性
 * 2.验证volatile不保证原子性
 *  2.1 原子性指的是什么？    不可分割，完整性，要么成功，要么失败，一个完整的业务中间不能穿插其它操作
 *  2.2 volatile是否可以保证原子性？ 不可以
 */
public class VolatileDemo {
    public static void main(String[] args) {
        MyDate myDate = new MyDate();
        for (int i = 0; i < 20; i++) {
            new Thread(()->{
                for (int j = 0; j < 1000; j++) {
                    myDate.addPlusPlus();
                }
            },String.valueOf(i)).start();
        }
        //需要等待上面20个线程完成之后，查看结果值
        while (Thread.activeCount() > 2){//系统默认两个线程，一个main线程，一个GC线程
            Thread.yield();
        }

        System.out.println(Thread.currentThread().getName() + "\t finally number value: "+ myDate.number);
    }

    //volatile可以保证可见性
    private static void seeOkByVolatile() {
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
            System.out.println(Thread.currentThread().getName() + "\t update value：" + myDate.number);
        },"AAA").start();

        //第二个线程是主线程
        while (myDate.number == 0){
            //main线程一直循环等待，直到number的值不再等于0
        }
        System.out.println(Thread.currentThread().getName() + "\t mission is over" + "value: "+ myDate.number);
    }

}
