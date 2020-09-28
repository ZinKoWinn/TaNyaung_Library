package com.zinkowin.tanyaung.models;

import java.time.LocalDate;

public class BookDonator {
	private int id;
	private String name;
	private String address;
	private Book book;
	private Category category;
	private Author author;
	private int quantity;
	private int price;
	private LocalDate date;

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
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

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getPrice() {
		
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getCategoryName() {
		return category.getName();
	}

	public String getAuthorName() {
		return author.getName();
	}

	public String getBookName() {
		return book.getName();
	}

	public int getTotal() {
		return getPrice() * getQuantity();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.getName().toString();
	}
}
