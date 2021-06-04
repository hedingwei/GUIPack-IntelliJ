package com.yunxin.guipack.intellij.framework;

import com.yunxin.guipack.share.component.MLabel;

import javax.swing.*;
import java.awt.*;

public class SimpleDockTabView extends DockTabView {

    protected JToolBar toolBar ;
    protected JToolBar trailingBar;
    protected MLabel titleLabel ;
    protected JPanel headerPanel;

    public SimpleDockTabView() {
    }

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

        trailingBar = new JToolBar();
        trailingBar.setRollover(true);
        trailingBar.setFloatable(false);

        headerPanel = new JPanel();

    }

    @Override
    protected void setupTitleLeadingPart(JToolBar toolBar) {

        headerPanel.setLayout(new BorderLayout());

        headerPanel.add(toolBar,BorderLayout.WEST);
        headerPanel.add(trailingBar, BorderLayout.CENTER);
        add(headerPanel,BorderLayout.NORTH);
        toolBar.add(titleLabel);
        headerPanel.setBorder(new MLineBorder(IntelliJPanel.defaultBorderColor,1,false,false,false,true));

    }

    @Override
    protected void setupTitleTrailingPart(JToolBar toolBar) {


    }

    @Override
    protected JToolBar buildTitleTrailingPart() {
        return trailingBar;
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
