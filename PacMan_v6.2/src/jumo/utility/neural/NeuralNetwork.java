package jumo.utility.neural;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import dataRecording.DataTuple;

public class NeuralNetwork {
	private double[] inputs;
	private double[] outputs;
	private double[] errors;
	private double[][] weights;
	private double[] biases;
	private double[] outputDifferences;
	
	private static Random r = new Random();
	
	private int numTotalNodes;
	private int numInput;
	private int numHidden;
	private int numOutput;
	
	private int[] layerNumbers;

	public NeuralNetwork(int numInput, int numHidden, int numOutput) {
		this(numInput, numHidden, numOutput, true);
	}

	public NeuralNetwork(int numInput, int numHidden, int numOutput, double[][] weights, double[] biases) {
		this(numInput, numHidden, numOutput, false);
		
		this.weights = weights;
		this.biases = biases;
	}
	
	private NeuralNetwork(int numInput, int numHidden, int numOutput, boolean initWeightsAndBiases) {
		this.numInput = numInput;
		this.numHidden = numHidden;
		this.numOutput = numOutput;
		
		numTotalNodes = numInput + numHidden + numOutput;
		
		layerNumbers = new int[] { numInput, numHidden, numOutput };
		
		inputs = new double[numTotalNodes];
		outputs = new double[numTotalNodes];
		errors = new double[numTotalNodes];
		outputDifferences = new double[numOutput];
		
		if (initWeightsAndBiases) {
			weights = initializeWeights(numTotalNodes);
			biases = initializeBiases(numTotalNodes);
		}
	}
	
	public double[] output() {
		int outputLayerIndex = layerNumbers.length - 1;
		double[] output = new double[layerNumbers[outputLayerIndex]];
		
		for (int i = 0; i < output.length; i++) {
			output[i] = outputs[layerIndex(i, outputLayerIndex)];
		}
		
		return output;
	}
	
	private int layerIndex(int nodeIndex, int layer) {
		int layerIndex = 0;
		
		for (int i = 0; i < layer; i++) {
			layerIndex += layerNumbers[i];
		}
		
		return layerIndex + nodeIndex;
	}
	
	private static double[][] initializeWeights(int numTotalNodes) {
		double[][] weights = new double[numTotalNodes][numTotalNodes];
		
		for (int i = 0; i < numTotalNodes; i++) {
			for (int j = 0; j < numTotalNodes; j++) {
				weights[i][j] = getInitialWeight();
			}
		}
		
		return weights;
	}
	
	private static double[] initializeBiases(int numTotalNodes) {
		double[] biases = new double[numTotalNodes];
		
		for (int i = 0; i < numTotalNodes; i ++) {
			biases[i] = getInitialWeight();
		}
		
		return biases;
	}
	
	private static double getInitialWeight() {
		return r.nextDouble() * 2.0 - 1.0;
	}
	
	public NeuralNetwork train(DataTuple[] tds, int maxIterations, int maxTimeSeconds, String errorLogFilename) {
		int iteration = 1;
		
		ArrayList<Double> errors = new ArrayList<Double>();
		
		long startTime = (new Date()).getTime();
		
		while (
				iteration <= maxIterations &&
				((new Date()).getTime() - startTime)/1000 < maxTimeSeconds
				) {
			double learningRate = 0.2; //1.0 / iteration;
			
			double accumulatedError = 0;
			
			for (DataTuple td : tds) {
				this.trainSingle(td, learningRate);
				
				accumulatedError += this.averageError();
			}
			
			accumulatedError = accumulatedError / tds.length;
			
			errors.add(accumulatedError);
			
			SaveErrorLog(errors, errorLogFilename);
			
			iteration = iteration + 1;
		}
		
		return this;
	}
	
	public double[][] weights() { return weights; }
	public double[] biases() { return biases; }
	public int numInput() { return numInput; }
	public int numHidden() { return numHidden; }
	public int numOutput() { return numOutput; }
	
