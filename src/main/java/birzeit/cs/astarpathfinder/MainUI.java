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
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MainUI {
    private static final Image img = new Image("palestine_map.png");
    private final Map<String, Vertex> cities = Driver.getCities();
    private final AnchorPane pane = new AnchorPane();
    private Stage stage;

    public MainUI() throws FileNotFoundException {
        // Border pane is the main pane for the UI
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(0, 0, 10, 0));
        borderPane.setStyle("-fx-background-color: #f6fbff");


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
        HBox hBox = new HBox(30);
        hBox.setAlignment(Pos.CENTER);
        borderPane.setCenter(hBox);

        // setting
        ImageView imageView = new ImageView(img);
        imageView.setFitHeight(771);
        imageView.setFitWidth(753);
        pane.getChildren().addAll(imageView);
        hBox.getChildren().addAll(pane);
        pane.setPrefSize(imageView.getFitWidth(), imageView.getFitHeight());
        initializeMap();

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
        Button FindPathBtn = new Button("Find Shortest Path");

        // Button for clearing the map
        Button clearBtn = new Button("Clear Map");
        clearBtn.setDisable(true);

        // Text areas for the results
        // A* Algorithm
        Label labelAStar = new Label("A* Algorithm");
        labelAStar.setFont(Font.font("Ariel", FontWeight.BOLD, 14));
        labelAStar.setStyle("-fx-text-fill: BLUE; -fx-font-weight: bold;");
        TextArea textAreaAStar = new TextArea();
        textAreaAStar.setPrefHeight(150);
        textAreaAStar.setPrefWidth(250);
        textAreaAStar.setEditable(false);

        // BFS Algorithm
        Label labelBFS = new Label("BFS Algorithm");
        labelBFS.setFont(Font.font("Ariel", FontWeight.BOLD, 14));
        labelBFS.setStyle("-fx-text-fill: GREEN;-fx-font-weight: bold;");
        TextArea textAreaBFS = new TextArea();
        textAreaBFS.setPrefHeight(150);
        textAreaBFS.setPrefWidth(250);
        textAreaAStar.setEditable(false);

        // Adding the nodes and roads to the map
        vBox.getChildren().addAll(listView1, listView2, FindPathBtn, labelAStar, textAreaAStar, labelBFS, textAreaBFS, clearBtn);
        hBox.getChildren().addAll(vBox);


        // Event handler for the button
        FindPathBtn.setOnAction(event -> {
            // Checking if the user selected two cities
            if (listView1.getValue() == null || listView2.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("Please select two cities");
                alert.showAndWait();
            } else {
                // clearing the map
                pane.getChildren().clear();
                pane.getChildren().addAll(imageView);
                initializeMap();

                // getting the source and destination cities
                Vertex source = listView1.getValue();
                Vertex destination = listView2.getValue();

                // A* Algorithm
                AStar aStar = new AStar();
                aStar.calculateAStar(source, destination);
                double aStarValue = AStar.calculateTotalCost(destination);
                textAreaAStar.setText(aStar.printPath(destination) + "\n" + "Total Cost: " + aStarValue);
                textAreaAStar.setStyle("-fx-text-fill: Black;");


                // BFS Algorithm
                BFS bfs = new BFS();
                List<Vertex> path = bfs.calculateBFS(source, destination);
                double bfsValue = bfs.calculateTotalCost(path);
                textAreaBFS.setText(bfs.calculateBFS(source, destination) + "\n" + "Total Cost: " + bfsValue);
                textAreaBFS.setStyle("-fx-text-fill: Black;");

                // drawing the paths
                drawPath(aStar.getvVrticesUI(), path);
                FindPathBtn.setDisable(true);
                clearBtn.setDisable(false);
            }
        });

        clearBtn.setOnAction(event -> {
            // clearing the map
            pane.getChildren().clear();
            pane.getChildren().addAll(imageView);
            textAreaBFS.clear();
            textAreaAStar.clear();
            initializeMap();
            FindPathBtn.setDisable(false);
            clearBtn.setDisable(true);
        });

        // Creating the scene and setting the stage
        Scene scene = new Scene(borderPane);
        stage = new Stage();
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle("Path Finder");
    }

    public static Image getImg() {
        return img;
    }

    public Stage getStage() {
        return stage;
    }

    // setting the nodes and roads on the map
    public void initializeMap() {
        // setting the nodes on the map
        for (Map.Entry<String, Vertex> entry : cities.entrySet()) {
            // the circle that represents the city on the map
            Circle point = new Circle(10);

            // a label that hold the city name
            Label cityName = new Label(entry.getValue().getCityName());

            // set the font size for the city name
            final double MAX_FONT_SIZE = 12.0;
            cityName.setFont(new Font(MAX_FONT_SIZE));
            cityName.setStyle("-fx-font-weight: bold;");

            entry.getValue().convertCoordinatesToPixel();

            // set circle coordinates
            point.setCenterX(entry.getValue().getXCoordinate());
            point.setCenterY(entry.getValue().getYCoordinate());

            // set label beside the circle
            cityName.setLayoutX(entry.getValue().getXCoordinate() - 10);
            cityName.setLayoutY(entry.getValue().getYCoordinate() - 10);

            // set circle color
            point.setFill(Color.RED);

            // setting city circle to the circle above
            entry.getValue().setCircle(point);

            // add the circle and the label to the scene
            pane.getChildren().addAll(point, cityName);

        }
    }

    // setting the roads on the map
    private void drawPath(ArrayList<Vertex> aStarPaths, List<Vertex> bfsPaths) {
        // drawing the paths

        while (aStarPaths.size() > 1) {
            Vertex v1 = aStarPaths.get(0);
            Vertex v2 = aStarPaths.get(1);
            Line line = new Line(v1.getXCoordinate(), v1.getYCoordinate(), v2.getXCoordinate(), v2.getYCoordinate());
            line.setStroke(Color.BLUE);
            line.setStrokeWidth(3);
            pane.getChildren().add(line);
            aStarPaths.remove(0);
        }

        while (bfsPaths.size() > 1) {
            Vertex v1 = bfsPaths.get(0);
            Vertex v2 = bfsPaths.get(1);
            Line line = new Line(v1.getXCoordinate() + 5, v1.getYCoordinate() + 5, v2.getXCoordinate() + 5, v2.getYCoordinate() + 5);
            line.setStroke(Color.GREEN);
            line.setStrokeWidth(3);
            pane.getChildren().add(line);
            bfsPaths.remove(0);
        }
    }
}
