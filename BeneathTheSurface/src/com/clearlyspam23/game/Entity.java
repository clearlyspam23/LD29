package com.clearlyspam23.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.clearlyspam23.util.UtilMath;

public class Entity {
	
	private Vector2 location = new Vector2();
	private Vector2 velocity = new Vector2();
	private Vector2 bounds = new Vector2();
	private Rectangle rectangle;
	private Facing currentFacing;
	private int health;
	private World myWorld;
	
	public Entity(Vector2 location, Vector2 bounds){
		this.location.set(location);
		this.bounds.set(bounds);
	}
	
	public Entity(float locx, float locy, float boundsx, float boundsy){
		location.set(locx, locy);
		bounds.set(boundsx, boundsy);
		rectangle = new Rectangle(locx-boundsx/2, locy-boundsy/2, boundsx, boundsy);
	}
	
	public enum Facing{
		left, right
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

	public Facing getCurrentFacing() {
		return currentFacing;
	}

	public void setCurrentFacing(Facing currentFacing) {
		this.currentFacing = currentFacing;
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
		move(delta * velocity.x, delta * velocity.y);
	}
	
	public void move(float amountX, float amountY)
	{
		setLocation(location.x + amountX, location.y + amountY);
		System.out.println(location);
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
				setLocation(location.x + overlap.width, location.y);
			else
				setLocation(location.x - overlap.width, location.y);
		}
		else
		{
			if(location.y > r.y + r.height/2)
				setLocation(location.x, location.y+overlap.height);
			else
				setLocation(location.x, location.y-overlap.height);
		}
	}

	public Rectangle getRectangle() {
		return rectangle;
	}

}
