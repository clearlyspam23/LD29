package com.clearlyspam23.game;

import com.badlogic.gdx.math.Rectangle;

public class ProjectileEntity extends TeamBasedEntity{
	
	private int damage;
	private float duration;

	public ProjectileEntity(float locx, float locy, float boundsx, float boundsy, int team, int damage, float duration) {
		super(locx, locy, boundsx, boundsy, team);
		this.damage = damage;
		this.duration = duration;
	}
	
	public void update(float delta)
	{
		duration-=delta;
		if(duration<=0)
		{
			getWorld().removeEntity(this);
		}
	}
	
	public void collidesWithEntity(Entity other)
	{
		if(!isSameTeam(other)&&other instanceof UnitEntity)
		{
			UnitEntity ue = (UnitEntity) other;
			if(!ue.avoidsCollision())
			{
				ue.damage(damage);
				getWorld().removeEntity(this);
			}
		}
	}
	
	public void collidesWithTile(Rectangle r, Tile tile)
	{
		super.collidesWithTile(r, tile);
		getWorld().removeEntity(this);
	}
	
	public void setDuration(float dur)
	{
		duration = dur;
	}
	
	public float getDuration()
	{
		return duration;
	}

}
