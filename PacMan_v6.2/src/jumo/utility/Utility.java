package jumo.utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class Utility {
	public static boolean IsBetweenClosed(int value, int min, int max) {
		return min <= value && value <= max;
	}
	
	public static boolean IsBetweenOpen(int value, int min, int max) {
		return min < value && value < max;
	}
	
	public static boolean IsBetweenOpenClosed(int value, int min, int max) {
		return min < value && value <= max;
	}
	
	public static boolean IsBetweenClosedOpen(int value, int min, int max) {
		return min <= value && value < max;
	}
	
	public static int ShortestDistanceToIndices(Game game, int fromNodeIndex, int[] toIndices) {
		int toNodeIndex = game.getClosestNodeIndexFromNodeIndex(fromNodeIndex, toIndices, DM.PATH);
		
		return game.getShortestPathDistance(fromNodeIndex, toNodeIndex);
	}
	
	public static int ShortestDistanceFromIndices(Game game, int toNodeIndex, int[] fromIndices) {
		return Utility.Minimum(Utility.DistancesFromIndices(game, toNodeIndex, fromIndices));
	}
	
	public static int[] DistancesToIndices(Game game, int fromNodeIndex, int[] toIndices) {
		int[] distances = new int[toIndices.length];
		
		for (int i = 0; i < distances.length; i++) {
			distances[i] = game.getShortestPathDistance(fromNodeIndex, toIndices[i]);
		}
		
		return distances;
	}
	
	public static int[] DistancesFromIndices(Game game, int toNodeIndex, int[] fromIndices) {
		int[] distances = new int[fromIndices.length];
		
		for (int i = 0; i < distances.length; i++) {
			distances[i] = game.getShortestPathDistance(fromIndices[i], toNodeIndex);
		}
		
		return distances;
	}
	
	public static int[] AllGhostIndices(Game game) {
		int[] ghostIndices = new int[GHOST.values().length];
		
		int i = 0;
		for (GHOST g : GHOST.values()) {
			ghostIndices[i++] = game.getGhostCurrentNodeIndex(g);
		}
		
		return ghostIndices;
	}
	

	public static int[] RegularGhostIndices(Game game) {
		ArrayList<Integer> ghostIndicesList = new ArrayList<Integer>();
		
		for (GHOST g : GHOST.values()) {
			if (game.getGhostEdibleTime(g) == 0 && game.getGhostLairTime(g) == 0) {
				ghostIndicesList.add(game.getGhostCurrentNodeIndex(g));
			}
		}
		
		int[] ghostIndices = new int[ghostIndicesList.size()];
		for (int i = 0; i < ghostIndices.length; i++) {
			ghostIndices[i] = ghostIndicesList.get(i).intValue();
		}
		
		return ghostIndices;
	}
	
	public static int[] RegularGhostIndicesIncludingLair(Game game) {
		ArrayList<Integer> ghostIndicesList = new ArrayList<Integer>();
		
		for (GHOST g : GHOST.values()) {
			if (game.getGhostEdibleTime(g) == 0 && game.getGhostLairTime(g) == 0) {
				ghostIndicesList.add(game.getGhostCurrentNodeIndex(g));
			}
			else if (game.getGhostLairTime(g) > 0) {
				ghostIndicesList.add(game.getGhostInitialNodeIndex());
			}
		}
		
		int[] ghostIndices = new int[ghostIndicesList.size()];
		for (int i = 0; i < ghostIndices.length; i++) {
			ghostIndices[i] = ghostIndicesList.get(i).intValue();
		}
		
		return ghostIndices;
	}
	
	public static int GhostsInLair(Game game) {
		int ghosts = 0;
		
		for (GHOST g : GHOST.values()) {
			if (game.getGhostLairTime(g) > 0) { ghosts += 1; }
		}
		
		return ghosts;
	}
	
	public static int[] EdibleGhostIndices(Game game) {
		return EdibleGhostIndices(game, 1);
	}
	
	public static int[] EdibleGhostIndices(Game game, int minimumTime) {
		ArrayList<Integer> ghostIndicesList = new ArrayList<Integer>();
		
		for (GHOST g : GHOST.values()) {
			if (game.getGhostEdibleTime(g) >= minimumTime && game.getGhostLairTime(g) == 0) {
				ghostIndicesList.add(game.getGhostCurrentNodeIndex(g));
			}
		}
		
		int[] ghostIndices = new int[ghostIndicesList.size()];
		for (int i = 0; i < ghostIndices.length; i++) {
			ghostIndices[i] = ghostIndicesList.get(i).intValue();
		}
		
		return ghostIndices;
	}
	
	private static List<Integer> IntArrayToList(int[] values) {
		List<Integer> valueList = new ArrayList<Integer>(values.length);
		for (int i : values) { valueList.add(i); }
		
		return valueList;
	}
	
	public static int Minimum(int[] values) {
		return Collections.min(IntArrayToList(values));
	}
	
	public static int Maximum(int[] values) {
		return Collections.max(IntArrayToList(values));
	}
	
	public static int Sum(int[] values) {
		int sum = 0;
		for (int i : values) { sum += i; }
		
		return sum;
	}
}
