package com.yunxin.gui.intellij.framework;

import org.jdesktop.swingx.JXPanel;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractPartView extends JXPanel{
    private JPanel contentHolder = null;
    public AbstractPartView() {
        setLayout(new BorderLayout());
        preInit();
        initComponents();
    }


    protected abstract void preInit();

    private void initComponents() {
        contentHolder = new JPanel();
        contentHolder.setLayout(new BorderLayout());
        add(contentHolder,BorderLayout.CENTER);
        initTitleBar();
        setBorder(new MLineBorder(IntelliJPanel.defaultBorderColor,1,false,false,false,false));
    }

    protected void initTitleBar() {
        setupTitleLeadingPart(buildTitleLeadingPart());
        setupTitleTrailingPart(buildTitleTrailingPart());
    }

    protected abstract void setupTitleLeadingPart(JToolBar toolBar);

    protected abstract void setupTitleTrailingPart(JToolBar toolBar) ;

    protected abstract JToolBar buildTitleTrailingPart();

    protected abstract JToolBar buildTitleLeadingPart();

    public void setPartContent(Component content){
        contentHolder.removeAll();
        contentHolder.add(content,BorderLayout.CENTER);
    }
}
