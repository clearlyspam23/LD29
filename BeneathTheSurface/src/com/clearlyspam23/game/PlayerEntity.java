package com.clearlyspam23.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.clearlyspam23.game.weapons.PrimaryWeapon;

public class PlayerEntity extends GroundUnitEntity{
	
	private PrimaryWeapon primaryWeapon;
	private List<Weapon> weapons;
	
	private int maxHealth;

	public PlayerEntity(float locx, float locy, float boundsx, float boundsy, int health) {
		super(locx, locy, boundsx, boundsy, 0, health);
		weapons = new ArrayList<Weapon>();
		this.setMaxHealth(health);
	}
	
	public void update(float delta)
	{
		super.update(delta);
		for(Weapon w : weapons)
			w.update(delta);
	}

	public PrimaryWeapon getPrimaryWeapon() {
		return primaryWeapon;
	}

	public void setPrimaryWeapon(PrimaryWeapon primaryWeapon) {
		this.primaryWeapon = primaryWeapon;
	}
	
	public void onDeath()
	{
		System.out.println("thou art dead!");
	}

	public List<Weapon> getWeapons() {
		return weapons;
	}

	public void addWeapon(Weapon weapon) {
		weapons.add(weapon);
		weapon.setEntity(this);
	}
	
	public Map<Weapon, List<ProjectileEntity>> shoot()
	{
		Map<Weapon, List<ProjectileEntity>> ans = new HashMap<Weapon, List<ProjectileEntity>>();
		for(Weapon w : weapons)
		{
			List<ProjectileEntity> projectiles = w.tryShoot();
			if(projectiles!=null)
				ans.put(w, projectiles);
					//getWorld().addEntity(pe);
		}
		return ans;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

}
