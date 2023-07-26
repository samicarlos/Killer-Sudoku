package com.example.sudokudemo;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {
    public static void display(String title, String message) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);

        Label label= new Label();
        label.setText(message);
        label.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
        Button closeButton=new Button("Play Again");
        closeButton.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
        closeButton.setPrefWidth(160);
        closeButton.setPrefHeight(60);
        closeButton.setOnAction(e->window.close());
        VBox layout= new VBox(10);
        layout.getChildren().addAll(label,closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene= new Scene(layout,250,150);
        window.setScene(scene);
        window.showAndWait();
    }
}
