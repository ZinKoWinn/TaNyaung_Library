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

import com.zinkowin.tanyaung.models.Member;
import com.zinkowin.tanyaung.utils.ApplicationException;
import com.zinkowin.tanyaung.utils.ConnectionManager;

public class MemberService {
	private static MemberService INSTANCE;

	public MemberService() {
	}

	public static MemberService getInstance() {
		return INSTANCE == null ? INSTANCE = new MemberService() : INSTANCE;
	}

	public void checkAndAdd(Member m) {
		checkingForAdd(m);
		add(m);
	}

	public void add(Member m) {
		String insert = "insert into library_member (name,card_number,occupation,ph_number,address,member_date) values (?,?,?,?,?,?)";
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement stmt = con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, m.getName());
			stmt.setString(2, m.getCard_number());
			stmt.setString(3, m.getOccupation());
			stmt.setString(4, m.getPh_number());
			stmt.setString(5, m.getAddress());
			stmt.setDate(6, Date.valueOf(m.getDate()));
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			while (rs.next()) {
				m.setId(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void checkingForAdd(Member m) {
		String checkToAdd = "select * from library_member where name = ? limit 1";
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement stmt = con.prepareStatement(checkToAdd)) {
			stmt.setString(1, m.getName());
			ResultSet rs = stmt.executeQuery();
			if (rs.next())
				throw new ApplicationException("This member name is exit! \nPlease enter another name.");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void update(Member m) {
		String update = "update library_member set name = ?, card_number = ?, occupation = ? , ph_number = ? , address = ? , member_date = ? where id = ?";
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement stmt = con.prepareStatement(update)) {
			stmt.setString(1, m.getName());
			stmt.setString(2, m.getCard_number());
			stmt.setString(3, m.getOccupation());
			stmt.setString(4, m.getPh_number());
			stmt.setString(5, m.getAddress());
			stmt.setDate(6, Date.valueOf(m.getDate()));
			stmt.setInt(7, m.getId());
			stmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete(Member m) {
		String delete = "delete from library_member where id = ?";
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement stmt = con.prepareStatement(delete)) {
			stmt.setInt(1, m.getId());
			stmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public List<Member> findAll() {
		return findByParams(null, null, null,null);
	}

	public List<Member> findByParams(String name, Member card_number, LocalDate startDate, LocalDate endDate) {
		String find = "select * from library_member where 1 = 1";
		List<Member> memberList = new ArrayList<>();
		List<Object> params = new LinkedList<>();
		StringBuilder sb = new StringBuilder(find);

		if (null != name && !name.isEmpty()) {
			sb.append(" and name like ?");
			params.add("%".concat(name).concat("%"));
		}

		if (null != card_number) {
			sb.append(" and card_number like ?");
			params.add(card_number.getCard_number());
		}
		if (null != startDate) {
			sb.append(" and member_date >= ?");
			params.add(Date.valueOf(startDate));
			
		if(null != endDate) {
			sb.append(" and member_date <= ?");
			params.add(endDate);
		}
		}else if(null != endDate) {
			sb.append(" where member_date <=?");
			params.add(endDate);
		}
		
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement stmt = con.prepareStatement(sb.toString())) {
			for (int i = 0; i < params.size(); i++) {
				stmt.setObject(i + 1, params.get(i));

			}
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				memberList.add(getObject(rs));

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return memberList;
	}

	public Member getObject(ResultSet rs) throws SQLException {
		Member m = new Member();
		m.setId(rs.getInt("id"));
		m.setName(rs.getString("name"));
		m.setCard_number(rs.getString("card_number"));
		m.setOccupation(rs.getString("occupation"));
		m.setPh_number(rs.getString("ph_number"));
		m.setAddress(rs.getString("address"));
		m.setDate(rs.getDate("member_date").toLocalDate());

		return m;
	}

}
