import java.util.Vector;

public class GameModel {
	// Set first move and second move for player 1
	public Move firstStep = new Move(7, 7);
	public Move secStep = new Move(7, 4);
	
	// Set this to 1 to disable the PRO rule
	private int proRuleStepsCnt = 1; 
	
	// Enable minimax+AB with/without finding must kill steps
	private boolean player1_findMustKillEnabled = true; 
	
	// 0 for minimax, 1 for greedy
	private int player2_minimaxOrGreedy = 0; 
	
	private Solver player1; // = new HumanSolver(this, Constants.COLOR_BLACK);
	private Solver player2;
	
	private Solver currentSolver;
	
	private int[][] board = new int[GameController.BOARD_SIZE_X][GameController.BOARD_SIZE_Y];
	
	private Vector<Move> recordMoves = new Vector<Move>();
	
	private int gameEndStatus = 0;
	GameController controller = null;
	
	private int player1StepCount = 0;
	private int player2StepCount = 0;
	
		
	public GameModel(GameController controller){
		this.controller = controller;
		
		resetGame();
	}
	
	public void enableAI(int [][] board) {	
		//LET player1 be computer StrategyEasy***********
		this.player1 = new AISolver(this, Constants.COLOR_BLACK, new AIStrategyABAndMustKill(board, player1_findMustKillEnabled));
		currentSolver = player1;
		//***********
		//LET player2 be computer StrategyHard***********
		AIStrategy strategyForP2;
		if(player2_minimaxOrGreedy == 0) {
			strategyForP2 = new AIStrategyAlpaBeta(board);
		} else {
			strategyForP2 = new AIStrategyGreedy(board);
		}
		this.player2 = new AISolver(this, Constants.COLOR_WHITE, strategyForP2);
	}
	
	
	int stepCount = 0;
	Move previousStep;
	public void clicked(Move move) {
		System.out.println("Calculating... Player "+currentSolver.getColor());
		boolean stepMade = false;
		if (stepCount >= proRuleStepsCnt) {
			
			
			stepMade = currentSolver.processNextMove(previousStep);
			move = currentSolver.getNewMove();
		} else {
			if(stepCount==0)
				move = firstStep;
			else if(stepCount==1) {
				((AIStrategyABAndMustKill)((AISolver) currentSolver).getCurrentStrategy()).putMove(previousStep, Constants.COLOR_WHITE);
				move = secStep;
			}
//			stepMade = currentSolver.makeNewMove(move);
			((AIStrategyABAndMustKill)((AISolver) currentSolver).getCurrentStrategy()).putMove(move, Constants.COLOR_BLACK);
			stepMade = true;
			
			stepCount++;
		}
		
		
		
		if(stepMade) {
			player1StepCount++;
			if(checkWinner(move)) return;
			controller.refreshViewAfterMove();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("----Player "+currentSolver.getColor()+"finished ! SWITCH----");
			switchTurn();
		}
		if(currentSolver == player2) {//if(currentSolver instanceof AISolver) {
			System.out.println("Calculating... Player "+currentSolver.getColor());
			currentSolver.processNextMove(move);
			if(checkWinner(currentSolver.getNewMove())) return;
			previousStep = currentSolver.getNewMove();
			controller.refreshViewAfterMove();
			player2StepCount++;
			System.out.println("----Player "+currentSolver.getColor()+"finished ! SWITCH----");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			switchTurn();
		}
	}
	
	
	public String getStepRecord() {
		return "Used Steps: p1="+player1StepCount+" p2="+player2StepCount;
	}
	
	public void resetGame() {
		for (int i = 0; i < GameController.BOARD_SIZE_X; i++)
			for (int j = 0; j < GameController.BOARD_SIZE_Y; j++)
				board[i][j] = 0;
		
		recordMoves.clear();
		currentSolver = player1;
		gameEndStatus = 0;
	}
	
	private void setOpponent(Solver solver) {
		this.player2=solver;
	}
	
	
	public void enableAI(AIStrategy strategy) {	
		
		setOpponent(new AISolver(this, Constants.COLOR_WHITE, strategy));	
	}

	public void disableAI() {
		setOpponent(new HumanSolver(this, Constants.COLOR_WHITE));	
	}
	int turn =1;
	public void switchTurn() {
		if(currentSolver == player1) {
			currentSolver = player2;
		}else {
			currentSolver = player1;
		}
		if(turn==1)turn = 2;else turn =1;
	}
	
	
	public boolean makeMove(Move move, int val) {

		int x = move.getX();
		int y = move.getY();
		
		if (gameEndStatus == 0 && board[x][y] == 0) {
			board[x][y] = val;
			recordMoves.add(move);

			if(recordMoves.size() == GameController.BOARD_SIZE_X * GameController.BOARD_SIZE_Y) {
				gameEndStatus = Constants.TIE_GAME;
			}
			return true;
		}
		return false;
	}
	
	public void regretLastMove() {
		if (!recordMoves.isEmpty()) {
			Move move = recordMoves.remove(recordMoves.size() - 1);
			
			int x = move.getX();
			int y = move.getY();
			
			board[x][y] = 0;
			currentSolver.setNewMove(move);
			switchTurn();
			
			if(player2 instanceof AISolver) {
				move = recordMoves.remove(recordMoves.size() - 1);
				x = move.getX();
				y = move.getY();
				
				board[x][y] = 0;
				currentSolver.setNewMove(move);
				switchTurn();
			}
		}

	}
	private boolean checkXY(int x, int y) {
		return (x < GameController.BOARD_SIZE_X && x >= 0 && y < GameController.BOARD_SIZE_Y && y >= 0);
	}
	
	public boolean checkWinner(Move move) {
		
		int x = move.getX();
		int y = move.getY();
		
		int count;
		int i, j;

		count = 1; // Vertical
		for (i = 1; checkXY(x, y + i) && board[x][y + i] == board[x][y]; i++)
			count++;
		for (j = -1; checkXY(x, y + j) && board[x][y + j] == board[x][y]; j--)
			count++;
		if (count >= 5) {
			gameEndStatus = currentSolver.getColor();
		}

		count = 1; // Horizontal
		for (i = 1; checkXY(x + i, y) && board[x + i][y] == board[x][y]; i++)
			count++;
		for (j = -1; checkXY(x + j, y) && board[x + j][y] == board[x][y]; j--)
			count++;
		if (count >= 5) {
			gameEndStatus = currentSolver.getColor();
		}

		count = 1; // BackSlash
		for (i = 1; checkXY(x + i, y + i) && board[x + i][y + i] == board[x][y]; i++)
			count++;
		for (j = -1; checkXY(x + j, y + j)
				&& board[x + j][y + j] == board[x][y]; j--)
			count++;
		if (count >= 5) {
			gameEndStatus = currentSolver.getColor();
		}

		count = 1; // Slash
		for (i = 1; checkXY(x - i, y + i) && board[x - i][y + i] == board[x][y]; i++)
			count++;
		for (j = -1; checkXY(x - j, y + j)
				&& board[x - j][y + j] == board[x][y]; j--)
			count++;
		if (count >= 5) {
			gameEndStatus = currentSolver.getColor();
		}

		if(gameEndStatus > 0 ) return true;
		
		return false;
	}
	
	public int[][] getBoardArray(){
		return board;
	}
	
	public Vector<Move> getMoveRecord(){
		return recordMoves;
	}
	
	public int getGameStatus() {
		return gameEndStatus;
	}
	
}
