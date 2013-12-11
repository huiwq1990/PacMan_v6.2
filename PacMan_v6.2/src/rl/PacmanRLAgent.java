/* Some thoughts on the Mario implementation. What is to be gained from training?
 * 1. Should the states be rough and abstract? Yes. How abstract? It should be 
 *    abstract enough that the number of state-action pairs is exceedingly large.
 *    But if it is too abstract, we may lose the approximate Markov property. 
 *    (e.g. if the defined Mario state lacks "Mario's position", then suppose
 *    two original scenes, one with Mario on high platform, the other wiht Mario 
 *    on low platform, and other parameters the same. They have the same abstract
 *    state S. But S x Action A -> undetermined for the two scenes.
 *       With that said, we hope given many trials and a large state space the
 *    effect is not affecting us.
 *  
 * 2. Learning for specific actions (keystrokes) or movement preferences?
 *    Learning for keystrokes seems to be hard, but can be tolerated. Consider we
 *    can first hard-code the preferences, and modify the reward function to "unit
 *    learn" the keystroke combo. For example, we could define first learning unit
 *    to be "advance", and set reward to be large for every step going rightward.
 *    Then we train the "search" unit, etc.
 *      After the units complete, we face the problem that given a scene, what is
 *    the task to carry out. This can be completed using a higher-level QTable, or
 *    simply estimate the reward given by carrying out each task, and pick the
 *    best-rewarded.
 *        I think the latter approach is easier, but possibly contain bugs. Let's see
 *    whether is will become a problem.
 * 
 * 3. How to let Mario advance?
 *    -given a scene, abstract to Mario state
 *    -construct a QTable AdvanceTable, containing State, Action pairs
 *    -each Action is a combination of keystrokes
 *    -the MDP is also learned, not predetermined?
 *    -the reward function: the number of steps rightward taken
 *    -possible problem: how to let Mario jump through gaps, platforms and enemies?
 *        -jump until necessary? could give negative rewards for unnecessary jumps
 *    -the Mario state should contain "complete" information about the scene
 *        -idea: "poles", where the Mario should be jumping off and how far?
 * 
 * */

package rl;

import java.util.ArrayList;
import java.util.List;

import pacman.controllers.Controller;
import pacman.game.Game;
import pacman.game.Constants.MOVE;


/**
 * The learning agent.
 * 
 * @author kunyi@stanford.edu (Kun Yi)
 */
public class PacmanRLAgent  extends Controller<MOVE> implements LearningAgent {

  private String name;
  
  // Training options, task and quota.
//  private MarioAIOptions options;
  private PacmanLearningTask learningTask;
  
  // Fields for the Mario Agent
  private PacmanState currentState;

  // Associated Qtable for the agent. Used for RL training.
  private ActionQtable actionTable;

  // The type of phase the Agent is in.
  // INIT: initial phase
  // LEARN: accumulatively update the Qtable
  private enum Phase {
    INIT, LEARN, EVAL
  };
  private Phase currentPhase = Phase.INIT;
  
  private int learningTrial = 0;
  
  private List<Integer> scores =
      new ArrayList<Integer>(LearningParams.NUM_TRAINING_ITERATIONS);
  
  public PacmanRLAgent() {
    setName("Super Mario 229");
    
    currentState = new PacmanState();
    actionTable = new ActionQtable(PacmanAction.TOTAL_ACTIONS);
    
    if (LearningParams.LOAD_QTABLE) {
      actionTable.loadQtable(LearningParams.FINAL_QTABLE_NAME);
    }
    
    Logger.println(0, "*************************************************");
    Logger.println(0, "*                                               *");
    Logger.println(0, "*                Super Mario 229                *");
    Logger.println(0, "*                 Agent created!                *");
    Logger.println(0, "*                                               *");
    Logger.println(0, "*************************************************");
  }
  
  @Override
  public boolean[] getAction() {
    // Transforms the best action number to action array.
//    int actionNumber = actionTable.getNextAction(currentState.getStateNumber());
//    
//    Logger.println(2, "Next action: " + actionNumber + "\n");
    
    return null;
  }

