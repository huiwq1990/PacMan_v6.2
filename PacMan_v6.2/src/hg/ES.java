package hg;

import static pacman.game.Constants.DELAY;

import java.util.EnumMap;
import java.util.Random;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Game;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;


/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Apr 29, 2009
 * Time: 12:16:49 PM
 */
public class ES implements EA {

    private final Evolvable[] population;
    private final double[] fitness;
    private final int elite;
//    private final Task task;
    private final Game task;
    private final int evaluationRepetitions = 1;
    
    
    
    
//    Controller<MOVE> pacManController = new SimpleMLPAgent();
    Controller<EnumMap<GHOST,MOVE>> ghostController = new StarterGhosts();
    Random rnd=new Random(0);
    Game game;
    
    public ES(Game task, Evolvable initial, int populationSize) {
        this.population = new Evolvable[populationSize];
        for (int i = 0; i < population.length; i++) {
            population[i] = initial.getNewInstance();
        }
        this.fitness = new double[populationSize];
        this.elite = populationSize / 2;
        this.task = task;

    }
    
    
    public ES( Evolvable initial, int populationSize) {
        this.population = new Evolvable[populationSize];
        for (int i = 0; i < population.length; i++) {
            population[i] = initial.getNewInstance();
        }
        this.fitness = new double[populationSize];
        this.elite = populationSize / 2;
        this.task = null;

    }

    public void nextGeneration() {
        for (int i = 0; i < elite; i++) {
            evaluate(i);
        }
        for (int i = elite; i < population.length; i++) {
            population[i] = population[i - elite].copy();
            population[i].mutate();
            evaluate(i);
        }
        shuffle();
        sortPopulationByFitness();
    }

    private void evaluate(int which) {
        fitness[which] = 0;
        for (int i = 0; i < evaluationRepetitions; i++) {
            population[which].reset();
            Controller<EnumMap<GHOST,MOVE>> ghostController = new StarterGhosts();
            game=new Game(rnd.nextLong());			
			while(!game.gameOver())
			{
		        game.advanceGame( (( Controller<MOVE>)population[which]).getMove(game.copy(),System.currentTimeMillis()+DELAY),
		        		ghostController.getMove(game.copy(),System.currentTimeMillis()+DELAY));
			}			
			fitness[which]+=game.getScore();            
//            System.out.println("which " + which + " fitness " + fitness[which]);
//            fitness[which] += task.evaluate((Agent) population[which])[0];
//            LOGGER.println("which " + which + " fitness " + fitness[which], LOGGER.VERBOSE_MODE.INFO);
        }
        fitness[which] = fitness[which] / evaluationRepetitions;
    }

    private void shuffle() {
        for (int i = 0; i < population.length; i++) {
            swap(i, (int) (Math.random() * population.length));
        }
    }

    private void sortPopulationByFitness() {
        for (int i = 0; i < population.length; i++) {
            for (int j = i + 1; j < population.length; j++) {
                if (fitness[i] < fitness[j]) {
                    swap(i, j);
                }
            }
        }
    }

    private void swap(int i, int j) {
        double cache = fitness[i];
        fitness[i] = fitness[j];
        fitness[j] = cache;
        Evolvable gcache = population[i];
        population[i] = population[j];
        population[j] = gcache;
    }

    public Evolvable[] getBests() {
        return new Evolvable[]{population[0]};
    }

    public double[] getBestFitnesses() {
        return new double[]{fitness[0]};  //To change body of implemented methods use File | Settings | File Templates.
    }

}
