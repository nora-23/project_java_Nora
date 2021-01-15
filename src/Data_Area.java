import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.stage.Stage;
import javafx.scene.chart.XYChart.Series;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Data_Area extends Application {

    private Series<String, Number> getChartSeries(String roomType) {

        Series<String, Number> series = new XYChart.Series<>();
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

                Integer price = Integer.valueOf(data[9]);
                String roomType = data[8];
                String neighborhood = data[5];

                if (roomType.equals(neededRoomType) && Arrays.asList(neededNeighborhoods).contains(neighborhood)) {

                    Integer currentPrice = hashT.get(new Node<>(neighborhood, roomType));

                    if (currentPrice == null) {
                        hashT.add(new Node<>(neighborhood, roomType), price);
                    } else if (price < currentPrice) {
                        hashT.add(new Node<>(neighborhood, roomType), price);
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
        xAxis.setLabel("Neighborhood");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setAutoRanging(false);
        yAxis.setUpperBound(70);
        yAxis.setLabel("Price");

        StackedAreaChart<String, Number> sac = new StackedAreaChart<>(xAxis, yAxis);

        stage.setTitle("Min Price in neighborhoods for room type");
        sac.setTitle("Min Price in neighborhoods for room type");

        Series<String, Number> seriesPrivate = getChartSeries("\"Private room\"");
        sac.getData().add(seriesPrivate);


        Series<String, Number> seriesHome = getChartSeries("\"Entire home/apt\"");
        sac.getData().add(seriesHome);

        Scene scene = new Scene(sac, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}