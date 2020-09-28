package com.zinkowin.tanyaung.models;

import java.time.LocalDate;

public class MoneyDonator {

	int id;
	String name;
	String address;
	int donation;
	LocalDate donate_date;
	String remark;

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

	public int getDonation() {
		return donation;
	}

	public void setDonation(int donation) {
		this.donation = donation;
	}

	public LocalDate getDonate_date() {
		return donate_date;
	}

	public void setDonate_date(LocalDate donate_date) {
		this.donate_date = donate_date;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {

		return this.getName();
	}

}
