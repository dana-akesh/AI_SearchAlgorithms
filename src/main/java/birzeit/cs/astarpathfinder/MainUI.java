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
        borderPane.setPadding(new Insets(0, 0, 0, 0));
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
        Button btn = new Button("Find Shortest Path");

        // Text areas for the results
        // A* Algorithm
        Label labelAStar = new Label("A* Algorithm");
        labelAStar.setFont(Font.font("Ariel", FontWeight.BOLD, 14));
        TextArea textAreaAStar = new TextArea();
        textAreaAStar.setPrefHeight(150);
        textAreaAStar.setPrefWidth(250);
        textAreaAStar.setEditable(false);

        // BFS Algorithm
        Label labelBFS = new Label("BFS Algorithm");
        labelBFS.setFont(Font.font("Ariel", FontWeight.BOLD, 14));
        TextArea textAreaBFS = new TextArea();
        textAreaBFS.setPrefHeight(150);
        textAreaBFS.setPrefWidth(250);
        textAreaAStar.setEditable(false);

        // Adding the nodes and roads to the map
        vBox.getChildren().addAll(listView1, listView2, btn, labelAStar, textAreaAStar, labelBFS, textAreaBFS);
        hBox.getChildren().addAll(vBox);


        // Event handler for the button
        btn.setOnAction(event -> {
            // Checking if the user selected two cities
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
                textAreaBFS.setText(bfs.calculateBFS(source, destination) + "");
                textAreaBFS.setStyle("-fx-text-fill: Black;");

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

    private void convertToXY() throws FileNotFoundException {

        File file = new File("Cities.csv");
        File XYfile = new File("XYCities.csv");

        PrintWriter printWriter = new PrintWriter(XYfile);
        Scanner scanner = new Scanner(file);
        while (file.exists() && scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] str = line.trim().split(",");

            printWriter.print(str[0] + ',');

            double y = (Double.parseDouble(str[1].trim()) / 585) * (600 + 10);
            double x = (Double.parseDouble(str[2].trim()) / 585) * (600 + 10);

            printWriter.println(x + "," + y);
        }
        printWriter.close();
    }

    // setting the x and y coordinates for each city
    private void setXY() throws FileNotFoundException {
        File file = new File("XYCities.csv");
        Scanner scanner = new Scanner(file);
        while (file.exists() && scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] str = line.trim().split(",");

            // set the x and y coordinates for each city
            cities.get(str[0]).setXCoordinate(Double.parseDouble(str[1].trim()));
            cities.get(str[0]).setYCoordinate(Double.parseDouble(str[2].trim()));
        }
    }

    // setting the nodes and roads on the map
    public void initializeMap() {
        // setting the nodes on the map
        for (Map.Entry<String, Vertex> entry : cities.entrySet()) {
            // the circle that represents the city on the map
            Circle point = new Circle(5);

            // a label that hold the city name
            Label cityName = new Label(entry.getValue().getCityName());

            // set the font size for the city name
            final double MAX_FONT_SIZE = 10.0;
            cityName.setFont(new Font(MAX_FONT_SIZE));

            entry.getValue().convertCoordinatesToPixel();

            // set circle coordinates
            point.setCenterX(entry.getValue().getXCoordinate());
            point.setCenterY(entry.getValue().getYCoordinate());

            // set label beside the circle
            cityName.setLayoutX(entry.getValue().getXCoordinate() - 10);
            cityName.setLayoutY(entry.getValue().getYCoordinate() - 10);

            System.out.println(cityName.getText() + " " + entry.getValue().getXCoordinate() + " " + entry.getValue().getYCoordinate());

            point.setFill(Color.RED);

            // setting city circle to the circle above
            entry.getValue().setCircle(point);

            // add the circle and the label to the scene
            pane.getChildren().addAll(point, cityName);

        }
    }

    public static Image getImg() {
        return img;
    }
}
