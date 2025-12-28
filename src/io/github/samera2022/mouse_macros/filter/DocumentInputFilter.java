package io.github.samera2022.mouse_macros.filter;

import javax.swing.text.DocumentFilter;

public class DocumentInputFilter extends DocumentFilter {

    public boolean isValidContent(String input) { return true; }

    @Override
    public void insertString(FilterBypass fb, int offset, String string, javax.swing.text.AttributeSet attr) throws javax.swing.text.BadLocationException {
        StringBuilder sb = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
        sb.insert(offset, string);
        if (isValidContent(sb.toString())) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, javax.swing.text.AttributeSet attrs) throws javax.swing.text.BadLocationException {
        StringBuilder sb = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
        sb.replace(offset, offset + length, text);
        if (isValidContent(sb.toString())) {
            super.replace(fb, offset, length, text, attrs);
        }
    }
}
