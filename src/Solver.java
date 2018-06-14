
public abstract class Solver {

	private GameModel gameModel;
	private int color;
	protected Move NewMove;
	
	public Solver(GameModel gameModel, int color) {
		this.gameModel = gameModel;
		this.color = color;
	}
	
	public abstract boolean processNextMove(Move move);

	public boolean makeNewMove(Move move) {
		return gameModel.makeMove(move, getColor());
	}

	public int getColor() {
		return color;
	}
	
	public Move getNewMove() {
		return NewMove;
	}
	
	public void setNewMove(Move move) {
		NewMove = move;
	}
}
