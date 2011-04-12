package GoalKeeperCheatSheet;


public class Asker {
	
	private GoalKeeperCheatSheetNeuronalNetwork NN;
	private PenaltyShot PS;
	
	public Asker(GoalKeeperCheatSheetNeuronalNetwork NN, PenaltyShot PS) {
		this.NN = NN;
		this.PS = PS;
	}
	
	public String ask() {
		return NN.ask(this.PS);
	}

	public PenaltyShot getPS() {
		return PS;
	}

	public void setPS(PenaltyShot pS) {
		PS = pS;
	}
	
}
