package com.yunxin.mygui.intellij.mcomponent.partview;

import com.jidesoft.swing.JideSplitPane;
import com.yunxin.mygui.share.component.BetterGlassPane;
import com.yunxin.mygui.share.layout.HorizontalLayout;
import com.yunxin.mygui.share.component.MLabel;
import com.yunxin.mygui.share.layout.VerticalLayout;
import org.jdesktop.swingx.JXPanel;
import org.pushingpixels.photon.icon.SvgBatikResizableIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;


public class IJPanel extends JXPanel implements MouseListener,MouseMotionListener,MouseWheelListener{

    public static int LEFT = 0;
    public static int RIGHT = 1;
    public static int BOTTOM = 2;
    public static int TOP = 3;

    public static int MODE_SPLIT = 0;
    public static int MODE_UNSPLIT = 1;

    JPanel[] barPanels = new JPanel[4];
    JSplitPane[] slidePanes = new JSplitPane[4];
    JComponent[][] toolBars = new JComponent[4][2];

    JPanel center = null;

    JRootPane rootPane = new JRootPane();

    JPanel statusPanel;

    JPanel toolbarPanel;


    //drag mode
    boolean isDragButtonMode = false;

    // panel resize
    private boolean resizing = false;
    private Point currentPoint = null;
    private int dLeft = 100;
    private int dRight = 100;
    private int dBottom = 100;
    private int dTop = 100;
    int splitterSize = 8;
    int initD = 0;
    private int resizeDirection = 0;    // 0: west; 1: east; 2: bottom;



     JPanel centralPanel = null;

     JFrame frame = null;

    public JFrame getTopFrame(){

        JFrame tmp = null;
        Container container = getParent();
        while(true){
            if(container!=null){
                if(container instanceof JFrame){
                    this.frame = (JFrame) container;
                    break;
                }else{
                    container = container.getParent();
                }
            }else{
                break;
            }

        }
        return frame;
    }

