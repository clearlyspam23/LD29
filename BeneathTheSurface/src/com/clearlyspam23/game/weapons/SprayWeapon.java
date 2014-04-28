package com.clearlyspam23.game.weapons;

import com.badlogic.gdx.math.Rectangle;
import com.clearlyspam23.game.ProjectileEntity;
import com.clearlyspam23.game.Tile;
import com.clearlyspam23.game.UnitEntity;
import com.clearlyspam23.game.WeaponEventListener;

public class SprayWeapon extends UpgradingWeapon implements WeaponEventListener{

	public SprayWeapon(float cooldown, float projectileSpeed,
			float projectileDuration, int projectileCount,
			int projectileDamage, float bulletSpread, float boundsX,
			float boundsY) {
		super(cooldown, projectileSpeed, projectileDuration, projectileCount,
				projectileDamage, bulletSpread, boundsX, boundsY);
		// TODO Auto-generated constructor stub
	}
	
	public void onPickUp()
	{
		super.onPickUp();
		getEntity().getPrimaryWeapon().addListener(this);
	}

	@Override
	public void afterProjectileSpawn(ProjectileEntity e) {
		e.setVelocity((float) (e.getVelocity().x+Math.random()*0.5f), (float)(e.getVelocity().y+Math.random()*0.5f));
	}

	@Override
	public void projectileUpdate(ProjectileEntity e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onProjectileCollision(ProjectileEntity e, UnitEntity other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onTileCollision(ProjectileEntity e, Rectangle rect, Tile t) {
		// TODO Auto-generated method stub
		return false;
	}

}
