package com.zinkowin.tanyaung.models;

public class Book {
	private int id;
	private Category category;
	private Author author;
	private String bookNumber;
	private String name;
	private String location;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public String getBookNumber() {
		return bookNumber;
	}

	public void setBookNumber(String bookNumber) {
		this.bookNumber = bookNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getCategoryName() {
		return category.getName();
	}

	public String getAuthorName() {
		return author.getName();
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getName();
	}
	
}
