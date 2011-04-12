package GoalKeeperCheatSheet;

import java.io.File;
import java.io.IOException;
import java.util.List;

import wbs.nn.PenaltyShotView;

public class Starter implements Runnable {
	
	private PenaltyShotView penaltyShot;
	private GoalKeeperCheatSheetNeuronalNetwork NN;
	private File f;
	
	private Thread thread;
	
	public Starter(PenaltyShotView penaltyShot, GoalKeeperCheatSheetNeuronalNetwork NN, File f) {
		if(this.thread == null) 
			this.thread = new Thread(this);
		
		this.penaltyShot = penaltyShot;
		this.NN = NN;
		this.f = f;
		
		this.thread.start();
	}

	@Override
	public void run() {
		penaltyShot.setProgressBar(0);
		try {
			List<PenaltyShot> shots = PenaltyShotImporter.processFile(f);
			NN.teach(shots);
			penaltyShot.setAskAndConfig();
		}catch (IOException e) {
			e.printStackTrace();
			penaltyShot.showError("IOException: File not found.");
		}
		penaltyShot.setProgressBar(1);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
