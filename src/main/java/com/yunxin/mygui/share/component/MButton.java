package com.yunxin.mygui.share.component;

import javax.swing.*;

public class MButton extends MIconComponentBase<JButton> {

    @Override
    protected Class<? extends JComponent> provideComponent() {
        return JButton.class;
    }
}
