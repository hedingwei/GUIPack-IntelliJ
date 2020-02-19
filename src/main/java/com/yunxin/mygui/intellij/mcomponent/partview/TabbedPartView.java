package com.yunxin.mygui.intellij.mcomponent.partview;

import com.jidesoft.swing.JideTabbedPane;
import com.jidesoft.swing.StringConverter;
import com.jidesoft.swing.TabEditingListener;
import com.jidesoft.swing.TabEditingValidator;
import com.yunxin.mygui.share.component.MLabel;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.TabbedPaneUI;
import java.awt.*;
import java.awt.event.HierarchyBoundsAdapter;
import java.awt.event.HierarchyEvent;
import java.awt.event.MouseEvent;
import java.beans.Transient;

public class TabbedPartView extends AbstractPartView {

    private JToolBar leadingBar;
    private JToolBar trailingBar;
    private JideTabbedPane tabbedPane;
    private MLabel titleLabel;


    public TabbedPartView() {
    }

    public TabbedPartView(String title, Icon icon){
        setText(title);
    }

    @Override
    protected void preInit() {
        titleLabel = new MLabel();
        leadingBar = new JToolBar();
        leadingBar.setFloatable(false);
        trailingBar = new JToolBar();
        trailingBar.setFloatable(false);
        tabbedPane = new JideTabbedPane();
        tabbedPane.setFont(IntelliJPanel.defaultFont);
        tabbedPane.setShowIconsOnTab(true);
        tabbedPane.setTabAreaInsets(new Insets(0,0,0,0));
        tabbedPane.addHierarchyBoundsListener(new HierarchyBoundsAdapter() {
            @Override
            public void ancestorResized(HierarchyEvent e) {
                if(e.getComponent() == tabbedPane) {
                    tabbedPane.getTabLeadingComponent().setPreferredSize(new Dimension((int) tabbedPane.getTabLeadingComponent().getPreferredSize().getWidth(), tabbedPane.getTabHeight()));
                    tabbedPane.getTabTrailingComponent().setPreferredSize(new Dimension((int) tabbedPane.getTabTrailingComponent().getPreferredSize().getWidth(), tabbedPane.getTabHeight()));

                }
            }
        });

        tabbedPane.setTabLeadingComponent(leadingBar);
        tabbedPane.setTabTrailingComponent(trailingBar);


    }

    @Override
    public void setupViewController(IPartViewController controller) {

    }

    @Override
    protected void setupTitleLeadingPart(JToolBar toolBar) {
        setPartContent(tabbedPane);
        toolBar.add(titleLabel);
    }

    @Override
    protected void setupTitleTrailingPart(JToolBar toolBar) {
    }

    @Override
    protected JToolBar buildTitleTrailingPart() {
        return trailingBar;
    }

    @Override
    protected JToolBar buildTitleLeadingPart() {
        return leadingBar;
    }

    public void setText(String title){
        this.titleLabel.setText(title);
    }

    public void setUI(TabbedPaneUI ui) {
        tabbedPane.setUI(ui);
    }

    public boolean isHideOneTab() {
        return tabbedPane.isHideOneTab();
    }

    public void setHideOneTab(boolean hideOne) {
        tabbedPane.setHideOneTab(hideOne);
    }

    public boolean isTabShown() {
        return tabbedPane.isTabShown();
    }

    public boolean isShowTabButtons() {
        return tabbedPane.isShowTabButtons();
    }

    public void setShowTabButtons(boolean showButtons) {
        tabbedPane.setShowTabButtons(showButtons);
    }

    public void setCloseAction(Action action) {
        tabbedPane.setCloseAction(action);
    }

    public Action getCloseAction() {
        return tabbedPane.getCloseAction();
    }

    public void setAutoFocusOnTabHideClose(boolean autoFocusonTabHideClose) {
        tabbedPane.setAutoFocusOnTabHideClose(autoFocusonTabHideClose);
    }

    public boolean isAutoFocusOnTabHideClose() {
        return tabbedPane.isAutoFocusOnTabHideClose();
    }

    public void resetDefaultCloseAction() {
        tabbedPane.resetDefaultCloseAction();
    }

    public void setSuppressStateChangedEvents(boolean suppress) {
        tabbedPane.setSuppressStateChangedEvents(suppress);
    }

    public boolean isSuppressStateChangedEvents() {
        return tabbedPane.isSuppressStateChangedEvents();
    }

