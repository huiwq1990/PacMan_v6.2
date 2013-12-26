package pacman.cwie;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Random;
import java.util.Set;

import pacman.controllers.Controller;
import pacman.game.Constants;
import pacman.game.Game;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.internal.Maze;
import pacman.game.internal.Node;


public class TheGhostBusters extends Controller<MOVE> 
{
    
	private static int PICK_GHOSTBUSTER = 1;
	
	private boolean initialized = false;
    Random rand;
	
	public MOVE getMove(Game game,long timeDue)
	{		
		switch (PICK_GHOSTBUSTER) {
		case 0:
			return new PeterVenkman().MakeDecision(game.copy(), timeDue);
		case 1:
			return new RaymondStantz().MakeDecision(game.copy(), timeDue);
		default:
			return new PeterVenkman().MakeDecision(game.copy(), timeDue);
		}

	}
}