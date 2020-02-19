package com.yunxin.mygui.share.component;

import javax.swing.*;

public class MRadioButton extends MIconComponentBase<JRadioButton> {

    @Override
    protected Class<? extends JComponent> provideComponent() {
        return JRadioButton.class;
    }
}
