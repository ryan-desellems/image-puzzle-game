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

//=========================================================================================================
class PuzzlePiece extends JButton
{
	ImageIcon	puzzleSlice;
	int 		row;
	int			col;
	int 		originRow;
	int			originCol;
//=========================================================================================================
PuzzlePiece()
{
	//default
}
//=========================================================================================================
PuzzlePiece(int rowIn, int colIn)
{
	this.puzzleSlice 			= new ImageIcon(); //button has no icon - blank button
	this.originRow				= rowIn;
	this.originCol				= colIn;
	this.col		 			= colIn;
	this.row					= rowIn;
}
//=========================================================================================================
PuzzlePiece(BufferedImage pieceToAssign, int rowIn, int colIn)  //normal button with chunk of main image
{
	this.puzzleSlice 			= new ImageIcon(pieceToAssign);
	this.originRow				= rowIn;
	this.originCol				= colIn;
	this.col		 			= colIn;
	this.row					= rowIn;

	setIcon(puzzleSlice);
}
//=========================================================================================================
void swapPiece(PuzzlePiece pieceToSwap)
{
	int temp = 0;                          //swap button position and icon with incoming button
	Icon tempImage;

	tempImage = pieceToSwap.getIcon();
	pieceToSwap.setIcon(this.getIcon());
	this.setIcon(tempImage);

	temp = pieceToSwap.row;
	pieceToSwap.row = this.row;
	this.row = temp;
	temp = 0;

	temp = pieceToSwap.col;
	pieceToSwap.col = this.col;
	this.col = temp;
	temp = 0;
}
//=========================================================================================================
boolean isSolved()
{
	if(row == originRow && col == originCol) //check if button is where it started
	{
		return true;
	}
	else
	{
		return false;
	}
}
//=========================================================================================================
}