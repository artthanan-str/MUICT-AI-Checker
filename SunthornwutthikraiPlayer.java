import java.util.Stack;
import java.util.Vector;

public class SunthornwutthikraiPlayer extends CheckerPlayer implements CheckerBoardConstants {

	int alpha, beta, maxDepth = 10;
	boolean isRed = false;
	Move prevMove, prevPrevMove;
	
	public SunthornwutthikraiPlayer(String name) {
		super(name);
	}

	@Override
	public void calculateMove(int[][] boardState, int whosTurn) {
		if(whosTurn == RED_PLAYER) isRed = true;
		
		chosenMove = null;
		alpha = -99; beta = 99;
		double maxScore = -9999, maxWeight = -9999;
		
		for (Move move : (Vector<Move>)Utils.getAllPossibleMoves(boardState, whosTurn)) {
			if (move == prevPrevMove) continue;
			double rowScore = 4.5 - Math.abs(move.getLocationAtIndex(0).row - 4.5);
			double colScore = 4.5 - Math.abs(move.getLocationAtIndex(0).col - 4.5);
			int[][] copyBoard = Utils.copyBoardState(boardState);
			Stack<int[][]> movesMade = Utils.executeMove(whosTurn, move, copyBoard);
			double score = getMin(copyBoard, 0, -9999, 9999, whosTurn);
			double weight = rowScore / 8.0 + colScore / 8.0;
			
			Utils.setBoardState(movesMade, boardState);
			if (score == maxScore) {
				if (weight > maxWeight) {
					maxScore = score;
					chosenMove = move;
				}
				//System.out.println(move.getLocationAtIndex(move.locationCount() - 1) + " Score: " + (score + weight));
			} else
			if (score > maxScore) {
				maxScore = score;
				chosenMove = move;
			}
		}
		prevPrevMove = prevMove.copy();
		prevMove = chosenMove.copy();	
		Utils.executeMove(whosTurn, chosenMove, boardState);
	}
	
	public int getOpponent(int whosTurn) {
		if (whosTurn == RED_PLAYER) return BLACK_PLAYER;
		return RED_PLAYER;
	}
	
	public int getMax(int[][] boardState, int depth, int alpha, int beta, int whosTurn) {
		
		if (depth == maxDepth || Utils.checkForWinner(boardState) != NEITHER_PLAYER) {
			return actualScore(boardState, whosTurn);
		}
		
		for (Move move : (Vector<Move>)Utils.getAllPossibleMoves(boardState, whosTurn)) {
			Location destLocation = move.getLocationAtIndex(move.locationCount() - 1);
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
	
	public int getMin(int[][] boardState, int depth, int alpha, int beta, int whosTurn) {
		
		if (depth == maxDepth || Utils.checkForWinner(boardState) != NEITHER_PLAYER) {
			return actualScore(boardState, whosTurn);
		}
		
		
		for (Move move : (Vector<Move>)Utils.getAllPossibleMoves(boardState, whosTurn)) {
			Location destLocation = move.getLocationAtIndex(move.locationCount() - 1);
			int[][] copyBoard = Utils.copyBoardState(boardState);
			Stack<int[][]> movesMade = Utils.executeMove(whosTurn, move, boardState);
			int bestInside = getMax(copyBoard, depth + 1, alpha, beta, getOpponent(whosTurn));
			
			if(bestInside < beta) {
				beta = bestInside;
			}
			
			Utils.setBoardState(movesMade, boardState);
			if (beta <= alpha) break;
		}
		return beta;
	}
	
	public int actualScore(int[][] boardState, int whosTurn) {
	
		if(playerName == "RED PLAYER") {
			return Utils.scoreCheckerBoard(boardState, whosTurn);
		}
		else {
			return -Utils.scoreCheckerBoard(boardState, whosTurn);
		}
	}
}
