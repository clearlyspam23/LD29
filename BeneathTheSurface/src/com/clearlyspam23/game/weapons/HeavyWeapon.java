package com.clearlyspam23.game.weapons;

import com.badlogic.gdx.math.Rectangle;
import com.clearlyspam23.game.ProjectileEntity;
import com.clearlyspam23.game.Tile;
import com.clearlyspam23.game.UnitEntity;
import com.clearlyspam23.game.WeaponEventListener;

public class HeavyWeapon extends UpgradingWeapon implements WeaponEventListener{

	public HeavyWeapon(float cooldown, float projectileSpeed,
			float projectileDuration, int projectileCount,
			int projectileDamage, float bulletSpread, float boundsX,
			float boundsY) {
		super(cooldown, projectileSpeed, projectileDuration, projectileCount,
				projectileDamage, bulletSpread, boundsX, boundsY);
	}
	
	public void onPickUp()
	{
		super.onPickUp();
		System.out.println("pickup");
		getEntity().getPrimaryWeapon().addListener(this);
	}

	@Override
	public void afterProjectileSpawn(ProjectileEntity e) {
		e.setAcceleration(e.getAcceleration().x, e.getAcceleration().y-e.getWorld().getGravity()/2);
		System.out.println("spawned");
	}

	@Override
	public void projectileUpdate(ProjectileEntity e) {
		
	}

	@Override
	public boolean onProjectileCollision(ProjectileEntity e, UnitEntity other) {
		return false;
	}

	@Override
	public boolean onTileCollision(ProjectileEntity e, Rectangle rect, Tile t) {
		return false;
	}

}
