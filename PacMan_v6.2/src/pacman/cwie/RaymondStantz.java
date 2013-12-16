package pacman.cwie;

import java.util.Random;

import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * Raymond Stantz
 * 
 * MCTS controller, using the structure of the UCT lab.
 * 
 * **/

public class RaymondStantz {
	
	//Constants
	private static int MAX_ITERATIONS = 5;
	
	public RaymondStantz(){
		
	}
	
	public MOVE MakeDecision(Game game, long timeDue) {
		
		//The main loop
		int iteration = 0;
		while(KeepIterating(iteration))
		{
			iteration ++;
			TreePolicy();
			BackPropergate(DefaultPolicy());
		}
		return game.getPossibleMoves(game.getPacmanCurrentNodeIndex())[new Random(System.currentTimeMillis()).nextInt(game.getPossibleMoves(game.getPacmanCurrentNodeIndex()).length)];
	}

	private void BackPropergate(float defaultPolicy) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Make moves through the maze, using the default policy
	 * 		Default Policy -> 
	 * **/
	private float DefaultPolicy() {
		// TODO Auto-generated method stub
		
		return 0.f;
	}

	private void TreePolicy() {
		// TODO Auto-generated method stub
		
	}

	private boolean KeepIterating(int iteration) {
		// TODO Auto-generated method stub
		return false;
	}

	
	//Helper functions
	private MOVE RandomGhostMove(Game game, GHOST ghost)
	{
		Random rand = new Random(System.currentTimeMillis());
		MOVE[] possibleGhostMoves = game.getPossibleMoves(game.getGhostCurrentNodeIndex(ghost));
		
		return possibleGhostMoves[rand.nextInt(possibleGhostMoves.length)];
	}
}
