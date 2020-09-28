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

import com.zinkowin.tanyaung.models.InCome;
import com.zinkowin.tanyaung.utils.ConnectionManager;

public class InComeService {
	
	private static InComeService INSTANCE;
	
	public InComeService() {}
	
	public static InComeService getInstance() {
		return INSTANCE == null ? INSTANCE = new InComeService() : INSTANCE;
	}
	
	public void add(InCome i) {
		String insert = "insert into Income (incomeType,incomeAmount,incomeDate,remark) values (?,?,?,?)";
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement stmt = con.prepareStatement(insert,Statement.RETURN_GENERATED_KEYS)){
			stmt.setString(1, i.getIncomeType());
			stmt.setInt(2, i.getIncomeAmount());
			stmt.setDate(3, Date.valueOf(i.getIncomeDate()));
			stmt.setString(4, i.getRemark());
			stmt.executeUpdate();
			
			ResultSet rs = stmt.getGeneratedKeys();
			while(rs.next()) i.setId(rs.getInt(1));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void update(InCome i) {
		String update = "update Income set incomeType = ?, incomeAmount = ?, incomeDate = ?, remark = ? where id = ?";;
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement stmt = con.prepareStatement(update)){
			stmt.setString(1, i.getIncomeType());
			stmt.setInt(2, i.getIncomeAmount());
			stmt.setDate(3, Date.valueOf(i.getIncomeDate()));
			stmt.setString(4, i.getRemark());
			stmt.setInt(5, i.getId());
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void delete(InCome i) {
		String delete = "delete from Income where id = ?";
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement stmt = con.prepareStatement(delete)){
			stmt.setInt(1, i.getId());
			stmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public List<InCome> findAll(){
		return findByParams(null, null);
	}
	
	public List<InCome> findByParams(LocalDate startDate, LocalDate endDate){
		String find = "select * from Income where 1 = 1";
		List<InCome> incomeList = new ArrayList<>();
		List<Object> params = new LinkedList<>();
		StringBuilder sb = new StringBuilder(find);
		
		if (null != startDate) {
			sb.append(" and incomeDate >= ?");
			params.add(Date.valueOf(startDate));

			if (null != endDate) {
				sb.append(" and incomeDate <= ?");
				params.add(Date.valueOf(endDate));
			}
		} else if (null != endDate) {
			sb.append(" where incomeDate <= ?");
			params.add(Date.valueOf(endDate));
		}
		
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement stmt = con.prepareStatement(sb.toString())){
			for (int i = 0; i < params.size(); i++) {
				stmt.setObject(i + 1, params.get(i));
			}
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) incomeList.add(getObject(rs));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return incomeList;
	}
	
	public InCome getObject(ResultSet rs)throws SQLException{
		InCome i = new InCome();
		i.setId(rs.getInt("id"));
		i.setIncomeType(rs.getString("incomeType"));
		i.setIncomeAmount(rs.getInt("incomeAmount"));
		i.setIncomeDate(rs.getDate("incomeDate").toLocalDate());
		i.setRemark(rs.getString("remark"));
		
		return i;
	}

}
