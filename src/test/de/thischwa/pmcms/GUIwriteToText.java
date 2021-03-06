package de.thischwa.pmcms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class GUIwriteToText {
	public static void main(String [] args) {
		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout ());
		final Text text0 = new Text (shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		final Text text1 = new Text (shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		text0.addVerifyListener (new VerifyListener () {
			@Override
			public void verifyText (VerifyEvent event) {
				text1.setTopIndex (text0.getTopIndex ());
				text1.setSelection (event.start, event.end);
				text1.insert (event.text);
			}
		});
		shell.setBounds(10, 10, 200, 200);
		shell.open ();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}

}
