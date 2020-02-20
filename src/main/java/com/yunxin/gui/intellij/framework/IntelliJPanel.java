package com.yunxin.gui.intellij.framework;

import com.yunxin.gui.share.SwingUtils;
import com.yunxin.gui.share.component.BetterGlassPane;
import org.jdesktop.swingx.JXEditorPane;
import org.jdesktop.swingx.JXFrame;
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


    IJPanelTabBar[] tabBars = new IJPanelTabBar[4];

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

        tabBars[LEFT] = new IJPanelTabBar(this, LEFT);
        tabBars[RIGHT] = new IJPanelTabBar(this, RIGHT);
        tabBars[TOP] = new IJPanelTabBar(this,TOP);
        tabBars[BOTTOM] = new IJPanelTabBar(this,BOTTOM);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(tabBars[LEFT],BorderLayout.WEST);
        getContentPane().add(tabBars[RIGHT],BorderLayout.EAST);
        getContentPane().add(tabBars[TOP],BorderLayout.NORTH);
        getContentPane().add(tabBars[BOTTOM],BorderLayout.SOUTH);

        getContentPane().setComponentZOrder(tabBars[LEFT],getContentPane().getComponentCount()-1);
        getContentPane().setComponentZOrder(tabBars[RIGHT],getContentPane().getComponentCount()-1);
        getContentPane().setComponentZOrder(tabBars[TOP],getContentPane().getComponentCount()-1);
        getContentPane().setComponentZOrder(tabBars[BOTTOM],getContentPane().getComponentCount()-1);

        getContentPane().addMouseListener(new MouseAdapter() {});
        getContentPane().addMouseMotionListener(new MouseAdapter() {});

        getGlassPane().addMouseListener(this);
        getGlassPane().addMouseMotionListener(this);

    }


    public IJTabItem addTab(int direction, int split_mode, String text, Icon icon, AbstractPartView view){
        return tabBars[direction].getMode(split_mode).addTab(text,icon,view);
    }

    public java.util.List<IJTabItem> getTabs(int direction, int split_mode){
        java.util.List<IJTabItem> list = new ArrayList<>();
        if(split_mode==MODE_SPLIT){
            Component[] components = tabBars[direction].splitMode.getComponents();
            for(Component c: components){
                if(c instanceof IJTabItem){
                    list.add((IJTabItem) c);
                }
            }
        }else if(split_mode == MODE_UNSPLIT){
            Component[] components = tabBars[direction].unSplitMode.getComponents();
            for(Component c: components){
                if(c instanceof IJTabItem){
                    list.add((IJTabItem) c);
                }
            }
        }

        return list;
    }

    public java.util.List<IJTabItem> getTabs(int direction){
        java.util.List<IJTabItem> list =  getTabs(direction,MODE_UNSPLIT);
        list.addAll(getTabs(direction,MODE_SPLIT));
        return list;
    }

    public IJTabItem getActiveTab(int direction, int split_mode){
        if(split_mode==MODE_UNSPLIT){
            Component[] components = tabBars[direction].unSplitMode.getComponents();
            for(Component c: components){
                if(c instanceof IJTabItem){
                    if(((IJTabItem) c).isSelected()){
                        return (IJTabItem) c;
                    }
                }
            }
        }else if(split_mode==MODE_SPLIT){
            Component[] components = tabBars[direction].splitMode.getComponents();
            for(Component c: components){
                if(c instanceof IJTabItem){
                    if(((IJTabItem) c).isSelected()){
                        return (IJTabItem) c;
                    }
                }
            }
        }
        return null;
    }

    public IJPanelTabToolBar getToolBar(int direction){ return tabBars[direction].toolBar; }

    public void setTabVisible(int direction, boolean visible){ tabBars[direction].modeArea.setVisible(visible); }

    public void setTabItemViewSize(IJTabItem item, int size){ item.tabViewSize = size;}

    void updateHighLightBounds(){
        for(int i=0;i<4;i++){
            tabBars[i].updateHoverSize();
        }
    }

    public void clearHighLighter(){
        ((JXPanel)getGlassPane()).removeAll();
        ((JXPanel)getGlassPane()).updateUI();
        updateHighLightBounds();
    }

    public static void main(String[] args){


        com.formdev.flatlaf.FlatDarkLaf.install();

        UIManager.put("Button.iconTextGap",4);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                IntelliJPanel intelliJPanel = new IntelliJPanel();
                intelliJPanel.addTab(IJPanelTabBar.LEFT,MODE_UNSPLIT,"Project",new com.formdev.flatlaf.icons.FlatFileViewComputerIcon(),new SimplePartView("Project"){{setPartContent(new JXEditorPane());}});
                intelliJPanel.getToolBar(BOTTOM).add(new JToggleButton(new com.formdev.flatlaf.icons.FlatFileViewFloppyDriveIcon()){
                    {
                        addItemListener(new ItemListener() {
                            @Override
                            public void itemStateChanged(ItemEvent e) {
                                JToggleButton button = (JToggleButton) e.getSource();
                                if(button.isSelected()){
                                    intelliJPanel.setTabVisible(IntelliJPanel.LEFT,true);
                                    intelliJPanel.setTabVisible(IntelliJPanel.RIGHT,true);
                                    intelliJPanel.setTabVisible(IntelliJPanel.TOP,true);
                                    intelliJPanel.setTabVisible(IntelliJPanel.BOTTOM,true);
                                }else{
                                    intelliJPanel.setTabVisible(IntelliJPanel.LEFT,false);
                                    intelliJPanel.setTabVisible(IntelliJPanel.RIGHT,false);
                                    intelliJPanel.setTabVisible(IntelliJPanel.TOP,false);
                                    intelliJPanel.setTabVisible(IntelliJPanel.BOTTOM,false);
                                }
                            }
                        });
                    }
                });

                JXFrame frame = new JXFrame("Test");
                frame.getContentPane().setLayout(new BorderLayout());
                frame.getContentPane().add(intelliJPanel,BorderLayout.CENTER);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setPreferredSize(new Dimension(1024,768));
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }


    @Override
    public void mouseDragged(MouseEvent e) {

        synchronized (getTreeLock()){
            if(activeResizeTabBar<0) return;
            if(!isDragging){
                isDragging = true;
            }


            Dimension tmpDim = tabBars[activeResizeTabBar].getSize();
            if(activeResizeTabBar==LEFT){
                tmpDim.width = e.getX() - tabBars[activeResizeTabBar].tabArea.getWidth();
                for(IJTabItem tabItem: tabBars[activeResizeTabBar].getActiveTabs()){
                    tabItem.tabViewSize = tmpDim.width;
                }
            }else if(activeResizeTabBar==RIGHT){
                tmpDim.width = (getWidth()-e.getX()) - tabBars[activeResizeTabBar].tabArea.getWidth();
                for(IJTabItem tabItem: tabBars[activeResizeTabBar].getActiveTabs()){
                    tabItem.tabViewSize = tmpDim.width;
                }

            }else if(activeResizeTabBar==TOP){
                tmpDim.height = e.getY() - tabBars[activeResizeTabBar].tabArea.getHeight();
                for(IJTabItem tabItem: tabBars[activeResizeTabBar].getActiveTabs()){
                    tabItem.tabViewSize = tmpDim.height;
                }
            }else if(activeResizeTabBar==BOTTOM){
                tmpDim.height = (getHeight()-e.getY()) - tabBars[activeResizeTabBar].tabArea.getHeight();
                for(IJTabItem tabItem: tabBars[activeResizeTabBar].getActiveTabs()){
                    tabItem.tabViewSize = tmpDim.height;
                }
            }
            if(tmpDim.width<=0){
                tmpDim.width=0;
            }
            if(tmpDim.height<=0){
                tmpDim.height=0;
            }
            tabBars[activeResizeTabBar].tabContent.setPreferredSize(tmpDim);

            tabBars[activeResizeTabBar].updateUI();
        }

    }



    @Override
    public void mouseMoved(MouseEvent e) {
        if(!isDragging){
            Rectangle r = null;
            for(int i=0;i<4;i++){
                r = tabBars[i].getSliderBound();
                if(r!=null){
                    if(r.contains(e.getPoint())&&tabBars[i].isActive()){
                        getGlassPane().setCursor(tabBars[i].getSliderCursor());
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



}
