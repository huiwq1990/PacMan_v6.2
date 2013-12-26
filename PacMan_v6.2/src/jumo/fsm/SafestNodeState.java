package jumo.fsm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import jumo.utility.Utility;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class SafestNodeState extends AState {
	public static int PARAMS = 1;
	
	public SafestNodeState() {
		params = new int[PARAMS];
	}
	
	@Override
	public boolean canHandle(Game game) {
		return true;
	}

	/*
	@Override
	public int getTarget(Game game) {
		return BestFirstSearchTarget(game, getParam(0));
	}
	*/
	
	@Override
	public int[] getPath(Game game) {
		return BestFirstSearchTarget(game, getParam(0));
	}
	
	@Override
	public boolean returnsTarget() {
		return false;
	}

	@Override
	public int getNumberOfParams() {
		return PARAMS;
	}
	
	private int[] BestFirstSearchTarget(Game game, int path_length) {
		Map<Integer, Integer> parents = new HashMap<Integer, Integer>();
		Set<Integer> open = new HashSet<Integer>();
		Set<Integer> closed = new HashSet<Integer>();
		
		open.add(game.getPacmanCurrentNodeIndex());
		
		while (open.size() > 0) {
			Integer best_index = FindBest(game, open);
			open.remove(best_index);
			
			closed.add(best_index);
			
			if (PathLength(parents, best_index) == path_length) {
				//return FirstMove(parents, best_index);
				return Path(parents, best_index);
			}
			
			int[] neighbors = FindNeighbors(game, best_index, parents);
			
			for (int n : neighbors) {
				if (closed.contains(n)) { continue; }
				
				open.add(n);
				parents.put(n, best_index);
			}
		}
		
		return new int[] { game.getPacmanCurrentNodeIndex() };
		//return game.getPacmanCurrentNodeIndex();
	}
	
	private static int[] FindNeighbors(Game game, int current, Map<Integer, Integer> parents) {
		Integer parent = parents.get(current);
		
		// Initial node
		if (parent == null) {
			return game.getNeighbouringNodes(current);
		}
		
		MOVE move = game.getMoveToMakeToReachDirectNeighbour(parent, current);
		
		return game.getNeighbouringNodes(current, move);
	}

	private static int NO_GHOSTS = -1000;
	private static int PILL_PRESENT = -10;
	private static int POWER_PILL_PRESENT = -50;
	
	private static Integer FindBest(Game game, Set<Integer> open_indices) {
		int minimum_distance = Integer.MAX_VALUE;
		Integer best = null;
		
		for (Integer node : open_indices) {
			int distance;
			
			try {
				int[] distances = Utility.DistancesFromIndices(game, node.intValue(), Utility.RegularGhostIndicesIncludingLair(game));
				
				distance = -Utility.Sum(distances);
			} catch (NoSuchElementException e) {
				distance = NO_GHOSTS;
			}
			
			if (game.getPillIndex(node.intValue()) != -1) {
				distance -= PILL_PRESENT;
			}
			
			if (game.getPowerPillIndex(node.intValue()) != -1) {
				distance -= POWER_PILL_PRESENT;
			}
			
			if (distance < minimum_distance) {
				minimum_distance = distance;
				best = node;
			}
		}
		
		return best;
	}
	
	private static int PathLength(Map<Integer, Integer> parents, int from) {
		int distance = 0;
		
		int current = from;
		while (parents.get(current) != null) {
			distance += 1;
			
			current = parents.get(current);
		}
		
		return distance;
	}
	
	private static int[] Path(Map<Integer, Integer> parents, int from) {
		List<Integer> path_list = new ArrayList<Integer>();

		int current = from;

		path_list.add(current);
		
		while (parents.get(current) != null) {
			int to = parents.get(current);
			
			current = to;
			path_list.add(current);
		}
		
		Collections.reverse(path_list);
		
		int[] path = new int[path_list.size()];
		for (int i = 0; i < path.length; i++) { path[i] = path_list.get(i); }
		
		return path;
	}
	
	private static int FirstMove(Map<Integer, Integer> parents, int from) {
		int current = from;
		
		while (parents.get(current) != null) {
			int to = parents.get(current);
			
			current = to;
		}
		
		return current;
	}
}
