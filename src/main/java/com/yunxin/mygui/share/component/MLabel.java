package com.yunxin.mygui.share.component;

import javax.swing.*;

public class MLabel extends MIconComponentBase<JLabel> {

    public MLabel() {
        setPadding(4,0,0,4);
    }

    @Override
    protected Class<? extends JComponent> provideComponent() {
        return JLabel.class;
    }
}
