import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Data_Scatter extends Application {

    private HashT<Integer, String> constructData() {
        HashT<Integer, String> hashT = new HashT<>();

        String line = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/data_final.csv"));
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                Integer price = Integer.valueOf(data[9]);
                String neighborhood = data[5];
                hashT.add(price, neighborhood);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hashT;
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Scatter Chart");

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Neighborhood");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Price");

        ScatterChart<String, Number> sc = new ScatterChart<>(xAxis, yAxis);
        sc.setTitle("Price relationship");

        Series series = new Series();

        HashT<Integer, String> hashT = constructData();
        Node[] nodes = hashT.getAllNodes();

        for (int i = 0; i < nodes.length; i++) {
            series.getData().add(new XYChart.Data(nodes[i].value, nodes[i].key));
        }

        sc.getData().add(series);

        Scene scene = new Scene(sc, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}