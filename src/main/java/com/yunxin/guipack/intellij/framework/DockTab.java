package com.yunxin.guipack.intellij.framework;

import com.formdev.flatlaf.util.UIScale;
import com.yunxin.guipack.share.SwingUtils;
import com.yunxin.guipack.share.icon.CompoundIcon;
import com.yunxin.guipack.share.icon.RotatedIcon;
import com.yunxin.guipack.share.icon.TextIcon;
import org.jdesktop.swingx.JXImageView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

import static com.yunxin.guipack.intellij.framework.IntelliJPanel.MODE_SPLIT;

public class DockTab extends JToggleButton implements ItemListener, MouseMotionListener, MouseListener {



    DockTabBar mode;

    DockTabView view;

    int tabViewSize = 100;

    int splitLocation = 200;

    String text;
    Icon icon;



    public DockTab(String text, Icon icon, DockTabBar mode, DockTabView view) {
        this.text = text;
        this.icon = icon;
        this.mode = mode;
        this.view = view;
        setBackground(mode.getBackground());
        updateIcons();
        addItemListener(this);

        addMouseListener(this);
        addMouseMotionListener(this);
        view.putClientProperty("myController",this);
        setFont(new Font(Font.DIALOG,Font.PLAIN,12));

    }

    public void updateIcons(){
        TextIcon textIcon = new TextIcon(this,text);
        Icon ci = null;
        if(icon!=null){
            ci = new CompoundIcon(CompoundIcon.Axis.X_AXIS,5, icon,textIcon);
        }else{
            ci = new CompoundIcon(textIcon);
        }

        Icon ri;
        if(mode.tab.direction == IntelliJPanel.LEFT){
            ri = new RotatedIcon(ci, RotatedIcon.Rotate.UP);
            setBorder(new EmptyBorder(UIScale.scale(10),UIScale.scale(4),UIScale.scale(10),UIScale.scale(4)));
        }else if(mode.tab.direction == IntelliJPanel.RIGHT){
            ri = new RotatedIcon(ci, RotatedIcon.Rotate.DOWN);
            setBorder(new EmptyBorder(UIScale.scale(10),UIScale.scale(4),UIScale.scale(10),UIScale.scale(4)));
        }else{
            ri = new RotatedIcon(ci, RotatedIcon.Rotate.ABOUT_CENTER);
            setBorder(new EmptyBorder(UIScale.scale(4),UIScale.scale(10),UIScale.scale(4),UIScale.scale(10)));
        }
        super.setIcon(ri);
    }



    @Override
    public void itemStateChanged(ItemEvent e) {

        if(isSelected()){
            mode.setSelectedExcept(this,true);
            if(mode.mode == MODE_SPLIT){
                mode.tab.tabContent.setRightComponent(view);
            }else{
                mode.tab.tabContent.setLeftComponent(view);
            }
            if(tabViewSize >=0){
                if((mode.tab.direction== IntelliJPanel.LEFT)||mode.tab.direction== IntelliJPanel.RIGHT){
                    mode.tab.tabContent.setPreferredSize(new Dimension(tabViewSize,mode.tab.tabContent.getHeight()));
                }else{
                    mode.tab.tabContent.setPreferredSize(new Dimension(mode.tab.tabContent.getWidth(), tabViewSize));
                }
            }

            mode.tab.tabContent.setVisible(isSelected());
            mode.activeItem = this;

        }else{
            if(mode.mode == MODE_SPLIT){
                if(mode.tab.tabContent.getBottomComponent()==view){
                    mode.tab.tabContent.setBottomComponent(null);
                }else{

                }
            }else{
                if(mode.tab.tabContent.getTopComponent()==view){
                    mode.tab.tabContent.setTopComponent(null);
                }
            }
            boolean hasActive = false;
            for(Component c: mode.tabList){
                if(c instanceof DockTab){
                    if(((DockTab) c).isSelected()){
                        mode.activeItem = (DockTab) c;
                        hasActive = true;
                    }
                }
            }
            if(!hasActive){
                mode.activeItem = null;
            }

        }

        if(mode.tab.tabContent.getComponentCount()==3){
            mode.tab.tabContent.setVisible(true);
            mode.tab.tabContent.setDividerLocation(splitLocation);
            mode.tab.tabContent.setDividerSize(mode.tab.intelliJPanel.defaultTabDivederSize);
            if((mode.tab.direction== IntelliJPanel.LEFT)||(mode.tab.direction== IntelliJPanel.RIGHT)){
                ((MLineBorder)mode.tab.splitModeTabBar.activeItem.view.getBorder()).setInsets(false,false,true,false);
                ((MLineBorder)mode.tab.unSplitModeTabBar.activeItem.view.getBorder()).setInsets(false,false,false,true);
            }else{
                ((MLineBorder)mode.tab.splitModeTabBar.activeItem.view.getBorder()).setInsets(true,false,false,false);
                ((MLineBorder)mode.tab.unSplitModeTabBar.activeItem.view.getBorder()).setInsets(false,true,false,false);
            }
        }else{
            mode.tab.tabContent.setDividerSize(0);
            if(mode.tab.tabContent.getComponentCount()==1){
                mode.tab.tabContent.setVisible(false);
            }else if(mode.tab.tabContent.getComponentCount()==2){
                mode.tab.tabContent.setVisible(true);
            }
        }

        updateIcons();

        mode.tab.revalidate();
    }

