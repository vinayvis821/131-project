package project;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.sound.midi.Sequence;

import edu.princeton.cs.introcs.StdDraw;
import support.cse131.ArgsProcessor;


public class Main {
	
	public static void main(String[] args) {
		
		StdDraw.enableDoubleBuffering();
		ArgsProcessor ap = new ArgsProcessor( args );
		Color playerOneColor = new Color( 9, 247, 5 );
		Color playerTwoColor = new Color( 247, 117, 5 );
		
		double p1x = 0.50;  // x location of the demo point
		double p1y = 0.05;  // y location of the demo point
		double p2x = 0.50;  
		double p2y = 0.05;
		// This is a test: This print state won't be here if this works
		int x = 5;
		x += 6;
		System.out.println( x + ": This is a test" );
		
		//
		// This song will play in the background allowing your other work
		//   to proceed. 
		// If annoyed, comment this out
		// If you want more, change playOnce() to playAlways()
		//
		BackgroundSong sbsp = new BackgroundSong("SpongeBobSquarePants.wav");
		//sbsp.playOnce();
		int counter = 0;
		int playerOnePoints = 0;
		int playerTwoPoints = 0;
		int scoreLimit = ap.nextInt( "What should be the score limit?" );
		boolean canOneBeHit = true;
		boolean canTwoBeHit = true;
		boolean delPowerup = false;
		boolean reset = false;
		
		while (true) {
			StdDraw.clear();
			
			if( playerOnePoints >= scoreLimit || playerTwoPoints >= scoreLimit )
			{
				clickAnywhere( playerOnePoints, playerTwoPoints, scoreLimit, playerOneColor, playerTwoColor );
			}
			StdDraw.setPenRadius();
			StdDraw.picture(0.5, 0.5, "images/ocean-background.jpg", 1.2, 1.2);
			StdDraw.setPenColor();
			StdDraw.rectangle(.90, .95,  .10,  .05);
			StdDraw.rectangle( .10, .95, .10, .05);
			StdDraw.setPenColor( playerOneColor );
			StdDraw.filledRectangle( .10, .95, .10, .05);
			StdDraw.setPenColor( playerTwoColor );
			StdDraw.filledRectangle(0.90, .95, .10, .05);
			StdDraw.setPenColor();
			String playerOneText = "Player 1";
			String playerTwoText = "Player 2";
			String pointsOneText = playerOnePoints + " points";
			String pointsTwoText = playerTwoPoints + " points";
			String playUntilText = "First to " + scoreLimit + " wins!";
			StdDraw.text( .10, .9750, playerOneText );
			StdDraw.text( .10, .9250, pointsOneText);
			StdDraw.text( .90, .9750, playerTwoText );
			StdDraw.text( .90, .9250, pointsTwoText);
			StdDraw.text( .50,  .9750, playUntilText );
			
			if( reset )
			{
				delPowerup = false;
			}
			generateTreasure();
			generateObstacles();
			if( delPowerup == false || reset == true )
			{
				generatePowerup();
				if( reset )
				{
					reset = false;
					canOneBeHit = true;
					canTwoBeHit = true;
				}
			}
			

			//
			// Should we move?
			//
			if (checkFor(KeyEvent.VK_W)) {
				p1y += 0.003;
			}
			if (checkFor(KeyEvent.VK_A)) {
				p1x -= 0.003;
			}
			if (checkFor(KeyEvent.VK_S)) {
				p1y -= 0.003;
			}
			if (checkFor(KeyEvent.VK_D)) {
				p1x += 0.003;
			}
			
			if (checkFor(KeyEvent.VK_UP)) {
				p2y += 0.003;
			}
			if (checkFor(KeyEvent.VK_LEFT)) {
				p2x -= 0.003;
			}
			if (checkFor(KeyEvent.VK_DOWN)) {
				p2y -= 0.003;
			}
			if (checkFor(KeyEvent.VK_RIGHT)) {
				p2x += 0.003;
			}
			
			
			if( isPoint( p1x, p1y ) )
			{
				p1x = .6;
				p1y = 0.05;
				p2x = .4;
				p2y = 0.05;
				playerOnePoints++;
				reset = true;
			}
			if( isPoint( p2x, p2y ) )
			{
				p1x = .4;
				p1y = 0.05;
				p2x = .6;
				p2y = 0.05;
				playerTwoPoints++;
				reset = true;
			}
			if( isContact( p1x, p1y, canOneBeHit ) )
			{
				playerOnePoints--;
				StdDraw.pause( 1000 );
			}
			if( isContact( p2x, p2y, canTwoBeHit ) )
			{
				playerTwoPoints--;
				StdDraw.pause( 1000 );
			}
			if( isContactPowerup(p1x,p1y, canOneBeHit, canTwoBeHit ) )
			{
				canOneBeHit = false;
				delPowerup = true;
			}
			if( isContactPowerup( p2x,p2y, canOneBeHit, canTwoBeHit ) )
			{
				canTwoBeHit = false;
				delPowerup = true;
			}
			//
			// The pirate
			//
			if( canOneBeHit == false )
				StdDraw.setPenColor( ColorUtils.randomColor() );
			else
				StdDraw.setPenColor( playerOneColor);
			StdDraw.filledCircle(p1x, p1y, .03);
			if( canTwoBeHit == false )
				StdDraw.setPenColor( ColorUtils.randomColor() );
			else
				StdDraw.setPenColor( playerTwoColor);
			StdDraw.filledCircle(p2x, p2y, .03);		
			
			StdDraw.show();  
			StdDraw.pause(10);   // 1/100 of a second
			counter += 1;
		}

	}
	
