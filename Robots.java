/**


<p> You will be implementing a robot apocalypse game.  The robot hordes are
attacking the earth.  Armed only with your wits and a ray gun with three
charges, you will defend the earth from the robots.  Most of the robots will
simply take the shortest route to you, paying no heed to other robots or
obstructions, but collisions will destroy them.  You can use this to your
advantage by tricking them into running into each other or obstructions.
Some robots can explode, so be careful about letting them get close!  As a
last resort, you can fire a ray, buy you have a limited number of charges;
fortunately, you an get more with each new wave of the invasion.  Your ray
gun is very powerful!  It will destroy everything except rocks in the
direction that it is fired.  Sadly, you are destined to fail.  While you do
provide earths final resistance, the robot hordes are endless and they will
eventually overwhelm you.  The best you can do is inflict as much damage on
them as you can before that happens.

<h1>The user interface</h1>

<p>The user interface is text-based.  Java doesn't provide a mechanism for unbuffered I/O, so it is necessary to hit enter after each key of input.  The movement keys may seem odd, but them come from the popular editor <i>vi</i> and have been used in many, many UNIX games over the past few decades (most notably <i>nethack</i>), and will be very familiar to anybody coming from that tradition.

<p>There is an undocumented (in the UI) quit command bound to 'q'.

<p>You are welcome to add additional case statements to PlayerCharacter.moveTo and PlayerCharacter.doZap in order to alias the movement keys to something you are more familiar with; however, you may not remove the default keybindings!

<h1> What do you need to do?</h1>

<p>Most of the code is provided for you.  You are responsible to implementing or completing the following classes and methods:

<p>* class PlayerCharacter: one or more instance variables and several methods
<p>* class Robot: collideWith, moveTo, and getHit
<p>* class SmartRobot: moveTo
<p>* class Tableau: startWait
 */

public class Robots {
  /**
   * The main method
   *
   *
   * @param args Optionally, a non-default set of dimensions for the tableau
   */

public static void main(String [] args)
  {
    Tableau t;

    if (args.length == 2) {
      int x = Integer.parseInt(args[0]);
      int y = Integer.parseInt(args[1]);

      t = new Tableau(x, y);
    } else {
      t = new Tableau();
    }

    do {
      t.generateLevel();

      do {
        t.takeTurns();
      } while (!t.levelCleared() && !t.gameOver());
    } while(!t.gameOver());
    System.out.println(t + "; You died.  Final score: " + 
                       t.getScore() + " points");
  }
}
