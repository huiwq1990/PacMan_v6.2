package jumo.fsm;

import java.util.NoSuchElementException;

import jumo.utility.Utility;
import pacman.game.Game;
import pacman.game.Constants.DM;

public class HuntGhostState extends AState {

	public static int PARAMS = 2;

	public HuntGhostState() {
		params = new int[PARAMS];
	}
	
	@Override
	public boolean canHandle(Game game) {
		int edible_ghosts = Utility.EdibleGhostIndices(game, getParam(0)).length;
		
		int closest_ghost_distance;
		
		try {
			closest_ghost_distance = Utility.ShortestDistanceFromIndices(game, game.getPacmanCurrentNodeIndex(), Utility.EdibleGhostIndices(game, getParam(0)));
		} catch (NoSuchElementException e) {
			// No ghosts are edible :(
			closest_ghost_distance = Integer.MAX_VALUE;
		}
		
		return closest_ghost_distance < getParam(1);
	}

	@Override
	public int getTarget(Game game) {
		return game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), Utility.EdibleGhostIndices(game, getParam(0)), DM.PATH);
	}

	@Override
	public int getNumberOfParams() {
		return PARAMS;
	}

}
