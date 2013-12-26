package jumo.fsm;

import pacman.game.Game;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public abstract class AState implements IState {
	private int priority;
	protected int[] params;
	
	public int getParam(int index) {
		return params[index];
	}
	
	public void setParam(int index, int value) {
		params[index] = value;
	}
	
	@Override
	public void setPriority(int value) {
		priority = value;
	}
	
	@Override
	public int getPriority() {
		return priority;
	}

	@Override
	public int compareTo(IState o) {
		return Integer.valueOf(this.getPriority()).compareTo(Integer.valueOf(o.getPriority()));
	}
	
	@Override
	public int getTarget(Game game) {
		throw new NotImplementedException();
	}
	
	@Override
	public int[] getPath(Game game) {
		throw new NotImplementedException();
	}
	
	@Override
	public boolean returnsTarget() {
		return true;
	}
}
