package jumo.utility.neural;

public class TrainingData {
	public double[] inputs;
	public double[] expectedOutputs;
	
	public TrainingData(double[] inputs, double[] expectedOutputs) {
		this.inputs = inputs;
		this.expectedOutputs = expectedOutputs;
	}
}