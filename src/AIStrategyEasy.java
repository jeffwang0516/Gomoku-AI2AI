import java.awt.List;
import java.util.ArrayList;

class Score {
	public static int ONE = 10;
	public static int TWO = 100;
	public static int THREE = 1000;
	public static int FOUR = 100000;
	public static int FIVE = 10000000;
	public static int BLOCKED_ONE = 1;
	public static int BLOCKED_TWO = 10;
	public static int BLOCKED_THREE = 100;
	public static int BLOCKED_FOUR = 10;
	
}

public class AIStrategyEasy implements AIStrategy{
	
	private int myColor = Constants.COLOR_BLACK;
	private int opponentColor = Constants.COLOR_WHITE;
	private int[][] board;
	
	public AIStrategyEasy(int[][] board) {
		this.board = board;
		
	}
	
	@Override
	public Move computeAIMove(Move previousMove) {

		int x = previousMove.getX();
		int y = previousMove.getY();
		// PLAYER 1 AI Defines Here
		
		
		
		
		
		
		
		return minimax(2); // return next step by new Move(x,y)
	}
	// Max min
	
	Move minimax(int deep) {
		int best = Integer.MIN_VALUE;
		Move bestMove = new Move(0, 0);
		ArrayList<Move> moves = generateNextSteps(deep);
		
		for(int i=0; i<moves.size(); i++) {
			Move point = moves.get(i);
			board[point.getX()][point.getY()] = myColor;
			int scoreMin = min(deep-1);
			if(scoreMin >= best) {
				best = scoreMin;
				bestMove = point;
			}
			board[point.getX()][point.getY()] = Constants.EMPTY;
		}
		
		return bestMove;
	}
	
	private int min(int deep) {
		int score = evaluateBoard(myColor) - evaluateBoard(opponentColor);
		
		if(deep<=0 || checkIfWin()) {
			return score;
		}
		
		int best = Integer.MAX_VALUE;
		ArrayList<Move> moves = generateNextSteps(deep);
		
		for(int i=0; i<moves.size(); i++) {
			Move point = moves.get(i);
			board[point.getX()][point.getY()] = opponentColor;
			int scoreMax = max(deep-1);
			board[point.getX()][point.getY()] = Constants.EMPTY;
			
			if(scoreMax < best) {
				best = scoreMax;
			}
		}
		return best;
	}
	
	private int max(int deep) {
		int score = evaluateBoard(myColor) - evaluateBoard(opponentColor);
		
		if(deep<=0 || checkIfWin()) {
			return score;
		}
		
		int best = Integer.MIN_VALUE;
		ArrayList<Move> moves = generateNextSteps(deep);
		
		for(int i=0; i<moves.size(); i++) {
			Move point = moves.get(i);
			board[point.getX()][point.getY()] = myColor;
			int scoreMin = min(deep-1);
			board[point.getX()][point.getY()] = Constants.EMPTY;
			
			if(scoreMin > best) {
				best = scoreMin;
			}
		}
		return best;
	}
	
