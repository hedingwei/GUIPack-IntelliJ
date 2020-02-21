package com.yunxin.mygui.share;

import com.yunxin.guipack.share.layout.HorizontalLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

public class LayoutTest {
    public static void main(String[] args) {
        com.formdev.flatlaf.FlatIntelliJLaf.install();
        UIManager.put("Button.arc",0);
        UIManager.put("TabbedPane.tabHeight",23);
        UIManager.put("TabbedPane.tabSelectionHeight",0);
        UIManager.put("TabbedPane.tabsOverlapBorder",true);
        UIManager.put("TabbedPane.contentSeparatorHeight",1);
        UIManager.put("TabbedPane.hasFullBorder",false);
        UIManager.put("ToolBar.buttonMargins",new Insets(0,0,0,0));
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {

                    JFrame frame = new JFrame("Test Layout");
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setPreferredSize(new Dimension(1024,768));
                    frame.getContentPane().setLayout(new BorderLayout());

                    JPanel toolbarLeft = new JPanel(){{
                        setLayout(new HorizontalLayout(0,true));
                        setBackground(Color.DARK_GRAY);
                    }};
                    JPanel toolbarRight = new JPanel(){{setLayout(new HorizontalLayout(0,false));
                        setBackground(Color.GREEN);
                    }};

                    JPanel panel = new JPanel(){{setLayout(new GridLayout(1,2));}};
                    frame.getContentPane().add(panel,BorderLayout.NORTH);
                    panel.add(toolbarLeft);
                    panel.add(toolbarRight);

                    for(int i=0;i<10;i++){
                        JButton lb = new JButton("Left Button "+i){{setBorder(new EmptyBorder(0,0,0,0));}};
                        JButton rb = new JButton("Right Button "+i){{setBorder(new EmptyBorder(0,0,0,0));}};
                        toolbarLeft.add(lb);
                        toolbarRight.add(rb);
                    }

                    frame.pack();
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);



                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
