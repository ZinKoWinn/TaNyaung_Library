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

import com.zinkowin.tanyaung.models.MoneyDonator;
import com.zinkowin.tanyaung.utils.ConnectionManager;

public class MoneyDonatorService {

	private static MoneyDonatorService INSTANCE;
	String find = "select * from money_donator where 1 = 1";

	public MoneyDonatorService() {
	}

	public static MoneyDonatorService getInstance() {
		return INSTANCE == null ? INSTANCE = new MoneyDonatorService() : INSTANCE;
	}

	public void add(MoneyDonator m) {
		String insert = "insert into money_donator (name,address,donation,donate_date,remark) values (?,?,?,?,?)";
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement stmt = con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, m.getName());
			stmt.setString(2, m.getAddress());
			stmt.setInt(3, m.getDonation());
			stmt.setDate(4, Date.valueOf(m.getDonate_date()));
			stmt.setString(5, m.getRemark());
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			while (rs.next())
				m.setId(rs.getInt(1));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void update(MoneyDonator m) {
		String update = "update money_donator set name = ?, address = ?, donation = ?, donate_date = ?, remark = ? where id = ?";
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement stmt = con.prepareStatement(update)) {
			stmt.setString(1, m.getName());
			stmt.setString(2, m.getAddress());
			stmt.setInt(3, m.getDonation());
			stmt.setDate(4, Date.valueOf(m.getDonate_date()));
			stmt.setString(5, m.getRemark());
			stmt.setInt(6, m.getId());
			stmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void delete(MoneyDonator m) {
		String delete = "delete from money_donator where id = ?";
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement stmt = con.prepareStatement(delete)) {
			stmt.setInt(1, m.getId());
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public List<MoneyDonator> findAll() {
		return findByParams(null, null, null, null);
	}

	public List<MoneyDonator> findByParams(MoneyDonator name, String remark, LocalDate startDate, LocalDate endDate) {
		List<MoneyDonator> moneyDonatorList = new ArrayList<>();
		List<Object> params = new LinkedList<>();
		StringBuilder sb = new StringBuilder(find);

		if (null != name) {
			sb.append(" and name like ?");
			params.add(name);
		}

		if (null != remark) {
			sb.append(" and remark like ?");
			params.add(remark);
		}

		if (null != startDate) {
			sb.append(" and donate_date >= ?");
			params.add(Date.valueOf(startDate));

			if (null != endDate) {
				sb.append(" and donate_date <= ?");
				params.add(Date.valueOf(endDate));
			}
		} else if (null != endDate) {
			sb.append(" where donate_date <= ?");
			params.add(endDate);
		}

		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement stmt = con.prepareStatement(sb.toString())) {
			for (int i = 0; i < params.size(); i++) {
				stmt.setObject(i + 1, params.get(i));
			}
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				moneyDonatorList.add(getObject(rs));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return moneyDonatorList;
	}

	public MoneyDonator getObject(ResultSet rs) throws SQLException {

		MoneyDonator m = new MoneyDonator();
		m.setId(rs.getInt("id"));
		m.setName(rs.getString("name"));
		m.setAddress(rs.getString("address"));
		m.setDonation(rs.getInt("donation"));
		m.setDonate_date(rs.getDate("donate_date").toLocalDate());
		m.setRemark(rs.getString("remark"));
		return m;
	}

}
