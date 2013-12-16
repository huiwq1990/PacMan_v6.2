package pacman.run;

import pacman.Executor;
import pacman.controllers.HumanController;
import pacman.controllers.KeyBoardInput;
import pacman.controllers.examples.StarterGhosts;
import pacman.controllers.examples.StarterPacMan;

public class PacmanLocationInfo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Executor exec=new Executor();

	
		//run the game in asynchronous mode.
		boolean visual=true;
		exec.runGameTimed(new HumanController(new KeyBoardInput()),new StarterGhosts(),visual);	
	
	}

}
