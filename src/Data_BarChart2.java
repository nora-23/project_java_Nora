import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Data_BarChart2 extends Application {

    private HashT<String, Integer> constructData() {
        HashT<String, Integer> hashT = new HashT<>();

        String line = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/data_final.csv"));
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                String neighborhood = data[5];
                Integer reviewCount = Integer.valueOf(data[11]);

                Integer currentCount = hashT.get(neighborhood);
                if (currentCount == null) {
                    hashT.add(neighborhood, reviewCount);
                } else {
                    hashT.add(neighborhood, reviewCount + currentCount);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hashT;
    }

    public void start(Stage stage) {
        stage.setTitle("BarChart 2");

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Neighborhood");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Review Count");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Review count by neighborhood");

        Series series = new Series();

        HashT<String, Integer> hashT = constructData();
        Node<String, Integer>[] nodes = hashT.getAllNodes();

        for (int i = 0; i < nodes.length; i++) {
            series.getData().add(new XYChart.Data(nodes[i].key, nodes[i].value));
        }

        barChart.getData().add(series);

        Scene scene = new Scene(barChart, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}