    public IJPanel() {
        initComponent();
        new JideSplitPane();

        TabbedPartView panel = new TabbedPartView("hello",null);
        panel.addTab("Project",new com.formdev.flatlaf.icons.FlatFileViewComputerIcon(),new JTextArea("hello"));
        panel.addTab("Structure",new com.formdev.flatlaf.icons.FlatInternalFrameIconifyIcon(),new JButton("bbbb"));
        panel.setPreferredSize(new Dimension(100,300));

        String s ="<svg t=\"1581699566339\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"2975\" width=\"200\" height=\"200\"><path d=\"M512 95.352695c4.952802 0 14.475689 1.217734 22.624277 9.371438l86.620823 86.62287a64.007803 64.007803 0 0 0 45.255716 18.744923h119.56412c17.647939 0 31.998785 14.354939 31.998785 31.998784v119.56412a64.003709 64.003709 0 0 0 18.74083 45.252647l82.472339 82.4662c8.147564 8.153704 9.366322 17.675568 9.366322 22.6253s-1.218758 14.470573-9.374508 22.626323l-82.465177 82.465177a64.002686 64.002686 0 0 0-18.74083 45.252646v123.721813c0 17.643845-14.350846 31.998785-31.998784 31.998785H662.344147a64.007803 64.007803 0 0 0-45.255717 18.744923l-82.45699 82.464153c-8.15575 8.15575-17.678638 9.373485-22.63144 9.373485s-14.468526-1.217734-22.624277-9.372462l-82.465176-82.465176a64.011896 64.011896 0 0 0-45.255717-18.744923H242.09071c-17.647939 0-31.998785-14.353916-31.998784-31.998785v-119.563096a64.001663 64.001663 0 0 0-18.74083-45.251624l-86.62901-86.624916c-8.147564-8.153704-9.366322-17.675568-9.366321-22.6253s1.218758-14.470573 9.374508-22.627347l86.620823-86.623893a64.001663 64.001663 0 0 0 18.74083-45.251623V242.09071c0-17.643845 14.350846-31.998785 31.998784-31.998784h115.408474a64.007803 64.007803 0 0 0 45.255716-18.744923l86.612637-86.620823c8.156774-8.15575 17.679661-9.373485 22.632463-9.373485m0-63.99757c-24.569582 0-49.13814 9.372461-67.879993 28.116361l-86.620823 86.623893h-115.408474c-53.013401 0-95.995331 42.97886-95.995331 95.995331v115.405404l-86.620823 86.623893c-37.490869 37.488823-37.490869 98.269118 0 135.75794l86.620823 86.623893v119.563096c0 53.016471 42.98193 95.995331 95.995331 95.995331h119.56412l82.465177 82.465177c18.74083 18.7439 43.310412 28.116361 67.879993 28.116361 24.569582 0 49.13814-9.372461 67.879993-28.116361l82.465177-82.465177h123.720789c53.020564 0 95.995331-42.97886 95.995331-95.995331V662.343123l82.465177-82.465176c37.490869-37.488823 37.490869-98.269118 0-135.75794l-82.465177-82.465177V242.09071c0-53.016471-42.974767-95.995331-95.995331-95.995331H666.499793l-86.620823-86.623893c-18.74083-18.7439-43.310412-28.116361-67.87897-28.116361z\" p-id=\"2976\"></path><path d=\"M326.642408 518.932899l97.238649 72.217789c8.241708 6.121418 19.676132 5.503341 27.209711-1.471514l253.534344-234.697324a21.333888 21.333888 0 0 1 22.825868-3.982707l5.006014 2.125407c7.150864 3.035126 10.486842 11.291161 7.452739 18.442024a14.084787 14.084787 0 0 1-2.275833 3.667529L458.773751 700.020234c-7.674796 8.93858-21.142529 9.962909-30.081109 2.288113a21.473057 21.473057 0 0 1-1.790785-1.728364L288.010553 549.860283c-6.650467-7.216355-6.191003-18.458397 1.025353-25.107841a17.758456 17.758456 0 0 1 4.88424-3.196809l11.410888-5.021363a21.332864 21.332864 0 0 1 21.311374 2.398629z\" p-id=\"2977\"></path></svg>";

        Icon icon = SvgBatikResizableIcon.getSvgIcon(new ByteArrayInputStream(s.getBytes()),new Dimension(16,16));

        buildCollapsePanel("left 1", new com.formdev.flatlaf.icons.FlatFileViewComputerIcon(),panel, LEFT, false, true);
        JPanel panel1 = new SimplePartView("sss",null);
        panel1.setPreferredSize(new Dimension(200,300));
        buildCollapsePanel("left 222", icon,panel1, LEFT,false, false);

        JPanel panel2 = new JPanel();
        panel2.setBackground(Color.GREEN);
        panel2.setPreferredSize(new Dimension(50,400));
        buildCollapsePanel("left 333", new com.formdev.flatlaf.icons.FlatFileViewComputerIcon(),panel2, LEFT,true, false);

        JPanel panel4 = new JPanel();
        panel4.setBackground(Color.RED);
        buildCollapsePanel("left 4443", new com.formdev.flatlaf.icons.FlatFileViewComputerIcon(),panel4, LEFT,true, false);

//        TabbedPartView bp1 = new TabbedPartView("hello",null);
//        bp1.addTab("Run",new com.formdev.flatlaf.icons.FlatInternalFrameIconifyIcon(),new JPanel());
//        buildCollapsePanel("Run", new com.formdev.flatlaf.icons.FlatDescendingSortIcon(),bp1, BOTTOM, false, true);
//
//        TabbedPartView bp2 = new TabbedPartView("b2",null);
//        bp2.addTab("Run b2",new com.formdev.flatlaf.icons.FlatInternalFrameIconifyIcon(),new JPanel());
//        buildCollapsePanel("Run", new com.formdev.flatlaf.icons.FlatDescendingSortIcon(),bp2, BOTTOM, true, true);
//


        TabbedPartView rp1 = new TabbedPartView("hello",null);
        rp1.addTab("Run",new com.formdev.flatlaf.icons.FlatInternalFrameIconifyIcon(),new JPanel());
        buildCollapsePanel("Run", new com.formdev.flatlaf.icons.FlatDescendingSortIcon(),rp1, RIGHT, false, true);


//        TabbedPartView tp1 = new TabbedPartView("top hello",null);
//        tp1.addTab("Run",new com.formdev.flatlaf.icons.FlatInternalFrameIconifyIcon(),new JPanel());
//        buildCollapsePanel("Run", new com.formdev.flatlaf.icons.FlatDescendingSortIcon(),tp1, TOP, false, true);
//
//
//        TabbedPartView tp2 = new TabbedPartView("top hello",null);
//        tp2.addTab("Run",new com.formdev.flatlaf.icons.FlatInternalFrameIconifyIcon(),new JPanel());
//        buildCollapsePanel("Run", new com.formdev.flatlaf.icons.FlatDescendingSortIcon(),tp2, TOP, true, false);


        centralPanel.setLayout(new BorderLayout());
        JTextArea textArea = new JTextArea();
        centralPanel.add(textArea,BorderLayout.CENTER);


    }

