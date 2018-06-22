import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.Hashtable;

public class AIStrategyABAndMustKill implements AIStrategy{
	
	private int myColor = Constants.COLOR_BLACK;
	private int opponentColor = Constants.COLOR_WHITE;
	private int[][] board;
	private ArrayList<Move> steps = new ArrayList<Move>();
	
	private int[][] scoreSelf;
	private int[][] scoreOpponent;
	
	int minimaxDepth = 4; // Should be even WHEN this is a max node
	int findKillDepth = 7;
	boolean findMustKillEnabled;
	
	private Dictionary<Long, Score> boardStatus = new Hashtable<Long, Score>();
	private ZobristHash zobristHash;
	private boolean isZobristEnabled = false;
	
	public AIStrategyABAndMustKill(int[][] board, boolean findMustKill) {
		this.board = board;
		this.findMustKillEnabled = findMustKill;
		
		initScore();
		
		zobristHash = new ZobristHash(myColor, opponentColor);
//		zobristHash.performAndGetHashValue(GameModel.firstStep, myColor);
		
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
	
		// PLAYER 1 AI Defines Here
		
		// Update score after opponent's move
		updateScore(previousMove);
		if(isZobristEnabled) zobristHash.performAndGetHashValue(previousMove, opponentColor);
		
		// Do minimax
		Move point = minimax(minimaxDepth);
		
		// Update score after own move
		updateScore(point);
		if(isZobristEnabled) zobristHash.performAndGetHashValue(point, myColor);
		// Step record
		steps.add(point);
		
		return point; // return next step by new Move(x,y)
	}
	
	public void putMove(Move point, int playerColor) {
		board[point.getX()][point.getY()] = playerColor;
		updateScore(point);
		if(isZobristEnabled) zobristHash.performAndGetHashValue(point, playerColor);
	}
	
	public void removeMove(Move point) {
		int originalColor = board[point.getX()][point.getY()];
		board[point.getX()][point.getY()] = Constants.EMPTY;
		updateScore(point);
		if(isZobristEnabled) zobristHash.performAndGetHashValue(point, originalColor);
	}
	
	
//	Move deepingMinimax(int deep) {
//		Move result=null;
//		for(int i=2;i<=deep;i+=2) {
//			result = minimax(i);
//			if(result.score >=Score.FOUR) {
//				System.out.println("Minimax Found in deep="+i);
//				return result;
//			}
//		}
//		return result;
//	}
	
