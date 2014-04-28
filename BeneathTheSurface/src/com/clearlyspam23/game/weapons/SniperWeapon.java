package com.clearlyspam23.game.weapons;

import com.badlogic.gdx.math.Rectangle;
import com.clearlyspam23.game.ProjectileEntity;
import com.clearlyspam23.game.Tile;
import com.clearlyspam23.game.UnitEntity;
import com.clearlyspam23.game.WeaponEventListener;

public class SniperWeapon extends UpgradingWeapon implements WeaponEventListener{
	
	private int count = 1;

	public SniperWeapon(float cooldown, float projectileSpeed,
			float projectileDuration, int projectileCount,
			int projectileDamage, float bulletSpread, float boundsX,
			float boundsY) {
		super(cooldown, projectileSpeed, projectileDuration, projectileCount,
				projectileDamage, bulletSpread, boundsX, boundsY);
	}
	
	public void onPickUp()
	{
		super.onPickUp();
		getEntity().getPrimaryWeapon().addListener(this);
		count = getEntity().getPrimaryWeapon().getListeners().contains(this) ? 2 : 1;
	}

	@Override
	public void afterProjectileSpawn(ProjectileEntity e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void projectileUpdate(ProjectileEntity e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onProjectileCollision(ProjectileEntity e, UnitEntity other) {
		return e.getHitEntities().size()<=count;
	}

	@Override
	public boolean onTileCollision(ProjectileEntity e, Rectangle rect, Tile t) {
		// TODO Auto-generated method stub
		return false;
	}

}
