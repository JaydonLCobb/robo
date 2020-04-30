/**
 * Class to represent rock and rubble.  Instances of Obstruction that are not
 * PermanentObstruction are rubble.
 */
public class Obstruction extends Cell {
  /**
   * Stringifies rubble
   *
   * @return The stringified rubble
   */
  @Override
  public String toString()
  {
    return "#";
  }

  /**
   * Constructs a new pile of rubble.
   *
   * @param x The x position of the rubble
   * @param y The y position of the rubble
   */
  public Obstruction(int x, int y)
  {
    super(x, y);
  }


  /**
   * Handles the situation where a Cell is zapped (by a ray or an exploding
   * robot).  Zapping vaporizes (no rubble) everything except PermanentRock
   * (which isn't effected.  Returns true if and only if the value of the
   * cell should be changed to null.
   *
   * @return Whether or not the cell should be nullified
   */
  @Override
  public boolean getZapped()
  {
    return true;
  }

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
  @Override
  public Cell getHit(Tableau t, Robot by)
  {
    return this;
  }
}
