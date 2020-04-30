/**
 * Abstract base class for tableau cells (both obstructions and characters).
 */
public abstract class Cell {
  protected int xPos, yPos;

  /**
   * Stringifies a cell
   *
   * @return The stringified cell
   */
  @Override
  public abstract String toString();

  /**
   * Constructs a new cell.
   *
   * @param x The x postition of the cell
   * @param y The y postition of the cell
   */
  protected Cell(int x, int y)
  {
    xPos = x;
    yPos = y;
  }

  /**
   * Returns the x position of the cell
   *
   * @return The x position
   */
  public int getX()
  {
    return xPos;
  }

  /**
   * Returns the y position of the cell
   *
   * @return The y position
   */
  public int getY()
  {
    return yPos;
  }

  /**
   * Sets the position of the cell to (x, y)
   *
   * @param x The new x position
   * @param y The new y position
   */
  public void setPosition(int x, int y)
  {
    xPos = x;
    yPos = y;
  }

  /**
   * Handles the situation where a Cell is zapped (by a ray or an exploding
   * robot).  Zapping vaporizes (no rubble) everything except PermanentRock
   * (which isn't effected.  Returns true if and only if the value of the
   * cell should be changed to null.
   *
   * @return Whether or not the cell should be nullified
   */
  public abstract boolean getZapped();

  /**
   * Handles the situation where a Cell is hit by a (non-exploding) robot.
   * Getting hit will leave rock or rubble if cell was rock or rubble, will
   * leave rubble if cell was a robot.  Will cause an explosion if cell is
   * exploding robot.  Returns the value that should be placed in cell after
   * hit.
   *
   * @param t The tableau
   * @param by The thing doing the hitting
   * @return New value for cell
   */
  public abstract Cell getHit(Tableau t, Robot by);

  /**
   * Signals whether or not a cell can be removed (from Tableau's robot
   * list).  Robots return true; everything else false.  The PC should be
   * marked dead.
   *
   * @return false
   */
  public boolean removable()
  {
    return false;
  }
}
