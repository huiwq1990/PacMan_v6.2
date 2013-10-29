package ga;

import java.util.Random;

public class Simple1
{
    private static final int TARGET = 100;     // The number for the algorithm to find.
    private static final int MAX_INPUTS = 50;  // Number of chromosomes in population.
    private static final int MAX_EPOCHS = 30;  // Arbitrary number of cycles to stop after.

/*
     0000 0000 0000 0000 0000 0000 0000 0000 0000

     Four bits are required to represent the range of characters used:
     DIGITS:
        0: 0000
        1: 0001
        2: 0010
        3: 0011
        4: 0100
        5: 0101
        6: 0110
        7: 0111
        8: 1000
        9: 1001
     OPERATORS:
        +: 1010
        +: 1011
        -: 1100
        -: 1101
        *: 1110
        /: 1111
*/

    private static final int MAX_SIZE = 36;

    private static final int DIGITS[][] = new int[][] {{0, 0, 0, 0}, 
                                                       {0, 0, 0, 1}, 
                                                       {0, 0, 1, 0}, 
                                                       {0, 0, 1, 1}, 
                                                       {0, 1, 0, 0}, 
                                                       {0, 1, 0, 1}, 
                                                       {0, 1, 1, 0}, 
                                                       {0, 1, 1, 1}, 
                                                       {1, 0, 0, 0}, 
                                                       {1, 0, 0, 1}};

    private static final int OPERATORS[][] = new int[][] {{1, 0, 1, 0}, 
                                                          {1, 0, 1, 1}, 
                                                          {1, 1, 0, 0}, 
                                                          {1, 1, 0, 1}, 
                                                          {1, 1, 1, 0}, 
                                                          {1, 1, 1, 1}};

    private static int inputs[][] = new int[MAX_INPUTS][MAX_SIZE];       // Current population.
    private static int nextGen[][] = new int[MAX_INPUTS][MAX_SIZE];      // Next population.
    private static int totals[] = new int[MAX_INPUTS];                   // Decoded values.
    private static double fitness[] = new double[MAX_INPUTS];            // Fitness as percentage.
    private static boolean selected[] = new boolean[MAX_INPUTS];         // Eligible parents.

    private static void geneticAlgorithm()
    {
        boolean done = false;

        initializeChromosomes();

        int epoch = 0;

        while(!done)
        {
            for(int i = 0; i < MAX_INPUTS; i++)
            {
                totals[i] = decodeInput(i);

                
                //when find target or reach max epoch, the program is halt
                if(totals[i] == TARGET || epoch == MAX_EPOCHS){
                    done = true;
                }
            } // i

            getFitness();

            for(int i = 0; i < MAX_INPUTS; i++)
            {
                for(int j = 0; j < MAX_SIZE; j++)
                {
                    System.out.print(inputs[i][j]);
                } // j
                System.out.print(" = " + totals[i]);
                System.out.print("\t" + fitness[i] + "%\n");
            } // i
            System.out.print("\n");

            selection();

            mating();

            prepNextEpoch();

            epoch++;
            // This is here simply to show the runtime status.
            System.out.println("Epoch: " + epoch);
        }

        System.out.println("Done.");
        System.out.println("Completed " + epoch + " epochs.");

        return;
    }

    
    //set input value
    private static void initializeChromosomes()
    {
        int getRand = 0;
        int l = 0;//the length of chromosome
/*
    j =   0    1    2    3    4    5    6    7    8
        0000 0000 0000 0000 0000 0000 0000 0000 0000

        0-3:   operand
        4-7:   operator
        8-11:  operand
        12-15: operator
        16-19: operand
        20-23: operator
        24-27: operand
        28-31: operator
        32-35: operand
*/
        for(int i = 0; i < MAX_INPUTS; i++)
        {
            l = 0;
            for(int j = 0; j <= 8; j++)
            {
                if(j % 2 == 0){
                    // j is even; this is an operand
                    getRand = getRandomNumber(9);
                    for(int k = 0; k <= 3; k++)
                    {
                        inputs[i][l] = DIGITS[getRand][k];
                        l++;
                    } // k
                }else{
                    // j is odd; this is an operator
                    getRand = getRandomNumber(5);
                    for(int k = 0; k <= 3; k++)
                    {
                        inputs[i][l] = OPERATORS[getRand][k];
                        l++;
                    } // k
                }
            } // j
        } // i
        return;
    }