    public boolean isSuppressSetSelectedIndex() {
        return tabbedPane.isSuppressSetSelectedIndex();
    }

    public void setSuppressSetSelectedIndex(boolean suppressSetSelectedIndex) {
        tabbedPane.setSuppressSetSelectedIndex(suppressSetSelectedIndex);
    }

    public void setSelectedIndex(int index) {
        tabbedPane.setSelectedIndex(index);
    }

    public void popupSelectedIndex(int index) {
        tabbedPane.popupSelectedIndex(index);
    }

    public void setComponentAt(int index, Component c) {
        tabbedPane.setComponentAt(index, c);
    }

    public boolean isAutoRequestFocus() {
        return tabbedPane.isAutoRequestFocus();
    }

    public void setAutoRequestFocus(boolean autoRequestFocus) {
        tabbedPane.setAutoRequestFocus(autoRequestFocus);
    }

    public void moveSelectedTabTo(int tabIndex) {
        tabbedPane.moveSelectedTabTo(tabIndex);
    }

    public boolean requestFocusForVisibleComponent() {
        return tabbedPane.requestFocusForVisibleComponent();
    }

    public boolean isHideTrailingWhileNoButtons() {
        return tabbedPane.isHideTrailingWhileNoButtons();
    }

    public void setHideTrailingWhileNoButtons(boolean hideTrailingWhileNoButtons) {
        tabbedPane.setHideTrailingWhileNoButtons(hideTrailingWhileNoButtons);
    }

    public boolean isLayoutTrailingComponentBeforeButtons() {
        return tabbedPane.isLayoutTrailingComponentBeforeButtons();
    }

    public void setLayoutTrailingComponentBeforeButtons(boolean layoutTrailingComponentBeforeButtons) {
        tabbedPane.setLayoutTrailingComponentBeforeButtons(layoutTrailingComponentBeforeButtons);
    }

    public void processMouseSelection(int tabIndex, MouseEvent e) {
        tabbedPane.processMouseSelection(tabIndex, e);
    }

    public int getTabHeight() {
        return tabbedPane.getTabHeight();
    }

    public boolean isRightClickSelect() {
        return tabbedPane.isRightClickSelect();
    }

    public void setRightClickSelect(boolean rightClickSelect) {
        tabbedPane.setRightClickSelect(rightClickSelect);
    }

    public int getTabAtLocation(int x, int y) {
        return tabbedPane.getTabAtLocation(x, y);
    }

    public boolean isShowGripper() {
        return tabbedPane.isShowGripper();
    }

    public void setShowGripper(boolean showGripper) {
        tabbedPane.setShowGripper(showGripper);
    }

    public boolean isShowIconsOnTab() {
        return tabbedPane.isShowIconsOnTab();
    }

    public void setShowIconsOnTab(boolean showIconsOnTab) {
        tabbedPane.setShowIconsOnTab(showIconsOnTab);
    }

    public boolean isUseDefaultShowIconsOnTab() {
        return tabbedPane.isUseDefaultShowIconsOnTab();
    }

    public void setUseDefaultShowIconsOnTab(boolean useDefaultShowIconsOnTab) {
        tabbedPane.setUseDefaultShowIconsOnTab(useDefaultShowIconsOnTab);
    }

    public boolean isShowCloseButtonOnTab() {
        return tabbedPane.isShowCloseButtonOnTab();
    }

    public void setShowCloseButtonOnTab(boolean showCloseButtonOnTab) {
        tabbedPane.setShowCloseButtonOnTab(showCloseButtonOnTab);
    }

    public boolean isUseDefaultShowCloseButtonOnTab() {
        return tabbedPane.isUseDefaultShowCloseButtonOnTab();
    }

    public void setUseDefaultShowCloseButtonOnTab(boolean useDefaultShowCloseButtonOnTab) {
        tabbedPane.setUseDefaultShowCloseButtonOnTab(useDefaultShowCloseButtonOnTab);
    }

    public void setTabEditingAllowed(boolean allowed) {
        tabbedPane.setTabEditingAllowed(allowed);
    }

    public boolean isTabEditingAllowed() {
        return tabbedPane.isTabEditingAllowed();
    }

    public void setTabEditingValidator(TabEditingValidator tabEditValidator) {
        tabbedPane.setTabEditingValidator(tabEditValidator);
    }

    public TabEditingValidator getTabEditingValidator() {
        return tabbedPane.getTabEditingValidator();
    }

    public boolean isShowCloseButton() {
        return tabbedPane.isShowCloseButton();
    }

