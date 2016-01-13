import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.net.*;
import java.applet.*;
import javax.swing.Timer;
import hsa.*;

/** The "MiniDreamland" class.
  * Purpose: Blob has to move left and right avoiding the obstacles while collecting stars and trying to survive as long as they can
  * Includes a DrawingPanel class which extends JPanel, a general purpose container,
  * Shows on a console a trace of the mouse (x,y) points.
  * @authors: Kitty Huang and Joann Lau
  * @version June 10,2014
 */

// The main program
public class MiniDreamland extends JFrame
{
    private DrawingPanel drawingArea;
    private Image background, play, playHighlight, introduction, introScreen, map, back, highScoreScreen, obstacle, obstacle2, obstacle3, mainMenu,
	highScoreIcon, quit, map1, map2, map3, blob, plant, plant2, blueFish, pinkFish, blueAlien, greenAlien, rock, asteroid, star, bubble, gameOverScreen;
    private int menuScreen = 0;
    private int mapScreen = 0;
    private int gameOver = 0;
    private int xBlob = 0; //x position of the blob
    private int yBlob = 380; // y position of the blob
    private int xMonster = (int) (Math.random () * 500) + 15; // x position of the monster
    private int yMonster = -35; //y position of the monster
    private int newGame = 0;
    private int starsCollect = 100;
    private int score;
    private int obstacleType, obstacleType2, obstacleType3;
    private Timer timer;
    private int highScore;
    private boolean highlightPlay;
    static final Font SCORE_FONT = new Font ("Arial", Font.PLAIN, 10);
    static final Font HIGHSCORE_FONT = new Font ("Century Gothic", Font.PLAIN, 60);
    static final Font CURRENT_SCORE_FONT = new Font ("Century Gothic", Font.PLAIN, 30);
    static final Font NAME_FONT = new Font ("Century Gothic", Font.PLAIN, 12);
    private boolean song = true;
    private AudioClip backgroundSong;

    /** Sets up the fondation of the game and imports all the image and audio files needed
      */
    public MiniDreamland ()
    {
	super ("Mini Dreamland"); //name of window

	setResizable (false); //not allowing the screen to be enlarged

	background = new ImageIcon ("back.jpg").getImage (); //background
	play = new ImageIcon ("play.png").getImage (); //play button
	playHighlight = new ImageIcon ("playHighlight.png").getImage (); //highlighted play button
	introduction = new ImageIcon ("Introduction.png").getImage (); //Introduction button
	introScreen = new ImageIcon ("Introduction.jpg").getImage (); //Introduction page
	map = new ImageIcon ("map.jpg").getImage (); //map page
	back = new ImageIcon ("back.png").getImage (); //back button
	mainMenu = new ImageIcon ("mainmenu.png").getImage (); //main menu button
	highScoreScreen = new ImageIcon ("HighScore.jpg").getImage (); //highscore page
	highScoreIcon = new ImageIcon ("highScore.png").getImage (); //highscore button
	quit = new ImageIcon ("quit.png").getImage (); //quit button
	map1 = new ImageIcon ("map1.jpg").getImage (); //land level
	map2 = new ImageIcon ("map2.jpg").getImage (); //sea level
	map3 = new ImageIcon ("map3.jpg").getImage (); //space level
	blob = new ImageIcon ("blob.png").getImage (); //character of game
	plant = new ImageIcon ("plant.png").getImage (); //plant monster
	plant2 = new ImageIcon ("plant2.png").getImage (); //plant monster
	blueFish = new ImageIcon ("bluefish.png").getImage (); //blue fish monster
	pinkFish = new ImageIcon ("pinkfish.png").getImage (); //pink fish monster
	blueAlien = new ImageIcon ("bluealien.png").getImage (); //blue alien monster
	greenAlien = new ImageIcon ("greenalien.png").getImage (); //green alien monster
	rock = new ImageIcon ("rock.png").getImage (); //rock obstacle
	asteroid = new ImageIcon ("asteroid.png").getImage (); //ateroid obstacle
	star = new ImageIcon ("star.png").getImage (); //star obstacle
	bubble = new ImageIcon ("bubble.png").getImage (); //bubble obstacle
	gameOverScreen = new ImageIcon ("gameOver.jpg").getImage (); //game over screen
	//creating a frame
	Dimension imageSize = new Dimension (700, 550); //determining the size of the screen
	drawingArea = new DrawingPanel (imageSize);
	Container contentPane = getContentPane ();
	contentPane.add (drawingArea, BorderLayout.CENTER);

	backgroundSong = Applet.newAudioClip (getCompleteURL ("happy.au")); //getting the song file
	backgroundSong.loop (); //playing the song file in a loop
    }