    private static int decodeInput(int InputIndex)
    {
        // Take a chromosome, decode it, evaluate it mathematically, and return the answer.
        // Ignore the usual rules of algebraic evaluation, and simple go from left to right.
        int pointer = 0;
        boolean done = false;
        int operator = 0;
        int operand = 0;
        int total = 0;
/*
        0000 0000 0000 0000 0000 0000 0000 0000 0000

        0-3:   operand
        4-7:   operator
        8-11:  operand
        12-15: operator
        16-19: operand
        20-23: operator
        24-27: operand
        28-31: operator
        32-35: operand
*/
        pointer = 0;
        // Get first operand...
        for(int i = 3; i >= 0; i--)
        {
            // Convert from binary to decimal.
            total += inputs[InputIndex][pointer] * Math.pow(2, i);
            pointer++;
        } // i

        while(!done)
        {
            // Get next operator...
            operator = 0;
            for(int i = 3; i >= 0; i--)
            {
                // Convert from binary to decimal.
                operator += inputs[InputIndex][pointer] * Math.pow(2, i);
                pointer++;
            } // i

            // Get next operand...
            operand = 0;
            for(int i = 3; i >= 0; i--)
            {
                // Convert from binary to decimal.
                operand += inputs[InputIndex][pointer] * Math.pow(2, i);
                pointer++;
            } // i

            if(operator == 10 || operator == 11){
                // Addition
                total += operand;
            }else if(operator == 12 || operator == 13){
                // Subtraction
                total -= operand;
            }else if(operator == 14){
                // Multiplication
                total *= operand;
            }else{
                // Division
                if(operand != 0){ // Avoid divide-by-zero errors.
                    total /= operand;
                }
            }

            if(pointer >= 35){
                done = true;
            }
        }

        total = Math.round(total);

        return total;

    }
/*
    NOTE: The way I've designed the GetFitness function in this example, the computer will sometimes hang or throw an "NaN" error message.

    NaN means "not a number".  This will often show up when a "divide-by-zero"-like error occurs while using type Double variables (as opposed to integers). 
     The way I've  implemented this genetic algorithm, it will sometimes weed out all but the one chromosome closest to the target, which leaves nothing but a whole population of the exact same chromosome.  And when there's no diversity, the algorithm will only be able to generate "NaN" for fitness scores.

    In Example 2, I've added a little code to avoid this.
*/
    private static void getFitness()
    {
        // Renders errors into percentage of correctness.
        // Lowest errors = 100%
            // Less errors renders to higher percentage.
            // More errors renders to lower percentage.
        // Highest errors = 0%

        int bestScore = 0;
        int worstScore = 0;

        // The worst score would be the one furthest from the Target.
        worstScore = Math.abs(TARGET - totals[maximum(totals)]);

        // The best would be the closest.
        bestScore = Math.abs(TARGET - totals[minimum(totals)]);

        // Convert to a weighted percentage.
        bestScore = worstScore - Math.abs(TARGET - totals[minimum(totals)]);
        for(int i = 0; i < MAX_INPUTS; i++)
        {
            fitness[i] = (worstScore - (Math.abs(TARGET - totals[i]))) * 100 / bestScore;
        }
        return;
    }

    private static void selection()
    {
        /*
        'We start out with n individuals, and will stay with n.
        'To do this, pick out the most fit, mate them, then replace the least fit
        'with the new offspring.  The parents will remain for the next
        'population. Basically, the least fit are always being weeded out.
        */
        int getRand = 0;

        for(int i = 0; i < MAX_INPUTS; i++)
        {
            getRand = getRandomNumber(100);
            if(fitness[i] >= getRand){
                selected[i] = true;
            }else{
                selected[i] = false;
            }
        }
        return;
    }

