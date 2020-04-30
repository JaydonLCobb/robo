import java.util.ArrayList;
import java.util.Random;

/**
 * The Tableau class manages the game board.
 * 
 * 
 * @author anthonysong
 */
public class Tableau {
	/**
	 * The per-level increase in robot density.
	 */
	public static final double ROBOT_DENSITY_DELTA = 0.002;
	/**
	 * The per-level increase in frequency of smart robots.
	 */
	public static final double SMART_PROBABILITY_DELTA = 0.005;
	/**
	 * The per-level increase in frequency of fast robots.
	 */
	public static final double FAST_PROBABILITY_DELTA = 0.005;
	/**
	 * The per-level increase in frequency of exploding robots.
	 */
	public static final double EXPLODING_PROBABILITY_DELTA = 0;
	/**
	 * The per-level decrease in number of obstructions.
	 */
	public static final double OBSTRUCTION_DENSITY_DELTA = -0.0002;
	/**
	 * The initial robot density.
	 */
	public static final double INITIAL_ROBOT_DENSITY = 0.005;
	/**
	 * The initial frequency of smart robots relative to all robots.
	 */
	public static final double INITIAL_SMART_PROBABILITY = 0.05;
	/**
	 * The initial frequency of fast robots relative to all robots.
	 */
	public static final double INITIAL_FAST_PROBABILITY = 0.05;
	/**
	 * The initial frequency of exploding robots relative to all robots.
	 */
	public static final double INITIAL_EXPLODING_PROBABILITY = 0.05;
	/**
	 * The initial density of rock and rubble.
	 */
	public static final double INITIAL_OBSTRUCTION_DENSITY = 0.05;
	/**
	 * The initial frequency of rock relative to rock and rubble.
	 */
	public static final double INITIAL_PERMANENT_PROBABILITY = 0.5;
	/**
	 * The size of the tableau in the horizontal dimension.
	 */
	public static final int DEFAULT_X_SIZE = 58;
	/**
	 * The size of the tableau in the vertical dimension.
	 */
	public static final int DEFAULT_Y_SIZE = 21;
	/**
	 * The number of ray gun zaps added to the PC's ray gun per level.
	 */
	public static final int ZAPS_PER_LEVEL = 3;

	private ArrayList<Robot> npcs;
	private double robotDensity;
	private double smartProbability;
	private double fastProbability;
	private double explodingProbability;
	private double obstructionDensity;
	private double permanentObstructionProbability;
	private int score;
	private int level;
	private int xSize, ySize;
	private Cell[][] tableau;
	private PlayerCharacter pc;
	private int numRobots;
	private int numZaps;
	private boolean waiting;

	/**
	 * Constructs a new tableau.
	 *
	 * @param x The size of the tableau in the horizontal dimension.
	 * @param y The size of the tableau in the vertical dimension.
	 */
	public Tableau(int x, int y) {
		robotDensity = INITIAL_ROBOT_DENSITY;
		smartProbability = INITIAL_SMART_PROBABILITY;
		fastProbability = INITIAL_FAST_PROBABILITY;
		explodingProbability = INITIAL_EXPLODING_PROBABILITY;
		obstructionDensity = INITIAL_OBSTRUCTION_DENSITY;
		permanentObstructionProbability = INITIAL_PERMANENT_PROBABILITY;
		score = 0;
		level = 0;
		tableau = null;
		pc = null;
		xSize = x;
		ySize = y;
		numRobots = 0;
		npcs = null;
		numZaps = 0;
		waiting = false;
	}

	/**
	 * TODO: Starts waiting if and only if there are no smart robots on the level.
	 * Waits until PC or all robots are dead. PC waits in place until all the robots
	 * are dead or if the PC dies. It is a gamble. 
	 *
	 * @return Whether or not waiting is commenced (can the pc wait?)
	 * 
	 */
	public boolean startWait() {
		
		// There are no smart robots, starts as true.
		boolean noSmart = true;
		
		// Check to see if no smart robots on the level. 
		for( int i = 0; i < npcs.size(); i++) {
			
			// Check to see if robot i in the list is a smart robot.
			if(npcs.get(i).toString() == "S") {
				noSmart = false;
				return false;
			}			
		}
		
		// If there are no smart robots.
		if(noSmart) {
			waiting = true;
			
			// while the pc or robots are still alive, take turns. 
			while(!this.gameOver() && !this.levelCleared()) {
				this.takeTurns();				
			}			
		}
		
		// return true because the no more smart robots and 
		// waiting is commenced.
		return true;
		
	}

	/**
	 * Returns the game score.
	 *
	 * @return The score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Constructs a new tableau with default dimensions.
	 */
	public Tableau() {
		this(DEFAULT_X_SIZE, DEFAULT_Y_SIZE);
	}

	/**
	 * Removes a robot from the game and updates score.
	 *
	 * @param index The index of the robot
	 */
	public void removeRobot(int index) {
		score += level;
		npcs.set(index, null);
	}

