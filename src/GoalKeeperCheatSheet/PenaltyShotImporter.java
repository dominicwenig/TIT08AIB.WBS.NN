package GoalKeeperCheatSheet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class imports the PenealtyShots from a csv formatted file and converts
 * the String data into normalized numbers.
 * 
 * The layout of the file must obtain the following syntax:
 * Anlauflaenge;Richtung;Verzoegerung;gefoult;Ort;Kurve;Wichtig;Zuschauer;Ziel
 * kurz
 * |mittel|lang;vonLinks|mitte|vonRechts;ja|nein;ja|nein;auswaerts|heimspiel;
 * vorGegner|vorFans;ja|nein;int
 * 
 */
public class PenaltyShotImporter {

	private static List<PenaltyShot> shots;

	/**
	 * Processes a csv file to import and convert the PenaltyShots contained in
	 * the file.
	 * 
	 * @param file
	 *            the file containing PenaltyShot formatted in csv
	 * @return the list of imported and converted PenaltyShots
	 * @throws IOException
	 *             If the training data file could not be read
	 */
	public static List<PenaltyShot> processFile(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		shots = new ArrayList<PenaltyShot>();

		String line;
		while (((line = reader.readLine()) != null)) {
			PenaltyShot shot = getShot(line);
			if (shot != null) {
				shots.add(shot);
			}
		}

		return shots;
	}

	/**
	 * Processes a given line and returns a PenaltyShot specified by the given
	 * line.
	 * 
	 * @param line
	 *            A line of a csv formatted file
	 * @return The imported and converted PenaltyShot extracted from the given
	 *         line. Returns null if the line is invalid to the expected layout.
	 */
	public static PenaltyShot getShot(String line) {
		if ("Anlauflaenge;Richtung;Verzoegerung;gefoult;Ort;Kurve;Wichtig;Zuschauer;Ziel"
				.equals(line)) {
			return null;
		}

		String[] attributes = line.split(";");

		if (attributes.length != 9) {
			System.out
					.println("Invalid data record (incorrect number of columns): "
							+ line);
		}

		PenaltyShot shot = null;
		try {
			double runUpLength = getRunUpLength(attributes[0]);
			double runUpDirection = getRunUpDirection(attributes[1]);
			double delay = getDelay(attributes[2]);
			double fouled = getFouled(attributes[3]);
			double place = getPlace(attributes[4]);
			double ownFanBlock = getOwnBlock(attributes[5]);
			double importance = getImportance(attributes[6]);
			double spectators = getSpectators(attributes[7]);
			double targetX = getTargetX(attributes[8]);
			double targetY = getTargetY(attributes[8]);

			shot = new PenaltyShot(runUpLength, runUpDirection, delay, fouled,
					place, ownFanBlock, importance, spectators, targetX,
					targetY);
		} catch (Exception e) {
			System.out.println("Invalid data record (invalid data): " + line);
		}

		return shot;
	}

	/**
	 * Converts and returns the target's Y-coordinate for a given String input.
	 * 
	 * @param string
	 *            A String containing "unten" or "oben" specifying the target's
	 *            Y-coordinate.
	 * @return The normalized number for the given target (Y-coordinate). If the
	 *         string is "unten" it returns 0.0, or if it is "oben" it returns
	 *         1.0.
	 */
	public static Double getTargetY(String string) {

		Double result = null;
		if (string.toLowerCase().contains("unten")) {
			result = 0.0;
		} else if (string.toLowerCase().contains("oben")) {
			result = 1.0;
		}
		return result;
	}

	/**
	 * Converts and returns the target's X-coordinate for a given String input.
	 * 
	 * @param string
	 *            A String containing "links", "mitte" or "rechts" specifying
	 *            the target's X-coordinate.
	 * @return The normalized number for the given target (X-coordinate). If the
	 *         string is "links" it returns 0.0, if the string is "mitte" it
	 *         returns 0.5, or if it is "rechts" it returns 1.0.
	 */
	public static Double getTargetX(String string) {

		Double result = null;
		if (string.toLowerCase().contains("links")) {
			result = 0.0;
		} else if (string.toLowerCase().contains("mitte")) {
			result = 0.5;
		} else if (string.toLowerCase().contains("rechts")) {
			result = 1.0;
		}
		return result;
	}

	/**
	 * Converts and returns the spectators for a given String input.
	 * 
	 * @param string
	 *            A String containing the amount of spectators.
	 * @return The normalized number for the given spectators. If the amount is
	 *         below 15412 it returns 0.0, if the amount is below 21333.25 it
	 *         returns 0.2, if the amount is below 28011.5 it returns 0.4, if
	 *         the amount is below 31978 it returns 0.6, if the amount is below
	 *         39285 it returns 0.8, or if the amount is above 39285 it returns
	 *         1.0.
	 */
	public static Double getSpectators(String string) {

		Double result = null;
		try {
			if (Double.parseDouble(string) <= 15412) {
				result = 0.0;
			} else if (Double.parseDouble(string) <= 21333.25) {
				result = 0.2;
			} else if (Double.parseDouble(string) <= 28011.5) {
				result = 0.4;
			} else if (Double.parseDouble(string) <= 31978) {
				result = 0.6;
			} else if (Double.parseDouble(string) <= 39285) {
				result = 0.8;
			} else if (Double.parseDouble(string) > 39285) {
				result = 1.0;
			}
		} catch (Exception e) {
			result = null;
		}
		return result;

	}

