package example;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class InnerClassTest1 extends JButton {

	void doSomething() {
		this.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				final int index = "ABCDE".substring(3).length();
			}

		});
	}
}
