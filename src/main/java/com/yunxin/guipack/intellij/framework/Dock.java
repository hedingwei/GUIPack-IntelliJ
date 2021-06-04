package com.yunxin.guipack.intellij.framework;

import com.yunxin.guipack.share.SwingUtils;
import com.yunxin.guipack.share.layout.VerticalLayout;
import com.yunxin.guipack.share.layout.HorizontalLayout;
import org.jdesktop.swingx.JXImageView;
import org.jdesktop.swingx.JXPanel;

import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.HierarchyBoundsAdapter;
import java.awt.event.HierarchyEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import static com.yunxin.guipack.intellij.framework.IntelliJPanel.*;

public class Dock extends JXPanel implements LayoutManager {




    DockToolBar toolBar;
    DockTabBar splitModeTabBar;
    DockTabBar unSplitModeTabBar;

    JSplitPane tabContent;

    JXPanel toolBarArea;
    JXPanel tabBarArea;
    IntelliJPanel intelliJPanel;

    int direction = 0;
    int gap = 0;

    int defaultModeAreaSize = 20;
    Rectangle highLightBound = null;


    Color highLightColor = null;

    Color storeHighLightColor = null;


    boolean isHighLight = false;



    PropertyChangeListener pcl;

    JXImageView highLightPanel_split;
    JXImageView highLightPanel_unsplit;

    public Dock(IntelliJPanel intelliJPanel, int direction){

        if(getBackground().getRed()<80){
            highLightColor = SwingUtils.darken(getBackground(),-getBackground().getRed());
        }else{
            highLightColor = SwingUtils.darken(getBackground(),getBackground().getRed());
        }


        this.intelliJPanel = intelliJPanel;
        this.direction = direction;
        setLayout(new BorderLayout());
        initComponents();

        pcl = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {

                JSplitPane splitPane = (JSplitPane) evt.getSource();

                if((splitPane.getRightComponent()==null)||(splitPane.getLeftComponent()==null)){
                    return;
                }

                Object o = ((JComponent)splitPane.getLeftComponent()).getClientProperty("myController");
                if(o!=null){
                    ((DockTab)o).splitLocation = (splitPane.getDividerLocation());
                }
                o = ((JComponent)splitPane.getRightComponent()).getClientProperty("myController");
                if(o!=null){
                    ((DockTab)o).splitLocation= (splitPane.getDividerLocation());
                }
            }
        };

        tabContent.addPropertyChangeListener("dividerLocation",pcl);
        tabContent.setDividerLocation(0.5);
        tabContent.setDividerSize(intelliJPanel.defaultTabDivederSize);

        tabContent.addHierarchyBoundsListener(new HierarchyBoundsAdapter() {
            @Override
            public void ancestorResized(HierarchyEvent e) {
                if(!(e.getChangedParent() instanceof IntelliJPanel)){
                    return;
                }
                refresh();

            }
        });


