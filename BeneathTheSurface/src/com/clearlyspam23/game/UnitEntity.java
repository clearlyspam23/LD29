package com.clearlyspam23.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

public class UnitEntity extends TeamBasedEntity{
	
	private int health;
	private Vector2 movementVelocity = new Vector2();
	private Vector2 movementAcceleration = new Vector2();
	private boolean avoidsCollision = false;
	private List<UnitEventListener> listeners = new ArrayList<UnitEventListener>();
	
	public UnitEntity(float locx, float locy, float boundsx, float boundsy, int team, int health) {
		super(locx, locy, boundsx, boundsy, team);
		this.health = health;
	}
	
	public void update(float delta)
	{
		super.update(delta);
		if(isDead())
			onDeath();
	}
	
	public boolean affectedByGravity()
	{
		return true;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}
	
	public void damage(int amount){
		health-=amount;
	}
	
	public boolean isDead(){
		return health<=0;
	}
	
	public Vector2 getMovement()
	{
		return movementVelocity;
	}
	
	public void setMovement(float x, float y)
	{
		movementVelocity.set(x, y);
	}
	
	public void onDeath()
	{
		getWorld().removeEntity(this);
		for(UnitEventListener l : listeners)
			l.onDeath(this);
	}
	
//	public void collidesWithTile(Rectangle r, Tile tile)
//	{
//		pushOutOfTile(r);
//		setVelocity(0, 0);
//	}
	
	public void move(float delta){
		super.move(delta);
		movementVelocity.set(movementVelocity.x+movementAcceleration.x*delta, movementVelocity.y+movementAcceleration.y*delta);
		move(movementVelocity.x*delta, movementVelocity.y*delta);
	}

	public Vector2 getMovementAcceleration() {
		return movementAcceleration;
	}

	public void setMovementAcceleration(float x, float y) {
		movementAcceleration.set(x, y);
	}

	public boolean avoidsCollision() {
		return avoidsCollision;
	}

	public void setAvoidsCollision(boolean avoidsCollision) {
		this.avoidsCollision = avoidsCollision;
	}

	public List<UnitEventListener> getListeners() {
		return listeners;
	}

	public void addListener(UnitEventListener l) {
		listeners.add(l);
	}
}
