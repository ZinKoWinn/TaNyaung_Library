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

import com.zinkowin.tanyaung.models.OutCome;
import com.zinkowin.tanyaung.utils.ConnectionManager;

public class OutComeService {

	private static OutComeService INSTANCE;

	public OutComeService() {
	}

	public static OutComeService getInstance() {
		return INSTANCE == null ? INSTANCE = new OutComeService() : INSTANCE;
	}

	public void add(OutCome o) {
		String insert = "insert into Outcome (content,expenses,outcome_date,remark) values (?,?,?,?)";
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement stmt = con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, o.getContent());
			stmt.setInt(2, o.getExpenses());
			stmt.setDate(3, Date.valueOf(o.getOutcomeDate()));
			stmt.setString(4, o.getRemark());
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			while (rs.next())
				o.setId(rs.getInt(1));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void update(OutCome o) {
		String update = "update Octcome set content = ?, expenses = ?, outcome_date = ?, remark = ? where id = ?";
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement stmt = con.prepareStatement(update)) {
			stmt.setString(1, o.getContent());
			stmt.setInt(2, o.getExpenses());
			stmt.setDate(3, Date.valueOf(o.getOutcomeDate()));
			stmt.setString(4, o.getRemark());
			stmt.setInt(5, o.getId());
			stmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void delete(OutCome o) {
		String delete = "delete from Outcome where id = ?";
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement stmt = con.prepareStatement(delete)) {
			stmt.setInt(1, o.getId());
			stmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public List<OutCome> findAll() {
		return null;
	}

	public List<OutCome> findByParams(LocalDate startDate, LocalDate endDate) {
		String find = "select * from Outcome where 1 = 1";
		List<OutCome> outcomeList = new ArrayList<>();
		List<Object> params = new LinkedList<>();
		StringBuilder sb = new StringBuilder(find);

		if (null != startDate) {
			sb.append(" and outcome_date >= ?");
			params.add(Date.valueOf(startDate));

			if (null != endDate) {
				sb.append(" and outcome_date <= ?");
				params.add(Date.valueOf(endDate));
			}
		} else if (null != endDate) {
			sb.append(" where outcome_date <= ?");
			params.add(Date.valueOf(endDate));
		}

		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement stmt = con.prepareStatement(sb.toString())) {
			for (int i = 0; i < params.size(); i++) {
				stmt.setObject(i + 1, params.get(i));

			}

			ResultSet rs = stmt.executeQuery();
			while (rs.next())
				outcomeList.add(getObject(rs));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return outcomeList;
	}

	public OutCome getObject(ResultSet rs) throws SQLException {
		OutCome o = new OutCome();
		o.setId(rs.getInt("id"));
		o.setContent(rs.getString("content"));
		o.setExpenses(rs.getInt("expenses"));
		o.setOutcomeDate(rs.getDate("outcome_date").toLocalDate());
		o.setRemark(rs.getString("remark"));
		return o;
	}

}
