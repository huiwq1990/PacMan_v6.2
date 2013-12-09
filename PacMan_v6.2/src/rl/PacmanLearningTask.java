package rl;

import static pacman.game.Constants.DELAY;

import java.util.EnumMap;
import java.util.Random;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Game;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class PacmanLearningTask {
	
	double avgScore=0;
	
	Random rnd=new Random(0);
	Game game;
	
	Controller<MOVE> pacManController;
	Controller<EnumMap<GHOST,MOVE>> ghostController;
	
	
	public PacmanLearningTask(Controller<MOVE> pacManController){
		this.pacManController = pacManController;
	}
	public void reset(){
		game=new Game(rnd.nextLong());
		ghostController = new StarterGhosts();
	}
	
	
	public double runSingleEpisode(final int repetitionsOfSingleEpisode){
		reset();
		for(int i=0;i<repetitionsOfSingleEpisode;i++)
		{
			while(!game.gameOver())
			{
		        game.advanceGame(pacManController.getMove(game.copy(),System.currentTimeMillis()+DELAY),
		        		ghostController.getMove(game.copy(),System.currentTimeMillis()+DELAY));
			}
			
			avgScore+=game.getScore();
			System.out.println(i+"\t"+game.getScore());
		}
		return avgScore/repetitionsOfSingleEpisode;
	}
	

}