	public static void generateTreasure()
	{
		StdDraw.setPenColor( new Color( 140, 110, 84 ) );
		StdDraw.filledRectangle(.50, .90, .10, 0.05);
		StdDraw.setPenRadius( 0.005 );
		StdDraw.setPenColor( Color.YELLOW );
		StdDraw.line(.4750, .90, .5250, .90);
		StdDraw.line(.50, .9250, .50, .8750);
	}
	
	public static boolean isPoint( double px, double py )
	{
		if( (px >= .40 && px <= .60 ) && (py >= .85 && py <= .95 ) )
			return true;
		return false;
	}
	
	public static void generateObstacles()
	{
		StdDraw.setPenColor( Color.red );
		StdDraw.filledRectangle( .85, .15, .15, .06);
		StdDraw.filledRectangle( .05, .15, .15, .06);
		StdDraw.filledRectangle( .30, .35, .15, .06);
		StdDraw.filledRectangle( .60, .35, .15, .06);
		StdDraw.filledRectangle( .02, .54, .15, .06);
		StdDraw.filledRectangle( .35, .54, .15, .06);
		StdDraw.filledRectangle( .85, .54, .15, .06);
		StdDraw.filledRectangle( .25, .70, .15, .06);
		StdDraw.filledRectangle( .65, .70, .15, .06);
	}
	
	public static boolean isContact( double px, double py, boolean canBeHit )
	{
		if( canBeHit )
		{
			if( (px >= .70 && px <= 1 ) && (py >= .09 && py <= .21 ) )
				return true;
			if( (px >= -.10 && px <= .20 ) && (py >= .09 && py <= .21 ) )
				return true;
			if( (px >= .15 && px <= .45 ) && (py >= .29 && py <= .41 ) )
				return true;
			if( (px >= .45 && px <= .75 ) && (py >= .29 && py <= .41 ) )
				return true;
			if( (px >= -.13 && px <= .17 ) && (py >= .48 && py <= .60 ) )
				return true;
			if( (px >= .20 && px <= .50 ) && (py >= .48 && py <= .60 ) )
				return true;
			if( (px >= .70 && px <= 1 ) && (py >= .48 && py <= .60 ) )
				return true;
			if( (px >= .10 && px <= .40 ) && (py >= .64 && py <= .76 ) )
				return true;
			if( (px >= .50 && px <= .80 ) && (py >= .64 && py <= .76 ) )
				return true;
			return false;
		}
		return false;
	}
	
	public static boolean generatePowerup()
	{	
		StdDraw.setPenColor( Color.YELLOW );
		double[] x = { .02, .05, .08 };
		double[] y = { .32, .35, .32 };
		StdDraw.filledPolygon(x, y);
		return false;
	}
	
	public static boolean isContactPowerup( double px, double py, boolean canOneBeHit, boolean canTwoBeHit )
	{
		if( canOneBeHit == false || canTwoBeHit == false )
		{
			
		}
		else
		{
			if( (px >= .02 && px <= .08 ) && (py >= .32 && py <= .35 ) )
				return true;
		}
		return false;
	}
	
	public static void clickAnywhere( int playerOnePoints, int playerTwoPoints, int scoreLimit, Color playerOneColor, Color playerTwoColor )
	{
		StdDraw.clear();
		String whoWon = "";
		if( playerOnePoints >= scoreLimit )
		{
			StdDraw.setPenColor( playerOneColor );
			whoWon = "Player One won the game!";
		}
		else
		{
			StdDraw.setPenColor( playerTwoColor );
			whoWon = "Player Two won the game!";
		}
		StdDraw.text( .5,  .75, whoWon);
		StdDraw.text( .5,  .25, "Click anywhere to play again");
		StdDraw.show();
		while( !StdDraw.isMousePressed() )
			StdDraw.pause( 40 );
		while( StdDraw.isMousePressed() )
			StdDraw.pause( 40 );
		String[] args = new String[0];
		main( args );
	}
	
	/**
	 * Check to see if a given key is pressed at the moment.  This method does not
	 *   wait for a key to be pressed, so if nothing is pressed, it returns
	 *   false right away.
	 *   
	 * The event constants are found at:
	 *   https://docs.oracle.com/javase/7/docs/api/java/awt/event/KeyEvent.html
	 * @param key the integer code of the key
	 * @return true if that key is down, false otherwise
	 */
	private static boolean checkFor(int key) {
		if (StdDraw.isKeyPressed(key)) {
			return true;
		}
		else {
			return false;
		}
	}

}