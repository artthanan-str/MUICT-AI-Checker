// Member
// 1. Thanan 	Sapthamrong			5888176	Sec1
// 2. Kritsada 	Sunthornwutthikrai	5888182	Sec1

import java.util.*;

public class AlphaBetaPlayer extends CheckerPlayer implements CheckerBoardConstants {
	
	int alpha, beta, maxDepth = 2;
	
	public AlphaBetaPlayer(String name){
		super(name);
	}

	public void calculateMove(int[][] bs, int whosTurn){
		
		chosenMove = null;
		alpha = -99; beta = 99;
		int maxScore = -9999;
		
		for (Move move : (Vector<Move>)Utils.getAllPossibleMoves(bs, whosTurn)) {
			
			int[][] copyBoard = Utils.copyBoardState(bs);
			Stack<int[][]> movesMade = Utils.executeMove(whosTurn, move, copyBoard);
			int score = getMin(copyBoard, 0, -9999, 9999, whosTurn);
			
			Utils.setBoardState(movesMade, bs);
			if (score > maxScore) {
				maxScore = score;
				chosenMove = move;
			}
		}
		Utils.executeMove(whosTurn, chosenMove, bs);
	}
	
	//Get opponent turn
	public int getOpponent(int whosTurn) {
		if (whosTurn == RED_PLAYER) return BLACK_PLAYER;
		return RED_PLAYER;
	}
	
	//Find Maximum value (recursion)
	public int getMax(int[][] boardState, int depth, int alpha, int beta, int whosTurn) {
		
		if (depth == maxDepth || Utils.checkForWinner(boardState) != NEITHER_PLAYER) {
			return actualScore(boardState, whosTurn);
		}
		
		for (Move move : (Vector<Move>)Utils.getAllPossibleMoves(boardState, whosTurn)) {
			
			int[][] copyBoard = Utils.copyBoardState(boardState);
			Stack<int[][]> movesMade = Utils.executeMove(whosTurn, move, copyBoard);
			int bestInside = getMin(copyBoard, depth + 1, alpha, beta, getOpponent(whosTurn));
			
			if(bestInside > alpha) {
				alpha = bestInside;
			}
			
			Utils.setBoardState(movesMade, boardState);
			if(alpha >= beta) break;
		}
		
		return alpha;
	}
	
	//Find Minimum value (recursion)
	public int getMin(int[][] boardState, int depth, int alpha, int beta, int whosTurn) {
		
		if (depth == maxDepth || Utils.checkForWinner(boardState) != NEITHER_PLAYER) {
			return actualScore(boardState, whosTurn);
		}
		
		for (Move move : (Vector<Move>)Utils.getAllPossibleMoves(boardState, whosTurn)) {
			
			int[][] copyBoard = Utils.copyBoardState(boardState);
			Stack<int[][]> movesMade = Utils.executeMove(whosTurn, move, boardState);
			int bestInside = getMax(copyBoard, depth + 1, alpha, beta, getOpponent(whosTurn));
			
			if(bestInside < beta) {
				beta = bestInside;
			}
			
			Utils.setBoardState(movesMade, boardState);
			if (alpha >= beta) break;
		}
		
		return beta;
	}
	
	//Find the actual score that suits for player
	public int actualScore(int[][] boardState, int whosTurn) {
	
		if(playerName.equals("RED PLAYER")) {
			return Utils.scoreCheckerBoard(boardState, whosTurn);
		}
		else {
			return -Utils.scoreCheckerBoard(boardState, whosTurn);
		}
	}
	
}