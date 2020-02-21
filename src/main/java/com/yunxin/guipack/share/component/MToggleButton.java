package com.yunxin.guipack.share.component;

import javax.swing.*;

public class MToggleButton extends MIconComponentBase<JToggleButton> {

    public MToggleButton() { }

    public MToggleButton(String text){
        setText(text);
    }

    public MToggleButton(String text, Icon icon){
        super.title = text;
        super.icon = icon;
        super.updateLabel();
    }

    @Override
    protected Class<? extends JComponent> provideComponent() {
        return JToggleButton.class;
    }
}
