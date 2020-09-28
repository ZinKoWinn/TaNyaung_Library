package com.zinkowin.tanyaung.views.controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.ResourceBundle;


import com.zinkowin.tanyaung.factory.DecimalFormatedFactory;
import com.zinkowin.tanyaung.factory.TextFormatedFactory;
import com.zinkowin.tanyaung.models.Author;
import com.zinkowin.tanyaung.services.AuthorService;
import com.zinkowin.tanyaung.utils.ConnectionManager;
import com.zinkowin.tanyaung.views.controllers.custom.KoConfirm;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

public class AuthorList implements Initializable {

	@FXML
	private TextField search_name;
	@FXML
	private TextField search_country;
	@FXML
	private ImageView addimg;
	@FXML
	private TableView<Author> authorTbView;
	@FXML
	private TableColumn<Author, Integer> noCol;
	@FXML
	private TableColumn<Author, String> ageCol;
	@FXML
	private TableColumn<Author, String> couCol;
	@FXML
	private TableColumn<Author, String> totCol;
	private AuthorService authService;

	@FXML
	public void add() {

		AuthorEdit.showView(null, this::listener);

	}

	@FXML
	public void search() {
		authorTbView.getItems().clear();
		List<Author> authorList = authService.findByParams(search_name.getText(), search_country.getText());
		authorTbView.getItems().addAll(authorList);

	}

	@FXML
	public void clear() {
		search_name.clear();
		search_country.clear();

	}
	
	public void listener(Author author) {
		search();
	}

	public void createMenu() {
		MenuItem edit = new MenuItem("_Edit");
		edit.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.ALT_DOWN));

		MenuItem delete = new MenuItem("_Delete");
		delete.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.ALT_DOWN));

		authorTbView.setContextMenu(new ContextMenu(edit, delete));
		edit.setOnAction(e -> edit());
		delete.setOnAction(d -> delete());
	}

	public void edit() {
		KoConfirm confirm = new KoConfirm();
		confirm.setTitle("CONFIRMATION");
		confirm.setContentText("Are you sure to edit this item?");
		confirm.show();

		confirm.setOnCancled(e -> confirm.close());

		confirm.setOnConfirmed(e -> {
			Author a = authorTbView.getSelectionModel().getSelectedItem();
			AuthorEdit.showView(a, this::listener);
			confirm.close();
		});
	}

	public void delete() {

		KoConfirm confirm = new KoConfirm();
		confirm.setTitle("CONFIRMATION");
		confirm.setContentText("Are you sure to delete this item?");
		confirm.show();

		confirm.setOnCancled(e -> confirm.close());

		confirm.setOnConfirmed(e -> {
			Author a = authorTbView.getSelectionModel().getSelectedItem();
			authService.delete(a);
			search();
		});

	}
	
	private void doFormatFactory() {
		noCol.setCellFactory(new DecimalFormatedFactory<>());
	    ageCol.setCellFactory(new TextFormatedFactory<>());
		ageCol.setCellFactory(new TextFormatedFactory<>());
		couCol.setCellFactory(new TextFormatedFactory<>());
		totCol.setCellFactory(new TextFormatedFactory<>());
	}

	private void setIconImage() throws FileNotFoundException {
		addimg.setImage(new Image(new FileInputStream("resources/images/memberAdd.png")));
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		authService = AuthorService.getInstance();
		search();

		search_name.textProperty().addListener((a, b, c) -> search());
		search_country.textProperty().addListener((a, b, c) -> search());
		createMenu();
		doFormatFactory();
		try {
			setIconImage();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}

	}

}
