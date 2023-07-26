package com.example.sudokudemo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class SudokuDemo extends Application {

    Stage window;
    Button playerButton, solveButton, generateButton, clearButton, groupsButton, showColorsButton, saveButton, hintButton, notesButton;
    Label mistakeCounter;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        window = stage;
        window.setTitle("Killer Sudoku Demo");

        HBox topMenu = new HBox(10);
        topMenu.setAlignment(Pos.CENTER);
        playerButton=new Button("Show Player's Board");
        solveButton = new Button("Solve");
        clearButton = new Button("Clear");
        AtomicInteger mistakes = new AtomicInteger(); //mistakes counter
        mistakeCounter = new Label(mistakes.get() + "/3");
        groupsButton = new Button("Show Groups");
        showColorsButton = new Button("Show Colors");
        topMenu.getChildren().addAll(groupsButton, showColorsButton, playerButton, solveButton, clearButton, mistakeCounter);

        GridPane centerMenu = new GridPane();
        centerMenu.setPadding(new Insets(10, 10, 10, 10));
        centerMenu.setAlignment(Pos.CENTER);

        //initiere
        int[][] emptyArray = new int[9][9];
        Game sudoku = new Game(emptyArray, emptyArray, emptyArray, emptyArray);
        sudoku.emptySolvedBoard();
        sudoku.emptyPlayerBoard();
        sudoku.emptyColoredBoard();
        sudoku.emptyGroupsBoard();

        TextField[][] textField = new TextField[9][9];
        String[][] style = new String[9][9];
        Label[][] labels = new Label[9][9];
        TextField[][] notesField=new TextField[9][9];
        StackPane[][] stackPanes = new StackPane[9][9];

        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++) {

                textField[i][j] = new TextField();

                textField[i][j].setPrefWidth(60);
                textField[i][j].setPrefHeight(60);
                textField[i][j].setAlignment(Pos.CENTER);
                textField[i][j].setFont(Font.font("Verdana", FontWeight.BOLD, 18));

                labels[i][j] = new Label("");

                notesField[i][j]= new TextField();
                notesField[i][j].setPrefWidth(5);
                notesField[i][j].setPrefHeight(5);
                notesField[i][j].setAlignment(Pos.BOTTOM_RIGHT);
                notesField[i][j].setFont(Font.font("Verdana", FontWeight.BOLD, 12));
                notesField[i][j].setStyle("-fx-text-fill: gray; -fx-background-color: transparent");
                notesField[i][j].setEditable(false);


                stackPanes[i][j] = new StackPane();

                stackPanes[i][j].getChildren().addAll(textField[i][j], labels[i][j], notesField[i][j]);

                StackPane.setAlignment(labels[i][j], Pos.TOP_LEFT);
                StackPane.setMargin(labels[i][j], new Insets(5));

                StackPane.setAlignment(notesField[i][j], Pos.BOTTOM_CENTER);
                StackPane.setMargin(notesField[i][j], new Insets(2));

                style[i][j] = "";

                int row = i;
                int col = j;

                textField[i][j].focusedProperty().addListener((arg0, oldValue, newValue) -> {
                    if (textField[row][col].isEditable() && !newValue) {
                        if (!textField[row][col].getText().matches("[1-9]"))
                            textField[row][col].setText("");
                        else if (Integer.parseInt(textField[row][col].getText()) != sudoku.getSolvedBoard()[row][col] && Integer.parseInt(textField[row][col].getText()) != sudoku.getPlayerBoard()[row][col]) {
                            sudoku.setPlayerBoardValue(Integer.parseInt(textField[row][col].getText()), row, col);
                            style[row][col] = style[row][col] + "-fx-text-fill: #960f0f;";
                            textField[row][col].setStyle(style[row][col]);
                            mistakes.getAndIncrement();
                            mistakeCounter.setText(("Mistakes: " + mistakes.get() + "/3"));
                            if (mistakes.get() > 3) {
                                AlertBox.display("Oops...", "Game Over!");
                                generateGame(sudoku, mistakes, textField, style, labels,notesField);
                            }
                        } else if (Integer.parseInt(textField[row][col].getText()) == sudoku.getPlayerBoard()[row][col])
                            ;
                        else {
                            style[row][col] = style[row][col] + "-fx-text-fill: black";
                            textField[row][col].setStyle(style[row][col]);
                            sudoku.setPlayerBoardValue(Integer.parseInt(textField[row][col].getText()), row, col);
                            textField[row][col].setEditable(false);
                        }
                    }
                });

                notesField[i][j].focusedProperty().addListener((arg0, oldValue, newValue)->{
                    if(!newValue){
                        if (!notesField[row][col].getText().matches("[1-9]"))
                            notesField[row][col].setText("");
                    }
                });

            }

        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++) {
                GridPane.setConstraints(stackPanes[i][j], j, i);
                centerMenu.getChildren().add(stackPanes[i][j]);
            }

        generateGame(sudoku, mistakes, textField, style, labels,notesField);



        playerButton.setOnAction(e->{
            for(int i=0;i<9;i++)
                for(int j=0;j<9;j++) {
                    if (sudoku.getPlayerBoard()[i][j] != 0)
                        textField[i][j].setText("" + sudoku.getPlayerBoard()[i][j]);
                    else
                        textField[i][j].setText("");
                }

        });
        solveButton.setOnAction(e -> {
            for (int i = 0; i < 9; i++)
                for (int j = 0; j < 9; j++)
                    if (sudoku.getSolvedBoard()[i][j] != 0)
                        textField[i][j].setText("" + sudoku.getSolvedBoard()[i][j]);
        });
        clearButton.setOnAction(e -> {
            for (int i = 0; i < 9; i++)
                for (int j = 0; j < 9; j++) {
                    textField[i][j].setText("");
                }
        });

        groupsButton.setOnAction(e -> {
            for (int i = 0; i < 9; i++)
                for (int j = 0; j < 9; j++) {
                    textField[i][j].setText("" + sudoku.getGroupsBoard()[i][j]);
                }
        });
        showColorsButton.setOnAction(e -> {
            for (int i = 0; i < 9; i++)
                for (int j = 0; j < 9; j++) {
                    textField[i][j].setText("" + sudoku.getColoredBoard()[i][j]);
                }
        });


        HBox bottomMenu = new HBox(10);
        bottomMenu.setAlignment(Pos.CENTER);
        bottomMenu.setPadding(new Insets(0, 10, 10, 10));

        generateButton = new Button("New Game");
        generateButton.setMinSize(75,35);
        generateButton.setFont(Font.font("Arial", 16));
        generateButton.setOnAction(e -> generateGame(sudoku, mistakes, textField, style, labels,notesField));

        //saveButton = new Button("Save");

        hintButton = new Button("Hint");
        hintButton.setMinSize(75,35);
        hintButton.setFont(Font.font("Arial", 16));
        hintButton.setOnAction(e -> giveHint(sudoku.getPlayerBoard(), sudoku.getSolvedBoard(), textField));

        notesButton = new Button("Notes");
        notesButton.setMinSize(75,35);
        notesButton.setFont(Font.font("Arial", 16));
        notesButton.setOnAction(e-> {
            toggleNotes(textField, notesField, sudoku.getPlayerBoard(), sudoku.getSolvedBoard());
            if(notesField[0][0].isEditable())
            notesButton.setStyle("-fx-background-color: rgba(48,162,255,0.4)");
            else
                notesButton.setStyle("");
        });
        mistakeCounter.setMinSize(75,35);
        mistakeCounter.setFont(Font.font("Arial", 16));

        bottomMenu.getChildren().addAll(generateButton, notesButton, hintButton, mistakeCounter);

        BorderPane layout = new BorderPane();
        layout.setTop(topMenu);
        layout.setCenter(centerMenu);
        layout.setBottom(bottomMenu);

        Scene scene = new Scene(layout, 600, 600);
        window.setScene(scene);
        window.show();

    }

    private void toggleNotes(TextField[][] textField, TextField[][] notesField, int[][] player, int[][] board) {
        for(int i=0;i<9;i++)
            for(int j=0;j<9;j++) {
                if (player[i][j] != board[i][j])
                    textField[i][j].setEditable(!textField[i][j].isEditable());
                notesField[i][j].setEditable((!notesField[i][j].isEditable()));
            }
    }

    private void giveHint(int[][] player, int[][] board, TextField[][] textField) {
        Random rand = new Random();
        int row = rand.nextInt(9);
        int col = rand.nextInt(9);
        if (player[row][col] == 0) {
            player[row][col] = board[row][col];
            textField[row][col].setText("" + board[row][col]);
            textField[row][col].setEditable(false);
            return;
        }
        for (int i = row; i >= 0; i--)
            for (int j = 8; j >= 0; j--)
                if (player[i][j] == 0) {
                    player[i][j] = board[i][j];
                    textField[i][j].setText("" + board[i][j]);
                    textField[i][j].setEditable(false);
                    return;
                }
        for (int i = row; i < 9; i++)
            for (int j = 0; j < 9; j++)
                if (player[i][j] == 0) {
                    player[i][j] = board[i][j];
                    textField[i][j].setText("" + board[i][j]);
                    textField[i][j].setEditable(false);
                    return;
                }
    }

    private void createBorders(TextField[][] textField, String[][] style) {
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++) {
                if (i == 2 && j == 2 || i == 2 && j == 5 || i == 5 && j == 2 || i == 5 && j == 5) {
                    style[i][j] = style[i][j] + "-fx-border-color: black; -fx-border-width: 0.5 2 2 0.5;";
                } else if (i == 2 && j == 3 || i == 2 && j == 6 || i == 5 && j == 3 || i == 5 && j == 6) {
                    style[i][j] = style[i][j] + "-fx-border-color: black; -fx-border-width: 0.5 0.5 2 2;";
                } else if (i == 3 && j == 2 || i == 3 && j == 5 || i == 6 && j == 2 || i == 6 && j == 5) {
                    style[i][j] = style[i][j] + "-fx-border-color: black; -fx-border-width: 2 2 0.5 0.5;";
                } else if (i == 3 && j == 3 || i == 3 && j == 6 || i == 6 && j == 3 || i == 6 && j == 6) {
                    style[i][j] = style[i][j] + "-fx-border-color: black; -fx-border-width: 2 0.5 0.5 2;";
                } else if (i == 2 || i == 5) {
                    style[i][j] = style[i][j] + "-fx-border-color: black; -fx-border-width: 0.5 0.5 2 0.5;";
                } else if (i == 3 || i == 6) {
                    style[i][j] = style[i][j] + "-fx-border-color: black; -fx-border-width: 2 0.5 0.5 0.5;";
                } else if (j == 2 || j == 5) {
                    style[i][j] = style[i][j] + "-fx-border-color: black; -fx-border-width: 0.5 2 0.5 0.5;";
                } else if (j == 3 || j == 6) {
                    style[i][j] = style[i][j] + "-fx-border-color: black; -fx-border-width: 0.5 0.5 0.5 2;";
                } else
                    style[i][j] = style[i][j] + "-fx-border-color: black; -fx-border-width: 0.5 0.5 0.5 0.5;";
                textField[i][j].setStyle(style[i][j]);
            }

    }

    private void generateGame(Game sudoku, AtomicInteger mistakes, TextField[][] textField, String[][] style, Label[][] labels,TextField[][] notesField) {
        sudoku.setSolvedBoard(GameLogic.generateBoard());
        GameLogic.solveBoard(sudoku.getSolvedBoard(), true);
        sudoku.emptyPlayerBoard();
        mistakes.set(0);
        mistakeCounter.setText(("Mistakes: " + mistakes.get() + "/3"));

        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++) {
                style[i][j] = "";
                if (sudoku.getPlayerBoard()[i][j] != 0) {
                    textField[i][j].setText("" + sudoku.getPlayerBoard()[i][j]);
                    textField[i][j].setEditable(false);
                    labels[i][j].setText((""));
                    notesField[i][j].setText("");
                } else {
                    textField[i][j].setText("");
                    if(notesField[i][j].isEditable())
                    textField[i][j].setEditable(false);
                    else
                        textField[i][j].setEditable(true);
                    labels[i][j].setText((""));
                    notesField[i][j].setText("");
                }
            }
        createBorders(textField, style);
        generateColors(sudoku, textField, style);
        generateLabels(sudoku, labels);

    }

    private void generateColors(Game sudoku, TextField[][] textField, String[][] style) {
        sudoku.setColoredBoard(GameLogic.colorBoard(sudoku.getSolvedBoard()));
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++) {
                if (sudoku.getColoredBoard()[i][j] == 1)
                    style[i][j] = style[i][j] + "-fx-control-inner-background: rgba(255,205,36,0.58);";
                else if (sudoku.getColoredBoard()[i][j] == 2)
                    style[i][j] = style[i][j] + "-fx-control-inner-background: rgba(59,170,255,0.29);";
                else if (sudoku.getColoredBoard()[i][j] == 3)
                    style[i][j] = style[i][j] + "-fx-control-inner-background: rgba(38,224,25,0.29);";
                else// if(sudoku.getColoredBoard()[i][j]==4)
                    style[i][j] = style[i][j] + "-fx-control-inner-background: rgba(225,115,130,0.75);";
                textField[i][j].setStyle(style[i][j]);
            }
    }

    private void generateLabels(Game sudoku, Label[][] labels) {
        sudoku.setGroupsBoard(GameLogic.groupColors(sudoku.getColoredBoard()));
        int nrOfGroups = GameLogic.getNumberOfGroups(sudoku.getGroupsBoard());
        int[] viz = new int[nrOfGroups + 1];
        int[] sum = new int[nrOfGroups + 1];

        for (int i = 0; i < nrOfGroups + 1; i++) {
            viz[i] = 0;
            sum[i] = 0;
        }

        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++) {
                sum[sudoku.getGroupsBoard()[i][j]] = sum[sudoku.getGroupsBoard()[i][j]] + sudoku.getSolvedBoard()[i][j];
            }

        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++) {
                if (viz[sudoku.getGroupsBoard()[i][j]] == 0) {
                    labels[i][j].setText("" + sum[sudoku.getGroupsBoard()[i][j]]);
                    viz[sudoku.getGroupsBoard()[i][j]] = 1;
                }
            }


    }

}