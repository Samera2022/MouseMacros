package io.github.samera2022.mousemacros.app.ui.component;

import io.github.samera2022.mousemacros.app.Localizer;
import io.github.samera2022.mousemacros.app.model.MacroAction;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Table model for displaying macro actions in a structured format.
 */
public class MacroActionTableModel extends AbstractTableModel {
    private final List<MacroAction> actions = new ArrayList<>();
    private final String[] columnNames;
    
    public MacroActionTableModel() {
        // Column names: Time, Type, Button, X, Y, Delay
        columnNames = new String[] {
            Localizer.get("table.time"),
            Localizer.get("table.type"),
            Localizer.get("table.button"),
            "X",
            "Y",
            Localizer.get("table.delay")
        };
    }
    
    @Override
    public int getRowCount() {
        return actions.size();
    }
    
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }
    
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        MacroAction action = actions.get(rowIndex);
        switch (columnIndex) {
            case 0: return action.getFormattedTime();
            case 1: return action.getType();
            case 2: return action.getButton();
            case 3: return action.getX();
            case 4: return action.getY();
            case 5: return action.getDelay() + "ms";
            default: return null;
        }
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        // X, Y, Delay columns are editable
        return columnIndex >= 3 && columnIndex <= 5;
    }
    
    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        // TODO: Implement editing logic when needed
        fireTableCellUpdated(rowIndex, columnIndex);
    }
    
    public void addAction(MacroAction action) {
        actions.add(action);
        int row = actions.size() - 1;
        fireTableRowsInserted(row, row);
    }
    
    public void clear() {
        int size = actions.size();
        if (size > 0) {
            actions.clear();
            fireTableRowsDeleted(0, size - 1);
        }
    }
    
    public int getActionCount() {
        return actions.size();
    }
    
    public MacroAction getActionAt(int row) {
        return actions.get(row);
    }
}