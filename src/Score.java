import java.util.ArrayList;

public class Score {
	public static int ONE = 10;
	public static int TWO = 100;
	public static int THREE = 1000;
	public static int FOUR = 100000;
	public static int FIVE = 1000000;
	public static int BLOCKED_ONE = 1;
	public static int BLOCKED_TWO = 10;
	public static int BLOCKED_THREE = 100;
	public static int BLOCKED_FOUR = 1000;
	
	public int deep = 0;
	public int score = -1;
	
	public ArrayList<Move> stepsForKill;
	
	public int getMaxScoreInSteps() {
		if(score != -1) return score;
		
		int maxVal = Integer.MIN_VALUE;
		
		for(int i=0;i<stepsForKill.size();i++) {
			if(stepsForKill.get(i).score > maxVal) {
				maxVal = stepsForKill.get(i).score;
			}
		}
		
		return maxVal == Integer.MIN_VALUE ? -1 : maxVal;
	}
}