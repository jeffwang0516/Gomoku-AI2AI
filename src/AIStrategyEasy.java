import java.awt.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import javax.naming.InitialContext;
import javax.xml.crypto.dsig.keyinfo.RetrievalMethod;



public class AIStrategyEasy implements AIStrategy{
	
	private int myColor = Constants.COLOR_BLACK;
	private int opponentColor = Constants.COLOR_WHITE;
	private int[][] board;
	private ArrayList<Move> steps = new ArrayList<Move>();
	
	private int[][] scoreSelf;
	private int[][] scoreOpponent;
	
	int minimaxDepth = 4; // Should be even WHEN this is a max node
	int findKillDepth = 5;
	
	public AIStrategyEasy(int[][] board) {
		this.board = board;
		
		initScore();
	}
	
	public void initScore() {
		scoreSelf = new int[GameController.BOARD_SIZE_X][GameController.BOARD_SIZE_Y];
		scoreOpponent = new int[GameController.BOARD_SIZE_X][GameController.BOARD_SIZE_Y];
		
		for(int i=0;i<board.length;i++) {
		    for(int j=0;j<board[i].length;j++) {
		    		Move point = new Move(i, j);
				  if(board[i][j] == Constants.EMPTY) {
					  	
				    if(this.hasNeighbor(point, 2, 2)) { 
				      int cs = calScoreOfPoint(point, myColor);
				      int hs = calScoreOfPoint(point, opponentColor);
				      this.scoreSelf[i][j] = cs;
				      this.scoreOpponent[i][j] = hs;
				    }
				
				  } else if (board[i][j] == myColor) { 
				    this.scoreSelf[i][j] = calScoreOfPoint(point, myColor);
				    this.scoreOpponent[i][j] = 0;
				  } else if (board[i][j] == opponentColor) { 
				    this.scoreSelf[i][j] = calScoreOfPoint(point, opponentColor);
				    this.scoreOpponent[i][j] = 0;
				  }
		    }
		  }
	}
	
	@Override
	public Move computeAIMove(Move previousMove) {

		
		
		// PLAYER 1 AI Defines Here
//		board[8][7] = board[10][7] =board[11][7]  =1;
//		for(int i=0;i<GameController.BOARD_SIZE_X;i++) {
//			for(int j=0;j<GameController.BOARD_SIZE_Y;j++) {
//				System.out.print(board[i][j]+", ");;
//			}
//			System.out.println();
//		}
//		System.out.println("EV1: "+evaluateBoard(myColor));
//		System.out.println("------");
//		ArrayList<Move> arrayList = new ArrayList<Move>();
//		Move p1 = new Move(0, 0);p1.score=5;
//		Move p2 = new Move(0, 1);p2.score=15;
//		arrayList.add(p1);arrayList.add(p2);
//		for(int i=0;i<arrayList.size();i++) {
//			Move eMove = arrayList.get(i);
//			System.out.println(eMove.getX()+", "+eMove.getY()+" "+eMove.score);
//		}
//		
//		Collections.sort(arrayList, new Comparator<Move>() {
//
//			@Override
//			public int compare(Move o1, Move o2) {
//				// TODO Auto-generated method stub
//				return o2.score-o1.score;
//			}
//		});
//		System.out.println("------");
//		for(int i=0;i<arrayList.size();i++) {
//			Move eMove = arrayList.get(i);
//			System.out.println(eMove.getX()+", "+eMove.getY()+" "+eMove.score);
//		}
//		
//		System.out.println("------");
//		
//		System.out.println("------");
//		for(int i=0;i<arrayList.size();i++) {
//			System.out.println("Step: "+arrayList.get(i).getX()+" "+arrayList.get(i).getY());
//		}
//		System.out.println("EV1: "+evaluateBoard(myColor) +", EV2: "+ evaluateBoard(opponentColor));
//		int[] test = {0,0,1,0,2,0};
//		System.out.println("TEST: "+evaluateRow(test, Constants.COLOR_WHITE)+"   "+scoreOfPattern(1, 0, 0));
//		return new Move(7, 7);
		
		// Update score after opponent's move
		updateScore(previousMove);
		
		Move point = minimax(minimaxDepth);
		
		// DEBUG message
		int myScore = calScoreOfPoint(point, Constants.COLOR_BLACK);
		int  opponentScore= calScoreOfPoint(point, Constants.COLOR_WHITE);
		System.out.println("BLACK: "+myScore+" WHITE: "+opponentScore);
		System.out.println("EV1: "+evaluate(myColor) +", EV2: "+ evaluate(opponentColor));
//		System.out.println("---------board----------");
//		for(int i=0;i<GameController.BOARD_SIZE_X;i++) {
//			for(int j=0;j<GameController.BOARD_SIZE_Y;j++) {
//				System.out.format("%d, ",board[j][i]);;
//			}
//			System.out.println();
//		}
//		System.out.println("---------self score----------");
//		for(int i=0;i<GameController.BOARD_SIZE_X;i++) {
//			for(int j=0;j<GameController.BOARD_SIZE_Y;j++) {
//				System.out.format("%03d, ",scoreSelf[j][i]);;
//			}
//			System.out.println();
//		}
//		System.out.println("---------oppo score----------");
//		for(int i=0;i<GameController.BOARD_SIZE_X;i++) {
//			for(int j=0;j<GameController.BOARD_SIZE_Y;j++) {
//				System.out.format("%03d, ",scoreOpponent[j][i]);;
//			}
//			System.out.println();
//		}
//		System.out.println("-------------------");
		// END DEBUG MESSAGE
		
		// Update score after own move
		updateScore(point);
		
		// Step record
		steps.add(point);
		
		return point; // return next step by new Move(x,y)
	}
	
	
	
