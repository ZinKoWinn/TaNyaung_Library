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

import com.zinkowin.tanyaung.models.Author;
import com.zinkowin.tanyaung.models.Book;
import com.zinkowin.tanyaung.models.CardRegister;
import com.zinkowin.tanyaung.models.Category;
import com.zinkowin.tanyaung.models.Home;
import com.zinkowin.tanyaung.models.Member;
import com.zinkowin.tanyaung.models.TopBorrower;
import com.zinkowin.tanyaung.utils.ConnectionManager;

public class HomeService {
	private static HomeService INSTANCE;
	
	String find = "select bb.id book_borrow_id, bb.return_book book_return,bb.date_from date_from,bb.date_to date_to,m.id member_id,m.name member_name,m.card_number member_cardnumber,b.id book_id,b.name book_name,c.id category_id,c.name category_name,a.id author_id,a.name author_name,r.id register_id,r.card_number register_cardnumber from book_borrow bb join library_member m on bb.library_member_id = m.id join book b on bb.book_id = b.id join category c on bb.book_category_id = c.id join author a on bb.book_author_id = a.id join card_register r on bb.card_register_id = r.id where 1 = 1";
	
	public HomeService() {
	}

	public static HomeService getInstance() {
		return INSTANCE == null ? INSTANCE = new HomeService() : INSTANCE;
	}

	public void add(Home h) {
		String insert = "insert into book_borrow (return_book,date_from,date_to,library_member_id,book_id,book_category_id,book_author_id,card_register_id) values (?,?,?,?,?,?,?,?)";
		try (Connection con = ConnectionManager.getConnection();
				 PreparedStatement stmt = con.prepareStatement(insert,Statement.RETURN_GENERATED_KEYS)){
	         stmt.setString(1, h.getRepay());
			stmt.setDate(2, Date.valueOf(h.getFrom()));
			stmt.setDate(3, Date.valueOf(h.getTo()));
			stmt.setInt(4, h.getMember().getId());
			stmt.setInt(5, h.getBook().getId());
			stmt.setInt(6, h.getCategory().getId());
			stmt.setInt(7, h.getAuthor().getId());
			stmt.setInt(8, h.getCardRegister().getId());
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			while (rs.next()) {
				h.setId(rs.getInt(1));

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void update(Home h) {
		String update = "update book_borrow set return_book = ?, date_from = ?, date_to = ?, library_member_id = ?, book_id = ?, book_category_id = ?, book_author_id = ?, card_register_id = ? where id = ?";
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement stmt = con.prepareStatement(update)) {
			stmt.setString(1, h.getRepay());
			stmt.setDate(2, Date.valueOf(h.getFrom()));
			stmt.setDate(3, Date.valueOf(h.getTo()));
			stmt.setInt(4, h.getMember().getId());
			stmt.setInt(5, h.getBook().getId());
			stmt.setInt(6, h.getCategory().getId());
			stmt.setInt(7, h.getAuthor().getId());
			stmt.setInt(8, h.getCardRegister().getId());
			stmt.setInt(9, h.getId());
			stmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete(Home h) {
		String delete = "delete from book_borrow where id = ?";
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement stmt = con.prepareStatement(delete)) {
			stmt.setInt(1, h.getId());
			stmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public List<Home> findAll() {
		return findByParams(null, null, null, null,null);
	}

	public List<Home> findByParams(Member name, CardRegister cardnumber, LocalDate from, LocalDate to,String ov) {
		ArrayList<Home> homeList = new ArrayList<>();
		StringBuilder sb = new StringBuilder(find);
		List<Object> params = new LinkedList<>();

		if (null != name) {
			sb.append(" and m.name like ?");
			params.add(name.getName());
		}

		if (null != cardnumber) {
			sb.append("and r.card_number like ?");
			params.add(cardnumber.getCard_number());
		}

		if (null != from) {
			sb.append(" and bb.date_from >= ?");
			params.add(Date.valueOf(from));
		}
		
		if(null != to) {
			sb.append("and bb. date_to<=?");
			params.add(Date.valueOf(to));
		}
		
		if(null != ov) {
			sb.append(" and bb.return_book like ? ");
			params.add(ov);
		}
		
//		if(null != from && from.isBefore(to)) {
//			sb.append(" and bb.date_from >= ?");
//			params.add(Date.valueOf(from));
//		}
//		
//		if(null != to && to.isAfter(from)) {
//			sb.append(" and bb.date_to <= ?");
//			params.add(Date.valueOf(to));
//		}

		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement stmt = con.prepareStatement(sb.toString())) {
			for (int i = 0; i < params.size(); i++) {
				stmt.setObject(i + 1, params.get(i));
			}
				ResultSet rs = stmt.executeQuery();
			
				while (rs.next()) {
					homeList.add(getObject(rs));
				

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return homeList;
	}
	
	
	public List<TopBorrower> AllTop() {
		return findTopBorrower(null, null);
	}
	
	public List<TopBorrower> findTopBorrower( LocalDate from, LocalDate to) {
		String findTop ="select m.name , count(m.id)  from book_borrow b join library_member m on b.library_member_id = m.id where 1 = 1 ";
	    List<TopBorrower> topList = new ArrayList<>();
	    StringBuilder sb = new StringBuilder(findTop);
	    List<Object> params = new LinkedList<>();
	   // select m.name , count(*),b.date_from from book_borrow b join library_member m on b.library_member_id = m.id where 1 = 1 and 9/7/2020
		if (null != from) {
			sb.append(" and b.date_from >= ? ");
			params.add(Date.valueOf(from));
		}
		if(null != to) {
			sb.append("and b. date_from <=? ");
			params.add(Date.valueOf(to));
		}
		
		
		sb.append(" group by m.name order by count(m.id) desc ");
		
		try(Connection conn = ConnectionManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sb.toString())) {
			
			for (int i = 0; i < params.size(); i++) {
				stmt.setObject(i + 1, params.get(i));
			}
			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				TopBorrower top = new TopBorrower();
				top.setKey(rs.getString("m.name"));
				top.setValue(rs.getInt("count(m.id)"));
				topList.add(top);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return topList;
	}

	public Home getObject(ResultSet rs) throws SQLException {
		Home h = new Home();
		h.setId(rs.getInt("book_borrow_id"));
		h.setRepay(rs.getString("book_return"));
		h.setFrom(rs.getDate("date_from").toLocalDate());
		h.setTo(rs.getDate("date_to").toLocalDate());

		Member m = new Member();
		m.setId(rs.getInt("member_id"));
		m.setName(rs.getString("member_name"));

		Book b = new Book();
		b.setId(rs.getInt("book_id"));
		b.setName(rs.getString("book_name"));

		Category c = new Category();
		c.setId(rs.getInt("category_id"));
		c.setName(rs.getString("category_name"));

		Author a = new Author();
		a.setId(rs.getInt("author_id"));
		a.setName(rs.getString("author_name"));
		
		CardRegister r = new CardRegister();
		r.setId(rs.getInt("register_id"));
		r.setCard_number(rs.getString("register_cardnumber"));
		

		h.setMember(m);
		h.setBook(b);
		h.setCategory(c);
		h.setAuthor(a);
		h.setCardRegister(r);
		return h;
	}
}