package com.yunxin.guipack.intellij.framework;

import com.yunxin.guipack.share.component.MLabel;

import javax.swing.*;
import java.awt.*;

public class SimpleDockTabView extends DockTabView {

    private JToolBar toolBar ;
    private JToolBar trailingBar;
    private MLabel titleLabel ;

    public SimpleDockTabView(String title) {
        this.titleLabel.setText(title);
    }

    public SimpleDockTabView(String title, Icon icon) {
        this.titleLabel.setText(title);
        this.titleLabel.setIcon(icon);

    }

    public void preInit(){
        titleLabel = new MLabel();
        titleLabel.setFont(IntelliJPanel.defaultFont);
        titleLabel.setPadding(4,2,2,4);
        toolBar = new JToolBar();
        toolBar.setRollover(true);
        toolBar.setFloatable(false);

    }

    @Override
    protected void setupTitleLeadingPart(JToolBar toolBar) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        panel.add(toolBar,BorderLayout.WEST);
        add(panel,BorderLayout.NORTH);
        toolBar.add(titleLabel);
        panel.setBorder(new MLineBorder(IntelliJPanel.defaultBorderColor,1,false,false,false,true));

    }

    @Override
    protected void setupTitleTrailingPart(JToolBar toolBar) {


    }

    @Override
    protected JToolBar buildTitleTrailingPart() {
        return toolBar;
    }

    @Override
    protected JToolBar buildTitleLeadingPart() {
        return toolBar;
    }

    public void setTitle(String title){
        this.titleLabel.setText(title);
    }



    public void setTitleIcon(Icon icon){
        this.titleLabel.setIcon(icon);
    }
}
