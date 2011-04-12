package GoalKeeperCheatSheet;

/**
 * This class is a data object class. It represent a PenaltyShot in a way that
 * it stores certain influences for a PenaltyShot and the chosen target.
 */
public class PenaltyShot {
	//Input parameters
	private double runUpLength;
	private double runUpDirection;
	private double delay;
	private double fouled;
	private double place;
	private double ownFanBlock;
	private double importance;
	private double spectators;

	//Output parameters
	private double targetX;
	private double targetY;

	/**
	 * Creates a new PenaltyShot which is identified by the given parameters.
	 * 
	 * @param runUpLength
	 *            How far the striker is away from the ball. (kurz = 0; mittel =
	 *            0.5; lang=1)
	 * @param runUpDirection
	 *            In direction the striker starts his run up from. (vonLinks =
	 *            0; mitte = 0.5; vonRechts = 1)
	 * @param delay
	 *            If a the striker delayed his run up. (nein = 0; ja = 1)
	 * @param fouled
	 *            If the striker was fouled in the situation causing the
	 *            PenaltyShot. (nein = 0; ja = 1)
	 * @param place
	 *            If the striker's team plays at their home stadium. (Heimspiel
	 *            = 0; Auswärts = 1)
	 * @param ownFanBlock
	 *            If the striker's shots in front of his own fan block. (vorFans
	 *            = 0; vorGeger = 1)
	 * @param importance
	 *            If the PenaltyShot is important for the current standing.
	 *            (nein = 0; ja = 1)
	 * @param spectators
	 *            How many spectators are in the stadium. (If the number of
	 *            spectators is below 15412 it must be 0.0, if the number of
	 *            spectators is below 21333.25 it must be 0.2, if the number of
	 *            spectators is below 28011.5 it must be 0.4, if the amount is
	 *            below 31978 it must be 0.6, if the number of spectators is
	 *            below 39285 it must be 0.8, or if the number of spectators is
	 *            above 39285 it must be 1.0.)
	 * @param targetX
	 *            The target's X-coordinate. ("links" = 0.0; "mitte" = 0.5;
	 *            "rechts" = 1.0.)
	 * @param targetY
	 *            The target's Y-coordinate. ("oben" = 1.0; "unten" = 0.0)
	 */
	public PenaltyShot(double runUpLength, double runUpDirection, double delay,
			double fouled, double place, double ownFanBlock, double importance,
			double spectators, double targetX, double targetY) {
		super();
		this.runUpLength = runUpLength;
		this.runUpDirection = runUpDirection;
		this.delay = delay;
		this.fouled = fouled;
		this.place = place;
		this.ownFanBlock = ownFanBlock;
		this.importance = importance;
		this.spectators = spectators;
		this.targetX = targetX;
		this.targetY = targetY;
	}

	public double getRunUpLength() {
		return runUpLength;
	}

	public double getRunUpDirection() {
		return runUpDirection;
	}

	public double getDelay() {
		return delay;
	}

	public double getFouled() {
		return fouled;
	}

	public double getPlace() {
		return place;
	}

	public double getOwnFanBlock() {
		return ownFanBlock;
	}

	public double getImportance() {
		return importance;
	}

	public double getSpectators() {
		return spectators;
	}

	public double getTargetX() {
		return targetX;
	}

	public double getTargetY() {
		return targetY;
	}

	@Override
	public String toString() {
		return "PenaltyShot [runUpLength=" + runUpLength + ", runUpDirection="
				+ runUpDirection + ", delay=" + delay + ", fouled=" + fouled
				+ ", place=" + place + ", ownFanBlock=" + ownFanBlock
				+ ", importance=" + importance + ", spectators=" + spectators
				+ ", targetX=" + targetX + ", targetY=" + targetY + "]";
	}

}
