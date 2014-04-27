package com.clearlyspam23.game.weapons;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.clearlyspam23.game.ProjectileEntity;
import com.clearlyspam23.game.Weapon;

public class UpgradingWeapon extends Weapon{
	
	private float cooldown;
	private int projectileCount;
	private int projectileDamage;
	private float projectileSpeed;
	private float projectileDuration;
	private Vector2 primaryBounds;
	private float bulletSpread;
	
	public UpgradingWeapon(float cooldown, float projectileSpeed, float projectileDuration, int projectileCount, int projectileDamage, float bulletSpread, float boundsX, float boundsY)
	{
		this.cooldown = cooldown;
		this.projectileSpeed = projectileSpeed;
		this.projectileDuration = projectileDuration;
		this.projectileCount = projectileCount;
		this.projectileDamage = projectileDamage;
		this.bulletSpread = bulletSpread;
		primaryBounds = new Vector2(boundsX, boundsY);
	}

	@Override
	public void onPickUp() {
		PrimaryWeapon w = getEntity().getPrimaryWeapon();
		w.setBaseCooldown(w.getBaseCooldown()*(1-cooldown));
		w.setProjectileSpeed(w.getProjectileSpeed()+projectileSpeed);
		w.setProjectileDuration(w.getProjectileDuration()+projectileDuration);
		w.setProjectileCount(w.getProjectileCount()+projectileCount);
		w.setProjectileDamage(w.getProjectileDamage()+projectileDamage);
		w.setBulletSpread(w.getBulletSpread()*(1-bulletSpread));
		w.getPrimaryBounds().add(primaryBounds);
	}

	@Override
	public void update(float delta) {
		
	}

	@Override
	public List<ProjectileEntity> tryShoot() {
		return null;
	}
	
	public String toString()
	{
		return "cooldown change: " + cooldown + " speed change: " + projectileSpeed + " duration change: " + projectileDuration + " count change: " + projectileCount +
				" damage change: " + projectileDamage + " spread change: " + bulletSpread + " bounds change: " + primaryBounds;
	}

}
