package com.yunxin.mygui;

import com.yunxin.mygui.intellij.mcomponent.partview.IJPanel;
import com.yunxin.mygui.share.component.BetterGlassPane;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args){

//        UIManager.put("JideSplitPane.dividerSize",0);
//        com.formdev.flatlaf.FlatLightLaf.install();
            com.formdev.flatlaf.FlatIntelliJLaf.install();
//        com.formdev.flatlaf.FlatDarculaLaf.install();
//        com.formdev.flatlaf.FlatDarkLaf.install();
//        com.formdev.flatlaf.ui.FlatToggleButtonUI


//        UIManager.put("SplitPane.background",new Color(20,20,20));

//        UIManager.put("SplitPaneDivider.border",new MLineBorder(Color.lightGray,1,true,true,false,false));
//        UIManager.put("ToggleButton.arc",0);
        UIManager.put("Button.arc",0);
        UIManager.put("TabbedPane.tabHeight",23);
        UIManager.put("TabbedPane.tabSelectionHeight",0);
        UIManager.put("TabbedPane.tabsOverlapBorder",true);
        UIManager.put("TabbedPane.contentSeparatorHeight",1);
        UIManager.put("TabbedPane.hasFullBorder",false);
        UIManager.put("ToolBar.buttonMargins",new Insets(0,0,0,0));


        UIManager.put("Button.iconTextGap",4);

        System.out.println("--------------");
        for(Object key: UIManager.getLookAndFeelDefaults().keySet())
        {
            System.out.println(key+"=>"+UIManager.getLookAndFeelDefaults().get(key));
        }

        System.out.println("default=========");

//        UIManager.put("Component.arc",0);

//        WebLookAndFeel.install ();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Test");
                new BetterGlassPane(frame.getRootPane());
                frame.getContentPane().setLayout(new BorderLayout());
                frame.getContentPane().add(new IJPanel());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                frame.setPreferredSize(new Dimension(1024,900));
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });


    }
}