	private static void SaveErrorLog(ArrayList<Double> errors, String filename) {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < errors.size(); i++) {
			if (i > 0) {
				sb.append("\n");
			}
			
			sb.append(errors.get(i));
		}
		
		try {
			PrintWriter out = new PrintWriter(filename);
			out.println(sb.toString());
			out.flush();
			out.close();
		}
		catch (IOException e) {
			System.err.println(e.toString());
		}
	}
	
	public double averageError() {
		double accumulatedError = 0;
		
		for (int i = 0; i < outputDifferences.length; i++) {
			
			accumulatedError += outputDifferences[i];
		}
		
		return Math.pow(accumulatedError / outputDifferences.length, 2);
	}
	
	public NeuralNetwork forward(double[] inputValues) {
		// output of an input unit is its actual input value
		int inputLayer = 0;
		for (int ii = 0; ii < layerNumbers[inputLayer]; ii++) {
			int index = layerIndex(ii, inputLayer);
			
			inputs[index] = inputValues[ii];
			outputs[index] = inputs[index];
		}
		
		// Propagate forward, starting at the first hidden layer (layer 0 is input)
		for (int layer = inputLayer + 1; layer < layerNumbers.length; layer++) {
			int numLayer = layerNumbers[layer];
			
			for (int j = 0; j < numLayer; j++) {
				double accumulatedInput = 0.0;
				int jIndex = layerIndex(j, layer);
				
				int previousLayer = layer - 1;
				int previousLayerCount = layerNumbers[previousLayer];
				
				for (int i = 0; i < previousLayerCount; i++) {
					int iIndex = layerIndex(i, previousLayer);
					
					accumulatedInput += weights[iIndex][jIndex] * outputs[iIndex];
				}
				
				accumulatedInput += biases[jIndex];
				
				inputs[jIndex] = accumulatedInput;
				outputs[jIndex] = sigmoid(inputs[jIndex]);
			}
		}
		
		return this;
	}
	
	private NeuralNetwork backpropagate(double[] expectedOutputs, double learningRate) {
		int outputLayer = layerNumbers.length - 1;
		int numOutputLayer = layerNumbers[outputLayer];
		
		//compute the error
		for (int j = 0; j < numOutputLayer; j++) {
			int jIndex = layerIndex(j, outputLayer);
			
			double jOutput = outputs[jIndex];
			outputDifferences[j] = expectedOutputs[j] - jOutput;
			errors[jIndex] = sigmoidDerivative(jOutput) * outputDifferences[j];
		}
		
		// compute the error with respect to the next higher layer, k
		for (int layer = outputLayer - 1; layer >= 0; layer--) {
			int numLayer = layerNumbers[layer];
			int nextLayer = layer + 1;
			int numNextLayer = layerNumbers[nextLayer];
			
			for (int j = 0; j < numLayer; j++) {
				int jIndex = layerIndex(j, layer);
				
				double accumulatedError = 0.0;
				
				for (int k = 0; k < numNextLayer; k++) {
					int kIndex = layerIndex(k, nextLayer);
					
					accumulatedError += errors[kIndex] * weights[jIndex][kIndex];
				}
				
				errors[jIndex] = sigmoidDerivative(outputs[jIndex]) * accumulatedError;
			}
		}
		
		for (int i = 0; i < numTotalNodes; i++) {
			for (int j = 0; j < numTotalNodes; j++) {
				double dWij = learningRate * errors[j] * outputs[i];
				weights[i][j] += dWij;
			}
		}
		
		for (int j = 0; j < numTotalNodes; j++) {
			double dBj = learningRate * errors[j];
			biases[j] += dBj;
		}
		
		return this;
	}
	
	private NeuralNetwork trainSingle(DataTuple td, double learningRate) {
		this.forward(td.nnInputs());
		this.backpropagate(td.nnExpectedOutput(), learningRate);
		
		return this;
	}
	
	private static double sigmoid(double I) {
		return 1.0 / (1.0 + Math.exp(-I));
	}
	
	private static double sigmoidDerivative(double x) {
		return x * (1.0 - x);
	}
}
