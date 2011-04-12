package FeedForwardNetwork;

public class FeedForwardNetwork {
	public static final int MAX_INPUT_LAYER_SIZE = 20;
	public static final int MAX_HIDDEN_LAYER_SIZE = 40;
	public static final int MAX_OUTPUT_LAYER_SIZE = 20;

	public static final int INPUT_TO_HIDDEN = 0;
	public static final int HIDDEN_TO_OUTPUT = 1;

	public static final float DEFAULT_EPSILON = 1;
	public static final float DEFAULT_LEARNING_RATE = 0;

	private double[] InputLayer = new double[MAX_INPUT_LAYER_SIZE + 1];
	private double[] HiddenLayer = new double[MAX_HIDDEN_LAYER_SIZE + 1];
	private double[] OutputLayer = new double[MAX_OUTPUT_LAYER_SIZE];

	private double[][] weightsToHidden = new double[MAX_INPUT_LAYER_SIZE + 1][MAX_HIDDEN_LAYER_SIZE];
	private double[][] weightsToOutput = new double[MAX_HIDDEN_LAYER_SIZE + 1][MAX_OUTPUT_LAYER_SIZE];

	private int inNeurons;
	private int hiddenNeurons;
	private int outNeurons;

	private double epsilon; // accepted error
	private double learningRate;

	public FeedForwardNetwork() {
		configure(1, 1, 1);
	}

	public FeedForwardNetwork(int in, int hidden, int out) {
		configure(in, hidden, out);
	}

	public int random(int x) {
		double rand = ((Math.random() * x) % (x + 1));
		return (int) rand;
	}

	public void configure(int in, int hidden, int out) {
		if ((in > 0) && (in < MAX_INPUT_LAYER_SIZE))
			inNeurons = in;
		else
			inNeurons = 1;
		if ((hidden > 0) && (hidden < MAX_HIDDEN_LAYER_SIZE))
			hiddenNeurons = hidden;
		else
			hiddenNeurons = 1;
		if ((out > 0) && (out < MAX_OUTPUT_LAYER_SIZE))
			outNeurons = out;
		else
			outNeurons = 1;

		epsilon = DEFAULT_EPSILON;
		learningRate = DEFAULT_LEARNING_RATE;

		// to be added: error handling !
	}

	public void init() {
		// initialize weights

		int i, j;

		// all neuron activations set to 0

		for (i = 0; i < MAX_INPUT_LAYER_SIZE + 1; i++)
			InputLayer[i] = 0;

		InputLayer[inNeurons] = 1; // threshold activation (common trick)

		for (i = 0; i < MAX_HIDDEN_LAYER_SIZE + 1; i++)
			HiddenLayer[i] = 0;

		HiddenLayer[hiddenNeurons] = 1; // threshold activation (common
										// trick)

		for (i = 0; i < MAX_OUTPUT_LAYER_SIZE; i++)
			OutputLayer[i] = 0;

		// all weights are set to 0

		for (i = 0; i < MAX_INPUT_LAYER_SIZE + 1; i++)
			for (j = 0; j < MAX_HIDDEN_LAYER_SIZE; j++)
				weightsToHidden[i][j] = 0;

		for (i = 0; i < MAX_HIDDEN_LAYER_SIZE + 1; i++)
			for (j = 0; j < MAX_OUTPUT_LAYER_SIZE; j++)
				weightsToOutput[i][j] = 0;

		// the weights of the configured net (node subset)
		// are set to a random number between -0.5 and 0.5

		for (i = 0; i < MAX_INPUT_LAYER_SIZE + 1; i++)
			for (j = 0; j < MAX_HIDDEN_LAYER_SIZE; j++) {
				double rand = (random(100) - 50) / 100.0;
				weightsToHidden[i][j] = rand;
			}

		for (i = 0; i < MAX_HIDDEN_LAYER_SIZE + 1; i++)
			for (j = 0; j < MAX_OUTPUT_LAYER_SIZE; j++) {
				double rand = (random(100) - 50) / 100.0;
				weightsToOutput[i][j] = rand;
			}
	}

	public void setInput(int x, double value) {

		if ((x >= 0) && (x < inNeurons) && (value >= 0) && (value <= 1))
			InputLayer[x] = value;

		// add error handling !
	}

	public void setOutput(int x, double value) {

		if ((x >= 0) && (x < outNeurons) && (value >= 0) && (value <= 1))
			OutputLayer[x] = value;

		// add error handling !
	}

	public void apply() {
		int i, j;
		double net;

		// add input check !

		// propagate activation through the net.

		// compute hidden layer activation

		InputLayer[inNeurons] = 1; // for threshold computation

		for (j = 0; j < hiddenNeurons; j++) {
			net = 0; // netto input of a neuron

			for (i = 0; i < inNeurons + 1; i++) {
				net += weightsToHidden[i][j] * InputLayer[i];
			}

			HiddenLayer[j] = t(net); // using transfer function (sigmoid)
		}

		for (j = 0; j < outNeurons; j++) {
			net = 0; // netto input of a neuron

			for (i = 0; i < hiddenNeurons + 1; i++) {
				net += weightsToOutput[i][j] * HiddenLayer[i];
			}

			OutputLayer[j] = t(net); // using transfer function (sigmoid)
		}

	}

