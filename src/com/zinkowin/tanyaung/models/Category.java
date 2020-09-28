package com.zinkowin.tanyaung.models;

public class Category {

	private int id;
	private String name;
	
	public Category(String name) {
		super();
		this.name = name;
	}
	public Category() {
		
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.getName();
	}

}
