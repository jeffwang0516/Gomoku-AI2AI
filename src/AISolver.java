
public class AISolver extends Solver{

	
	private AIStrategy currentStrategy;
	
	
	public AISolver(GameModel gameModel, int color, AIStrategy strategy) {
		super(gameModel, color);
		this.currentStrategy=strategy;
	}

	@Override
	public boolean processNextMove(Move move) {
		// TODO Auto-generated method stub

		Move newMove = currentStrategy.computeAIMove(move);
		NewMove = newMove;
		return makeNewMove(newMove);
	}
	
	public AIStrategy getCurrentStrategy() {
		return currentStrategy;
	}
	
}
