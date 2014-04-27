package com.clearlyspam23.game;

public class WeaponEntity extends Entity {
	
	private Weapon weapon;
	private float bounceDuration;
	private boolean up = true;
	
	private static final float MAX_BOUNCE = 1f;
	private static final float BOUNCE_SPEED = 0.5f;

	public WeaponEntity(float locx, float locy, float boundsx, float boundsy, Weapon w) {
		super(locx, locy, boundsx, boundsy);
		weapon = w;
		setVelocity(0, BOUNCE_SPEED);
	}
	
	public void update(float delta)
	{
		bounceDuration+=delta;
		if(bounceDuration>=MAX_BOUNCE)
		{
			bounceDuration-=MAX_BOUNCE;
			up = !up;
			setVelocity(0, (up ? BOUNCE_SPEED : -BOUNCE_SPEED));
		}
	}
	
	public void collidesWithEntity(Entity other)
	{
		if(other instanceof PlayerEntity)
		{
			((PlayerEntity)other).addWeapon(weapon);
			getWorld().removeEntity(this);
		}
	}

}