	int MAX_SCORE = Score.THREE;
	int MIN_SCORE = Score.FOUR;
	// Find must kill step (FOURS or THREES)
	Move checkMustKillStep(int deep, boolean onlyFour) {
		int return_score = 0;
		
		// Setup search range
		if(onlyFour) {
			MAX_SCORE = Score.BLOCKED_FOUR;
		    MIN_SCORE = Score.FIVE;
		    return_score = Score.FOUR;
		} else {
			MAX_SCORE = Score.THREE;
		    MIN_SCORE = Score.BLOCKED_FOUR;
		    return_score = Score.THREE * 2;
		}
//		System.out.print(".");
		for(int i=1;i<=deep;i++) {
			
			lastMaxPoint = null;
			lastMinPoint = null;
			ArrayList<Move> result = max_kill(myColor, i);
			
			if(result == null) continue;
			if(!result.isEmpty()) {
				Move first = result.get(0);
//				System.out.println("Found must kill.");
				first.score = return_score;
//				System.out.println("SUCCESS deep="+i+" Score:"+first.score +" Out of:"+result.size() );
				return first;
			}
		}
		
		return null;
		
	}
	Move lastMaxPoint = null;
	ArrayList<Move> findMax(int playerColor, int SCORE){
		// SCORE: lower bound in finding max steps
		
		ArrayList<Move> fives = new ArrayList<Move>();
		ArrayList<Move> result = new ArrayList<Move>();
		
		for(int i=0;i<GameController.BOARD_SIZE_X;i++) {
			for(int j=0;j<GameController.BOARD_SIZE_Y;j++) {
				Move point = new Move(i, j);
				if(board[i][j] == Constants.EMPTY) {
					if(scoreOpponent[i][j] >= Score.FIVE) {
						point.score = Score.FIVE;
						if(playerColor == myColor) point.score *= -1;
						fives.add(point);
					} else if(scoreSelf[i][j] >= Score.FIVE) {
						point.score = Score.FIVE;
						if(playerColor == opponentColor) point.score *= -1;
						fives.add(point);
					} else {
						if( (lastMaxPoint==null) || (i == lastMaxPoint.getX() || j == lastMaxPoint.getY() 
								|| Math.abs(i-lastMaxPoint.getX()) == Math.abs(j-lastMaxPoint.getY()) ) ){
							if(playerColor == myColor) {
								point.score = scoreSelf[i][j];
							} else {
								point.score = scoreOpponent[i][j];
							}
							
							if(point.score >= SCORE) {
								result.add(point);
							}
						}
					}
				}
			}
			
		}
		
		if(!fives.isEmpty()) return fives;
		
		Collections.sort(result, new Comparator<Move>() {

			@Override
			public int compare(Move o1, Move o2) {
				// TODO Auto-generated method stub
				return o2.score-o1.score;
			}
		});
		
		return result;
	}
	Move lastMinPoint = null;
	ArrayList<Move> findMin(int playerColor, int score){
		ArrayList<Move> fives = new ArrayList<Move>();
		ArrayList<Move> fours = new ArrayList<Move>();
		ArrayList<Move> blockedfours = new ArrayList<Move>();
		ArrayList<Move> result = new ArrayList<Move>();
		
		for(int i=0;i<GameController.BOARD_SIZE_X;i++) {
			for(int j=0;j<GameController.BOARD_SIZE_Y;j++) {
				Move point = new Move(i, j);
				if(board[i][j] == Constants.EMPTY) {
					int score1,score2;
					
					if(playerColor==myColor) {
						score1 = scoreSelf[i][j];
						score2 = scoreOpponent[i][j];
					} else {
						score2 = scoreSelf[i][j];
						score1 = scoreOpponent[i][j];
					}
					
					if(score1>=Score.FIVE) {
						point.score = -score1;
						ArrayList<Move> res = new ArrayList<Move>();
						res.add(point);
						return res;
					}
					if(score2>=Score.FIVE) {
						point.score = score2;
						fives.add(point);
						continue;
					}
					
					if(score1>=Score.FOUR) {
						point.score = -score1;
						fours.add(0, point);
						continue;
					}
					if(score2>=Score.FOUR) {
						point.score = score2;
						fours.add(point);
						continue;
					}
					
					if(score1>=Score.BLOCKED_FOUR) {
						point.score = -score1;
						blockedfours.add(0, point);
						continue;
					}
					if(score2>=Score.BLOCKED_FOUR) {
						point.score = score2;
						blockedfours.add(point);
						continue;
					}
				}
			}
			
		}
		
		if(!fives.isEmpty()) return fives;
		
		if(!fours.isEmpty()) {
			fours.addAll(blockedfours);
			return fours;
		}
		
		result.addAll(blockedfours);
		Collections.sort(result, new Comparator<Move>() {

			@Override
			public int compare(Move o1, Move o2) {
				// TODO Auto-generated method stub
				return o2.score-o1.score;
			}
		});
		
		return result;
	}
	private int reverseRole(int playerColor) {
		return playerColor == myColor ? opponentColor : myColor;
	}
	ArrayList<Move> max_kill(int playerColor, int deep) {
		ArrayList<Move> result = new ArrayList<Move>();
		
		if(deep<0) return null;
		
		if(isZobristEnabled) {
			Score cachedScore = boardStatus.get(zobristHash.getCurrentHash());
			if(cachedScore != null) {
				if(cachedScore.deep >= deep) {
//					System.out.println("FOUND Cache KILL!");
					return cachedScore.stepsForKill;
				}
			}
		}
		
		ArrayList<Move> points = findMax(playerColor, MAX_SCORE);
		if(!points.isEmpty() && points.get(0).score >= Score.FOUR) {
			result.add(points.get(0));
			return result;
		}
		if(points.isEmpty()) return null;
		
		for(int i=0;i<points.size();i++) {
			Move point = points.get(i);
//			board[point.getX()][point.getY()] = playerColor;
//			updateScore(point);
			putMove(point, playerColor);
			
			if(!(point.score<=-Score.FIVE)) lastMaxPoint = point;
			ArrayList<Move> moves = min_kill(reverseRole(playerColor), deep-1);
//			board[point.getX()][point.getY()] = Constants.EMPTY;
//			updateScore(point);
			removeMove(point);
			
			if(moves != null) {
				// ----
				Score hashScore = new Score();
				hashScore.deep = deep;
				
				// ----
				if(!moves.isEmpty()) {
					moves.add(0, point);
					if(isZobristEnabled) {
						hashScore.stepsForKill = moves;
						boardStatus.put(zobristHash.getCurrentHash(), hashScore);
					}
					//--
					return moves;
				} else {
					ArrayList<Move> res = new ArrayList<Move>();
					res.add(point);
					if(isZobristEnabled) {
						hashScore.stepsForKill = res;
						boardStatus.put(zobristHash.getCurrentHash(), hashScore);
					}
					//--
					return res;
				}
			}	
		}
		
		return null;
	}
	
