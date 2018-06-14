
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class GameView extends JFrame{
	private static final int WINDOW_HEIGHT = GameController.BOARD_SIZE_Y * Board.GRID_SIZE + 80;
	private static final int WINDOW_WIDTH = GameController.BOARD_SIZE_X * Board.GRID_SIZE + 40;
	private static final int BOARD_MARGIN_Y = 16;
	private static final int BOARD_MARGIN_X = 16;
	
	private static Dimension screenSize = java.awt.Toolkit.getDefaultToolkit()
			.getScreenSize();
	
	private GameMenu gameMenu;
	private Board board;

	public GameView() {

		gameMenu = GameMenu.getMenuInstance();
		setJMenuBar(gameMenu);
		
		board = Board.getInstance();
		board.setLocation(BOARD_MARGIN_X, BOARD_MARGIN_Y);		
		add(board);
		
		
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setTitle("Gomoku");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		
		setLocation((int) screenSize.getWidth() / 2 - WINDOW_WIDTH / 2,
				(int) screenSize.getHeight() / 2 - WINDOW_HEIGHT / 2);
//		setVisible(true);
	}
	
	
	public void refresh() {
		board.repaint();
	}
	
	public void showGameEndNotification(int winnerColor) {
		if (winnerColor == Constants.TIE_GAME) {
			JOptionPane.showMessageDialog(null, "遊戲結束", "平手！", JOptionPane.INFORMATION_MESSAGE);
		}
		else if (winnerColor > 0) {
			JOptionPane.showMessageDialog(null, "遊戲結束", ((winnerColor == Constants.COLOR_BLACK) ? "黑色"
					: "白色") + "獲勝!", JOptionPane.INFORMATION_MESSAGE);
		}
		
	}
	
	public void setMenuListener(ActionListener al) {
		gameMenu.addMenuListener(al);
	}
	
	public void setBoardClickListener(MouseAdapter ma) {
		board.addMouseListener(ma);
	}
	
	public void updateBoardData(int[][] boardArr, Vector<Move> recordStep) {
		board.updateBoardArray(boardArr, recordStep);
	}
}
