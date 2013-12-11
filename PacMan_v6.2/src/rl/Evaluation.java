package rl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pacman.Executor;
import pacman.controllers.examples.StarterGhosts;



/**
 * Class to evaluate performance.
 * 
 * @author zheyang@stanford.edu (Zhe Yang)
 */
public class Evaluation {
  
  public static enum Mode {
    DEBUG,
    DEMO,
    EVAL;
    
    static Mode getMode(String mode) {
      for (Mode m : Mode.values()) {
        if (m.name().equalsIgnoreCase(mode)) {
          return m;
        }
      }
      return Mode.DEMO;
    }
  }
  
  public static class EvaluationData {
    float averageScore = 0;
    float wins = 0;
    float averageKills = 0;
    float averageDistance = 0;
    float averageTimeSpent = 0;
    
    public String toString() {
      return String.format("%f %f %f %f %f",
          averageScore, wins, averageKills, averageDistance, averageTimeSpent);
    }
    
    public void computeFinalEvalInfo() {
      averageScore /= LearningParams.NUM_EVAL_ITERATIONS;
      wins /= LearningParams.NUM_EVAL_ITERATIONS;
      averageKills /= LearningParams.NUM_EVAL_ITERATIONS;
      averageDistance /= LearningParams.NUM_EVAL_ITERATIONS;
      averageTimeSpent /= LearningParams.NUM_EVAL_ITERATIONS;
    }
    
//    public void accumulateEvalInfo(EvaluationInfo evaluationInfo) {
//      averageScore += evaluationInfo.computeWeightedFitness();
//      wins += evaluationInfo.marioStatus == Mario.STATUS_WIN ? 1 : 0;
//      averageKills += 1.0 *
//          evaluationInfo.killsTotal / evaluationInfo.totalNumberOfCreatures;
//      averageDistance += 1.0 *
//          evaluationInfo.distancePassedCells / evaluationInfo.levelLength;
//      averageTimeSpent += evaluationInfo.timeSpent;
//    }
  }
  
  private Mode mode;
  
//  private MarioAIOptions marioAIOptions;
  private PacmanRLAgent agent;
  
  private List<EvaluationData> evaluationResults =
      new ArrayList<EvaluationData>();
  
  public Evaluation(Mode mode) {
    this.mode = mode;
    
    agent = new PacmanRLAgent();
    
//    marioAIOptions = new MarioAIOptions();
//    marioAIOptions.setAgent(agent);
//    marioAIOptions.setVisualization(false);
//    marioAIOptions.setFPS(24);
    
    // Level 1
    /*
    marioAIOptions.setLevelDifficulty(0);
    marioAIOptions.setDeadEndsCount(true);
    marioAIOptions.setTubesCount(true);
    marioAIOptions.setBlocksCount(true);
    marioAIOptions.setGapsCount(false);
    marioAIOptions.setCannonsCount(false);
    marioAIOptions.setGreenMushroomMode(0);
*/    
//    agent.setOptions(marioAIOptions);
    
    agent.setLearningTask(new PacmanLearningTask(agent));
  }
  

  public float evaluate() {
    if (mode == Mode.DEBUG) {
//      marioAIOptions.setVisualization(true);
      LearningParams.DEBUG = 2;
    }
    
    agent.learn();
    
    
    
    Executor exec=new Executor();
    exec.runGameTimed(agent, new StarterGhosts(), true);

    return 1.2f;
//    if (mode == Mode.DEMO) {
//      marioAIOptions.setVisualization(true);
//    }

//    BasicTask basicTask = new BasicTask(marioAIOptions);
//    
//    Logger.println(0, "*************************************************");
//    Logger.println(0, "*                                               *");
//    Logger.println(0, "*              Starting Evaluation              *");
//    Logger.println(0, "*                                               *");
//    Logger.println(0, "*************************************************");
//    
//    System.out.println("Task = " + basicTask);
//    System.out.println("Agent = " + agent);
//    
//    EvaluationData results = new EvaluationData();
//    evaluationResults.add(results);
//    
//    for (int i = 0; i < LearningParams.NUM_EVAL_ITERATIONS; i++) {
//      // Set to a different seed for evaluation.
//      
//      //if (LearningParams.EVAL_SEED >= 0) {
//      //  marioAIOptions.setLevelRandSeed(Utils.seeds[LearningParams.EVAL_SEED]);
//      //}
//      Date date = new Date();
//      marioAIOptions.setLevelRandSeed(Math.round(date.getTime() % 1000));
//         
//      // Make evaluation on the same episode once.
//      int failedCount = 0;
//      while (!basicTask.runSingleEpisode(1)) {
//        System.err.println("MarioAI: out of computational time per action?");
//        failedCount++;
//        if (failedCount >= 3) {
//          System.err.println("Exiting.. =(");
//          System.exit(0);
//        }
//      }
//
//      EvaluationInfo evaluationInfo = basicTask.getEvaluationInfo();
//      results.accumulateEvalInfo(evaluationInfo);
//
//      System.out.println(evaluationInfo.toString());
//    }
    
//    results.computeFinalEvalInfo();
//    return results.averageScore;
  }
  
  public void dumpResult() {
    Utils.dump("eval.txt", Utils.join(evaluationResults, "\n"));
  }
  
  /**
   * 获取参数
   * 例如
   * 模式(mode)的参�?-m
   * @param args
   * @param name
   * @return
   */
  public static String getParam(String[] args, String name) {
    for (int i = 0; i < args.length; i++) {
      String s = args[i];
      if (s.startsWith("-") && s.substring(1).equals(name)) {
        if (i + 1 < args.length) {
          String v = args[i + 1];
          if (!v.startsWith("-")) {
            return v;
          }
        }
        return "";
      }
    }
    return null;
  }
  
  public static boolean isNullOrEmpty(String v) {
    return v == null || v.isEmpty();
  }
  
  /**
   * 获取参数
   * @param args
   * @param name
   * @param defaultValue
   * @return
   */
  public static int getIntParam(String[] args, String name, int defaultValue) {
    String v = getParam(args, name);
    return isNullOrEmpty(v) ? defaultValue : Integer.valueOf(v);
  }
  
  public static boolean getBooleanParam(String[] args, String name) {
    String v = getParam(args, name);
    return v != null;
  }

  public static void main(String[] args) {
	
	  // -m DEMO is Demo Mode  
    Mode mode = Mode.getMode(getParam(args, "m"));
    int numRounds = getIntParam(args, "n", 1);

    LearningParams.NUM_MODES_TO_TRAIN =
        getIntParam(args, "nm", LearningParams.NUM_MODES_TO_TRAIN);
    LearningParams.NUM_SEEDS_TO_TRAIN =
        getIntParam(args, "ns", LearningParams.NUM_SEEDS_TO_TRAIN);
    LearningParams.NUM_TRAINING_ITERATIONS =
        getIntParam(args, "i", LearningParams.NUM_TRAINING_ITERATIONS);
    LearningParams.NUM_EVAL_ITERATIONS =
        getIntParam(args, "ei", LearningParams.NUM_EVAL_ITERATIONS);
    LearningParams.EVAL_SEED =
        getIntParam(args, "es", LearningParams.EVAL_SEED);
    
    LearningParams.LOAD_QTABLE = getBooleanParam(args, "l");
    
    Evaluation eval = new Evaluation(mode);

    for (int i = 0; i < numRounds; i++) {
      System.out.println("~ Round " + i + " ~");
      float finalScore = eval.evaluate();
      System.out.println("Final Score = " + finalScore + "\n");
    }
    eval.dumpResult();

    System.exit(0);
  }
}
