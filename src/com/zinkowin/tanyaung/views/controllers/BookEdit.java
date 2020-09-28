package com.zinkowin.tanyaung.views.controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import com.zinkowin.tanyaung.models.Author;
import com.zinkowin.tanyaung.models.Book;
import com.zinkowin.tanyaung.models.Category;
import com.zinkowin.tanyaung.services.AuthorService;
import com.zinkowin.tanyaung.services.BookService;
import com.zinkowin.tanyaung.services.CategoryService;
import com.zinkowin.tanyaung.utils.ApplicationException;
import com.zinkowin.tanyaung.views.controllers.custom.KoAlert;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BookEdit implements Initializable {
	@FXML
	private Label title;

	@FXML
	private ComboBox<Category> category;

	@FXML
	private ComboBox<Author> author;

	@FXML
	private TextField bookname;
	
	@FXML
	private TextField number;


	@FXML
	private TextField location;

	private CategoryService catService;
	private AuthorService authService;
	private BookService bookService;
	private Book book;
	private static Stage stage;
	private Consumer<Book> handler;

	public static void showView(Book book, Consumer<Book> handler) {
		try {
			FXMLLoader loader = new FXMLLoader(BookEdit.class.getResource("/com/zinkowin/tanyaung/views/BookEdit.fxml"));
			Parent view = loader.load();

			stage = new Stage();
			BookEdit control = loader.getController();
			control.handler = handler;
			control.setData(book);

			stage.setScene(new Scene(view));
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setResizable(false);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void save() {

		boolean bookIsNull = book == null;

		try {
			if (bookIsNull) {
				book = new Book();
			}

			book.setCategory(category.getValue());
			if (null == category.getValue()) {
				throw new ApplicationException("Category name is empty! \nPlease select a category!");
			}

			book.setAuthor(author.getValue());
			if (null == author.getValue()) {
				throw new ApplicationException("Author name is empty! \nPlease select a author!");
			}

			book.setName(bookname.getText());
			if (bookname.getText().isEmpty()) {
				throw new ApplicationException("Book name is empty! \nPlease enter book name!");
			}
			
			book.setBookNumber(number.getText());
			if(number.getText().isEmpty()) {
				throw new ApplicationException("Book number is empty! \nPlease enter book number.");
			}

			book.setLocation(location.getText());
			if(location.getText().isEmpty()) {
		    	throw new ApplicationException("Book's location is empty! \nPlease enter book's location.");
			}
			//book.setLocation(Integer.valueOf(location.getText()));
			if (bookIsNull)
				bookService.checkAndAdd(book);
			else
				bookService.update(book);
			
			handler.accept(book);
			close();
			
		} catch (Exception e) {
			KoAlert alert = new KoAlert();
			alert.setTitle("WARNING");
			alert.setContentText(e.getMessage());
			alert.show();
		}
		
	

	}

	public void close() {
		title.getScene().getWindow().hide();

	}

	public void clear() {
		category.setValue(null);
		author.setValue(null);
		bookname.setText("");
		location.setText("");

		category.setPromptText("Category");
		author.setPromptText("Author");
		bookname.setPromptText("Enter Book Name");
		location.setPromptText("Enter Price");

	}

	public void setData(Book book) throws FileNotFoundException {
		this.book = book;

		if (null != book) {
			category.setValue(book.getCategory());
			author.setValue(book.getAuthor());
			bookname.setText(book.getName());
			number.setText(book.getBookNumber());
			location.setText(String.valueOf(book.getLocation()));
			title.setText("EDIT");
			stage.setTitle("Edit");
			stage.getIcons().add(new Image(new FileInputStream("resources/images/edit.png")));
		} else {
			title.setText("ADD");
			stage.setTitle("ADD");
			stage.getIcons().add(new Image(new FileInputStream("resources/images/add.png")));
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		catService = CategoryService.getInstance();
		authService = AuthorService.getInstance();
		bookService = BookService.getInstance();
		List<Category> catList = catService.findAll();
		category.getItems().addAll(catList);

		List<Author> authList = authService.findAll();
		author.getItems().addAll(authList);

	}

}
