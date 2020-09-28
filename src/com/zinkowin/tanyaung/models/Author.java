package com.zinkowin.tanyaung.models;

public class Author {
public Author(){
    
}
	private int id;
	private String name;
	private String age;
	private String country;
	private String totalBook;


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

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getTotalBook() {
		return totalBook;
	}

	public void setTotalBook(String totalBook) {
		this.totalBook = totalBook;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.getName();
	}

}
