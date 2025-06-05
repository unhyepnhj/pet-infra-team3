package view;

import java.awt.Component;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellRenderer;

/*
 * JTable용 유틸리티 클래스
 * 버튼 renderer, editor 구현
 */

class ButtonRenderer extends JButton implements TableCellRenderer {	// 버튼 renedrer
 public ButtonRenderer() {
     setOpaque(true);
 }
 public Component getTableCellRendererComponent(JTable table, Object value,
         boolean isSelected, boolean hasFocus, int row, int column) {
     setText((value == null) ? "" : value.toString());
     return this;
 }
}

class ButtonEditor extends DefaultCellEditor {	// 버튼 클릭 시 동작 정의용
 private String label;
 private final JButton button;
 private boolean clicked;
 private final ActionListener action;

 public ButtonEditor(JCheckBox checkBox, String label, java.util.function.Consumer<Integer> onClick) {
     super(checkBox);
     this.label = label;
     this.button = new JButton(label);
     this.action = e -> {
         int row = ((JTable) SwingUtilities.getAncestorOfClass(JTable.class, button)).getSelectedRow();
         onClick.accept(row);
     };
     button.addActionListener(action);
 }

 public Component getTableCellEditorComponent(JTable table, Object value,
         boolean isSelected, int row, int column) {
     button.setText(label);
     clicked = true;
     return button;
 }

 public Object getCellEditorValue() {
     clicked = false;
     return label;
 }

 public boolean stopCellEditing() {
     clicked = false;
     return super.stopCellEditing();
 }

 protected void fireEditingStopped() {
     super.fireEditingStopped();
 }
}
