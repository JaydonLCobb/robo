import java.util.Scanner;

/**
 * TODO: The class implements the player character (PC). This is the player character that is stuck in 
 * a wasteland of evil robots. His goal is to use his zapper to kill as many robots as possible. 
 * 
 * @author anthonysong
 * 
 */
public class PlayerCharacter extends Character {

	/*
	 * Is the player character alive or dead.
	 */
	private boolean alive;

	/**
	 * TODO: Constructs a new PC at position (x, y) in the tableau
	 *
	 * @param x The PCs initial x position.
	 * @param y The PCs initial y position.
	 */
	public PlayerCharacter(int x, int y) {
		super(x, y);
		alive = true;
	}

	/**
	 * TODO: Returns true if and only if the PC is still alive.
	 *
	 * @return Whether or not the PC lives
	 */
	public boolean isAlive() {
		return alive;
	}

	/**
	 * TODO: Marks the PC as no longer being alive
	 */
	public void die() {
		alive = false;
	}

	/**
	 * Stringifies the PC
	 *
	 * @return The stringified PC
	 */
	@Override
	public String toString() {
		return "@";
	}

	/**
	 * This method should never be called.
	 *
	 * @param c Never called
	 * @param t Never called
	 * @return Never called
	 */
	public Cell collideWith(Cell c, Tableau t) {
		return this;
	}

	/**
	 * Handles player I/O. Returns the new (possibly unchanged) position of the PC.
	 *
	 * @param t The tableau
	 * @return The new position of the PC
	 */
	public Pair moveTo(Tableau t) {
		Scanner sc = new Scanner(System.in);
		boolean goodMove = false;
		int toX, toY;
		do {
			toX = xPos;
			toY = yPos;

			System.out.print("; your move: ");

			switch (sc.next().charAt(0)) {
			// Move Left
			case 'h':
				toX--;
				break;
			// Move down
			case 'j':
				toY++;
				break;
			// Move up
			case 'k':
				toY--;
				break;
			// Move right
			case 'l':
				toX++;
				break;
			case 'y':
				toX--;
				toY--;
				break;
			case 'u':
				toX++;
				toY--;
				break;
			case 'b':
				toX--;
				toY++;
				break;
			case 'n':
				toX++;
				toY++;
				break;
			case '.':
				goodMove = true;
				break;
			case 'z':
				if (t.hasZap()) {
					doZap(t);
					goodMove = true;
				}
				break;
			case 'w':
				goodMove = t.startWait();
				break;
			case 'q':
				System.exit(0);
				break;
			default:
			}

			if (toX >= 0 && toX < t.getX() && toY >= 0 && toY < t.getY() && t.getCell(toX, toY) == null) {
				goodMove = true;
			}

			if (!goodMove) {
				System.out.print(t + " Invalid move");
			}
		} while (!goodMove);

		return new Pair(toX, toY);
	}

	/**
	 * 
	 * 
	 * TODO: Gets a valid direction from the player and zaps a ray in that
	 * direction. Rays destroy all robots and rubble (but not rock) in a straight
	 * line in a single direction from the PC to the edge of the tableau
	 *
	 * Can go in all cardinal directions, and diagnols
	 *
	 * @param The tableau
	 */
	void doZap(Tableau t) {
		// if we have zap
		if (t.hasZap()) {

			Scanner sc = new Scanner(System.in);
			boolean goodMove = false;

			// Changing position
			int x = xPos;
			int y = yPos;

			// direction of zap
			int zap_x = 0;
			int zap_y = 0;

			// where to stop the zap to not go out of bounds.
			int x_lim = 0;
			int y_lim = 0;

			while (!goodMove) {

				System.out.print("zap which direction? ");
				System.out.println();

				// determine the direction
				switch (sc.next().charAt(0)) {

				// Zap Left
				case 'h':
					zap_x--;
					x_lim = 0;
					// y_lim = yPos;
					goodMove = true;
					break;

				// Zap down
				case 'j':
					zap_y++;
					y_lim = t.getY()-1;
					// x_lim = xPos;
					goodMove = true;
					break;

				// Zap up
				case 'k':
					zap_y--;
					y_lim = 0;
					// x_lim = xPos;
					goodMove = true;
					break;

				// Zap right
				case 'l':
					zap_x++;
					x_lim = t.getX() - 1;
					// y_lim = yPos;
					goodMove = true;
					break;

				// up left
				case 'y':
					zap_y--;
					zap_x--;
					y_lim = 0;
					x_lim = 0;
					break;

				// up right
				case 'u':
					zap_y--;
					zap_x++;
					y_lim = 0;
					x_lim = t.getX() - 1;
					goodMove = true;
					break;

				// down left
				case 'b':
					zap_y++;
					zap_x--;
					y_lim = t.getX() - 1;
					x_lim = 0;
					goodMove = true;
					break;

				// down right
				case 'n':
					zap_y++;
					zap_x++;
					y_lim = t.getY() - 1;
					x_lim = t.getX() - 1;
					goodMove = true;
					break;

				}// switch case end.
			} // while loop end.

			// add vector of zap_x and zap_y until it reaches a limit.
			while ((y != y_lim) && (x != x_lim)) {
				x = x + zap_x;
				y = y + zap_y;

				// Null pointer exception...
				// If cell is not null
				if (t.getCell(x, y) != null) {
					
					// If the cell can be zapped nullify it. 
					if ((t.getCell(x, y)).getZapped()) {
						t.nullifyCell(x, y);
					}
				}

			}
			
			// decrease zaps by one
			t.useZap();
		}

		// if There are no zaps.
		else {
			System.out.println("No Zaps Left!");
		}

	}

	/**
	 * 
	 * 
	 * TODO Handles the situation where a Cell is zapped (by a ray or an exploding
	 * robot). Zapping vaporizes (no rubble) everything except PermanentRock (which
	 * isn't effected. Returns true if and only if the value of the cell should be
	 * changed to null. The PC is killed by a zap.
	 *
	 * @return Whether or not the cell should be nullified
	 */
	@Override
	public boolean getZapped() {

		// The PC is dead
		alive = false;
		// else return true because PC can be zapped.
		return true;
	}

	/**
	 * Handles the situation where a Cell is hit by a (non-exploding) robot. Getting
	 * hit will leave rock or rubble if cell was rock or rubble, will leave rubble
	 * if cell was a robot. Will cause an explosion if cell is exploding robot.
	 * Returns the value that should be placed in cell after hit, and marks the PC
	 * as dead.
	 *
	 * @param t  The tableau
	 * @param by The thing doing the hitting
	 * @return New value for cell
	 */
	@Override
	public Cell getHit(Tableau t, Robot by) {
		alive = false;
		
		return by;
	}

	/**
	 * Signals whether or not a cell can be removed (from Tableau's robot list).
	 * Robots return true; everything else false. The PC should be marked dead.
	 *
	 * @return false
	 */
	@Override
	public boolean removable() {
		alive = false;

		return false;
	}
}
