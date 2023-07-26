package com.example.sudokudemo;

public class Game {
    private int[][] solvedBoard, playerBoard, coloredBoard, groupsBoard;

    public Game(int[][] playerBoard, int[][] solvedBoard, int[][] coloredBoard,int[][] groupsBoard) {
        this.solvedBoard = playerBoard;
        this.playerBoard = solvedBoard;
        this.coloredBoard = coloredBoard;
        this.groupsBoard=groupsBoard;
    }

    public int[][] getGroupsBoard() {
        return groupsBoard;
    }

    public void setGroupsBoard(int[][] group) {
        this.groupsBoard = group;
    }

    public int[][] getSolvedBoard() {
        return solvedBoard;
    }

    public void setSolvedBoard(int [][] board) {
        this.solvedBoard =board;
    }

    public void setSolvedBoardValue(int value, int row, int col) {
        this.solvedBoard[row][col] = value;
    }

    public int[][] getPlayerBoard() {
        return playerBoard;
    }

    public void setPlayerBoard(int[][] playerBoard) {
        this.playerBoard = playerBoard;
    }

    public void setPlayerBoardValue(int value, int row, int col) {
        this.playerBoard[row][col] = value;
    }

    public int[][] getColoredBoard() {
        return coloredBoard;
    }

    public void setColoredBoard(int[][] coloredBoard) {
        this.coloredBoard = coloredBoard;
    }

    public int[][] createSolvedBoardCopy()
    {
        int [][]board=new int[9][9];
        for(int i=0;i<9;i++)
            for(int j=0;j<9;j++)
                board[i][j]= solvedBoard[i][j];
        return board;
    }

    public void emptySolvedBoard(){
        for(int i=0;i<9;i++)
            for(int j=0;j<9;j++)
                this.solvedBoard[i][j]=0;
    }

    public void emptyPlayerBoard(){
        for(int i=0;i<9;i++)
            for(int j=0;j<9;j++)
                this.playerBoard[i][j]=0;
    }
    public void emptyColoredBoard(){
        for(int i=0;i<9;i++)
            for(int j=0;j<9;j++)
                this.coloredBoard[i][j]=0;
    }
    public void emptyGroupsBoard(){
        for(int i=0;i<9;i++)
            for(int j=0;j<9;j++)
                this.groupsBoard[i][j]=0;
    }

}
