package com.clearlyspam23.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.clearlyspam23.util.UtilMath;

public class GroundUnitEntity extends UnitEntity{
	
	private Vector2 lastNormal = new Vector2();
	private boolean wasOnGround = false;
	
	private static final float JUMP_DELAY = 0.25f;
	private float nextJumpCounter;
	private boolean didMove = false;

	public GroundUnitEntity(float locx, float locy, float boundsx, float boundsy, int team, int health) {
		super(locx, locy, boundsx, boundsy, team, health);
	}
	
	public void collidesWithTile(Rectangle r, Tile tile)
	{
		super.collidesWithTile(r, tile);
		if(nextJumpCounter<=0)
			setMovement(getMovement().x, 0);
		UtilMath.calculateNormal(r, getLocation(), lastNormal);
		float bounds = tile.getMovementCap();
		if(lastNormal.x!=0)
		{
			setVelocity(0, Math.max(-bounds, Math.min(getVelocity().y, bounds)));
		}
		else
		{
			if(lastNormal.y>0)
				setVelocity(0, 0);
		}
		if(!wasOnGround)
		{
			setMovement(getMovement().x, 0);
			setVelocity(0, 0);
		}
	}

	public boolean isOnGround() {
		return lastNormal.x!=0||lastNormal.y!=0;
	}
	
	public boolean isOnFlatGround(){
		return isOnGround()&&lastNormal.y==1;
	}
	
	public void update(float delta)
	{
		super.update(delta);
		wasOnGround = isOnGround();
		if(!didMove&&isOnGround())
		{
			slowDown(0.3f, delta);
		}
		didMove = false;
		lastNormal.set(0, 0);
		if(nextJumpCounter>0)
			nextJumpCounter-=delta;
	}
	
	private boolean oppositeSigns(float x, float y)
	{
		return x>0&&y<=0||x<=0&&y>0;
	}
	
	private void slowDown(float amount, float delta)
	{
		setMovement(getMovement().x*amount, getMovement().y);
	}
	
	public void jump(float amount)
	{
		if(isOnGround()&&nextJumpCounter<=0&&lastNormal.y>=0)
		{
			Vector2 velo = getMovement();
			setMovement(velo.x, velo.y+amount);
			velo = getVelocity();
			setVelocity(velo.x+amount*2/4*lastNormal.x, velo.y);
			nextJumpCounter = JUMP_DELAY;
			for(UnitEventListener l : getListeners())
				l.onJump(this);
		}
		else if(nextJumpCounter>0&&!isOnGround())
		{
			setVelocity(getVelocity().x, 0);
		}
	}
	
	private void setMovementWithinCap(float amount, float cap)
	{
		Vector2 velo = getMovement();
		float absCap = Math.abs(cap);
		setMovement(Math.max(-absCap, Math.min(velo.x+amount, absCap)), velo.y);
	}
	
	public void unit_move(float amount, float cap)
	{
		float scaleFactor = 4;
		didMove = true;
		if(!isOnFlatGround())
		{
			if(nextJumpCounter>0)
				return;
			Vector2 velo = getVelocity();
			if(oppositeSigns(velo.x, amount))
			{
				velo.x+=amount/scaleFactor;
				if(!oppositeSigns(velo.x, amount))
				{
					float remainder = amount/scaleFactor-velo.x;
					setMovementWithinCap(remainder, cap);
				}
			}
			else
			{
				float speed = amount/scaleFactor;
				velo.x-=speed;
				if(oppositeSigns(velo.x, speed))
				{
					float remainder = amount/scaleFactor+velo.x;
					velo.x+=remainder;
					speed-=remainder;
				}
				setMovementWithinCap(amount/scaleFactor, cap);
			}
		}
		else
		{
			setMovementWithinCap(amount, cap);
		}
	}

}