    private void initComponent() {
        setLayout(new BorderLayout());
        buildAroundBars();
        buildMainArea();
        for(int i=0;i<4;i++){
            slidePanes[i].setBorder(new EmptyBorder(0,0,0,0));
        }

    }

    private void  buildMainArea() {
        rootPane = new JRootPane();


        center = new JPanel();
        center.setLayout(new BorderLayout());
        add(rootPane,BorderLayout.CENTER);


        rootPane.getContentPane().setLayout(new BorderLayout());
        rootPane.getContentPane().add(center,BorderLayout.CENTER);


        BorderLayout borderLayout = new BorderLayout();
        centralPanel = new JPanel();
        centralPanel.setLayout(borderLayout);
        centralPanel.addMouseListener(new MouseAdapter() {});
        centralPanel.addMouseMotionListener(new MouseAdapter() {});


        PropertyChangeListener pcl = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {

                JSplitPane splitPane = (JSplitPane) evt.getSource();

                if((splitPane.getRightComponent()==null)||(splitPane.getLeftComponent()==null)){
                    System.out.println("pp: null");
                    System.out.println(splitPane.getDividerLocation());
                    return;
                }

                Object o = ((JComponent)splitPane.getLeftComponent()).getClientProperty("myController");
                if(o!=null){
                    ((PartViewCollapseButton)o).setSplitLocation(splitPane.getDividerLocation());
                }
                o = ((JComponent)splitPane.getRightComponent()).getClientProperty("myController");
                if(o!=null){
                    ((PartViewCollapseButton)o).setSplitLocation(splitPane.getDividerLocation());
                }
            }
        };

        slidePanes[LEFT] = new JSplitPane();
        slidePanes[LEFT].setOrientation(JSplitPane.VERTICAL_SPLIT);
        slidePanes[LEFT].setTopComponent(null);
        slidePanes[LEFT].setBottomComponent(null);
        slidePanes[LEFT].setDividerSize(0);
//        slidePanes[LEFT].setPreferredSize(new Dimension(300,100));
        slidePanes[LEFT].setContinuousLayout(true);
        if(slidePanes[LEFT].getUI() instanceof BasicSplitPaneUI){
            ((BasicSplitPaneUI) slidePanes[LEFT].getUI()).getDivider().setBorder(new MLineBorder(Color.lightGray,1,false,false,true,true));
        }
        slidePanes[LEFT].setBorder(new MLineBorder(Color.lightGray,1,false,false,false,false));






        slidePanes[RIGHT] = new JSplitPane();
        slidePanes[RIGHT].setOrientation(JSplitPane.VERTICAL_SPLIT);
        slidePanes[RIGHT].setTopComponent(null);
        slidePanes[RIGHT].setBottomComponent(null);
        slidePanes[RIGHT].setDividerSize(0);
        slidePanes[RIGHT].setBorder(new MLineBorder(Color.lightGray,1,false,false,false,false));
        if(slidePanes[RIGHT].getUI() instanceof BasicSplitPaneUI){
            ((BasicSplitPaneUI) slidePanes[RIGHT].getUI()).getDivider().setBorder(new MLineBorder(Color.lightGray,1,false,false,true,true));
        }

        slidePanes[TOP] = new JSplitPane();
        slidePanes[TOP].setLeftComponent(null);
        slidePanes[TOP].setRightComponent(null);
        slidePanes[TOP].setDividerSize(0);
        slidePanes[TOP].setBorder(new MLineBorder(Color.lightGray,1,false,false,false,false));
        if(slidePanes[TOP].getUI() instanceof BasicSplitPaneUI){
            ((BasicSplitPaneUI) slidePanes[TOP].getUI()).getDivider().setBorder(new MLineBorder(Color.lightGray,1,true,true,false,false));
        }

