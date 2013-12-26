package pacman.cwie;
import java.awt.Color;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import pacman.controllers.Controller;
import pacman.game.Constants;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.GameView;
import sun.awt.image.PNGImageDecoder.Chromaticities;
import sun.launcher.LauncherHelper;

import java.util.Random; 


import com.sun.xml.internal.ws.policy.spi.PolicyAssertionValidator.Fitness;

/**
 * Peter Venkman -> http://www.imdb.com/character/ch0000490/
 * Aka Bill Murray.
 * 
 * This Ghostbuster uses a Genetic Algorithm to beat the beasts of the maze.
 * **/

public class PeterVenkman{
	//Constants
    static int CHROMOSOME_SIZE=64;
    static int POPULATION_SIZE=32;
    static int WANTED_FITNESS=20;
    static float MUTATION_PROBABILITY = 0.5f;
    
    //Game heuristics
    static int MAX_GENERATIONS = 20;
    static int EDIBLE_TIME = 20;
    static int FLEE_DISTANCE = 10;

    static int UNWANTED_FITNESS = -10;
    
    static int GHOST_EDIBLE_SCORE = 10000;
    static int POWER_PILL_SCORE = 1000;
    static int POWER_PILL_MULTIPLIER = 2;
    static int PILL_SCORE = 5;
    static int NEUTRAL_SCORE = 1;
    
	public class Gene implements Comparable{

		ArrayList<Integer> junctions = new ArrayList<>();
		Random ran = new Random(System.currentTimeMillis());
		//Variables
		private int m_Fitness;
		private int m_Chromosome[];
		private MOVE m_InitialMove;
		
		public MOVE getInitialMove() {
			return m_InitialMove;
		}

		//Constructor
		Gene(){
			m_Chromosome = new int[CHROMOSOME_SIZE];
			m_Fitness = 0;
			
		}
		
		Gene(MOVE move, Game game){
			m_Chromosome = new int[CHROMOSOME_SIZE];
			m_Fitness = 0;
			m_Chromosome = BuildChromosome(move, game);
			m_InitialMove = move;
		}
		
		public int getFitness(Game game){
			int fitness = 0;
			
			int [] phenoType = getPhenotype(game);
			
			fitness = fitness + getGhostScore(game);
			
			for(int i =0; i<phenoType.length; i++)
			{
				fitness = fitness + phenoType[i];
			}
			m_Fitness = fitness;
			System.out.println("Fitness is set -> " + m_Fitness);
			return fitness;
		}
		private int getGhostScore(Game game) {
			int score = 0;
			for(GHOST ghost : GHOST.values())
			{
				if(game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost), DM.PATH) < FLEE_DISTANCE)
					score -= 10000;
			}
			
