package com.yunxin.mygui.share.component;

import javax.swing.*;

public class MCheckBox extends MIconComponentBase<JCheckBox> {

    @Override
    protected Class<? extends JComponent> provideComponent() {
        return JCheckBox.class;
    }
}
