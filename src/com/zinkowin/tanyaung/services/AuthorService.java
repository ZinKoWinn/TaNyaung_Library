package com.zinkowin.tanyaung.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.zinkowin.tanyaung.models.Author;
import com.zinkowin.tanyaung.utils.ApplicationException;
import com.zinkowin.tanyaung.utils.ConnectionManager;

public class AuthorService {
	private static AuthorService INSTANCE;

	public AuthorService() {

	}

	public static AuthorService getInstance() {
		
		return INSTANCE == null ? INSTANCE = new AuthorService() : INSTANCE;
	}

	public void checkAndAdd(Author a) {
		checkingForAdd(a);
		add(a);
	}

	public void add(Author a) {
		String insert = "insert into author (name,age,country,totalBook) values (?,?,?,?)";

		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
			statement.setString(1, a.getName());
			statement.setString(2, a.getAge());
			statement.setString(3, a.getCountry());
			statement.setString(4, a.getTotalBook());
			statement.executeUpdate();

			ResultSet rs = statement.getGeneratedKeys();
			while (rs.next()) {
				a.setId(rs.getInt(1));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void checkingForAdd(Author a) {
		String checkToAdd = "select * from author where name = ? limit 1";
		try(Connection con = ConnectionManager.getConnection();
				PreparedStatement stmt = con.prepareStatement(checkToAdd)) {
			stmt.setString(1, a.getName());
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) throw new ApplicationException("This author's name is exit! \nPlease enter another name.");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void update(Author a) {
		String update = "update author set name = ?, age = ?, country = ?, totalBook = ? where id = ? ";

		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(update, Statement.RETURN_GENERATED_KEYS)) {
			statement.setString(1, a.getName());
			statement.setString(2, a.getAge());
			statement.setString(3, a.getCountry());
			statement.setString(4, a.getTotalBook());
			statement.setInt(5, a.getId());
			statement.executeUpdate();

			ResultSet rs = statement.getGeneratedKeys();
			while (rs.next()) {
				a.setId(rs.getInt(1));

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete(Author a) {
		String delete = "delete from author where id = ?";

		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(delete)) {
			statement.setInt(1, a.getId());
			statement.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Author> findAll() {
		return findByParams(null, null);
	}

	public List<Author> findByParams(String name, String country) {
		String find = "select * from author where 1 = 1";

		List<Author> authorList = new ArrayList<>();
		StringBuilder sb = new StringBuilder(find);
		List<Object> params = new LinkedList<>();

		if (null != name && !name.isEmpty()) {
			sb.append(" and name like ?");
			params.add("%".concat(name).concat("%"));
		}

		if (null != country && !country.isEmpty()) {
			sb.append(" and country like ?");
			params.add("%".concat(country).concat("%"));
		}

		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(sb.toString())) {
			for (int i = 0; i < params.size(); i++) {
				statement.setObject(i + 1, params.get(i));
			}

			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				Author author = new Author();
				author.setId(rs.getInt("id"));
				author.setName(rs.getString("name"));
				author.setAge(rs.getString("age"));
				author.setCountry(rs.getString("country"));
				author.setTotalBook(rs.getString("totalBook"));
				authorList.add(author);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return authorList;

	}
}