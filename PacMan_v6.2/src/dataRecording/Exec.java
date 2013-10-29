package dataRecording;

import pacman.Executor;
import pacman.controllers.KeyBoardInput;
import pacman.controllers.examples.RandomGhosts;
import pacman.controllers.examples.RandomPacMan;

public class Exec {

	public static void main(String[] args) {
		Executor exec=new Executor();
		boolean visual=true;
		String fileName="replay.txt";
		exec.runGameTimedRecorded(new DataCollectorController(new KeyBoardInput()),new RandomGhosts(),visual,fileName);
	}

}