    /** retrieves the url for the song in order to play it
    *@parem fileName   the name of the audio file
    *return            returns the URL of the audio file
    */
    public URL getCompleteURL (String fileName)
    {
	try
	{
	    return new URL ("file:" + System.getProperty ("user.dir") + "/" + fileName);
	}
	catch (MalformedURLException e)
	{
	    System.err.println (e.getMessage ());
	}
	return null;
    }


    // Inner class for the drawing area
    private class DrawingPanel extends JPanel
    {
	private boolean timerOn;

	/** adds all the listener needed and sets size of the drawing panel
	  * @param size  the size of the board
	  */
	public DrawingPanel (Dimension size)
	{
	    setPreferredSize (size); //set size of this panel
	    timerOn = false;
	    //sets timer
	    timer = new Timer (100, new TimerEventHandler ());
	    // Add mouse listeners  to the drawing panel
	    this.addMouseMotionListener (new MouseMotionHandler ());
	    this.addMouseListener (new MouseHandler ());

	    setFocusable (true);
	    addKeyListener (new KeyHandler ());
	    requestFocusInWindow ();
	}

	/** Repaint the board's drawing panel
	*@param g The Graphics context
	*/
	public void paintComponent (Graphics g)
	{
	    type ();
	    //main menu
	    super.paintComponent (g);
	    g.drawImage (background, 0, 0, this);
	    g.drawImage (introduction, 300, 370, this);
	    g.drawImage (highScoreIcon, 75, 450, this);
	    g.drawImage (quit, 300, 460, this);
	    g.setFont (NAME_FONT);
	    g.drawString ("Creators: Kitty and Joann", 450, 230);

	    //play button
	    if (highlightPlay)
		g.drawImage (playHighlight, 80, 350, 80, 60, this);
	    else
		g.drawImage (play, 80, 350, 80, 60, this);
	    //map screen
	    if (menuScreen == 1)
	    {
		g.drawImage (map, 0, 0, this);
		g.drawImage (back, 75, 500, 60, 40, this);
	    }
	    //introduction screen
	    else if (menuScreen == 2)
	    {
		g.drawImage (introScreen, 0, 0, this);
		g.drawImage (back, 75, 500, 60, 40, this);
	    }
	    //highscore screen
	    else if (menuScreen == 3)
	    {
		highScore ();
		g.drawImage (highScoreScreen, 0, 0, this);
		g.drawImage (back, 75, 500, 60, 40, this);
		//displays the high score
		g.setColor (Color.WHITE);
		g.setFont (HIGHSCORE_FONT);
		g.drawString (String.valueOf (highScore), 323, 250);
	    }
	    //elements in the land map
	    if (mapScreen == 1)
	    {
		g.drawImage (map1, 0, 0, this);
		g.drawImage (blob, xBlob, yBlob, this);
		g.drawImage (obstacle, xMonster, yMonster, this);
		g.drawImage (mainMenu, 580, 0, this);
		moveObstacles ();
		//after one obstacle leaves the screen another random obstacle appears at a randomized location
		if (yMonster >= 550)
		{
		    xMonster = (int) (Math.random () * 660) + 15;
		    yMonster = -25;
		    obstacleType = (int) (Math.random () * 4) + 1;
		}
		//if a star appears and is touched by the user
		else if (obstacleType == 4 && yBlob == (yMonster + 30) && xMonster >= xBlob && xMonster <= (xBlob + 80))
		{
		    //makes the star diappear and every star collected adds 100 points to the total score
		    if (gameOver != 1)
		    {
			xMonster = 715;
			score += starsCollect;
			//repaint ();
		    }
		    //if game is over it doesn't count the stars being touched by the blob
		    else
		    {
		    }
		}
		//if the blob touches an obstacle besides the star then user loses and game is over
		else if ((obstacleType == 1 || obstacleType == 2 || obstacleType == 3) && yBlob == (yMonster + 80) && xMonster >= (xBlob - 50) && xMonster <= (xBlob + 80))
		{
		    gameOver = 1;
		}

	    }
	    //elements in the sea map
	    else if (mapScreen == 2)
	    {
		g.drawImage (map2, 0, 0, this);
		g.drawImage (blob, xBlob, yBlob, this);
		g.drawImage (obstacle, xMonster, yMonster, this);
		moveObstacles ();
		g.drawImage (mainMenu, 580, 0, this);
		//after one obstacle leaves the screen another random obstacle appears at a randomized location
		if (yMonster >= 550)
		{
		    xMonster = (int) (Math.random () * 660) + 15;
		    yMonster = -25;
		    obstacleType = (int) (Math.random () * 4) + 1;
		}
		//if a star appears and is touched by the user
		else if (obstacleType == 4 && yBlob == (yMonster + 30) && xMonster >= (xBlob - 70) && xMonster <= (xBlob + 80))
		{
		    //makes the star diappear and every star collected adds 100 points to the total score
		    if (gameOver != 1)
		    {
			xMonster = 715;
			score += starsCollect;
		    }
		    //if game is over it doesn't count the stars being touched by the blob
		    else
		    {
		    }
		}
		//if the blob touches an obstacle besides the star then user loses and game is over
		else if ((obstacleType == 1 || obstacleType == 2 || obstacleType == 3) && yBlob == (yMonster + 80) && xMonster >= (xBlob - 40) && xMonster <= (xBlob + 80))
		{
		    gameOver = 1;
		}

	    }
	    //elements in the space map
	    else if (mapScreen == 3)
	    {
		g.drawImage (map3, 0, 0, this);
		g.drawImage (blob, xBlob, yBlob, this);
		g.drawImage (obstacle, xMonster, yMonster, this);
		g.drawImage (mainMenu, 580, 0, this);
		moveObstacles ();
		//after one obstacle leaves the screen another random obstacle appears at a randomized location
		if (yMonster >= 550)
		{
		    xMonster = (int) (Math.random () * 660) + 15;
		    yMonster = -25;
		    obstacleType = (int) (Math.random () * 4) + 1;
		}
		//if a star appears and is touched by the user
		else if (obstacleType == 4 && yBlob == (yMonster + 30) && xMonster >= xBlob && xMonster <= (xBlob + 80))
		{
		    //makes the star diappear and every star collected adds 100 points to the total score
		    if (gameOver != 1)
		    {
			xMonster = 715;
			score += starsCollect;
		    }
		    //if game is over it doesn't count the stars being touched by the blob
		    else
		    {
		    }
		}
		//if the blob touches an obstacle besides the star then user loses and game is over
		else if ((obstacleType == 1 || obstacleType == 2 || obstacleType == 3) && yBlob == (yMonster + 80) && xMonster >= (xBlob - 50) && xMonster <= (xBlob + 80))
		{
		    gameOver = 1;
		}

	    }
	    //if user loses then it calls the game over screen
	    if (gameOver == 1)
	    {
		highScore ();
		g.drawImage (gameOverScreen, 0, 0, this);
		g.setFont (CURRENT_SCORE_FONT);
		g.drawString ("Score: " + score, 300, 400);
	    }
	    //if user clicks on play then the score is displayed in the corner while playing
	    else if (newGame == 1)
	    {
		g.setFont (SCORE_FONT);
		g.setColor (Color.WHITE);
		g.drawString ("Score: " + (score), 20, 20);
	    }
	}

