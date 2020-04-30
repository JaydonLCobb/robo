/**
 * Class to represent smart robots.  Smart robots will not collide with
 * rubble or other robots (however, other robots can collide with smart
 * robots.
 * 
 * @author anthonysong
 */
public class SmartRobot extends Robot {


  /**
   * Constructs a new smart robot at position (x, y)
   *
   * @param x The x position of the robot
   * @param y The y position of the robot
   */
  public SmartRobot(int x, int y)
  {
    super(x, y);
  }
  
  /**
   * Stringifies a smart robot
   *
   * @return The stringified robot
   */
  @Override
  public String toString()
  {
    return "S";
  }


  /**
   * TODO: Smart robots move toward the PC in at least one dimension, both if
   * possible, but only if there exists a move which doesn't result in a
   * collision.  A smart robot will never collide with obstrutions or other
   * robots.  It can get stuck behind an obstruction (or, optionally, you can
   * implement some more intelligent pathfinding around obsructions, but that
   * is harder to code, and make an already very hard game harder, as well).
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
	  	if(xDir < 0) {xDir = -1;}
	  	else if(xDir > 0) {xDir = 1;}
	  
	  // create the closest direction to the character. 
	  int yDir = y_pc - yPos;
	  	if(yDir < 0) {yDir = -1;}
	  	else if(yDir > 0) {yDir = 1;}
	  	
	  	// if the next position is equal to the PC position, then PC is dead.
	  	if((xDir + xPos == x_pc )&& (yDir + yPos == y_pc) ) {	  		
	  		//remove and kill the PC cell.
	  		t.nullifyCell(t.getPC().getX(), t.getPC().getY());
	  		t.getPC().die();
	  	}
	  	
	  // If cell is null, then return the pair. 
	  if(t.getCell(xPos + xDir, yPos + yDir) == null) {
		  
		  return new Pair(xPos + xDir, yPos + yDir);
	  }
	  // If cell is not null. The robots stays put, because it does not run into other objects.
	  else {
		  return new Pair(xPos, yPos);
		  
	  }
	  
	  
  
  }
  
}
