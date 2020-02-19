package com.yunxin.mygui.intellij.mcomponent.partview;

import org.jdesktop.swingx.JXPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

import static com.yunxin.mygui.intellij.mcomponent.partview.IJPanelTabBar.*;


public class IJPanelTabMode extends JXPanel {

    private static final Component[] EMPTY_ARRAY = new Component[0];

    int mode;
    IJPanelTabBar tab;
    ArrayList<JComponent> tabList = new ArrayList<>();

    Rectangle boundInGlass = null;

     IJTabItem activeItem = null;


    public IJPanelTabMode(IJPanelTabBar tab, int mode) {
        this.tab = tab;
        this.mode = mode;
        initComponents();

        setDoubleBuffered(true);
        setBorder(new MLineBorder(tab.intelliJPanel.defaultBorderColor,0,false,false,false,false));
    }

    private void initComponents() {
        if (((tab.direction == LEFT) || (tab.direction == RIGHT))) {
            if (mode == MODE_SPLIT) {
                setLayout(new XVerticalLayout(0, false));
            } else {
                setLayout(new XVerticalLayout(0, true));
            }
        } else if (((tab.direction == TOP) || (tab.direction == BOTTOM))) {
            if (mode == MODE_SPLIT) {
                setLayout(new XHorizontalLayout(0, false));
            } else {
                setLayout(new XHorizontalLayout(0, true));
            }
        }
    }

    public void addTab(JComponent component) {
        super.add(component);
        tabList.add(component);
    }

    public void addTab(String text, Icon icon, AbstractPartView content){
        IJTabItem item = new IJTabItem(text,icon,this,content);
        item.setBackground(getBackground());
        addTab(item);
    }

    public int getTabCount() {
        return tabList.size();
    }

    public int getTabIndex(JComponent component) {
        return tabList.indexOf(component);
    }



    public void removeTab(JComponent component) {
        try {
            tabList.remove(component);
        }catch (Throwable t){t.printStackTrace();}
        try{
            remove(component);
        }catch (Throwable t){t.printStackTrace();}

    }

    public void addTab(JComponent component, int index) {

        tabList.add(index, component);
        add(component, index);
    }

    @Override
    public Component[] getComponents() {
        return tabList.toArray(EMPTY_ARRAY);
    }

    @Override
    public int getComponentCount() {
        return super.getComponentCount();
    }



    public void addTabAfter(JComponent target, JComponent toAdd) {
        if (toAdd == target) return;

        int indexOfTaget = tabList.indexOf(target);
        if (indexOfTaget >= 0) {
            int indexOfToAdd = tabList.indexOf(toAdd);
            if (indexOfToAdd >= 0) { // 两个元素都在
                if((indexOfTaget+1)==indexOfToAdd){ //正好是自然顺序，那就算了
                    return;
                }else{
                    tabList.add(indexOfTaget+1,toAdd);
                    tabList.remove(indexOfToAdd);
                }
            }else{
                addTab(toAdd,indexOfTaget+1);
            }
        }
    }

    public void addTabBefore(JComponent target, JComponent toAdd) {
        if (toAdd == target) return;

        int indexOfTaget = tabList.indexOf(target);
        if (indexOfTaget >= 0) {
            int indexOfToAdd = tabList.indexOf(toAdd);
            if (indexOfToAdd >= 0) { // 两个元素都在
                if((indexOfTaget+1)==indexOfToAdd){ //正好是自然顺序，那就算了
                    return;
                }else{
                    tabList.add(indexOfTaget,toAdd);
                    tabList.remove(indexOfToAdd);
                }
            }else{
                addTab(toAdd,indexOfTaget);
            }
        }
    }

    public void setSelectedExcept(IJTabItem item, boolean select){
        for(JComponent component: tabList){
            if(item==component) {
                item.setSelected(select);
            }else{
                if(component instanceof IJTabItem){
                    ((IJTabItem) component).setSelected(!select);
                }
            }

        }
    }


    public class XVerticalLayout implements LayoutManager {
        private int gap = 0;

        private boolean top2bottom = true;

