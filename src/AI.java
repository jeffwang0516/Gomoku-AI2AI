import java.util.Vector;

public class AI {
	private int[][] board;
	private static final int width = GameController.BOARD_SIZE_X;
	private static final int height = GameController.BOARD_SIZE_X;
	private int[][] aiWeight = new int[width][height];

	
	public AI(int[][] board) {
		this.board = board;
	}
	public boolean checkXY(int x, int y) {
		return (x < height && x >= 0 && y < width && y >= 0);
	}	
	
	public boolean matchChessType(int x, int y, String dir, String type, boolean isComputer) {
		StringBuilder str = new StringBuilder("");
		int len = type.length();

		board[x][y] = 1;
		if (isComputer) {
			board[x][y] = 2;
			type = type.replace("1", "x").replace("2", "1").replace("x", "2");
		}

		if (dir.equals("-")) {
			for (int i = -(len - 1); i < len; i++) {
				if (checkXY(x + i, y))
					str.append(String.valueOf(board[x + i][y]));
			}
		} else if (dir.equals("|")) {
			for (int i = -(len - 1); i < len; i++) {
				if (checkXY(x, y + i))
					str.append(String.valueOf(board[x][y + i]));
			}
		} else if (dir.equals("/")) {
			for (int i = -(len - 1); i < len; i++) {
				if (checkXY(x + i, y + i))
					str.append(String.valueOf(board[x + i][y + i]));
			}
		} else {
			for (int i = -(len - 1); i < len; i++) {
				if (checkXY(x - i, y + i))
					str.append(String.valueOf(board[x - i][y + i]));
			}
		}

		board[x][y] = 0;
		if (str.indexOf(type) != -1)
			return true;
		return false;
	}

	public void findAllDir(int x, int y, String type, int weight) {
		if (matchChessType(x, y, "-", type, false) != false)
			aiWeight[x][y] += weight;

		if (matchChessType(x, y, "|", type, false) != false)
			aiWeight[x][y] += weight;

		if (matchChessType(x, y, "/", type, false) != false)
			aiWeight[x][y] += weight;

		if (matchChessType(x, y, "\\", type, false) != false)
			aiWeight[x][y] += weight;

		if (matchChessType(x, y, "-", type, true) != false)
			aiWeight[x][y] += weight;

		if (matchChessType(x, y, "|", type, true) != false)
			aiWeight[x][y] += weight;

		if (matchChessType(x, y, "/", type, true) != false)
			aiWeight[x][y] += weight;

		if (matchChessType(x, y, "\\", type, true) != false)
			aiWeight[x][y] += weight;
	}

	public void findFive(int x, int y) {
		/*
		 * Status Weight 11111 100000
		 */
		String[] type = { "11111" };
		int[] weight = { 100000 };

		findAllDir(x, y, type[0], weight[0]);
	}

	public void findFour(int x, int y) {
		/*
		 * Status Weight 011110 40000 211110 10000 011112 10111 11101 11011
		 */
		String[] type = { "011110", "211110", "011112", "10111", "11101",
				"11011" };
		int[] weight = { 40000, 10000, 10000, 10000, 10000, 10000 };

		for (int i = 0; i < type.length; i++)
			findAllDir(x, y, type[i], weight[i]);
	}

	public void findThree(int x, int y) {
		/*
		 * Status Weight 01110 10000 010110 011010 21110 2000 01112 210110
		 * 010112
		 */
		String[] type = { "01110", "010110", "011010", "21110", "01112",
				"210110", "010112" };
		int[] weight = { 10000, 10000, 10000, 2000, 2000, 2000, 2000 };

		for (int i = 0; i < type.length; i++)
			findAllDir(x, y, type[i], weight[i]);
	}

	public void findTwo(int x, int y) {
		/*
		 * Status Weight 01100 600 00110 01010 010010 211000 200 000112 201100
		 * 001102 200110 011002 210100 001012 201010 010102 210010 010012
		 */

		String[] type = { "01100", "00110", "01010", "010010", "211000",
				"000112", "201100", "001102", "200110", "011002", "210100",
				"001012", "201010", "010102", "210010", "010012" };
		int[] weight = { 600, 600, 600, 600, 200, 200, 200, 200, 200, 200, 200,
				200, 200, 200, 200, 200 };

		for (int i = 0; i < type.length; i++)
			findAllDir(x, y, type[i], weight[i]);
	}

