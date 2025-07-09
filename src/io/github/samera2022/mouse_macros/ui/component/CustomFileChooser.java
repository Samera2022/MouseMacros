package io.github.samera2022.mouse_macros.ui.component;

import io.github.samera2022.mouse_macros.constant.ColorConsts;
import io.github.samera2022.mouse_macros.constant.OtherConsts;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static io.github.samera2022.mouse_macros.constant.ColorConsts.*;

public class CustomFileChooser extends JFileChooser {
    private int mode;
    private Color bgColor;
    private Color fgColor;
    private Color panelBgColor;
    private Color panelFgColor;
    private Color buttonBgColor;
    private Color buttonFgColor;
    private Color caretColor;
    private Color listBgColor;
    private Color listFgColor;
    private Color listSelectionBgColor;
    private Color listSelectionFgColor;
    private Color panelBorderColor;

    private boolean uiInitialized = false;

    public CustomFileChooser(int mode) {
        super(FileSystemView.getFileSystemView().getHomeDirectory());
        this.mode = mode;
        applyColorScheme();
        setAcceptAllFileFilterUsed(false);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        if (!uiInitialized) {
            customizeUI();
            uiInitialized = true;
        }
    }

    @Override
    public void addChoosableFileFilter(FileFilter filter) {
        // 防止添加"所有文件"过滤器
        if (!filter.equals(getAcceptAllFileFilter())) {
            super.addChoosableFileFilter(filter);
        }
    }

    @Override
    public void resetChoosableFileFilters() {
        // 获取当前选择的过滤器（如果有）
        FileFilter currentFilter = getFileFilter();

        // 清空所有过滤器
        super.resetChoosableFileFilters();

        // 重新添加除"所有文件"外的过滤器
        for (FileFilter filter : getChoosableFileFilters()) {
            if (!filter.equals(getAcceptAllFileFilter())) {
                super.addChoosableFileFilter(filter);
            }
        }

        // 恢复之前选择的过滤器
        if (currentFilter != null && !currentFilter.equals(getAcceptAllFileFilter())) {
            setFileFilter(currentFilter);
        }
    }


    private void applyColorScheme() {
        switch (mode) {
            case OtherConsts.DARK_MODE:
                bgColor = DARK_MODE_BACKGROUND;
                fgColor = DARK_MODE_FOREGROUND;
                panelBgColor = DARK_MODE_PANEL_BACKGROUND;
                panelFgColor = DARK_MODE_PANEL_FOREGROUND;
                buttonBgColor = DARK_MODE_BUTTON_BACKGROUND;
                buttonFgColor = DARK_MODE_BUTTON_FOREGROUND;
                caretColor = DARK_MODE_CARET;
                listBgColor = DARK_MODE_LIST_BACKGROUND;
                listFgColor = DARK_MODE_LIST_FOREGROUND;
                listSelectionBgColor = DARK_MODE_LIST_SELECTION_BG;
                listSelectionFgColor = DARK_MODE_LIST_SELECTION_FG;
                panelBorderColor = DARK_MODE_PANEL_BORDER;
                break;
            case OtherConsts.LIGHT_MODE:
                bgColor = LIGHT_MODE_BACKGROUND;
                fgColor = LIGHT_MODE_FOREGROUND;
                panelBgColor = LIGHT_MODE_PANEL_BACKGROUND;
                panelFgColor = LIGHT_MODE_PANEL_FOREGROUND;
                buttonBgColor = LIGHT_MODE_BUTTON_BACKGROUND;
                buttonFgColor = LIGHT_MODE_BUTTON_FOREGROUND;
                caretColor = LIGHT_MODE_CARET;
                listBgColor = LIGHT_MODE_LIST_BACKGROUND;
                listFgColor = LIGHT_MODE_LIST_FOREGROUND;
                listSelectionBgColor = LIGHT_MODE_LIST_SELECTION_BG;
                listSelectionFgColor = LIGHT_MODE_LIST_SELECTION_FG;
                panelBorderColor = LIGHT_MODE_PANEL_BORDER;
                break;
        }
    }

