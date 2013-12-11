package rl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Q table implementation of the cs229 learning agent.
 * 
 * @author kunyi@stanford.edu (Kun Yi)
 */
public class ActionQtable extends Qtable {

  TransitionTable transitions;

  public ActionQtable(int actionRange) {
    super(actionRange);
    transitions = new TransitionTable(actionRange);
  }

  @Override
  public int getBestAction(long stateNumber) {
    float[] rewards = this.getActionsQValues(stateNumber);
    if (rewards == null) {
      System.err.println("No rewards defined for this state");
      return 0;
    } else {
      float maxRewards = Float.NEGATIVE_INFINITY;
      int indexMaxRewards = 0;

      for (int i = 0; i < rewards.length; i++) {
        if (maxRewards < rewards[i]) {
          maxRewards = rewards[i];
          indexMaxRewards = i;
        }
      }

      Logger.println(4, "Q values: " + Utils.join(rewards, ", "));
      Logger.println(4, "Best action: " + indexMaxRewards);
      
      return indexMaxRewards;
    }
  }
  
  @Override
  public void updateQvalue(float reward, long currentStateNumber) {
    transitions.addTransition(prevState, prevAction, currentStateNumber);

    // Update Q values using the following update rule:
    //
    // Q(prevState, prevAction) =
    //     (1 - alpha) * Qprev + alpha * (reward + gamma * maxQ)
    //
    // where alpha = learningRate / # prevState/prevAction visited.

    float[] prevQs = getActionsQValues(prevState);
    float prevQ = prevQs[prevAction];

    int bestAction = getBestAction(currentStateNumber);
    float maxQ = getActionsQValues(currentStateNumber)[bestAction];

    float alpha =
        learningRate / transitions.getCount(prevState, prevAction);
    
    //alpha = 0.15f;

    //alpha = 0.15f;
    
    float newQ = (1 - alpha) * prevQ +  alpha * (reward + gammaValue * maxQ);

    prevQs[prevAction] = newQ;
    
    this.getTable().put(prevState, prevQs);
  }
  

  @Override
  float[] getInitialQvalues(long stateNumber) {
    float[] initialQvalues = new float[actionRange];
    for (int i = 0; i < actionRange; i++) {
      // Set as random float ranged (-.1, .1), check whether makes sense.
      initialQvalues[i] = (float) (randomGenerator.nextFloat() * 0.2 - 0.1);
    }
    return initialQvalues;
  }

  public void dumpQtable(String logfile) {
    Logger.println(1, "** Dumping Qtable to " + logfile + " **");
    
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(logfile));
      writer.write("");
      for (long key : getTable().keySet()){
        writer.append(printState(key) + "\n");
      }
      writer.close();
    } catch (IOException x) {
      System.err.println("Failed to write qtable to: " + logfile);
    }
  }
  
  public String printState(long key) {
    return String.format(
        "%d:%s:%s",
        key,
        Utils.join(getTable().get(key), " "),
        Utils.join(transitions.getCounts(key), " "));
  }
  
  private void parseState(String line) {
    String[] tokens = line.split(":");
    long state = Long.valueOf(tokens[0]);
    String[] qvalueStrings = tokens[1].split(" ");
    String[] countStrings = tokens[2].split(" ");
    float[] qvalues = getActionsQValues(state);
    for (int i = 0; i < actionRange; i++) {
      qvalues[i] = Float.valueOf(qvalueStrings[i]);
      transitions.setCount(state, i, Integer.valueOf(countStrings[i]));
    }
  }
  
  public void loadQtable(String logfile) {
    Logger.println(1, "** Loading Qtable from " + logfile + " **");
    try {
      BufferedReader reader = new BufferedReader(new FileReader(logfile));
      String line;
      while ((line = reader.readLine()) != null) {
        parseState(line);
      }
      reader.close();
    } catch (Exception e) {
      System.err.println("Failed to load qtable from: " + logfile);
    }
  }
}
