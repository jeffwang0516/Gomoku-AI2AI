
public class GomokuMain {
	public static void main(String[] args) {
		
		GameModel gameModel = new GameModel();
		GameView gameView = new GameView();
		
		new GameController(gameView, gameModel).startGame();
	}
}
