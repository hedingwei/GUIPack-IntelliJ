package com.yunxin.guipack.intellij.framework;

import javax.swing.*;

public class DockToolBar extends JToolBar{

    Dock tabBar;
    public DockToolBar(Dock tab) {
        this.tabBar = tab;
        setFloatable(false);
        setFont(IntelliJPanel.defaultFont);
    }


}
