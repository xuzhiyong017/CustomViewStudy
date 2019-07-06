package com.example.mylibrary;

import android.text.TextUtils;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

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

////        byte[] bytes = new byte[1];
////        new String(bytes);
////
////        String date = "2018-08-06 02:44:15";
////        String date1 = "2018-08-06 02:44:15";
////        System.out.println(isSameDay(date,date1));
//
//        AtomicInteger count = new AtomicInteger();
//
////        Observable.range(1, 10)
////                .doOnNext(ignored -> count.incrementAndGet())
////                .ignoreElements()
////                .andThen(Single.just(count.get()))
////                .subscribe(System.out::println);
//
//
//        Observable.range(1, 10)
//                .doOnNext(ignored ->
//                {
//                    count.incrementAndGet();
//                    System.out.println("1");
//                })
//                .ignoreElements()
//                .andThen(Single.defer(() -> Single.just(count.get())))
//                .subscribe(System.out::println);
//
////        final ArrayList<Person> personArrayList = new ArrayList<>();
////
////            for(int i= 0; i< 3 ; i++){
////                final Person person = new Person();
////                person.index = i;
////
////                new Thread(new Runnable() {
////                    @Override
////                    public void run() {
////                        personArrayList.add(person);
////                    }
////                }).start();
////
////            }
////
////        try {
////            Thread.sleep(3000L);
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        }
////        System.out.println(Arrays.toString(personArrayList.toArray()));
//        String name = getName();
//        System.out.println(name == null);

//        Gson gson = new Gson();
//        System.out.println(gson.toJson(new TestJava("xuzhiyog")));
//        System.out.println(gson.toJson(new TestJava("",29)));

//        for (int i = 0; i < 30; i = i+ 1) {
//
//            System.out.println(String.format("%04d",i));
//        }
//        String cmd1 = "-y -i " + "concat:"+ "inputone.mp3"+"|"+ "inputtwo.mp3"+" -acodec copy "+"outpath.mp3";
//        System.out.println(cmd1);

        new JavaTest().testFlatMap();

    }

    private void testFlatMap() {
        Integer[] integers = {1,2,3,4,5};
        Observable.fromArray(integers)
                .flatMap(new Function<Integer, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Integer integer) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<Integer>() {
                            @Override
                            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                               new Thread(new Runnable() {
                                   @Override
                                   public void run() {
                                       try {
                                           Thread.sleep(3000);
                                       } catch (InterruptedException e) {
                                           e.printStackTrace();
                                       }
                                       emitter.onNext(integer);
                                       emitter.onComplete();
                                   }
                               }).start();

                            }
                        }).doOnTerminate(new Action() {
                            @Override
                            public void run() throws Exception {
                                System.out.println("doOnTerminate "+integer);
                            }
                        });
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        System.out.println("doOnTerminate");
                    }
                })
                .subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                System.out.println(o);
            }
        });
    }


    public static class TestJava{
        String name;
        int age;


        public TestJava(String name) {
            this.name = name;
        }

        public TestJava(int age){
            this.age = age;
        }

        public TestJava(String name, int age) {
            this.name = name;
            this.age = age;
        }
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
