
/**
 * Class to represent robots (non-player characters). This implements many methods that 
 * extend to other specific robots. This includes how the robot moves and if it can blow up. 
 * 
 * @author anthonysong
 */
public class Robot extends Character {
	private static final Cell Cell = null;
	
	/*
	 * This is the index of the robot in the array list of robots. 
	 */
	protected int index;

	/**
	 * Creates a new robot at position (x, y)
	 *
	 * @param x The x position of the robot
	 * @param y The y position of the robot
	 */
	public Robot(int x, int y) {
		super(x, y);
		// p.setX(super.getX());
		// p.setY(super.getY());

	}

	/**
	 * Stringifies a robot
	 *
	 * @return The stringified robot
	 */
	@Override
	public String toString() {
		return "R";
	}

	/**
	 * Returns the index of the robot in the tableau
	 *
	 * @return The index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Updates the index of the robot in the tableau
	 *
	 * @param i The new index
	 */
	public void setIndex(int i) {
		index = i;
	}

	/**
	 * 
	 * TODO: Handles collisions of the robot with another cell. This is the default
	 * collideWith method. If something is in the cell, the robot is removed and the
	 * cell is hit. The content of the cell after the collision is returned.
	 *
	 * @param c The cell to collide with
	 * @param t The tableau
	 * @return The value to be placed in the cell after the collision
	 */
	public Cell collideWith(Cell c, Tableau t) {

		// After move to it will go to collide with.
		// Work with cell C. short method. table is used.

		// If null return this robot in the cell.
		if (c == null) {
			return this;
		}

		// if object, robot dies, return cell.
		else {

			// if object, cell is also hit, below returns this robot.
			// Robot r = (Robot) t.getCell(xPos, yPos);
			c.getHit(t, this);

			// nullify the robot, then remove the robot.
			t.nullifyCell(xPos, yPos);
			t.removeRobot(index);

			return c;
		}

	}

	/**
	 * Handles the situation where a Cell is zapped (by a ray or an exploding
	 * robot). Zapping vaporizes (no rubble) everything except PermanentRock (which
	 * isn't effected. Returns true if and only if the value of the cell should be
	 * changed to null.
	 *
	 * @return Whether or not the cell should be nullified
	 */
	@Override
	public boolean getZapped() {
		return true;
	}

	/**
	 * 
	 * TODO: Handles the situation where a Cell is hit by a (non-exploding) robot.
	 * Getting hit will leave rock or rubble if cell was rock or rubble, will leave
	 * rubble if cell was a robot. Will cause an explosion if cell is exploding
	 * robot. Returns the value that should be placed in cell after hit.
	 *
	 * @param by The thing doing the hitting
	 * @return New value for cell
	 * 
	 * pseudo code:
	 * 
	 *  if current cell is rock or rubble
	 *  	return rock or rubble.
	 *  
	 *  if current cell is robot.
	 *  	return rubble.
	 *  
	 *  if current cell is exploding robot
	 *  	explosion.
	 *  	return null?
	 * 
	 * 
	 * 
	 */
	@Override
	public Cell getHit(Tableau t, Robot by) {


		// if current cell is an exploding robot, its doesnt explode. Other robot (by)
		// is removed.

		if (this.toString() == "E") {

			// Remove robot (by)
			t.nullifyCell(by.getX(), by.getY());
			t.removeRobot(by.getIndex());
			return this;
		}

		// else If hitting robot is exploding robot there is explosion.
		else if (by.toString() == "E") {
			// current is cell is removed.

			if (this != null) {
				t.nullifyCell(xPos, yPos);
				t.removeRobot(this.getIndex());
			}
			// return exploding robot.
			return by;
		}

		// Else current cell is not exploding.
		else {

			// Otherwise, if hitting robot is not exploding Robot,
			// remove the hitting robot and this robot.
			t.removeRobot(by.getIndex());
			t.removeRobot(this.getIndex());

			// return rubble
			return new Obstruction(this.getX(), this.getY());
		}
	}

	/**
	 * Signals whether or not a cell can be removed (from Tableau's robot list).
	 * Robots return true; everything else false. The PC should be marked dead.
	 *
	 * @return false
	 */
	@Override
	public boolean removable() {
		return true;
	}

	/**
	 * TODO: Finds a new position for the moving robot. Robots move toward the PC.
	 * "Toward the PC" means that the robot moves in the direction of the PC in at
	 * least one dimension, and if possible, it moves toward the PC in both
	 * dimensions.
	 *
	 * @param t The tableau
	 * @return The new position
	 */
	public Pair moveTo(Tableau t) {

		// access the location of the player character.
		int x_pc = (t.getPC()).getX();
		int y_pc = (t.getPC()).getY();

		// create the closest direction to the character.
		int xDir = x_pc - xPos;
		if (xDir < 0) {
			xDir = -1;
		} else if (xDir > 0) {
			xDir = 1;
		}

		// create the closest direction to the character.
		int yDir = y_pc - yPos;
		if (yDir < 0) {
			yDir = -1;
		} else if (yDir > 0) {
			yDir = 1;
		}

		// if the next position is equal to the PC position, then PC is dead.
		if ((xDir + xPos == x_pc) && (yDir + yPos == y_pc)) {
			// remove and kill the PC cell.
			t.nullifyCell(t.getPC().getX(), t.getPC().getY());
			t.getPC().die();
		}

		return new Pair(xPos + xDir, yPos + yDir);

	}
}
