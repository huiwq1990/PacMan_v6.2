package hg;

import pacman.controllers.Controller;
import pacman.game.Game;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;



/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Apr 28, 2009
 * Time: 2:09:42 PM
 */
public class  SimpleMLPAgent  extends Controller<MOVE>  implements  Evolvable {
	
	
	public int mazeIndex;
	public int currentLevel;
	public int pacmanPosition;
	public int pacmanLivesLeft;
	public int currentScore;
	public int totalGameTime;
	public int currentLevelTime;
	public int numOfPillsLeft;
	public int numOfPowerPillsLeft;
	
	public boolean isBlinkyEdible = false;
	public boolean isInkyEdible = false;
	public boolean isPinkyEdible = false;
	public boolean isSueEdible = false;
	
	public int blinkyDist = -1;
	public int inkyDist = -1;
	public int pinkyDist = -1;
	public int sueDist = -1;
	
	private MOVE myMove=MOVE.NEUTRAL;
	
	public MOVE getMove(Game game, long timeDue) 
	{
		
		int current=game.getPacmanCurrentNodeIndex();
		
		this.mazeIndex = game.getMazeIndex();
		this.currentLevel = game.getCurrentLevel();
		this.pacmanPosition = game.getPacmanCurrentNodeIndex();
		this.pacmanLivesLeft = game.getPacmanNumberOfLivesRemaining();
		this.currentScore = game.getScore();
		this.totalGameTime = game.getTotalTime();
		this.currentLevelTime = game.getCurrentLevelTime();
		this.numOfPillsLeft = game.getNumberOfActivePills();
		this.numOfPowerPillsLeft = game.getNumberOfActivePowerPills();
		
		
		if (game.getGhostLairTime(GHOST.BLINKY) == 0) {
			this.isBlinkyEdible = game.isGhostEdible(GHOST.BLINKY);
			this.blinkyDist = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(),game.getGhostCurrentNodeIndex(GHOST.BLINKY));
		}
		
		if (game.getGhostLairTime(GHOST.INKY) == 0) {
		this.isInkyEdible = game.isGhostEdible(GHOST.INKY);
		this.inkyDist = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(),game.getGhostCurrentNodeIndex(GHOST.INKY));
		}
		
		if (game.getGhostLairTime(GHOST.PINKY) == 0) {
		this.isPinkyEdible = game.isGhostEdible(GHOST.PINKY);
		this.pinkyDist = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(),game.getGhostCurrentNodeIndex(GHOST.PINKY));
		}
		
		if (game.getGhostLairTime(GHOST.SUE) == 0) {
		this.isSueEdible = game.isGhostEdible(GHOST.SUE);
		this.sueDist = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(),game.getGhostCurrentNodeIndex(GHOST.SUE));
		}
		
		
		 double[] inputs = new double[]{mazeIndex,currentLevel,pacmanPosition,pacmanLivesLeft,currentScore,totalGameTime,currentLevelTime,numOfPillsLeft,numOfPowerPillsLeft,blinkyDist,inkyDist,pinkyDist,sueDist};

	        double[] outputs = mlp.propagate (inputs);
//	        double[] action = new double[numberOfOutputs];
	        double maxOutput = Double.MIN_VALUE;
	        int maxNum =0;
	        for (int i = 0; i < outputs.length; i++) {
	            if(outputs[i]>maxOutput){
	            	maxOutput = outputs[i];
	            	maxNum = i;
	            	break;
	            }
	        }

	        
//	        return action;
		return getMove(maxNum);
	}
	public static MOVE getMove(int i){ 

	     switch(i){ 

	     case 0: return MOVE.DOWN;  
	     
	     case 1: return MOVE.LEFT; 

	     case 2:  return MOVE.NEUTRAL;

	     case 3:  return MOVE.RIGHT; 

	     case 4:  return MOVE.UP; 

	     } 
	     return null;

	} 
	
    private MLP mlp;
    private String name = "SimpleMLPAgent";
    final int numberOfOutputs = 5;
    final int numberOfInputs = 13;

    public SimpleMLPAgent () {
        mlp = new MLP (numberOfInputs, 15, numberOfOutputs);
    }

    private SimpleMLPAgent (MLP mlp) {
        this.mlp = mlp;
    }

    public Evolvable getNewInstance() {
        return new SimpleMLPAgent(mlp.getNewInstance());
    }

    public Evolvable copy() {
        return new SimpleMLPAgent (mlp.copy ());
    }

    public void reset() {
        mlp.reset ();
    }

    public void mutate() {
        mlp.mutate ();
    }

  
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private double probe (int x, int y, byte[][] scene) {
        int realX = x + 11;
        int realY = y + 11;
        return (scene[realX][realY] != 0) ? 1 : 0;
    }
}
