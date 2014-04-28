package com.clearlyspam23.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Rectangle;

public class ProjectileEntity extends TeamBasedEntity{
	
	private int damage;
	private float duration;
	private List<WeaponEventListener> listeners = new ArrayList<WeaponEventListener>();
	private List<Entity> hitEntities = new ArrayList<Entity>();

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
		for(WeaponEventListener l : listeners)
			l.projectileUpdate(this);
	}
	
	public void collidesWithEntity(Entity other)
	{
		if(!isSameTeam(other)&&other instanceof UnitEntity)
		{
			UnitEntity ue = (UnitEntity) other;
			if(!ue.avoidsCollision()&&!hitEntities.contains(ue))
			{
				ue.damage(damage);
				hitEntities.add(ue);
				boolean b = false;
				for(WeaponEventListener l : listeners)
					b=b||l.onProjectileCollision(this, ue);
				if(!b)
					getWorld().removeEntity(this);
			}
		}
	}
	
	public void collidesWithTile(Rectangle r, Tile tile)
	{
		super.collidesWithTile(r, tile);
		boolean b = false;
		for(WeaponEventListener l : listeners)
			b=b||l.onTileCollision(this, r, tile);
		if(!b)
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

	public List<WeaponEventListener> getListeners() {
		return listeners;
	}

	public void addListener(WeaponEventListener listener) {
		this.listeners.add(listener);
	}
	
	public void setWorld(World world)
	{
		super.setWorld(world);
		for(WeaponEventListener l : listeners)
			l.afterProjectileSpawn(this);
	}

	public List<Entity> getHitEntities() {
		return hitEntities;
	}

}
