/**
 * Class to represent fast robots.  Fast robots are twice as fast as all
 * other robots.  They get to move a second time each round of gameplay.
 */
class FastRobot extends Robot {
  /**
   * Stringifies a fast robot
   *
   * @return The stringified robot
   */
  @Override
  public String toString()
  {
    return "F";
  }

  /**
   * Constructs a new fast robot at position (x, y)
   *
   * @param x The x position of the robot
   * @param y The y position of the robot
   */
  public FastRobot(int x, int y)
  {
    super(x, y);
  }
}
