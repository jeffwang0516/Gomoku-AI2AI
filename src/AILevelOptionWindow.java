import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AILevelOptionWindow extends JFrame{

	private static AILevelOptionWindow instance;
	private static Dimension screenSize = java.awt.Toolkit.getDefaultToolkit()
			.getScreenSize();

	private JButton[] btn_level = new JButton[2];
	
	private AILevelOptionWindow() {
		
		setSize(200, 100);
		setLayout(new BorderLayout());

		JLabel settings = new JLabel("請選擇難度");
		add(settings, BorderLayout.CENTER);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());

		
		btn_level[0] = new JButton("Easy");
		//yesButton.addActionListener(this);
		buttonPanel.add(btn_level[0]);

		btn_level[1] = new JButton("Hard");
		//noButton.addActionListener(this);
		buttonPanel.add(btn_level[1]);

		add(buttonPanel, BorderLayout.SOUTH);
		setLocation((int) screenSize.getWidth() / 2 - 200 / 2,
				(int) screenSize.getHeight() / 2 - 100 / 2);
		
		hideAIOptionWindow();

	}
	
	public static AILevelOptionWindow getInstance() {
        if (instance == null) {
            synchronized(AILevelOptionWindow.class) {
                if (instance == null) {
                    instance = new AILevelOptionWindow();
                }
            }
        }
        return instance;
    }
	
	public void addOptionsListener(ActionListener al) {
		for(JButton btn: btn_level) {
			btn.addActionListener(al);
		}
	}
	
	public void showAIOptionWindow() {
		this.setVisible(true);
		this.setAlwaysOnTop(true);	
	}
	
	public void hideAIOptionWindow() {
		this.setVisible(false);
		this.setAlwaysOnTop(false);	
	}
}
