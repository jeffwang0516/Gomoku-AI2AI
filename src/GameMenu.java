import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class GameMenu extends JMenuBar{
		
		private JMenu diner = new JMenu("遊戲");
		private JMenuItem newGame = new JMenuItem("新遊戲");
		private JMenuItem undo = new JMenuItem("悔棋");
		private JMenuItem exit = new JMenuItem("離開");
		
		private final static GameMenu instance = new GameMenu();
		
		private GameMenu() {
			
			newGame.setAccelerator(KeyStroke.getKeyStroke("control N"));
			undo.setAccelerator(KeyStroke.getKeyStroke("U"));
			exit.setAccelerator(KeyStroke.getKeyStroke("control Q"));
			diner.add(newGame);
			diner.add(undo);
			diner.add(exit);
			
			add(diner);
//			setJMenuBar(bar);
		}
		
		public void addMenuListener(ActionListener al) {
			newGame.addActionListener(al);
			undo.addActionListener(al);
			exit.addActionListener(al);
		}
		
		public static GameMenu getMenuInstance() {
			return instance;
		}
}