        slidePanes[BOTTOM] = new JSplitPane();;
        slidePanes[BOTTOM].setLeftComponent(null);
        slidePanes[BOTTOM].setRightComponent(null);
        slidePanes[BOTTOM].setDividerSize(0);
        slidePanes[BOTTOM].setBorder(new MLineBorder(Color.lightGray,1,false,false,false,false));

        if(slidePanes[BOTTOM].getUI() instanceof BasicSplitPaneUI){
            ((BasicSplitPaneUI) slidePanes[BOTTOM].getUI()).getDivider().setBorder(new MLineBorder(Color.lightGray,1,true,true,false,false));
        }

        center.add(slidePanes[LEFT], BorderLayout.WEST);
        center.add(slidePanes[RIGHT],BorderLayout.EAST);
        center.add(slidePanes[TOP],BorderLayout.NORTH);
        center.add(slidePanes[BOTTOM],BorderLayout.SOUTH);
        center.add(centralPanel,BorderLayout.CENTER);
        centralPanel.setOpaque(true);

        slidePanes[LEFT].setOpaque(true);
        slidePanes[RIGHT].setOpaque(true);
        slidePanes[BOTTOM].setOpaque(true);
        slidePanes[TOP].setOpaque(true);

        slidePanes[LEFT].addPropertyChangeListener("dividerLocation", pcl);
        slidePanes[RIGHT].addPropertyChangeListener("dividerLocation", pcl);
        slidePanes[BOTTOM].addPropertyChangeListener("dividerLocation", pcl);
        slidePanes[TOP].addPropertyChangeListener("dividerLocation", pcl);


