package com.zinkowin.tanyaung.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.zinkowin.tanyaung.models.Category;
import com.zinkowin.tanyaung.utils.ApplicationException;
import com.zinkowin.tanyaung.utils.ConnectionManager;

public class CategoryService {
	private static CategoryService INSTANCE;
	private static final String insert = "insert into category (name) values (?)";
	private static final String update = "update category  name = ? where id = ?";
	private static final String delete = "delete from category where id = ?";
	private static final String find = "select * from category where 1 = 1";

	public CategoryService() {
	}

	public static CategoryService getInstance() {
		
		return INSTANCE == null ? INSTANCE = new CategoryService() : INSTANCE;
	}

	public void checkAndAdd(Category c) {
		checkingForAdd(c);
		add(c);
	}

	public void add(Category c) {
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement stmt = con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {

			stmt.setString(1, c.getName());
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			while (rs.next()) {
				c.setId(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void checkAndUpdate(Category c) {
		checkingForUpdate(c);
		update(c);
	}

	public void update(Category c) {
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement stmt = con.prepareStatement(update)) {

			stmt.setString(1, c.getName());
			stmt.setInt(2, c.getId());
			stmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete(Category c) {
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement stmt = con.prepareStatement(delete)) {
			stmt.setInt(1, c.getId());
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void checkingForAdd(Category c) {
		String checkForAdd = "select * from category where name = ? limit 1";
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement stmt = con.prepareStatement(checkForAdd)) {
			stmt.setString(1, c.getName());
			ResultSet rs = stmt.executeQuery();
			if (rs.next())
				throw new ApplicationException("This name is exits!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void checkingForUpdate(Category c) {
		String checkForUpdate = "select * from category where name = ? limit 1";
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement stmt = con.prepareStatement(checkForUpdate)) {
			stmt.setString(1, c.getName());
			ResultSet rs = stmt.executeQuery();
			if (rs.next())
				throw new ApplicationException("This name is exits!");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void upload(File file) throws IOException {
		
			Files.readAllLines(file.toPath())
			.stream()
			.map(c -> new Category(c))
			.forEach(this::checkAndAdd);
		
	}

	public List<Category> findAll() {
		String find = "select * from category where 1 = 1 ";
		List<Category> list = new ArrayList<Category>();

		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(find)) {

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				list.add(getObject(rs));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<Category> findByName(String name) {
		List<Category> list = new ArrayList<>();
		boolean isConcat = name != null && !name.isEmpty();
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement statement = conn
						.prepareStatement(isConcat ? find.concat(" and name like ?") : find)) {

			if (isConcat) {
				statement.setString(1, "%".concat(name).concat("%"));
			}
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				list.add(getObject(rs));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

	public Category getObject(ResultSet rs) throws SQLException {
		Category category = new Category();
		category.setId(rs.getInt("id"));
		category.setName(rs.getString("name"));
		return category;
	}
}
