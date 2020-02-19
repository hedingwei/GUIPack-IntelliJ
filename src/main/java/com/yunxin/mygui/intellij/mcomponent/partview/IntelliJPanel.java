package com.yunxin.mygui.intellij.mcomponent.partview;

import com.formdev.flatlaf.util.ColorFunctions;
import com.yunxin.mygui.share.component.BetterGlassPane;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXImagePanel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXRootPane;

import javax.swing.*;
import java.awt.*;

import static com.yunxin.mygui.intellij.mcomponent.partview.IJPanelTabBar.*;

public class IntelliJPanel extends JXRootPane{


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

        getContentPane().add(new JXPanel(),BorderLayout.CENTER);


        tabBars[LEFT].getMode(MODE_SPLIT).addTab("Favoriates",null,new SimplePartView("Project Split"));
        tabBars[LEFT].getMode(MODE_UNSPLIT).addTab("Project",null,new SimplePartView("LEFT UNSplit 1"));
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
//        com.formdev.flatlaf.FlatIntelliJLaf.install();
//        com.formdev.flatlaf.FlatDarculaLaf.install();
        com.formdev.flatlaf.FlatDarkLaf.install();
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


}
