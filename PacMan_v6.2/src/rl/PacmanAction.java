package rl;

import java.awt.event.KeyEvent;

import pacman.game.Constants.MOVE;


/**
 * Mario actions.
 * 
 * @author zheyang@stanford.edu (Zhe Yang)
 */
public class PacmanAction {


	

  
  // Update the total number when adding new actions.
  public static final int TOTAL_ACTIONS = 5;
  
  private  int actionNumber;
  private  MOVE action;
  

  
  public static MOVE getAction(int actionNumber) {
	  
	  switch(actionNumber)
  	{
	    	case 0: 	return MOVE.UP;
	    	case 1: return MOVE.RIGHT;
	    	case 2: 	return MOVE.DOWN;
	    	case 3: 	return MOVE.LEFT;
	    	default: 				return MOVE.NEUTRAL;
  	}
   
  }
  
  
  
  public static void main(String[] args) {

	  System.out.println(PacmanAction.getAction(2));
  }
}
