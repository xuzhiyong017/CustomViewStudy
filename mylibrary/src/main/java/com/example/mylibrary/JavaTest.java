package com.example.mylibrary;

import java.util.ArrayList;

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

        byte[] bytes = new byte[1];
        new String(bytes);



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
    }


    public static class Person{
        public int index;

        @Override
        public String toString() {
            return "index="+index;
        }
    }
}
