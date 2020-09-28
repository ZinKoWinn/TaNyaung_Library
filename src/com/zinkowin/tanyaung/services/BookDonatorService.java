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
import com.zinkowin.tanyaung.models.BookDonator;
import com.zinkowin.tanyaung.models.Category;
import com.zinkowin.tanyaung.utils.ConnectionManager;

public class BookDonatorService {

	private static BookDonatorService INSTANCE;

	public BookDonatorService() {
	}

	public static BookDonatorService getInstance() {
		return INSTANCE == null ? INSTANCE = new BookDonatorService() : INSTANCE;
	}
	

	public void add(BookDonator d) {
		String insert = "insert into book_donator (name,address,quantity,price,donate_date,book_id,book_category_id,book_author_id) values (?,?,?,?,?,?,?,?)";
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement stmt = con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, d.getName());
			stmt.setString(2, d.getAddress());
			stmt.setInt(3, d.getQuantity());
			stmt.setInt(4, d.getPrice());
			stmt.setDate(5, Date.valueOf(d.getDate()));
			stmt.setInt(6, d.getBook().getId());
			stmt.setInt(7, d.getCategory().getId());
			stmt.setInt(8, d.getAuthor().getId());
			stmt.executeUpdate();

			ResultSet rs = stmt.getGeneratedKeys();
			while (rs.next())
				d.setId(rs.getInt(1));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void update(BookDonator d) {
		String update = "update book_donator set name = ?, address = ?, quantity = ?, price = ?, donate_date = ?, book_id = ?, book_category_id = ?, book_author_id = ? where id = ?";
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement stmt = con.prepareStatement(update)) {
			stmt.setString(1, d.getName());
			stmt.setString(2, d.getAddress());
			stmt.setInt(3, d.getQuantity());
			stmt.setInt(4, d.getPrice());
			stmt.setDate(5, Date.valueOf(d.getDate()));
			stmt.setInt(6, d.getBook().getId());
			stmt.setInt(7, d.getCategory().getId());
			stmt.setInt(8, d.getAuthor().getId());
			stmt.setInt(9, d.getId());
			stmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void delete(BookDonator d) {
		String delete = "delete from book_donator where id = ?";
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement stmt = con.prepareStatement(delete)) {
			stmt.setInt(1, d.getId());
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public List<BookDonator> findAll() {
		return findByParams(null, null, null);
	}

	public List<BookDonator> findByParams(BookDonator donatorname, LocalDate startDate, LocalDate endDate) {
		String find = "select d.id donator_id,d.name donator_name,d.address donator_address,d.quantity quantity,d.price price,d.donate_date donate_date,b.id book_id,b.name book_name,c.id category_id,c.name category_name,a.id author_id,a.name author_name from book_donator d join book b on d.book_id = b.id join category c on d.book_category_id = c.id join author a on d.book_author_id = a.id where 1 = 1";
		List<BookDonator> donatorList = new ArrayList<>();
		StringBuilder sb = new StringBuilder(find);
		List<Object> params = new LinkedList<>();
		if (null != donatorname) {
			sb.append(" and d.name like ?");
			params.add(donatorname.getName());
		}
		if (null != startDate) {
			sb.append(" and d.donate_date >= ?");
			params.add(Date.valueOf(startDate));
		

		if (null != endDate) {
			sb.append(" and d.donate_date <= ?");
			params.add(Date.valueOf(endDate));
		}
		
		}else if(null != endDate) {
			sb.append(" where d.donate_date <= ?");
			params.add(endDate);
		}
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement stmt = con.prepareStatement(sb.toString())) {
			for (int i = 0; i < params.size(); i++) {
				stmt.setObject(i + 1, params.get(i));
			}
			ResultSet rs = stmt.executeQuery();
			while (rs.next())
				donatorList.add(getObject(rs));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return donatorList;
	}
	
	public BookDonator getObject(ResultSet rs) throws SQLException {
		
		BookDonator d = new BookDonator();
		d.setId(rs.getInt("donator_id"));
		d.setName(rs.getString("donator_name"));
		d.setAddress(rs.getString("donator_address"));
		d.setQuantity(rs.getInt("quantity"));
		d.setPrice(rs.getInt("price"));
		d.setDate(rs.getDate("donate_date").toLocalDate());
		Book b = new Book();
		b.setId(rs.getInt("book_id"));
		b.setName(rs.getString("book_name"));
		
		Category c = new Category();
		c.setId(rs.getInt("category_id"));
		c.setName(rs.getString("category_name"));
		
		Author a = new Author();
		a.setId(rs.getInt("author_id"));
		a.setName(rs.getString("author_name"));
		
		d.setBook(b);
		d.setCategory(c);
		d.setAuthor(a);
		
		return d;
	}

}
