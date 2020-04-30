/**
 * Abstract base class to represent the PC and all robots
 */
public abstract class Character extends Cell {
  
  /**
   * Constructs a new character at position (x, y)
   *
   * @param x The x position of the character
   * @param y The y position of the character
   */
  protected Character(int x, int y)
  {
    super(x, y);
  }

  /**
   * Finds a new position for the moving character.
   *
   * @param t The tableau
   * @return The new position
   */
  public abstract Pair moveTo(Tableau t);

  /**
   * Collides character with a cell
   *
   * @param c The cell to be collided with
   * @param t The tableau
   * @return The value to be placed in the cell after the collision
   */
  public abstract Cell collideWith(Cell c, Tableau t);
}
