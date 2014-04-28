package com.clearlyspam23.game;

import com.badlogic.gdx.math.Rectangle;

public interface WeaponEventListener {
	
	public void afterProjectileSpawn(ProjectileEntity e);
	
	public void projectileUpdate(ProjectileEntity e);
	
	public boolean onProjectileCollision(ProjectileEntity e, UnitEntity other);
	
	public boolean onTileCollision(ProjectileEntity e, Rectangle rect, Tile t);

}