        new BetterGlassPane(rootPane);
        rootPane.getGlassPane().addMouseMotionListener(this);
        rootPane.getGlassPane().addMouseListener(this);
        rootPane.getGlassPane().addMouseWheelListener(this);


    }

    private void buildAroundBars() {
        buildTopBar();
        buildLeftBar();
        buildRightBar();
        buildBottomBar();

    }

    private void buildBottomBar() {
        JPanel bottom = new JPanel();
        barPanels[BOTTOM] = bottom;
        bottom.setBorder(new MLineBorder(Color.lightGray,1,false,false,true,true));

        VerticalLayout layout1 = new VerticalLayout();
        bottom.setLayout(layout1);
        add(bottom,BorderLayout.SOUTH);

        JPanel panel = new JPanel();
        EmptyBorder border = new EmptyBorder(0,21,0,21);
        panel.setBorder(border);
        GridLayout layout = new GridLayout(1,2);
        panel.setLayout(layout);
        toolBars[BOTTOM][MODE_UNSPLIT] = new JPanel(){{setBorder(new EmptyBorder(0,0,0,0));}};
        toolBars[BOTTOM][MODE_SPLIT] = new JPanel(){{setBorder(new EmptyBorder(0,0,0,0));}};

        toolBars[BOTTOM][MODE_UNSPLIT].setLayout(new HorizontalLayout(0,true));
        toolBars[BOTTOM][MODE_SPLIT].setLayout(new HorizontalLayout(0,false));



        panel.add(toolBars[BOTTOM][MODE_UNSPLIT]);
        panel.add(toolBars[BOTTOM][MODE_SPLIT]);
        bottom.add(panel);

        statusPanel = new JPanel();
        statusPanel.setBorder(new MLineBorder(Color.lightGray,1,false,false,true,false));
        statusPanel.setLayout(new BorderLayout());
        JToolBar statusBar = new JToolBar();
        statusBar.setFloatable(false);
        statusPanel.add(statusBar,BorderLayout.CENTER);
        MLabel mLabel = new MLabel();
        mLabel.setIcon(new com.formdev.flatlaf.icons.FlatFileViewComputerIcon());
        mLabel.setPadding(4,2,2,4);
        statusBar.add(mLabel);
        bottom.add(statusPanel);
    }


    private void buildRightBar() {
        JPanel panel = new JPanel();
        barPanels[RIGHT] = panel;
        panel.setBorder(new MLineBorder(Color.lightGray,1,true,false,false,false));
        GridLayout layout = new GridLayout(2,1);
        panel.setLayout(layout);
        toolBars[RIGHT][MODE_UNSPLIT] = new JPanel(){{setBorder(new EmptyBorder(0,0,0,0));}};
        toolBars[RIGHT][MODE_SPLIT] = new JPanel(){{setBorder(new EmptyBorder(0,0,0,0));}};

        toolBars[RIGHT][MODE_SPLIT].setLayout(new VerticalLayout(0,false));
        toolBars[RIGHT][MODE_UNSPLIT].setLayout(new VerticalLayout(0,true));

        panel.add(toolBars[RIGHT][MODE_UNSPLIT]);
        panel.add(toolBars[RIGHT][MODE_SPLIT]);


        add(panel,BorderLayout.EAST);
    }

    private void buildLeftBar() {
        JPanel panel = new JPanel(){};
        barPanels[LEFT] = panel;
        panel.setBorder(new MLineBorder(Color.lightGray,1,false,true,false,false));


        LayoutManager layout = new GridLayout(2,1);
        panel.setLayout(layout);

        toolBars[LEFT][MODE_UNSPLIT] = new JPanel(){{setBorder(new EmptyBorder(0,0,0,0));}};
        toolBars[LEFT][MODE_SPLIT] = new JPanel(){{setBorder(new EmptyBorder(0,0,0,0));}};

        toolBars[LEFT][MODE_SPLIT].setLayout(new VerticalLayout(0,false));
        toolBars[LEFT][MODE_UNSPLIT].setLayout(new VerticalLayout(0,true));

        panel.add(toolBars[LEFT][MODE_UNSPLIT]);
        panel.add(toolBars[LEFT][MODE_SPLIT]);

        add(panel,BorderLayout.WEST);
    }

    private void buildTopBar() {

        JPanel top = new JPanel();
        barPanels[TOP] = top;
        top.setBorder(new MLineBorder(Color.lightGray,1,false,false,false,false));

        top.setLayout(new VerticalLayout());
        add(top,BorderLayout.NORTH);

        JPanel panel = new JPanel();
        EmptyBorder border = new EmptyBorder(0,21,0,21);
        panel.setBorder(border);
        panel.setLayout(new GridLayout(1,2));
        toolBars[TOP][MODE_UNSPLIT] = new JPanel(){{setBorder(new EmptyBorder(0,0,0,0));}};
        toolBars[TOP][MODE_SPLIT] = new JPanel(){{setBorder(new EmptyBorder(0,0,0,0));}};

        toolBars[TOP][MODE_UNSPLIT].setLayout(new HorizontalLayout(0,true));
        toolBars[TOP][MODE_SPLIT].setLayout(new HorizontalLayout(0,false));

//        panel.add(null);

        panel.add(toolBars[TOP][MODE_UNSPLIT]);
        panel.add(toolBars[TOP][MODE_SPLIT]);


        JPanel statusPanel = new JPanel(){{setBorder(new EmptyBorder(0,0,0,0));}};
        toolbarPanel = statusPanel;
        statusPanel.setBorder(new MLineBorder(Color.lightGray,1,false,false,false,true));
        statusPanel.setLayout(new BorderLayout());
        JToolBar statusBar = new JToolBar();
        statusBar.setBorder(new EmptyBorder(0,0,0,0));
        statusBar.setFloatable(false);
        statusPanel.add(statusBar,BorderLayout.CENTER);
        MLabel mLabel = new MLabel();
        mLabel.setIcon(new com.formdev.flatlaf.icons.FlatFileViewComputerIcon());
        mLabel.setPadding(4,2,2,4);
        statusBar.add(mLabel);
        top.add(statusPanel);
        top.add(panel);

    }






    public void buildCollapsePanel(String title, Icon icon, JComponent container, int direction, boolean split, boolean isSelected){

        PartViewCollapseButton button = new PartViewCollapseButton();
        button.setView(container);
        container.putClientProperty("myController",button);
        button.setTitle(title);
        button.setDirection(direction);
        button.setIcon(icon);
        button.setSplit(split);


        button.setIJPanel(this);

        ((JComponent)toolBars[direction][split?MODE_SPLIT:MODE_UNSPLIT]).add(button);
        ((JComponent)toolBars[direction][split?MODE_SPLIT:MODE_UNSPLIT]).revalidate();
        ((JComponent)toolBars[direction][split?MODE_SPLIT:MODE_UNSPLIT]).invalidate();
        ((JComponent)toolBars[direction][split?MODE_SPLIT:MODE_UNSPLIT]).updateUI();
        button.setSelected(isSelected);


    }




    public void setSelectedExcept(JComponent toolBar, JToggleButton button, boolean isSelected){
        for(Component c: toolBar.getComponents()){
            if(c == button){
                continue;
            }
            if(c instanceof JToggleButton){
                ((JToggleButton) c).setSelected(isSelected);
            }
        }

    }

    public PartViewCollapseButton getActiveSplitView(JComponent toolBar){
        for(Component c: toolBar.getComponents()){
            if(c instanceof JToggleButton){
                if(((JToggleButton) c).isSelected()){
                    return (PartViewCollapseButton) c;
                }
            }
        }
        return null;
    }





    @Override
    public void mouseClicked(MouseEvent e) { }

    @Override
    public void mousePressed(MouseEvent e) {
        if(resizeDirection == 0){
            initD =dLeft;
            e.consume();
        }else if(resizeDirection == 1){
            initD = dRight;
            e.consume();
        }else if(resizeDirection == 2){
            initD = dBottom;
            e.consume();
        }else if(resizeDirection == 3){
            initD = dTop;
            e.consume();
        }else{
            initD = 0;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(resizing){
            resizing=false;
            e.consume();
            revalidate();

        }
    }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) {
        int dd = 10;
        if(resizing){
            if(resizeDirection==0){

                slidePanes[LEFT].setPreferredSize(new Dimension((int) e.getPoint().getX() -dd,slidePanes[LEFT].getHeight()));
                if(slidePanes[LEFT].getPreferredSize().width<=0){
                    slidePanes[LEFT].setPreferredSize(new Dimension(0,slidePanes[LEFT].getPreferredSize().height));
                }

            }else if(resizeDirection==1){
                slidePanes[RIGHT].setPreferredSize(new Dimension((int) (center.getWidth()-e.getPoint().getX()-dd),slidePanes[RIGHT].getHeight()));
                if(slidePanes[RIGHT].getPreferredSize().width<=0){
                    slidePanes[RIGHT].setPreferredSize(new Dimension(0,slidePanes[RIGHT].getPreferredSize().height));
                }
            }else if(resizeDirection==2){
                slidePanes[BOTTOM].setPreferredSize(new Dimension(slidePanes[BOTTOM].getWidth(), (int) (center.getHeight()-e.getPoint().getY()-dd)));
                if(slidePanes[BOTTOM].getPreferredSize().height<=0){
                    slidePanes[BOTTOM].setPreferredSize(new Dimension(slidePanes[BOTTOM].getPreferredSize().width,0));
                }
            }else if(resizeDirection==3){
                slidePanes[TOP].setPreferredSize(new Dimension(slidePanes[TOP].getPreferredSize().width,e.getPoint().y-dd));
                if(slidePanes[TOP].getPreferredSize().height<=0){
                    slidePanes[TOP].setPreferredSize(new Dimension(slidePanes[TOP].getPreferredSize().width,0));
                }
            }
            center.revalidate();
        }


    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(isDragButtonMode) return;
        if(resizeDirection == LEFT ){
            slidePanes[LEFT].setPreferredSize(new Dimension((int) e.getPoint().getX() -dLeft,slidePanes[LEFT].getHeight()));
            if(slidePanes[LEFT].getPreferredSize().width<=0){
                slidePanes[LEFT].setPreferredSize(new Dimension(0,slidePanes[LEFT].getPreferredSize().height));
            }
            resizing=true;
            center.revalidate();
            e.consume();
            PartViewCollapseButton button = getActiveSplitView((JComponent) toolBars[LEFT][MODE_UNSPLIT]);
            if(button!=null){
                button.setPartSize(slidePanes[LEFT].getPreferredSize());
            }
            button = getActiveSplitView((JComponent) toolBars[LEFT][MODE_SPLIT]);
            if(button!=null){
                button.setPartSize(slidePanes[LEFT].getPreferredSize());
            }

        }else if(resizeDirection == RIGHT){
            slidePanes[RIGHT].setPreferredSize(new Dimension((int) (center.getWidth()-e.getPoint().getX()-dRight),slidePanes[RIGHT].getHeight()));
            if(slidePanes[RIGHT].getPreferredSize().width<=0){
                slidePanes[RIGHT].setPreferredSize(new Dimension(0,slidePanes[RIGHT].getPreferredSize().height));
            }
            resizing=true;
            center.revalidate();
            e.consume();

            PartViewCollapseButton button = getActiveSplitView((JComponent) toolBars[RIGHT][MODE_SPLIT]);
            if(button!=null){
                button.setPartSize(slidePanes[RIGHT].getPreferredSize());
            }
            button = getActiveSplitView((JComponent) toolBars[RIGHT][MODE_UNSPLIT]);
            if(button!=null){
                button.setPartSize(slidePanes[RIGHT].getPreferredSize());
            }

        }else if(resizeDirection== BOTTOM){
            slidePanes[BOTTOM].setPreferredSize(new Dimension(slidePanes[BOTTOM].getWidth(), (int) (center.getHeight()-e.getPoint().getY()-dBottom)));
            if(slidePanes[BOTTOM].getPreferredSize().height<=0){
                slidePanes[BOTTOM].setPreferredSize(new Dimension(slidePanes[BOTTOM].getPreferredSize().width,0));
            }
            resizing=true;
            center.revalidate();
            e.consume();

            PartViewCollapseButton button = getActiveSplitView((JComponent) toolBars[BOTTOM][MODE_SPLIT]);
            if(button!=null){
                button.setPartSize(slidePanes[BOTTOM].getPreferredSize());
            }
            button = getActiveSplitView((JComponent) toolBars[BOTTOM][MODE_UNSPLIT]);
            if(button!=null){
                button.setPartSize(slidePanes[BOTTOM].getPreferredSize());
            }

        }else if(resizeDirection==TOP){
            slidePanes[TOP].setPreferredSize(new Dimension(slidePanes[TOP].getWidth(), (int) (e.getPoint().y)));
            if(slidePanes[TOP].getPreferredSize().height<=0){
                slidePanes[TOP].setPreferredSize(new Dimension(slidePanes[TOP].getPreferredSize().width,0));
            }
            resizing=true;
            center.revalidate();
            e.consume();

            PartViewCollapseButton button = getActiveSplitView((JComponent) toolBars[TOP][MODE_SPLIT]);
            if(button!=null){
                button.setPartSize(slidePanes[TOP].getPreferredSize());
            }
            button = getActiveSplitView((JComponent) toolBars[TOP][MODE_UNSPLIT]);
            if(button!=null){
                button.setPartSize(slidePanes[TOP].getPreferredSize());
            }

        }

        else{
            resizing = false;
            resizeDirection = -1;
        }





    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(isDragButtonMode) return;
        if(!resizing){
            currentPoint = e.getPoint();
            dLeft = (int) (currentPoint.getX()-slidePanes[LEFT].getWidth());
            dRight = (int) (center.getWidth() - slidePanes[RIGHT].getWidth()-currentPoint.getX());
            dBottom = (int) (center.getHeight() - slidePanes[BOTTOM].getHeight() - currentPoint.getY());
            dTop = currentPoint.y - slidePanes[TOP].getHeight();

//            System.out.println(dLeft+" "+dRight+" "+dBottom+" "+dTop);
            if((dLeft<splitterSize)&&(dLeft>(-splitterSize))){
                rootPane.getGlassPane().setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
                resizeDirection = 0;
                e.consume();
            }else if((dRight<splitterSize)&&(dRight>(-splitterSize))){
                rootPane.getGlassPane().setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
                resizeDirection = 1;
                e.consume();
            }else if((dBottom<splitterSize)&&(dBottom>(-splitterSize))){
                rootPane.getGlassPane().setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
                resizeDirection = 2;
                e.consume();

            }else if((dTop<splitterSize)&&(dTop>(-splitterSize))){
                rootPane.getGlassPane().setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
                resizeDirection = 3;
                e.consume();
            }
            else{
                resizeDirection = -1;

                rootPane.getGlassPane().setCursor(Cursor.getDefaultCursor());


            }
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {}

    public JPanel getCenter() {
        return center;
    }

    public List<PartViewCollapseButton> getPartViewCollapseButton(int direction,int split){
        Component[] c = toolBars[direction][split].getComponents();
        List<PartViewCollapseButton> list = new ArrayList<>();
        for(Component cc: c){
            if(cc instanceof PartViewCollapseButton){
                list.add((PartViewCollapseButton) cc);
            }
        }
        return list;
    }

    public List<PartViewCollapseButton> getPartViewCollapseButton(int direction){
        List l1 = getPartViewCollapseButton(direction,MODE_UNSPLIT);
        List l2 = getPartViewCollapseButton(direction,MODE_SPLIT);
        l1.addAll(l2);
        return l1;
    }
}
