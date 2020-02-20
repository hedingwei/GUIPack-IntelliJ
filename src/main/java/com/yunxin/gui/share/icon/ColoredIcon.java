package com.yunxin.gui.share.icon;

import com.yunxin.gui.share.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class ColoredIcon implements Icon{

    Icon wrappedIcon;
    Color color;
    int width,height;
    public ColoredIcon(Icon wrappedIcon, Color color, int w, int h) {
        this.wrappedIcon = wrappedIcon;
        this.color = color;
        this.width = w;
        this.height = h;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D)g.create();

        //  The "desktophints" is supported in JDK6

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Map map = (Map)(toolkit.getDesktopProperty("awt.font.desktophints"));

        if (map != null)
        {
            g2.addRenderingHints(map);
        }
        else
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
        Color tmp = g2.getColor();
        g2.setColor(color);
        g2.fillRect(0,0,getIconWidth(),getIconHeight());
        g2.setColor(SwingUtils.darken(color,-100));
        wrappedIcon.paintIcon(c,g2,x,y);
    }

    @Override
    public int getIconWidth() {
        return width;
    }

    @Override
    public int getIconHeight() {
        return this.height;
    }
}
