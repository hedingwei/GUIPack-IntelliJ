package com.yunxin.mygui.intellij.mcomponent.partview;

import javax.swing.*;

public class IJPanelTabToolBar extends JToolBar{

    IJPanelTabBar tabBar;
    public IJPanelTabToolBar(IJPanelTabBar tab) {
        this.tabBar = tab;
        setFloatable(false);
//        setDoubleBuffered(true);
        setFont(IntelliJPanel.defaultFont);
    }


}
