package jumo.fsm;

import java.util.NoSuchElementException;

import jumo.utility.Utility;
import pacman.game.Game;
import pacman.game.Constants.DM;

public class GoToPowerPillState extends AState {

	public static int PARAMS = 3;
	
	public GoToPowerPillState() {
		params = new int[PARAMS];
	}
	
	@Override
	public boolean canHandle(Game game) {
		if (game.getActivePowerPillsIndices().length == 0) { return false; }
		
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
		
		int nearest_pill_distance = Utility.ShortestDistanceToIndices(game, game.getPacmanCurrentNodeIndex(), game.getActivePowerPillsIndices());
		
		return closest_ghost_distance > getParam(0) && closest_ghost_distance < getParam(1) && nearest_pill_distance < getParam(2) && Utility.RegularGhostIndices(game).length > 0;
	}

	@Override
	public int getTarget(Game game) {
		int current = game.getPacmanCurrentNodeIndex();
		
		int closest_powerpill = game.getClosestNodeIndexFromNodeIndex(current, game.getActivePowerPillsIndices(), DM.PATH);
		return closest_powerpill;
		/*
		int[] path_to_powerpill = game.getShortestPath(current, closest_powerpill);
		
		
		int target = current;
		if (path_to_powerpill.length > 1) { // We are not at the direct neighbor
			target = path_to_powerpill[path_to_powerpill.length - 2];
		}
		
		return target;
		*/
	}

	@Override
	public int getNumberOfParams() {
		return PARAMS;
	}

}
