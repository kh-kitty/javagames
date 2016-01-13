import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * Plays a simple game of Chinese Checkers
 * @author Kitty Huang and Kitty Su
 * @version May 25, 2015
 */
public class ChineseCheckersMain extends JFrame implements ActionListener
{

	private JMenuItem newMenuItem, quitMenuItem, aboutMenuItem;
	private ChineseCheckersBoard gameBoard;

	/**
	 * Creates a Simple Chinese Checkers Frame Application
	 */
	public ChineseCheckersMain()
	{
		super("Chinese Checkers");
		setResizable(false);

		// Cat image from
		// https://s-media-cache-ak0.pinimg.com/736x/79/7d/e9/797de9ea2823223fb4b5648ac5ef6f19.jpg
		setIconImage(Toolkit.getDefaultToolkit().getImage("iconPicture.png"));

		gameBoard = new ChineseCheckersBoard();

		// Centre the frame in the middle (almost) of the screen
		setLayout(new BorderLayout());
		gameBoard = new ChineseCheckersBoard();
		getContentPane().add(gameBoard, BorderLayout.CENTER);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screen.width - ChineseCheckersBoard.WIDTH) / 2,
				(screen.height - ChineseCheckersBoard.HEIGHT) / 2);

		// Add in a Menu
		JMenuBar menuBar = new JMenuBar();
		JMenu gameMenu = new JMenu("Game");
		gameMenu.setMnemonic('G');
		newMenuItem = new JMenuItem("New Game");
		newMenuItem.addActionListener(this);

		quitMenuItem = new JMenuItem("Exit");
		quitMenuItem.addActionListener(this);
		gameMenu.add(newMenuItem);

		gameMenu.addSeparator();
		gameMenu.add(quitMenuItem);
		menuBar.add(gameMenu);

		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic('H');
		aboutMenuItem = new JMenuItem("About");
		aboutMenuItem.addActionListener(this);
		helpMenu.add(aboutMenuItem);
		menuBar.add(helpMenu);
		setJMenuBar(menuBar);
	}

	/**
	 * Method that deals with the menu options
	 * @param event the event that triggered this method
	 */
	public void actionPerformed(ActionEvent event)
	{
		if (event.getSource() == newMenuItem)
		{
			gameBoard.newGame();
		}
		else if (event.getSource() == aboutMenuItem)
		{
			JOptionPane.showMessageDialog(gameBoard,
					"Chinese Checkers by Kitty and Kitty\n\u00a9 2015",
					"About Chinese Checkers", JOptionPane.INFORMATION_MESSAGE);
		}
		else if (event.getSource() == quitMenuItem)
		{
			System.exit(0);
		}
	}

	public static void main(String[] args)
	{
		ChineseCheckersMain game = new ChineseCheckersMain();
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.pack();
		game.setVisible(true);
	}

}
