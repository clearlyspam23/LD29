package com.clearlyspam23.game;


public interface UnitEventListener {
	
	public void onDeath(UnitEntity e);
	
	public void onJump(UnitEntity e);
	public void onHit(UnitEntity e);

}
