package com.clearlyspam23.game.weapons;

import java.util.List;

import com.clearlyspam23.game.ProjectileEntity;
import com.clearlyspam23.game.Weapon;

public abstract class BasicWeapon extends Weapon{
	
	private float cooldown;
	private float baseCooldown;
	
	public List<ProjectileEntity> tryShoot()
	{
		if(cooldown<=0)
		{
			cooldown = baseCooldown;
			return shoot();
		}
		return null;
	}
	
	public abstract List<ProjectileEntity> shoot();
	
	public float getCooldown() {
		return cooldown;
	}

	public void setCooldown(float cooldown) {
		this.cooldown = cooldown;
	}
	
	public void update(float delta)
	{
		if(cooldown>0)
			cooldown-=delta;
	}

	public float getBaseCooldown() {
		return baseCooldown;
	}

	public void setBaseCooldown(float baseCooldown) {
		this.baseCooldown = baseCooldown;
	}

}