        updateHoverSize();

    }


    private void initComponents() {
        toolBarArea = new JXPanel();

        toolBar = new DockToolBar(this);
        splitModeTabBar = new DockTabBar(this,MODE_SPLIT);
        unSplitModeTabBar = new DockTabBar(this,MODE_UNSPLIT);

        tabBarArea = new JXPanel();
        tabContent = new JSplitPane();
        tabContent.setVisible(false);
        tabContent.setLeftComponent(null);
        tabContent.setRightComponent(null);
        tabContent.setContinuousLayout(true);
        tabContent.setDividerSize(8);

        highLightPanel_split = new JXImageView();
        highLightPanel_unsplit = new JXImageView();



        if(direction == LEFT){

            tabBarArea.setLayout(new GridLayout(2,1));
            toolBarArea.setLayout(new HorizontalLayout(0,true));
            toolBarArea.setBorder(new MLineBorder(intelliJPanel.defaultBorderColor,1,false,true,false,false));

            toolBar.setOrientation(SwingConstants.VERTICAL);
            toolBar.setBorder(new MLineBorder(intelliJPanel.defaultBorderColor,1,false,true,false,false));

            add(toolBarArea,BorderLayout.WEST);
            add(tabContent,BorderLayout.CENTER);

            tabContent.setBorder(new MLineBorder(intelliJPanel.defaultBorderColor,1,false,true,false,false));
            tabContent.setOrientation(JSplitPane.VERTICAL_SPLIT);

            tabBarArea.add(unSplitModeTabBar);
            tabBarArea.add(splitModeTabBar);
            toolBarArea.add(toolBar);
            toolBarArea.add(tabBarArea);
        }else if(direction == RIGHT){

            tabBarArea.setLayout(new GridLayout(2,1));
            toolBarArea.setLayout(new HorizontalLayout(0,false));
            toolBarArea.setBorder(new MLineBorder(intelliJPanel.defaultBorderColor,1,true,false,false,false));

            toolBar.setOrientation(SwingConstants.VERTICAL);
            toolBar.setBorder(new MLineBorder(intelliJPanel.defaultBorderColor,1,true,false,false,false));

            add(toolBarArea,BorderLayout.EAST);
            add(tabContent,BorderLayout.CENTER);

            tabContent.setOrientation(JSplitPane.VERTICAL_SPLIT);
            tabContent.setBorder(new MLineBorder(intelliJPanel.defaultBorderColor,1,true,false,false,false));

            tabBarArea.add(unSplitModeTabBar);
            tabBarArea.add(splitModeTabBar);
            toolBarArea.add(toolBar);
            toolBarArea.add(tabBarArea);
        }else if(direction == TOP){

            tabBarArea.setLayout(new GridLayout(1,2));
            toolBarArea.setLayout(new VerticalLayout(0,true));

            toolBarArea.setBorder(new MLineBorder(intelliJPanel.defaultBorderColor,1,false,false,false,true));
            toolBar.setBorder(new MLineBorder(intelliJPanel.defaultBorderColor,1,false,false,false,true));
            tabContent.setBorder(new MLineBorder(intelliJPanel.defaultBorderColor,1,false,false,false,true));

            add(toolBarArea,BorderLayout.NORTH);
            add(tabContent,BorderLayout.CENTER);

            tabContent.setOrientation(JSplitPane.HORIZONTAL_SPLIT);

            tabBarArea.add(unSplitModeTabBar);
            tabBarArea.add(splitModeTabBar);
            toolBarArea.add(toolBar);
            toolBarArea.add(tabBarArea);
        }else if(direction == BOTTOM){
            tabBarArea.setLayout(new GridLayout(1,2));

            toolBarArea.setLayout(new VerticalLayout(0,false));
            toolBarArea.setBorder(new MLineBorder(intelliJPanel.defaultBorderColor,1,false,false,true,false));
            toolBar.setBorder(new MLineBorder(intelliJPanel.defaultBorderColor,1,false,false,true,false));

            tabContent.setBorder(new MLineBorder(intelliJPanel.defaultBorderColor,1,false,false,true,false));

            add(toolBarArea,BorderLayout.SOUTH);
            add(tabContent,BorderLayout.CENTER);

            tabBarArea.add(unSplitModeTabBar);
            tabBarArea.add(splitModeTabBar);
            toolBarArea.add(toolBar);
            toolBarArea.add(tabBarArea);
        }


    }


    public int getVisibleTabCount(){
        return splitModeTabBar.getVisibleTabCount()+ unSplitModeTabBar.getVisibleTabCount();
    }

    public boolean isActive(){
        return tabContent.isVisible();
    }

    public Cursor getSliderCursor(){
        if((direction == LEFT)||(direction == RIGHT)){
            return Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR);
        }else{
            return Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR);
        }
    }

    int sliderSize = 5;
    public Rectangle getSliderBound(){
        if(direction==LEFT){
            Rectangle r = getBounds();
            r.setBounds(r.width-sliderSize/2,r.y,3*sliderSize,r.height);
            return r;

        }else if(direction == RIGHT){
            Rectangle r = getBounds();
            r.setBounds(r.x-sliderSize/2,r.y,3*sliderSize,r.height);
            return r;
        }else if(direction==TOP){
            Rectangle r = getBounds();
            r.setBounds(r.x,r.y+r.height-sliderSize,r.width,2*sliderSize);
            return r;
        }else if(direction==BOTTOM){
            Rectangle r = getBounds();
            r.setBounds(r.x,r.y-sliderSize,r.width,2*sliderSize);
            return r;
        }
        return null;
    }

    public void refresh(){

        if(getVisibleTabCount()==0){
            tabBarArea.setVisible(false);
            tabContent.setVisible(false);
            ((MLineBorder) toolBarArea.getBorder()).setInsets(false,false,false,false);
            ((MLineBorder) toolBarArea.getBorder()).setInsets(false,false,false,false);
        }else{
            tabBarArea.setVisible(true);
            if((splitModeTabBar.activeItem==null)&&(unSplitModeTabBar.activeItem==null)){
                tabContent.setVisible(false);
            }else{
                tabContent.setVisible(true);
            }

            if(direction==LEFT){
                ((MLineBorder) toolBarArea.getBorder()).setInsets(false,true,false,false);
                ((MLineBorder) toolBar.getBorder()).setInsets(false,true,false,false);
            }else if(direction==RIGHT){
                ((MLineBorder) toolBarArea.getBorder()).setInsets(true,false,false,false);
                ((MLineBorder) toolBar.getBorder()).setInsets(true,false,false,false);
            }else if(direction==BOTTOM){
                ((MLineBorder) toolBarArea.getBorder()).setInsets(false,false,true,false);
                ((MLineBorder) toolBar.getBorder()).setInsets(false,false,true,false);
            }else if(direction==TOP){
                ((MLineBorder) toolBarArea.getBorder()).setInsets(false,false,false,true);
                ((MLineBorder) toolBar.getBorder()).setInsets(false,false,false,true);
            }

        }

        if(toolBar.getComponentCount()==0){
            toolBar.setVisible(false);
        }else{
            toolBar.setVisible(true);
        }


        splitModeTabBar.boundInGlass = SwingUtilities.convertRectangle(splitModeTabBar, splitModeTabBar.getBounds(),intelliJPanel.getGlassPane());
        unSplitModeTabBar.boundInGlass = SwingUtilities.convertRectangle(unSplitModeTabBar, unSplitModeTabBar.getBounds(),intelliJPanel.getGlassPane());

        updateHoverSize();
    }


    public DockTabBar getMode(int splitMode){
        if(splitMode==MODE_SPLIT){
            return this.splitModeTabBar;
        }else if(splitMode == MODE_UNSPLIT){
            return unSplitModeTabBar;
        }else{
            throw new UnsupportedOperationException("UnSupported MODE");
        }
    }

    public DockToolBar getToolBar() {
        return toolBar;
    }


    @Override
    public void addLayoutComponent(String name, Component comp) {

    }

    @Override
    public void removeLayoutComponent(Component comp) {

    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        Insets insets = parent.getInsets();
        Dimension pref = new Dimension(0, 0);

        for (int i = 0, c = parent.getComponentCount(); i < c; i++) {
            Component m = parent.getComponent(i);
            if (m.isVisible()) {
                Dimension componentPreferredSize =
                        parent.getComponent(i).getPreferredSize();
                pref.height += componentPreferredSize.height + gap;
                pref.width = Math.max(pref.width, componentPreferredSize.width);
            }
        }

        pref.width += insets.left + insets.right;
        pref.height += insets.top + insets.bottom;

        return pref;
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return null;
    }

    @Override
    public void layoutContainer(Container parent) {

    }


    public void highLight(){
        if(!isHighLight){
            isHighLight = true;


            BufferedImage bufferedImage1 = new BufferedImage(Math.max(splitModeTabBar.boundInGlass.width,20),Math.max(splitModeTabBar.boundInGlass.height,20),BufferedImage.TYPE_INT_ARGB);
            BufferedImage bufferedImage2 = new BufferedImage(Math.max(splitModeTabBar.boundInGlass.width,20),Math.max(splitModeTabBar.boundInGlass.height,20),BufferedImage.TYPE_INT_ARGB);
            Color color = highLightColor;
            {
                Graphics2D graphics2D =  bufferedImage1.createGraphics();
                graphics2D.setColor(color);
                graphics2D.fillRect(0,0,bufferedImage1.getWidth(),bufferedImage1.getHeight());
                graphics2D.dispose();
            }

            {
                Graphics2D graphics2D =  bufferedImage2.createGraphics();
                graphics2D.setColor(color);
                graphics2D.fillRect(0,0,bufferedImage2.getWidth(),bufferedImage2.getHeight());
                graphics2D.dispose();
            }




            highLightPanel_split.setBounds(splitModeTabBar.boundInGlass);
            highLightPanel_unsplit.setBounds(unSplitModeTabBar.boundInGlass);

            highLightPanel_split.setImage(bufferedImage1);
            highLightPanel_unsplit.setImage(bufferedImage2);


            ((JXPanel)intelliJPanel.getGlassPane()).add(highLightPanel_split);
            ((JXPanel)intelliJPanel.getGlassPane()).add(highLightPanel_unsplit);
            highLightPanel_split.setAlpha(0.3f);
            highLightPanel_unsplit.setAlpha(0.3f);
            ((JXPanel)intelliJPanel.getGlassPane()).updateUI();

        }
    }

    public void unHighLight(){
        if(isHighLight){
            isHighLight = false;
            try{
                ((JXPanel)intelliJPanel.getGlassPane()).remove(highLightPanel_split);
            }catch (Throwable t){}
            try{
                ((JXPanel)intelliJPanel.getGlassPane()).remove(highLightPanel_unsplit);
            }catch (Throwable t){}
            ((JXPanel)intelliJPanel.getGlassPane()).updateUI();
        }
    }

    void updateHoverSize(){

        if((splitModeTabBar.getComponentCount()+ unSplitModeTabBar.getComponentCount())>0){
            splitModeTabBar.boundInGlass = SwingUtilities.convertRectangle(tabBarArea, splitModeTabBar.getBounds(),intelliJPanel.getGlassPane());
            unSplitModeTabBar.boundInGlass = SwingUtilities.convertRectangle(tabBarArea, unSplitModeTabBar.getBounds(),intelliJPanel.getGlassPane());

        }else{

            if((direction==LEFT)){
                unSplitModeTabBar.boundInGlass = new Rectangle(getX()+ toolBar.getX()+ toolBar.getWidth(),getY()+ toolBar.getY(),defaultModeAreaSize,getHeight()/2);
                splitModeTabBar.boundInGlass = new Rectangle(getX()+ toolBar.getX()+ toolBar.getWidth(),getY()+ toolBar.getY()+getHeight()/2,defaultModeAreaSize,getHeight()/2);

            }else if(direction==RIGHT){
                unSplitModeTabBar.boundInGlass = new Rectangle(getX()+ toolBar.getX()-defaultModeAreaSize,getY()+ toolBar.getY(),defaultModeAreaSize,getHeight()/2);
                splitModeTabBar.boundInGlass = new Rectangle(getX()+ toolBar.getX()-defaultModeAreaSize,getY()+ toolBar.getY()+getHeight()/2,defaultModeAreaSize,getHeight()/2);

            }
            else if((direction==TOP)){
                unSplitModeTabBar.boundInGlass = new Rectangle(getX()+ toolBar.getX(),getY()+ toolBar.getY()+ toolBar.getHeight(),getWidth()/2,defaultModeAreaSize);
                splitModeTabBar.boundInGlass = new Rectangle(getX()+ toolBar.getX()+getWidth()/2,getY()+ toolBar.getY()+ toolBar.getHeight(),getWidth()/2,defaultModeAreaSize);

            }else if(direction==BOTTOM){
                unSplitModeTabBar.boundInGlass = new Rectangle(getX()+ toolBar.getX(),getY()+getHeight()- toolBar.getHeight()-defaultModeAreaSize,getWidth()/2,defaultModeAreaSize);
                splitModeTabBar.boundInGlass = new Rectangle(toolBar.getX()+getWidth()/2,getY()+getHeight()- toolBar.getHeight()-defaultModeAreaSize,getWidth()/2,defaultModeAreaSize);
            }
        }




    }

    public java.util.List<DockTab> getActiveTabs(){
        java.util.List<DockTab> list = new ArrayList<>();
        for(JComponent c: splitModeTabBar.tabList){
            if(c instanceof DockTab){
                if(((DockTab) c).isSelected()){
                    list.add((DockTab) c);
                    break;
                }
            }
        }
        for(JComponent c: unSplitModeTabBar.tabList){
            if(c instanceof DockTab){
                if(((DockTab) c).isSelected()){
                    list.add((DockTab) c);
                    break;
                }
            }
        }
        return list;
    }

    public JSplitPane getTabContent() {
        return tabContent;
    }

}
