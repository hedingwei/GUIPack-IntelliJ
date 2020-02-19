package com.yunxin.mygui.intellij.mcomponent.partview;

import com.formdev.flatlaf.util.ColorFunctions;
import com.yunxin.mygui.share.icon.CompoundIcon;
import com.yunxin.mygui.share.icon.RotatedIcon;
import com.yunxin.mygui.share.icon.TextIcon;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;

public class SwingUtils {

    private  static Class containerClass = Container.class;

    public static JLabel buildLabel(String text, Icon icon,RotatedIcon.Rotate rotate){
        JLabel label = new JLabel();
        TextIcon textIcon = new TextIcon(label,text);
        CompoundIcon ci = null;
        if(icon!=null){
            ci = new CompoundIcon(icon,textIcon);
        }else{
            ci = new CompoundIcon(textIcon);
        }

        RotatedIcon ri = new RotatedIcon(ci, rotate);;
        label.setIcon(ri);
        return label;
    }

    public static void setTextAndIcon(JComponent component, String text, Icon icon, RotatedIcon.Rotate rotate){
        TextIcon textIcon = new TextIcon(component,text);
        CompoundIcon ci = null;
        if(icon!=null){
            ci = new CompoundIcon(icon,textIcon);
        }else{
            ci = new CompoundIcon(textIcon);
        }
        RotatedIcon ri = new RotatedIcon(ci, rotate);
        try {
           Method method = component.getClass().getMethod("setIcon",Icon.class);
           method.invoke(component,ri);
        } catch (Throwable e) { }
    }

    public static ArrayList<Component> getComponentList(Container container){
        try {
            Field field = containerClass.getDeclaredField("component");
            field.setAccessible(true);
            ArrayList<Component> componentArrayList = (ArrayList<Component>) field.get(container);
            return componentArrayList;
        } catch (Throwable e) {
        }
        return null;
    }

    public static int indexOf(Container container, Component c1){
        try {
            int ic1;
            Field field = containerClass.getDeclaredField("component");
            field.setAccessible(true);
            ArrayList<Component> componentArrayList = (ArrayList<Component>) field.get(container);
            return componentArrayList.indexOf(c1);

        } catch (Throwable e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void switchComponentOrder(Container container, Component c1, Component c2,boolean revalidate){
        try {
            int ic1, ic2;
            Field field = containerClass.getDeclaredField("component");
            field.setAccessible(true);
            ArrayList<Component> componentArrayList = (ArrayList<Component>) field.get(container);
            ic1 = componentArrayList.indexOf(c1);
            if(ic1<0) return;
            ic2 = componentArrayList.indexOf(c2);
            if(ic2<0) return;
            Collections.swap(componentArrayList,ic1,ic2);
            if(revalidate){
                container.revalidate();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void setBefore(Container container, Component before, Component c2,boolean revalidate){
        try {
            int ic1, ic2;
            Field field = containerClass.getDeclaredField("component");
            field.setAccessible(true);
            ArrayList<Component> componentArrayList = (ArrayList<Component>) field.get(container);
            ic1 = componentArrayList.indexOf(before);
            if(ic1<0) return;
            ic2 = componentArrayList.indexOf(c2);
            if(ic2<0) return;

            if((ic2+1)==ic1) return;

            componentArrayList.remove(ic2);

            componentArrayList.add(ic1,c2);

            if(revalidate){
                container.revalidate();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void setAfter(Container container, Component after, Component c2,boolean revalidate){
        try {
            int ic1, ic2;
            Field field = containerClass.getDeclaredField("component");
            field.setAccessible(true);
            ArrayList<Component> componentArrayList = (ArrayList<Component>) field.get(container);
            ic1 = componentArrayList.indexOf(after);
            if(ic1<0) return;
            ic2 = componentArrayList.indexOf(c2);
            if(ic2<0) return;

            componentArrayList.remove(ic2);

            if((ic1+1)>componentArrayList.size()){
                componentArrayList.add(c2);
            }else{
                componentArrayList.add(ic1+1,c2);
            }

            if(revalidate){
                container.revalidate();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    public static Color darken(Color color,int amount){
        return ColorFunctions.applyFunctions(color,new ColorFunctions.Darken(amount,true,false));
    }

}
