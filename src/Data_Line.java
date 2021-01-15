import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.stage.Stage;

import java.io.BufferedReader;

import javafx.scene.chart.XYChart.Series;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Data_Line extends Application {

    private Series<String, Number> getChartSeries(String roomType) {

        Series<String, Number> series = new Series<>();
        series.setName(roomType);

        HashT<Node<String, String>, Integer> hashTPrivate = constructData(roomType);
        Node<Node<String, String>, Integer>[] nodes_P = hashTPrivate.getAllNodes();
        for (int i = 0; i < nodes_P.length; i++) {
            series.getData().add(new XYChart.Data<>(nodes_P[i].key.key, nodes_P[i].value));
        }
        return series;
    }

    private HashT<Node<String, String>, Integer> constructData(String neededRoomType) {

        HashT<Node<String, String>, Integer> hashT = new HashT<>();
        String[] neededNeighborhoods = {"\"Centrum-West\"", "\"Centrum-Oost\"", "\"Oud-Noord\"", "\"Westerpark\"", "\"De Baarsjes - Oud-West\"", "\"De Pijp - Rivierenbuurt\""};

        String line = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/data_final.csv"));
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                Integer reviewCount = Integer.valueOf(data[11]);
                String roomType = data[8];
                String neighborhood = data[5];


                if (roomType.equals(neededRoomType) && Arrays.asList(neededNeighborhoods).contains(neighborhood)) {

                    Integer currentCount = hashT.get(new Node<>(neighborhood, roomType));
                    if (currentCount == null) {
                        hashT.add(new Node<>(neighborhood, roomType), reviewCount);
                    } else {
                        hashT.add(new Node<>(neighborhood, roomType), reviewCount + currentCount);
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hashT;
    }

    @Override
    public void start(Stage stage) {

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Neighborhoods");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Review count");


        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Review count by neighborhood");

        Series seriesPrivate = getChartSeries("\"Private room\"");
        lineChart.getData().add(seriesPrivate);


        Series seriesHome = getChartSeries("\"Entire home/apt\"");
        lineChart.getData().add(seriesHome);

        Scene scene = new Scene(lineChart, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}