	private class TimerEventHandler implements ActionListener
	{
	    /** Responds to the timer
	    *@param event the event that triggered this method
	    */
	    public void actionPerformed (ActionEvent event)
	    {
		//if user loses then the time stops counting
		if (gameOver == 1)
		{
		    timer.stop ();
		    //repaint ();
		}
		//keeps counting the score if user is still playing
		else
		{
		    score++;
		    //repaint ();
		}
		repaint ();
	    }
	}
    }


    /** starts the timer and the initial variables needed to start the game
    */
    public void newGame ()
    {
	timer.start ();
	newGame = 1;
	moveObstacles ();
	obstacleType = (int) (Math.random () * 4) + 1;

    }

    /**resets all values to 0 after completing a game
      */
    public void reset ()
    {
	menuScreen = 0;
	mapScreen = 0;
	gameOver = 0;
	newGame = 0;
	score = 0;
    }

    /**calls a text file to compare the scores of previous highscore and current score
    */
    public void highScore ()
    {
	TextInputFile inFile = new TextInputFile ("HighScore.txt");
	highScore = inFile.readInt ();
	inFile.close ();
	//if score if higher than the highscore then the highscore will be replaced by the score
	if (score > highScore)
	{
	    TextOutputFile outFile = new TextOutputFile ("HighScore.txt");
	    highScore = score;
	    outFile.println (score);
	    outFile.close ();
	}
    }


