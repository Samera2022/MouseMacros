package io.github.samera2022.mousemacros.app.ui.component;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public abstract class DocumentInputFilter extends DocumentFilter {
    public abstract boolean isValidContent(String input);

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        StringBuilder sb = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
        sb.insert(offset, string);
        if (isValidContent(sb.toString())) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        StringBuilder sb = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
        sb.replace(offset, offset + length, text);
        if (isValidContent(sb.toString())) {
            super.replace(fb, offset, length, text, attrs);
        }
    }
}
