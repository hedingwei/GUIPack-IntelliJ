package com.yunxin.mygui.intellij;

import com.formdev.flatlaf.icons.*;
import com.yunxin.guipack.intellij.framework.IntelliJPanel;
import com.yunxin.guipack.intellij.framework.SimpleDockTabView;
import org.jdesktop.swingx.JXEditorPane;
import org.jdesktop.swingx.JXFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import static com.yunxin.guipack.intellij.framework.IntelliJPanel.Direction.*;
import static com.yunxin.guipack.intellij.framework.IntelliJPanel.SplitMode.Split;
import static com.yunxin.guipack.intellij.framework.IntelliJPanel.SplitMode.UnSplit;

public class Demo {
    public static void main(String[] args){


        com.formdev.flatlaf.FlatDarkLaf.install();

        UIManager.put("Button.iconTextGap",4);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                IntelliJPanel intelliJPanel = new IntelliJPanel();
                intelliJPanel
                        .getToolBar(IntelliJPanel.Direction.Bottom)
                        .add(new JToggleButton(
                                new com.formdev.flatlaf.icons.FlatFileViewFloppyDriveIcon()){
                                    {
                                        setSelected(true);
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
                                }
                        );

                intelliJPanel.addTab(Left, UnSplit,"Project",new FlatFileViewComputerIcon(),new SimpleDockTabView("Project"){{setPartContent(new JXEditorPane());}});
                intelliJPanel.addTab(Left,Split,"Structure", new FlatInternalFrameIconifyIcon(), new SimpleDockTabView("Structure"));

                intelliJPanel.addTab(Right, UnSplit,"Maven Project",new FlatFileViewFloppyDriveIcon(),new SimpleDockTabView("Maven Project"));
                intelliJPanel.addTab(Right,Split,"Database", new FlatFileChooserListViewIcon(), new SimpleDockTabView("Database"));

                intelliJPanel.addTab(Top, UnSplit,"Home",new FlatFileChooserHomeFolderIcon(),new SimpleDockTabView("Home"));
                intelliJPanel.addTab(Top,Split,"Help", new FlatHelpButtonIcon(), new SimpleDockTabView("Help"));

                intelliJPanel.addTab(Bottom, UnSplit,"Run",new FlatTreeCollapsedIcon(),new SimpleDockTabView("Run"){{setPartContent(new JXEditorPane());}});
                intelliJPanel.addTab(Bottom,Split,"Console", new FlatHelpButtonIcon(), new SimpleDockTabView("Console"));


                JXFrame frame = new JXFrame("IntelliJ GUI Demo");
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

}
