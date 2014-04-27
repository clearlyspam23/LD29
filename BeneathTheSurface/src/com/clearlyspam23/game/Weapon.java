package com.clearlyspam23.game;

import java.util.List;

public abstract class Weapon {
	
	private PlayerEntity entity;
	
	private String pickUpText;
	
	public PlayerEntity getEntity() {
		return entity;
	}
	
	public void setEntity(PlayerEntity p)
	{
		entity = p;
		onPickUp();
	}
	
	public abstract void onPickUp();
	
	public abstract void update(float delta);
	
	public abstract List<ProjectileEntity> tryShoot();

	public String getPickUpText() {
		return pickUpText;
	}

	public void setPickUpText(String pickUpText) {
		this.pickUpText = pickUpText;
	}

}
