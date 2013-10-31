package hg;

import pacman.Executor;
import pacman.controllers.Controller;
import pacman.controllers.examples.RandomGhosts;
import pacman.controllers.examples.RandomPacMan;
import pacman.controllers.examples.StarterGhosts;
import pacman.controllers.examples.StarterPacMan;
import pacman.game.Constants.MOVE;
import wox.serial.Easy;

public class ExecuteEvolvedAgent {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Executor exec = new Executor();
		Controller<MOVE> evolveAgent = new SimpleMLPAgent();
		evolveAgent = (Controller<MOVE>) Easy.load("evolved.xml");

		boolean visual=true;
		exec.runGameTimed(evolveAgent,new StarterGhosts(),visual);

	}

}
