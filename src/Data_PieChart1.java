import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Data_PieChart1 extends Application {

    private HashT<String, Integer> constructData() {
        HashT<String, Integer> hashT = new HashT<>();

        String line = "";
        String neededNeighborhood = "\"Centrum-Oost\"";

        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/data_final.csv"));
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                String roomType = data[8];
                String neighborhood = data[5];

                Integer reviewCount = Integer.valueOf(data[11]);
                Integer currentCount = hashT.get(roomType);

                if (neighborhood.equals(neededNeighborhood)) {
                    if (currentCount == null) {
                        hashT.add(roomType, reviewCount);
                    } else {
                        hashT.add(roomType, reviewCount + currentCount);
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

        stage.setTitle("Centrum-Oost");
        PieChart pieChart = new PieChart();

        HashT<String, Integer> hashT = constructData();
        Node<String, Integer>[] nodes = hashT.getAllNodes();

        for (int i = 0; i < nodes.length; i++) {
            pieChart.getData().add(new PieChart.Data(nodes[i].key, nodes[i].value));
        }

        Scene scene = new Scene(pieChart, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}