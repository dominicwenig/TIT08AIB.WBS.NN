package GoalKeeperCheatSheet;

import java.util.ArrayList;
import java.util.List;

import FeedForwardNetwork.FeedForwardNetwork;

/**
 * This class represents a NeuronalNetwork for a GoalKeeperCheatSheet. It can be
 * taught and asks afterwards to predict new PenaltyShots.
 * 
 */
public class GoalKeeperCheatSheetNeuronalNetwork {
	public static final int NUM_OF_INPUT_NODES = 8;
	public static final int NUM_OF_OUTPUT_NODES = 2;
	private static final int ITERATIONS_INTERVALL_FOR_PRINTOUT = 1000;
	private static final int NUM_OF_MAXIMUM_ITERATIONS = 5000;

	private int numOfHiddenNodes;
	private double epsilon;
	private double learningRate;
	private FeedForwardNetwork NN;
	private List<PenaltyShot> learnedShots;

	/**
	 * Creates a new network.
	 * 
	 * @param numOfHiddenNodes
	 *            The number of hidden nodes the network will work with
	 * @param epsilon
	 *            The epsilon used while learning/teaching the network.
	 *            [0<epsilon<1]
	 * @param learningRate
	 *            The learning rate used while learning/teaching the network.
	 *            [0<learning rate<10]
	 */
	public GoalKeeperCheatSheetNeuronalNetwork(int numOfHiddenNodes,
			double epsilon, double learningRate) {
		this.numOfHiddenNodes = numOfHiddenNodes;
		this.epsilon = epsilon;
		this.learningRate = learningRate;

		NN = new FeedForwardNetwork(NUM_OF_INPUT_NODES, this.numOfHiddenNodes,
				NUM_OF_OUTPUT_NODES);
		NN.init();
		NN.setEpsilon(this.epsilon);
		NN.setLearningRate(this.learningRate);

		learnedShots = new ArrayList<PenaltyShot>();
	}
	
	public void setEpsilon(double epsilon) {
		this.NN.setEpsilon(epsilon);
	}
	
	public void setLearningRate(double learningRate) {
		this.NN.setLearningRate(learningRate);
	}
	