	public double getWeight(int layer, int x, int y) {
		double ret = -1;

		if (layer == INPUT_TO_HIDDEN) // from input to hidden
		{
			if ((x >= 0) && (x < inNeurons + 1) && // includes threshold
					(y >= 0) && (y < hiddenNeurons)) {
				ret = weightsToHidden[x][y];
			}
		}

		if (layer == HIDDEN_TO_OUTPUT) // from hidden layer to output
		{
			if ((x >= 0) && (x < hiddenNeurons + 1) && // includes threshold
					(y >= 0) && (y < outNeurons)) {
				ret = weightsToOutput[x][y];
			}
		}

		return ret;

		// add error handling !
	}

	public double getOutput(int x) {

		double ret = -1;

		if ((x >= 0) && (x < outNeurons))
			ret = OutputLayer[x];

		return ret;

		// add error handling !
	}

	public double getInput(int x) {

		double ret = -1;

		if ((x >= 0) && (x < inNeurons))
			ret = InputLayer[x];

		return ret;

		// add error handling !
	}

	public double getHidden(int x) {

		double ret = -1;

		if ((x >= 0) && (x < hiddenNeurons))
			ret = HiddenLayer[x];

		return ret;
	}

	public void setEpsilon(double eps) {
		if (eps > 0) {
			epsilon = eps;
		}
	}

	public void setLearningRate(double mu) {
		if ((mu > 0) && (mu <= 10)) {
			learningRate = mu;
		}
	}

	public void setWeights(double[][] w1, double[][] w2) {

		int i, j;

		for (i = 0; i < inNeurons + 1; i++)
			for (j = 0; j < hiddenNeurons; j++)
				weightsToHidden[i][j] = w1[i][j];

		for (i = 0; i < hiddenNeurons + 1; i++)
			for (j = 0; j < outNeurons; j++)
				weightsToOutput[i][j] = w2[i][j];

	}

	public void setWeight(int level, int i, int j, double w) {
		/* check correct weight position */

		if ((level > 1) || (level < 0)) {
			/* error - handling ! */
		} else {
			if (level == 0) {
				if ((i >= 0) && (i < inNeurons + 1) && (j >= 0)
						&& (j < hiddenNeurons)) {
					weightsToHidden[i][j] = w;
				}
			}
			if (level == 1) {
				if ((i >= 0) && (i < hiddenNeurons + 1) && (j >= 0)
						&& (j < outNeurons)) {
					weightsToOutput[i][j] = w;
				}
			}
		}
	}

	public void getWeights(double[][] w1, double[][] w2) {

		int i, j;

		for (i = 0; i < inNeurons + 1; i++)
			for (j = 0; j < hiddenNeurons; j++)
				w1[i][j] = weightsToHidden[i][j];

		for (i = 0; i < hiddenNeurons + 1; i++)
			for (j = 0; j < outNeurons; j++)
				w2[i][j] = weightsToOutput[i][j];

	}

	public double getEpsilon() {
		return epsilon;
	}

	public double getLearningRate() {
		return learningRate;
	}

	public void backpropagate(double[] t) {

		double[] deltaH = new double[MAX_HIDDEN_LAYER_SIZE];
		int i, j;
		double e, y;
		double delta;

		// neural network learning step

		e = energy(t, OutputLayer);

		if (epsilon < e) {
			// backpropagation

			// update weights to output layer
			// Formula : delta_wij = lernrate dj hiddenlayer_i
			// dj = (tj-yj)yj(1-yj)

			for (i = 0; i < hiddenNeurons + 1; i++)
				deltaH[i] = 0;

			for (j = 0; j < outNeurons; j++) {
				y = OutputLayer[j];
				delta = (t[j] - y) * y * (1 - y);

				for (i = 0; i < hiddenNeurons + 1; i++) {
					deltaH[i] += delta * weightsToOutput[i][j];
					weightsToOutput[i][j] += learningRate * delta
							* HiddenLayer[i];
				}
			}

			for (i = 0; i < hiddenNeurons; i++) {
				delta = deltaH[i] * HiddenLayer[i] * (1 - HiddenLayer[i]);

				for (j = 0; j < inNeurons + 1; j++) {
					weightsToHidden[j][i] += learningRate * delta
							* InputLayer[j];
				}
			}
		}
	}

	public double t(double x) {
		return (double) (1 / (1 + Math.exp((double) -x)));
	}

	public double energy(double[] t, double[] y) {
		// no range checks !!

		double energy = 0;
		int i;

		for (i = 0; i < outNeurons; i++) {
			energy += (t[i] - y[i]) * (t[i] - y[i]);
		}

		energy /= 2.0;

		return energy;

	}

}