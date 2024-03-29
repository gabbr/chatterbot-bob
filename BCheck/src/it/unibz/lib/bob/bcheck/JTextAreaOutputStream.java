package it.unibz.lib.bob.bcheck;

import java.io.OutputStream;

import javax.swing.JTextArea;

public class JTextAreaOutputStream extends OutputStream {
	JTextArea ta;

	public JTextAreaOutputStream(JTextArea t) {

		super();
		ta = t;
	}

	public void write(int i) {
		char[] chars = new char[1];
		chars[0] = (char) i;
		String s = new String(chars);
		ta.append(s);
	}

	public void write(char[] buf, int off, int len) {
		String s = new String(buf, off, len);
		ta.append(s);
	}
}
