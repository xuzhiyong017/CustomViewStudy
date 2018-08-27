package com.example.mylibrary;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * @author: xuzhiyong
 * @project: CustomViewStudy
 * @package: com.sky.customviewstudy
 * @description: Java Test class
 * @date: 2018/6/12
 * @time: 9:16
 * @Email: 18971269648@163.com
 */
public class JavaTest {

    public static void main(String[] args){

//        byte[] bytes = new byte[1];
//        new String(bytes);
//
//        String date = "2018-08-06 02:44:15";
//        String date1 = "2018-08-06 02:44:15";
//        System.out.println(isSameDay(date,date1));

        AtomicInteger count = new AtomicInteger();

//        Observable.range(1, 10)
//                .doOnNext(ignored -> count.incrementAndGet())
//                .ignoreElements()
//                .andThen(Single.just(count.get()))
//                .subscribe(System.out::println);


        Observable.range(1, 10)
                .doOnNext(ignored ->
                {
                    count.incrementAndGet();
                    System.out.println("1");
                })
                .ignoreElements()
                .andThen(Single.defer(() -> Single.just(count.get())))
                .subscribe(System.out::println);

//        final ArrayList<Person> personArrayList = new ArrayList<>();
//
//            for(int i= 0; i< 3 ; i++){
//                final Person person = new Person();
//                person.index = i;
//
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        personArrayList.add(person);
//                    }
//                }).start();
//
//            }
//
//        try {
//            Thread.sleep(3000L);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println(Arrays.toString(personArrayList.toArray()));
        String name = getName();
        System.out.println(name == null);

    }

    public static String getName(){
        return null;
    }


    public static boolean isSameDay(String startTime,String endTime){
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat newDateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        try {
            String start = newDateFormatter.format(dateFormatter.parse(startTime));
            String end = newDateFormatter.format(dateFormatter.parse(endTime));
            if(start != null && start.equals(end)){
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    public static class Person{
        public int index;

        @Override
        public String toString() {
            return "index="+index;
        }
    }
}
