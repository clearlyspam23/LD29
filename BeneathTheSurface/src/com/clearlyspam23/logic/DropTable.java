package com.clearlyspam23.logic;

import java.util.ArrayList;
import java.util.List;

public class DropTable {
	
	private List<TableEntry> table = new ArrayList<TableEntry>();
	
	public void addEntry(float chance, Object object)
	{
		table.add(new TableEntry(chance, object));
	}
	
	public Object getValue(float value)
	{
		for(TableEntry e : table)
		{
			value-=e.chance;
			if(value<=0)
				return e.item;
		}
		return null;
	}
	
	public Object getValue()
	{
		return getValue((float) (Math.random()*100));
	}
	
	private class TableEntry
	{
		public float chance;
		public Object item;
		
		//this table assumes values between 0 and 100
		
		public TableEntry(float chance, Object item)
		{
			this.item = item;
			this.chance = chance;
		}
		
	}

}