    private static void mating()
    {
        int pointer1 = 0;
        int pointer2 = 0;
        int maxChild = 0;
        int canMate = 0;
        int cannotMate[] = new int[MAX_INPUTS];
        int parentA = 0;
        int parentB = 0;
        int newChild[] = new int[MAX_SIZE];

        for(int i = 0; i < MAX_INPUTS; i++)
        {
            cannotMate[i] = -1;
        }

        // Determine total who can mate.
        pointer1 = 0;
        pointer2 = 0;
        for(int i = 0; i < MAX_INPUTS; i++)
        {
            if(selected[i] == true){
                canMate++;
                // Copy selected individuals to next generation.
                for(int j = 0; j < MAX_SIZE; j++)
                {
                    nextGen[pointer1][j] = inputs[i][j];
                } // j
                pointer1++;
            }else{ // Cannot mate.
                cannotMate[pointer2] = i;
                pointer2++;
            }
        } // i

        maxChild = MAX_INPUTS - canMate; // Total number of offspring to be created.

        if(canMate > 1 && pointer2 > 0){
            for(int i = 0; i < maxChild; i++)
            {
                parentA = chooseParent();
                parentB = chooseParent(parentA);
                crossover(parentA, parentB, newChild);
                for(int j = 0; j < MAX_SIZE; j++)
                {
                    nextGen[pointer1][j] = newChild[j];
                } // j
                pointer1++;
            } // i
        }
        return;
    }

    private static int chooseParent()
    {
        // Overloaded function, see also "ChooseParent(ByVal ParentA As Integer)".
        int parent = 0;
        boolean done = false;

        while(!done)
        {
            // Randomly choose an eligible parent.
            parent = getRandomNumber(MAX_INPUTS - 1);
            if(selected[parent] == true){
                done = true;
            }
        }

        return parent;
    }

    private static int chooseParent(int parentA)
    {
        // Overloaded function, see also "ChooseParent()".
        int parent = 0;
        boolean done = false;

        while(!done)
        {
            // Randomly choose an eligible parent.
            parent = getRandomNumber(MAX_INPUTS - 1);
            if(parent != parentA){
                if(selected[parent] == true){
                    done = true;
                }
            }
        }

        return parent;
    }

    private static void crossover(int chromA, int chromB, int[] newChrom)
    {
        // select a random gene along the length of the chromosomes and swap all genes after that point.
        int randomPoint = 0;

        // We want the point to be at a logical place, so that valid operands and
        // Operators are kept intact.
        randomPoint = getRandomNumber(8);
        randomPoint *= 4;

        for(int i = 0; i < MAX_SIZE; i++)
        {
            if(i < randomPoint){
                newChrom[i] = inputs[chromA][i];
            }else if(i >= randomPoint){
                newChrom[i] = inputs[chromB][i];
            }
        }
        return;
    }

    private static void prepNextEpoch()
    {
        // Copy next generation into current generation input.
        for(int i = 0; i < MAX_INPUTS; i++)
        {
            for(int j = 0; j < MAX_SIZE; j++)
            {
                inputs[i][j] = nextGen[i][j];
            } // j
        } // i

        // Reset flags for selected individuals.
        for(int i = 0; i < MAX_INPUTS; i++)
        {
            selected[i] = false;
        }
        return;
    }

    private static int getRandomNumber(int high) //int low, int high)
    {
        return new Random().nextInt(high);
    }

    private static int minimum(int[] intArray)
    {
        // Returns an array index.
        int winner = 0;
        boolean foundNewWinner = false;
        boolean done = false;
        
        while(!done)
        {
            foundNewWinner = false;
            for(int i = 0; i < MAX_INPUTS; i++)
            {
                if(i != winner){             // Avoid self-comparison.
                    // The minimum has to be in relation to the Target.
                    if(Math.abs(TARGET - intArray[i]) < Math.abs(TARGET - intArray[winner])){
                        winner = i;
                        foundNewWinner = true;
                    }
                }
            }

            if(foundNewWinner == false){
                done = true;
            }
        }

        return winner;
    }

    private static int maximum(int[] intArray)
    {
        // Returns an array index.
        int winner = 0;
        boolean foundNewWinner = false;
        boolean done = false;
        
        while(!done)
        {
            foundNewWinner = false;
            for(int i = 0; i < MAX_INPUTS; i++)
            {
                if(i != winner){             // Avoid self-comparison.
                    // The minimum has to be in relation to the Target.
                    if(Math.abs(TARGET - intArray[i]) > Math.abs(TARGET - intArray[winner])){
                        winner = i;
                        foundNewWinner = true;
                    }
                }
            }

            if(foundNewWinner == false){
                done = true;
            }
        }

        return winner;
    }
    
    public static void main(String[] args)
    {
        geneticAlgorithm();
        return;
    }

}