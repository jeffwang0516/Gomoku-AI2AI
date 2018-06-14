public class AIStrategyHard implements AIStrategy{
	private int[][] board;
	private AI aiInstance;
	public AIStrategyHard(int[][] board) {
		this.board = board;
		this.aiInstance = new AI(board);
	}	
	
	@Override
	public Move computeAIMove(Move previousMove) {
		
		int x = previousMove.getX();
		int y = previousMove.getY();
		// PLAYER 2 AI Defines Here
		
		
		
		
		
		
		
		
		return aiInstance.computeAIMove(x, y); // return next step by new Move(x,y)
	}

}
