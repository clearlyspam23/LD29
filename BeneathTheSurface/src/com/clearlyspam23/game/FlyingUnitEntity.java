package com.clearlyspam23.game;

public class FlyingUnitEntity extends UnitEntity {

	public FlyingUnitEntity(float locx, float locy, float boundsx,
			float boundsy, int team, int health) {
		super(locx, locy, boundsx, boundsy, team, health);
	}
	
	public boolean affectedByGravity()
	{
		return false;
	}

}
