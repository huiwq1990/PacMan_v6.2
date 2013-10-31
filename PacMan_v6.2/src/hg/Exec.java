package hg;

import pacman.Executor;
import pacman.controllers.examples.RandomGhosts;
import pacman.controllers.examples.RandomPacMan;
import pacman.controllers.examples.StarterGhosts;

public class Exec {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Executor exec=new Executor();

		
		//run multiple games in batch mode - good for testing.
		int numTrials=10;
		exec.runExperiment(new RandomPacMan(),new StarterGhosts(),numTrials);
	}

}
