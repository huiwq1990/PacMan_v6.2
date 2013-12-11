package rl;

import java.util.Hashtable;

/**
 * Transition table that keeps track of transitions.
 * 
 * @author zheyang@stanford.edu (Zhe Yang)
 */
public class TransitionTable {
  
  public static class ActionData {
    private int actionCount = 0;
    
    private Hashtable<Long, Integer> transitions =
        new Hashtable<Long, Integer>();
    
    public int getActionCount() {
      return actionCount;
    }
    
    public void setActionCount(int count) {
      actionCount = count;
    }
    
    public void addTransition(long toState) {
      actionCount += 1;
      
      if (!transitions.containsKey(toState)) {
        transitions.put(toState, 0);
      }
      transitions.put(toState, transitions.get(toState) + 1);
    }
  }

  Hashtable<Long, ActionData[]> stateCounter = new Hashtable<Long, ActionData[]>();
  
  private final int actionRange;
  
  public TransitionTable(int actionRange) {
    this.actionRange = actionRange;
  }

  private ActionData[] getState(long state) {
    ActionData[] actionData = stateCounter.get(state);
    if (actionData == null) {
      actionData = new ActionData[actionRange];
      for (int i = 0; i < actionRange; i++) {
        actionData[i] = new ActionData();
      }
      stateCounter.put(state, actionData);
    }
    return actionData;
  }
  
  private ActionData getActionData(long fromState, int action) {
    return getState(fromState)[action];
  }
  
  public void addTransition(long fromState, int action, long toState) {
    getActionData(fromState, action).addTransition(toState);
  }
  
  public int getCount(long fromState, int action) {
    return getActionData(fromState, action).getActionCount();
  }
  
  public int[] getCounts(long state) {
    int[] counts = new int[actionRange];
    for (int i = 0; i < actionRange; i++) {
      counts[i] = getActionData(state, i).getActionCount();
    }
    return counts;
  }
  
  public void setCount(long fromState, int action, int count) {
    getActionData(fromState, action).setActionCount(count);
  }
}