	/**
	 * Teaches the network with the given set of training records.
	 * 
	 * @param shots
	 *            The list of training records containing PenaltyShots observed
	 *            in the past.
	 * @return The benchmarking object for this teaching run.
	 */
	public BenchmarkTeaching teach(List<PenaltyShot> shots) {
		learnedShots.addAll(shots);
		int numOfTestCases = learnedShots.size();

		// Define the input and teaching arrays
		double[][] in = new double[numOfTestCases][NUM_OF_INPUT_NODES];
		double[][] teach = new double[numOfTestCases][NUM_OF_OUTPUT_NODES];
		for (int i = 0; i < numOfTestCases; i++) {
			in[i][0] = learnedShots.get(i).getRunUpLength();
			in[i][1] = learnedShots.get(i).getRunUpDirection();
			in[i][2] = learnedShots.get(i).getDelay();
			in[i][3] = learnedShots.get(i).getFouled();
			in[i][4] = learnedShots.get(i).getPlace();
			in[i][5] = learnedShots.get(i).getOwnFanBlock();
			in[i][6] = learnedShots.get(i).getImportance();
			in[i][7] = learnedShots.get(i).getSpectators();
			teach[i][0] = learnedShots.get(i).getTargetX();
			teach[i][1] = learnedShots.get(i).getTargetY();
		}

		double[] output = new double[NUM_OF_OUTPUT_NODES];
		double error = 0.0;
		double totalError = 0.0;
		double maxError = 0.0;
		double minError = Double.MAX_VALUE;
		boolean learned;
		int iterations = 0;
		int correctClassifications = 0;

		System.out.printf("Starting:\n");
		// Learn until all training data record is correctly classified
		while (correctClassifications < numOfTestCases) {
			// Perform backpropagation until the network has learned
			for (int i = 0; i < numOfTestCases; i++) {
				iterations++;

				for (int j = 0; j < NUM_OF_INPUT_NODES; j++) {
					NN.setInput(j, in[i][j]);
				}

				learned = false;

				while (!learned) {
					NN.apply();

					for (int j = 0; j < NUM_OF_OUTPUT_NODES; j++) {
						output[j] = NN.getOutput(j);
					}

					error = NN.energy(teach[i], output);

					if (error > NN.getEpsilon()) {
						NN.backpropagate(teach[i]);
					} else {
						learned = true;
					}
				}
			}

			// Calculate the error for the current state of the network
			correctClassifications = 0;
			totalError = 0.0f;
			for (int i = 0; i < numOfTestCases; i++) {
				for (int j = 0; j < NUM_OF_INPUT_NODES; j++) {
					NN.setInput(j, in[i][j]);
				}

				NN.apply();

				for (int j = 0; j < NUM_OF_OUTPUT_NODES; j++) {
					output[j] = NN.getOutput(j);
				}

				error = NN.energy(teach[i], output);

				if (error < minError)
					minError = error;
				if (error > maxError)
					maxError = error;

				totalError += error;

				if (error < NN.getEpsilon()) {
					correctClassifications++;
				}
			}

			if ((iterations / numOfTestCases)
					% ITERATIONS_INTERVALL_FOR_PRINTOUT == 0)
				System.out.printf("[%4d]>> Korrekte: %d Fehler : %f\n",
						iterations / numOfTestCases, correctClassifications,
						totalError);
			if ((iterations / numOfTestCases) > NUM_OF_MAXIMUM_ITERATIONS) {
				System.out.println("Break due to too much iterations");
				break;
			}
		}

		// Return a benchmark of the current teaching process
		return new BenchmarkTeaching(iterations / numOfTestCases,
				correctClassifications, totalError, maxError, totalError
						/ numOfTestCases, minError);
	}

	/**
	 * Asks the Neuronal Network where a shot might goes to.
	 * 
	 * @param penaltyShot
	 *            A PenaltyShot with all existing input parameters.
	 * @return The String specifying the where the shot might go to.
	 */
	public String ask(PenaltyShot penaltyShot) {
		// Set the Input parameters
		NN.setInput(0, penaltyShot.getRunUpLength());
		NN.setInput(1, penaltyShot.getRunUpDirection());
		NN.setInput(2, penaltyShot.getDelay());
		NN.setInput(3, penaltyShot.getFouled());
		NN.setInput(4, penaltyShot.getPlace());
		NN.setInput(5, penaltyShot.getOwnFanBlock());
		NN.setInput(6, penaltyShot.getImportance());
		NN.setInput(7, penaltyShot.getSpectators());

		// Apply the network for the given input
		NN.apply();

		// Get the output of the network
		double[] o = new double[NUM_OF_OUTPUT_NODES];
		for (int j = 0; j < NUM_OF_OUTPUT_NODES; j++) {
			o[j] = NN.getOutput(j);
		}

		// Converting the output to it's String representation
		StringBuilder builder = new StringBuilder();
		if (o[0] <= 0.33) {
			builder.append("links");
		} else if (o[0] <= 0.66) {
			builder.append("mitte");
		} else {
			builder.append("rechts");
		}
		if (o[1] <= 0.5) {
			builder.append("Unten");
		} else {
			builder.append("Oben");
		}

		// Return the calculated target's String representation
		return builder.toString();
	}

	/**
	 * This class is a data object class containing some benchmark data for the
	 * learning process of the network.
	 */
	public class BenchmarkTeaching {
		int iterations;
		int correctItems;
		double totalError;
		double maxError;
		double avgError;
		double minError;

		public BenchmarkTeaching(int iterations, int correctItems,
				double totalError, double maxError, double avgError,
				double minError) {
			this.iterations = iterations;
			this.correctItems = correctItems;
			this.totalError = totalError;
			this.maxError = maxError;
			this.avgError = avgError;
			this.minError = minError;
		}
	}
}