  /**
   * Importance of this function: the scene observation is THE RESULT after
   * performing some action given the previous state. Therefore we could get
   * information on:
   *     1. prev state x prev action -> current state.
   *     2. get the reward for prev state, prev action pair.
   * 
   * The reward function, however, is not provided and has to be customized.
   */
	public MOVE getMove(Game game, long timeDue) 
	{
	    if (currentPhase == Phase.INIT ) {
      // Start learning after Mario lands on the ground.
      Logger.println(1, "============== Learning Phase =============");
      currentPhase = Phase.LEARN;
    } else if (currentPhase == Phase.LEARN) {
      // Update the Qvalue entry in the Qtable.
      actionTable.updateQvalue(
          currentState.calculateReward(), currentState.getStateNumber());
    }
	    
	    int actionNumber = actionTable.getNextAction(currentState.getStateNumber());
	    return PacmanAction.getAction(actionNumber);
	}
  
  
//  @Override
//  public void integrateObservation(Environment environment) {
//    // Update the current state.
//    currentState.update(environment);
//    
//    if (currentPhase == Phase.INIT && environment.isMarioOnGround()) {
//      // Start learning after Mario lands on the ground.
//      Logger.println(1, "============== Learning Phase =============");
//      currentPhase = Phase.LEARN;
//    } else if (currentPhase == Phase.LEARN) {
//      // Update the Qvalue entry in the Qtable.
//      actionTable.updateQvalue(
//          currentState.calculateReward(), currentState.getStateNumber());
//    }
//  }
  
  private void learnOnce() {
    Logger.println(1, "================================================");
    Logger.println(0, "Trial: %d", learningTrial);

    init();
//   System.out.println( ((PacmanRLAgent)learningTask.getAgent()).learningTrial);
   
   int score = (int)learningTask.runSingleEpisode(1);

//    EvaluationInfo evaluationInfo =
//        learningTask.getEnvironment().getEvaluationInfo();
//    
//    int score = evaluationInfo.computeWeightedFitness();
    
    Logger.println(1, "Intermediate SCORE = " + score);
//    Logger.println(1, evaluationInfo.toStringSingleLine());
    
    scores.add(score);

    // Dump the info of the most visited states into file.
    if (LearningParams.DUMP_INTERMEDIATE_QTABLE) {
      actionTable.dumpQtable(
          String.format(LearningParams.QTABLE_NAME_FORMAT, learningTrial));
    }
    
    learningTrial++;
  }

  @Override
  public void learn() {
	  
	  for (int i = 0; i < LearningParams.NUM_TRAINING_ITERATIONS; i++) {
          learnOnce();
      }
//    for (int m = 0; m < LearningParams.NUM_MODES_TO_TRAIN; m++) {
//      options.setMarioMode(m);
//      for (int j = 0; j < LearningParams.NUM_SEEDS_TO_TRAIN; j++) {
//        if (j > 0) {
//          options.setLevelRandSeed(Utils.getSeed(j - 1));
//        }
//        for (int i = 0; i < LearningParams.NUM_TRAINING_ITERATIONS; i++) {
//          learnOnce();
//        }
//      }
//    }
//    setUpForEval();
  }
  
  @Override
  public void init() {
    Logger.println(1, "=================== Init =================");
    currentPhase = Phase.INIT;
    actionTable.explorationChance = LearningParams.EXPLORATION_CHANCE;
  }

  @Override
  public void reset() {
    Logger.println(1, "================== Reset =================");
    currentState = new PacmanState();
  }
  
  public void setUpForEval() {
    Logger.println(1, "============= Dumping Results ============");
    // Dump final Qtable.
    actionTable.dumpQtable(LearningParams.FINAL_QTABLE_NAME);
    // Dump training scores.
    dumpScores(LearningParams.SCORES_NAME);

    // Entering EVAL phase.
    Logger.println(1, "================ Eval Phase ==============");
    currentPhase = Phase.EVAL;

    // Set exploration chance for evaluations.
    actionTable.explorationChance = LearningParams.EVAL_EXPLORATION_CHANCE;
  }
  
  private void dumpScores(String logfile) {
    Utils.dump(logfile, Utils.join(scores, "\n"));
  }
  

  /**
   *  Gives access to the evaluator through learningTask.evaluate(Agent).
   */
  @Override
  public void setLearningTask(PacmanLearningTask learningTask) {
    this.learningTask = learningTask;
  }



@Override
public void giveIntermediateReward(float intermediateReward) {
	// TODO Auto-generated method stub
	
}

@Override
public void setObservationDetails(int rfWidth, int rfHeight, int egoRow,
		int egoCol) {
	// TODO Auto-generated method stub
	
}

@Override
public String getName() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public void setName(String name) {
	// TODO Auto-generated method stub
	
}

@Override
public void giveReward(float reward) {
	// TODO Auto-generated method stub
	
}

@Override
public void newEpisode() {
	// TODO Auto-generated method stub
	
}

@Override
public void setEvaluationQuota(long num) {
	// TODO Auto-generated method stub
	
}

@Override
public Agent getBestAgent() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public void integrateObservation(Game game) {
	// TODO Auto-generated method stub
	
}

 
}