	/**
	 * Moves a character from (xFrom, yFrom) to (xTo, yTo). Handles collisions.
	 *
	 * @param xFrom The x position of the robot
	 * @param yFrom The x position of the robot
	 * @param xTo   The x position of the destination
	 * @param yTo   The x position of the destination
	 */
	public void moveCharacter(int xFrom, int yFrom, int xTo, int yTo) {
		Character c;

		/* This conditional shouldn't be necessary. See comment in takeTurns. */
		if ((c = (Character) tableau[yFrom][xFrom]) != null) {
			tableau[yFrom][xFrom] = null;
			tableau[yTo][xTo] = c.collideWith(tableau[yTo][xTo], this);
			if (tableau[yTo][xTo] != null) {
				tableau[yTo][xTo].setPosition(xTo, yTo);
			}
		}
	}

	/**
	 * Decrements numZaps.
	 */
	public void useZap() {
		numZaps--;
	}

	/**
	 * Returns true if and only if the PC may zap a ray.
	 *
	 * @return Whether or not the PC may zap a ray
	 */
	public boolean hasZap() {
		return numZaps > 0;
	}

	/**
	 * Generates a new level.
	 */
	public void generateLevel() {
		Random r = new Random();
		double roll;

		npcs = new ArrayList<>();

		level++;

		tableau = new Cell[ySize][xSize];

		numRobots = (int) (robotDensity * xSize * ySize);
		tableau[ySize / 2][xSize / 2] = pc = new PlayerCharacter(xSize / 2, ySize / 2);
		numZaps += ZAPS_PER_LEVEL;
		waiting = false;

		for (int i = 0; i < numRobots; i++) {
			boolean placed = false;
			do {
				int xPos = r.nextInt(xSize);
				int yPos = r.nextInt(ySize);
				/* Robots go in an open cell that is at least 3 Cells from the PC */
				if (tableau[yPos][xPos] == null && !((xPos < ((xSize / 2) + 3) && xPos > ((xSize / 2) - 3))
						&& (yPos < ((ySize / 2) + 3) && yPos > ((ySize / 2) - 3)))) {
					placed = true;
					if ((roll = r.nextDouble()) <= explodingProbability) {
						tableau[yPos][xPos] = new ExplodingRobot(xPos, yPos);
					} else if (roll > explodingProbability && roll <= (explodingProbability + smartProbability)) {
						tableau[yPos][xPos] = new SmartRobot(xPos, yPos);
					} else if (roll > (explodingProbability + smartProbability)
							&& roll <= (explodingProbability + smartProbability + fastProbability)) {
						tableau[yPos][xPos] = new FastRobot(xPos, yPos);
					} else {
						tableau[yPos][xPos] = new Robot(xPos, yPos);
					}
					npcs.add((Robot) tableau[yPos][xPos]);
					((Robot) tableau[yPos][xPos]).setIndex(i);
				}
			} while (!placed);
		}

		int numObstructions = (int) (obstructionDensity * xSize * ySize);
		for (int i = 0; i < numObstructions; i++) {
			boolean placed = false;
			do {
				int xPos = r.nextInt(xSize);
				int yPos = r.nextInt(ySize);
				if (tableau[yPos][xPos] == null) {
					placed = true;
					if (r.nextDouble() <= permanentObstructionProbability) {
						tableau[yPos][xPos] = new PermanentObstruction(xPos, yPos);
					} else {
						tableau[yPos][xPos] = new Obstruction(xPos, yPos);
					}
				}
			} while (!placed);
		}

		robotDensity += ROBOT_DENSITY_DELTA;
		smartProbability += SMART_PROBABILITY_DELTA;
		fastProbability += FAST_PROBABILITY_DELTA;
		explodingProbability += EXPLODING_PROBABILITY_DELTA;
		obstructionDensity += OBSTRUCTION_DENSITY_DELTA;
		if (robotDensity > 0.5) {
			robotDensity = 0.5;
		}
		if (smartProbability > 0.5) {
			smartProbability = 0.5;
		}
		if (explodingProbability > 0.25) {
			explodingProbability = 0.25;
		}
		if (fastProbability > 0.25) {
			fastProbability = 0.25;
		}
		if (obstructionDensity < 0) {
			obstructionDensity = 0;
		}
	}

