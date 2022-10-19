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
import java.util.*;

class Puzzle extends JPanel
				implements ActionListener
{
	BufferedImage 						puzzleImage;
	GridLayout    						puzzleLayout;
	PuzzlePiece[][] 					puzzlePieces;

	int 								xCoordinate = 0;
	int 								yCoordinate = 0;
	int									blankRow = 0;
	int									blankCol = 0;

	public static final int				NUM_ROWS = 3;
 	public static final int				NUM_COLS = 3;

 	public boolean	 					victory = false;
	boolean								scrambling = false;

	int									safeguard = 0;

//=========================================================================================================
Puzzle()
{
}
//=========================================================================================================
Puzzle(BufferedImage imageToUse)
{
	puzzlePieces = new PuzzlePiece[NUM_ROWS][NUM_COLS];                //set up puzzle board

	int subWidth  = (imageToUse.getWidth()/NUM_COLS);					//divide image according to grid size
	int subHeight = (imageToUse.getHeight()/NUM_ROWS);

	setLayout(puzzleLayout = new GridLayout(NUM_ROWS,NUM_COLS));      //create grid

	for(int row=0;row<NUM_ROWS;row++)
	{
		for(int col=0;col<NUM_COLS;col++)							//load grid with buttons and image icons
		{
			if(((row+1) + (col+1)) != (NUM_ROWS + NUM_COLS))
			{
				puzzlePieces[row][col] = new PuzzlePiece(imageToUse.getSubimage(xCoordinate,yCoordinate,subWidth,subHeight),row,col);
			}
			else													//creating blank space for game
			{
				puzzlePieces[row][col] = new PuzzlePiece(row,col);
				this.blankRow = row;
				this.blankCol = col;
			}
			add(puzzlePieces[row][col]);                                //adding buttons and image
			puzzlePieces[row][col].setActionCommand("PUZZLE");
			puzzlePieces[row][col].addActionListener(this);
			xCoordinate += subWidth;
		}
		xCoordinate = 0;
		yCoordinate += subHeight;
	}
}
//=========================================================================================================
public void actionPerformed(ActionEvent e)
{
	if(e.getActionCommand().equals("PUZZLE"))
	{
		processMove((PuzzlePiece)e.getSource());                      //process clicked puzzle piece
	}
}
//=========================================================================================================
void processMove(PuzzlePiece pieceToCheck)
{
	if(isNextToBlankPiece(pieceToCheck))                              //if the piece can be moved
	{
		puzzlePieces[blankRow][blankCol].swapPiece(pieceToCheck);     //swap it with the blank piece
		blankRow = pieceToCheck.originRow;                            //update the blank piece's location
		blankCol = pieceToCheck.originCol;
	}
	if(isFinished() && !scrambling)									//if the game is won and the board didn't randomly solve itself
	{
		System.out.println("YOU WIN!");                              //update public victory variable (to manipulate timer and gamestate in ImageGame.java)
		victory = true;
	}
}
//=========================================================================================================
boolean isNextToBlankPiece(PuzzlePiece pieceToCheck)
{
	if(  (Math.abs(pieceToCheck.originRow - blankRow) == 1) && (pieceToCheck.originCol == blankCol) ||
		 (Math.abs(pieceToCheck.originCol - blankCol) == 1) && (pieceToCheck.originRow == blankRow) )
	{
		System.out.println("Next to a blank.");                    //If the grid position is one to the left, right, up, or down
		return true;
	}
	else if(pieceToCheck.originRow == blankRow && pieceToCheck.originCol == blankCol)
	{
		System.out.println("Blank button");                        //The blank button was clicked
		return false;
	}
	System.out.println("Not next to a blank.");                   //Invalid button was clicked
	return false;
}
//=========================================================================================================
boolean isFinished()
{
	for(int row=0;row<NUM_ROWS;row++)
	{
		for(int col=0;col<NUM_COLS;col++)
		{
			if(!puzzlePieces[row][col].isSolved())              	 //If the individual piece is not in it's original location
			{
				return false;
			}
		}
	}
	return true;                                                     //Every piece is in it's original location, the game is over
}
//=========================================================================================================
void scramble()
{
	Random		scrambler;
	int			col = 0;
	int			row = 0;

	System.out.println("Scramble");

	scrambler = new Random(); 										// automatically selects a seed

	scrambling = true;
	System.out.println("Scrambling!");
	for (int n = 0; n < ((NUM_ROWS*NUM_COLS)*(NUM_ROWS*NUM_COLS)) ; n++) //scramble the board according to the grid size
	{
	    row = blankRow + scrambler.nextInt(3) - 1; 						//nextInt(3) is {0,1,2}
		if (row < 0 || row >= NUM_ROWS)
		{
			 row = blankRow;											//if random is invalid, it becomes the blank button row
		}
		col = blankCol + scrambler.nextInt(3) - 1;
		if (col < 0 || col >= NUM_COLS)
		{
			col = blankCol;												//if random is invalid, it becomes the blank button column
		}
		puzzlePieces[row][col].doClick();                              //click the button to process the move
	 }
	 scrambling = false;
	 System.out.println("Scrambling complete!");
	 if(isFinished() && safeguard < 10)											//if the board magically solved itself
	 {
		 	safeguard++;														//keep it from doing that
			scramble();
	 }
	 else if (safeguard >= 10)                                                 // *there is a rare but possible chance that the game will infinitely solve itself
	 {                                                                        // if that happens ten times consecutively, idk man

		JOptionPane.showMessageDialog(null, "Buy a lottery ticket. Trust me.", "Wow.", JOptionPane.INFORMATION_MESSAGE);
	 }
}
//========================================================================================================

void dump()															//debugging code provided by Mr. Larue
{
PuzzlePiece[][] p;
p = puzzlePieces; // I'm lazy and don't want to type a lot.
for (int r = 0; r < NUM_ROWS; r++)
    {
    for (int c = 0; c < NUM_COLS; c++)
        System.out.printf("%d, %d   %d, %d       ", p[r][c].row, p[r][c].col, p[r][c].originRow, p[r][c].originCol);
    System.out.println();
    }
System.out.printf("Blank: %d, %d\n", blankRow, blankCol);
System.out.println("-------------");
}
}
//=========================================================================================================
//END OF PUZZLE
