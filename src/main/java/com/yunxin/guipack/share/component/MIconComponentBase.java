package com.yunxin.guipack.share.component;

import com.formdev.flatlaf.ui.FlatEmptyBorder;
import com.yunxin.guipack.share.icon.CompoundIcon;
import com.yunxin.guipack.share.icon.RotatedIcon;
import com.yunxin.guipack.share.icon.TextIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.lang.reflect.Method;

public abstract class MIconComponentBase<T> extends JComponent {


    private RotatedIcon.Rotate rotate = RotatedIcon.Rotate.ABOUT_CENTER;
     String title;
     Icon icon;
    protected JComponent component;
    protected Class componentClass;
    protected Method getIconMethod;
    protected Method getTextMethod;
    protected Method setIconMethod;
    protected Method setTextMethod;
    private EmptyBorder border;

    public T getComponent(){
        return (T) component;
    }

    public MIconComponentBase() {
        componentClass = provideComponent();
        try {
            component = (JComponent) componentClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            getIconMethod =componentClass.getMethod("getIcon");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        try {
            getTextMethod =componentClass.getMethod("getText");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        try {
            setIconMethod =componentClass.getMethod("setIcon",Icon.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        try {
            setTextMethod =componentClass.getMethod("setText",String.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        setLayout(new BorderLayout());
        add(component,BorderLayout.CENTER);
        setPadding(0,0,0,0);
        title="";
    }

    public void setPadding(int l, int t,int b,int r){

        border = new FlatEmptyBorder(t,l,b,r);
        setBorder(border);
    }

    public void setText(String title){
        this.title = title;
        updateLabel();
    }

    protected abstract Class<? extends JComponent> provideComponent();


    protected void updateLabel(){
        TextIcon textIcon = new TextIcon(this,this.title);
        CompoundIcon ci = null;
        Icon cc = icon;
        if(cc!=null){
            ci = new CompoundIcon(CompoundIcon.Axis.X_AXIS,5,cc,textIcon);
        }else{
            ci = new CompoundIcon(CompoundIcon.Axis.X_AXIS,5,textIcon);
        }
        RotatedIcon ri  = new RotatedIcon(ci, rotate);
        componentSetIcon(ri);
        revalidate();
    }

    private Icon getIcon(){

        if(getIconMethod!=null){
            try {
                return (Icon) getIconMethod.invoke(component);
            } catch (Throwable e) {
                return null;
            }
        }else{
            return null;
        }
    }

    private void componentSetIcon(Icon icon){
        if(setIconMethod!=null){
            try {
                setIconMethod.invoke(component,icon);
            } catch (Throwable t){

            }

        }else{

        }
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
        updateLabel();
    }

    public RotatedIcon.Rotate getRotate() {
        return rotate;
    }

    public void setRotate(RotatedIcon.Rotate rotate) {
        this.rotate = rotate;
        updateLabel();
    }

    public String getText() {
        return title;
    }





}
