package com.clearlyspam23.game.weapons;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.clearlyspam23.game.ProjectileEntity;
import com.clearlyspam23.game.UnitEntity;
import com.clearlyspam23.game.WeaponEventListener;

public class PrimaryWeapon extends BasicWeapon{
	
	private int projectileCount;
	private int projectileDamage;


	private float projectileSpeed;
	private float projectileDuration;
	private Vector2 primaryBounds;
	private float bulletSpread;
	
	private List<WeaponEventListener> listeners = new ArrayList<WeaponEventListener>();
	
	public PrimaryWeapon(float cooldown, float projectileSpeed, float projectileDuration, int projectileCount, int projectileDamage, float bulletSpread, float boundsX, float boundsY)
	{
		setBaseCooldown(cooldown);
		this.projectileDamage = projectileDamage;
		this.projectileCount = projectileCount;
		this.projectileSpeed = projectileSpeed;
		this.projectileDuration = projectileDuration;
		this.bulletSpread = bulletSpread;
		primaryBounds = new Vector2(boundsX, boundsY);
	}
	

	@Override
	public void onPickUp() {
		getEntity().setPrimaryWeapon(this);
	}

	@Override
	public List<ProjectileEntity> shoot() {
		List<ProjectileEntity> ans = new ArrayList<ProjectileEntity>();
		Vector2 location = new Vector2();
		location.set(getEntity().getLocation());
		UnitEntity.Facing facing = getEntity().getCurrentFacing();
		if(facing==UnitEntity.Facing.right)
			location.add(getEntity().getBounds().x/2, 0);
		else
			location.add(-getEntity().getBounds().x/2, 0);
		float baseVelocity = projectileSpeed*(facing==UnitEntity.Facing.right ? 1 : -1);
		ProjectileEntity e = new ProjectileEntity(location.x, location.y, primaryBounds.x, primaryBounds.y, getEntity().getTeam(), projectileDamage, projectileDuration);
		e.setVelocity(baseVelocity + (getEntity().getVelocity().x + getEntity().getMovement().x)/2, 0);
		for(WeaponEventListener l : listeners)
			e.addListener(l);
		ans.add(e);
		// all projectiles happen in pairs
		float difference = bulletSpread/2f/projectileCount;
		System.out.println(difference);
		float runningTotal = 0;
		for(int i = 0; i < projectileCount-1; i++)
		{
			System.out.println(runningTotal);
			float sin = (float) Math.sin(Math.toRadians(bulletSpread/2f-runningTotal));
			float cos = (float) Math.cos(Math.toRadians(bulletSpread/2f-runningTotal));
			e = new ProjectileEntity(location.x, location.y, primaryBounds.x, primaryBounds.y, getEntity().getTeam(), projectileDamage, projectileDuration);
			e.setVelocity(baseVelocity*cos + (getEntity().getVelocity().x + getEntity().getMovement().x)/2, baseVelocity*sin);
			for(WeaponEventListener l : listeners)
				e.addListener(l);
			ans.add(e);
			e = new ProjectileEntity(location.x, location.y, primaryBounds.x, primaryBounds.y, getEntity().getTeam(), projectileDamage, projectileDuration);
			e.setVelocity(baseVelocity*cos + (getEntity().getVelocity().x + getEntity().getMovement().x)/2, baseVelocity*-sin);
			for(WeaponEventListener l : listeners)
				e.addListener(l);
			ans.add(e);
			runningTotal+=difference;
		}
		return ans;
	}

	public int getProjectileCount() {
		return projectileCount;
	}

	public void setProjectileCount(int projectileCount) {
		this.projectileCount = projectileCount;
		if(projectileCount<1)
			projectileCount = 1;
	}

	public float getProjectileSpeed() {
		return projectileSpeed;
	}

	public void setProjectileSpeed(float projectileSpeed) {
		this.projectileSpeed = projectileSpeed;
		if(this.projectileSpeed < 5f)
			this.projectileSpeed = 5f;
	}


	public float getProjectileDuration() {
		return projectileDuration;
	}


	public void setProjectileDuration(float projectileDuration) {
		this.projectileDuration = projectileDuration;
		if(this.projectileDuration<1)
			this.projectileDuration = 1;
	}


	public Vector2 getPrimaryBounds() {
		return primaryBounds;
	}


	public void setPrimaryBounds(Vector2 primaryBounds) {
		this.primaryBounds = primaryBounds;
		if(this.primaryBounds.x<0.125)
			this.primaryBounds.x = 0.125f;
		if(this.primaryBounds.y<0.125f)
			this.primaryBounds.y = 0.125f;
	}


	public float getBulletSpread() {
		return bulletSpread;
	}


	public void setBulletSpread(float bulletSpread) {
		this.bulletSpread = bulletSpread;
		if(this.bulletSpread<5f)
			this.bulletSpread = 5f;
	}
	
	public int getProjectileDamage() {
		return projectileDamage;
	}


	public void setProjectileDamage(int projectileDamage) {
		this.projectileDamage = projectileDamage;
		if(this.projectileDamage<1)
			this.projectileDamage = 1;
	}
	
	public String toString()
	{
		return "cooldown: " + getBaseCooldown() + " speed: " + projectileSpeed + " duration: " + projectileDuration + " count: " + projectileCount +
				" damage: " + projectileDamage + " spread: " + bulletSpread + " bounds: " + primaryBounds;
	}


	public List<WeaponEventListener> getListeners() {
		return listeners;
	}


	public void addListener(WeaponEventListener listener) {
		this.listeners.add(listener);
	}

}
