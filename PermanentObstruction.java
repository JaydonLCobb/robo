/**
 * Class to represent permanent rock
 */
public class PermanentObstruction extends Obstruction {
  /**
   * Stringifies a rock
   *
   * @return The stringified rock
   */
  @Override
  public String toString()
  {
    return "%";
  }

  /**
   * Constructs a new instance of a rock
   *
   * @param x The x position of the rock
   * @param y The y position of the rock
   */
  public PermanentObstruction(int x, int y)
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
    return false;
  }
}