    public void setShowCloseButton(boolean showCloseButton) {
        tabbedPane.setShowCloseButton(showCloseButton);
    }

    public boolean isShowTabArea() {
        return tabbedPane.isShowTabArea();
    }

    public void setShowTabArea(boolean showTabArea) {
        tabbedPane.setShowTabArea(showTabArea);
    }

    public boolean isShowTabContent() {
        return tabbedPane.isShowTabContent();
    }

    public void setShowTabContent(boolean showTabContent) {
        tabbedPane.setShowTabContent(showTabContent);
    }

    public StringConverter getStringConverter() {
        return tabbedPane.getStringConverter();
    }

    public void setStringConverter(StringConverter stringConverter) {
        tabbedPane.setStringConverter(stringConverter);
    }

    public String getDisplayTitleAt(int index) {
        return tabbedPane.getDisplayTitleAt(index);
    }

    public boolean isBoldActiveTab() {
        return tabbedPane.isBoldActiveTab();
    }

    public void setBoldActiveTab(boolean boldActiveTab) {
        tabbedPane.setBoldActiveTab(boldActiveTab);
    }

    public void removeTabAt(int index) {
        tabbedPane.removeTabAt(index);
    }

    public boolean isTabClosableAt(int tabIndex) {
        return tabbedPane.isTabClosableAt(tabIndex);
    }

    public void setTabClosableAt(int tabIndex, boolean closable) {
        tabbedPane.setTabClosableAt(tabIndex, closable);
    }

    public Component getLastFocusedComponent(Component pageComponent) {
        return tabbedPane.getLastFocusedComponent(pageComponent);
    }

    public void insertTab(String title, Icon icon, Component component, String tip, int index) {
        tabbedPane.insertTab(title, icon, component, tip, index);
    }

    public Font getSelectedTabFont() {
        return tabbedPane.getSelectedTabFont();
    }

    public void setSelectedTabFont(Font selectedTabFont) {
        tabbedPane.setSelectedTabFont(selectedTabFont);
    }

    public int getColorTheme() {
        return tabbedPane.getColorTheme();
    }

    public int getDefaultColorTheme() {
        return tabbedPane.getDefaultColorTheme();
    }

    public void setColorTheme(int colorTheme) {
        tabbedPane.setColorTheme(colorTheme);
    }

    public int getTabResizeMode() {
        return tabbedPane.getTabResizeMode();
    }

    public void setTabResizeMode(int resizeMode) {
        tabbedPane.setTabResizeMode(resizeMode);
    }

    public int getDefaultTabResizeMode() {
        return tabbedPane.getDefaultTabResizeMode();
    }

    public int getTabShape() {
        return tabbedPane.getTabShape();
    }

    public int getDefaultTabStyle() {
        return tabbedPane.getDefaultTabStyle();
    }

    public void setTabShape(int tabShape) {
        tabbedPane.setTabShape(tabShape);
    }

    public void setTabLeadingComponent(Component component) {
        tabbedPane.setTabLeadingComponent(component);
    }

    public Component getTabLeadingComponent() {
        return tabbedPane.getTabLeadingComponent();
    }

    public void setTabTrailingComponent(Component component) {
        tabbedPane.setTabTrailingComponent(component);
    }

    public Component getTabTrailingComponent() {
        return tabbedPane.getTabTrailingComponent();
    }

    public boolean isShowCloseButtonOnSelectedTab() {
        return tabbedPane.isShowCloseButtonOnSelectedTab();
    }

    public void setShowCloseButtonOnSelectedTab(boolean i) {
        tabbedPane.setShowCloseButtonOnSelectedTab(i);
    }

    public boolean isShowCloseButtonOnMouseOver() {
        return tabbedPane.isShowCloseButtonOnMouseOver();
    }

    public void setShowCloseButtonOnMouseOver(boolean showCloseButtonOnMouseOverOnly) {
        tabbedPane.setShowCloseButtonOnMouseOver(showCloseButtonOnMouseOverOnly);
    }

    public JideTabbedPane.ColorProvider getTabColorProvider() {
        return tabbedPane.getTabColorProvider();
    }

    public void setTabColorProvider(JideTabbedPane.ColorProvider tabColorProvider) {
        tabbedPane.setTabColorProvider(tabColorProvider);
    }

    public void editTabAt(int tabIndex) {
        tabbedPane.editTabAt(tabIndex);
    }

    public boolean isTabEditing() {
        return tabbedPane.isTabEditing();
    }

