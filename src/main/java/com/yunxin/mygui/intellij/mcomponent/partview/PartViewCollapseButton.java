package com.yunxin.mygui.intellij.mcomponent.partview;

import com.formdev.flatlaf.util.UIScale;
import com.yunxin.mygui.share.icon.CompoundIcon;
import com.yunxin.mygui.share.icon.RotatedIcon;
import com.yunxin.mygui.share.icon.TextIcon;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yunxin.mygui.intellij.mcomponent.partview.IJPanel.MODE_SPLIT;
import static com.yunxin.mygui.intellij.mcomponent.partview.IJPanel.MODE_UNSPLIT;


public class PartViewCollapseButton extends JToggleButton implements ItemListener, MouseMotionListener, MouseListener {

    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int BOTTOM = 2;
    public static final int TOP = 3;

    private IJPanel IJPanel;
    private int direction = IJPanel.TOP;
    private Component view;
    private boolean split;
    private int splitMode = MODE_SPLIT;
    private String title;
    private Dimension partSize = null;
    private JCheckBoxMenuItem spliteMenuItem = null;
    private JPopupMenu popupMenu = null;

    JFrame topFrame = null;

    Map[] states4Bar = new HashMap[4];

    private int splitLocation = 100;





    public PartViewCollapseButton() {
        setBackground(UIManager.getColor("Panel.background"));
        setFont(new Font(Font.SANS_SERIF,Font.PLAIN,UIScale.scale(12)));
        initPopup();
        initAction();
        addMouseListener(this);
        addMouseMotionListener(this);
        for(int i=0;i<4;i++){
            states4Bar[i] = new HashMap();
        }
    }

    private void initAction() {
        addItemListener(this);
    }

