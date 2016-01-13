import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

/**
 * Keeps track of the panel where the game is played and includes behaviours
 * like showPossibleMoves, mousePressed etc
 * @author Kitty Huang and Kitty Su
 * @version June 2015
 */
public class ChineseCheckersBoard extends JPanel implements MouseListener,
		MouseMotionListener
{

	// Program constants
	private final int HIDDEN = -1;
	private final int EMPTY = 0;
	private final int PLAYER_ONE = 1;
	private final int PLAYER_TWO = 2;
	private final int POSSIBLE_MOVE = 3;
	private final int POSSIBLE_JUMP = 4;
	private final int CURRENT_MOVE = 5;
	private final int JUMP_DELAY = 700;

	static final int WIDTH = 1000;
	static final int HEIGHT = 700;
	static final int OK = 0;
	static final int CANCEL = 2;
	static final int CLOSE = -1;

	private static int[][] even = { { 0, -1 }, // Left
			{ 0, 1 }, // Right
			{ -1, 0 }, // Upper left
			{ 1, 0 }, // Lower left
			{ -1, 1 }, // Upper right
			{ 1, 1 } }; // Lower right

	private static int[][] odd = { { 0, -1 }, // Left
			{ 0, 1 }, // Right
			{ -1, -1 }, // Upper left
			{ 1, -1 }, // Lower left
			{ -1, 0 }, // Upper right
			{ 1, 0 } }; // Lower right

	private int gameState, typeOfMove, oldRow, oldColumn;

	int[][] board;

	private Image mainBackground, gameBackground, instructionsImage,
			instructionsImage2, instructionsImage3, instructionsImage4,
			instructionsImage5, instructionsImage6, playButton, emptyYarn,
			instructionsButton, blueYarn, redYarn, greenYarn, newGameButton,
			instructionsGameButton, endTurnButton, backButton, nextButton,
			playAgainButton,
			redDot, blueDot, yellowDot, gameBackgroundAI, endBackgroundAI,
			endBackgroundP1, endBackgroundP2;

	private AudioClip backgroundMusic;

	private int currentX, currentY, noOfMoves, winner,
			noOfPlayers, currentPlayer; // 1 for player2 for computer

	private boolean gameOver, instructionsOver, playOver, newGameOver,
			backOver, instructionsGameOver, nextOver, playAgain, mainMenu,
			gamePlay, instructions1, instructions2, instructions3,
			instructions4, instructions5, instructions6;

	private ArrayList<Move> computerPossibleMoves;

	/**
	 * Constructs a board  to play the game 
	 */
	public ChineseCheckersBoard()
	{
		computerPossibleMoves = new ArrayList<Move>();

		board = new int[][] { { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
				{ -1, -1, -1, -1, -1, 1, -1, -1, -1, -1, -1 },
				{ -1, -1, -1, -1, 1, 1, -1, -1, -1, -1, -1 },
				{ -1, -1, -1, -1, 1, 1, 1, -1, -1, -1, -1 },
				{ -1, -1, -1, 1, 1, 1, 1, -1, -1, -1, -1 },
				{ -1, -1, -1, 0, 0, 0, 0, 0, -1, -1, -1 },
				{ -1, -1, 0, 0, 0, 0, 0, 0, -1, -1, -1 },
				{ -1, -1, 0, 0, 0, 0, 0, 0, 0, -1, -1 },
				{ -1, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1 },
				{ -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1 },
				{ -1, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1 },
				{ -1, -1, 0, 0, 0, 0, 0, 0, 0, -1, -1 },
				{ -1, -1, 0, 0, 0, 0, 0, 0, -1, -1, -1 },
				{ -1, -1, -1, 0, 0, 0, 0, 0, -1, -1, -1 },
				{ -1, -1, -1, 2, 2, 2, 2, -1, -1, -1, -1 },
				{ -1, -1, -1, -1, 2, 2, 2, -1, -1, -1, -1 },
				{ -1, -1, -1, -1, 2, 2, -1, -1, -1, -1, -1 },
				{ -1, -1, -1, -1, -1, 2, -1, -1, -1, -1, -1 },
				{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 } };

		//setting all the variables to the value they need to be
		mainMenu = true;
		gamePlay = false;
		instructions1 = false;
		instructions2 = false;
		instructions3 = false;
		instructions4 = false;
		instructions5 = false;
		instructions6 = false;
		gameOver = false;
		winner = 0;
		currentPlayer = 1;

		loadResources();
		backgroundMusic.loop();

		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFont(new Font("Arial", Font.PLAIN, 18));

		this.addMouseListener(this);
		this.addMouseMotionListener(this);

	}

	/**
	 * Loads up the images and sound files
	 */
	private void loadResources()
	{
		mainBackground = new ImageIcon("mainBackground.png").getImage();
		gameBackground = new ImageIcon("gameBackground.png").getImage();
		gameBackgroundAI = new ImageIcon("gameBackgroundAI.png").getImage();
		instructionsImage = new ImageIcon("instructionsImage.png").getImage();
		instructionsImage2 = new ImageIcon("instructionsImage2.png").getImage();
		instructionsImage3 = new ImageIcon("instructionsImage3.jpg").getImage();
		instructionsImage4 = new ImageIcon("instructionsImage4.jpg").getImage();
		instructionsImage5 = new ImageIcon("instructionsImage5.jpg").getImage();
		instructionsImage6 = new ImageIcon("instructionsImage6.jpg").getImage();

		playButton = new ImageIcon("playButton.png").getImage();
		instructionsButton = new ImageIcon("instructionsButton.png").getImage();
		newGameButton = new ImageIcon("newGameButton.png").getImage();
		instructionsGameButton = new ImageIcon("instructionsButtonGame.png")
				.getImage();
		nextButton = new ImageIcon("nextButton.png").getImage();
		backButton = new ImageIcon("backButton.png").getImage();
		endTurnButton = new ImageIcon("endTurnButton.png").getImage();
		playAgainButton = new ImageIcon("playAgainButton.png").getImage();

		emptyYarn = new ImageIcon("emptyYarn.png").getImage();
		blueYarn = new ImageIcon("blueYarn.png").getImage();
		redYarn = new ImageIcon("redYarn.png").getImage();
		greenYarn = new ImageIcon("GreenYarn.png").getImage();

		blueDot = new ImageIcon("blueDot.png").getImage();
		redDot = new ImageIcon("redDot.png").getImage();
		yellowDot = new ImageIcon("yellowDot.png").getImage();

		endBackgroundAI = new ImageIcon("endBackgroundAI.png").getImage();
		endBackgroundP1 = new ImageIcon("endBackgroundP1.png").getImage();
		endBackgroundP2 = new ImageIcon("endBackgroundP2.png").getImage();

		// Load audio files - files from Youtube's Free Music library and
		// freesound.org
		backgroundMusic = Applet
				.newAudioClip(getCompleteURL("backgroundMusic.wav"));
	}

	/**
	 * Creates a new game
	 */
	public void newGame()
	{
		noOfPlayers = 0;
		
		//if the player chooses the number of players 
		if (noOfPlayers())
		{
			computerPossibleMoves.clear();
			mainMenu = false;
			instructions1 = false;
			instructions2 = false;
			instructions3 = false;
			instructions4 = false;
			instructions5 = false;
			instructions6 = false;
			winner = 0;
			currentPlayer = 1;
			gamePlay = true;
			playAgain = false;
			gameOver = false;
			noOfMoves = 0;
			clearBoard();
		}
	}

	/**
	 * To see if the player chooses the number of players and reads in what they chose
	 * @return true if the player chooses a number and false if they did not
	 */
	public boolean noOfPlayers()
	{
		// Create a panel with radio buttons
		JPanel panel = new JPanel();
		Border lowerEtched = BorderFactory
				.createEtchedBorder(EtchedBorder.RAISED);

		panel.setBorder(BorderFactory.createTitledBorder(lowerEtched,
				"Choose Number of Player(s)"));
		panel.setLayout(new GridLayout(1, 2));

		// Create a group of radio buttons to add to the Panel
		ButtonGroup playerGroup = new ButtonGroup();
		JRadioButton[] buttonList = new JRadioButton[2];

		// Create and add each radio button to the panel
		buttonList[0] = new JRadioButton("1 Player", true);
		buttonList[1] = new JRadioButton("2 Players");
		playerGroup.add(buttonList[0]);
		playerGroup.add(buttonList[1]);
		panel.add(buttonList[0]);
		panel.add(buttonList[1]);

		// Show a dialog with the panel attached
		int choice = JOptionPane.showConfirmDialog(this, panel,
				"Player Options", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.DEFAULT_OPTION);

		// Update grade if OK is selected
		if (choice == JOptionPane.OK_OPTION)
		{
			for (int index = 0; index < buttonList.length; index++)
				if (buttonList[index].isSelected())
					noOfPlayers = index + 1;
		}

		if (choice == CANCEL || choice == CLOSE)
			return false;
		else
			return true;
	}

	/**
	 * Resets the board
	 */
	public void clearBoard()
	{

		board = new int[][] { { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
				{ -1, -1, -1, -1, -1, 1, -1, -1, -1, -1, -1 },
				{ -1, -1, -1, -1, 1, 1, -1, -1, -1, -1, -1 },
				{ -1, -1, -1, -1, 1, 1, 1, -1, -1, -1, -1 },
				{ -1, -1, -1, 1, 1, 1, 1, -1, -1, -1, -1 },
				{ -1, -1, -1, 0, 0, 0, 0, 0, -1, -1, -1 },
				{ -1, -1, 0, 0, 0, 0, 0, 0, -1, -1, -1 },
				{ -1, -1, 0, 0, 0, 0, 0, 0, 0, -1, -1 },
				{ -1, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1 },
				{ -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1 },
				{ -1, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1 },
				{ -1, -1, 0, 0, 0, 0, 0, 0, 0, -1, -1 },
				{ -1, -1, 0, 0, 0, 0, 0, 0, -1, -1, -1 },
				{ -1, -1, -1, 0, 0, 0, 0, 0, -1, -1, -1 },
				{ -1, -1, -1, 2, 2, 2, 2, -1, -1, -1, -1 },
				{ -1, -1, -1, -1, 2, 2, 2, -1, -1, -1, -1 },
				{ -1, -1, -1, -1, 2, 2, -1, -1, -1, -1, -1 },
				{ -1, -1, -1, -1, -1, 2, -1, -1, -1, -1, -1 },
				{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 } };
	}

	/**
	 * Checks if a player has placed all their yarn on the other corner of the
	 * board
	 * @return true, if the game is over and the current player wins false, if
	 *         the game is not over
	 */
	public boolean isGameOver()
	{
		// Player 2 is on the other side
		// in Player 1's original location
		if (board[1][5] == PLAYER_TWO && board[2][4] == PLAYER_TWO
				&& board[2][5] == PLAYER_TWO && board[3][4] == PLAYER_TWO
				&& board[3][5] == PLAYER_TWO && board[3][6] == PLAYER_TWO
				&& board[4][3] == PLAYER_TWO && board[4][4] == PLAYER_TWO
				&& board[4][5] == PLAYER_TWO && board[4][6] == PLAYER_TWO)
		{
			winner = 2;
			gameOver = true;
			return true;
		}
		// Player 1 is on the other side
		// in Player 2's original location
		else if (board[17][5] == PLAYER_ONE && board[16][4] == PLAYER_ONE
				&& board[16][5] == PLAYER_ONE && board[15][4] == PLAYER_ONE
				&& board[15][5] == PLAYER_ONE && board[15][6] == PLAYER_ONE
				&& board[14][3] == PLAYER_ONE && board[14][4] == PLAYER_ONE
				&& board[14][5] == PLAYER_ONE && board[14][6] == PLAYER_ONE)
		{
			winner = 1;
			gameOver = true;
			return true;
		}
		else
			return false;
	}

	/**
	 * Makes a move by changing the position of a yarn
	 * @param originalRow the initial row the yarn was in
	 * @param originalColumn the initial column the yarn was in
	 * @param row the new row to move to
	 * @param column the new column to move to
	 */
	public void makeMove(int originalRow, int originalColumn, int row,
			int column)
	{
		//Makes the move if it is the first one in a turn 
		if (noOfMoves == 0)
		{
			board[row][column] = currentPlayer;
			board[originalRow][originalColumn] = EMPTY;
		}
		//Makes the move if the current yarn piece is the yarn that they were moving in the turn
		else if (originalRow == oldRow && originalColumn == oldColumn)
		{
			board[row][column] = currentPlayer;
			board[originalRow][originalColumn] = EMPTY;
		}

		//if the yarn was jumping, update the position where it landed so the player can choose that specific yarn piece to continue making moves in that turn 
		if (typeOfMove == 2)
		{
			oldRow = row;
			oldColumn = column;
		}
		
		noOfMoves++;
		
		repaint();

		//if the player only moved the yarn to an adjacent spot, end turn 
		if (typeOfMove == 1 && noOfMoves == 1)
		{
			clearPossibleMoves();
			endMove();
		}

	}

	/**
	 * ends the current turn and changes the required variables for the next player
	 */
	public void endMove()
	{
		currentPlayer = 3 - currentPlayer;
		oldRow = 0;
		oldColumn = 0;
		typeOfMove = 0;
		noOfMoves = 0;
		repaint();
		if (currentPlayer == 2 && noOfPlayers == 1)
		{
			computerPlayer();
		}
	}

	/**
	 * Checks for the possible jumps when it is in two player mode
	 * @param row the row that the selected yarn is in
	 * @param column the column that the selected yarn is in
	 * @param side the direction to check in
	 */
	public void checkForJump(int row, int column, int side)
	{

		if ((row != oldRow || column != oldColumn) && noOfMoves > 0)
		{
			return;
		}

		int noOfEmpty = 0;
		int newRow = row;
		int newColumn = column;

		// Since the longest length of the board in any legal moving direction,
		// is 9, the longest distance until the yarn to jump over, is 5
		for (int jump = 0; jump < 6; jump++)
		{
			// Odd
			if (newRow % 2 != 0)
			{
				newRow = newRow + odd[side][0];
				newColumn = newColumn + odd[side][1];
			}
			// Even
			else
			{
				newRow = newRow + even[side][0];
				newColumn = newColumn + even[side][1];
			}

			// Count the number of consecutive empties there are
			if (board[newRow][newColumn] == EMPTY
					|| board[newRow][newColumn] == POSSIBLE_MOVE)
				noOfEmpty++;

			// There is a piece there
			else if (board[newRow][newColumn] == PLAYER_ONE
					|| board[newRow][newColumn] == PLAYER_TWO)
				jump = 6;

			// Hidden piece, out of the visible array bounds
			else
				return;

		}

		int noOfEmpty2 = 0;

		//checking if there's the same empty spaces after the middle yarn piece
		for (int empty = 0; empty <= noOfEmpty; empty++)
		{
			// Odd
			if (newRow % 2 != 0)
			{
				newRow = newRow + odd[side][0];
				newColumn = newColumn + odd[side][1];
			}
			// Even
			else
			{
				newRow = newRow + even[side][0];
				newColumn = newColumn + even[side][1];
			}
			if ((noOfEmpty == 0||noOfEmpty == noOfEmpty2) && board[newRow][newColumn] == EMPTY)
			{
				board[row][column] = CURRENT_MOVE;
				board[newRow][newColumn] = POSSIBLE_JUMP;
			}

			if (board[newRow][newColumn] == EMPTY)
				noOfEmpty2++;
			else
				return;
		}
	}

	/**
	 * Shows the possible moves for a particular yarn on the board
	 * @param row the row of the yarn
	 * @param column the column of the yarn
	 */
	public void showPossibleMoves(int row, int column)
	{
		int newRow;
		int newColumn;
		Move originalMove = new Move(row, column, null);

		if (noOfMoves > 0)
		{
			if (row != oldRow || column != oldColumn)
			{
				return;
			}
		}

		for (int side = 0; side < 6; side++)
		{

			// Odd
			if (row % 2 != 0)
			{
				newRow = row + odd[side][0];
				newColumn = column + odd[side][1];
			}
			// Even
			else
			{
				newRow = row + even[side][0];
				newColumn = column + even[side][1];
			}

			if (board[newRow][newColumn] == EMPTY)
			{
				if (noOfPlayers == 1 && currentPlayer == PLAYER_TWO)
				{
					computerPossibleMoves.add(new Move(newRow, newColumn,
							originalMove));
				}
				else if (noOfMoves == 0)
				{
					board[row][column] = CURRENT_MOVE;
					board[newRow][newColumn] = POSSIBLE_MOVE;
				}
			}

			if (board[newRow][newColumn] != HIDDEN)
				checkForJump(row, column, side);
		}

	}

	 /**
     * Draws the various pages of the game including background, buttons,
     * pieces, board
     * @param g the Graphics object used to draw the images
     */
    public void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Draw the main menu if the user is not in game
            if (mainMenu) {
                    g.drawImage(mainBackground, 0, 0, this);

                    // Instructions button
                    if (instructionsOver)
                            g.drawImage(instructionsButton, 12, 628, this);
                    // Play game button
                    else if (playOver)
                            g.drawImage(playButton, 796, 628, this);

            }
            // Instructions
            else if (instructions1) {
                    g.drawImage(instructionsImage, 0, 0, this);

                    // Back button to main menu
                    if (backOver)
                            g.drawImage(backButton, 23, 628, this);
                    // Next button to next page
                    else if (nextOver)
                            g.drawImage(nextButton, 875, 626, this);
            } else if (instructions2) {
                    g.drawImage(instructionsImage2, 0, 0, this);

                    // Back button to instructions page 1
                    if (backOver)
                            g.drawImage(backButton, 23, 628, this);
                    // Next button to next page
                    else if (nextOver)
                            g.drawImage(nextButton, 875, 626, this);
            } else if (instructions3) {
                    g.drawImage(instructionsImage3, 0, 0, this);

                    // Back button to instructions page 2
                    if (backOver)
                            g.drawImage(backButton, 23, 628, this);
                    // Next button to next page
                    else if (nextOver)
                            g.drawImage(nextButton, 875, 626, this);
            } else if (instructions4) {
                    g.drawImage(instructionsImage4, 0, 0, this);

                    // Back button to instructions page 3
                    if (backOver)
                            g.drawImage(backButton, 23, 628, this);
                    // Next button to next page
                    else if (nextOver)
                            g.drawImage(nextButton, 875, 626, this);
            } else if (instructions5) {
                    g.drawImage(instructionsImage5, 0, 0, this);

                    // Back button to instructions page 4
                    if (backOver)
                            g.drawImage(backButton, 23, 628, this);
                    // Next button to next page
                    else if (nextOver)
                            g.drawImage(nextButton, 875, 626, this);
            } else if (instructions6) {
                    g.drawImage(instructionsImage6, 0, 0, this);

                    // Back button to instructions page 5
                    if (backOver)
                            g.drawImage(backButton, 23, 628, this);
                    // Next button to next page
                    else if (playOver)
                            g.drawImage(playButton, 799, 636, this);
            }
            // Draw the game play
            else {
                    
                    if (noOfPlayers == 2)
                            g.drawImage(gameBackground, 0, 0, this);
                    else
                            g.drawImage(gameBackgroundAI, 0, 0, this);

                    // New game button
                    if (newGameOver)
                            g.drawImage(newGameButton, 76, 518, this);
                    // Instructions button
                    else if (instructionsGameOver)
                            g.drawImage(instructionsGameButton, 62, 569, this);

                    // Draw marker to display who is the current player
                    if (currentPlayer == PLAYER_ONE)
                            g.drawImage(blueDot, 38, 81, this);
                    else if (currentPlayer == PLAYER_TWO)
                            g.drawImage(redDot, 38, 143, this);

                    // Draw the board
                    for (int row = 0; row < board.length; row++) {
                            for (int column = 0; column < board[0].length; column++) {
                                    // Even
                                    if (row % 2 == 0) {
                                            if (board[row][column] == EMPTY)
                                                    g.drawImage(emptyYarn, 360 + column * 60, -30 + row
                                                                    * 40, this);
                                            else if (board[row][column] == PLAYER_ONE)
                                                    g.drawImage(blueYarn, 360 + column * 60, -30 + row
                                                                    * 40, this);
                                            else if (board[row][column] == PLAYER_TWO)
                                                    g.drawImage(redYarn, 360 + column * 60, -30 + row
                                                                    * 40, this);
                                            else if (board[row][column] == POSSIBLE_MOVE
                                                            || board[row][column] == POSSIBLE_JUMP)
                                                    g.drawImage(yellowDot, 360 + column * 60, -30 + row
                                                                    * 40, this);
                                                else if (board[row][column] == CURRENT_MOVE)
                                                        g.drawImage(greenYarn, 360 + column * 60, -30 + row
                                                                        * 40, this);
                                        }
                                        // Odd
                                        else {
                                                if (board[row][column] == EMPTY)
                                                        g.drawImage(emptyYarn, 330 + column * 60, -30 + row
                                                                        * 40, this);
                                                else if (board[row][column] == PLAYER_ONE)
                                                        g.drawImage(blueYarn, 330 + column * 60, -30 + row
                                                                        * 40, this);
                                                else if (board[row][column] == PLAYER_TWO)
                                                        g.drawImage(redYarn, 330 + column * 60, -30 + row
                                                                        * 40, this);
                                                else if (board[row][column] == POSSIBLE_MOVE
                                                                || board[row][column] == POSSIBLE_JUMP)
                                                        g.drawImage(yellowDot, 330 + column * 60, -30 + row
                                                                        * 40, this);
                                                else if (board[row][column] == CURRENT_MOVE)
                                                        g.drawImage(greenYarn, 330 + column * 60, -30 + row
                                                                        * 40, this);

                                        }
                                }
                        }

                        // Display the end turn button when the player has made a move
                        if (noOfMoves >= 1) {
                                if (typeOfMove >= 1) {
                                        // Location of button depends on which player
                                        if (currentPlayer == 1)
                                                g.drawImage(endTurnButton, 800, 10, this);
                                        else if (noOfPlayers == 2)
                                                g.drawImage(endTurnButton, 800, 640, this);
                                }
                        }

                         if (isGameOver()) 
             {
                     // First player wins
                     if (winner == PLAYER_ONE)
                             g.drawImage(endBackgroundP1, 0, 0, this);
                     // Second player wins
                     else if (winner == PLAYER_TWO && noOfPlayers == 2)
                             g.drawImage(endBackgroundP2, 0, 0, this);
                     // Computer wins
                     else
                             g.drawImage(endBackgroundAI, 0, 0, this);
                     // Play again button
                     if (playAgain)
                             g.drawImage(playAgainButton, 397, 528, this);

             }

                }

        }

        @Override
        public void mouseDragged(MouseEvent event) {

        }

        @Override
        public void mouseMoved(MouseEvent event) {
                // Find the coordinates of the mouse
                int x = event.getX();
                int y = event.getY();

                // In the main menu
                if (mainMenu) {
                        // Over instructions button
                        if (x >= 12 && x <= 252 && y >= 628 && y <= 678)
                                instructionsOver = true;
                        else
                                instructionsOver = false;

                        // Over play game button
                        if (x >= 796 && x <= 986 && y >= 628 && y <= 678)
                                playOver = true;
                        else
                                playOver = false;
                }
                // In the game
                else if (gamePlay) {
                        // Over game button
                        if (x >= 76 && x <= 215 && y >= 518 && y <= 561)
                                newGameOver = true;
                        else
                                newGameOver = false;

                        // Over instructions button
                        if (x >= 62 && x <= 231 && y >= 569 && y <= 611)
                                instructionsGameOver = true;
                        else
                                instructionsGameOver = false;
                }
                 // Game is over
        else if (gameOver)
        {
                if (x >= 397 && x <= 603 && y >= 528 && y <= 592)
                        playAgain = true;
                else
                        playAgain = false;
        }
                // Instructions1
                else if (instructions1) {
                        // Over back button to main menu
                        if (x >= 23 && x <= 114 && y >= 628 && y <= 687)
                                backOver = true;
                        else
                                backOver = false;

                        // Over next button to go to the next page
                        if (x >= 875 && x <= 981 && y >= 626 && y <= 683)
                                nextOver = true;
                        else
                                nextOver = false;
                } else if (instructions2 || instructions3 || instructions4
                                || instructions5) {
                        // Over back button to previous page
                        if (x >= 23 && x <= 114 && y >= 628 && y <= 687)
                                backOver = true;
                        else
                                backOver = false;

                        // Over next button to go to the next page
                        if (x >= 875 && x <= 981 && y >= 626 && y <= 683)
                                nextOver = true;
                        else
                                nextOver = false;
                } else if (instructions6) {
                        // Over back button to previous page
                        if (x >= 23 && x <= 114 && y >= 628 && y <= 687)
                                backOver = true;
                        else
                                backOver = false;

                        // Play the game
                        if (x >= 801 && x <= 991 && y >= 626 && y <= 683)
                                playOver = true;
                        else
                                playOver = false;
                }

                repaint();
        }

        /**
         * Clears the possible moves on the board
         */
        public void clearPossibleMoves() {
                // Run through each position
                for (int row = 0; row < board.length; row++) {
                        for (int column = 0; column < board[0].length; column++) {
                                // Change the position to empty, it is currently a possible move
                                // piece
                                if (board[row][column] == POSSIBLE_MOVE
                                                || board[row][column] == POSSIBLE_JUMP)
                                        board[row][column] = EMPTY;
                                // Change the position to a green yarn if the piece is currently being
                                // moved
                                if (board[row][column] == CURRENT_MOVE)
                                        board[row][column] = currentPlayer;
                        }
                }

                repaint();
        }

        /**
         * Checks all possible jumps of the computer player from a given position
         * @param move the move object which is the position that the yarn will
         *                jump from
         */
        public void addJump(Move move) {
                // Get the row and column of the position
                int row = move.getRow();
                int column = move.getColumn();

                // Stop searching if the position is out of the board
                if (board[row][column] == HIDDEN)
                        return;

                // Search for possible jumps on the six legal sides
                for (int side = 0; side < 6; side++) {
                        if ((row != oldRow || column != oldColumn) && noOfMoves > 0)
                                return;
                        int noOfEmpty = 0;
                        int newRow = row;
                        int newColumn = column;
                        boolean possibleJump = false;

                        // Since the longest length of the board in any legal moving
                        // direction,
                        // is 9, the longest distance until the yarn to jump over, is 5
                        for (int jump = 0; jump < 6; jump++) {
                                // Odd
                                if (newRow % 2 != 0) {
                                        newRow = newRow + odd[side][0];
                                        newColumn = newColumn + odd[side][1];
                                }
                                // Even
                                else {
                                        newRow = newRow + even[side][0];
                                        newColumn = newColumn + even[side][1];
                                }

                                // Hidden piece, out of the visible array bounds
                                if (board[newRow][newColumn] == HIDDEN) {
                                        noOfEmpty = -1;
                                        jump = 6;
                                }
                                // Count the number of consecutive empties there are
                                else if (board[newRow][newColumn] == EMPTY) {
                                        noOfEmpty++;
                                }
                                // There is a piece there
                                else if (board[newRow][newColumn] == PLAYER_ONE
                                                || board[newRow][newColumn] == PLAYER_TWO)
                                        jump = 6;

                        }
                        // Ensures that noOfEmpty is not -1, meaning that it is out of the
                        // visible array bounds
                        if (noOfEmpty >= 0) {
                                int noOfEmpty2 = 0;
                                
                                // Checks to see if there is equal number of empty spots
                                // on both sides of the yarn to jump over
                                for (int empty = 0; empty <= noOfEmpty; empty++) {
                                        // Odd
                                        if (newRow % 2 != 0) {
                                                newRow = newRow + odd[side][0];
                                                newColumn = newColumn + odd[side][1];
                                        }
                                        // Even
                                        else {
                                                newRow = newRow + even[side][0];
                                                newColumn = newColumn + even[side][1];
                                        }

                                        // Checks for a possible jump over a yarn adjacent to
                                        // the Move object
                                        if (noOfEmpty == 0 && board[newRow][newColumn] == EMPTY) {
                                                possibleJump = true;
                                        // Checks for equal number of empty yarns on either side
                                        // plus an additional spot for the yarn to jump into
                                        } else if (noOfEmpty == noOfEmpty2
                                                        && board[newRow][newColumn] == EMPTY) {
                                                possibleJump = true;
                                        }

                                        // Continue counting if the current position is still empty
                                        if (board[newRow][newColumn] == EMPTY)
                                                noOfEmpty2++;
                                        // If it is not empty
                                        else {
                                                possibleJump = false;
                                                empty = noOfEmpty;
                                        }
                                }
                        }

                        // Check if the jump is already present in the ArrayList only if the
                        // jump is possible
                        if (possibleJump) {
                                // Check for repetition
                                // Check if the possible jumps have more jumps
                                Move nextMove = new Move(newRow, newColumn, move);
                                if (!isAlreadyInComputerMoves(nextMove)) {
                                        computerPossibleMoves.add(nextMove);
                                        addJump(nextMove);
                                }

                        }
                }

                return;
        }

        /**
         * Checks if a given end position is already in the list of checked computer
         * moves
         * @param checkMove the position to check for being in the list of computer moves
         * @return true if the move to check has the same row and column as a move
         *         in the computer moves list, false if not
         */
        public boolean isAlreadyInComputerMoves(Move checkMove) {
                for (Move move : computerPossibleMoves) {
                        if (move.getRow() == checkMove.getRow()
                                        && move.getColumn() == checkMove.getColumn())
                                return true;
                }
                return false;
        }

        /**
         * Generates the possible moves adjacent to the current yarn
         * @param initial the original position of the yarn
         */
        private void generateSingleAIMoves (Move initial){
                // Get the row and column of the initial position
                int row = initial.getRow();
                int column = initial.getColumn();
                int newRow = row;
                int newColumn = column;
                
                // Check the six sides for possible moves
                for (int side = 0; side < 6; side++) {

                                // Odd
                                if (row % 2 != 0) {
                                        newRow = row + odd[side][0];
                                        newColumn = column + odd[side][1];
                                }
                                // Even
                                else {
                                        newRow = row + even[side][0];
                                        newColumn = column + even[side][1];
                                }
                                if (board[newRow][newColumn] == EMPTY) {
                                                computerPossibleMoves.add(new Move(newRow, newColumn,
                                                                initial));
                                }
                        }
        }
        
        /**
         * Finds the best possible move for the AI and makes the move
         */
        public void computerPlayer() {
                
                // Check all computer yarns above the region that the yarns must be in
                // to win for possible moves
                for (int row = 17; row > 0; row--) {
                        for (int column = 1; column < 10; column++) {
                                // If the current position is the computer player's yarn
                                // Check and add for all possible moves and jumps
                                if (board[row][column] == PLAYER_TWO) {
                                        oldRow = row;
                                        oldColumn = column;
                                        Move firstMove = new Move(row, column, null);
                                        computerPossibleMoves.add(firstMove);
                                        generateSingleAIMoves(firstMove);
                                        addJump(firstMove);
                                }
                        }
                }

                int largestDistance = -1;
                Move bestMove = new Move(-1, -1, null);

                // Finds the best move out of all the possibleMoves
                for (int possibleMove = 0; possibleMove < computerPossibleMoves.size(); possibleMove++) {
                        Move currentMove = computerPossibleMoves.get(possibleMove);
                        if (currentMove.isPossibleMove()
                                        && currentMove.advancementToEnd() >= largestDistance) {
                                bestMove = currentMove;
                                largestDistance = bestMove.advancementToEnd();
                        }
                }
                
                ArrayList<Move> bestMoveList = bestMove.getMoveList();
                
                // Go through the best move sequence and show all the moves
                for (int move = 0; move < bestMoveList.size() - 1; move++) {
                        Move start = bestMoveList.get(move);
                        Move end = bestMoveList.get(move + 1);
                        board[start.getRow()][start.getColumn()] = CURRENT_MOVE;
                        paintImmediately(0,0,getWidth(),getHeight());
                        try {
                                Thread.sleep(JUMP_DELAY);
                        } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                        }
                        board[start.getRow()][start.getColumn()] = EMPTY;
                        board[end.getRow()][end.getColumn()] = PLAYER_TWO;
                }

                computerPossibleMoves.clear();
                currentPlayer = 1;
        }

        /**
         * Gets the URL needed for newAudioClip
         * @param fileName the name of the music file to play
         * @return the complete file name
         */
        public URL getCompleteURL(String fileName) {
                try {
                        return new URL("file:" + System.getProperty("user.dir") + "/"
                                        + fileName);
                } catch (MalformedURLException e) {
                        System.err.println(e.getMessage());
                }
                return null;
        }

        @Override
        public void mouseClicked(MouseEvent arg0) {
                // TODO Auto-generated method stub

        }

        @Override
        public void mouseEntered(MouseEvent arg0) {
                // TODO Auto-generated method stub

        }

        @Override
        public void mouseExited(MouseEvent arg0) {
                // TODO Auto-generated method stub

        }

        @Override
        public void mousePressed(MouseEvent event) {

                // Find the coordinates of the mouse
                int x = event.getX();
                int y = event.getY();

                if (mainMenu) {
                        // Go to the instructions page
                        if (x >= 12 && x <= 252 && y >= 628 && y <= 678) {
                                instructions1 = true;
                                mainMenu = false;
                                gameState = 1;
                        }

                        // Go to play game
                        else if (x >= 796 && x <= 986 && y >= 628 && y <= 678) {
                                if(noOfPlayers())
                                {
                                gamePlay = true;
                                mainMenu = false;
                                }
                                
                        }
                } 
                // Game is finished
                else if (gameOver)
                {
                         if (x >= 397 && x <= 603 && y >= 528 && y <= 592)
             {       
                     gamePlay = true;
                     newGame();
                     gameOver = false;
             }
                }
                else if (instructions1) {
                        // Go back to main menu
                        if (x >= 23 && x <= 114 && y >= 628 && y <= 687) {
                                if (gameState == 1)
                                        mainMenu = true;
                                else
                                        gamePlay = true;

                                instructions1 = false;

                        }
                        // Go to instructions 2
                        else if (x >= 875 && x <= 981 && y >= 626 && y <= 683) {
                                instructions2 = true;
                                instructions1 = false;
                        }
                } else if (instructions2) {
                        // Go back to instructions 1
                        if (x >= 23 && x <= 114 && y >= 628 && y <= 687) {
                                instructions1 = true;
                                instructions2 = false;
                        }
                        // Go to instructions 3
                        else if (x >= 875 && x <= 981 && y >= 626 && y <= 683) {
                                instructions3 = true;
                                instructions2 = false;
                        }
                } else if (instructions3) {
                        // Go back to instructions 2
                        if (x >= 23 && x <= 114 && y >= 628 && y <= 687) {
                                instructions2 = true;
                                instructions3 = false;
                        }
                        // Go to instructions 4
                        else if (x >= 875 && x <= 981 && y >= 626 && y <= 683) {
                                instructions4 = true;
                                instructions3 = false;
                        }
                } else if (instructions4) {
                        // Go back to instructions 3
                        if (x >= 23 && x <= 114 && y >= 628 && y <= 687) {
                                instructions3 = true;
                                instructions4 = false;
                        }
                        // Go to instructions 5
                        else if (x >= 875 && x <= 981 && y >= 626 && y <= 683) {
                                instructions5 = true;
                                instructions4 = false;
                        }
                } else if (instructions5) {
                        // Go back to instructions 4
                        if (x >= 23 && x <= 114 && y >= 628 && y <= 687) {
                                instructions4 = true;
                                instructions5 = false;
                        }
                        // Go to instructions 6
                        else if (x >= 875 && x <= 981 && y >= 626 && y <= 683) {
                                instructions6 = true;
                                instructions5 = false;
                        }
                } else if (instructions6) {
                        // Go back to instructions 5
                        if (x >= 23 && x <= 114 && y >= 628 && y <= 687) {
                                instructions5 = true;
                                instructions6 = false;
                        }
                        // Go to game play
                        else if (x >= 801 && x <= 991 && y >= 626 && y <= 683) {
                                
                    
                                // Start a new game if the player came from the main menu
                                if (gameState == 1)
                                        newGame();
                                // Go back to the game the player was playing
                                else
                                {
                                    instructions6= false;
                                        gamePlay = true;
                                gameState = 2;
                                }
                        	
                        }
                }
                // In the game
                else {
                        // New game
                        if (x >= 76 && x <= 215 && y >= 518 && y <= 561)
                                newGame();

                        // Go to instructions
                        if (x >= 62 && x <= 231 && y >= 569 && y <= 611) {
                                instructions1 = true;
                                gamePlay = false;
                                gameState = 2;
                        }

                        // In a one player game, the human player finishes their move
                        if (typeOfMove == 1 && noOfMoves == 1) {
                                currentPlayer = 3 - currentPlayer;
                                noOfMoves = 0;
                                repaint();
                                if (noOfPlayers == 1 && currentPlayer == PLAYER_TWO)
                                        computerPlayer();
                        }

                        // End turn button
                        if (noOfMoves >= 1) {
                                // Switch other player when the end turn button is pressed
                                if (currentPlayer == PLAYER_ONE && x >= 800 && x <= 990
                                                && y >= 10 && y <= 60) {
                                        clearPossibleMoves();
                                        currentPlayer = PLAYER_TWO;
                                        typeOfMove = 0;
                                        noOfMoves = 0;

                                        // Do the same for other player, they are not the AI
                                        if (noOfPlayers == 1)
                                                computerPlayer();
                                } else if (currentPlayer == PLAYER_TWO && x >= 800 && x <= 990
                                                && y >= 640 && y <= 690) {
                                        clearPossibleMoves();
                                        currentPlayer = PLAYER_ONE;
                                        typeOfMove = 0;
                                        noOfMoves = 0;
                                }

                        }
                        // Current position in terms of on board
                        int currentRow = (y + 30) / 40;
                        int currentColumn = 0;

                        // If in the range of the board
                        if (x >= 330 && x <= 990 && y >= 10) {
                                // Find the column value
                                // Even
                                if (currentRow % 2 == 0) {
                                        currentColumn = (x - 360) / 60;
                                }
                                // Odd
                                else {
                                        currentColumn = (x - 330) / 60;
                                }
                                
                                // On clicked marble
                                if (board[currentRow][currentColumn] == currentPlayer) {
                                        clearPossibleMoves();
                                        currentX = currentRow;
                                        currentY = currentColumn;
                                        showPossibleMoves(currentRow, currentColumn);
                                }
                                // On clicked marble's possible moves
                                else if (board[currentRow][currentColumn] == POSSIBLE_MOVE) {
                                        typeOfMove = 1;
                                        makeMove(currentX, currentY, currentRow, currentColumn);

                                        // Clear other possible moves
                                        clearPossibleMoves();
                                } else if (board[currentRow][currentColumn] == POSSIBLE_JUMP) {
                                        typeOfMove = 2;
                                        makeMove(currentX, currentY, currentRow, currentColumn);

                                        clearPossibleMoves();
                                } else
                                        clearPossibleMoves();
                        }
                        
                }
                repaint();
        }

        @Override
        public void mouseReleased(MouseEvent arg0) {
                // TODO Auto-generated method stub

        }

}