    public Container getGlassPane(){
        return (Container) mode.tab.intelliJPanel.getGlassPane();
    }


    boolean isDragging = false;
    int inArea = -1;
    int inMode = -1;
    Point dragPointInGlassPane = null;

    DockTabBar dragToMode = null;


    DockTabBar tmpParentMode = null;
    int tmpParentIndex = -1;

    Point initPoint = null;

    JXImageView dragImageLabel = new JXImageView();
    JXImageView glueButton = new JXImageView();

    boolean addBefore = false;
    int tempIndex = 0;

    void getImageOfComponent(){

        dragImageLabel.setImage(SwingUtils.component2Image(this));
        dragImageLabel.setAlpha(0.4f);

    }

    @Override
    public synchronized void mouseDragged(MouseEvent e) {
        synchronized (getTreeLock()){
            if(!isDragging){
                isDragging = true;
                tmpParentMode = mode;
                tmpParentIndex = mode.getTabIndex(this);
                setVisible(false);
                initPoint = e.getPoint();
                dragImageLabel.setPreferredSize(getSize());
                dragImageLabel.setSize(getSize());
                int m = Math.min(getSize().width,getSize().height);
                glueButton.setPreferredSize(new Dimension(m,m));
                glueButton.setSize((new Dimension(m,m)));
                glueButton.setBackground(SwingUtils.darken(IntelliJPanel.defaultBorderColor,20));
                getImageOfComponent();
                getGlassPane().add(dragImageLabel);
                mode.tab.refresh();
                mode.tab.updateHoverSize();
            }

            if(isDragging){

                for(int i=0;i<4;i++){
                    mode.tab.intelliJPanel.tabBars[i].updateHoverSize();
                }
                dragPointInGlassPane = SwingUtilities.convertPoint(e.getComponent(),e.getX(),e.getY(),mode.tab.intelliJPanel.getGlassPane());
                dragImageLabel.setLocation(dragPointInGlassPane.x-initPoint.x,dragPointInGlassPane.y-initPoint.y);
                DockTabBar tmp =  getSplitMode(dragPointInGlassPane);

                if(tmp==null){

                    if(dragToMode!=null){
                        dragToMode.tab.unHighLight();
                        cleanGlueButton();
                        dragToMode.tab.updateUI();
                        dragToMode = tmp;
                        return;
                    }
                }
                if(tmp!=null){
                    dragToMode = tmp;

                    dragToMode.tab.highLight();
                    cleanGlueButton();

                    Component component = SwingUtilities.getDeepestComponentAt(mode.tab.intelliJPanel.getContentPane(),dragPointInGlassPane.x,dragPointInGlassPane.y);
                    if(component instanceof DockTab){
                        Point p = SwingUtilities.convertPoint(getGlassPane(),dragPointInGlassPane.x,dragPointInGlassPane.y,component);
                        tempIndex = dragToMode.getTabIndex((DockTab) component);
                        if((dragToMode.tab.direction== IntelliJPanel.LEFT)||(dragToMode.tab.direction== IntelliJPanel.RIGHT)){

                            if(p.y>(component.getHeight()/2)){
                                addBefore = false;
                                dragToMode.addTab(glueButton,tempIndex+1);
                            }else{
                                addBefore = true;
                                dragToMode.addTab(glueButton,tempIndex);
                            }
                        }else{
                            if(p.x>(component.getWidth()/2)){
                                addBefore = false;
                                dragToMode.addTab(glueButton,tempIndex+1);
                            }else{
                                addBefore = true;
                                dragToMode.addTab(glueButton,tempIndex);
                            }
                        }
                        dragToMode.tab.updateUI();
                    }else if(component instanceof DockTabBar){
                        dragToMode.addTab(glueButton);
                        dragToMode.tab.updateUI();

                    }else{
//                        System.out.println("else...");
                    }
                }

            }
        }


    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

        if(isDragging){
            if(dragToMode!=null){
                dragToMode.tab.unHighLight();
                boolean isSelected = isSelected();
                if(isSelected){
                    setSelected(false);
                }
                tmpParentMode.removeTab(this);
                if(addBefore){
                    try {
                        dragToMode.addTab(this, tempIndex);
                    }catch (Throwable t){
                        dragToMode.addTab(this);
                    }
                }else{
                    if(dragToMode.getComponentCount()==0){
                        dragToMode.addTab(this);
                    }else{
                        if((tempIndex+1)>=dragToMode.getComponentCount()){
                            dragToMode.addTab(this);
                        }else{
                            dragToMode.addTab(this, tempIndex + 1);
                        }
                    }
                }

                mode = dragToMode;
                updateIcons();
                mode.tab.refresh();
                mode.updateUI();
                setSelected(isSelected);
            }

            {
                setVisible(true);
            }
            try{
                cleanGlueButton();
            }catch (Throwable t){}
            try {
                mode.tab.intelliJPanel.clearHighLighter();
            }catch (Throwable t){}

            isDragging = false;

        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    void cleanGlueButton(){
        DockTabBar mm = null;
        if(tmpParentMode!=null){
            mm = tmpParentMode;
        }else if(dragToMode!=null){
            mm = tmpParentMode;
        }
        if(mm!=null){
            for(int i=0;i<4;i++){
                try {
                    mm.tab.intelliJPanel.tabBars[i].splitModeTabBar.removeTab(glueButton);
                }catch (Throwable t){}

                try {
                    mm.tab.intelliJPanel.tabBars[i].unSplitModeTabBar.removeTab(glueButton);
                }catch (Throwable t){}

            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) { }

    Dock getInArea(Point p){

        if(mode.tab.intelliJPanel.tabBars[IntelliJPanel.LEFT].highLightBound.contains(p)){
            return mode.tab.intelliJPanel.tabBars[IntelliJPanel.LEFT];
        }else if(mode.tab.intelliJPanel.tabBars[IntelliJPanel.RIGHT].highLightBound.contains(p)){
            return mode.tab.intelliJPanel.tabBars[IntelliJPanel.RIGHT];
        }else if(mode.tab.intelliJPanel.tabBars[IntelliJPanel.TOP].highLightBound.contains(p)){
            return mode.tab.intelliJPanel.tabBars[IntelliJPanel.TOP];
        }else if(mode.tab.intelliJPanel.tabBars[IntelliJPanel.BOTTOM].highLightBound.contains(p)){
            return mode.tab.intelliJPanel.tabBars[IntelliJPanel.BOTTOM];
        }else{
            return null;
        }


    }

    DockTabBar getSplitMode(Point p){
        for(int i=0;i<4;i++){
            if(mode.tab.intelliJPanel.tabBars[i].splitModeTabBar.boundInGlass.contains(p)){
                return mode.tab.intelliJPanel.tabBars[i].splitModeTabBar;
            }else if(mode.tab.intelliJPanel.tabBars[i].unSplitModeTabBar.boundInGlass.contains(p)){
                return mode.tab.intelliJPanel.tabBars[i].unSplitModeTabBar;
            }
        }
        return null;
    }


}