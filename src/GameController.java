import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;


public class GameController implements Runnable {

	private GameView gameView;
	private GameModel gameModel = new GameModel(this);
	
	public static final int BOARD_SIZE_X = 15;
	public static final int BOARD_SIZE_Y = 15;
	
	private boolean gameReady = false;
	
	public GameController(GameView gameView) {
		this.gameView=gameView;
//		this.gameModel=gameModel;
		
		gameView.setMenuListener(new MenuListener());
		gameView.setBoardClickListener(new CustomMouseListener());

		ModeSelectWindow.getInstance()
			.addOptionsListener(new OptionListener());
		AILevelOptionWindow.getInstance()
			.addOptionsListener(new AILevelOptionListener());	

	}
	Thread gameThread = null;
	public void startGame() {
		updateBoardData();
		
//		ModeSelectWindow.getInstance().showOptionWindow();
		gameModel.enableAI(gameModel.getBoardArray());
		setGameReady(true);
		gameThread = new Thread(this);
		gameModel.resetGame();
		gameView.setVisible(true);
		
		gameThread.start();
	}
	
	public void setGameReady(boolean ready) {
		gameReady=ready;
	}
	
	public void restartGame() {
		setGameReady(false);
		ModeSelectWindow.getInstance().showOptionWindow();
		gameModel.resetGame();
		refreshViewAfterMove();
	}
	
	public void updateBoardData() {
		int[][] board = gameModel.getBoardArray();
		Vector<Move> moves = gameModel.getMoveRecord();
		gameView.updateBoardData(board, moves);
		
	}
	
	public void refreshViewAfterMove() {
		updateBoardData();
		gameView.refresh();
	}
	
	class CustomMouseListener extends MouseAdapter{
		public void mousePressed(MouseEvent e) {
			if(!gameReady) return;
			
			int x = e.getX();
			int y = e.getY();
			int gridSize = Board.GRID_SIZE;
//			
			if (!((x % gridSize) == 0 || ((x + 1) % gridSize) == 0 || ((x - 1) % gridSize) == 0
					|| (y % gridSize) == 0 || ((y + 1) % gridSize) == 0 || ((y - 1) % gridSize) == 0)) {
//				gameModel.clicked(new Move(x / gridSize, y / gridSize));
////				System.out.println("TESTTE");
//				refreshViewAfterMove();
//				if(gameModel.getGameStatus() > 0) {
//					gameView.showGameEndNotification(gameModel.getGameStatus());
//				}
			}
			
			// Auto Start
//			System.out.println("Start...");
//			int count = 1;
//			while(gameModel.getGameStatus() <= 0) {
//				count++;
//				refreshViewAfterMove();
//				if(count % 100000 != 0) continue;
//				System.out.println("In");
//				gameModel.clicked(new Move(0, 0));
//				refreshViewAfterMove();
////				try {
////					Thread.sleep(1000);
////				} catch (InterruptedException e1) {
////					// TODO Auto-generated catch block
////					e1.printStackTrace();
////				}
//				
//			}
			
//			if(gameModel.getGameStatus() > 0) {
//				gameView.showGameEndNotification(gameModel.getGameStatus());
//			}
		}
		
	}
	
	class OptionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String actionCommand = e.getActionCommand();
			if (actionCommand.equals("是")) {
				gameModel.enableAI(gameModel.getBoardArray());
				setGameReady(true);
//				AILevelOptionWindow.getInstance().showAIOptionWindow();
			}else {
				gameModel.disableAI();
				setGameReady(true);
			}
			ModeSelectWindow.getInstance().hideOptionWindow();
		}
	}
	
	class AILevelOptionListener implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
//			String actionCommand = e.getActionCommand();
//			int[][] board = gameModel.getBoardArray();
//			if (actionCommand.equals("Easy")) {
//				gameModel.enableAI(new AIStrategyEasy(board));
//			} else if (actionCommand.equals("Hard")) {
//				gameModel.enableAI(new AIStrategyHard(board));
//			}
//			AILevelOptionWindow.getInstance().hideAIOptionWindow();
//			
//			setGameReady(true);
		}
	}
	
	class MenuListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			if (command.equals("新遊戲")) {
				restartGame();
			} else if (command.equals("悔棋")) {
				if (gameModel.getGameStatus() > 0)
					return;
				
				gameModel.regretLastMove();
				refreshViewAfterMove();
				
			} else if (command.equals("離開")) {
				System.exit(0);
			}
		}
	}
	long startTime;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		startTime = System.currentTimeMillis();
		System.out.println("Start Time:" + startTime);
		// ... do something ...
		
		while(true) {
			gameModel.clicked(new Move(0, 0));
			System.out.println("Perform click.. One round");
			refreshViewAfterMove();
			if(gameModel.getGameStatus() > 0) {
				
				System.out.println("Game End");
				long estimatedTime = System.currentTimeMillis() - startTime;
				System.out.println("Time Consumed:" + estimatedTime);
				System.out.println(gameModel.getStepRecord());
				gameView.showGameEndNotification(gameModel.getGameStatus());
				
				break;
			}
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
	}
}
