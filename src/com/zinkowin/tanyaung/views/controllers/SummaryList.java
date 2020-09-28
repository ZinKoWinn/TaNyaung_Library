package com.zinkowin.tanyaung.views.controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.zinkowin.tanyaung.factory.DateFormatedFactory;
import com.zinkowin.tanyaung.factory.DateInterval;
import com.zinkowin.tanyaung.factory.FormatToMmk;
import com.zinkowin.tanyaung.factory.PriceFormatedFactory;
import com.zinkowin.tanyaung.factory.TimePeriod;
import com.zinkowin.tanyaung.models.Summary;
import com.zinkowin.tanyaung.services.SummaryService;
import com.zinkowin.tanyaung.views.controllers.custom.KoConfirm;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class SummaryList implements Initializable {

	@FXML
	private ComboBox<TimePeriod> date;
	@FXML
	private TableView<Summary> summaryTbView;
	@FXML
	private TableColumn<Summary, Integer> incomeCol;
	@FXML
	private TableColumn<Summary, Integer> outcomeCol;
	@FXML
	private TableColumn<Summary, Integer> remainCol;
	@FXML
	private TableColumn<Summary, LocalDate> dateCol;
	@FXML
	private ImageView addimg;
	@FXML
	PieChart pie;
	@FXML
	Label title;
	@FXML
	Label lb;
	@FXML
	Label lbin;
	@FXML
	Label lbout;
	@FXML
	Label lbre;
	private SummaryService sumService;

	public void add() {
		SummaryEdit.showView(null, this::handler);
	}

	public void search() {
		TimePeriod period = date.getValue();
		DateInterval interval = new DateInterval(period);
		summaryTbView.getItems().clear();
		List<Summary> summaryList = sumService.findByParams(interval.startDate(), interval.endDate());
		summaryTbView.getItems().addAll(summaryList);
		loadChartData();
	}

	public void clear() {
		date.setValue(TimePeriod.ALL);
	}

	public void handler(Summary s) {
		search();
	}

	private void createMenu() {
		MenuItem edit = new MenuItem("_Edit");
		edit.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.ALT_DOWN));

		MenuItem delete = new MenuItem("_Delete");
		delete.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.ALT_DOWN));

		summaryTbView.setContextMenu(new ContextMenu(edit, delete));
		edit.setOnAction(e -> edit());
		delete.setOnAction(e -> delete());
	}

	private void edit() {
		KoConfirm confirm = new KoConfirm();
		confirm.setTitle("CONFIRMATION");
		confirm.setContentText("Are you sure to edit this item?");
		confirm.show();

		confirm.setOnCancled(e -> confirm.close());

		confirm.setOnConfirmed(e -> {
			Summary s = summaryTbView.getSelectionModel().getSelectedItem();
			SummaryEdit.showView(s, this::handler);
			confirm.close();
		});
	}

	private void delete() {
		KoConfirm confirm = new KoConfirm();
		confirm.setTitle("CONFIRMATION");
		confirm.setContentText("Are you sure to delete this item?");
		confirm.show();

		confirm.setOnCancled(e -> confirm.close());

		confirm.setOnConfirmed(e -> {
			Summary s = summaryTbView.getSelectionModel().getSelectedItem();
			sumService.delete(s);
			confirm.close();
			search();
		});
	}

	private void doFormatFactory() {
		incomeCol.setCellFactory(new PriceFormatedFactory<>());
		outcomeCol.setCellFactory(new PriceFormatedFactory<>());
		remainCol.setCellFactory(new PriceFormatedFactory<>());
		dateCol.setCellFactory(new DateFormatedFactory<>());
	}

	private void setIconImage() throws FileNotFoundException {
		addimg.setImage(new Image(new FileInputStream("resources/images/add.png")));
	}

	private void loadChartData() {
		pie.getData().clear();
		List<PieChart.Data> pieChartDataList = new ArrayList<>();
		int totalIncome = summaryTbView.getItems().stream().mapToInt(income -> income.getIncome()).sum();
		int totalOutCome = summaryTbView.getItems().stream().mapToInt(outcome -> outcome.getOutcome()).sum();
		int totalRemain = summaryTbView.getItems().stream().mapToInt(remain -> remain.getRemain()).sum();

		pieChartDataList.add(new PieChart.Data("InCome", totalIncome));
		pieChartDataList.add(new PieChart.Data("Expenses", totalOutCome));
		pieChartDataList.add(new PieChart.Data("Remain", totalRemain));

	    title.setText("Income & Expenses Summary For " + date.getValue());
		pie.setLabelLineLength(10);
		pie.setLegendSide(Side.BOTTOM);
		pie.getData().addAll(pieChartDataList);
		
		lbin.setText(String.valueOf(FormatToMmk.formatToMmk(totalIncome)));
		lbout.setText(String.valueOf(FormatToMmk.formatToMmk(totalOutCome)));
		lbre.setText(String.valueOf(FormatToMmk.formatToMmk(totalRemain)));
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		sumService = SummaryService.getInstance();

		date.getItems().addAll(TimePeriod.values());
		date.getSelectionModel().select(TimePeriod.THIS_MONTH);
		date.getSelectionModel().selectedItemProperty().addListener((ob, o, n) -> search());

		createMenu();
		doFormatFactory();
		try {
			setIconImage();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		search();

	}

}
