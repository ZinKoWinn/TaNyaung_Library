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

import com.zinkowin.tanyaung.models.Summary;
import com.zinkowin.tanyaung.utils.ConnectionManager;

public class SummaryService {

	private static SummaryService INSTANCE;

	public SummaryService() {
	}

	public static SummaryService getInstance() {
		return INSTANCE == null ? INSTANCE = new SummaryService() : INSTANCE;
	}

	public void add(Summary s) {
		String insert = "insert into Summary (remark,income, outcome, remain, sumdate) values (?,?,?,?,?)";
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement stmt = con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, s.getRemark());
			stmt.setInt(2, s.getIncome());
			stmt.setInt(3, s.getOutcome());
			stmt.setInt(4, s.getRemain());
			stmt.setDate(5, Date.valueOf(s.getSumdate()));
			stmt.executeUpdate();

			ResultSet rs = stmt.getGeneratedKeys();
			while (rs.next())
				s.setId(rs.getInt(1));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void update(Summary s) {
		String update = "update Summary set remark = ?, income = ?, outcome = ?, remain = ?, sumdate = ? where id = ?";
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement stmt = con.prepareStatement(update)) {
			stmt.setString(1, s.getRemark());
			stmt.setInt(2, s.getIncome());
			stmt.setInt(3, s.getOutcome());
			stmt.setInt(4, s.getRemain());
			stmt.setDate(5, Date.valueOf(s.getSumdate()));
			stmt.setInt(6, s.getId());
			stmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete(Summary s) {
		String delete = "delete from Summary where id = ?";
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement stmt = con.prepareStatement(delete)) {
			stmt.setInt(1, s.getId());
			stmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Summary> findAll() {
		return findByParams(null, null);
	}

	public List<Summary> findByParams(LocalDate startDate, LocalDate endDate) {
		String find = "select * from Summary where 1 = 1";
		List<Summary> summaryList = new ArrayList<>();
		List<Object> params = new LinkedList<>();
		StringBuilder sb = new StringBuilder(find);

		if (null != startDate) {
			sb.append(" and sumdate >= ?");
			params.add(Date.valueOf(startDate));

			if (null != endDate) {
				sb.append(" and sumdate <= ?");
				params.add(Date.valueOf(endDate));
			}
		} else if (null != endDate) {
			sb.append(" where sumdate <= ?");
			params.add(Date.valueOf(endDate));
		}
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement stmt = con.prepareStatement(sb.toString())) {
			for (int i = 0; i < params.size(); i++) {
				stmt.setObject(i + 1, params.get(i));
			}

			ResultSet rs = stmt.executeQuery();
			while (rs.next())
				summaryList.add(getObject(rs));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return summaryList;
	}
	
	

	public Summary getObject(ResultSet rs) throws SQLException {
		Summary s = new Summary();
		s.setId(rs.getInt("id"));
		s.setRemark(rs.getString("remark"));
		s.setIncome(rs.getInt("income"));
		s.setOutcome(rs.getInt("outcome"));
		s.setRemain(rs.getInt("remain"));
		s.setSumdate(rs.getDate("sumdate").toLocalDate());
		return s;
	}

}