			return score;
		}

		public int getFitness(){
			return m_Fitness;
		}
	
		
		public int[] getPhenotype(Game game) {
			int[] phenoType = new int[CHROMOSOME_SIZE];
			for(int i = 0; i<m_Chromosome.length; i++)
			{
				boolean valueIsSet = false;
//				for(GHOST ghost : GHOST.values())
//				{
//					if(game.getGhostCurrentNodeIndex(ghost) == m_Chromosome[i])
//					{
//						phenoType[i] = -10000;
//						valueIsSet = true;
//					}					
//				}
				if(!valueIsSet)
				{
					
					if(game.getPowerPillIndex(m_Chromosome[i]) != -1)
					{
						phenoType[i] = POWER_PILL_SCORE;
						valueIsSet = true;
					}
				}
				if(!valueIsSet)
				{
					if(game.getPillIndex(m_Chromosome[i]) != -1)
					{
						phenoType[i] = PILL_SCORE;
						valueIsSet = true;
					}
				}
				
				if(!valueIsSet)
					phenoType[i] = NEUTRAL_SCORE;//if nothing is on
			}
			
			
			return phenoType;
			
		}
	


	

		private double GhostValues(double d, int ghostEdible) {
			// The value smaller the closer the ghost is, formula is -(1/distance) * 1000
			int modifier = ghostEdible > EDIBLE_TIME ? 1 : -1;
			return  -(1 / d) * 1000;
		}


		public void RandomChromozones(Game game){
			MOVE nextMove = getRandomMove(game.getPacmanCurrentNodeIndex(), game);
			m_Chromosome = BuildChromosome(nextMove, game);
			m_InitialMove = nextMove;
		}
		
        public Gene[] Spawn(Game game, Gene sexualPartner){
            Gene[] result = new Gene[2];
            result[0] = new Gene();
            result[1] = new Gene();
            result[0].RandomChromozones(game);
            result[1].RandomChromozones(game);
            

            for(int i=1; i<CHROMOSOME_SIZE; i++){
                result[0].m_Chromosome[i] = m_Chromosome[i];
                result[1].m_Chromosome[i] = sexualPartner.m_Chromosome[i];
            }

            return result;
        }
        



		

		private int[] BuildChromosome(MOVE nextMove, Game game) {
			int [] newChromosome = new int [CHROMOSOME_SIZE];
			newChromosome[0] = game.getPacmanCurrentNodeIndex(); 
			
			for(int i = 1; i<CHROMOSOME_SIZE; i++)
			{
				if(i > 0)
				{
					if(game.getNeighbour(newChromosome[i - 1], nextMove) != -1)//there is actually a neighbor in this direction
					{
						newChromosome[i] = game.getNeighbour(newChromosome[i-1], nextMove);
						if(getJunctions(game).contains(newChromosome[i]))
							nextMove = getRandomMove(newChromosome[i], game);
					}else{
						nextMove = getRandomMove(newChromosome[i-1], game);
						newChromosome[i] = game.getNeighbour(newChromosome[i-1], nextMove);
					}
				}
			}
			return newChromosome;
		}

		private int[] BuildChromosome(MOVE nextMove, Game game, int index) {
			int [] newChromosome = new int [CHROMOSOME_SIZE];
			newChromosome[0] = game.getPacmanCurrentNodeIndex(); 
			
			for(int i = index; i<CHROMOSOME_SIZE; i++)
			{
				if(i > 0)
				{
					if(game.getNeighbour(newChromosome[i - 1], nextMove) != -1)//there is actually a neighbor in this direction
					{
						newChromosome[i] = game.getNeighbour(newChromosome[i-1], nextMove);
						if(getJunctions(game).contains(newChromosome[i]))
							nextMove = getRandomMove(newChromosome[i], game);
					}else{
						nextMove = getRandomMove(newChromosome[i-1], game);
						newChromosome[i] = game.getNeighbour(newChromosome[i-1], nextMove);
					}
				}
			}
			return newChromosome;
		}
		private ArrayList<Integer> getJunctions(Game game) {
			if(junctions.size() == 0)
			{
				for(int i = 0; i< game.getJunctionIndices().length; i++)
				{
					junctions.add(game.getJunctionIndices()[i]);
				}
			}
	
			return junctions;
		}
		
        public int getChromosomeElement(int index){ return m_Chromosome[index]; }
		public void setChromosomeElement(int index, int value){ m_Chromosome[index]=value; }
		public int getChromosomeSize() { return m_Chromosome.length; }
		
		
		private MOVE getRandomMove(int pacmanCurrentNodeIndex, MOVE pacmanLastMoveMade, Game game)
		{
			MOVE[] moves = game.getPossibleMoves(pacmanCurrentNodeIndex, pacmanLastMoveMade);
			
				int nextMove = ran.nextInt(moves.length);
				return moves[nextMove];
			
		}
		private MOVE getRandomMove(int pacmanCurrentNodeIndex, Game game) {
			MOVE[] moves = game.getPossibleMoves(pacmanCurrentNodeIndex);
			
				int nextMove = ran.nextInt(moves.length);
				return moves[nextMove];
			
		}

		public MOVE getMove(Game game) {
			return game.getMoveToMakeToReachDirectNeighbour(m_Chromosome[0], m_Chromosome[1]);
		}
		
		public void DrawGene(Game game, Color color) {
			for(int i = 0; i < m_Chromosome.length -1; i++)
			{
				try {
					GameView.addLines(game, color, m_Chromosome[i], m_Chromosome[i+1]);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		private void PrintChromosones() {
			for(int i = 0; i<CHROMOSOME_SIZE; i++)
				System.out.print(m_Chromosome[i] + ",");
			
			System.out.println();
		}
		private void PrintPhenoType(int[] phenoType) {
			for(int i = 0; i<CHROMOSOME_SIZE; i++)
				System.out.print(phenoType[i] + ",");
			
			System.out.println();
			
		}

		@Override
		public int compareTo(Object o) {
			
	            //typecasting temporary Gene to compare with "this"
	            Gene tempGene = (Gene)o;
	            
	            //if 'this' fitness is worse than the other Gene's
	            if(getFitness() < tempGene.getFitness()){
	                //this is pushed back.
	                return -1;
	            }//if not:
	            else if(getFitness() > tempGene.getFitness()){
	                //then push to the front .
	                return 1;
	            }
	            //if they're equal then nothing is done.
	            return 0;
	        
	}
		   public void Mutate(Game game){
			   Random rand = new Random(System.currentTimeMillis());
	            int randomPick = rand.nextInt(CHROMOSOME_SIZE);
	            
	            ArrayList<Integer> junctions = new ArrayList();
	            
	            for(int j=0; j<game.getJunctionIndices().length; j++){
	                junctions.add(game.getJunctionIndices()[j]);
	            }
	            
	            for(int i=randomPick/*CHROMOSOME_SIZE-1*/; i<0; i--){
	                if(junctions.contains(m_Chromosome[i])){
	                    MOVE[] moves = game.getPossibleMoves(m_Chromosome[i]);
	                    
	                    MOVE randomMove = moves[rand.nextInt(moves.length)];
	                    
	                    BuildChromosome(randomMove, game, i);
	                    break;
	                }
	            }
	        }
	};

	// Variables
	ArrayList<Gene> m_Population;
	MOVE m_LastMove = MOVE.NEUTRAL;
	int m_LastFitness = 0;
	public  MOVE MakeDecision(Game game, long timeDue) {	
		m_Population = new ArrayList<>();
		int generation = 0;
		for (int i = 0; i < POPULATION_SIZE; i++) {
			Gene gene = new Gene();
			gene.RandomChromozones(game);
			m_Population.add(gene);
		}
		
		Gene bestGene = null;
		while (generation < MAX_GENERATIONS) {
			UpdateGeneration(game);
			int score = Integer.MIN_VALUE;
		
			
			for(int i = 0; i < m_Population.size(); i++)
			{
				if(bestGene == null)
					bestGene = getGene(i);

				if(bestGene.getFitness() > getGene(i).getFitness())
					bestGene = getGene(i);
			}
			
			bestGene.DrawGene(game, Color.red);
			//bestGene.PrintChromosones();
			Fornicate(game);
			generation++;
		}

		return bestGene.getMove(game);
	}
	
    public void Fornicate(Game game){
    	//sort your population
    	 Collections.sort(m_Population);
    	//kill the old and weak.
    	for(int i =0; i<POPULATION_SIZE/2; i+=2)
    		m_Population.remove(0);
    	    	

    	
    	Gene[] children = null;
    	//make children
   
            
    	for(int i = 0; i<POPULATION_SIZE/2; i+=2)
    	{
    		children = getGene(i).Spawn(game, getGene(i+1));
    	     if(new Random(System.currentTimeMillis()).nextFloat() < 0.05){
    	            children[0].Mutate(game);
    	            children[1].Mutate(game);
    	        }
	    	m_Population.add(children[0]);
	    	m_Population.add(children[1]);
    	}
    	//populate the missing population
    }
	
	public void UpdateGeneration(Game game) {
		for(int i = 0; i < POPULATION_SIZE; i+=2)
		{
			getGene(i).getFitness(game);
			getGene(i).getMove(game);
		}
	}
    
	public Gene getGene(int index){ return m_Population.get(index); }
	

	public PeterVenkman() {
		m_Population = new ArrayList<>();
		
	}

}
