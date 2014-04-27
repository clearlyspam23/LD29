package com.clearlyspam23.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.clearlyspam23.util.UtilMath;

public class Entity {
	
	private Facing currentFacing;
	private Vector2 location = new Vector2();
	private Vector2 velocity = new Vector2();
	private Vector2 bounds = new Vector2();
	private Vector2 acceleration = new Vector2();
	private Rectangle rectangle;
	private World myWorld;
	
	public Entity(Vector2 location, Vector2 bounds){
		this.location.set(location);
		this.bounds.set(bounds);
	}
	
	public enum Facing{
		left, right
	}
	
	public Facing getCurrentFacing() {
		return currentFacing;
	}

	public void setCurrentFacing(Facing currentFacing) {
		this.currentFacing = currentFacing;
	}
	
	public boolean affectedByGravity()
	{
		return false;
	}
	
	public Entity(float locx, float locy, float boundsx, float boundsy){
		location.set(locx, locy);
		bounds.set(boundsx, boundsy);
		rectangle = new Rectangle(locx-boundsx/2, locy-boundsy/2, boundsx, boundsy);
	}

	public Vector2 getLocation() {
		return location;
	}

	public void setLocation(Vector2 location) {
		this.location.set(location);
		rectangle.x = location.x - bounds.x/2;
		rectangle.y = location.y - bounds.y/2;
	}
	
	public void setLocation(float x, float y){
		location.set(x, y);
		rectangle.x = x - bounds.x/2;
		rectangle.y = y - bounds.y/2;
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity.set(velocity);
	}
	
	public void setVelocity(float x, float y){
		velocity.set(x, y);
	}

	public World getWorld() {
		return myWorld;
	}

	public void setWorld(World myWorld) {
		this.myWorld = myWorld;
	}

	public Vector2 getBounds() {
		return bounds;
	}

	public void setBounds(Vector2 bounds) {
		this.bounds = bounds;
		rectangle.width = bounds.x;
		rectangle.height = bounds.y;
	}
	
	public void move(float delta){
		velocity.set(velocity.x + acceleration.x*delta, velocity.y+acceleration.y*delta);
		move(delta * velocity.x, delta * velocity.y);
	}
	
	public void move(float amountX, float amountY)
	{
		setLocation(location.x + amountX, location.y + amountY);
	}
	
	public void collidesWithEntity(Entity other)
	{
		
	}
	
	public void collidesWithTile(Rectangle r, Tile tile)
	{
		pushOutOfTile(r);
	}
	
	public void pushOutOfTile(Rectangle r)
	{
		Rectangle overlap = UtilMath.intersection(rectangle, r);
		if(overlap.width < overlap.height)
		{
			if(location.x > r.x + r.width/2)
				setLocation(r.x+r.width+bounds.x/2, location.y);
			else
				setLocation(r.x - bounds.x/2, location.y);
		}
		else
		{
			if(location.y > r.y + r.height/2)
				setLocation(location.x, r.y+r.height+bounds.y/2);
			else
				setLocation(location.x, r.y-bounds.y/2);
		}
	}

	public Rectangle getRectangle() {
		return rectangle;
	}
	
	public void update(float delta)
	{
		
	}

	public Vector2 getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(float x, float y) {
		acceleration.set(x, y);
	}

}
