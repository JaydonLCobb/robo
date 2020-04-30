/**
 * A simple pair class to facilitate the return of two integers
 */
public class Pair {
  private int x, y;

  /**
   * Constructs a new pair (x, y)
   *
   * @param x The x value
   * @param y The y value
   */
  Pair(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Returns the x value
   *
   * @return The x value
   */
  public int getX()
  {
    return x;
  }

  /**
   * Returns the y value
   *
   * @return The y value
   */
  public int getY()
  {
    return y;
  }

  /**
   * Sets the x value
   *
   * @param x The new x value
   */
  public void setX(int x)
  {
    this.x = x;
  }

  /**
   * Sets the y value
   *
   * @param y The new y value
   */
  public void setY(int y)
  {
    this.y = y;
  }

  /**
   * Stringifies a pair
   *
   * @return The stringified pair
   */
  @Override
  public String toString()
  {
    return "(" + x + "," + y + ")";
  }
}
