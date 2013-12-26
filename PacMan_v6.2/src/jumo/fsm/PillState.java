package jumo.fsm;

import java.util.NoSuchElementException;

import jumo.utility.Utility;
import pacman.game.Game;
import pacman.game.Constants.DM;

public class PillState extends AState {
	public static int PARAMS = 2;
	
	public PillState() {
		params = new int[PARAMS];
	}
	
	@Override
	public boolean canHandle(Game game) {
		if (game.getActivePillsIndices().length == 0) {
			return false;
		}
		
		int closest_ghost_distance;
		
		try {
			closest_ghost_distance = Utility.ShortestDistanceFromIndices(game, game.getPacmanCurrentNodeIndex(), Utility.RegularGhostIndices(game));
		} catch (NoSuchElementException e) {
			// All ghosts are edible
			closest_ghost_distance = Integer.MAX_VALUE;
		}
		
		// All ghosts in lair
		if (closest_ghost_distance < 0) {
			closest_ghost_distance = Integer.MAX_VALUE;
		}
		
		int nearest_pill_distance = Utility.ShortestDistanceToIndices(game, game.getPacmanCurrentNodeIndex(), game.getActivePillsIndices());
		
		return closest_ghost_distance > getParam(0) && nearest_pill_distance < getParam(1);
	}

	@Override
	public int getTarget(Game game) {
		return game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), game.getActivePillsIndices(), DM.PATH);
	}

	@Override
	public int getNumberOfParams() {
		return PARAMS;
	}

}