	public void findWeightEasy() {
		for (int y = 0; y < 15; y++) {
			for (int x = 0; x < 15; x++) {
				if (board[x][y] != 0)
					continue;

				findThree(x, y);
				findFour(x, y);
				findFive(x, y);
			}
		}
	}
	
	public void findWeightHard() {
		for (int y = 0; y < 15; y++) {
			for (int x = 0; x < 15; x++) {
				if (board[x][y] != 0)
					continue;

				findTwo(x, y);
				findThree(x, y);
				findFour(x, y);
				findFive(x, y);
			}
		}
	}
	
	
	
	public void initWeight() {
		int n = 15;
		boolean[][] proceed = new boolean[15][15];
		Vector<Triplet<Integer, Integer, Integer>> que = new Vector<Triplet<Integer, Integer, Integer>>();
		int dir[][] = { { -1, -1 }, { 0, -1 }, { 0, 1 }, { -1, 0 }, { 1, 0 },
				{ -1, 1 }, { 1, -1 }, { 1, 1 } };
		int x, y;

		x = y = n = n / 2;
		n = 50;

		que.add(new Triplet<Integer, Integer, Integer>(x, y, n));
		proceed[x][y] = true;
		while (!que.isEmpty()) {
			Triplet<Integer, Integer, Integer> cur = que.remove(0);
			x = cur.getX();
			y = cur.getY();
			aiWeight[x][y] = cur.getN();
			for (int i = 0; i < dir.length; i++) {
				if (checkXY(x + dir[i][0], y + dir[i][1])
						&& !proceed[x + dir[i][0]][y + dir[i][1]]) {
					que.add(new Triplet<Integer, Integer, Integer>(x
							+ dir[i][0], y + dir[i][1], (cur.getN() == 0) ? 0
							: (cur.getN() - 1)));
					proceed[x + dir[i][0]][y + dir[i][1]] = true;
				}
			}
		}
	}

	public void lastHandWeight(int x, int y) {
		if (x == 0 && y == 0)
			return;
		
		initWeight();
		
		int n = 7;
		boolean[][] proceed = new boolean[15][15];
		Vector<Triplet<Integer, Integer, Integer>> que = new Vector<Triplet<Integer, Integer, Integer>>();
		int[][] dir = { { -1, -1 }, { 0, -1 }, { 0, 1 }, { -1, 0 }, { 1, 0 },
				{ -1, 1 }, { 1, -1 }, { 1, 1 } };

		que.add(new Triplet<Integer, Integer, Integer>(x, y, n));
		proceed[x][y] = true;
		while (!que.isEmpty()) {
			Triplet<Integer, Integer, Integer> cur = que.remove(0);
			x = cur.getX();
			y = cur.getY();

			if (board[x][y] == 0)
				aiWeight[x][y] += cur.getN();

			for (int i = 0; i < dir.length; i++) {
				if (checkXY(x + dir[i][0], y + dir[i][1])
						&& !proceed[x + dir[i][0]][y + dir[i][1]]) {
					que.add(new Triplet<Integer, Integer, Integer>(x
							+ dir[i][0], y + dir[i][1], (cur.getN() == 0) ? 0
							: (cur.getN() - 1)));
					proceed[x + dir[i][0]][y + dir[i][1]] = true;
				}
			}
		}
	}

	public void findWeight() {
		// Updates aiWeight 2D-array
		for (int y = 0; y < 15; y++) {
			for (int x = 0; x < 15; x++) {
				if (this.board[x][y] != 0)
					continue;

				this.findTwo(x, y);
				this.findThree(x, y);
				this.findFour(x, y);
				this.findFive(x, y);
			}
		}
	}
	
	public Move computeAIMove(int x, int y) {
		// Calculate weight after previous move by opponent
		this.lastHandWeight(x, y);
		this.findWeight();
		
		int max = -1000;
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				if (board[i][j] == 0) {
					if (max < aiWeight[i][j]) {
						x = i;
						y = j;
						max = aiWeight[i][j];
					}
				}
			}
		}
		
		return new Move(x, y);
	}
	
}
