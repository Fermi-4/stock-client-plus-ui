package com.fermilabz.demo.stockui;

import com.fermilabz.demo.stockclient.StockClient;
import com.fermilabz.demo.stockclient.StockPrice;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

import static javafx.collections.FXCollections.observableArrayList;

@Component
public class ChartController {

    @FXML
    public LineChart<String, Double> chart;
    private StockClient stockClient;

    public ChartController(StockClient stockClient) {
        this.stockClient = stockClient;
    }

    @FXML
    public void initialize() {
        String symbol1 = "AGRX";
        final PriceSubscriber priceSubscriber1 = new PriceSubscriber( symbol1 );
        stockClient.pricesFor( symbol1 ).subscribe( priceSubscriber1 );

//        String symbol2 = "AGRX";
//        final PriceSubscriber priceSubscriber2 = new PriceSubscriber( symbol2 );
//        stockClient.pricesFor( symbol2 ).subscribe( priceSubscriber2 );

        ObservableList<Series<String, Double>> data = observableArrayList();
        data.add( priceSubscriber1.getSeries() );
//        data.add( priceSubscriber2.getSeries() );
        chart.setData( data );
    }



    private class PriceSubscriber implements Consumer<StockPrice> {
        private final Series<String, Double> series;
        private final String symbol;
        private final ObservableList<XYChart.Data<String, Double>> seriesData = observableArrayList();

        public PriceSubscriber(String symbol) {
            this.symbol = symbol;
            series = new Series<>( symbol, seriesData );
        }

        @Override
        public void accept(StockPrice stockPrice) {
            Platform.runLater(() -> {
                seriesData.add( new XYChart.Data<>( String.valueOf(stockPrice.getTime().getSecond()), stockPrice.getPrice()));
            });
        }

        public Series<String, Double> getSeries() {
            return series;
        }
    }
}
