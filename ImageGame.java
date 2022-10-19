//Name: Ryan DeSellems
//Date: 11/10/2020
//Professor: Larue
//Course: COMP 2200

import java.awt.*;
import javax.swing.*;
import java.lang.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.event.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.imageio.stream.*;
import javax.swing.Timer.*;

//ImageGame CLASS============================================================
class ImageGame extends JFrame
                               implements ActionListener
{
	boolean 		gameRunning = false;

	JButton 		newButton;
	JButton			playButton;									//gui set up
	JButton         gamestateButton;

	JLabel     		timerLabel;

	Puzzle			imagePanel;
	JPanel			buttonPanel;

	Puzzle 			puzzleBoard;

	JFileChooser 	chooser;

	Timer			clock;
	int				updateFreq = 100;							//how often timer wil be sent an action commande
	float			timeElapsed = 0;

//=======================================================================================================
ImageGame()//constructor
{
	newButton 			= new JButton("NEW");
	playButton			= new JButton("PLAY");
	gamestateButton 	= new JButton("PAUSE");

	chooser = new JFileChooser(".");

	newButton.setActionCommand("NEW");
	playButton.setActionCommand("PLAY");						//adding action commands and listeners
	gamestateButton.setActionCommand("PAUSE");

	newButton.addActionListener(this);
	playButton.addActionListener(this);
	gamestateButton.addActionListener(this);

	playButton.setEnabled(false);
	gamestateButton.setEnabled(false);

	timerLabel 	= new JLabel("Welcome. Press [NEW] to choose a picture file and begin.");

	imagePanel 		= new Puzzle();
	buttonPanel 	= new JPanel();

	buttonPanel.add(newButton);
	buttonPanel.add(playButton);
	buttonPanel.add(gamestateButton);
	buttonPanel.add(timerLabel);

	add(imagePanel, BorderLayout.CENTER);
	add(buttonPanel, BorderLayout.SOUTH);

	clock = new Timer(updateFreq,this);
	clock.setActionCommand("TIMER");

	setupMainFrame(40,40,"Puzzle Game - DeSellems - Project 4");
}
//=======================================================================================================
void setupMainFrame(int xScreenPercentage,
                    int yScreenPercentage,
                    String title          )
{
	Toolkit    tk;												//Mainframe code
	Dimension   d;

	setDefaultCloseOperation(EXIT_ON_CLOSE);

	tk = Toolkit.getDefaultToolkit();
	d = tk.getScreenSize();
	setSize(xScreenPercentage * d.width/100,
	        yScreenPercentage * d.height/100);

	setLocation((100-xScreenPercentage)*d.width/200,
	            (100-yScreenPercentage)*d.height/200);

	setTitle(title);  // For the title bar

	setMinimumSize(new Dimension(600,600));

	setVisible(true);

}  // end of setupMainFrame()
//=======================================================================================================
public void actionPerformed(ActionEvent e)
{
	if(e.getActionCommand().equals("NEW"))
	{
		clock.stop();                                   //reset game and prepare to play
		timeElapsed = 0;
		timerLabel.setText("" + timeElapsed);
		imagePanel.setVisible(false);
		openFile();
		puzzleBoard.victory = false;
		playButton.setEnabled(true);
		timerLabel.setText("Press [PLAY] to begin");
	}
	else if(e.getActionCommand().equals("PLAY"))
	{
		System.out.println("Play");                    //start game, scramble board, and start timer
		imagePanel.setVisible(true);
		puzzleBoard.scramble();
		puzzleBoard.victory = false;
		clock.start();
		playButton.setEnabled(false);
		gamestateButton.setEnabled(true);
	}
	else if(e.getActionCommand().equals("PAUSE"))
	{
		changeGamestateButton();                     //stop time and change button
		System.out.println("Paused");
		clock.stop();
	}
	else if(e.getActionCommand().equals("RESUME"))
	{
		changeGamestateButton();
		System.out.println("Playing");              //start time and change button
		clock.start();
	}
	else if(e.getActionCommand().equals("TIMER"))
	{
			String timerText;

		    timeElapsed += 0.1f;
		    timerText = String.format("Time Elapsed: %3.1f" , timeElapsed);  //update label according to time
			timerLabel.setText(timerText);
			if(puzzleBoard.victory)											//if the game is won
			{
				clock.stop();												//freeze game and tell the player
				timerText = String.format("%3.1f" , timeElapsed);
				JOptionPane.showMessageDialog(null, "You completed the puzzle in " + timerText + " seconds!", "Congratulations! You win!", JOptionPane.INFORMATION_MESSAGE);
				gamestateButton.setEnabled(false);
			}
	}
}
//=======================================================================================================
void openFile()
{
	int 				userResponse;
	BufferedImage		gameImage;
	File				inFile;

	try
	{
		userResponse = chooser.showOpenDialog(this);				//prompt for file to open

		if(userResponse == JFileChooser.APPROVE_OPTION)
		{
			System.out.println(" Dir path: " + chooser.getCurrentDirectory().getPath());
			System.out.println("File name: " + chooser.getSelectedFile().getName());
		    System.out.println("File path: " + chooser.getSelectedFile().getPath());
       		System.out.println("File canonical path: " + chooser.getSelectedFile().getCanonicalPath());

			inFile = chooser.getSelectedFile();						//get file and read in
			gameImage = ImageIO.read(inFile);
			gameImage = imageToBufferedImage(gameImage.getScaledInstance(400,400,0));

			puzzleBoard = new Puzzle(gameImage);
			imagePanel.removeAll();
			imagePanel.add(puzzleBoard);
			imagePanel.revalidate();
		}
		else
		{
			System.out.println("Cancelled");
		}
	}
	catch(IOException e)
	{
	    System.out.println("Exception found in OpenFile()");
    	e.printStackTrace();
		JOptionPane.showMessageDialog(null, "Something very bad has happened. Get help.", "Error", JOptionPane.ERROR_MESSAGE);
	}
}
//=========================================================================================================
void changeGamestateButton()
{
	gameRunning = !gameRunning;
	if(gameRunning)                                        //swap button to pause or resume depending on game state
	{
		gamestateButton.setText("RESUME");
		gamestateButton.setActionCommand("RESUME");
		imagePanel.setVisible(false);
	}
	else if(!gameRunning)
	{
		gamestateButton.setText("PAUSED");
		gamestateButton.setActionCommand("PAUSE");
		imagePanel.setVisible(true);
	}
	System.out.println("Button is changed to " + gameRunning);

}
//=========================================================================================================
public static BufferedImage imageToBufferedImage(Image im)
{                                                              //convert image back to buffered image

	BufferedImage bi = new BufferedImage (im.getWidth(null),im.getHeight(null),BufferedImage.TYPE_INT_RGB);

	Graphics bg = bi.getGraphics();

	bg.drawImage(im, 0, 0, null);

	bg.dispose();

	return bi;

}
//=========================================================================================================
}
//END OF IMAGEGAME CLASS
