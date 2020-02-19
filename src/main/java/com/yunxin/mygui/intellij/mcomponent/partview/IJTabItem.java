package com.yunxin.mygui.intellij.mcomponent.partview;

import com.formdev.flatlaf.util.UIScale;
import com.yunxin.mygui.share.component.MToggleButton;
import com.yunxin.mygui.share.icon.ColoredIcon;
import com.yunxin.mygui.share.icon.CompoundIcon;
import com.yunxin.mygui.share.icon.RotatedIcon;
import com.yunxin.mygui.share.icon.TextIcon;
import org.jdesktop.swingx.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import static com.yunxin.mygui.intellij.mcomponent.partview.IJPanel.MODE_UNSPLIT;
import static com.yunxin.mygui.intellij.mcomponent.partview.IJPanelTabBar.MODE_SPLIT;

public class IJTabItem extends JToggleButton implements ItemListener, MouseMotionListener, MouseListener {

    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int BOTTOM = 2;
    public static final int TOP = 3;

    IJPanelTabMode mode;

    AbstractPartView view;

    Dimension partSize = null;

    int splitLocation = 200;

    String text;
    Icon icon;



    public IJTabItem(String text, Icon icon, IJPanelTabMode mode, AbstractPartView view) {
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
        if(mode.tab.direction== LEFT){
            ri = new RotatedIcon(ci, RotatedIcon.Rotate.UP);
            setBorder(new EmptyBorder(UIScale.scale(10),UIScale.scale(4),UIScale.scale(10),UIScale.scale(4)));
        }else if(mode.tab.direction == RIGHT){
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
            if(partSize!=null){
                mode.tab.tabContent.setPreferredSize(partSize);
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
                if(c instanceof IJTabItem){
                    if(((IJTabItem) c).isSelected()){
                        mode.activeItem = (IJTabItem) c;
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
            if((mode.tab.direction==IJPanelTabBar.LEFT)||(mode.tab.direction==IJPanelTabBar.RIGHT)){
                ((MLineBorder)mode.tab.splitMode.activeItem.view.getBorder()).setInsets(false,false,true,false);
                ((MLineBorder)mode.tab.unSplitMode.activeItem.view.getBorder()).setInsets(false,false,false,true);
            }else{
                ((MLineBorder)mode.tab.splitMode.activeItem.view.getBorder()).setInsets(true,false,false,false);
                ((MLineBorder)mode.tab.unSplitMode.activeItem.view.getBorder()).setInsets(false,true,false,false);
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

    IJPanelTabMode dragToMode = null;


    IJPanelTabMode tmpParentMode = null;
    int tmpParentIndex = -1;

    Point initPoint = null;

    JXImagePanel dragImageLabel = new JXImagePanel();
    JXImagePanel glueButton = new JXImagePanel();

    boolean addBefore = false;
    int tempIndex = 0;

    void getImageOfComponent(){
        BufferedImage bufferedImage = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Map map = (Map)(toolkit.getDesktopProperty("awt.font.desktophints"));

        if (map != null)
        {
            g2d.addRenderingHints(map);
        }
        else
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
        paint(g2d);

        dragImageLabel.setImage(bufferedImage);
        dragImageLabel.setAlpha(0.4f);



    }

    @Override
    public void mouseDragged(MouseEvent e) {
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
        }

        if(isDragging){

            dragPointInGlassPane = SwingUtilities.convertPoint(e.getComponent(),e.getX(),e.getY(),mode.tab.intelliJPanel.getGlassPane());
            dragImageLabel.setLocation(dragPointInGlassPane.x-initPoint.x,dragPointInGlassPane.y-initPoint.y);


            mode.tab.intelliJPanel.updateHighLightBounds();
            IJPanelTabMode tmp =  getSplitMode(dragPointInGlassPane);
            mode.tab.intelliJPanel.updateHighLightBounds();
            if(tmp==null){

                if(dragToMode==null){
                }else{
                    dragToMode.tab.unHighLight();
                    cleanGlueButton();
                    dragToMode.tab.updateUI();
                }
            }else{
                dragToMode = tmp;

                dragToMode.tab.highLight();
                cleanGlueButton();
                Component component = SwingUtilities.getDeepestComponentAt(mode.tab.intelliJPanel.getContentPane(),dragPointInGlassPane.x,dragPointInGlassPane.y);
                if(component instanceof IJTabItem){
                    Point p = SwingUtilities.convertPoint(getGlassPane(),dragPointInGlassPane.x,dragPointInGlassPane.y,component);
                    tempIndex = dragToMode.getTabIndex((IJTabItem) component);
                    if((dragToMode.tab.direction==IJPanelTabBar.LEFT)||(dragToMode.tab.direction==IJPanelTabBar.RIGHT)){

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
                }else if(component instanceof IJPanelTabMode){
                    dragToMode.addTab(glueButton);
                    dragToMode.tab.updateUI();

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
        IJPanelTabMode mm = null;
        if(tmpParentMode!=null){
            mm = tmpParentMode;
        }else if(dragToMode!=null){
            mm = tmpParentMode;
        }
        if(mm!=null){
            for(int i=0;i<4;i++){
                try {
                    mm.tab.intelliJPanel.tabBars[i].splitMode.removeTab(glueButton);
                }catch (Throwable t){}

                try {
                    mm.tab.intelliJPanel.tabBars[i].unSplitMode.removeTab(glueButton);
                }catch (Throwable t){}

            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    IJPanelTabBar getInArea(Point p){

        if(mode.tab.intelliJPanel.tabBars[LEFT].highLightBound.contains(p)){
            return mode.tab.intelliJPanel.tabBars[LEFT];
        }else if(mode.tab.intelliJPanel.tabBars[RIGHT].highLightBound.contains(p)){
            return mode.tab.intelliJPanel.tabBars[RIGHT];
        }else if(mode.tab.intelliJPanel.tabBars[TOP].highLightBound.contains(p)){
            return mode.tab.intelliJPanel.tabBars[TOP];
        }else if(mode.tab.intelliJPanel.tabBars[BOTTOM].highLightBound.contains(p)){
            return mode.tab.intelliJPanel.tabBars[BOTTOM];
        }else{
            return null;
        }


    }

    IJPanelTabMode getSplitMode(Point p){
        for(int i=0;i<4;i++){
            if(mode.tab.intelliJPanel.tabBars[i].splitMode.boundInGlass.contains(p)){
                return mode.tab.intelliJPanel.tabBars[i].splitMode;
            }else if(mode.tab.intelliJPanel.tabBars[i].unSplitMode.boundInGlass.contains(p)){
                return mode.tab.intelliJPanel.tabBars[i].unSplitMode;
            }
        }

        return null;
    }
}