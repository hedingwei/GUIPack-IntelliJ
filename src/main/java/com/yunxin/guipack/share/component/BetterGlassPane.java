package com.yunxin.guipack.share.component;
import org.jdesktop.swingx.JXPanel;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.*;

import static java.awt.AWTEvent.MOUSE_EVENT_MASK;
import static java.awt.AWTEvent.MOUSE_MOTION_EVENT_MASK;
import static java.awt.AWTEvent.MOUSE_WHEEL_EVENT_MASK;
import static java.awt.event.MouseEvent.*;
import static javax.swing.SwingUtilities.*;

public class BetterGlassPane extends JXPanel implements AWTEventListener {
    private static final long serialVersionUID = 1l;

    private JRootPane rootPane;
    private EventListenerList listeners = new EventListenerList();

    public BetterGlassPane() {
        Toolkit.getDefaultToolkit().addAWTEventListener(this,
                MOUSE_WHEEL_EVENT_MASK|MOUSE_MOTION_EVENT_MASK|MOUSE_EVENT_MASK);
    }
    public BetterGlassPane(JRootPane rootPane) {
        this(); (this.rootPane=rootPane).setGlassPane(this);
        setOpaque(false);
        setVisible(true);
        setLayout(null);



    }



    public void setRootPane(JRootPane pane) { this.rootPane = pane; }

    @Override public synchronized MouseListener[] getMouseListeners() { return listeners.getListeners(MouseListener.class); }
    @Override public synchronized void addMouseListener(MouseListener listener) { listeners.add(MouseListener.class,listener); }
    @Override public synchronized void removeMouseListener(MouseListener listener) { listeners.remove(MouseListener.class,listener); }

    @Override public synchronized MouseMotionListener[] getMouseMotionListeners() { return listeners.getListeners(MouseMotionListener.class); }
    @Override public synchronized void addMouseMotionListener(MouseMotionListener listener) { listeners.add(MouseMotionListener.class,listener); }
    @Override public synchronized void removeMouseMotionListener(MouseMotionListener listener) { listeners.remove(MouseMotionListener.class,listener); }

    @Override public synchronized MouseWheelListener[] getMouseWheelListeners() { return listeners.getListeners(MouseWheelListener.class); }
    @Override public synchronized void addMouseWheelListener(MouseWheelListener listener) { listeners.add(MouseWheelListener.class,listener); }
    @Override public synchronized void removeMouseWheelListener(MouseWheelListener listener) { listeners.remove(MouseWheelListener.class,listener); }

    public void eventDispatched(AWTEvent event) {
        if(rootPane!=null&&event instanceof MouseEvent) {
            MouseEvent mouseEvent = (MouseEvent)event, newMouseEvent;

            Object source = event.getSource();

            if(source instanceof Component) {
                Component sourceComponent = (Component)source;

                if(SwingUtilities.getRootPane(sourceComponent)!=rootPane){
                    return; //it's not our root pane (e.g. different window)
                }

                newMouseEvent = convertMouseEvent(sourceComponent, mouseEvent, this);
            } else newMouseEvent = convertMouseEvent(null, mouseEvent, this);

            switch(event.getID()) {
                case MOUSE_CLICKED:
                    for(MouseListener listener:listeners.getListeners(MouseListener.class))
                        listener.mouseClicked(newMouseEvent);
                    break;
                case MOUSE_PRESSED:
                    for(MouseListener listener:listeners.getListeners(MouseListener.class))
                        listener.mousePressed(newMouseEvent);
                    break;
                case MOUSE_RELEASED:
                    for(MouseListener listener:listeners.getListeners(MouseListener.class))
                        listener.mouseReleased(newMouseEvent);
                    break;
                case MOUSE_MOVED:
                    for(MouseMotionListener listener:listeners.getListeners(MouseMotionListener.class))
                        listener.mouseMoved(newMouseEvent);
                    break;
                case MOUSE_ENTERED:
                    for(MouseListener listener:listeners.getListeners(MouseListener.class))
                        listener.mouseEntered(newMouseEvent);
                    break;
                case MOUSE_EXITED:
                    for(MouseListener listener:listeners.getListeners(MouseListener.class))
                        listener.mouseExited(newMouseEvent);
                    break;
                case MOUSE_DRAGGED:
                    for(MouseMotionListener listener:listeners.getListeners(MouseMotionListener.class))
                        listener.mouseDragged(newMouseEvent);
                    break;
                case MOUSE_WHEEL:
                    for(MouseWheelListener listener:listeners.getListeners(MouseWheelListener.class))
                        listener.mouseWheelMoved((MouseWheelEvent)newMouseEvent);
                    break;
            }

            /* consume the original mouse event, if the new mouse event was consumed */
            if(newMouseEvent.isConsumed())
                mouseEvent.consume();
        }
    }


    /**
     * If someone sets a new cursor to the GlassPane
     * we expect that he knows what he is doing
     * and return the super.contains(x,y)
     * otherwise we return false to respect the cursors
     * for the underneath components
     */
    public boolean contains(int x, int y) {
        Container container = rootPane.getContentPane();
        Point containerPoint = convertPoint(this, x, y, container);
        if(containerPoint.y>0) {
            Component component = getDeepestComponentAt(
                    container, containerPoint.x, containerPoint.y);
            return component==null||component.getCursor()==Cursor.getDefaultCursor();
        } else return true;
    }
}