    public void stopTabEditing() {
        tabbedPane.stopTabEditing();
    }

    public void cancelTabEditing() {
        tabbedPane.cancelTabEditing();
    }

    public int getEditingTabIndex() {
        return tabbedPane.getEditingTabIndex();
    }

    public void repaintTabAreaAndContentBorder() {
        tabbedPane.repaintTabAreaAndContentBorder();
    }

    public ListCellRenderer getTabListCellRenderer() {
        return tabbedPane.getTabListCellRenderer();
    }

    public void setTabListCellRenderer(ListCellRenderer tabListCellRenderer) {
        tabbedPane.setTabListCellRenderer(tabListCellRenderer);
    }

    public boolean hasFocusComponent() {
        return tabbedPane.hasFocusComponent();
    }

    public Insets getContentBorderInsets() {
        return tabbedPane.getContentBorderInsets();
    }

    public void setContentBorderInsets(Insets contentBorderInsets) {
        tabbedPane.setContentBorderInsets(contentBorderInsets);
    }

    public Insets getTabAreaInsets() {
        return tabbedPane.getTabAreaInsets();
    }

    public void setTabAreaInsets(Insets tabAreaInsets) {
        tabbedPane.setTabAreaInsets(tabAreaInsets);
    }

    public Insets getTabInsets() {
        return tabbedPane.getTabInsets();
    }

    public void setTabInsets(Insets tabInsets) {
        tabbedPane.setTabInsets(tabInsets);
    }

    public boolean isDragOverDisabled() {
        return tabbedPane.isDragOverDisabled();
    }

    public void setDragOverDisabled(boolean dragOverDisabled) {
        tabbedPane.setDragOverDisabled(dragOverDisabled);
    }

    public void scrollSelectedTabToVisible(boolean scrollLeft) {
        tabbedPane.scrollSelectedTabToVisible(scrollLeft);
    }

    public void addTabEditingListener(TabEditingListener l) {
        tabbedPane.addTabEditingListener(l);
    }

    public void removeTabEditingListener(TabEditingListener l) {
        tabbedPane.removeTabEditingListener(l);
    }

    public TabEditingListener[] getTabEditingListeners() {
        return tabbedPane.getTabEditingListeners();
    }

    public Icon getIconForTab(int tabIndex) {
        return tabbedPane.getIconForTab(tabIndex);
    }

    public boolean isScrollSelectedTabOnWheel() {
        return tabbedPane.isScrollSelectedTabOnWheel();
    }

    public void setScrollSelectedTabOnWheel(boolean scrollSelectedTabOnWheel) {
        tabbedPane.setScrollSelectedTabOnWheel(scrollSelectedTabOnWheel);
    }

    public boolean isCloseTabOnMouseMiddleButton() {
        return tabbedPane.isCloseTabOnMouseMiddleButton();
    }

    public void setCloseTabOnMouseMiddleButton(boolean closeTabOnMouseMiddleButton) {
        tabbedPane.setCloseTabOnMouseMiddleButton(closeTabOnMouseMiddleButton);
    }

    public int getTabAlignment() {
        return tabbedPane.getTabAlignment();
    }

    public void setTabAlignment(int tabAlignment) {
        tabbedPane.setTabAlignment(tabAlignment);
    }

    public String getResourceString(String key) {
        return tabbedPane.getResourceString(key);
    }

    public boolean isTabListPopupVisible() {
        return tabbedPane.isTabListPopupVisible();
    }

    public void hideTabListPopup() {
        tabbedPane.hideTabListPopup();
    }

    public void showTabListPopup(JButton listButton) {
        tabbedPane.showTabListPopup(listButton);
    }

    public JideTabbedPane.NoFocusButton createNoFocusButton(int type) {
        return tabbedPane.createNoFocusButton(type);
    }

    public void addChangeListener(ChangeListener l) {
        tabbedPane.addChangeListener(l);
    }

    public void removeChangeListener(ChangeListener l) {
        tabbedPane.removeChangeListener(l);
    }

    public ChangeListener[] getChangeListeners() {
        return tabbedPane.getChangeListeners();
    }

    public SingleSelectionModel getModel() {
        return tabbedPane.getModel();
    }

    public void setModel(SingleSelectionModel model) {
        tabbedPane.setModel(model);
    }

    public int getTabPlacement() {
        return tabbedPane.getTabPlacement();
    }

    public void setTabPlacement(int tabPlacement) {
        tabbedPane.setTabPlacement(tabPlacement);
    }

