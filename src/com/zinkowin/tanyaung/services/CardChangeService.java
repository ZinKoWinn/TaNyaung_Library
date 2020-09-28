package com.zinkowin.tanyaung.services;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.zinkowin.tanyaung.models.CardChange;
import com.zinkowin.tanyaung.utils.ConnectionManager;

public class CardChangeService {

	private static CardChangeService INSTANCE;

	public CardChangeService() {
	}

	public static CardChangeService getInstance() {
		return INSTANCE == null ? INSTANCE = new CardChangeService() : INSTANCE;
	}

	public void add(CardChange cc) {
		String insert = "insert into card_change (name,card_number,occupation,ph_number,address,price,date) values (?,?,?,?,?,?,?)";
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement stmt = con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, cc.getName());
			stmt.setString(2, cc.getCard_number());
			stmt.setString(3, cc.getOccupation());
			stmt.setString(4, cc.getPh_number());
			stmt.setString(5, cc.getAddress());
			stmt.setInt(6, cc.getPrice());
			stmt.setDate(7,Date.valueOf(cc.getDate()));
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			while (rs.next()) {
				cc.setId(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void update(CardChange cc) {
		String update = "update card_change set name = ?, card_number = ?, occupation = ?, ph_number = ?, address = ?, price = ?, date = ? where id = ?";
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement stmt = con.prepareStatement(update)) {
			stmt.setString(1, cc.getName());
			stmt.setString(2, cc.getCard_number());
			stmt.setString(3, cc.getOccupation());
			stmt.setString(4, cc.getPh_number());
			stmt.setString(5, cc.getAddress());
			stmt.setInt(6, cc.getPrice());
			stmt.setDate(7,Date.valueOf(cc.getDate()));
			stmt.setInt(8, cc.getId());
			stmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void delete(CardChange cc) {
		String delete = "delete from card_change where id = ?";
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement stmt = con.prepareStatement(delete)) {
			stmt.setInt(1, cc.getId());
			stmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public List<CardChange> findAll() {
		return findByParams(null, null,null,null);
	}

	public List<CardChange> findByParams(String name, CardChange card_number,LocalDate startDate,LocalDate endDate) {
		String find = "select * from card_change where 1 = 1";
		List<CardChange> cardList = new ArrayList<>();
		List<Object> params = new LinkedList<>();
		StringBuilder sb = new StringBuilder(find);
		
		if(null != name && !name.isEmpty()) {
			sb.append(" and name like ?");
			params.add("%".concat(name).concat("%"));
		}
		
		if(null != card_number) {
			sb.append(" and card_number like ?");
			params.add(card_number.getCard_number());
		}
		if(null != startDate) {
			sb.append(" and date >= ?");
			params.add(Date.valueOf(startDate));
		
		if(null != endDate) {
			sb.append(" and date <= ?");
			params.add(Date.valueOf(endDate));
		}
		}else if(null != endDate) {
			sb.append(" where date <=?");
			params.add(endDate);
		}
		try(Connection con = ConnectionManager.getConnection();
				PreparedStatement stmt = con.prepareStatement(sb.toString())){
			for (int i = 0; i < params.size(); i++) {
				stmt.setObject(i + 1, params.get(i));
				
			}
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				cardList.add(getObject(rs));
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cardList;
	}
	
	public CardChange getObject(ResultSet rs) throws SQLException{
		CardChange cc = new CardChange();
		cc.setId(rs.getInt("id"));
		cc.setName(rs.getString("name"));
		cc.setCard_number(rs.getString("card_number"));
		cc.setOccupation(rs.getString("occupation"));
		cc.setPh_number(rs.getString("ph_number"));
		cc.setAddress(rs.getString("address"));
		cc.setPrice(rs.getInt("price"));
		cc.setDate(rs.getDate("date").toLocalDate());
		return cc;
	}

}
