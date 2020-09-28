package com.zinkowin.tanyaung.views.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.zinkowin.tanyaung.factory.DateInterval;
import com.zinkowin.tanyaung.factory.TimePeriod;
import com.zinkowin.tanyaung.models.TopBorrower;
import com.zinkowin.tanyaung.services.HomeService;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class DashBoard implements Initializable{
	@FXML
	private ComboBox<TimePeriod> date;
	@FXML 
	private Label title;
	@FXML
	private PieChart pie;
	@FXML
	private BarChart<String, Integer> toptenBarChart;
	private int MAX_ITEM = 10;
	private HomeService homeService;
	
	@SuppressWarnings("unchecked")
	public void loadChartData() {
		
		TimePeriod period = date.getValue();
		DateInterval dt = new DateInterval(period);
		
		List<TopBorrower> toptenList = homeService.findTopBorrower(dt.startDate(), dt.endDate()).stream().limit(MAX_ITEM).collect(Collectors.toList());
		pie.getData().clear();
		toptenBarChart.getData().clear();
		List<XYChart.Data<String,Integer>> chartList = new ArrayList<>();
        toptenList.forEach(i->{
        	pie.getData().addAll(new PieChart.Data(i.getKey(),i.getValue()));
        	chartList.add(new XYChart.Data<>((i.getKey()),i.getValue()));
        	
        });
        title.setText("TaNyaung Library Top Reader 10 Chart");
        XYChart.Series<String,Integer> series = new XYChart.Series<>();
        series.setName(date.getValue().toString());
        series.getData().addAll(chartList);
        toptenBarChart.getData().addAll(series);
        
        
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		homeService = HomeService.getInstance();
		date.getItems().addAll(TimePeriod.values());
		date.getSelectionModel().select(TimePeriod.THIS_MONTH);
		date.getSelectionModel().selectedItemProperty().addListener((a,b,c)->loadChartData());
		loadChartData();
		
	}

}