	int MAX_SCORE = Score.THREE;
	int MIN_SCORE = Score.FOUR;
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
		ArrayList<Move> points = findMax(playerColor, MAX_SCORE);
		if(!points.isEmpty() && points.get(0).score >= Score.FOUR) {
			result.add(points.get(0));
			return result;
		}
		if(points.isEmpty()) return null;
		
		for(int i=0;i<points.size();i++) {
			Move point = points.get(i);
			board[point.getX()][point.getY()] = playerColor;
			updateScore(point);
			
			if(!(point.score<=-Score.FIVE)) lastMaxPoint = point;
			ArrayList<Move> moves = min_kill(reverseRole(playerColor), deep-1);
			board[point.getX()][point.getY()] = Constants.EMPTY;
			updateScore(point);
			
			if(moves != null) {
				if(!moves.isEmpty()) {
					moves.add(0, point);
					return moves;
				} else {
					ArrayList<Move> res = new ArrayList<Move>();
					res.add(point);
					return res;
				}
			}	
		}
		
		return null;
	}
	
	ArrayList<Move> min_kill(int playerColor, int deep) {
		ArrayList<Move> result = new ArrayList<Move>();
		int status = checkIfWin();
		if(status == playerColor) return null;
		if(status == reverseRole(playerColor)) return result;
		if(deep<0) return null;
		
		ArrayList<Move> points = findMin(playerColor, MIN_SCORE);
		if(points.isEmpty()) return null;
		if(!points.isEmpty() && -1*points.get(0).score >= Score.FOUR) return null;
		
		for(int i=0;i<points.size();i++) {
			Move point = points.get(i);
			board[point.getX()][point.getY()] = playerColor;
			updateScore(point);
			
			lastMinPoint = point;
			ArrayList<Move> moves = max_kill(reverseRole(playerColor), deep-1);
			board[point.getX()][point.getY()] = Constants.EMPTY;
			updateScore(point);
			
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
		
		for( Move m: bestMoves){
			System.out.println("Possible: "+m.getX()+", "+m.getY()+" Score="+m.score);
		}
//		System.out.println("Score = "+best);
		return bestMoves.get((int)(Math.floor(Math.random() * bestMoves.size())));
	}
	
	private int min(int deep, int alpha, int beta) {
		int score = evaluate(opponentColor);//(evaluateBoard(myColor) - evaluateBoard(opponentColor));
		
		if(deep<=0 || checkIfWin() > 0) {
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
		return best;
	}
	
	private int max(int deep, int alpha, int beta) {
		int score = evaluate(myColor);//evaluateBoard(myColor) - evaluateBoard(opponentColor);
		
		if(deep<=0 || checkIfWin() > 0) {
			int best = score;
			if( best < Score.FOUR && best > -1*Score.FOUR) {
				Move move = checkMustKillStep(findKillDepth, true);
				if(move != null) {
//					System.out.println("MaxKill:"+move.score);
					return move.score;
				}
			}
			if( best < Score.THREE*2 && best > -1*Score.THREE*2) {
				Move move = checkMustKillStep(findKillDepth, false);
				if(move != null) {
					return move.score;
				}
			}
//			System.out.println("NoMaxKill:"+score);
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
			
//			if( (deep <= 2) && best < Score.FOUR && best > -1*Score.FOUR) {
//				Move move = checkMustKillStep(7, true);
//				if(move != null) {
//					return move.score;
//				}
//			}
//			if( (deep <= 2) && best < Score.THREE*2 && best > -1*Score.THREE*2) {
//				Move move = checkMustKillStep(7, false);
//				if(move != null) {
//					return move.score;
//				}
//			}
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
					if(hasNeighbor(point, num, num) ) {
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
		
//		selfScoreMax = fixScore(selfScoreMax);
//		opponentScoreMax = fixScore(opponentScoreMax);
//		int result = (playerColor == myColor ? 1 : -1) * (selfScoreMax - opponentScoreMax);
//		
//		return result;
		
		return selfScoreMax - opponentScoreMax;
		
	}
	
	private int fixScore(int score) {
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
	private void update(int x, int y) {
		int color = board[x][y];
		Move point = new Move(x, y);
		if( color != opponentColor) {
			int cs = calScoreOfPoint(point, myColor);
			
			this.scoreSelf[x][y] = cs;
			
		} else {
			this.scoreSelf[x][y] = 0;
		}
		
		if(color != myColor) {
			int hs = calScoreOfPoint(point, opponentColor);
			this.scoreOpponent[x][y] = hs;
		} else {
			this.scoreOpponent[x][y] = 0;
		}
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
	private int calScoreOfPoint(Move point, int playerColor) {
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
	
	
	private int checkIfWin() {
		for(int i=0;i<GameController.BOARD_SIZE_X;i++) {
			for(int j=0;j<GameController.BOARD_SIZE_Y;j++) {
				if(board[i][j] != Constants.EMPTY) {
					int status = checkIfFive(i, j) ;
					if(status != 0) {
						return status ;
					}
				}
			}
		}
		return 0;
	}
	private int checkIfFive(int x, int y) {
		
		
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
//		System.out.println("TESTEVROW");
//		for(int i=0;i<row.length;i++) {
//			System.out.print(row[i]);
//		}
//		System.out.println("TESTEVROW END");
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
		
		return result;
	}
	
	
	
	
	
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