    /* identifying the type of monster for each map
    */
    public void type ()
    {
	//object obstacles in each map
	if (obstacleType == 1)
	{
	    if (mapScreen == 1)
		obstacle = rock;
	    else if (mapScreen == 2)
		obstacle = bubble;
	    else if (mapScreen == 3)
		obstacle = asteroid;
	}
	//1st type of monster in each map
	else if (obstacleType == 2)
	{
	    if (mapScreen == 1)
		obstacle = plant;
	    else if (mapScreen == 2)
		obstacle = blueFish;
	    else if (mapScreen == 3)
		obstacle = blueAlien;
	}
	//2nd type of monster in each map
	else if (obstacleType == 3)
	{
	    if (mapScreen == 1)
		obstacle = plant2;
	    else if (mapScreen == 2)
		obstacle = pinkFish;
	    else if (mapScreen == 3)
		obstacle = greenAlien;
	}
	//stars in all the maps
	else if (obstacleType == 4)
	    obstacle = star;
    }


    /**monsters move automatically until off the screen
    */
    public void moveObstacles ()
    {
	//depending on which map it is on, the delay speed is different
	// to make the level more challenging at the end
	if (mapScreen == 1)
	{
	    yMonster += 25;
	    //delay
	    try
	    {
		Thread.sleep (50);
	    }

	    catch (InterruptedException e)
	    {

	    }
	    repaint ();
	}
	//smaller delay
	else if (mapScreen == 2)
	{
	    yMonster += 25;
	    //delay
	    try
	    {
		Thread.sleep (40);
	    }

	    catch (InterruptedException e)
	    {

	    }
	    repaint ();
	}
	//even smaller delay
	else if (mapScreen == 3)
	{
	    yMonster += 25;
	    //delay
	    try
	    {
		Thread.sleep (32);
	    }

	    catch (InterruptedException e)
	    {

	    }
	    repaint ();
	}
    }


