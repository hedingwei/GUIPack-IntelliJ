package com.yunxin.gui.intellij.framework;

import javax.swing.*;

public class IJPanelTabToolBar extends JToolBar{

    IJPanelTabBar tabBar;
    public IJPanelTabToolBar(IJPanelTabBar tab) {
        this.tabBar = tab;
        setFloatable(false);
        setFont(IntelliJPanel.defaultFont);
    }


}