    private void initPopup() {
        popupMenu = new JPopupMenu();
        setComponentPopupMenu(popupMenu);
        JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem("Split Mode");
        menuItem.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(IJPanel ==null) return;

                boolean isSelected = isSelected();

                setSelected(false);

                split = menuItem.isSelected();
                splitMode = split? MODE_SPLIT: MODE_UNSPLIT;

                if(splitMode == MODE_SPLIT){
                    try {
                        IJPanel.toolBars[direction][MODE_UNSPLIT].remove(PartViewCollapseButton.this);
                    }catch (Throwable t){}
                    IJPanel.toolBars[direction][MODE_SPLIT].add(PartViewCollapseButton.this);
                }else{
                    try {
                        IJPanel.toolBars[direction][MODE_SPLIT].remove(PartViewCollapseButton.this);
                    }catch (Throwable t){}
                    IJPanel.toolBars[direction][MODE_UNSPLIT].add(PartViewCollapseButton.this);
                }

                setSelected(isSelected);
                IJPanel.toolBars[direction][MODE_SPLIT].invalidate();
                IJPanel.toolBars[direction][MODE_SPLIT].updateUI();
                IJPanel.toolBars[direction][MODE_UNSPLIT].invalidate();
                IJPanel.toolBars[direction][MODE_UNSPLIT].updateUI();
                IJPanel.barPanels[direction].revalidate();
            }
        });
        spliteMenuItem = menuItem;
        popupMenu.add(menuItem);
    }



    public javax.swing.JFrame getTopFrame() {
        if(topFrame == null){
            topFrame = IJPanel.getTopFrame();
        }
        return topFrame;
    }

    public Component getTopGlassPane(){
       return getTopFrame().getGlassPane();
    }

    public Dimension getPartSize() {
        return partSize;
    }

    public void setPartSize(Dimension partSize) {
        this.partSize = partSize;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
        updateLable();
        updateViewBorder();
    }

    public Component getView() {
        return view;
    }

    public void setView(Component view) {
        this.view = view;
    }

    public JCheckBoxMenuItem getSpliteMenuItem() {
        return spliteMenuItem;
    }

    public void setSpliteMenuItem(JCheckBoxMenuItem spliteMenuItem) {
        this.spliteMenuItem = spliteMenuItem;
    }

    public JPopupMenu getPopupMenu() {
        return popupMenu;
    }

    public void setPopupMenu(JPopupMenu popupMenu) {
        this.popupMenu = popupMenu;
    }

    public boolean isSplit() {
        return split;
    }

    public void setSplit(boolean split) {
        this.split = split;
        this.splitMode = split?MODE_SPLIT:MODE_UNSPLIT;
        spliteMenuItem.setSelected(split);

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        updateLable();
    }

    Icon myIcon = null;

    public void setIcon(Icon icon){
        this.myIcon = icon;
        updateLable();
    }

    private void updateLable(){
        TextIcon textIcon = new TextIcon(this,title);
        CompoundIcon ci = null;
        if(myIcon!=null){
            ci = new CompoundIcon(CompoundIcon.Axis.X_AXIS,5, myIcon,textIcon);
        }else{
            ci = new CompoundIcon(textIcon);
        }
        RotatedIcon ri;
        if(direction== LEFT){
            ri = new RotatedIcon(ci, RotatedIcon.Rotate.UP);
            setBorder(new EmptyBorder(UIScale.scale(6),UIScale.scale(2),UIScale.scale(6),UIScale.scale(2)));
        }else if(direction == RIGHT){
            ri = new RotatedIcon(ci, RotatedIcon.Rotate.DOWN);
            setBorder(new EmptyBorder(UIScale.scale(6),UIScale.scale(2),UIScale.scale(6),UIScale.scale(2)));
        }else{
            ri = new RotatedIcon(ci, RotatedIcon.Rotate.ABOUT_CENTER);
            setBorder(new EmptyBorder(2,6,2,6));
            setBorder(new EmptyBorder(UIScale.scale(2),UIScale.scale(6),UIScale.scale(2),UIScale.scale(6)));
        }
        super.setIcon(ri);

    }

    public void updateViewBorder(){
        if(direction == TOP){
            ((JPanel)getView()).setBorder(new MLineBorder(Color.lightGray,1,false,false,false,true));
        }else if(direction == BOTTOM){
            ((JPanel)getView()).setBorder(new MLineBorder(Color.lightGray,1,false,false,true,false));
        }else  if(direction == LEFT){
            ((JPanel)getView()).setBorder(new MLineBorder(Color.lightGray,1,false,true,false,false));
        }else if(direction ==RIGHT){
            ((JPanel)getView()).setBorder(new MLineBorder(Color.lightGray,1,true,false,false,false));
        }
    }

    public IJPanel getIJPanel() {
        return IJPanel;
    }

    public void setIJPanel(IJPanel IJPanel) {
        this.IJPanel = IJPanel;
    }


    @Override
    public void itemStateChanged(ItemEvent e) {

        if(isSelected()){
            IJPanel.setSelectedExcept(IJPanel.toolBars[direction][splitMode],this,false);
            if(split){
                IJPanel.slidePanes[direction].setRightComponent(view);
            }else{
                IJPanel.slidePanes[direction].setLeftComponent(view);
            }
            System.out.println("view pref: "+view.getPreferredSize());
            if(getPartSize()!=null){
                IJPanel.slidePanes[direction].setPreferredSize(getPartSize());
            }

            IJPanel.slidePanes[direction].setVisible(isSelected());

        }else{


            if(split){
                if(IJPanel.slidePanes[direction].getBottomComponent()==view){
                    IJPanel.slidePanes[direction].setBottomComponent(null);
                }else{

                }
            }else{
                if(IJPanel.slidePanes[direction].getTopComponent()==view){
                    IJPanel.slidePanes[direction].setTopComponent(null);
                }
            }
        }

        if(IJPanel.slidePanes[direction].getComponentCount()==3){
            IJPanel.slidePanes[direction].setVisible(true);
            IJPanel.slidePanes[direction].setDividerSize(IJPanel.splitterSize+1);
            IJPanel.slidePanes[direction].setDividerLocation(getSplitLocation());
        }else{
            IJPanel.slidePanes[direction].setDividerSize(0);
            if(IJPanel.slidePanes[direction].getComponentCount()==1){
                IJPanel.slidePanes[direction].setVisible(false);
            }else if(IJPanel.slidePanes[direction].getComponentCount()==2){
                IJPanel.slidePanes[direction].setVisible(true);
            }
        }

        IJPanel.center.revalidate();
    }

    public int getSplitLocation() {
        return splitLocation;
    }

    public void setSplitLocation(int splitLocation) {
        this.splitLocation = splitLocation;
    }

    JXButton button = new JXButton("dddd");
    Point draggedStart = null;
    JXPanel tempHolder = new JXPanel(){{setBorder(new EtchedBorder());}};

    int half =0;

    int inArea;

    int inSplitMode;

    boolean setbefore = true;

    @Override
    public void mouseDragged(MouseEvent e) {
        if(!IJPanel.isDragButtonMode){
            IJPanel.isDragButtonMode = true;
            ((Container)getTopGlassPane()).setLayout(null);
            ((Container)getTopGlassPane()).add(button);
            draggedStart = e.getPoint();
//            this.setVisible(false);

        }else{
            Point p = SwingUtilities.convertPoint(e.getComponent(),e.getX(),e.getY(), IJPanel);
            Point p1 = SwingUtilities.convertPoint(e.getComponent(),e.getX(),e.getY(),getTopGlassPane());
            button.setBounds(p1.x-draggedStart.x,p1.y-draggedStart.y,this.getWidth(),this.getHeight());

             inArea = getInArea(p);
             inSplitMode = getSplitMode(inArea,p);
            if(inArea>=0){

                if(inSplitMode>=0){
                    tempHolder.setPreferredSize(getSize());
                    if(SwingUtils.indexOf(IJPanel.toolBars[inArea][inSplitMode],tempHolder)<0){
                        ((Container) IJPanel.toolBars[inArea][inSplitMode]).add(tempHolder);
                    }
                    Point pointInBar = SwingUtilities.convertPoint(this,e.getX(),e.getY(), IJPanel.toolBars[inArea][inSplitMode]);
                    if((direction== IJPanel.TOP)||(direction==BOTTOM)){

                    }else{
                        Component con = SwingUtilities.getDeepestComponentAt(IJPanel.toolBars[inArea][inSplitMode],pointInBar.x,pointInBar.y);

                        if(con instanceof PartViewCollapseButton){
                            half = con.getHeight()/2;
                            if((con.getBounds().getY()+con.getHeight()-pointInBar.y)>half){
                                setbefore = true;
                                SwingUtils.setBefore(((Container) IJPanel.toolBars[inArea][inSplitMode]),con,tempHolder,true);
                            }else{
                                setbefore = false;
                                SwingUtils.setAfter(((Container) IJPanel.toolBars[inArea][inSplitMode]),con,tempHolder,true);
                            }
                        }else{
                        }


                    }
                }
                highlightBar(inArea);
            }else{
                restoreBars();

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

        if(IJPanel.isDragButtonMode){
            IJPanel.rootPane.getGlassPane().setCursor(Cursor.getDefaultCursor());
            if((inArea>=0)&&(inSplitMode>=0)){
                this.getParent().remove(this);
                IJPanel.toolBars[inArea][inSplitMode].add(this);

                SwingUtils.setAfter(IJPanel.toolBars[inArea][inSplitMode],tempHolder,this,false);
//                printToolBar(applicationPanel.toolBars[inArea][inSplitMode]);
                IJPanel.toolBars[inArea][inSplitMode].remove(tempHolder);

//                printToolBar(applicationPanel.toolBars[inArea][inSplitMode]);
//                setVisible(true);
                setDirection(inArea);
                setSplit(inSplitMode==MODE_SPLIT);
                boolean isSelected = isSelected();
                if(isSelected){
                    setSelected(false);
                    setSelected(true);
                }
            }else{
//                setVisible(true);
            }

            restoreBars();
            ((Container)getTopGlassPane()).remove(button);
            ((Container)getTopGlassPane()).revalidate();
            SwingUtilities.updateComponentTreeUI(IJPanel);
            IJPanel.isDragButtonMode = false;


        }


    }

    void printToolBar(Container c){
        List<String> l = new ArrayList<>();
        List<Component> cs = SwingUtils.getComponentList(c);
        for(int i=0;i<cs.size();i++){
            if(cs.get(i) instanceof PartViewCollapseButton){
                l.add(((PartViewCollapseButton) cs.get(i)).getTitle());
            }else{
                l.add("<holder>");

            }
        }
        System.out.println(l);
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    void highlightBar(int direction){

        if(!states4Bar[direction].containsKey("backgroundColor")){
            states4Bar[direction].put("backgroundColor", IJPanel.barPanels[direction].getComponent(0).getBackground());
            if((direction==BOTTOM)){
                if(IJPanel.getPartViewCollapseButton(direction).isEmpty()){
                    ((JComponent) IJPanel.barPanels[direction].getComponent(0)).setPreferredSize(new Dimension(IJPanel.barPanels[direction].getPreferredSize().width,20));
                    ((JComponent) IJPanel.barPanels[direction].getComponent(0)).setVisible(true);
                    IJPanel.barPanels[direction].revalidate();
                }
                ((JComponent) IJPanel.barPanels[direction].getComponent(0)).getComponent(0).setBackground(Color.white);
                ((JComponent) IJPanel.barPanels[direction].getComponent(0)).getComponent(1).setBackground(Color.white);
                IJPanel.barPanels[direction].getComponent(0).setBackground(Color.white);
            }else if(direction==TOP){
                if(IJPanel.getPartViewCollapseButton(direction).isEmpty()){
                    ((JComponent) IJPanel.barPanels[direction].getComponent(1)).setPreferredSize(new Dimension(IJPanel.barPanels[direction].getPreferredSize().width,20));
                    ((JComponent) IJPanel.barPanels[direction].getComponent(1)).setVisible(true);
                    IJPanel.barPanels[direction].revalidate();
                }
                ((JComponent) IJPanel.barPanels[direction].getComponent(1)).getComponent(0).setBackground(Color.white);
                ((JComponent) IJPanel.barPanels[direction].getComponent(1)).getComponent(1).setBackground(Color.white);
                IJPanel.barPanels[direction].getComponent(1).setBackground(Color.white);
            }
            else {
                IJPanel.barPanels[direction].setBackground(Color.white);
                IJPanel.barPanels[direction].getComponent(0).setBackground(Color.white);
                IJPanel.barPanels[direction].getComponent(1).setBackground(Color.white);


            }
        }

    }

    void restoreBars(){
        for(int i=0;i<4;i++){
            if(states4Bar[i].containsKey("backgroundColor")){
                if((i==BOTTOM)){
                    if(IJPanel.getPartViewCollapseButton(i).isEmpty()){
                        ((JComponent) IJPanel.barPanels[i].getComponent(0)).setVisible(false);
                        ((JComponent) IJPanel.barPanels[i].getComponent(0)).revalidate();
                    }else{
                    }
                    ((JComponent) IJPanel.barPanels[i].getComponent(0)).getComponent(0).setBackground((Color) states4Bar[i].get("backgroundColor"));
                    ((JComponent) IJPanel.barPanels[i].getComponent(0)).getComponent(1).setBackground((Color) states4Bar[i].get("backgroundColor"));
                    IJPanel.barPanels[i].getComponent(0).setBackground((Color) states4Bar[i].get("backgroundColor"));
                }else if(i==TOP){
                    if(IJPanel.getPartViewCollapseButton(i).isEmpty()){
                        ((JComponent) IJPanel.barPanels[i].getComponent(1)).setVisible(false);
                        ((JComponent) IJPanel.barPanels[i].getComponent(1)).revalidate();
                    }else{
                    }
                    ((JComponent) IJPanel.barPanels[i].getComponent(1)).getComponent(0).setBackground((Color) states4Bar[i].get("backgroundColor"));
                    ((JComponent) IJPanel.barPanels[i].getComponent(1)).getComponent(1).setBackground((Color) states4Bar[i].get("backgroundColor"));
                    IJPanel.barPanels[i].getComponent(1).setBackground((Color) states4Bar[i].get("backgroundColor"));
                }
                else{
                    IJPanel.barPanels[i].setBackground((Color) states4Bar[i].get("backgroundColor"));
                    IJPanel.barPanels[i].getComponent(0).setBackground((Color) states4Bar[i].get("backgroundColor"));
                    IJPanel.barPanels[i].getComponent(1).setBackground((Color) states4Bar[i].get("backgroundColor"));
                }
                states4Bar[i].remove("backgroundColor");
            }


            for(int j=0;j<2;j++){
                try {
                    IJPanel.toolBars[i][j].remove(tempHolder);
                }catch (Throwable t){}
            }


        }
    }

    int getInArea(Point p){
        if((p.x>=0)&&(p.x<=20)){
            //left
            return LEFT;

        }else if((p.x<= IJPanel.getWidth())&&(p.x>=(IJPanel.getWidth()-20))){
            //right

            return RIGHT;
        }else if((p.y<= IJPanel.toolbarPanel.getHeight()+20)&&(p.y>= IJPanel.toolbarPanel.getHeight())){
            //top

            return TOP;
        }else if((p.y<=(IJPanel.getHeight()- IJPanel.statusPanel.getHeight()))&&(p.y>=(IJPanel.getHeight()- IJPanel.statusPanel.getHeight()-20))){
            //bottom
            return BOTTOM;
        }
        return -1;
    }

    Rectangle rectangle_split = new Rectangle();
    Rectangle rectangle_unsplit = new Rectangle();
    Point pInBars = null;

    int getSplitMode(int direction, Point p){
        if(direction==-1) return -1;

        pInBars = SwingUtilities.convertPoint(IJPanel,p.x,p.y, IJPanel.barPanels[direction]);

        IJPanel.toolBars[direction][MODE_UNSPLIT].getBounds(rectangle_unsplit);
        IJPanel.toolBars[direction][MODE_SPLIT].getBounds(rectangle_split);


        if(direction== IJPanel.TOP){
            rectangle_split.y = rectangle_split.y+ IJPanel.toolbarPanel.getHeight();
            rectangle_unsplit.y = rectangle_unsplit.y+ IJPanel.toolbarPanel.getHeight();
        }

        if(rectangle_split.contains(pInBars)) return MODE_SPLIT;

        if(rectangle_unsplit.contains(pInBars)) return MODE_UNSPLIT;

        return -1;

    }



    public static void main(String[] args) {
        /**
         *  0,0 => 0
         *  0,1 => 1
         *  1,0 => 2
         *  1,1 => 3
         *  2,0 => 4
         *  2,1 => 5
         *  3,0 => 6
         *  3,1 => 7
         *
         */

        for(int i=1;i<=4;i++){
            for(int j=1;j<=2;j++){
                System.out.println(i+","+j+"="+(i<<j));
            }
        }
    }
}