	/**
	 * Takes turns for the PC and all robots in the tableau. Fast robots get to go
	 * twice. Cleans up dead robots.
	 */
	public void takeTurns() {
		Character c;
		Pair pos;

		System.out.print(this);

		if (!waiting) {
			pos = pc.moveTo(this);
			moveCharacter(pc.getX(), pc.getY(), pos.getX(), pos.getY());
		}

		for (int i = 0; i < npcs.size() && pc.isAlive(); i++) {
			if ((c = npcs.get(i)) != null) {
				pos = c.moveTo(this);
				moveCharacter(c.getX(), c.getY(), pos.getX(), pos.getY());
			}
		}

		/* Fast robots get to go again */
		for (int i = 0; i < npcs.size() && pc.isAlive(); i++) {
			if ((c = npcs.get(i)) instanceof FastRobot) {
				pos = c.moveTo(this);
				moveCharacter(c.getX(), c.getY(), pos.getX(), pos.getY());
			}
		}

		/*
		 * There is a subtle bug in which a robot is removed from the tableau but it
		 * remains in the list. This results in the wrong number of remaining robots
		 * being reported to the player. It also forces the check in moveCharacter
		 * (which shouldn't be necessary). My Java-debugger-foo is not sufficient to the
		 * task to tracking this down in reasonable time, so I'm sidestepping the issue
		 * with this (really bad) loop to check for inconsistancies between the tableau
		 * and the list. This loop would not be necessary if I just fixed the damn bug
		 * properly.
		 */
		for (int i = 0; i < npcs.size(); i++) {
			Robot r = npcs.get(i);
			if (r != null && tableau[r.getY()][r.getX()] != r) {
				System.out.println("Found one!");
				npcs.set(i, null);
			}
		}

		for (int i = 0; i < npcs.size() && pc.isAlive();) {
			if ((c = npcs.get(i)) == null) {
				npcs.remove(i);
			} else {
				i++;
			}
		}

		for (int i = 0; i < npcs.size() && pc.isAlive(); i++) {
			npcs.get(i).setIndex(i);
		}
	}

	/**
	 * Returns true if and only if the level is clear of robots.
	 *
	 * @return Wether or not the level is free of robots
	 */
	public boolean levelCleared() {
		return npcs.size() == 0;
	}

	/**
	 * Returns true if and only if the PC is alive
	 *
	 * @return Whether or not the game is over
	 */
	public boolean gameOver() {
		return pc.isAlive() == false;
	}

	/**
	 * Returns the value of cell (x, y) in the tableau
	 *
	 * @param x The x position of the cell
	 * @param y The y position of the cell
	 * @return The cell at (x, y)
	 */
	public Cell getCell(int x, int y) {
		return tableau[y][x];
	}

	/**
	 * Kills characters (robots and PC) by unusual means (rays and explosions).
	 *
	 * @param x The x position of the character
	 * @param y The y position of the character
	 */
	public void nullifyCell(int x, int y) {
		if (tableau[y][x] != null && tableau[y][x].removable()) {
			removeRobot(((Robot) tableau[y][x]).getIndex());
		}

		tableau[y][x] = null;
	}

	/**
	 * Returns the size of the dungeon in the X dimension
	 *
	 * @return The size of the dungeon in the X dimension
	 */
	public int getX() {
		return xSize;
	}

	/**
	 * Returns the size of the dungeon in the Y dimension
	 *
	 * @return The size of the dungeon in the Y dimension
	 */
	public int getY() {
		return ySize;
	}

	/**
	 * Returns a reference to the PC
	 *
	 * @return A reference to the PC
	 */
	public PlayerCharacter getPC() {
		return pc;
	}

	/**
	 * Stringifies the tableau and a help menu
	 *
	 * @return The stringified tableau
	 */
	@Override
	public String toString() {
		String s = "";

		for (int j = 0; j < ySize + 2; j++) {
			for (int i = 0; i < xSize + 2; i++) {
				if (i == 0 || i == xSize + 1) {
					s = s + "|";
				} else if (j == 0 || j == ySize + 1) {
					s = s + "-";
				} else {
					if (tableau[j - 1][i - 1] == null) {
						s = s + " ";
					} else {
						s = s + tableau[j - 1][i - 1].toString();
					}
				}
			}
			switch (j) {
			case 0:
				s = s + " ~~ Robots 227! ~~  ";
				break;
			case 2:
				s = s + " Movement:          ";
				break;
			case 3:
				s = s + "   h: left          ";
				break;
			case 4:
				s = s + "   j: down            ";
				break;
			case 5:
				s = s + "   k: up          ";
				break;
			case 6:
				s = s + "   l: right         ";
				break;
			case 7:
				s = s + "   y: up-left       ";
				break;
			case 8:
				s = s + "   u: up-right      ";
				break;
			case 9:
				s = s + "   b: down-left     ";
				break;
			case 10:
				s = s + "   n: down-right    ";
				break;
			case 11:
				s = s + "   .: stay in place ";
				break;
			case 12:
				s = s + "   z: zap a ray (" + numZaps + ")";
				break;
			case 13:
				s = s + "   w: wait in place ";
				break;
			case 14:
				s = s + " Key:               ";
				break;
			case 15:
				s = s + "   @: You!          ";
				break;
			case 16:
				s = s + "   R: Normal robot  ";
				break;
			case 17:
				s = s + "   E: Exploding     ";
				break;
			case 18:
				s = s + "      robot         ";
				break;
			case 19:
				s = s + "   F: Fast robot    ";
				break;
			case 20:
				s = s + "   S: Smart robot   ";
				break;
			case 21:
				s = s + "   #: Rubble        ";
				break;
			case 22:
				s = s + "   %: Permanent rock";
				break;
			default:
			}
			s = s + "\n";
		}

		return s + "Level " + level + ", " + score + " points, " + npcs.size() + " robots";
	}

}