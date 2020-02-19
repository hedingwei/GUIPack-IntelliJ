package com.yunxin.mygui.share;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class XList {
    public static void main(String[] args) {
        List<String> list = new LinkedList<>();
        String s1 = new String("1");
        String s2 = new String("2");
        String s3 = new String("3");

        list.add(s1);
        list.add(s2);
        list.add(s3);
        list.add(s1);
        System.out.println(list);
        list.remove(0);
        System.out.println(list);
    }
}
