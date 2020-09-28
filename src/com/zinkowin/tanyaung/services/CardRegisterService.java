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

import com.zinkowin.tanyaung.models.CardRegister;
import com.zinkowin.tanyaung.utils.ApplicationException;
import com.zinkowin.tanyaung.utils.ConnectionManager;

public class CardRegisterService {

	private static CardRegisterService INSTANCE;

	public CardRegisterService() {
	}

	public static CardRegisterService getInstance() {
		return INSTANCE == null ? INSTANCE = new CardRegisterService() : INSTANCE;
	}

	public void checkAndAdd(CardRegister cr) {
		checkingForAdd(cr);
		add(cr);
	}

	public void add(CardRegister cr) {
		String insert = "insert into card_register (name,card_number,occupation,ph_number,address,price,date) values (?,?,?,?,?,?,?)";
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement stmt = con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, cr.getName());
			stmt.setString(2, cr.getCard_number());
			stmt.setString(3, cr.getOccupation());
			stmt.setString(4, cr.getPh_number());
			stmt.setString(5, cr.getAddress());
			stmt.setInt(6, cr.getPrice());
			stmt.setDate(7, Date.valueOf(cr.getDate()));
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			while (rs.next()) {
				cr.setId(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void checkingForAdd(CardRegister cr) {
		String checkToAdd = "selet * from card_register where name = ? limit 1";
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement stmt = con.prepareStatement(checkToAdd)) {
			stmt.setString(1, cr.getName());
			ResultSet rs = stmt.executeQuery();
			if (rs.next())
				throw new ApplicationException("This member name is exit! \n Please enter another name.");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void update(CardRegister cr) {
		String update = "update card_register set name = ?, card_number = ?, occupation = ?, ph_number = ?, address = ?, price = ?, date = ? where id = ?";
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement stmt = con.prepareStatement(update)) {
			stmt.setString(1, cr.getName());
			stmt.setString(2, cr.getCard_number());
			stmt.setString(3, cr.getOccupation());
			stmt.setString(4, cr.getPh_number());
			stmt.setString(5, cr.getAddress());
			stmt.setInt(6, cr.getPrice());
			stmt.setDate(7, Date.valueOf(cr.getDate()));
			stmt.setInt(8, cr.getId());
			stmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void delete(CardRegister cr) {
		String delete = "delete from card_register where id = ?";
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement stmt = con.prepareStatement(delete)) {
			stmt.setInt(1, cr.getId());
			stmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public List<CardRegister> findAll() {
		return findByParams(null, null, null, null);
	}

	public List<CardRegister> findByParams(CardRegister name, CardRegister card_number, LocalDate startDate, LocalDate endDate) {
		String find = "select * from card_register where 1 = 1";
		List<CardRegister> cardList = new ArrayList<>();
		List<Object> params = new LinkedList<>();
		StringBuilder sb = new StringBuilder(find);

		if (null != name) {
			sb.append(" and name like ?");
			params.add(name.getName());
		}

		if (null != card_number) {
			sb.append(" and card_number like ?");
			params.add(card_number.getCard_number());
		}
		if (null != startDate) {
			sb.append(" and date >= ?");
			params.add(Date.valueOf(startDate));
		

		if (null != endDate) {
			sb.append(" and date <= ?");
			params.add(Date.valueOf(endDate));
		}
		}else if(null != endDate) {
			sb.append(" where date <= ?");
			params.add(endDate);
		}
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement stmt = con.prepareStatement(sb.toString())) {
			for (int i = 0; i < params.size(); i++) {
				stmt.setObject(i + 1, params.get(i));

			}
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				cardList.add(getObject(rs));

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return cardList;
	}

	public CardRegister getObject(ResultSet rs) throws SQLException {
		CardRegister cr = new CardRegister();
		cr.setId(rs.getInt("id"));
		cr.setName(rs.getString("name"));
		cr.setCard_number(rs.getString("card_number"));
		cr.setOccupation(rs.getString("occupation"));
		cr.setPh_number(rs.getString("ph_number"));
		cr.setAddress(rs.getString("address"));
		cr.setPrice(rs.getInt("price"));
		cr.setDate(rs.getDate("date").toLocalDate());
		return cr;
	}

}