    public int getTabLayoutPolicy() {
        return tabbedPane.getTabLayoutPolicy();
    }

    public void setTabLayoutPolicy(int tabLayoutPolicy) {
        tabbedPane.setTabLayoutPolicy(tabLayoutPolicy);
    }

    @Transient
    public int getSelectedIndex() {
        return tabbedPane.getSelectedIndex();
    }

    @Transient
    public Component getSelectedComponent() {
        return tabbedPane.getSelectedComponent();
    }

    public void setSelectedComponent(Component c) {
        tabbedPane.setSelectedComponent(c);
    }

    public void addTab(String title, Icon icon, Component component, String tip) {
        tabbedPane.addTab(title, icon, component, tip);
    }

    public void addTab(String title, Icon icon, Component component) {
        tabbedPane.addTab(title, icon, component);
    }

    public void addTab(String title, Component component) {
        tabbedPane.addTab(title, component);
    }

    public int getTabCount() {
        return tabbedPane.getTabCount();
    }

    public int getTabRunCount() {
        return tabbedPane.getTabRunCount();
    }

    public String getTitleAt(int index) {
        return tabbedPane.getTitleAt(index);
    }

    public Icon getIconAt(int index) {
        return tabbedPane.getIconAt(index);
    }

    public Icon getDisabledIconAt(int index) {
        return tabbedPane.getDisabledIconAt(index);
    }

    public String getToolTipTextAt(int index) {
        return tabbedPane.getToolTipTextAt(index);
    }

    public Color getBackgroundAt(int index) {
        return tabbedPane.getBackgroundAt(index);
    }

    public Color getForegroundAt(int index) {
        return tabbedPane.getForegroundAt(index);
    }

    public boolean isEnabledAt(int index) {
        return tabbedPane.isEnabledAt(index);
    }

    public Component getComponentAt(int index) {
        return tabbedPane.getComponentAt(index);
    }

    public int getMnemonicAt(int tabIndex) {
        return tabbedPane.getMnemonicAt(tabIndex);
    }

    public int getDisplayedMnemonicIndexAt(int tabIndex) {
        return tabbedPane.getDisplayedMnemonicIndexAt(tabIndex);
    }

    public Rectangle getBoundsAt(int index) {
        return tabbedPane.getBoundsAt(index);
    }

    public void setTitleAt(int index, String title) {
        tabbedPane.setTitleAt(index, title);
    }

    public void setIconAt(int index, Icon icon) {
        tabbedPane.setIconAt(index, icon);
    }

    public void setDisabledIconAt(int index, Icon disabledIcon) {
        tabbedPane.setDisabledIconAt(index, disabledIcon);
    }

    public void setToolTipTextAt(int index, String toolTipText) {
        tabbedPane.setToolTipTextAt(index, toolTipText);
    }

    public void setBackgroundAt(int index, Color background) {
        tabbedPane.setBackgroundAt(index, background);
    }

    public void setForegroundAt(int index, Color foreground) {
        tabbedPane.setForegroundAt(index, foreground);
    }

    public void setEnabledAt(int index, boolean enabled) {
        tabbedPane.setEnabledAt(index, enabled);
    }

    public void setDisplayedMnemonicIndexAt(int tabIndex, int mnemonicIndex) {
        tabbedPane.setDisplayedMnemonicIndexAt(tabIndex, mnemonicIndex);
    }

    public void setMnemonicAt(int tabIndex, int mnemonic) {
        tabbedPane.setMnemonicAt(tabIndex, mnemonic);
    }

    public int indexOfTab(String title) {
        return tabbedPane.indexOfTab(title);
    }

    public int indexOfTab(Icon icon) {
        return tabbedPane.indexOfTab(icon);
    }

    public int indexOfComponent(Component component) {
        return tabbedPane.indexOfComponent(component);
    }

    public int indexAtLocation(int x, int y) {
        return tabbedPane.indexAtLocation(x, y);
    }

    public void setTabComponentAt(int index, Component component) {
        tabbedPane.setTabComponentAt(index, component);
    }

    public Component getTabComponentAt(int index) {
        return tabbedPane.getTabComponentAt(index);
    }

    public int indexOfTabComponent(Component tabComponent) {
        return tabbedPane.indexOfTabComponent(tabComponent);
    }

    @Override
    public void setTitle(String title) {
        setText(title);
    }

    public JideTabbedPane getTabbedPane() {
        return tabbedPane;
    }
}
