import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class ModeSelectWindow extends JFrame{
		
		private static Dimension screenSize = java.awt.Toolkit.getDefaultToolkit()
				.getScreenSize();
	
		private JButton yesButton;
		private JButton noButton;
		
		private static ModeSelectWindow instance;
		
		private ModeSelectWindow() {
			
			setSize(200, 100);
			setLayout(new BorderLayout());

			JLabel settings = new JLabel("是否和電腦對戰?");
			add(settings, BorderLayout.CENTER);
			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout(new FlowLayout());

			yesButton = new JButton("是");
			//yesButton.addActionListener(this);
			buttonPanel.add(yesButton);

			noButton = new JButton("否");
			//noButton.addActionListener(this);
			buttonPanel.add(noButton);

			add(buttonPanel, BorderLayout.SOUTH);
			setLocation((int) screenSize.getWidth() / 2 - 200 / 2,
					(int) screenSize.getHeight() / 2 - 100 / 2);
			
		}
		
		public static ModeSelectWindow getInstance() {
	        if (instance == null) {
	            synchronized(ModeSelectWindow.class) {
	                if (instance == null) {
	                    instance = new ModeSelectWindow();
	                }
	            }
	        }
	        return instance;
	    }

//		public void actionPerformed(ActionEvent e) {
//			String actionCommand = e.getActionCommand();
//			if (actionCommand.equals("是")) {
//				gameModel.enableAI(true);
//			}else {
//				gameModel.enableAI(false);
//			}
//			
//			dispose();
//		}
		
		public void addOptionsListener(ActionListener al) {
			yesButton.addActionListener(al);
			noButton.addActionListener(al);
		}
		
		public void showOptionWindow() {
			setVisible(true);
			setAlwaysOnTop(true);
		}
		
		public void hideOptionWindow() {
			setVisible(false);
			setAlwaysOnTop(false);
		}

}