package com.example.cellphonesaferactivity.Beans;

public class BlackUsers 
{
	private int id;
	private String number;
	private int group;
	public BlackUsers(int id, String number, int group) {
		super();
		this.id = id;
		this.number = number;
		this.group = group;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public int getGroup() {
		return group;
	}
	public void setGroup(int group) {
		this.group = group;
	}
	
}