    private class MouseHandler extends MouseAdapter
    {
	/** responds to where the mouse clicks and acts based on that
	* @ param event    the event that triggers the method
	*/
	public void mousePressed (MouseEvent event)
	{
	    Point pressed = event.getPoint ();
	    //Responds if the user clicks the play button
	    //if the mainmenu button is clicked on the game over screen, it resets the score and 
	    //starsCollected to 100 as well as other variables within the reset method
	    if (gameOver == 1 && pressed.x >= 220 && pressed.x < 470 && pressed.y <= 330 && pressed.y < 420)
	    {
		reset ();
		score = 0;
		starsCollect = 100;
		repaint ();
	    }
	    //Responds if the user cicks the play button
	    if (pressed.x >= 80 && pressed.x < 160 && pressed.y >= 350 && pressed.y < 410 && menuScreen == 0)
	    {
		//calls the map screen
		menuScreen = 1;
		repaint ();
	    }
	    // Responds if the user cicks the introduction button
	    else if (pressed.x >= 300 && pressed.x < 450 && pressed.y >= 370 && pressed.y < 405 && menuScreen == 0)
	    {
		// calls the introduction screen
		menuScreen = 2;
		repaint ();
		setCursor (Cursor.getDefaultCursor ()); // change mouse cursor from a hand to a regular pointer
	    }
	    //Responds if the user clicks the highscore button
	    else if (pressed.x >= 80 && pressed.x < 250 && pressed.y >= 450 && pressed.y < 490 && menuScreen == 0)
	    {
		//calls the highscore screen
		menuScreen = 3;
		repaint ();
	    }
	    //Responds if the quit button is selected
	    else if (pressed.x >= 300 && pressed.x < 400 && pressed.y >= 450 && pressed.y < 490 && menuScreen == 0)
	    {
		//closes the window
		hide ();
		System.exit (0);
	    }
	    //When the screen is not on the main menu screen and back button is pressed
	    if (pressed.x >= 75 && pressed.x < 135 && pressed.y >= 500 && pressed.y < 540 && (menuScreen == 1 || menuScreen == 2 || menuScreen == 3))
	    {
		//calls the main menu screen
		menuScreen = 0;
		repaint ();
	    }
	    //Responds when the first map is selected
	    if (pressed.x >= 100 && pressed.x < 230 && pressed.y >= 50 && pressed.y < 200 && menuScreen == 1)
	    {
		//calls the first map screen and resets the main menu screen
		mapScreen = 1;
		menuScreen = 0;
		newGame ();
		repaint ();
	    }
	    //Responds when the second map is selected
	    else if (pressed.x >= 300 && pressed.x < 450 && pressed.y >= 250 && pressed.y < 360 && menuScreen == 1)
	    {
		//calls the second map screen and resets the main menu screen
		mapScreen = 2;
		menuScreen = 0;
		newGame ();
		repaint ();
	    }
	    //Responds whent the third map is selected
	    else if (pressed.x >= 530 && pressed.x < 680 && pressed.y >= 350 && pressed.y < 500 && menuScreen == 1)
	    {
		//calls the third map screen and resets the main menu screen
		mapScreen = 3;
		menuScreen = 0;
		newGame ();
		repaint ();
	    }
	    //responds if the main menu screen is selected when the game is being played
	    else if (pressed.x >= 590 && pressed.x < 710 && pressed.y >= 0 && pressed.y < 20 && (mapScreen == 1 || mapScreen == 2 || mapScreen == 3))
	    {
		//calls the main menu screen
		timer.stop ();
		reset ();
		repaint ();
	    }
	}
    }


