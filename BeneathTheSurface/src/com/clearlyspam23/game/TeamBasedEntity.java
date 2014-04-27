package com.clearlyspam23.game;

public class TeamBasedEntity extends Entity{
	
	private int team;

	public TeamBasedEntity(float locx, float locy, float boundsx, float boundsy, int team) {
		super(locx, locy, boundsx, boundsy);
		this.team = team;
	}
	
	public void setTeam(int team)
	{
		this.team = team;
	}
	
	public int getTeam()
	{
		return team;
	}
	
	public boolean isSameTeam(Entity other)
	{
		if(other instanceof TeamBasedEntity)
			return team==((TeamBasedEntity)other).team;
		return true;
	}

}
