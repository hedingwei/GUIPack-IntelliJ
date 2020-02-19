package com.yunxin.mygui.share.layout;

import java.awt.*;

public class HorizontalLayout implements LayoutManager {
    private int gap = 0;
    private boolean left2Right = true;

    public HorizontalLayout() {
        this(0,true);
    }

    public HorizontalLayout(int gap) {
        this.gap = gap;
    }
    public HorizontalLayout(int gap, boolean left) {
        this.gap = gap;
        this.left2Right = left;
    }

    public int getGap() {
        return this.gap;
    }

    public void setGap(int gap) {
        this.gap = gap;
    }

    public void addLayoutComponent(String name, Component c) {
    }

    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()){

            if(left2Right){
                Insets insets = parent.getInsets();
                Dimension size = parent.getSize();
                int height = size.height - insets.top - insets.bottom;
                int width = insets.left;
                int i = 0;

                for(int c = parent.getComponentCount(); i < c; ++i) {
                    Component m = parent.getComponent(i);
                    if (m.isVisible()) {
                        m.setBounds(width, insets.top, m.getPreferredSize().width, height);
                        width += m.getSize().width + this.gap;
                    }
                }
            }else {
                Insets insets = parent.getInsets();
                Dimension size = parent.getSize();
                int height = size.height - insets.top - insets.bottom;
                int width = size.width;
                int i = 0;

                for(int c = parent.getComponentCount(); i < c; ++i) {
                    Component m = parent.getComponent(i);
                    if (m.isVisible()) {
                        m.setBounds(width-m.getPreferredSize().width, insets.top, m.getPreferredSize().width, height);
                        width = width - m.getPreferredSize().width - gap;
                    }
                }
            }
        }



    }

    public Dimension minimumLayoutSize(Container parent) {
        return this.preferredLayoutSize(parent);
    }

    public Dimension preferredLayoutSize(Container parent) {
        Insets insets = parent.getInsets();
        Dimension pref = new Dimension(0, 0);
        int i = 0;

        for(int c = parent.getComponentCount(); i < c; ++i) {
            Component m = parent.getComponent(i);
            if (m.isVisible()) {
                Dimension componentPreferredSize = parent.getComponent(i).getPreferredSize();
                pref.height = Math.max(pref.height, componentPreferredSize.height);
                pref.width += componentPreferredSize.width + this.gap;
            }
        }
        pref.width += insets.left + insets.right;
        pref.height += insets.top + insets.bottom;
        return pref;
    }

    public void removeLayoutComponent(Component c) {
    }
}