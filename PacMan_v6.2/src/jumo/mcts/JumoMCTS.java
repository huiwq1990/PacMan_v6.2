package jumo.mcts;

import java.util.EnumMap;
import java.util.Random;

import jumo.utility.Utility;
import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class JumoMCTS  extends Controller<MOVE> {

	public static int SLACK = 10;
	public static double DEFAULT_C_VALUE = 0.5;
	private double C_VALUE = 0.9;
	public int GHOST_MINIMUM_DISTANCE = 20;
	
	public static Controller<EnumMap<GHOST,MOVE>> DEFAULT_GHOSTSCONTROLLER = new StarterGhosts();
	private Controller<EnumMap<GHOST,MOVE>> ghostsController = new StarterGhosts();
	
	private static Random rnd = new Random();
	
	public JumoMCTS() {
		this(DEFAULT_C_VALUE, DEFAULT_GHOSTSCONTROLLER);
	}
	
	public JumoMCTS(double C) {
		this(C, DEFAULT_GHOSTSCONTROLLER);
	}
	
	public JumoMCTS(Controller<EnumMap<GHOST,MOVE>> ghostsController) {
		this(DEFAULT_C_VALUE, ghostsController);
	}
	
	public JumoMCTS(double C, Controller<EnumMap<GHOST,MOVE>> ghostsController) {
		this.C_VALUE = C;
		this.ghostsController = ghostsController;
	}
	
	@Override
	public MOVE getMove(Game game, long timeDue) {
		return MCTS(game.copy(), 1000, timeDue);
	}

	private MOVE MCTS(Game rootstate, int max_iterations, long timeDue) {
		Node rootnode = new Node(rootstate, null, null);
		
		while(System.currentTimeMillis() < timeDue - SLACK) {
			Node v1 = TreePolicy(rootnode);
			double score = DefaultPolicy(v1);
			Backup(v1, score);
		}
		
		Node best_child = BestChild(rootnode, C_VALUE);
		
		MOVE action = MOVE.NEUTRAL;
		if (best_child != null) {
			action = best_child.action_from_parent;
		}
		
		return action;
	}
	
	private Node TreePolicy(Node node) {
		Node v = node;
		
		while (!v.state.gameOver()) {
			if (!IsFullyExpanded(v)) {
				return Expand(v);
			}
			else {
				v = BestChild(v, C_VALUE);
			}
		}
		
		return v;
	}
	
	private boolean IsFullyExpanded(Node node) {
		int number_of_possibilities = node.state.getPossibleMoves(node.state.getPacmanCurrentNodeIndex(), node.state.getPacmanLastMoveMade()).length;
		
		return number_of_possibilities == node.children.size();
	}
	
	private boolean ChildrenContainsMove(Node node, MOVE action) {
		for (Node child : node.children) {
			if (action == child.action_from_parent) {
				return true;
			}
		}
		
		return false;
	}
	
	private Node Expand(Node node) {
		MOVE[] possible_moves = node.state.getPossibleMoves(node.state.getPacmanCurrentNodeIndex(), node.state.getPacmanLastMoveMade());
		
		int move_index = rnd.nextInt(possible_moves.length);
		MOVE action = possible_moves[move_index];
		
		int tried = 0;
		while (ChildrenContainsMove(node, action) && tried < possible_moves.length) {
			move_index = (move_index + 1) % possible_moves.length;
			action = possible_moves[move_index];
			
			tried += 1;
		}
		
		if (tried == possible_moves.length && ChildrenContainsMove(node, action)) {
			throw new RuntimeException("Node was already fully expanded!");
		}
		
		Game child_state = node.state.copy();
		child_state.advanceGame(action, ghostsController.getMove(child_state, System.currentTimeMillis() + 2));
		
		Node child = new Node(child_state, node, action);
		
		node.children.add(child);
		
		return child;
	}
	
	private double UCTScore(Node n, double C) {
		if (n.state.gameOver()) {
			return 0;
		}
		
		return n.meanScore() + C * Math.sqrt( 2 * Math.log10(n.parent.visits) / n.visits );
	}
	
	private Node BestChild(Node parent, double C) {
		Node best_child = null;
		double best_score = Double.NEGATIVE_INFINITY;
		
		for (Node child : parent.children) {
			double uct_score = UCTScore(child, C);
			
			if (uct_score > best_score) {
				best_child = child;
				best_score = uct_score;
			}
		}
		
		return best_child;
	}
	
	private double DefaultPolicy(Node node) {
		Game game = node.state.copy();
		
		int old_number_of_pills = game.getNumberOfActivePills();
		
		while (!game.gameOver()) {
			int current = game.getPacmanCurrentNodeIndex();
			
			EnumMap<GHOST, MOVE> ghostMoves = ghostsController.getMove(game, System.currentTimeMillis() + 2);
			MOVE pacManMove = MOVE.NEUTRAL;
			
			MOVE[] moves;
			int closest_ghost = game.getClosestNodeIndexFromNodeIndex(current, Utility.RegularGhostIndices(game), DM.PATH);
			
			// Only let PacMan turn around if there is a nearby ghost or a powerpill was just eaten
			if (game.wasPowerPillEaten() || (closest_ghost > 0 && game.getShortestPathDistance(current, closest_ghost) < GHOST_MINIMUM_DISTANCE)) {
				moves = game.getPossibleMoves(current);
			}
			else {
				moves = game.getPossibleMoves(current, game.getPacmanLastMoveMade());
			}
				
			pacManMove = moves[rnd.nextInt(moves.length)];
			
			game.advanceGame(pacManMove, ghostMoves);
		}
		
		return (old_number_of_pills - game.getNumberOfActivePills()) / (double)game.getNumberOfActivePills();
	}
	
	private void Backup(Node v, double score) {
		while (v != null) {
			v.visit();
			v.addScore(score);
			
			v = v.parent;
		}
	}
}
