package com.yunxin.mygui.intellij.mcomponent.partview;

import com.formdev.flatlaf.util.ColorFunctions;
import com.yunxin.mygui.share.component.BetterGlassPane;
import org.jdesktop.swingx.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import static com.yunxin.mygui.intellij.mcomponent.partview.IJPanelTabBar.*;

public class IntelliJPanel extends JXRootPane implements MouseMotionListener, MouseListener {


    IJPanelTabBar[] tabBars = new IJPanelTabBar[4];

    static Color defaultBorderColor = null;
    static int defaultTabDivederSize = 6;
    static Font defaultFont = new Font(Font.DIALOG,Font.PLAIN,10);

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
        getContentPane().add(new JXEditorPane(),BorderLayout.CENTER);

        getContentPane().setComponentZOrder(tabBars[LEFT],getContentPane().getComponentCount()-1);
        getContentPane().setComponentZOrder(tabBars[RIGHT],getContentPane().getComponentCount()-1);
        getContentPane().setComponentZOrder(tabBars[TOP],getContentPane().getComponentCount()-1);
        getContentPane().setComponentZOrder(tabBars[BOTTOM],getContentPane().getComponentCount()-1);


        getContentPane().addMouseListener(new MouseAdapter() {});
        getContentPane().addMouseMotionListener(new MouseAdapter() {
        });

        getGlassPane().addMouseListener(this);
        getGlassPane().addMouseMotionListener(this);


        tabBars[LEFT].getMode(MODE_SPLIT).addTab("Favoriates",null,new SimplePartView("Project Split"));
        tabBars[LEFT].getMode(MODE_UNSPLIT).addTab("Project",null,new SimplePartView("LEFT UNSplit 1"){{setPartContent(new JEditorPane());}});
        tabBars[LEFT].getMode(MODE_UNSPLIT).addTab("Structure",null,new SimplePartView("LEFT UNSplit 2"));

        tabBars[RIGHT].getMode(MODE_SPLIT).addTab("RIGHT SPLIT",null,new SimplePartView("RIGHT Split"));
        tabBars[RIGHT].getMode(MODE_UNSPLIT).addTab("RIGHT UNSPLIT",null,new SimplePartView("RIGHT UNSplit"));

        tabBars[BOTTOM].getMode(MODE_SPLIT).addTab("BOTTOM SPLIT",null,new SimplePartView("BOTTOM Split"));
        tabBars[BOTTOM].getMode(MODE_UNSPLIT).addTab("BOTTOM UNSPLIT",null,new SimplePartView("BOTTOM UNSplit"));

    }

    public void updateHighLightBounds(){
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

//        System.out.println("--------------");

//
//        System.out.println("default=========");

//        UIManager.getDefaults().get("control")

//        UIManager.put("JideSplitPane.dividerSize",0);
//        com.formdev.flatlaf.FlatLightLaf.install();
        com.formdev.flatlaf.FlatIntelliJLaf.install();
//        com.formdev.flatlaf.FlatDarculaLaf.install();
//        com.formdev.flatlaf.FlatDarkLaf.install();
//        com.formdev.flatlaf.ui.FlatToggleButtonUI



        UIManager.put("Button.iconTextGap",4);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JXFrame frame = new JXFrame("Test");
                frame.getContentPane().setLayout(new BorderLayout());
                frame.getContentPane().add(new IntelliJPanel(),BorderLayout.CENTER);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setPreferredSize(new Dimension(1024,768));
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });


    }

    Dimension tmpDim = new Dimension(0,0);

    @Override
    public void mouseDragged(MouseEvent e) {

        synchronized (getTreeLock()){
            if(activeResizeTabBar<0) return;
            if(!isDragging){
                isDragging = true;
            }


            tmpDim = tabBars[activeResizeTabBar].getSize();
            if(activeResizeTabBar==LEFT){
                tmpDim.width = e.getX() - tabBars[activeResizeTabBar].tabArea.getWidth();
            }else if(activeResizeTabBar==RIGHT){
                tmpDim.width = (getWidth()-e.getX()) - tabBars[activeResizeTabBar].tabArea.getWidth();

            }else if(activeResizeTabBar==TOP){
                tmpDim.height = e.getY() - tabBars[activeResizeTabBar].tabArea.getHeight();
            }else if(activeResizeTabBar==BOTTOM){
                tmpDim.height = (getHeight()-e.getY()) - tabBars[activeResizeTabBar].tabArea.getHeight();
            }
            if(tmpDim.width<=0){
                tmpDim.width=0;
            }
            if(tmpDim.height<=0){
                tmpDim.height=0;
            }
            tabBars[activeResizeTabBar].tabContent.setPreferredSize(tmpDim);
            tabBars[activeResizeTabBar].updateUI();
//            System.out.println("resizing:"+activeResizeTabBar+"\t"+tabBars[RIGHT].getBounds());
//            updateUI();
        }

    }

    boolean isDragging = false;
    int activeResizeTabBar = -1;

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
//        System.out.println("pressed.");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        isDragging = false;
        activeResizeTabBar = -1;
        getGlassPane().setCursor(Cursor.getDefaultCursor());
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
