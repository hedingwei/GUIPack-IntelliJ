package com.yunxin.guipack.intellij.framework;

import com.yunxin.guipack.share.SwingUtils;
import com.yunxin.guipack.share.component.BetterGlassPane;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXRootPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class IntelliJPanel extends JXRootPane implements MouseMotionListener, MouseListener {

    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int BOTTOM = 2;
    public static final int TOP = 3;

    public static int MODE_SPLIT = 0;
    public static int MODE_UNSPLIT = 1;


    Dock[] docks = new Dock[4];

    static Color defaultBorderColor = null;
    static int defaultTabDivederSize = 6;
    static Font defaultFont = new Font(Font.DIALOG,Font.PLAIN,10);

    boolean isDragging = false;
    int activeResizeTabBar = -1;

    public IntelliJPanel() {

        defaultBorderColor = (Color) UIManager.getLookAndFeelDefaults().get("Panel.background");
        new BetterGlassPane(this);
        defaultBorderColor = SwingUtils.darken(defaultBorderColor,25);
        initComponents();

    }

    private void initComponents() {

        docks[LEFT] = new Dock(this, LEFT);
        docks[RIGHT] = new Dock(this, RIGHT);
        docks[TOP] = new Dock(this,TOP);
        docks[BOTTOM] = new Dock(this,BOTTOM);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(docks[LEFT],BorderLayout.WEST);
        getContentPane().add(docks[RIGHT],BorderLayout.EAST);
        getContentPane().add(docks[TOP],BorderLayout.NORTH);
        getContentPane().add(docks[BOTTOM],BorderLayout.SOUTH);

        getContentPane().setComponentZOrder(docks[LEFT],getContentPane().getComponentCount()-1);
        getContentPane().setComponentZOrder(docks[RIGHT],getContentPane().getComponentCount()-1);
        getContentPane().setComponentZOrder(docks[TOP],getContentPane().getComponentCount()-1);
        getContentPane().setComponentZOrder(docks[BOTTOM],getContentPane().getComponentCount()-1);

        getContentPane().addMouseListener(new MouseAdapter() {});
        getContentPane().addMouseMotionListener(new MouseAdapter() {});

        getGlassPane().addMouseListener(this);
        getGlassPane().addMouseMotionListener(this);

    }

    public void setCentralComponent(Component component){
        getContentPane().add(component,BorderLayout.CENTER);
        component.addMouseListener(new MouseAdapter() {});
        component.addMouseMotionListener(new MouseAdapter() {});

    }


    public DockTab addTab(int direction, int split_mode, String text, Icon icon, DockTabView view){
        return docks[direction].getMode(split_mode).addTab(text,icon,view);
    }

    public DockTab addTab(Direction direction, SplitMode split_mode, String text, Icon icon, DockTabView view){
        return docks[direction.ordinal()].getMode(split_mode.ordinal()).addTab(text,icon,view);
    }




    public java.util.List<DockTab> getTabs(int direction, int split_mode){
        java.util.List<DockTab> list = new ArrayList<>();
        if(split_mode==MODE_SPLIT){
            Component[] components = docks[direction].splitModeTabBar.getComponents();
            for(Component c: components){
                if(c instanceof DockTab){
                    list.add((DockTab) c);
                }
            }
        }else if(split_mode == MODE_UNSPLIT){
            Component[] components = docks[direction].unSplitModeTabBar.getComponents();
            for(Component c: components){
                if(c instanceof DockTab){
                    list.add((DockTab) c);
                }
            }
        }

        return list;
    }

    public java.util.List<DockTab> getTabs(int direction){
        java.util.List<DockTab> list =  getTabs(direction,MODE_UNSPLIT);
        list.addAll(getTabs(direction,MODE_SPLIT));
        return list;
    }

    public DockTab getActiveTab(int direction, int split_mode){
        if(split_mode==MODE_UNSPLIT){
            Component[] components = docks[direction].unSplitModeTabBar.getComponents();
            for(Component c: components){
                if(c instanceof DockTab){
                    if(((DockTab) c).isSelected()){
                        return (DockTab) c;
                    }
                }
            }
        }else if(split_mode==MODE_SPLIT){
            Component[] components = docks[direction].splitModeTabBar.getComponents();
            for(Component c: components){
                if(c instanceof DockTab){
                    if(((DockTab) c).isSelected()){
                        return (DockTab) c;
                    }
                }
            }
        }
        return null;
    }

    public DockToolBar getToolBar(int direction){ return docks[direction].toolBar; }

    public DockToolBar getToolBar(Direction direction){ return docks[direction.ordinal()].toolBar; }


    public void setTabVisible(int direction, boolean visible){ docks[direction].tabBarArea.setVisible(visible); }

    public void setTabItemViewSize(DockTab item, int size){ item.tabViewSize = size;}

    void updateHighLightBounds(){
        for(int i=0;i<4;i++){
            docks[i].updateHoverSize();
        }
    }


    public Dock getDock(Direction direction){
        return docks[direction.ordinal()];
    }


    public void clearHighLighter(){
        ((JXPanel)getGlassPane()).removeAll();
        ((JXPanel)getGlassPane()).updateUI();
        updateHighLightBounds();
    }



    @Override
    public void mouseDragged(MouseEvent e) {

        synchronized (getTreeLock()){
            if(activeResizeTabBar<0) return;
            if(!isDragging){
                isDragging = true;
            }


            Dimension tmpDim = docks[activeResizeTabBar].getSize();
            if(activeResizeTabBar==LEFT){
                tmpDim.width = e.getX() - docks[activeResizeTabBar].toolBarArea.getWidth();
                for(DockTab tabItem: docks[activeResizeTabBar].getActiveTabs()){
                    tabItem.tabViewSize = tmpDim.width;
                }
            }else if(activeResizeTabBar==RIGHT){
                tmpDim.width = (getWidth()-e.getX()) - docks[activeResizeTabBar].toolBarArea.getWidth();
                for(DockTab tabItem: docks[activeResizeTabBar].getActiveTabs()){
                    tabItem.tabViewSize = tmpDim.width;
                }

            }else if(activeResizeTabBar==TOP){
                tmpDim.height = e.getY() - docks[activeResizeTabBar].toolBarArea.getHeight();
                for(DockTab tabItem: docks[activeResizeTabBar].getActiveTabs()){
                    tabItem.tabViewSize = tmpDim.height;
                }
            }else if(activeResizeTabBar==BOTTOM){
                tmpDim.height = (getHeight()-e.getY()) - docks[activeResizeTabBar].toolBarArea.getHeight();
                for(DockTab tabItem: docks[activeResizeTabBar].getActiveTabs()){
                    tabItem.tabViewSize = tmpDim.height;
                }
            }
            if(tmpDim.width<=0){
                tmpDim.width=0;
            }
            if(tmpDim.height<=0){
                tmpDim.height=0;
            }
            docks[activeResizeTabBar].tabContent.setPreferredSize(tmpDim);

            docks[activeResizeTabBar].updateUI();
        }

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(!isDragging){
            Rectangle r = null;
            for(int i=0;i<4;i++){
                r = docks[i].getSliderBound();
                if(r!=null){
                    if(r.contains(e.getPoint())&& docks[i].isActive()){
                        getGlassPane().setCursor(docks[i].getSliderCursor());
                        activeResizeTabBar = i;
                        return;
                    }else{
                        if(!isDragging){
                            activeResizeTabBar = -1;
                            getGlassPane().setCursor(Cursor.getDefaultCursor());
                        }

                    }
                }
            }
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(isDragging){
            isDragging = false;
            activeResizeTabBar = -1;
            getGlassPane().setCursor(Cursor.getDefaultCursor());
        }

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


    public static enum Direction {
        Left,Right,Bottom,Top
    }

    public static enum SplitMode {
        Split,UnSplit
    }


}
