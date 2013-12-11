package pacman;

import pacman.controllers.examples.StarterGhosts;
import pacman.controllers.examples.StarterPacMan;

public class TestStarterPacman {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Executor exec=new Executor();

		/*
		//run multiple games in batch mode - good for testing.
		int numTrials=10;
		exec.runExperiment(new RandomPacMan(),new RandomGhosts(),numTrials);
		 */
		
		/*
		//run a game in synchronous mode: game waits until controllers respond.
		int delay=5;
		boolean visual=true;
		exec.runGame(new RandomPacMan(),new RandomGhosts(),visual,delay);
  		 */
		
		///*
		//run the game in asynchronous mode.
		boolean visual=true;
//		exec.runGameTimed(new NearestPillPacMan(),new AggressiveGhosts(),visual);
		exec.runGameTimed(new StarterPacMan(),new StarterGhosts(),visual);
	}

}
