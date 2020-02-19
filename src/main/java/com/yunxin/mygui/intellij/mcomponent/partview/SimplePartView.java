package com.yunxin.mygui.intellij.mcomponent.partview;

import com.yunxin.mygui.share.component.MLabel;

import javax.swing.*;
import java.awt.*;

public class SimplePartView extends AbstractPartView {

    private JToolBar toolBar ;
    private JToolBar trailingBar;
    private MLabel titleLabel ;

    public SimplePartView(String title) {
        this.titleLabel.setText(title);
    }

    public SimplePartView(String title, Icon icon) {
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

    @Override
    public void setupViewController(IPartViewController controller) {

    }

    public void setTitleIcon(Icon icon){
        this.titleLabel.setIcon(icon);
    }
}
