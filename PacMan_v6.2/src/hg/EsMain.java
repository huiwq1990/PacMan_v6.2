package hg;

import pacman.controllers.Controller;
import pacman.game.Constants.MOVE;
import wox.serial.Easy;

public class EsMain {
	 final static int generations = 100;
	    final static int populationSize = 1000;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 Evolvable initial = new SimpleMLPAgent();
		 
		  ES es = new ES ( initial, populationSize);
		  
		  for (int gen = 0; gen < generations; gen++) {
              //task.setStartingSeed((int)(Math.random () * Integer.MAX_VALUE));
              es.nextGeneration();
              double bestResult = es.getBestFitnesses()[0];
              System.out.println("Generation " + gen + " best " + bestResult);
            
              Controller<MOVE> a = (Controller<MOVE>) es.getBests()[0];
             es.getBestFitnesses();
              
             Easy.save (es.getBests()[0], "evolved.xml");

          }
		
	}

}
