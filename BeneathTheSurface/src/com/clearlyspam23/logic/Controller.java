package com.clearlyspam23.logic;

import com.clearlyspam23.game.Entity;
import com.clearlyspam23.view.EntityRenderer;

public abstract class Controller<T extends Entity> {
	
	private T t;
	private Engine engine;
	private EntityRenderer renderer;
	
	public Controller(T t, EntityRenderer renderer)
	{
		this.t = t;
		this.renderer = renderer;
	}
	
	public T getEntity()
	{
		return t;
	}
	
	public void setEntity(T t)
	{
		this.t = t;
	}
	
	public void update(float delta, float mouseX, float mouseY)
	{
		if(getEntity()==null||!getEntity().getWorld().getEntities().contains(getEntity()))
			engine.removeController(this);
		else
			control(delta, mouseX, mouseY);
	}
	
	public abstract void control(float delta, float mouseX, float mouseY);

	public Engine getEngine() {
		return engine;
	}

	public void setEngine(Engine engine) {
		this.engine = engine;
	}

	public EntityRenderer getRenderer() {
		return renderer;
	}

	public void setRenderer(EntityRenderer renderer) {
		this.renderer = renderer;
	}

}
