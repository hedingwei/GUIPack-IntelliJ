package com.yunxin.gui.share;

import com.formdev.flatlaf.util.ColorFunctions;
import com.yunxin.gui.share.icon.CompoundIcon;
import com.yunxin.gui.share.icon.RotatedIcon;
import com.yunxin.gui.share.icon.TextIcon;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

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



    public static Color darken(Color color,int amount){
        return ColorFunctions.applyFunctions(color,new ColorFunctions.Darken(amount,true,false));
    }

}
