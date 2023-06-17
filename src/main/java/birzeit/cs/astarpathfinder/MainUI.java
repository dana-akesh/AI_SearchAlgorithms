package birzeit.cs.astarpathfinder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class MainUI {
    private Stage stage;
    private Image img = new Image("palestine_map.png");
    private Map<String, Vertex> cities = Driver.getCities();
    private StackPane pane = new StackPane();

    public MainUI() throws FileNotFoundException {
        toXY();
        setXY();

        // Border pane is the main pane for the UI
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(0, 0, 0, 0));
        borderPane.setStyle("-fx-background-color: #f6fbff");
        initalizeMap();

        // Label for the title
        Label label = new Label("Palestine Cities Shortest Path");
        label.setAlignment(Pos.CENTER);
        label.setPadding(new Insets(20, 0, 0, 0));
        label.setFont(Font.font("Ariel", FontWeight.BOLD, 40));
        label.setStyle("-fx-text-fill: Black");
        // HBox for the title
        HBox lblHB = new HBox(500);
        lblHB.getChildren().addAll(new Label(""), label);
        borderPane.setTop(lblHB);

        // HBox for the buttons
        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.CENTER);
        borderPane.setCenter(hBox);
        hBox.setStyle("-fx-background-color: transparent");

        // setting
        ImageView imageView = new ImageView(img);
        pane.getChildren().addAll(imageView);
        hBox.getChildren().addAll(pane);

        // VBox for the two combo boxes ,text areas ,and the button
        VBox vBox = new VBox(10);
        vBox.setAlignment(Pos.CENTER);

        // Combo boxes for the cities
        ObservableList<Vertex> items = FXCollections.observableArrayList(cities.values());
        ComboBox<Vertex> listView1 = new ComboBox<>();
        listView1.setItems(items);
        ComboBox<Vertex> listView2 = new ComboBox<>();
        listView2.setItems(items);

        // Button for finding the shortest path
        Button btn = new Button("Find Shortest Path");

        // Text areas for the results
        // A* Algorithm
        Label labelAStar = new Label("A* Algorithm");
        TextArea textAreaAStar = new TextArea();
        textAreaAStar.setPrefHeight(250);
        textAreaAStar.setPrefWidth(250);
        textAreaAStar.setEditable(false);

        // BFS Algorithm
        Label labelBFS = new Label("BFS Algorithm");
        TextArea textAreaBFS = new TextArea();
        textAreaBFS.setPrefHeight(250);
        textAreaBFS.setPrefWidth(250);
        textAreaAStar.setEditable(false);

        // Adding the nodes and roads to the map
        vBox.getChildren().addAll(listView1, listView2, btn, labelAStar, textAreaAStar, labelBFS, textAreaBFS);
        hBox.getChildren().addAll(vBox);

        // Event handler for the button
        btn.setOnAction(event -> {
            if (listView1.getValue() == null || listView2.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("Please select two cities");
                alert.showAndWait();
            } else {
                Vertex source = listView1.getValue();
                Vertex destination = listView2.getValue();

                // A* Algorithm
                AStar aStar = new AStar();
                aStar.calculateAStar(source, destination);
                textAreaAStar.setText(aStar.printPath(destination));
                textAreaAStar.setStyle("-fx-text-fill: Black;");

                // BFS Algorithm
                BFS bfs = new BFS();
                textAreaBFS.setText(bfs.calculateBFS(source, destination)+"");
                textAreaBFS.setStyle("-fx-text-fill: Black;");

                // todo: add the path to the map (nodes and roads)
                // todo: calculate path cost
            }
        });

        // Creating the scene and setting the stage
        Scene scene = new Scene(borderPane);
        stage = new Stage();
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle("Path Finder");
    }

    public Stage getStage() {
        return stage;
    }

    private void toXY() throws FileNotFoundException {

        File file = new File("Cities.csv");
        File XYfile = new File("XYCities.csv");

        PrintWriter printWriter = new PrintWriter(XYfile);
        Scanner scanner = new Scanner(file);
        while (file.exists() && scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] str = line.trim().split(",");

            printWriter.print(str[0] + ',');

            //x = image. width * (longitude + 180) / (2 * 180)
            printWriter.print(img.getWidth() * (Double.parseDouble(str[2].trim()) + 180) / (2 * 180));
            //y = image. height * (latitude + 180) / (2 * 180)
            printWriter.println("," + img.getHeight() * (Double.parseDouble(str[1].trim()) + 180) / (2 * 180));
        }
        printWriter.close();
    }

    private void setXY() throws FileNotFoundException {
        File file = new File("XYCities.csv");
        Scanner scanner = new Scanner(file);
        while (file.exists() && scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] str = line.trim().split(",");

            cities.get(str[0]).setXCoordinate(Double.parseDouble(str[1].trim()));
            cities.get(str[0]).setYCoordinate(Double.parseDouble(str[2].trim()));
        }
    }

    public void initalizeMap() {
        for (Map.Entry<String, Vertex> entry : cities.entrySet()) {
            // the circle that represents the city on the map
            Circle point = new Circle(10);

            // a label that hold the city name
            Label cityName = new Label(entry.getKey());

            final double MAX_FONT_SIZE = 9.0;
            cityName.setFont(new Font(MAX_FONT_SIZE));

            // set circle coordinates
            point.setCenterX(entry.getValue().getXCoordinate());
            point.setCenterY(entry.getValue().getYCoordinate());

            // set label beside the circle
            cityName.setLayoutX(entry.getValue().getYCoordinate() - 10);
            cityName.setLayoutY(entry.getValue().getXCoordinate() - 10);

            point.setFill(Color.RED);
            Tooltip tooltip = new Tooltip(entry.getValue().toString());
            tooltip.setAutoFix(true);
            Tooltip.install(point, tooltip);

            // setting city circle to the circle above
            entry.getValue().setCircle(point);

            // add the circle and the label to the scene
            pane.getChildren().addAll(entry.getValue().getCircle(), cityName);

        }
    }


}
