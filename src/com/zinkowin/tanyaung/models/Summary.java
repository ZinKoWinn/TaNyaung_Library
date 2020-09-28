package com.zinkowin.tanyaung.models;

import java.time.LocalDate;

public class Summary {

	int id;
	String remark;
	int income;
	int outcome;
	int remain;
	LocalDate sumdate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getIncome() {
		return income;
	}

	public void setIncome(int income) {
		this.income = income;
	}

	public int getOutcome() {
		return outcome;
	}

	public void setOutcome(int outcome) {
		this.outcome = outcome;
	}

	public int getRemain() {
		return remain;
	}

	public void setRemain(int remain) {
		this.remain = remain;
	}

	public LocalDate getSumdate() {
		return sumdate;
	}

	public void setSumdate(LocalDate sumdate) {
		this.sumdate = sumdate;
	}

}
