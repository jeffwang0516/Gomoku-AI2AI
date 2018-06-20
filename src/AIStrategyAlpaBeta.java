import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AIStrategyAlpaBeta implements AIStrategy{
	private int[][] board;
	private int myColor = Constants.COLOR_WHITE;
	private int opponentColor = Constants.COLOR_BLACK;
	private int[][] scoreSelf;
	private int[][] scoreOpponent;
	private ArrayList<Move> steps = new ArrayList<Move>();
	int minimaxDepth = 4;
	
//	private AI aiInstance;
	public AIStrategyAlpaBeta(int[][] board) {
		this.board = board;
//		this.aiInstance = new AI(board);
		
		initScore();
	}	
	private void initScore() {
		scoreSelf = new int[GameController.BOARD_SIZE_X][GameController.BOARD_SIZE_Y];
		scoreOpponent = new int[GameController.BOARD_SIZE_X][GameController.BOARD_SIZE_Y];
		
		for(int i=0;i<board.length;i++) {
		    for(int j=0;j<board[i].length;j++) {
		    		Move point = new Move(i, j);
				  if(board[i][j] == Constants.EMPTY) {
					  	
				    if(Util.hasNeighbor(this.board, point, 2, 2)) { 
				      int cs = Util.calScoreOfPoint(this.board, point, myColor);
				      int hs = Util.calScoreOfPoint(this.board, point, opponentColor);
				      this.scoreSelf[i][j] = cs;
				      this.scoreOpponent[i][j] = hs;
				    }
				
				  } else if (board[i][j] == myColor) { 
				    this.scoreSelf[i][j] = Util.calScoreOfPoint(this.board, point, myColor);
				    this.scoreOpponent[i][j] = 0;
				  } else if (board[i][j] == opponentColor) { 
				    this.scoreSelf[i][j] = Util.calScoreOfPoint(this.board, point, opponentColor);
				    this.scoreOpponent[i][j] = 0;
				  }
		    }
		  }
	}
	
	@Override
	public Move computeAIMove(Move previousMove) {
		
		int x = previousMove.getX();
		int y = previousMove.getY();
		// PLAYER 2 AI Defines Here
		updateScore(previousMove);
		
		
		
		
		Move newMove = minimax(minimaxDepth);
		updateScore(newMove);
		
		
		return newMove; // return next step by new Move(x,y)
	}
	
	Move minimax(int deep) {
		int best = Integer.MIN_VALUE;
		ArrayList<Move> bestMoves = new ArrayList<Move>();
		ArrayList<Move> moves = generateNextSteps(myColor, deep);
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		for(int i=0; i<moves.size(); i++) {
			Move point = moves.get(i);
			board[point.getX()][point.getY()] = myColor;
			updateScore(point);
			int scoreMin = min(deep-1, alpha, beta);
			board[point.getX()][point.getY()] = Constants.EMPTY;
			updateScore(point);
			
			if(scoreMin == best) {
				point.score = best;
				bestMoves.add(point);
			}
			if(scoreMin >= best) {
				best = scoreMin;
				bestMoves = new ArrayList<Move>();
				point.score = best;
				bestMoves.add(point);
			}
			if( scoreMin >= beta) {
				break;
			}
			alpha = Integer.max(alpha, scoreMin);
			
		}
		
//		for( Move m: bestMoves){
//			System.out.println("Possible: "+m.getX()+", "+m.getY()+" Score="+m.score);
//		}
//		System.out.println("Score = "+best);
		return bestMoves.get((int)(Math.floor(Math.random() * bestMoves.size())));
	}
	
	private int min(int deep, int alpha, int beta) {
		int score = evaluate(opponentColor);//(evaluateBoard(myColor) - evaluateBoard(opponentColor));
		
		if(deep<=0 || Util.checkIfWin(board) > 0) {
			return score;
		}
		
		int best = Integer.MAX_VALUE;
		ArrayList<Move> moves = generateNextSteps(opponentColor, deep);
		
		for(int i=0; i<moves.size(); i++) {
			Move point = moves.get(i);
			board[point.getX()][point.getY()] = opponentColor;
			updateScore(point);
			steps.add(point);
			int scoreMax = (int) (max(deep-1, alpha, beta) * 0.8);
			
			
			board[point.getX()][point.getY()] = Constants.EMPTY;
			updateScore(point);
			steps.remove(point);
			if(scoreMax < best) {
				best = scoreMax;
			}

			if( best < alpha) {
				return best;
			}
			beta = Integer.min(beta, best);
		}
		return best;
	}
	
	private int max(int deep, int alpha, int beta) {
		int score = evaluate(myColor);//evaluateBoard(myColor) - evaluateBoard(opponentColor);
		
		if(deep<=0 || Util.checkIfWin(board) > 0) {
			
			return score;
		}
		
		int best = Integer.MIN_VALUE;
		ArrayList<Move> moves = generateNextSteps(myColor, deep);
		
		for(int i=0; i<moves.size(); i++) {
			Move point = moves.get(i);
			board[point.getX()][point.getY()] = myColor;
			updateScore(point);
			steps.add(point);
			int scoreMin = (int) (min(deep-1, alpha, beta) * 0.8);
			board[point.getX()][point.getY()] = Constants.EMPTY;
			updateScore(point);
			steps.remove(point);
			
			if(scoreMin > best) {
				best = scoreMin;
			}
			if( best > beta) {
				return best;
			}
			alpha = Integer.max(alpha, best);
		}

		return best;
	}
		
	private ArrayList<Move> generateNextSteps(int playerColor, int deep) {
		// Generate next possible steps
		ArrayList<Move> fives = new ArrayList<Move>();
		ArrayList<Move> selffours = new ArrayList<Move>();
		ArrayList<Move> oppofours = new ArrayList<Move>();
		ArrayList<Move> selfBlockfours = new ArrayList<Move>();
		ArrayList<Move> oppoBlockfours = new ArrayList<Move>();
		ArrayList<Move> selftwothrees = new ArrayList<Move>();
		ArrayList<Move> oppotwothrees = new ArrayList<Move>();
		ArrayList<Move> selfthrees = new ArrayList<Move>();
		ArrayList<Move> oppothrees = new ArrayList<Move>();
		ArrayList<Move> selftwos = new ArrayList<Move>();
		ArrayList<Move> oppotwos = new ArrayList<Move>();
		ArrayList<Move> neighbors = new ArrayList<Move>();
//		ArrayList<Move> nextNeighbors = new ArrayList<Move>();
		
		for(int i=0; i<board.length; i++) {
			for(int j=0; j<board[i].length;j++) {
				if(board[i][j] == 0) {
					Move point = new Move(i, j);
					int num = steps.size() >= 6 ? 2 : 1;
					if(Util.hasNeighbor(board, point, num, num) ) {
						int myScore = scoreSelf[i][j];//calScoreOfPoint(point, Constants.COLOR_BLACK);
						int opponentScore = scoreOpponent[i][j];//calScoreOfPoint(point, Constants.COLOR_WHITE);
//						int opponentScore = calScoreOfPoint(point, Constants.COLOR_BLACK);
//						int  myScore= calScoreOfPoint(point, Constants.COLOR_WHITE);
						
						int maxScore = Integer.max(myScore, opponentScore);
						point.score = maxScore;
						
						if(myScore >= Score.FIVE) {
//							ArrayList<Move> res = new ArrayList<Move>();
//							res.add(point);
//							return res;
							fives.add(point);
						} else if(opponentScore >= Score.FIVE) {
							fives.add(point);
						} else if(myScore >= Score.FOUR) {
							selffours.add(point);
						} else if(opponentScore >= Score.FOUR) {
			            		oppofours.add(point);
			            } else if(myScore >= Score.BLOCKED_FOUR) {
							selfBlockfours.add(point);
						} else if(opponentScore >= Score.BLOCKED_FOUR) {
			            		oppoBlockfours.add(point);
			            } else if(myScore >= 2*Score.THREE) {
				            selftwothrees.add(point);
				        } else if(opponentScore >= 2*Score.THREE) {
				            oppotwothrees.add(point);
				        } else if(myScore >= Score.THREE) {
				            selfthrees.add(point);
				        } else if(opponentScore >= Score.THREE) {
				            oppothrees.add(point);
				        } else if(myScore >= Score.TWO) {
				            selftwos.add(point);
				        } else if(opponentScore >= Score.TWO) {
				            oppotwos.add(point);
				            
				        } else {
				            neighbors.add(point);
				        }
					}
//					} else if(deep >= 2 && hasNeighbor(point, 2, 2)) {
//						nextNeighbors.add(point);
//					}
				}
			}
		}
		
		if(!fives.isEmpty()) {
//			System.out.println("five");
//			ArrayList<Move> result = new ArrayList<Move>();
//			
//			result.add(fives.get(0));
//			return result;
			return fives;
		}
		
		
		if(playerColor == myColor && !selffours.isEmpty()) {return selffours;}
		if(playerColor == opponentColor && !oppofours.isEmpty()) {return oppofours;}
		
		if(playerColor == myColor && !oppofours.isEmpty() && selfBlockfours.isEmpty()) {return oppofours;}
		if(playerColor == opponentColor && !selffours.isEmpty() && oppoBlockfours.isEmpty()) {return oppofours;}
		
		ArrayList<Move> fours = new ArrayList<Move>();
		if(playerColor==myColor) {
			fours.addAll(selffours);
			fours.addAll(oppofours);
		} else {
			fours.addAll(oppofours);
			fours.addAll(selffours);
		}
		
		ArrayList<Move> blockedfours = new ArrayList<Move>();
		if(playerColor==myColor) {
			blockedfours.addAll(selfBlockfours);
			blockedfours.addAll(oppoBlockfours);
		} else {
			blockedfours.addAll(oppoBlockfours);
			blockedfours.addAll(selfBlockfours);
		}
		
		if(!fours.isEmpty()) {
			fours.addAll(blockedfours);
			return fours;
		}
		
		ArrayList<Move> result = new ArrayList<Move>();
		if(playerColor==myColor) {
			result.addAll(selftwothrees);
			result.addAll(oppotwothrees);
			result.addAll(selfBlockfours);
			result.addAll(oppoBlockfours);
			result.addAll(selfthrees);
			result.addAll(oppothrees);
		} else {
			result.addAll(oppotwothrees);
			result.addAll(selftwothrees);
			result.addAll(oppoBlockfours);
			result.addAll(selfBlockfours);
			result.addAll(oppothrees);
			result.addAll(selfthrees);
		}
		
		Collections.sort(result, new Comparator<Move>(){

			@Override
			public int compare(Move o1, Move o2) {
				// TODO Auto-generated method stub
				return o2.score - o1.score;
			}
		});
		
		if( !selftwothrees.isEmpty() || !oppotwothrees.isEmpty()) {
			return result;
		}
		
		ArrayList<Move> twos = new ArrayList<Move>();
		if(playerColor==myColor) {
			twos.addAll(selftwos);
			twos.addAll(oppotwos);
		} else {
			twos.addAll(oppotwos);
			twos.addAll(selftwos);
		}
		
		Collections.sort(twos, new Comparator<Move>(){

			@Override
			public int compare(Move o1, Move o2) {
				// TODO Auto-generated method stub
				return o2.score - o1.score;
			}
		});
		
		if(!twos.isEmpty()) {
			result.addAll(twos);
		} else {
			result.addAll(neighbors);
		}
		
		if(result.size() > 40) {
			ArrayList<Move> results = new ArrayList<Move>();
			for(int i=0;i< result.size() && i<40;i++) {
				results.add(result.get(i));
			}
			return results;
			
		}
		
		return result;
	}
	private int evaluate(int playerColor) {
		int selfScoreMax = 0;
		int opponentScoreMax = 0;
		
		for(int i=0;i<GameController.BOARD_SIZE_X;i++) {
			for(int j=0;j<GameController.BOARD_SIZE_Y;j++) {
				if(board[i][j] == myColor) {
					selfScoreMax = Math.max(scoreSelf[i][j], selfScoreMax);
				} else if (board[i][j] == opponentColor) {
					opponentScoreMax = Math.max(scoreOpponent[i][j], opponentScoreMax);
				}
			}
		}
		
		return selfScoreMax - opponentScoreMax;
		
	}
		
	private void updateScore(Move lastMove) {
		int radius = 6;
		int len = GameController.BOARD_SIZE_X;
		// -
		for(int i=-radius;i<radius;i++) {
		    int x = lastMove.getX(), y = lastMove.getY()+i;
		    if(y<0) continue;
		    if(y>=len) break;
		    update(x, y);
		  }

		  // |
		  
		  for(int i=-radius;i<radius;i++) {
		    int x = lastMove.getX()+i, y = lastMove.getY();
		    if(x<0) continue;
		    if(x>=len) break;
		    update(x, y);
		  }

		  // \
		  
		  for(int i=-radius;i<radius;i++) {
		    int x = lastMove.getX()+i, y = lastMove.getY()+i;
		    if(x<0 || y<0) continue;
		    if(x>=len || y>=len) break;
		    update(x, y);
		  }

		  // /
		  
		  for(int i=-radius;i<radius;i++) {
		    int x = lastMove.getX()+i, y = lastMove.getY()-i;
		    if(x<0 || y<0) continue;
		    if(x>=len || y>=len) continue;
		    update(x, y);
		  }
	}
	
	private void update(int x, int y) {
		int color = board[x][y];
		Move point = new Move(x, y);
		if( color != opponentColor) {
			int cs = Util.calScoreOfPoint(board, point, myColor);
			
			this.scoreSelf[x][y] = cs;
			
		} else {
			this.scoreSelf[x][y] = 0;
		}
		
		if(color != myColor) {
			int hs = Util.calScoreOfPoint(board, point, opponentColor);
			this.scoreOpponent[x][y] = hs;
		} else {
			this.scoreOpponent[x][y] = 0;
		}
	}
}