	/**
	 * Converts and returns the importance for a given String input.
	 * 
	 * @param string
	 *            A String containing "nein" or "ja" specifying the importance.
	 * @return The normalized number for the given importance. If the string is
	 *         "nein" it returns 0.0, or if it is "ja" it returns 1.0.
	 */
	public static Double getImportance(String string) {

		Double result = null;
		if ("nein".equals(string)) {
			result = 0.0;
		} else if ("ja".equals(string)) {
			result = 1.0;
		}
		return result;
	}

	/**
	 * Converts and returns the own block for a given String input.
	 * 
	 * @param string
	 *            A String containing "vorFans" or "vorGeger" specifying the own
	 *            block.
	 * @return The normalized number for the given own block. If the string is
	 *         "vorFans" it returns 0.0, or if it is "vorGeger" it returns 1.0.
	 */
	public static Double getOwnBlock(String string) {

		Double result = null;
		if ("vorFans".equals(string)) {
			result = 0.0;
		} else if ("vorGeger".equals(string)) {
			result = 1.0;
		}
		return result;
	}

	/**
	 * Converts and returns the place for a given String input.
	 * 
	 * @param string
	 *            A String containing "Heimspiel" or "Auswaerts" specifying the
	 *            place.
	 * @return The normalized number for the given place. If the string is
	 *         "Heimspiel" it returns 0.0, or if it is "Auswaerts" it returns
	 *         1.0.
	 */
	public static Double getPlace(String string) {

		Double result = null;
		if ("Heimspiel".equals(string)) {
			result = 0.0;
		} else if ("Auswaerts".equals(string)) {
			result = 1.0;
		}
		return result;
	}

	/**
	 * Converts and returns the fouled state for a given String input.
	 * 
	 * @param string
	 *            A String containing "nein" or "ja" specifying the fouled
	 *            state.
	 * @return The normalized number for the given fouled state. If the string
	 *         is "nein" it returns 0.0, or if it is "ja" it returns 1.0.
	 */
	public static Double getFouled(String string) {

		Double result = null;
		if ("nein".equals(string)) {
			result = 0.0;
		} else if ("ja".equals(string)) {
			result = 1.0;
		}
		return result;
	}

	/**
	 * Converts and returns the delay for a given String input.
	 * 
	 * @param string
	 *            A String containing "nein" or "ja" specifying the delay.
	 * @return The normalized number for the given delay. If the string is
	 *         "nein" it returns 0.0, or if it is "ja" it returns 1.0.
	 */
	public static Double getDelay(String string) {
		Double result = null;
		if ("nein".equals(string)) {
			result = 0.0;
		} else if ("ja".equals(string)) {
			result = 1.0;
		}
		return result;
	}

	/**
	 * Converts and returns the run up direction for a given String input.
	 * 
	 * @param string
	 *            A String containing "vonLinks", "mitte" or "vonRechts"
	 *            specifying the run up direction.
	 * @return The normalized number for the given run up direction. If the
	 *         string is "vonLinks" it returns 0.0, if the string is "mitte" it
	 *         returns 0.5, or if it is "vonRechts" it returns 1.0.
	 */
	public static Double getRunUpDirection(String string) {
		Double result = null;
		if ("vonLinks".equals(string)) {
			result = 0.0;
		} else if ("mitte".equals(string)) {
			result = 0.5;
		} else if ("vonRechts".equals(string)) {
			result = 1.0;
		}
		return result;
	}

	/**
	 * Converts and returns the run up length for a given String input.
	 * 
	 * @param string
	 *            A String containing "kurz", "mittel" or "lang" specifying the
	 *            run up length.
	 * @return The normalized number for the given run up length. If the string
	 *         is "kurz" it returns 0.0, if the string is "mittel" it returns
	 *         0.5, or if it is "lang" it returns 1.0.
	 */
	public static Double getRunUpLength(String string) {
		Double result = null;
		if ("kurz".equals(string)) {
			result = 0.0;
		} else if ("mittel".equals(string)) {
			result = 0.5;
		} else if ("lang".equals(string)) {
			result = 1.0;
		}
		return result;
	}

	/**
	 * Returns the list of penalty shots.
	 * 
	 * @return The list of penalty shots.
	 */
	public List<PenaltyShot> getShots() {
		return shots;
	}
}
