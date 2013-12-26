package jumo.mcts;

import java.util.LinkedList;

import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Node {
	public int visits;
	public double score;
	public Node parent;
	public LinkedList<Node> children;
	public Game state;
	public MOVE action_from_parent;
	
	public Node(Game state, Node parent, MOVE action_from_parent) {
		this.children = new LinkedList<Node>();
		this.visits = 0;
		this.score = 0;
		
		this.state = state.copy();
		this.parent = parent;
		this.action_from_parent = action_from_parent;
	}
	
	public void visit() {
		this.visits += 1;
	}
	
	public void addScore(double score) {
		this.score += score;
	}
	
	public double meanScore() {
		return score / visits;
	}
	
	public float averageChildScore() {
		if (children.size() == 0) {
			return 0;
		}
		
		int sum = 0;
		for (Node n : children) {
			sum += n.score;
		}
		
		return sum / (float)children.size();
	}
}
