package com.zinkowin.tanyaung.views.controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainView implements Initializable {

	@FXML
	private StackPane viewHolder;
	@FXML
	private VBox VView;
	@FXML
	private MenuBar menuBar;
	@FXML
	private ImageView dashboard;
	@FXML
	private ImageView home;
	@FXML
	private ImageView category;

	@FXML
	private ImageView author;

	@FXML
	private ImageView book;

	@FXML
	private ImageView moneydonator;

	@FXML
	private ImageView bookdonator;

	@FXML
	private ImageView member;
	@FXML
	private ImageView cardregister;
	@FXML
	private ImageView cardchange;
	@FXML
	private ImageView income;
	@FXML
	private ImageView outcome;
	@FXML
	private ImageView summary;

	public static void showView() {
		try {
			Parent root = FXMLLoader.load(MainView.class.getResource("/com/zinkowin/tanyaung/views/MainView.fxml"));
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle("Library");
			stage.getIcons().add(new Image(new FileInputStream("resources/images/icon.png")));
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void menuClick() {
		menuBar.getMenus().stream().flatMap(menu -> menu.getItems().stream()).filter(item -> item instanceof MenuItem)
				.forEach(item -> {
					item.setOnAction(event -> {
						String name = item.getText().replaceAll(" ", "");

						loadView(name);
						fadeShow();

					});
				});
	}

	public void loadView(String fileName) {
		if ("Close".equals(fileName)) {
			Platform.exit();
		} 

		else {

			try {
				Parent view = FXMLLoader.load(HomeList.class.getResource("/com/zinkowin/tanyaung/views/".concat(fileName).concat(".fxml")));

				viewHolder.getChildren().clear();
				viewHolder.getChildren().add(view);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	public void buttonClick() {

		VView.getChildren().stream().filter(node -> node instanceof Button).map(node -> (Button) node)
				.forEach(button -> {
					button.setOnAction(e -> {
						String name = button.getText().replaceAll(" ", "");
						fadeShow();
						loadView(name);

					});
				});
	}

	private void fadeShow() {
		FadeTransition fade = new FadeTransition();
		fade.setDuration(Duration.millis(4000));
		fade.setNode(viewHolder);
		fade.setFromValue(0);
		fade.setToValue(1);
		fade.play();

	}
	
	private void setIconImage() throws FileNotFoundException {
		dashboard.setImage(new Image(new FileInputStream("resources/images/dashboard.png")));
		home.setImage(new Image(new FileInputStream("resources/images/home.png")));
		category.setImage(new Image(new FileInputStream("resources/images/category.png")));
		author.setImage(new Image(new FileInputStream("resources/images/author.png")));
		book.setImage(new Image(new FileInputStream("resources/images/book.png")));
		moneydonator.setImage(new Image(new FileInputStream("resources/images/donator_monthly.png")));
		bookdonator.setImage(new Image(new FileInputStream("resources/images/book_donate.png")));
		member.setImage(new Image(new FileInputStream("resources/images/member.png")));
		cardregister.setImage(new Image(new FileInputStream("resources/images/cardregist.png")));
		cardchange.setImage(new Image(new FileInputStream("resources/images/cardcg.png")));
		income.setImage(new Image(new FileInputStream("resources/images/income.png")));
		outcome.setImage(new Image(new FileInputStream("resources/images/outcome.png")));
	    summary.setImage(new Image(new FileInputStream("resources/images/summary.png")));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadView("Home");
		buttonClick();
		menuClick();
		
		try {
			setIconImage();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
