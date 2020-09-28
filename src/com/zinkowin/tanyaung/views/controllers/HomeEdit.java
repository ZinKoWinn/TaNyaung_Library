package com.zinkowin.tanyaung.views.controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import com.zinkowin.tanyaung.models.Author;
import com.zinkowin.tanyaung.models.Book;
import com.zinkowin.tanyaung.models.CardRegister;
import com.zinkowin.tanyaung.models.Category;
import com.zinkowin.tanyaung.models.Home;
import com.zinkowin.tanyaung.models.Member;
import com.zinkowin.tanyaung.services.AuthorService;
import com.zinkowin.tanyaung.services.BookService;
import com.zinkowin.tanyaung.services.CardRegisterService;
import com.zinkowin.tanyaung.services.CategoryService;
import com.zinkowin.tanyaung.services.HomeService;
import com.zinkowin.tanyaung.services.MemberService;
import com.zinkowin.tanyaung.utils.ApplicationException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HomeEdit implements Initializable {
	@FXML
	private Label title;
	@FXML
	private ComboBox<Member> membername;
	@FXML
	private ComboBox<CardRegister> cardnumber;
	@FXML
	private ComboBox<Category> category;
	@FXML
	private ComboBox<Author> author;
	@FXML
	private ComboBox<Book> book;
	@FXML
	private DatePicker from;
	@FXML
	private DatePicker to;
	@FXML
	private ComboBox<String> remark;

	private HomeService homeService;
	private MemberService memberService;
	private CategoryService categoryService;
	private BookService bookService;
	private AuthorService authorService;
	private CardRegisterService cardService;
	private Home home;
	private static Stage stage;
	private Consumer<Home> saveHandler;

	public static void showView(Home home, Consumer<Home> saveHandler) {
		try {
			FXMLLoader loader = new FXMLLoader(HomeEdit.class.getResource("/com/zinkowin/tanyaung/views/HomeEdit.fxml"));
			Parent view = loader.load();

			stage = new Stage();
			HomeEdit controller = loader.getController();
			controller.saveHandler = saveHandler;
			controller.setData(home);

			stage.setScene(new Scene(view));
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void save() {

		boolean homeIsNull = home == null;

		try {
			if (homeIsNull) {
				home = new Home();
			}

			home.setMember(membername.getValue());
			if (null == membername.getValue()) {
				throw new ApplicationException("Please select member name!");
			}
			
			home.setCardRegister(cardnumber.getValue());
			if (null == cardnumber.getValue()) {
				throw new ApplicationException("Please select card number!");
			}
			
			home.setCategory(category.getValue());
			if (null == category.getValue()) {
				throw new ApplicationException("Please select category name!");
			}

			home.setAuthor(author.getValue());
			if (null == author.getValue()) {
				throw new ApplicationException("Please select author!");
			}

			home.setBook(book.getValue());
			if (null == book.getValue()) {
				throw new ApplicationException("Please select book!");
			}

			home.setFrom(from.getValue());
			if (null == from.getValue()) {
				throw new ApplicationException("Please select date from!");
			}

			home.setTo(to.getValue());
			if (null == to.getValue()) {
				throw new ApplicationException("Please select date to!");
			}
			
			home.setRepay(remark.getValue());
			if (null == remark.getValue()) {
				throw new ApplicationException("Please select Return Type!");
			}

			if (homeIsNull)
				homeService.add(home);
			else
				homeService.update(home);

			saveHandler.accept(home);
			close();

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setContentText(e.getMessage());
			alert.show();
			
		}

	}

	public void clear() {
		membername.setValue(null);
		cardnumber.setValue(null);
		category.setValue(null);
		author.setValue(null);
		book.setValue(null);
		from.setValue(LocalDate.now());
		to.setValue(LocalDate.now());
		remark.setValue(null);

		membername.setPromptText("Member Name");
		cardnumber.setPromptText("Card Number");
		category.setPromptText("Category Name");
		author.setPromptText("Author Name");
		book.setPromptText("Book Name");
		from.setPromptText("Date From");
		to.setPromptText("Date To");
		remark.setPromptText("Remark");
	}

	public void close() {
		title.getScene().getWindow().hide();

	}

	public void setData(Home home) throws FileNotFoundException {
		this.home = home;
		if (null != home) {
			remark.setValue(home.getRepay());
			from.setValue(home.getFrom());
			to.setValue(home.getTo());
			author.setValue(home.getAuthor());
			book.setValue(home.getBook());
			category.setValue(home.getCategory());
			membername.setValue(home.getMember());
			cardnumber.setValue(home.getCardRegister());
			title.setText("EDIT");
			stage.setTitle("Book Borrow Edit");
			stage.getIcons().add(new Image(new FileInputStream("resources/images/edit.png")));
		} else {
			title.setText("ADD");
			stage.setTitle("Book Borrow Add");
			stage.getIcons().add(new Image(new FileInputStream("resources/images/add.png")));
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		homeService = HomeService.getInstance();
		memberService = MemberService.getInstance();
		authorService = AuthorService.getInstance();
		bookService = BookService.getInstance();
		categoryService = CategoryService.getInstance();
		cardService = CardRegisterService.getInstance();
		
		membername.getItems().addAll(memberService.findAll());
		cardnumber.getItems().addAll(cardService.findAll());
		category.getItems().addAll(categoryService.findAll());
		author.getItems().addAll(authorService.findAll());
		book.getItems().addAll(bookService.findAll());
		remark.getItems().addAll("Rented","Returned","Over Date");


	}

}