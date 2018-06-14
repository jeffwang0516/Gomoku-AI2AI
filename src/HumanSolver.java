
public class HumanSolver extends Solver{
	
	public HumanSolver(GameModel gameModel, int color) {
		super(gameModel, color);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean processNextMove(Move move) {
		// TODO Auto-generated method stub
		NewMove = move;
		return makeNewMove(move);
		
	}
	
	

}
