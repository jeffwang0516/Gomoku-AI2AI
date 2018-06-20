
public class AIStrategyGreedy implements AIStrategy {

	
	private int[][] board;
	private AIGreedy aiInstance;
	
	public AIStrategyGreedy(int[][] board) {
		this.board = board;
		aiInstance = new AIGreedy(board);
	}
	@Override
	public Move computeAIMove(Move move) {
		// TODO Auto-generated method stub
		
		
		return aiInstance.computeAIMove(move.getX(), move.getY());
	}

}
