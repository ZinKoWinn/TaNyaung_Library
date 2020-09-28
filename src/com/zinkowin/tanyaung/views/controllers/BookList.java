package com.zinkowin.tanyaung.views.controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.zinkowin.tanyaung.factory.TextFormatedFactory;
import com.zinkowin.tanyaung.models.Author;
import com.zinkowin.tanyaung.models.Book;
import com.zinkowin.tanyaung.models.Category;
import com.zinkowin.tanyaung.services.AuthorService;
import com.zinkowin.tanyaung.services.BookService;
import com.zinkowin.tanyaung.services.CategoryService;
import com.zinkowin.tanyaung.views.controllers.custom.KoConfirm;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class BookList implements Initializable {
	@FXML
	private ComboBox<Category> search_category;
	@FXML
	private ComboBox<Author> search_author;
	@FXML
	private TextField search_bookName;
	@FXML
	private ImageView addimg;

	@FXML
	private TableView<Book> bookTbView;
	@FXML
	private TableColumn<Book, String> numberCol;
	@FXML
	private TableColumn<Book, String> locationCol;
	
	private AuthorService authorService;
	private BookService bookService;
	private CategoryService categoryService;

	public void add() {
		BookEdit.showView(null, this::listener);
	}

	public void search() {
		bookTbView.getItems().clear();
		List<Book> bookList = bookService.findByParams(search_bookName.getText(), search_category.getValue(),
				search_author.getValue());
		bookTbView.getItems().addAll(bookList);
	}

	public void save() {

	}

	public void clear() {

		search_category.setValue(null);
		search_author.setValue(null);

		search_bookName.setText("");

		search_category.setPromptText("Category");
		search_author.setPromptText("Author");
		search_bookName.setPromptText("Enter Book Name");

	}

	public void listener(Book book) {
		search();
	}

	public void createMenu() {
		MenuItem edit = new MenuItem("_Edit");
		edit.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.ALT_DOWN));
		
		MenuItem delete = new MenuItem("_Delete");
        delete.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.ALT_DOWN));
        
		bookTbView.setContextMenu(new ContextMenu(edit, delete));
		edit.setOnAction(e -> edit());
		delete.setOnAction(d -> delete());

	}

	public void edit() {
		KoConfirm confirm = new KoConfirm();
		confirm.setTitle("CONFIRMATION");
		confirm.setContentText("Are you sure to edit this item?");
		confirm.show();

		confirm.setOnCancled(event -> confirm.close());

		confirm.setOnConfirmed(event -> {
			Book b = bookTbView.getSelectionModel().getSelectedItem();
			BookEdit.showView(b, this::listener);
			confirm.close();
		});

	}

	public void delete() {
		KoConfirm confirm = new KoConfirm();
		confirm.setTitle("CONFIRMATION");
		confirm.setContentText("Are you sure to delte this item?");
		confirm.show();

		confirm.setOnConfirmed(event -> {
			Book b = bookTbView.getSelectionModel().getSelectedItem();
			bookService.delete(b);
			confirm.close();
			search();
		});

		confirm.setOnCancled(event -> confirm.close());

	}
	
	private void doFormatFactory() {
		numberCol.setCellFactory(new TextFormatedFactory<>());
	}
	
	private void setIconImage() throws FileNotFoundException {
		addimg.setImage(new Image(new FileInputStream("resources/images/add.png")));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		authorService = AuthorService.getInstance();
		bookService = BookService.getInstance();
		categoryService = CategoryService.getInstance();

		search_author.getItems().addAll(authorService.findAll());
		search_category.getItems().addAll(categoryService.findAll());

		search_category.valueProperty().addListener((a, b, c) -> search());
		search_author.valueProperty().addListener((a, b, c) -> search());
		search_bookName.textProperty().addListener((a, b, c) -> search());

		search();
		createMenu();
		doFormatFactory();
		try {
			setIconImage();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