	ArrayList<Move> min_kill(int playerColor, int deep) {
		ArrayList<Move> result = new ArrayList<Move>();
		int status = Util.checkIfWin(board);
		if(status == playerColor) return null;
		if(status == reverseRole(playerColor)) return result;
		if(deep<0) return null;
		
		if(isZobristEnabled) {
			Score cachedScore = boardStatus.get(zobristHash.getCurrentHash());
			if(cachedScore != null) {
				if(cachedScore.deep >= deep) {
//					System.out.println("FOUND Cache KILL!");
					return cachedScore.stepsForKill;
				}
			}
		}
		
		ArrayList<Move> points = findMin(playerColor, MIN_SCORE);
		if(points.isEmpty()) return null;
		if(!points.isEmpty() && -1*points.get(0).score >= Score.FOUR) return null;
		
		for(int i=0;i<points.size();i++) {
			Move point = points.get(i);
//			board[point.getX()][point.getY()] = playerColor;
//			updateScore(point);
			putMove(point, playerColor);
			
			lastMinPoint = point;
			ArrayList<Move> moves = max_kill(reverseRole(playerColor), deep-1);
//			board[point.getX()][point.getY()] = Constants.EMPTY;
//			updateScore(point);
			removeMove(point);
			
			if(moves != null) {
				moves.add(0, point);
				result.addAll(moves);
				continue;
			} else {
				return null;
			}
			
			
		}
		ArrayList<Move> keepOneResult = new ArrayList<Move>();
		
		Move move = result.get((int) Math.floor(result.size() * Math.random()));
		keepOneResult.add(move);
		if(isZobristEnabled) {
			Score hashScore = new Score();
			hashScore.deep = deep;
			//--
			hashScore.stepsForKill = keepOneResult;
			boardStatus.put(zobristHash.getCurrentHash(), hashScore);
		}
		//--
		// ----
		return keepOneResult;
	}
	// Original Max min
	Move minimax(int deep) {
		int best = Integer.MIN_VALUE;
		ArrayList<Move> bestMoves = new ArrayList<Move>();
		ArrayList<Move> moves = generateNextSteps(myColor, deep);
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		for(int i=0; i<moves.size(); i++) {
			Move point = moves.get(i);
//			board[point.getX()][point.getY()] = myColor;
//			updateScore(point);
			putMove(point, myColor);
			int scoreMin = min(deep-1, alpha, beta);
//			board[point.getX()][point.getY()] = Constants.EMPTY;
//			updateScore(point);
			removeMove(point);
			
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
		
		for( Move m: bestMoves){
			System.out.println("Possible: "+m.getX()+", "+m.getY()+" Score="+m.score);
		}
//		System.out.println("Score = "+best);
		return bestMoves.get((int)(Math.floor(Math.random() * bestMoves.size())));
	}
	
	private int min(int deep, int alpha, int beta) {
		int score = evaluate(opponentColor);//(evaluateBoard(myColor) - evaluateBoard(opponentColor));
		
		if(deep<=0 || Util.checkIfWin(board) > 0) {
			return score;
		}
		
//		Score cachedScore = boardStatus.get(zobristHash.getCurrentHash());
//		if(cachedScore != null) {
//			if(cachedScore.deep >= deep) {
////				System.out.println("FOUND Cache!");
//				if(cachedScore.score != -1)
//					return cachedScore.score;
//			}
//		}
		
		int best = Integer.MAX_VALUE;
		ArrayList<Move> moves = generateNextSteps(opponentColor, deep);
		
		for(int i=0; i<moves.size(); i++) {
			Move point = moves.get(i);
//			board[point.getX()][point.getY()] = opponentColor;
//			updateScore(point);
			putMove(point, opponentColor);
			steps.add(point);
			int scoreMax = (int) (max(deep-1, alpha, beta) * 0.8);
			
			
//			board[point.getX()][point.getY()] = Constants.EMPTY;
//			updateScore(point);
			removeMove(point);
			steps.remove(point);
			if(scoreMax < best) {
				best = scoreMax;
			}
//			if(deep==1&&i==moves.size()-1) {
//				System.out.println("MINNNN");
//			}
//			if(deep==1 && scoreMax >= Score.FOUR *0.8) {
//				
//				System.out.println("score from last max:"+scoreMax*5/4+" Best:"+best);
//			
//			}
//			if(deep==1&&i==moves.size()-1) {
//				System.out.println("Best:"+best);
//			}
			if( best < alpha) {
				return best;
			}
			beta = Integer.min(beta, best);
		}
		if(isZobristEnabled) {
			Score hashScore = new Score();
			hashScore.deep = deep;
			hashScore.score = best;
			boardStatus.put(zobristHash.getCurrentHash(), hashScore);
		}
		
		return best;
	}
	
	private int max(int deep, int alpha, int beta) {
		int score = evaluate(myColor);//evaluateBoard(myColor) - evaluateBoard(opponentColor);
		
		if(deep<=0 || Util.checkIfWin(board) > 0) {
//			if(findMustKillEnabled) {
//				int best = score;
//				if( best < Score.FOUR && best > -1*Score.FOUR) {
//					Move move = checkMustKillStep(findKillDepth, true);
//					if(move != null) {
//	//					System.out.println("MaxKill:"+move.score);
//						return move.score;
//					}
//				}
//				if( best < Score.THREE*2 && best > -1*Score.THREE*2) {
//					Move move = checkMustKillStep(findKillDepth, false);
//					if(move != null) {
//						return move.score;
//					}
//				}
//				System.out.println("NoMaxKill:"+score);
//			}
			return score;
		}
		
		Score cachedScore = boardStatus.get(zobristHash.getCurrentHash());
		if(cachedScore != null) {
			if(cachedScore.deep >= deep) {
				
				if(cachedScore.getMaxScoreInSteps() != -1) {
					System.out.println("FOUND Cache! MAX");
					return cachedScore.getMaxScoreInSteps();
				}
			}
		}
		
		
		int best = Integer.MIN_VALUE;
		ArrayList<Move> moves = generateNextSteps(myColor, deep);
		
		for(int i=0; i<moves.size(); i++) {
			Move point = moves.get(i);
//			board[point.getX()][point.getY()] = myColor;
//			updateScore(point);
			putMove(point, myColor);
			steps.add(point);
			int scoreMin = (int) (min(deep-1, alpha, beta) * 0.8);
//			board[point.getX()][point.getY()] = Constants.EMPTY;
//			updateScore(point);
			removeMove(point);
			steps.remove(point);
			
			if(scoreMin > best) {
				best = scoreMin;
			}
			if( best > beta) {
				return best;
			}
			alpha = Integer.max(alpha, best);
			
			if(findMustKillEnabled) {
				if( (deep <= 2) && best < Score.FOUR && best > -1*Score.FOUR) {
					Move move = checkMustKillStep(findKillDepth, true);
					if(move != null) {
						return move.score;
					}
				}
	//			if( (deep <= 2) && best < Score.THREE*2 && best > -1*Score.THREE*2) {
	//				Move move = checkMustKillStep(findKillDepth, false);
	//				if(move != null) {
	//					return move.score;
	//				}
	//			}
			}
		}
		if(isZobristEnabled) {
			Score hashScore = new Score();
			hashScore.deep = deep;
			hashScore.score = best;
			boardStatus.put(zobristHash.getCurrentHash(), hashScore);
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
		// Evaluate of board, find max of both score matrix
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
		
//		selfScoreMax = fixScore(selfScoreMax);
//		opponentScoreMax = fixScore(opponentScoreMax);
//		int result = (playerColor == myColor ? 1 : -1) * (selfScoreMax - opponentScoreMax);
//		
//		return result;
		
		return selfScoreMax - opponentScoreMax;
		
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
	
	public void updateScore(Move lastMove) {
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
}