	private boolean checkIfWin() {
		for(int i=0;i<GameController.BOARD_SIZE_X;i++) {
			for(int j=0;j<GameController.BOARD_SIZE_Y;j++) {
				if(board[i][j] != Constants.EMPTY) {
					if(checkIfFive(i, j)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	private boolean checkIfFive(int x, int y) {
		
		
		int count;
		int i, j;

		count = 1; // Vertical
		for (i = 1; checkXYInBound(x, y + i) && board[x][y + i] == board[x][y]; i++)
			count++;
		for (j = -1; checkXYInBound(x, y + j) && board[x][y + j] == board[x][y]; j--)
			count++;
		if (count >= 5) {
			return true;
		}

		count = 1; // Horizontal
		for (i = 1; checkXYInBound(x + i, y) && board[x + i][y] == board[x][y]; i++)
			count++;
		for (j = -1; checkXYInBound(x + j, y) && board[x + j][y] == board[x][y]; j--)
			count++;
		if (count >= 5) {
			return true;
		}

		count = 1; // BackSlash
		for (i = 1; checkXYInBound(x + i, y + i) && board[x + i][y + i] == board[x][y]; i++)
			count++;
		for (j = -1; checkXYInBound(x + j, y + j)
				&& board[x + j][y + j] == board[x][y]; j--)
			count++;
		if (count >= 5) {
			return true;
		}

		count = 1; // Slash
		for (i = 1; checkXYInBound(x - i, y + i) && board[x - i][y + i] == board[x][y]; i++)
			count++;
		for (j = -1; checkXYInBound(x - j, y + j)
				&& board[x - j][y + j] == board[x][y]; j--)
			count++;
		if (count >= 5) {
			return true;
		}

		
		
		return false;
	}
	// Evaluate whole board
	private int evaluateBoard(int playerColor) {
		int result = 0;
		
		// eval -
		for(int i=0;i<GameController.BOARD_SIZE_X; i++) {
			result += evaluateRow(board[i], playerColor);
		}
		// eval |
		for(int i=0;i<GameController.BOARD_SIZE_Y; i++) {
			int[] r = new int[GameController.BOARD_SIZE_X];
			for(int j=0;j<GameController.BOARD_SIZE_X; j++) {
				
				r[j] = board[j][i];
				
			}
			result += evaluateRow(r, playerColor);
		}
		// eval \
		// Assume board is square
		int boardSize = GameController.BOARD_SIZE_X;
		for(int i=0;i<boardSize; i++) {
			int size = boardSize - i;
			int[] r = new int[size];
			for(int j=0; j < size; j++) {
				r[j] = board[i+j][j];
			}
			result += evaluateRow(r, playerColor);
		}
		for(int i=1;i<boardSize; i++) {
			int size = boardSize - i;
			int[] r = new int[size];
			for(int j=0; j < size; j++) {
				r[j] = board[j][i+j];
			}
			result += evaluateRow(r, playerColor);
		}
		
		// eval /
		for(int i=0;i<boardSize; i++) {
			int size = i+1;
			int[] r = new int[size];
			for(int j=0; j < size; j++) {
				r[j] = board[i-j][j];
			}
			result += evaluateRow(r, playerColor);
		}
		for(int i=1;i<boardSize; i++) {
			int size = boardSize - i;
			int[] r = new int[size];
			for(int j=0; j < size; j++) {
				r[j] = board[i+j][boardSize-j-1];
			}
			result += evaluateRow(r, playerColor);
		}
		
		return result;
	}
	private int evaluateRow(int row[], int playerColor) {
		int result = 0;
		int count = 0;
		int block = 0;
		int emptyPosition = 0;
		
		for(int i=0; i<row.length; i++) {
			if(row[i] == playerColor) {
				count = 1;
				block = 0;
				emptyPosition = 0;
				
				if(i==0) block=1;
				else if(row[i-1] != Constants.EMPTY) block=1;
				
				for(i=i+1;i<row.length; i++) {
					if(row[i] == playerColor) {
						count++;
					}else if(emptyPosition == 0 && i < row.length-1 && row[i] == Constants.EMPTY && row[i+1] == playerColor) {
						emptyPosition = count;
					} else {
						break;
					}
				}
				
				if(i == row.length || row[i] != Constants.EMPTY) block++;
				result += scoreOfPattern(count, block, emptyPosition);
				
			}
		}
		
		return 0;
	}
	private ArrayList<Move> generateNextSteps(int deep) {
		ArrayList<Move> fives = new ArrayList<Move>();
		ArrayList<Move> fours = new ArrayList<Move>();
		ArrayList<Move> twothrees = new ArrayList<Move>();
		ArrayList<Move> threes = new ArrayList<Move>();
		ArrayList<Move> twos = new ArrayList<Move>();
		ArrayList<Move> neighbors = new ArrayList<Move>();
		ArrayList<Move> nextNeighbors = new ArrayList<Move>();
		
		for(int i=0; i<board.length; i++) {
			for(int j=0; j<board[i].length;j++) {
				if(board[i][j] == 0) {
					Move point = new Move(i, j);
					if(hasNeighbor(point, 1, 1) ) {
						int myScore = calScoreOfPoint(point, Constants.COLOR_BLACK);
						int opponentScore = calScoreOfPoint(point, Constants.COLOR_WHITE);
						
						if(myScore >= Score.FIVE) {
							ArrayList<Move> res = new ArrayList<Move>();
							res.add(point);
							return res;
						} else if(opponentScore >= Score.FIVE) {
							fives.add(point);
						} else if(myScore >= Score.FOUR) {
							fours.add(0, point);
						} else if(opponentScore >= Score.FOUR) {
			            		fours.add(point);
			            } else if(myScore >= 2*Score.THREE) {
				            twothrees.add(0, point);
				        } else if(opponentScore >= 2*Score.THREE) {
				            twothrees.add(point);
				        } else if(myScore >= Score.THREE) {
				            threes.add(0, point);
				        } else if(opponentScore >= Score.THREE) {
				            threes.add(point);
				        } else if(myScore >= Score.TWO) {
				            twos.add(0, point);
				        } else if(opponentScore >= Score.TWO) {
				            twos.add(point);
				        } else {
				            neighbors.add(point);
				        }
					} else if(deep >= 2 && hasNeighbor(point, 2, 2)) {
						nextNeighbors.add(point);
					}
				}
			}
		}
		
		if(!fives.isEmpty()) {
			ArrayList<Move> result = new ArrayList<Move>();
			result.add(fives.get(0));
			return result;
		}
		
		if(!fours.isEmpty()) return fours;
		
		if(!twothrees.isEmpty()) return twothrees;
		
		threes.addAll(twos);
		threes.addAll(neighbors);
		threes.addAll(nextNeighbors);
		
		
		if(threes.size() > 20) {
			ArrayList<Move> result = new ArrayList<Move>();
			for(int i=0;i< threes.size() && i<20;i++) {
				result.add(threes.get(i));
			}
			return result;
			
		}
		
		return threes;
	}
	
	private int calScoreOfPoint(Move point, int playerColor) {
		int width = GameController.BOARD_SIZE_X;
		int height = GameController.BOARD_SIZE_Y;
		int result = 0;
		int count = 0;
		int block = 0;
		int emptyPosition = 0;
		
		
		//// 以該點出發，找 - | \ / 連線
		
		
		count = 1;
		block = 0; // Meet border or opponent
		emptyPosition = 0;
		
		
		
		// Find in right dir
		for(int i=point.getY() + 1; ; i++) {
			if(i>=width) {
				block++;
				break;
			}
			
			int pointValue = board[point.getX()][i];
			if(pointValue == Constants.EMPTY) {
				if(emptyPosition==0 && i < width-1 && board[point.getX()][i+1] == playerColor) {
					emptyPosition = count;
				} else {
					break;
				}
			} else if (pointValue == playerColor) {
				count++;
			} else {
				block++;
				break;
			}
		}
		
		// Find in Left dir
		for(int i=point.getY() - 1; ; i--) {
			if(i<0) {
				block++;
				break;
			}
			
			int pointValue = board[point.getX()][i];
			if(pointValue == Constants.EMPTY) {
				if(emptyPosition==0 && i > 0 && board[point.getX()][i-1] == playerColor) {
					emptyPosition = count;
				} else {
					break;
				}
			} else if (pointValue == playerColor) {
				count++;
			} else {
				block++;
				break;
			}
		}
		result += scoreOfPattern(count, block, emptyPosition);
		
		// Find in down dir
		count = 1;
		block = 0; // Meet border or opponent
		emptyPosition = 0;
		
		for(int i=point.getX() + 1; ; i++) {
			if(i>=height) {
				block++;
				break;
			}
			
			int pointValue = board[i][point.getY()];
			if(pointValue == Constants.EMPTY) {
				if(emptyPosition==0 && i < height-1 && board[i+1][point.getY()] == playerColor) {
					emptyPosition = count;
				} else {
					break;
				}
			} else if (pointValue == playerColor) {
				count++;
			} else {
				block++;
				break;
			}
		}
		
		// Find in up dir
		for(int i=point.getX() - 1; ; i--) {
			if(i<0) {
				block++;
				break;
			}
			
			int pointValue = board[i][point.getY()];
			if(pointValue == Constants.EMPTY) {
				if(emptyPosition==0 && i > 0 && board[i-1][point.getY()] == playerColor) {
					emptyPosition = count;
				} else {
					break;
				}
			} else if (pointValue == playerColor) {
				count++;
			} else {
				block++;
				break;
			}
		}
		result += scoreOfPattern(count, block, emptyPosition);
		
		// Find in \ down dir
		count = 1;
		block = 0; // Meet border or opponent
		emptyPosition = 0;
		
		for(int i=1; ; i++) {
			int x = point.getX() + i;
			int y = point.getY() + i;
			
			if(!checkXYInBound(x, y)) {
				block++;
				break;
			}
			int pointValue = board[x][y];
			if(pointValue == Constants.EMPTY) {
				if(emptyPosition==0 && x<height-1 && y<width-1 && board[x+1][y+1] == playerColor) {
					emptyPosition = count;
				} else {
					break;
				}
			} else if (pointValue == playerColor) {
				count++;
			} else {
				block++;
				break;
			}
		}
		
		// Find in \ up dir
		for(int i=1; ; i++) {
			int x = point.getX() - i;
			int y = point.getY() - i;
			
			if(!checkXYInBound(x, y)) {
				block++;
				break;
			}
			int pointValue = board[x][y];
			if(pointValue == Constants.EMPTY) {
				if(emptyPosition==0 && x>0 && y>0 && board[x-1][y-1] == playerColor) {
					emptyPosition = count;
				} else {
					break;
				}
			} else if (pointValue == playerColor) {
				count++;
			} else {
				block++;
				break;
			}
		}
		result += scoreOfPattern(count, block, emptyPosition);
		
		// Find in / up dir
		count = 1;
		block = 0; // Meet border or opponent
		emptyPosition = 0;
		
		for(int i=1; ; i++) {
			int x = point.getX() + i;
			int y = point.getY() - i;
			
			if(!checkXYInBound(x, y)) {
				block++;
				break;
			}
			int pointValue = board[x][y];
			if(pointValue == Constants.EMPTY) {
				if(emptyPosition==0 && x<height-1 && y>0 && board[x+1][y-1] == playerColor) {
					emptyPosition = count;
				} else {
					break;
				}
			} else if (pointValue == playerColor) {
				count++;
			} else {
				block++;
				break;
			}
		}
		
		for(int i=1; ; i++) {
			int x = point.getX() - i;
			int y = point.getY() + i;
			
			if(!checkXYInBound(x, y)) {
				block++;
				break;
			}
			int pointValue = board[x][y];
			if(pointValue == Constants.EMPTY) {
				if(emptyPosition==0 && x>0 && y<width-1 && board[x-1][y+1] == playerColor) {
					emptyPosition = count;
				} else {
					break;
				}
			} else if (pointValue == playerColor) {
				count++;
			} else {
				block++;
				break;
			}
		}
		
		result += scoreOfPattern(count, block, emptyPosition);
		
		return result;
	}
	// Generate next possible steps
	
	
	public boolean checkXYInBound(int x, int y) {
		
		return (x < GameController.BOARD_SIZE_X && x >= 0 && y < GameController.BOARD_SIZE_Y && y >= 0);
	}	
	
	private boolean hasNeighbor(Move point, int distance, int numOfNeighbors) {
		int startX = point.getX() - distance;
		int endX = point.getX() + distance;
		int startY = point.getY() - distance;
		int endY = point.getY() + distance;
		
		for (int i=startX; i<=endX; i++) {
			for(int j=startY; j<=endY; j++) {
				if( checkXYInBound(i, j) && !(i == point.getX() && j == point.getY()) ) {
					if( board[i][j] != 0) {
						numOfNeighbors--;
						if(numOfNeighbors <= 0) return true;
					}
				}
			}
		}
		
		return false;
				
	}

	private int scoreOfPattern(int count, int block, int empty) {
	//没有空位
	  if(empty == 0) {
	    if(count >= 5) return Score.FIVE;
	    if(block == 0) {
	      switch(count) {
	        case 1: return Score.ONE;
	        case 2: return Score.TWO;
	        case 3: return Score.THREE;
	        case 4: return Score.FOUR;
	      }
	    }

	    if(block == 1) {
	      switch(count) {
	        case 1: return Score.BLOCKED_ONE;
	        case 2: return Score.BLOCKED_TWO;
	        case 3: return Score.BLOCKED_THREE;
	        case 4: return Score.BLOCKED_FOUR;
	      }
	    }

	  } else if(empty == 1 || empty == count-1) {
	    //第二个是空位
	    if(count >= 6) {
	      return Score.FIVE;
	    }
	    if(block == 0) {
	      switch(count) {
	        case 2: return Score.TWO;
	        case 3:
	        case 4: return Score.THREE;
	        case 5: return Score.FOUR;
	      }
	    }

	    if(block == 1) {
	      switch(count) {
	        case 2: return Score.BLOCKED_TWO;
	        case 3: return Score.BLOCKED_THREE;
	        case 4: return Score.THREE;
	        case 5: return Score.BLOCKED_FOUR;
	      }
	    }
	  } else if(empty == 2 || empty == count-2) {
	    //第二个是空位
	    if(count >= 7) {
	      return Score.FIVE;
	    }
	    if(block == 0) {
	      switch(count) {
	        case 3:
	        case 4:
	        case 5: return Score.THREE;
	        case 6: return Score.FOUR;
	      }
	    }

	    if(block == 1) {
	      switch(count) {
	        case 3: return Score.BLOCKED_THREE;
	        case 4: return Score.BLOCKED_FOUR;
	        case 5: return Score.BLOCKED_FOUR;
	        case 6: return Score.FOUR;
	      }
	    }

	    if(block == 2) {
	      switch(count) {
	        case 4:
	        case 5:
	        case 6: return Score.BLOCKED_FOUR;
	      }
	    }
	  } else if(empty == 3 || empty == count-3) {
	    if(count >= 8) {
	      return Score.FIVE;
	    }
	    if(block == 0) {
	      switch(count) {
	        case 4:
	        case 5: return Score.THREE;
	        case 6: return Score.THREE*2;
	        case 7: return Score.FOUR;
	      }
	    }

	    if(block == 1) {
	      switch(count) {
	        case 4:
	        case 5:
	        case 6: return Score.BLOCKED_FOUR;
	        case 7: return Score.FOUR;
	      }
	    }

	    if(block == 2) {
	      switch(count) {
	        case 4:
	        case 5:
	        case 6:
	        case 7: return Score.BLOCKED_FOUR;
	      }
	    }
	  } else if(empty == 4 || empty == count-4) {
	    if(count >= 9) {
	      return Score.FIVE;
	    }
	    if(block == 0) {
	      switch(count) {
	        case 5:
	        case 6:
	        case 7:
	        case 8: return Score.FOUR;
	      }
	    }

	    if(block == 1) {
	      switch(count) {
	        case 4:
	        case 5:
	        case 6:
	        case 7: return Score.BLOCKED_FOUR;
	        case 8: return Score.FOUR;
	      }
	    }

	    if(block == 2) {
	      switch(count) {
	        case 5:
	        case 6:
	        case 7:
	        case 8: return Score.BLOCKED_FOUR;
	      }
	    }
	  } else if(empty == 5 || empty == count-5) {
	    return Score.FIVE;
	  }

	  return 0;
	}
}

