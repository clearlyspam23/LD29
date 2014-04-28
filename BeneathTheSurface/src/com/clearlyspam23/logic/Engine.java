package com.clearlyspam23.logic;

import java.util.ArrayList;
import java.util.List;

import com.clearlyspam23.game.Entity;
import com.clearlyspam23.view.WorldView;

public class Engine {
	
	private List<Controller<? extends Entity>> controllers = new ArrayList<Controller<? extends Entity>>();
	private List<Controller<? extends Entity>> toBeRemoved = new ArrayList<Controller<? extends Entity>>();
	private WorldView view;
	
	private boolean isGameOver;
	
	public Engine(WorldView view)
	{
		this.view = view;
	}

	public List<Controller<? extends Entity>> getControllers() {
		return controllers;
	}

	public void addController(Controller<? extends Entity> entity){
		controllers.add(entity);
		entity.setEngine(this);
	}
	
	public void removeController(Controller<? extends Entity> entity)
	{
		toBeRemoved.add(entity);
	}
	
	public void control(float delta, float x, float y)
	{
		for(Controller<? extends Entity> c : toBeRemoved)
			controllers.remove(c);
		toBeRemoved.clear();
		for(Controller<? extends Entity> c : controllers)
		{
			c.update(delta, x, y);
		}
	}

	public WorldView getView() {
		return view;
	}

	public void setView(WorldView view) {
		this.view = view;
	}

	public boolean isGameOver() {
		return isGameOver;
	}

	public void setGameOver(boolean isGameOver) {
		this.isGameOver = isGameOver;
	}

}
