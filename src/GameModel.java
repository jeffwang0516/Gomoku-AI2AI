import java.util.Vector;

public class GameModel {
	// Set first move for player 1
	private Move firstStep = new Move(7,7);
	
	
	private Solver player1 = new HumanSolver(this, Constants.COLOR_BLACK);
	private Solver player2;
	
	private Solver currentSolver;
	
	private int[][] board = new int[GameController.BOARD_SIZE_X][GameController.BOARD_SIZE_Y];
	
	private Vector<Move> recordMoves = new Vector<Move>();
	
	private int gameEndStatus = 0;
	
	public GameModel(){
		resetGame();
	}
	
	public void enableAI(int [][] board) {	
		//LET player1 be computer StrategyEasy***********
		this.player1 = new AISolver(this, Constants.COLOR_BLACK, new AIStrategyEasy(board));
		currentSolver = player1;
		//***********
		//LET player2 be computer StrategyHard***********
		this.player2 = new AISolver(this, Constants.COLOR_WHITE, new AIStrategyHard(board));
	}
	
	
	Boolean ifFirstStep = true;
	Move previousStep;
	public void clicked(Move move) {
		System.out.println("Clicked");
		boolean stepMade = false;
		if (!ifFirstStep) {
			
			
			stepMade = currentSolver.processNextMove(previousStep);
			move = currentSolver.getNewMove();
		} else {
			move = firstStep;
			stepMade = currentSolver.makeNewMove(move);

			ifFirstStep = false;
		}
		
		
		
		if(stepMade) {
			if(checkWinner(move)) return;
			switchTurn();
		}
		if(currentSolver == player2) {//if(currentSolver instanceof AISolver) {
			currentSolver.processNextMove(move);
			if(checkWinner(currentSolver.getNewMove())) return;
			previousStep = currentSolver.getNewMove();

			switchTurn();
		}
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