        public XVerticalLayout() {
        }

        public XVerticalLayout(int gap) {
            this.gap = gap;
        }

        public XVerticalLayout(int gap, boolean top2bottom) {
            this.gap = gap;
            this.top2bottom = top2bottom;
        }

        public int getGap() {
            return gap;
        }

        public void setGap(int gap) {
            this.gap = gap;
        }

        public void addLayoutComponent(String name, Component c) {
        }

        public void layoutContainer(Container parent) {
            synchronized (parent.getTreeLock()) {
                if (top2bottom) {
                    Insets insets = parent.getInsets();
                    Dimension size = parent.getSize();
                    int width = size.width - insets.left - insets.right;
                    int height = insets.top;

                    for (int i = 0, c = tabList.size(); i < c; i++) {
                        Component m = tabList.get(i);
                        if (m.isVisible()) {
                            m.setBounds(insets.left, height, width, m.getPreferredSize().height);
                            height += m.getSize().height + gap;
                        }
                    }
                } else {
                    Insets insets = parent.getInsets();
                    Dimension size = parent.getSize();
                    int width = size.width - insets.left - insets.right;
                    int height = size.height;

                    for (int i = 0, c = tabList.size(); i < c; i++) {
                        Component m = tabList.get(i);
                        if (m.isVisible()) {
                            m.setBounds(insets.left, height - m.getPreferredSize().height, width, m.getPreferredSize().height);
                            height = height - m.getSize().height - gap;
                        }
                    }
                }
            }
        }

        public Dimension minimumLayoutSize(Container parent) {
            return preferredLayoutSize(parent);
        }

        public Dimension preferredLayoutSize(Container parent) {
            Insets insets = parent.getInsets();
            Dimension pref = new Dimension(0, 0);

            for (int i = 0, c = tabList.size(); i < c; i++) {
                Component m = tabList.get(i);
                if (m.isVisible()) {
                    Dimension componentPreferredSize =
                            tabList.get(i).getPreferredSize();
                    pref.height += componentPreferredSize.height + gap;
                    pref.width = Math.max(pref.width, componentPreferredSize.width);
                }
            }

            pref.width += insets.left + insets.right;
            pref.height += insets.top + insets.bottom;

            return pref;
        }

        public void removeLayoutComponent(Component c) {
        }

    }

    public class XHorizontalLayout implements LayoutManager {
        private int gap = 0;
        private boolean left2Right = true;

        public XHorizontalLayout() {
            this(0, true);
        }

        public XHorizontalLayout(int gap) {
            this.gap = gap;
        }

        public XHorizontalLayout(int gap, boolean left) {
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
            synchronized (parent.getTreeLock()) {

                if (left2Right) {
                    Insets insets = parent.getInsets();
                    Dimension size = parent.getSize();
                    int height = size.height - insets.top - insets.bottom;
                    int width = insets.left;
                    int i = 0;

                    for (int c = tabList.size(); i < c; ++i) {
                        Component m = tabList.get(i);
                        if (m.isVisible()) {
                            m.setBounds(width, insets.top, m.getPreferredSize().width, height);
                            width += m.getSize().width + this.gap;
                        }
                    }
                } else {
                    Insets insets = parent.getInsets();
                    Dimension size = parent.getSize();
                    int height = size.height - insets.top - insets.bottom;
                    int width = size.width;
                    int i = 0;

                    for (int c = tabList.size(); i < c; ++i) {
                        Component m = tabList.get(i);
                        if (m.isVisible()) {
                            m.setBounds(width - m.getPreferredSize().width, insets.top, m.getPreferredSize().width, height);
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

            for (int c = tabList.size(); i < c; ++i) {
                Component m = tabList.get(i);
                if (m.isVisible()) {
                    Dimension componentPreferredSize = tabList.get(i).getPreferredSize();
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

    public String toDebugString(){
        return "<"+tab.direction+","+mode+">";
    }

    public int getVisibleTabCount(){
        int c = 0;
        for(JComponent component : tabList){
            if(component instanceof IJTabItem){
                c++;
            }
        }
        return c;
    }


}
