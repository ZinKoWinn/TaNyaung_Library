package com.zinkowin.tanyaung.models;

import java.time.LocalDate;

public class OutCome {

	int id;
	String content;
	int expenses;
	LocalDate outcomeDate;
	String remark;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getExpenses() {
		return expenses;
	}

	public void setExpenses(int expenses) {
		this.expenses = expenses;
	}

	public LocalDate getOutcomeDate() {
		return outcomeDate;
	}

	public void setOutcomeDate(LocalDate outcomeDate) {
		this.outcomeDate = outcomeDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
