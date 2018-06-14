import java.awt.Color;
import java.awt.Graphics;
import java.util.Vector;
import javax.swing.JPanel;

public class Board extends JPanel {
	
	public static final int GRID_SIZE = 40;
	private static final int boardHieght = GameController.BOARD_SIZE_Y * GRID_SIZE;
	private static final int boardWidth = GameController.BOARD_SIZE_X * GRID_SIZE;
	
	private int[][] board = new int[GameController.BOARD_SIZE_X][GameController.BOARD_SIZE_Y];
	private Vector<Move> recordStep;
	
	private static Board instance;

	private Board() {
		setSize(boardWidth, boardHieght);
	}
	
	public static Board getInstance() {
        if (instance == null) {
            synchronized(Board.class) {
                if (instance == null) {
                    instance = new Board();
                }
            }
        }
        return instance;
        
    }
	
	public void updateBoardArray(int[][] boardArr, Vector<Move> recordStep) {
		this.board = boardArr;
		this.recordStep = recordStep;
	}

	public void paint(Graphics g) {
		super.paint(g);
		
		final int width = GameController.BOARD_SIZE_X;
		final int height = GameController.BOARD_SIZE_Y;
		final int r = 16;
		final int length = GRID_SIZE;
		final int offset = length / 2 - r;
		
		// Draw Background
		Color color = new Color(238, 180, 34);
		g.setColor(color);
		g.drawRect(0, 0, width * length, height * length);
		g.fillRect(0, 0, width * length, height * length);
		
		// Draw Grid
		g.setColor(Color.BLACK);
		for (int i = length / 2; i <= width * length; i += length) {
			g.drawLine(i - 1, length / 2, i - 1, height * length - length / 2);
			g.drawLine(i, length / 2, i, height * length - length / 2);
			g.drawLine(i + 1, length / 2, i + 1, height * length - length / 2);
		}
		for (int i = length / 2; i <= height * length; i += length) {
			g.drawLine(length / 2, i - 1, width * length - length / 2, i - 1);
			g.drawLine(length / 2, i, width * length - length / 2, i);
			g.drawLine(length / 2, i + 1, width * length - length / 2, i + 1);
		}
		
		// Draw Dot
		for (int i = 3; i <= 11; i += 4 ) {
			for (int j = 3; j <= 11; j += 4 ) {
				g.fillOval(i * length + offset + r / 2, j * length + offset + r / 2, r, r);
			}
		}
		
		// Draw Current Moves
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (board[i][j] != 0) {
					if (board[i][j] == Constants.COLOR_BLACK)
						g.setColor(Color.BLACK);
					else
						g.setColor(Color.WHITE);
					g.fillOval(i * length + offset, j * length + offset, 2 * r,
							2 * r);
					g.setColor(Color.BLACK);
					g.drawOval(i * length + offset, j * length + offset, 2 * r,
							2 * r);
				}
			}
		}
		
		// Draw Last Move Indicator
		if(recordStep.size() != 0) {
			Move lastMove = recordStep.lastElement();
			int x = lastMove.getX();
			int y = lastMove.getY();
			g.setColor(Color.RED);
			g.drawOval(x * length + offset, y * length + offset, 2 * r,
					2 * r);
			g.drawLine(x * length + length/2 - 1, y * length, x * length + length/2 - 1, y * length + length);
			g.drawLine(x * length + length/2, y * length, x * length + length/2, y * length + length);
			g.drawLine(x * length + length/2 + 1, y * length, x * length + length/2 + 1, y * length + length);
			g.drawLine(x * length, y * length + length/2 - 1, x * length + length, y * length + length/2 - 1);
			g.drawLine(x * length, y * length + length/2, x * length + length, y * length + length/2);
			g.drawLine(x * length, y * length + length/2 + 1, x * length + length, y * length + length/2 + 1);
		}
	}
}