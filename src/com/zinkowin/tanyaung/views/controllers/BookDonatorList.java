package com.zinkowin.tanyaung.views.controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import com.zinkowin.tanyaung.factory.DateFormatedFactory;
import com.zinkowin.tanyaung.factory.DateInterval;
import com.zinkowin.tanyaung.factory.DecimalFormatedFactory;
import com.zinkowin.tanyaung.factory.PriceFormatedFactory;
import com.zinkowin.tanyaung.factory.TimePeriod;
import com.zinkowin.tanyaung.models.BookDonator;
import com.zinkowin.tanyaung.services.BookDonatorService;
import com.zinkowin.tanyaung.views.controllers.custom.KoConfirm;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BookDonatorList implements Initializable {

	@FXML
	private ComboBox<BookDonator> name;
	@FXML
	private ComboBox<TimePeriod> date;
	@FXML
	private ImageView addimg;
	@FXML
	private TableView<BookDonator> bookDonatorTbView;
	@FXML
	private TableColumn<BookDonator, Integer> quantityCol;
	@FXML
	private TableColumn<BookDonator, Integer> priceCol;
	@FXML
	private TableColumn<BookDonator, Integer> totalCol;
	@FXML
	private TableColumn<BookDonator, LocalDate> dateCol;
	
	private BookDonatorService bookDonatorService;

	public void add() {
		BookDonatorEdit.showView(null, this::listener);

	}

	public void search() {
		TimePeriod period = date.getValue();
		DateInterval interval = new DateInterval(period);
		bookDonatorTbView.getItems().clear();
		List<BookDonator> donatorList = bookDonatorService.findByParams(name.getValue(),interval.startDate(),interval.endDate());
		bookDonatorTbView.getItems().addAll(donatorList);
	}

	public void listener(BookDonator donator) {
		search();
	}

	public void clear() {
		name.setValue(null);
		date.setValue(TimePeriod.ALL);
		
		name.setPromptText("Donator Name");
		date.setPromptText("Find by date");
	

	}

	public void createMenu() {
		MenuItem edit = new MenuItem("Edit");
		MenuItem delete = new MenuItem("Delete");

		bookDonatorTbView.setContextMenu(new ContextMenu(edit, delete));
		edit.setOnAction(e -> edit());
		delete.setOnAction(d -> delete());

	}

	public void edit() {
		KoConfirm confirm = new KoConfirm();
		confirm.setTitle("CONFIRMATION");
		confirm.setContentText("Are you sure to edit this item?");
		confirm.show();
		
		confirm.setOnCancled(event->confirm.close());
		
		confirm.setOnConfirmed(e->{
			BookDonator d = bookDonatorTbView.getSelectionModel().getSelectedItem();
			BookDonatorEdit.showView(d, this::listener);
			confirm.close();
		});
		

	}

	public void delete() {
		KoConfirm confirm = new KoConfirm();
		confirm.setTitle("CONFIRMATION");
		confirm.setContentText("Are you sure to delete this item?");
		confirm.show();
		
		confirm.setOnCancled(event->confirm.close());
		
		confirm.setOnConfirmed(e->{
			BookDonator d = bookDonatorTbView.getSelectionModel().getSelectedItem();
			bookDonatorService.delete(d);
			confirm.close();
			search();
		});
		

	}

	private void doFormatFactory() {
		quantityCol.setCellFactory(new DecimalFormatedFactory<>());
		priceCol.setCellFactory(new PriceFormatedFactory<>());
		totalCol.setCellFactory(new PriceFormatedFactory<>());
		dateCol.setCellFactory(new DateFormatedFactory<>());
	}
	
	private void setIconImage() throws FileNotFoundException {
		addimg.setImage(new Image(new FileInputStream("resources/images/add-friend.png")));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		bookDonatorService = BookDonatorService.getInstance();
		name.getItems().addAll(bookDonatorService.findAll());
		date.getItems().addAll(TimePeriod.values());
        date.getSelectionModel().select(TimePeriod.THIS_MONTH);
		date.getSelectionModel().selectedItemProperty().addListener((a,b,c)->search());
		name.valueProperty().addListener((ob, o, n) -> search());
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
