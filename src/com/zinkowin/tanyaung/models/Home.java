package com.zinkowin.tanyaung.models;

import java.time.LocalDate;

public class Home {
	int id;
	String repay;
	LocalDate from;
	LocalDate to;
	Member member;
	Book book;
	Category category;
	Author author;
	CardRegister cardRegister;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRepay() {
		return repay;
	}

	public void setRepay(String repay) {
		this.repay = repay;
	}

	public LocalDate getFrom() {
		return from;
	}

	public void setFrom(LocalDate from) {
		this.from = from;
	}

	public LocalDate getTo() {
		return to;
	}

	public void setTo(LocalDate to) {
		this.to = to;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
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

	public CardRegister getCardRegister() {
		return cardRegister;
	}

	public void setCardRegister(CardRegister cardRegister) {
		this.cardRegister = cardRegister;
	}

	public String getName() {
		return member.getName();
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

	public String getCardNumber() {
		return cardRegister.getCard_number();
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getRepay();
	}


}