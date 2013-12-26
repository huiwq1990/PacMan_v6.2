package jumo.fsm;

import pacman.game.Game;

public interface IState extends Comparable<IState> {
	public boolean canHandle(Game game);
	
	public int getTarget(Game game);
	public int[] getPath(Game game);
	
	public boolean returnsTarget();
	
	public int getNumberOfParams();
	public int getParam(int index);
	public void setParam(int index, int value);
	
	public void setPriority(int value);
	public int  getPriority();
}