    private class MouseMotionHandler extends MouseMotionAdapter
    {
	/**changing the cursor type or highlight things depending on the mouse position
	* @param event    the event that triggers the method
	*/
	public void mouseMoved (MouseEvent event)
	{
	    //tracks the position of mouse
	    Point pos = event.getPoint ();

	    if (menuScreen == 0)
	    {
		//if the mouse hovers over play the mouse will change to a hand
		if (pos.x >= 80 && pos.x < 160 && pos.y >= 350 && pos.y < 410)
		{
		    highlightPlay = true;
		    setCursor (Cursor.getPredefinedCursor (Cursor.HAND_CURSOR)); // change mouse cursor to a hand
		}
		//if mouse hovers over introduction button
		else if (pos.x >= 300 && pos.x < 450 && pos.y >= 370 && pos.y < 405)
		{
		    setCursor (Cursor.getPredefinedCursor (Cursor.HAND_CURSOR)); // change mouse cursor to a hand
		}
		//if mouse hovers over high score
		else if (pos.x >= 80 && pos.x < 250 && pos.y >= 450 && pos.y < 490)
		{
		    setCursor (Cursor.getPredefinedCursor (Cursor.HAND_CURSOR)); // change mouse cursor to a hand
		}
		//if mouse hovers over quit
		else if (pos.x >= 300 && pos.x < 400 && pos.y >= 450 && pos.y < 490)
		{
		    setCursor (Cursor.getPredefinedCursor (Cursor.HAND_CURSOR)); // change mouse cursor to a hand
		}
		//if mouse isn't hovering over any buttons
		else
		{
		    highlightPlay = false;
		    setCursor (Cursor.getDefaultCursor ());  // change mouse cursor to regualr pointer
		}
	    }
	    else if (menuScreen == 1)
	    {
		//if the mouse hovers map 1
		if (pos.x >= 100 && pos.x < 230 && pos.y >= 50 && pos.y < 200)
		    setCursor (Cursor.getPredefinedCursor (Cursor.HAND_CURSOR));
		//if the mouse hovers map 2
		else if (pos.x >= 300 && pos.x < 450 && pos.y >= 250 && pos.y < 360)
		    setCursor (Cursor.getPredefinedCursor (Cursor.HAND_CURSOR));
		//if the mouse hovers map 3
		else if (pos.x >= 530 && pos.x < 680 && pos.y >= 350 && pos.y < 500)
		    setCursor (Cursor.getPredefinedCursor (Cursor.HAND_CURSOR));
		//if mouse isn't hovering over any maps
		else
		    setCursor (Cursor.getDefaultCursor ());
	    }
	    //if mouse is hovbering over mainmenu button when game is being played
	    else if (menuScreen == 1 || menuScreen == 2 || menuScreen == 3)
	    {
		if (pos.x >= 75 && pos.x < 135 && pos.y >= 500 && pos.y < 540)
		    setCursor (Cursor.getPredefinedCursor (Cursor.HAND_CURSOR));
	    }
	    //if mouse is hovering over the main menu button in the game over screen
	    else if (gameOver == 1 && pos.x >= 300 && pos.x < 400 && pos.y <= 250 && pos.y < 300)
	    {
		setCursor (Cursor.getPredefinedCursor (Cursor.HAND_CURSOR));
	    }
	    //otherwise the mouse doesn't change
	    else
	    {
		setCursor (Cursor.getDefaultCursor ());  // change mouse cursor to its normal image
	    }
	    repaint ();
	}
    }


    private class KeyHandler extends KeyAdapter
    {
	/** responds the the keys
	* @param event   the event that triggers the method
	*/
	public void keyPressed (KeyEvent event)
	{
	    //when the user presses the left arrow key
	    if (event.getKeyCode () == KeyEvent.VK_LEFT)
	    {
		//if the blob is at the edge of the window, it cannot leave the screen
		if (xBlob <= 0)
		{
		    xBlob = 0;
		    repaint ();
		}
		//blob moves left
		else
		{
		    xBlob = xBlob - 25;
		    repaint ();
		}
	    }
	    //when the user presses the right arrow key
	    else if (event.getKeyCode () == KeyEvent.VK_RIGHT)
	    {
		//if the blob is at the edge of the window, it cannot leave the screen
		if (xBlob >= 600)
		{
		    xBlob = 600;
		    repaint ();
		}
		//blob moves right
		else
		{
		    xBlob = xBlob + 25;
		    repaint ();
		}
	    }
	}
    }


    //main program
    public static void main (String[] args) throws Exception
    {
	//running the game
	MiniDreamland frame = new MiniDreamland ();
	frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
	frame.pack ();
	frame.setVisible (true);

    } // main method
} // MiniDreamland class



