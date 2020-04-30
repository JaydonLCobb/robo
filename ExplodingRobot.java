/**
 * Class to represent an exploding robot.  Exploding robots will destroy all
 * characters and rubble adjacent to them when the explode.  Explosions only
 * occur if the exploding robot is the one to have collided with another, not
 * it the exploding robot is collided with.
 */

public class ExplodingRobot extends Robot {
  /**
   * Stringifies an exploding robot
   *
   * @return The stringified robot
   */
  @Override
  public String toString()
  {
    return "E";
  }

  /**
   * Creates a new exploding robot at position (x, y)
   *
   * @param x The x position of the robot
   * @param y The y position of the robot
   */
  public ExplodingRobot(int x, int y)
  {
    super(x, y);
  }

  /**
   * Handles collisions of exploding robots with other robots and
   * obstructions.  Removes all characters and rubble from the explosion
   * radius.
   *
   * @param c The cell being collided with
   * @param t The tableau
   * @return The value that belongs in the destination cell after the explosion
   */
  public Cell collideWith(Cell c, Tableau t)
  {
    if (c != null) {
      int x = c.getX();
      int y = c.getY();

      if (x - 1 >= 0 && y - 1 >= 0 && (t.getCell(x - 1, y - 1) != null) &&
          t.getCell(x - 1, y - 1).getZapped()) {
        t.nullifyCell(x - 1, y - 1);
      }
      if (x - 1 >= 0 && (t.getCell(x - 1, y) != null) &&
          t.getCell(x - 1, y).getZapped()) {
        t.nullifyCell(x - 1, y);
      }
      if (x - 1 >= 0 && y + 1 < t.getY() && (t.getCell(x - 1, y + 1) != null) &&
          t.getCell(x - 1, y + 1).getZapped()) {
        t.nullifyCell(x - 1, y + 1);
      }
      if (y - 1 >= 0 && (t.getCell(x, y - 1) != null) &&
          t.getCell(x, y - 1).getZapped()) {
        t.nullifyCell(x, y - 1);
      }
      if (y + 1 < t.getY() && (t.getCell(x, y + 1) != null) &&
          t.getCell(x, y + 1).getZapped()) {
        t.nullifyCell(x, y + 1);
      }
      if (x + 1 < t.getX() && y - 1 >= 0 && (t.getCell(x + 1, y - 1) != null) &&
          t.getCell(x + 1, y - 1).getZapped()) {
        t.nullifyCell(x + 1, y - 1);
      }
      if (x + 1 < t.getX() && (t.getCell(x + 1, y) != null) &&
          t.getCell(x + 1, y).getZapped()) {
        t.nullifyCell(x + 1, y);
      }
      if (x + 1 < t.getX() && y + 1 < t.getY() &&
          (t.getCell(x + 1, y + 1) != null) &&
          t.getCell(x + 1, y + 1).getZapped()) {
        t.nullifyCell(x + 1, y + 1);
      }
      return c.getZapped() ? null : c;
    }

    return this;
  }
}
