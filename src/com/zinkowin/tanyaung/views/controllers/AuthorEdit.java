package com.zinkowin.tanyaung.views.controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import com.zinkowin.tanyaung.models.Author;
import com.zinkowin.tanyaung.services.AuthorService;
import com.zinkowin.tanyaung.utils.ApplicationException;
import com.zinkowin.tanyaung.views.controllers.custom.KoAlert;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AuthorEdit implements Initializable {
	@FXML
	private Label title;
	@FXML
	private TextField name;
	@FXML
	private TextField age;
	@FXML
	private TextField country;
	@FXML
	private TextField totalBook;
	private static Stage stage;
	private Consumer<Author> saveHandler;
	private Author author;
	private AuthorService authorService;

	public static void showView(Author author, Consumer<Author> saveHandler) {
		try {
			FXMLLoader loader = new FXMLLoader(
					AuthorEdit.class.getResource("/com/zinkowin/tanyaung/views/AuthorEdit.fxml"));
			Parent view = loader.load();

			stage = new Stage();
			AuthorEdit controller = loader.getController();
			controller.saveHandler = saveHandler;
			controller.setData(author);

			stage.setScene(new Scene(view));
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setResizable(false);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void save() {
		boolean authorIsNull = author == null;
		try {
			if (authorIsNull) {
				author = new Author();
			}

			author.setName(name.getText());
			if (name.getText().isEmpty()) {
				throw new ApplicationException("Author's name is empty! \nPlease enter author's name");
			}

			if (age.getText().isEmpty()) {
				author.setAge("");
			}else {
				author.setAge(age.getText());
			}

			if (country.getText().isEmpty()) {
			    author.setCountry("");	
			}else {
				author.setCountry(country.getText());
			}

			
			if (totalBook.getText().isEmpty()) {
				author.setTotalBook("");
			}else {
				author.setTotalBook(totalBook.getText());
			}

			if (authorIsNull)
				authorService.checkAndAdd(author);
			else
				authorService.update(author);

			saveHandler.accept(author);
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
		age.setText("");
		country.setText("");
		totalBook.setText("");

		name.setPromptText("Enter author's name");
		age.setPromptText("Enter author's age");
		country.setPromptText("Enter author's country");
		totalBook.setPromptText("Enter totalBook");

	}

	public void close() {
		title.getScene().getWindow().hide();

	}

	public void setData(Author author) throws FileNotFoundException {
		this.author = author;
		if (null != author) {
			name.setText(author.getName());
			age.setText(String.valueOf(author.getAge()));
			country.setText(author.getCountry());
			totalBook.setText(String.valueOf(author.getTotalBook()));
			title.setText("EDIT");
			stage.setTitle("Author Edit");
			stage.getIcons().add(new Image(new FileInputStream("resources/images/edit.png")));
		} else {
			title.setText("ADD");
			stage.setTitle("Author Add");
			stage.getIcons().add(new Image(new FileInputStream("resources/images/add.png")));
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		authorService = AuthorService.getInstance();

	}

}
