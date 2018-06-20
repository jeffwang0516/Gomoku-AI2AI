import java.util.concurrent.ThreadLocalRandom;


public class ZobristHash {

	public long[] player1 = new long[GameController.BOARD_SIZE_X*GameController.BOARD_SIZE_Y];
	public long[] player2 = new long[GameController.BOARD_SIZE_X*GameController.BOARD_SIZE_Y];
	
	private int myColor = 0;
	private int opponentColor = 0;
	
	private long hash = 0;
	
	public ZobristHash(int myColor, int opponentColor) {
		this.myColor = myColor;
		this.opponentColor = opponentColor;
		
		
		for(int i=0;i< player1.length;i++) {
			player1[i] =ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE); //ThreadLocalRandom.current().nextLong();
			player2[i] = ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE);//ThreadLocalRandom.current().nextLong();
//			ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE);
//			System.out.println("Zobrist: p1:"+player1[i]+" p2:"+player2[i]);
		}
		
		hash = ThreadLocalRandom.current().nextLong();
	}
	
	public void init() {
		for(int i=0;i< player1.length;i++) {
			player1[i] = ThreadLocalRandom.current().nextLong();
			player2[i] = ThreadLocalRandom.current().nextLong();
		}
		
		hash = ThreadLocalRandom.current().nextLong();
	}
	
	public long performAndGetHashValue(Move point, int playerColor) {
		int index = point.getX() * GameController.BOARD_SIZE_X + point.getY();
		
		
		if(playerColor == myColor) {
			hash = hash ^ player1[index];
		} else if(playerColor == opponentColor){
			hash = hash ^ player2[index];
		}
		
		return hash;
	}
	
	public long getCurrentHash() {
		return hash;
	}
}