package com.yunxin.mygui.intellij.mcomponent.partview;

import com.yunxin.mygui.share.layout.HorizontalLayout;
import com.yunxin.mygui.share.layout.VerticalLayout;
import org.jdesktop.swingx.JXImagePanel;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXRootPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.HierarchyBoundsAdapter;
import java.awt.event.HierarchyEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class IJPanelTabBar extends JXPanel implements LayoutManager {

    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int BOTTOM = 2;
    public static final int TOP = 3;

    public static int MODE_SPLIT = 0;
    public static int MODE_UNSPLIT = 1;





    IJPanelTabToolBar toolBar;
    IJPanelTabMode splitMode;
    IJPanelTabMode unSplitMode;

    JSplitPane tabContent;

    JXPanel tabArea;
    JXPanel modeArea;
    IntelliJPanel intelliJPanel;

    int direction = 0;
    int gap = 0;

    int defaultModeAreaSize = 20;
    Rectangle highLightBound = null;


    Color highLightColor = null;

    Color storeHighLightColor = null;


    boolean isHighLight = false;



    PropertyChangeListener pcl;

    public IJPanelTabBar(IntelliJPanel intelliJPanel, int direction){

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
                    ((IJTabItem)o).splitLocation = (splitPane.getDividerLocation());
                }
                o = ((JComponent)splitPane.getRightComponent()).getClientProperty("myController");
                if(o!=null){
                    ((IJTabItem)o).splitLocation= (splitPane.getDividerLocation());
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

    public int getVisibleTabCount(){
        return splitMode.getVisibleTabCount()+unSplitMode.getVisibleTabCount();
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
            modeArea.setVisible(false);
            tabContent.setVisible(false);
            ((MLineBorder)tabArea.getBorder()).setInsets(false,false,false,false);
            ((MLineBorder)tabArea.getBorder()).setInsets(false,false,false,false);
        }else{
            modeArea.setVisible(true);
            if((splitMode.activeItem==null)&&(unSplitMode.activeItem==null)){
                tabContent.setVisible(false);
            }else{
                tabContent.setVisible(true);
            }

            if(direction==LEFT){
                ((MLineBorder)tabArea.getBorder()).setInsets(false,true,false,false);
                ((MLineBorder)toolBar.getBorder()).setInsets(false,true,false,false);
            }else if(direction==RIGHT){
                ((MLineBorder)tabArea.getBorder()).setInsets(true,false,false,false);
                ((MLineBorder)toolBar.getBorder()).setInsets(true,false,false,false);
            }else if(direction==BOTTOM){
                ((MLineBorder)tabArea.getBorder()).setInsets(false,false,true,false);
                ((MLineBorder)toolBar.getBorder()).setInsets(false,false,true,false);
            }else if(direction==TOP){
                ((MLineBorder)tabArea.getBorder()).setInsets(false,false,false,true);
                ((MLineBorder)toolBar.getBorder()).setInsets(false,false,false,true);
            }

        }

        if(toolBar.getComponentCount()==0){
            toolBar.setVisible(false);
        }else{
            toolBar.setVisible(true);
        }


        splitMode.boundInGlass = SwingUtilities.convertRectangle(splitMode,splitMode.getBounds(),intelliJPanel.getGlassPane());
        unSplitMode.boundInGlass = SwingUtilities.convertRectangle(unSplitMode,unSplitMode.getBounds(),intelliJPanel.getGlassPane());

        updateHoverSize();
    }

    private void initComponents() {
        tabArea = new JXPanel();

        toolBar = new IJPanelTabToolBar(this);
        splitMode = new IJPanelTabMode(this,MODE_SPLIT);
        unSplitMode = new IJPanelTabMode(this,MODE_UNSPLIT);

        modeArea = new JXPanel();
        tabContent = new JSplitPane();
        tabContent.setVisible(false);
        tabContent.setLeftComponent(null);
        tabContent.setRightComponent(null);
        tabContent.setContinuousLayout(true);
        tabContent.setDividerSize(8);



        if(direction == LEFT){

            modeArea.setLayout(new GridLayout(2,1));
            tabArea.setLayout(new HorizontalLayout(0,true));
            tabArea.setBorder(new MLineBorder(intelliJPanel.defaultBorderColor,1,false,true,false,false));

            toolBar.setOrientation(SwingConstants.VERTICAL);
            toolBar.setBorder(new MLineBorder(intelliJPanel.defaultBorderColor,1,false,true,false,false));

            add(tabArea,BorderLayout.WEST);
            add(tabContent,BorderLayout.CENTER);

            tabContent.setBorder(new MLineBorder(intelliJPanel.defaultBorderColor,1,false,true,false,false));
            tabContent.setOrientation(JSplitPane.VERTICAL_SPLIT);

            modeArea.add(unSplitMode);
            modeArea.add(splitMode);
            tabArea.add(toolBar);
            tabArea.add(modeArea);
        }else if(direction == RIGHT){

            modeArea.setLayout(new GridLayout(2,1));
            tabArea.setLayout(new HorizontalLayout(0,false));
            tabArea.setBorder(new MLineBorder(intelliJPanel.defaultBorderColor,1,true,false,false,false));

            toolBar.setOrientation(SwingConstants.VERTICAL);
            toolBar.setBorder(new MLineBorder(intelliJPanel.defaultBorderColor,1,true,false,false,false));

            add(tabArea,BorderLayout.EAST);
            add(tabContent,BorderLayout.CENTER);


            tabContent.setOrientation(JSplitPane.VERTICAL_SPLIT);
            tabContent.setBorder(new MLineBorder(intelliJPanel.defaultBorderColor,1,true,false,false,false));

            modeArea.add(unSplitMode);
            modeArea.add(splitMode);
            tabArea.add(toolBar);
            tabArea.add(modeArea);
        }else if(direction == TOP){

            modeArea.setLayout(new GridLayout(1,2));
            tabArea.setLayout(new VerticalLayout(0,true));

            tabArea.setBorder(new MLineBorder(intelliJPanel.defaultBorderColor,1,false,false,false,true));
            toolBar.setBorder(new MLineBorder(intelliJPanel.defaultBorderColor,1,false,false,false,true));
            tabContent.setBorder(new MLineBorder(intelliJPanel.defaultBorderColor,1,false,false,false,true));

            add(tabArea,BorderLayout.NORTH);
            add(tabContent,BorderLayout.CENTER);

            tabContent.setOrientation(JSplitPane.HORIZONTAL_SPLIT);

            modeArea.add(unSplitMode);
            modeArea.add(splitMode);
            tabArea.add(toolBar);
            tabArea.add(modeArea);
        }else if(direction == BOTTOM){
            modeArea.setLayout(new GridLayout(1,2));

            tabArea.setLayout(new VerticalLayout(0,false));
            tabArea.setBorder(new MLineBorder(intelliJPanel.defaultBorderColor,1,false,false,true,false));
            toolBar.setBorder(new MLineBorder(intelliJPanel.defaultBorderColor,1,false,false,true,false));

            tabContent.setBorder(new MLineBorder(intelliJPanel.defaultBorderColor,1,false,false,true,false));

            add(tabArea,BorderLayout.SOUTH);
            add(tabContent,BorderLayout.CENTER);

            modeArea.add(unSplitMode);
            modeArea.add(splitMode);
            tabArea.add(toolBar);
            tabArea.add(modeArea);
        }
        toolBar.add(new JXLabel(new com.formdev.flatlaf.icons.FlatFileViewComputerIcon()));

    }


    public IJPanelTabMode getMode(int splitMode){
        if(splitMode==MODE_SPLIT){
            return this.splitMode;
        }else if(splitMode == MODE_UNSPLIT){
            return unSplitMode;
        }else{
            throw new UnsupportedOperationException("UnSupported MODE");
        }
    }

    public IJPanelTabToolBar getToolBar() {
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

    JXImagePanel panel1;
    JXImagePanel panel2;
    public void highLight(){
        if(!isHighLight){
            isHighLight = true;


            BufferedImage bufferedImage1 = new BufferedImage(Math.max(splitMode.boundInGlass.width,20),Math.max(splitMode.boundInGlass.height,20),BufferedImage.TYPE_INT_ARGB);
            BufferedImage bufferedImage2 = new BufferedImage(Math.max(splitMode.boundInGlass.width,20),Math.max(splitMode.boundInGlass.height,20),BufferedImage.TYPE_INT_ARGB);
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

            panel1 = new JXImagePanel();
            panel2 = new JXImagePanel();


            panel1.setBounds(splitMode.boundInGlass);
            panel2.setBounds(unSplitMode.boundInGlass);

            panel1.setImage(bufferedImage1);
            panel2.setImage(bufferedImage2);


            ((JXPanel)intelliJPanel.getGlassPane()).add(panel1);
            ((JXPanel)intelliJPanel.getGlassPane()).add(panel2);
            panel1.setAlpha(0.3f);
            panel2.setAlpha(0.3f);
            ((JXPanel)intelliJPanel.getGlassPane()).updateUI();

        }
    }

    public void unHighLight(){
        if(isHighLight){
            isHighLight = false;
            try{
                ((JXPanel)intelliJPanel.getGlassPane()).remove(panel1);
            }catch (Throwable t){}
            try{
                ((JXPanel)intelliJPanel.getGlassPane()).remove(panel2);
            }catch (Throwable t){}
            ((JXPanel)intelliJPanel.getGlassPane()).updateUI();
        }
    }

    void updateHoverSize(){

        if((splitMode.getComponentCount()+unSplitMode.getComponentCount())>0){
            splitMode.boundInGlass = SwingUtilities.convertRectangle(modeArea,splitMode.getBounds(),intelliJPanel.getGlassPane());
            unSplitMode.boundInGlass = SwingUtilities.convertRectangle(modeArea,unSplitMode.getBounds(),intelliJPanel.getGlassPane());

        }else{

            if((direction==LEFT)){
                unSplitMode.boundInGlass = new Rectangle(getX()+toolBar.getX()+toolBar.getWidth(),getY()+toolBar.getY(),defaultModeAreaSize,getHeight()/2);
                splitMode.boundInGlass = new Rectangle(getX()+toolBar.getX()+toolBar.getWidth(),getY()+toolBar.getY()+getHeight()/2,defaultModeAreaSize,getHeight()/2);

            }else if(direction==RIGHT){
                unSplitMode.boundInGlass = new Rectangle(getX()+toolBar.getX()-defaultModeAreaSize,getY()+toolBar.getY(),defaultModeAreaSize,getHeight()/2);
                splitMode.boundInGlass = new Rectangle(getX()+toolBar.getX()-defaultModeAreaSize,getY()+toolBar.getY()+getHeight()/2,defaultModeAreaSize,getHeight()/2);

            }
            else if((direction==TOP)){
                unSplitMode.boundInGlass = new Rectangle(getX()+toolBar.getX(),getY()+toolBar.getY()+toolBar.getHeight(),getWidth()/2,defaultModeAreaSize);
                splitMode.boundInGlass = new Rectangle(getX()+toolBar.getX()+getWidth()/2,getY()+toolBar.getY()+toolBar.getHeight(),getWidth()/2,defaultModeAreaSize);

            }else if(direction==BOTTOM){
                unSplitMode.boundInGlass = new Rectangle(getX()+toolBar.getX(),getY()+getHeight()-toolBar.getHeight()-defaultModeAreaSize,getWidth()/2,defaultModeAreaSize);
                splitMode.boundInGlass = new Rectangle(toolBar.getX()+getWidth()/2,getY()+getHeight()-toolBar.getHeight()-defaultModeAreaSize,getWidth()/2,defaultModeAreaSize);
            }
        }




    }
}
