package rl;

/**
 * Various parameters used by the learning algorithm.
 * 
 * @author zheyang@stanford.edu (Zhe Yang)
 */
public class LearningParams {

  public static int DEBUG = 0;

  /**
   * Total number of iterations to train for each configuration (mode/seed).
   */
  public static int NUM_TRAINING_ITERATIONS = 800;

  /**
   * Number of Mario modes to train.
   */
  public static int NUM_MODES_TO_TRAIN = 3;

  /**
   * Whether we should use a different random seed for training.
   */
  public static int NUM_SEEDS_TO_TRAIN = 1;

  /**
   * Number of evaluation iterations to run.
   */
  public static int NUM_EVAL_ITERATIONS = 10;

  /**
   * Whether we should use a different random seed for evaluation.
   */
  public static int EVAL_SEED = -1;

  /**
   * Exploration chance during evaluation.
   */
  public static final float EVAL_EXPLORATION_CHANCE = 0.01f;

  // E-GREEDY Q-LEARNING SPECIFIC VARIABLES
  /**
   * For e-greedy Q-learning, when taking an action a random number is checked
   * against the explorationChance variable: if the number is below the
   * explorationChance, then exploration takes place picking an action at
   * random. Note that the explorationChance is not a final because it is
   * customary that the exploration chance changes as the training goes on.
   */
  public static final float EXPLORATION_CHANCE = 0.3f;

  /**
   * The discount factor is saved as the gammaValue variable. The discount
   * factor determines the importance of future rewards. If the gammaValue is 0
   * then the AI will only consider immediate rewards, while with a gammaValue
   * near 1 (but below 1) the AI will try to maximize the long-term reward even
   * if it is many moves away.
   */
  public static final float GAMMA_VALUE = 0.6f;
  //public static final float GAMMA_VALUE = 0.2f;
  
  /**
   * The learningRate determines how new information affects accumulated
   * information from previous instances. If the learningRate is 1, then the new
   * information completely overrides any previous information. Note that the
   * learningRate is not a final because it is customary that the exploration
   * chance changes as the training goes on.
   * 
   * The actual learning rate will decrease as the number of times the given
   * state and action are visited increase.
   */
  public static final float LEARNING_RATE = 0.8f;
 
  // Reward/state related params.
  /**
   * The minimum distance Mario must travel in a frame to receive distance
   * reward, and to indicate that Mario is moving instead of being stuck.
   */
  public static final int MIN_MOVE_DISTANCE = 2;
  
  /**
   * Mario will change to the stuck state if he has been stuck for
   * NUM_STUCK_FRAMES number of frames.
   */
  public static final int NUM_STUCK_FRAMES = 25;
  
  /**
   * Number of observation levels (window sizes).
   */
  public static final int NUM_OBSERVATION_LEVELS = 3;
  
  /**
   * Window size of each observation level.
   */
  public static final int[] OBSERVATION_SIZES = {1, 3, 5};
  
  /**
   * Scalers to apply to distance/elevation rewards when enemies are present
   * in the corresponding observation level.
   */
  public static final float[] ENEMIES_AROUND_REWARD_SCALER = {0f, 0f, 0.15f};
  
  public static final class REWARD_PARAMS {
    public static final int distance = 2;
    public static final int elevation = 8;
    public static final int collision = -800;
    public static final int killedByFire = 60;
    public static final int killedByStomp = 60;
    public static final int stuck = -20;
    
    // Params below are not used.
    public static final int win = 0;
    public static final int mode = 0;
    public static final int coins = 0;
    public static final int flowerFire = 0;
    public static final int kills = 0;
    public static final int killedByShell = 0;
    public static final int mushroom = 0;
    public static final int timeLeft = 0;
    public static final int hiddenBlock = 0;
    public static final int greenMushroom = 0;
    public static final int stomp = 0;
  };
  
  // Logging related params.
  /**
   * Whether we should dump intermediate Q table values.
   */
  public static final boolean DUMP_INTERMEDIATE_QTABLE = false;

  /**
   * Whether we should load the final Q table trained from last time.
   */
  public static boolean LOAD_QTABLE = false;
  
  /**
   * The format of intermediate Q table dump filenames.
   */
  public static String QTABLE_NAME_FORMAT = "qt.%d.txt";
  
  /**
   * The filename of the final Q table dump.
   */
  public static String FINAL_QTABLE_NAME = "qt.final.txt";

  /**
   * The filename of scores dump.
   */
  public static String SCORES_NAME = "scores.txt";
}
