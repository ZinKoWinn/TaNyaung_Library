package com.zinkowin.tanyaung.views.controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import com.zinkowin.tanyaung.models.Author;
import com.zinkowin.tanyaung.models.Book;
import com.zinkowin.tanyaung.models.BookDonator;
import com.zinkowin.tanyaung.models.Category;
import com.zinkowin.tanyaung.services.AuthorService;
import com.zinkowin.tanyaung.services.BookDonatorService;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BookDonatorEdit implements Initializable {
	@FXML
	private Label title;
	@FXML
	private TextField name;
	@FXML
	private TextField address;
	@FXML
	private ComboBox<Category> category;
	@FXML
	private ComboBox<Author> author;
	@FXML
	private ComboBox<Book> book;
	@FXML
	private TextField quantity;
	@FXML
	private TextField price;
	@FXML
	private DatePicker date;
	private CategoryService catService;
	private AuthorService authService;
	private BookService bookService;
	private BookDonator donator;
	private static Stage stage;
	private Consumer<BookDonator> handler;
	private BookDonatorService bookDonatorService;

	public static void showView(BookDonator donator, Consumer<BookDonator> handler) {
		try {
			FXMLLoader loader = new FXMLLoader(
					BookDonatorEdit.class.getResource("/com/zinkowin/tanyaung/views/BookDonatorEdit.fxml"));
			Parent view = loader.load();
			stage = new Stage();
			BookDonatorEdit control = loader.getController();
			control.handler = handler;
			control.setData(donator);
			stage.setScene(new Scene(view));
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setResizable(false);
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void save() {
		boolean donatorIsNull = donator == null;
		try {
			if (donatorIsNull)
				donator = new BookDonator();

			donator.setName(name.getText());
			if (name.getText().isEmpty())
				throw new ApplicationException("Donator's name is empty! \nPlease enter donator's name.");

			donator.setAddress(address.getText());
			if (address.getText().isEmpty())
				throw new ApplicationException("Donator's address is empty! \nPlease enter donator's address.");

			donator.setCategory(category.getValue());
			if (null == category.getValue())
				throw new ApplicationException("Category's name is empty! \nPlease select category name.");

			donator.setAuthor(author.getValue());
			if (null == author.getValue())
				throw new ApplicationException("Author's name is empty! \nPlease select author's name.");

			donator.setBook(book.getValue());
			if (null == book.getValue())
				throw new ApplicationException("Book's name is empty! \nPlease select book name.");

			if (quantity.getText().isEmpty())
				throw new ApplicationException("Quantity is empty! \nPlease enter quantity.");

			donator.setQuantity(Integer.valueOf(quantity.getText()));

			if (price.getText().isEmpty())
				throw new ApplicationException("Book's price is empty! \nPlease enter book price.");

			donator.setPrice(Integer.valueOf(price.getText()));

			donator.setDate(date.getValue());
			if (null == date.getValue()) {
				throw new ApplicationException("Donation date is empty! \nPlease select doantion date.");

			}

			if (donatorIsNull)
				bookDonatorService.add(donator);
			else
				bookDonatorService.update(donator);

			handler.accept(donator);
			close();

		} catch (Exception e) {
			KoAlert alert = new KoAlert();
			alert.setTitle("WARNING");
			alert.setContentText(e.getMessage());
			alert.show();
		}

	}

	public void clear() {
		name.setText("");
		address.setText("");
		quantity.setText("");
		price.setText("");
		category.setValue(null);
		author.setValue(null);
		book.setValue(null);
		date.setValue(null);

		name.setPromptText("Enter Donator Name");
		address.setPromptText("Enter Address");
		quantity.setPromptText("Enter Quantity");
		price.setPromptText("Enter Price");
		category.setPromptText("Please Select Category Name!");
		author.setPromptText("Please Select Author Name!");
		book.setPromptText("Please select Book Name!");
		date.setPromptText("Please Select Date!");
	}

	public void close() {
		name.getScene().getWindow().hide();
	}

	public void setData(BookDonator donator) throws FileNotFoundException {
		this.donator = donator;
		if (null != donator) {
			name.setText(donator.getName());
			address.setText(donator.getAddress());
			category.setValue(donator.getCategory());
			author.setValue(donator.getAuthor());
			book.setValue(donator.getBook());
			quantity.setText(String.valueOf(donator.getQuantity()));
			price.setText(String.valueOf(donator.getPrice()));
			date.setValue(donator.getDate());
			title.setText("EDIT");
			stage.setTitle("Book Donator Edit");
			stage.getIcons().add(new Image(new FileInputStream("resources/images/edit.png")));
		} else {
			title.setText("ADD");
			stage.setTitle("Book Donator Add");
			stage.getIcons().add(new Image(new FileInputStream("resources/images/add.png")));

		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		catService = CategoryService.getInstance();
		authService = AuthorService.getInstance();
		bookService = BookService.getInstance();
		bookDonatorService = BookDonatorService.getInstance();
		category.getItems().addAll(catService.findAll());
		author.getItems().addAll(authService.findAll());
		book.getItems().addAll(bookService.findAll());

	}

}
