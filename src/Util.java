
public class Util {

	
	public static int calScoreOfPoint(int[][] board, Move point, int playerColor) {
		int width = GameController.BOARD_SIZE_X;
		int height = GameController.BOARD_SIZE_Y;
		int result = 0;
		int count = 0;
		int block = 0;
		int emptyPosition = -1;
		int secondCount = 0; //Another dir
		
		
		//// 以該點出發，找 - | \ / 連線
		
		
		count = 1;
		block = 0; // Meet border or opponent
		emptyPosition = -1;
		secondCount = 0;
		
		
		// Find in right dir
		for(int i=point.getY() + 1; ; i++) {
			if(i>=width) {
				block++;
				break;
			}
			
			int pointValue = board[point.getX()][i];
			if(pointValue == Constants.EMPTY) {
				if(emptyPosition==-1 && i < width-1 && board[point.getX()][i+1] == playerColor) {
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
				if(emptyPosition==-1 && i > 0 && board[point.getX()][i-1] == playerColor) {
					emptyPosition = 0;
				} else {
					break;
				}
			} else if (pointValue == playerColor) {
				secondCount++;
				if(emptyPosition!=-1)
					emptyPosition++;
			} else {
				block++;
				break;
			}
		}
		count += secondCount;
		result += scoreOfPattern(count, block, emptyPosition);
		
		// Find in down dir
		count = 1;
		block = 0; // Meet border or opponent
		emptyPosition = -1;
		secondCount = 0;
		
		for(int i=point.getX() + 1; ; i++) {
			if(i>=height) {
				block++;
				break;
			}
			
			int pointValue = board[i][point.getY()];
			if(pointValue == Constants.EMPTY) {
				if(emptyPosition==-1 && i < height-1 && board[i+1][point.getY()] == playerColor) {
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
				if(emptyPosition==-1 && i > 0 && board[i-1][point.getY()] == playerColor) {
					emptyPosition = 0;
				} else {
					break;
				}
			} else if (pointValue == playerColor) {
				secondCount++;
				if(emptyPosition!=-1)
					emptyPosition++;
			} else {
				block++;
				break;
			}
		}
		count += secondCount;
		result += scoreOfPattern(count, block, emptyPosition);
		
		// Find in \ down dir
		count = 1;
		block = 0; // Meet border or opponent
		emptyPosition = -1;
		secondCount = 0;
		
		for(int i=1; ; i++) {
			int x = point.getX() + i;
			int y = point.getY() + i;
			
			if(!checkXYInBound(x, y)) {
				block++;
				break;
			}
			int pointValue = board[x][y];
			if(pointValue == Constants.EMPTY) {
				if(emptyPosition==-1 && x<height-1 && y<width-1 && board[x+1][y+1] == playerColor) {
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
				if(emptyPosition==-1 && x>0 && y>0 && board[x-1][y-1] == playerColor) {
					emptyPosition = 0;
				} else {
					break;
				}
			} else if (pointValue == playerColor) {
				secondCount++;
				if(emptyPosition!=-1)
					emptyPosition++;
			} else {
				block++;
				break;
			}
		}
		count += secondCount;
		result += scoreOfPattern(count, block, emptyPosition);
		
		// Find in / up dir
		count = 1;
		block = 0; // Meet border or opponent
		emptyPosition = -1;
		secondCount = 0;
		
		for(int i=1; ; i++) {
			int x = point.getX() + i;
			int y = point.getY() - i;
			
			if(!checkXYInBound(x, y)) {
				block++;
				break;
			}
			int pointValue = board[x][y];
			if(pointValue == Constants.EMPTY) {
				if(emptyPosition==-1 && x<height-1 && y>0 && board[x+1][y-1] == playerColor) {
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
				if(emptyPosition==-1 && x>0 && y<width-1 && board[x-1][y+1] == playerColor) {
					emptyPosition = 0;
				} else {
					break;
				}
			} else if (pointValue == playerColor) {
				secondCount++;
				if(emptyPosition!=-1)
					emptyPosition++;
			} else {
				block++;
				break;
			}
		}
		count += secondCount;
		
		result += scoreOfPattern(count, block, emptyPosition);
		
		return result;
	}
	
	public static int fixScore(int score) {
		  if(score < Score.FOUR && score >= Score.BLOCKED_FOUR) {
			if(score >= Score.BLOCKED_FOUR && score < (Score.BLOCKED_FOUR + Score.THREE)) {
			      //单独冲四，意义不大
			      return Score.THREE;
			    } else if(score >= Score.BLOCKED_FOUR + Score.THREE && score < Score.BLOCKED_FOUR * 2) {
			      return Score.FOUR;  //冲四活三，比双三分高，相当于自己形成活四
			    } else {
			      //双冲四 比活四分数也高
			      return Score.FOUR * 2;
			    }
			  }
			  return score;
		}
	public static int checkIfWin(int[][] board) {
		for(int i=0;i<GameController.BOARD_SIZE_X;i++) {
			for(int j=0;j<GameController.BOARD_SIZE_Y;j++) {
				if(board[i][j] != Constants.EMPTY) {
					int status = checkIfFive(board, i, j) ;
					if(status != 0) {
						return status ;
					}
				}
			}
		}
		return 0;
	}
	public static int checkIfFive(int[][] board, int x, int y) {
		
		
		int count;
		int i, j;

		count = 1; // Vertical
		for (i = 1; checkXYInBound(x, y + i) && board[x][y + i] == board[x][y]; i++)
			count++;
		for (j = -1; checkXYInBound(x, y + j) && board[x][y + j] == board[x][y]; j--)
			count++;
		if (count >= 5) {
			return board[x][y];
		}

		count = 1; // Horizontal
		for (i = 1; checkXYInBound(x + i, y) && board[x + i][y] == board[x][y]; i++)
			count++;
		for (j = -1; checkXYInBound(x + j, y) && board[x + j][y] == board[x][y]; j--)
			count++;
		if (count >= 5) {
			return board[x][y];
		}

		count = 1; // BackSlash
		for (i = 1; checkXYInBound(x + i, y + i) && board[x + i][y + i] == board[x][y]; i++)
			count++;
		for (j = -1; checkXYInBound(x + j, y + j)
				&& board[x + j][y + j] == board[x][y]; j--)
			count++;
		if (count >= 5) {
			return board[x][y];
		}

		count = 1; // Slash
		for (i = 1; checkXYInBound(x - i, y + i) && board[x - i][y + i] == board[x][y]; i++)
			count++;
		for (j = -1; checkXYInBound(x - j, y + j)
				&& board[x - j][y + j] == board[x][y]; j--)
			count++;
		if (count >= 5) {
			return board[x][y];
		}

		
		
		return 0;
	}
	public static boolean checkXYInBound(int x, int y) {
		
		return (x < GameController.BOARD_SIZE_X && x >= 0 && y < GameController.BOARD_SIZE_Y && y >= 0);
	}	
	
	public static boolean hasNeighbor(int[][] board, Move point, int distance, int numOfNeighbors) {
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

	private static int scoreOfPattern(int count, int block, int empty) {
		  //没有空位
		  if(empty <= 0) {
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
		    //第1个是空位
		    if(count >= 6) {
		      return Score.FIVE;
		    }
		    if(block == 0) {
		      switch(count) {
		        case 2: return Score.TWO;
		        case 3: return Score.THREE;
		        case 4: return Score.BLOCKED_FOUR;
		        case 5: return Score.FOUR;
		      }
		    }

		    if(block == 1) {
		      switch(count) {
		        case 2: return Score.BLOCKED_TWO;
		        case 3: return Score.BLOCKED_THREE;
		        case 4: return Score.BLOCKED_FOUR;
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
		        case 3: return Score.THREE;
		        case 4: 
		        case 5: return Score.BLOCKED_FOUR;
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
		        case 6: return Score.BLOCKED_FOUR;
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