    private <T extends Component> T findComponent(Container parent, Class<T> type) {
        for (Component comp : parent.getComponents()) {
            if (type.isInstance(comp)) {
                return type.cast(comp);
            }
            if (comp instanceof Container) {
                T result = findComponent((Container) comp, type);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    private <T extends Component> List<T> findComponents(Class<T> type) {
        return findComponents(this, type, new ArrayList<>());
    }

    private <T extends Component> List<T> findComponents(Container parent, Class<T> type, List<T> result) {
        for (Component comp : parent.getComponents()) {
            if (type.isInstance(comp)) {
                result.add(type.cast(comp));
            }
            if (comp instanceof Container) {
                findComponents((Container) comp, type, result);
            }
        }
        return result;
    }

    private void customizeUI() {
        // 设置文件选择器整体样式
        setBackground(bgColor);
        setForeground(fgColor);
        setBorder(new EmptyBorder(10, 10, 10, 10));
        customizeButtons();
        customizeFileList();
        customizeTextField();
        customizeFilterComboBox();
        customizeDirectoryComboBox();
        customizeToolBar();
        customizeSeparators();
        customizePanels();
        customizeContainer();
    }
    private void customizeContainer(){
        Component[] components = getComponents();
        Container comp = (Container) components[0];
        comp.setBackground(DARK_MODE_BACKGROUND);
        if (components.length>1)
            for (int i = 1; i<components.length; i++){
                Component c = components[i];
                if (c instanceof JPanel) {
                    c.setBackground(DARK_MODE_BACKGROUND);
                } else if (c instanceof JLabel) {
                    c.setBackground(DARK_MODE_BACKGROUND);
                }
            }
    }

    private void customizeButtons() {
        // 使用改进的查找方法获取所有按钮
        List<JButton> buttons = findComponents(JButton.class);
        for (JButton button : buttons) {
            // 跳过下拉按钮（会在combobox部分单独处理）
            if (button.getParent() instanceof JComboBox) continue;

            button.setBackground(buttonBgColor);
            button.setForeground(buttonFgColor);
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(buttonBgColor.darker(), 1),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));

            // 添加鼠标悬停效果
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(buttonBgColor.brighter());
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    button.setBackground(buttonBgColor);
                }
            });
        }
    }

    private void customizePanels() {
        // 查找所有JPanel组件
        List<JPanel> panels = findComponents(JPanel.class);
        for (JPanel panel : panels) {
            // 设置面板背景色
            panel.setBackground(panelBgColor);

            // 设置面板边框（统一风格）
            panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(panelBorderColor, 1),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
        }
    }

    private void customizeFileList() {
        // 查找所有JList组件
        JList<?> fileList = findComponent(this, JList.class);

        if (fileList != null) {
            fileList.setBorder(BorderFactory.createLineBorder(panelBorderColor, 1));
            // 设置列表样式
            fileList.setBackground(listBgColor);
            fileList.setForeground(listFgColor);
            fileList.setSelectionBackground(listSelectionBgColor);
            fileList.setSelectionForeground(listSelectionFgColor);
            fileList.setCellRenderer(new FileListRenderer());

            // 改进的滚动条查找方式
            Container parent = fileList.getParent();
            if (parent instanceof JViewport) {
                Container grandParent = parent.getParent();
                if (grandParent instanceof JScrollPane) {
                    JScrollPane scrollPane = (JScrollPane) grandParent;

                    // 设置滚动条样式
                    scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI(mode));
                    scrollPane.getHorizontalScrollBar().setUI(new CustomScrollBarUI(mode));
                }
            }
        } else {
            // 回退方案：部分LookAndFeel使用JTable
            JTable fileTable = findComponent(this, JTable.class);
            if (fileTable != null) {
                fileTable.setBorder(BorderFactory.createLineBorder(panelBorderColor, 1));
                fileTable.setBackground(listBgColor);
                fileTable.setForeground(listFgColor);
                fileTable.setSelectionBackground(listSelectionBgColor);
                fileTable.setSelectionForeground(listSelectionFgColor);

                // 设置表格渲染器
                fileTable.setDefaultRenderer(Object.class, new TableRenderer());

                // 设置滚动条样式
                Container parent = fileTable.getParent();
                if (parent instanceof JViewport) {
                    Container grandParent = parent.getParent();
                    if (grandParent instanceof JScrollPane) {
                        JScrollPane scrollPane = (JScrollPane) grandParent;
                        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI(mode));
                        scrollPane.getHorizontalScrollBar().setUI(new CustomScrollBarUI(mode));
                    }
                }
            }
        }
    }

    // 表格渲染器
    private class TableRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (isSelected) {
                setBackground(listSelectionBgColor);
                setForeground(listSelectionFgColor);
            } else {
                setBackground(listBgColor);
                setForeground(listFgColor);
            }

            return this;
        }
    }

    private void customizeTextField() {
        List<JTextField> textFields = findComponents(JTextField.class);
        for (JTextField textField : textFields) {
            textField.setBackground(panelBgColor);
            textField.setForeground(panelFgColor);
            textField.setCaretColor(caretColor);

            // 更新边框颜色
            textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(panelBorderColor, 1), // 使用新的边框色
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
        }
    }


    private void customizeFilterComboBox() {
        // 查找所有JComboBox组件
        List<JComboBox> combos = findComponents(JComboBox.class);

        // 文件类型过滤器通常是最后一个组合框
        if (!combos.isEmpty()) {
            JComboBox<?> filterCombo = combos.get(combos.size() - 1);
            filterCombo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(panelBorderColor, 1), // 使用新的边框色
                    BorderFactory.createEmptyBorder(2, 5, 2, 5)
            ));

            // 设置组合框样式
            filterCombo.setBackground(panelBgColor);
            filterCombo.setForeground(panelFgColor);
            filterCombo.setRenderer(new FilterComboRenderer());

            // 查找下拉按钮
            for (Component c : filterCombo.getComponents()) {
                if (c instanceof AbstractButton) {
                    AbstractButton button = (AbstractButton) c;
                    button.setBackground(buttonBgColor);
                    button.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

                    // 自定义箭头绘制
                    button.setUI(new BasicButtonUI() {
                        @Override
                        public void paint(Graphics g, JComponent c) {
                            super.paint(g, c);
                            Graphics2D g2 = (Graphics2D) g.create();
                            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                            g2.setColor(panelFgColor);

                            int w = c.getWidth();
                            int h = c.getHeight();
                            int size = Math.min(w, h) / 3;
                            int x = (w - size) / 2;
                            int y = (h - size) / 2;

                            int[] xPoints = {x, x + size, x + size / 2};
                            int[] yPoints = {y, y, y + size};
                            g2.fillPolygon(xPoints, yPoints, 3);
                            g2.dispose();
                        }
                    });
                }
            }
        }
    }


    private void customizeDirectoryComboBox() {

        // 查找所有JComboBox组件
        List<JComboBox> combos = findComponents(JComboBox.class);

        // 目录导航通常是第一个组合框
        if (!combos.isEmpty()) {
            JComboBox<?> dirCombo = combos.get(0);
            dirCombo.setBackground(panelBgColor);
            dirCombo.setForeground(panelFgColor);
            dirCombo.setRenderer(new DirComboRenderer());
        }

        JComboBox<?> dirCombo = combos.get(0);

        // 更新边框颜色
        dirCombo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(panelBorderColor, 1), // 使用新的边框色
                BorderFactory.createEmptyBorder(2, 5, 2, 5)
        ));
    }

    private void customizeToolBar() {
        // 查找所有JToolBar组件
        JToolBar toolbar = findComponent(this, JToolBar.class);

        if (toolbar != null) {
            toolbar.setBackground(panelBgColor);
            toolbar.setFloatable(false);
            toolbar.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

            // 设置工具栏按钮
            for (Component c : toolbar.getComponents()) {
                if (c instanceof AbstractButton) {
                    AbstractButton button = (AbstractButton) c;
                    button.setBackground(panelBgColor);
                    button.setForeground(panelFgColor);
                    button.setFocusPainted(false);
                    button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

                    // 添加悬停效果
                    button.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            button.setBackground(buttonBgColor);
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                            button.setBackground(panelBgColor);
                        }
                    });
                }
            }
        }
    }

    private void customizeSeparators() {
        // 查找所有JSeparator组件
        List<JSeparator> separators = findComponents(JSeparator.class);
        for (JSeparator separator : separators) {
            separator.setBackground(panelBgColor.darker());
            separator.setForeground(panelBgColor.darker());
        }
    }

    // 自定义文件列表渲染器
    private class FileListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            File file = (File) value;
            setText(file.getName());
            setIcon(UIManager.getIcon(file.isDirectory() ? "FileView.directoryIcon" : "FileView.fileIcon"));

            if (isSelected) {
                setBackground(listSelectionBgColor);
                setForeground(listSelectionFgColor);
            } else {
                setBackground(listBgColor);
                setForeground(listFgColor);
            }

            setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
            return this;
        }
    }

    // 自定义文件类型过滤器渲染器
    private class FilterComboRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            // 优先获取文件过滤器的描述文本
            String displayText = (value instanceof FileFilter) ? ((FileFilter) value).getDescription() : value.toString();

            // 使用正确的显示文本调用父类方法
            super.getListCellRendererComponent(list, displayText, index, isSelected, cellHasFocus);

            setBackground(panelBgColor);
            setForeground(panelFgColor);
            setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));

            if (isSelected) {
                setBackground(buttonBgColor);
                setForeground(buttonFgColor);
            }

            return this;
        }
    }

    // 自定义目录导航渲染器
    private class DirComboRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            setBackground(panelBgColor);
            setForeground(panelFgColor);
            setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));

            if (isSelected) {
                setBackground(buttonBgColor);
                setForeground(buttonFgColor);
            }

            return this;
        }
    }

    public void setMode(int mode) {
        if (this.mode != mode) {
            this.mode = mode;
            applyColorScheme();
            customizeUI();
            revalidate();
            repaint();
        }
    }
}