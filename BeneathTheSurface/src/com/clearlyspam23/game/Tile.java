package com.clearlyspam23.game;

public class Tile {
	
	public boolean blocksMovement;
	public float movementFactor;
	public float movementCap = Float.MAX_VALUE;
	
	public boolean blocksMovement()
	{
		return blocksMovement;
	}
	
	public float getMovementFactor()
	{
		return movementFactor;
	}
	
	public float getMovementCap()
	{
		return movementCap;
		
	}
}
