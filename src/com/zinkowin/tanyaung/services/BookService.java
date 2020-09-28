package com.zinkowin.tanyaung.services;

import com.zinkowin.tanyaung.models.Author;
import com.zinkowin.tanyaung.models.Book;
import com.zinkowin.tanyaung.models.Category;
import com.zinkowin.tanyaung.utils.ApplicationException;
import com.zinkowin.tanyaung.utils.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BookService {
    private static BookService INSTANCE;
    
      String find = "select b.id book_id,  b.name book_name, b.bookNumber book_number, b.location book_location, a.id author_id, a.name author_name, c.id category_id, c.name category_name from book b join category c on b.category_id = c.id join author a  on b.author_id = a.id where 1 = 1";
    
    public BookService(){}
    
    public static BookService getInstance(){
      
        return INSTANCE == null ? INSTANCE = new BookService() : INSTANCE;
    }
    
    public void checkAndAdd(Book b) {
    	checkingForAdd(b);
    	add(b);
    }
    
    public void add(Book b){
      String insert = "insert into book (name,bookNumber,location,author_id,category_id) value (?,?,?,?,?)";
        try( Connection con = ConnectionManager.getConnection();
           PreparedStatement stmt = con.prepareStatement(insert,Statement.RETURN_GENERATED_KEYS)){
          stmt.setString(1, b.getName());
          stmt.setString(2, b.getBookNumber());
          stmt.setString(3,b.getLocation());
          stmt.setInt(4, b.getAuthor().getId());
          stmt.setInt(5, b.getCategory().getId());
          stmt.executeUpdate();
          
          ResultSet rs = stmt.getGeneratedKeys();
			while (rs.next()) {
				b.setId(rs.getInt(1));
			}

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void checkingForAdd(Book b) {
    	String checkToAdd = "select * from book where name = ? limit 1";
    	try (Connection con = ConnectionManager.getConnection();
    			PreparedStatement stmt = con.prepareStatement(checkToAdd)){
    		stmt.setString(1, b.getName());
    		ResultSet rs = stmt.executeQuery();
    		if(rs.next()) throw new ApplicationException("This is exit! \n Please enter another name.");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    public void update(Book b){
        String update = "update book set name = ?, bookNumber = ?, location =?, author_id =?, category_id =? where id =?";
        try(Connection con = ConnectionManager.getConnection(); 
                PreparedStatement stmt = con.prepareStatement(update)){
        	stmt.setString(1, b.getName());
            stmt.setString(2, b.getBookNumber());
            stmt.setString(3,b.getLocation());
            stmt.setInt(4, b.getAuthor().getId());
            stmt.setInt(5, b.getCategory().getId());
            stmt.setInt(6, b.getId());
            stmt.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public void delete(Book b){
        String delete = "delete from book where id = ?";
        try(Connection con = ConnectionManager.getConnection();
                PreparedStatement stmt = con.prepareStatement(delete)){
            stmt.setInt(1, b.getId());
            stmt.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public List<Book> findAll(){
        return findByParams(null, null, null);
    }
    
    public List<Book> findByParams(String name,Category category,Author author){
        List<Book> bookList = new ArrayList<>();
        StringBuilder sb = new StringBuilder(find);
        List<Object> params = new LinkedList<>();

        if( null !=  name && !name.isEmpty()){
            sb.append(" and b.name like ?");
            params.add("%".concat(name).concat("%"));
        }
        
        if(null != category) {
			sb.append(" and c.name like ?");
			params.add(category.getName());
		}
        
        if( null != author ){
            sb.append(" and a.name like ?");
            params.add(author.getName());
        }
        
        try(Connection con = ConnectionManager.getConnection();
                PreparedStatement stmt = con.prepareStatement(sb.toString())){
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject( i+ 1,params.get(i));
                
            }
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
               bookList.add(getObject(rs));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookList;
    }
    
    public Book getObject(ResultSet rs) throws SQLException{
        Book b = new Book();
        b.setId(rs.getInt("book_id"));
        b.setName(rs.getString("book_name"));
        b.setBookNumber(rs.getString("book_number"));
        b.setLocation(rs.getString("book_location"));
        
        Category c = new Category();
        c.setId(rs.getInt("category_id"));
        c.setName(rs.getString("category_name"));
        
        Author a = new Author();
        a.setId(rs.getInt("author_id"));
        a.setName(rs.getString("author_name"));
       
        
        b.setCategory(c);
        b.setAuthor(a);
        
        return b;
    }
 
}
