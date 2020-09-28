package com.zinkowin.tanyaung.models;

import java.util.List;

public class BorrowDto {
	private Member member;

	private List<Home> homeDetail;

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public List<Home> getHomeDetail() {
		return homeDetail;
	}

	public void setHomeDetail(List<Home> homeDetail) {
		this.homeDetail = homeDetail;
	}


}
