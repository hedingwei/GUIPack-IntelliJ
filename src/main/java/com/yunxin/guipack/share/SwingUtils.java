package com.yunxin.guipack.share;

import com.formdev.flatlaf.util.ColorFunctions;
import com.yunxin.guipack.share.icon.CompoundIcon;
import com.yunxin.guipack.share.icon.RotatedIcon;
import com.yunxin.guipack.share.icon.TextIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

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

    public static BufferedImage component2Image(Component c){
        BufferedImage bufferedImage = new BufferedImage(c.getWidth(),c.getHeight(),BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Map map = (Map)(toolkit.getDesktopProperty("awt.font.desktophints"));
        if (map != null)
        {
            g2d.addRenderingHints(map);
        }
        else
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON );

        c.paint(g2d);
        return bufferedImage;
    }

}
