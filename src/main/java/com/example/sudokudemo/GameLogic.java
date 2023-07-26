package com.example.sudokudemo;

import java.util.Random;

public class GameLogic {
    private static long testTime;

    public static int[][] generateBoard() {

        int[][] board = new int[9][9];
        emptyBoard(board);
        Random rand = new Random();
        int k = 0;
        int attempts = 0;
        while (k < 17) {
            int row = rand.nextInt(9);
            int col = rand.nextInt(9);
            while (board[row][col] != 0) {
                row = rand.nextInt(9);
                col = rand.nextInt(9);
            }

            int value = rand.nextInt(9) + 1;
            if (!validateBoard(board, value, row, col)) {
                int copyValue = value;
                while (copyValue < 9 && !validateBoard(board, copyValue, row, col))
                    copyValue++;
                if (copyValue < 9)
                    value = copyValue;
                else {
                    while (value > 0 && !validateBoard(board, value, row, col))
                        value--;
                }
            }
            if (value == 0) {
                emptyBoard(board);
                attempts = 0;
                k = 0;
            } else {
                int[][] boardCopy = createBoardCopy(board);
                boardCopy[row][col] = value;

                if (solveBoard(boardCopy, true)) {
                    k++;
                    board[row][col] = value;
                } else {
                    attempts++;
                    if (attempts > 2) {
                        emptyBoard(board);
                        attempts = 0;
                        k = 0;
                    }
                }
            }
        }
        return board;
    }

    public static boolean validateBoard(int[][] board, int value, int row, int col) {
        for (int i = 0; i < 9; i++)
            if (i != row && board[i][col] == value)
                return false;
        for (int j = 0; j < 9; j++)
            if (j != col && board[row][j] == value)
                return false;
        for (int i = row - row % 3; i < row - row % 3 + 3; i++)
            for (int j = col - col % 3; j < col - col % 3 + 3; j++)
                if (i != row && j != col && board[i][j] == value)
                    return false;
        return true;
    }

    public static boolean solveBoard(int[][] board, boolean time) {
        if (time == true) {
            testTime = System.currentTimeMillis();
        } else if (System.currentTimeMillis() - testTime > 300)
            return false;
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                if (board[i][j] == 0) {
                    for (int value = 1; value <= 9; value++)
                        if (validateBoard(board, value, i, j)) {
                            board[i][j] = value;

                            if (solveBoard(board, false)) {
                                return true;
                            } else {
                                board[i][j] = 0;
                            }
                        }
                    return false;
                }
        return true;
    }

    public static int[][] colorBoard(int[][] board) {
        int[][] color = new int[9][9];
        emptyBoard(color);
        int[] viz = new int[10];
        int kColor;
        Random rand = new Random();
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++) {
                if (color[i][j] == 0) {
                    kColor = assignColor(color, i, j);
                    int kMax = rand.nextInt(3) + 2;

                    for (int k = 1; k <= 9; k++)
                        viz[k] = 0;

                    if (addColor(color, board, viz, kColor, kMax, i, j) == kMax - 1) {
                        if (!shareColorWithNeighbour(color, board, viz, i, j, kColor)) {
                            for (int k = 1; k <= 4; k++)
                                if (k != kColor && checkNeighbourColors(color, board, viz, i, j, k, 0)) {
                                    if (shareColorWithNeighbour(color, board, viz, i, j, k)) {
                                        color[i][j] = k;
                                        break;
                                    }
                                }
                        }
                    }
                }
            }
        return color;
    }

    public static int assignColor(int[][] color, int row, int col) {
        Random rand = new Random();
        int k = rand.nextInt(4) + 1;
        if (row == 0 && col == 0) {
            while (color[row][col + 1] == k || color[row + 1][col] == k)
                k = rand.nextInt(4) + 1;
        } else if (row == 0 && col == 8) {
            while (color[row][col - 1] == k || color[row + 1][col] == k)
                k = rand.nextInt(4) + 1;
        } else if (row == 8 && col == 0) {
            while (color[row - 1][col] == k || color[row][col + 1] == k)
                k = rand.nextInt(4) + 1;
        } else if (row == 8 && col == 8) {
            while (color[row - 1][col] == k || color[row][col - 1] == k)
                k = rand.nextInt(4) + 1;
        } else if (row == 0) {
            while (color[row][col - 1] == k || color[row + 1][col] == k || color[row][col + 1] == k)
                k = rand.nextInt(4) + 1;
        } else if (row == 8) {
            while (color[row][col - 1] == k || color[row - 1][col] == k || color[row][col + 1] == k)
                k = rand.nextInt(4) + 1;
        } else if (col == 0) {
            while (color[row - 1][col] == k || color[row][col + 1] == k || color[row + 1][col] == k)
                k = rand.nextInt(4) + 1;
        } else if (col == 8) {
            while (color[row - 1][col] == k || color[row][col - 1] == k || color[row + 1][col] == k)
                k = rand.nextInt(4) + 1;
        } else {
            while (color[row - 1][col] == k || color[row][col + 1] == k || color[row + 1][col] == k || color[row][col - 1] == k)
                k = rand.nextInt(4) + 1;
        }
        return k;
    }

    public static int addColor(int[][] color, int[][] board, int[] viz, int kColor, int kMax, int row, int col) {

        color[row][col] = kColor;
        viz[board[row][col]] = 1;
        kMax = kMax - 1;

        if (kMax > 0) {
            Random rand = new Random();
            int kNext = rand.nextInt(4) + 1;
            if (row == 0 && col == 0) {
                while (kNext == 1 || kNext == 4)
                    kNext = rand.nextInt(4) + 1;
            } else if (row == 0 && col == 8) {
                while (kNext == 1 || kNext == 2)
                    kNext = rand.nextInt(4) + 1;
            } else if (row == 8 && col == 0) {
                while (kNext == 4 || kNext == 3)
                    kNext = rand.nextInt(4) + 1;
            } else if (row == 8 && col == 8) {
                while (kNext == 2 || kNext == 3)
                    kNext = rand.nextInt(4) + 1;
            } else if (row == 0) {
                while (kNext == 1)
                    kNext = rand.nextInt(4) + 1;
            } else if (row == 8) {
                while (kNext == 3)
                    kNext = rand.nextInt(4) + 1;
            } else if (col == 0) {
                while (kNext == 4)
                    kNext = rand.nextInt(4) + 1;
            } else if (col == 8) {
                while (kNext == 2)
                    kNext = rand.nextInt(4) + 1;
            }

            if (kNext == 1) {
                if (viz[board[row - 1][col]] == 0 && color[row - 1][col] == 0 && checkNeighbourColors(color, board, viz, row - 1, col, kColor, kNext))
                    addColor(color, board, viz, kColor, kMax, row - 1, col);
                else
                    return kMax;
            }

            if (kNext == 4) {
                if (viz[board[row][col - 1]] == 0 && color[row][col - 1] == 0 && checkNeighbourColors(color, board, viz, row, col - 1, kColor, kNext))
                    addColor(color, board, viz, kColor, kMax, row, col - 1);
                else
                    return kMax;
            }

            if (kNext == 2) {
                if (viz[board[row][col + 1]] == 0 && color[row][col + 1] == 0 && checkNeighbourColors(color, board, viz, row, col + 1, kColor, kNext))
                    addColor(color, board, viz, kColor, kMax, row, col + 1);
                else return kMax;
            }

            if (kNext == 3) {
                if (viz[board[row + 1][col]] == 0 && color[row + 1][col] == 0 && checkNeighbourColors(color, board, viz, row + 1, col, kColor, kNext))
                    addColor(color, board, viz, kColor, kMax, row + 1, col);
                else
                    return kMax;
            }
        }
        return 0;
    }

    public static boolean checkNeighbourColors(int[][] color, int[][] board, int[] viz, int row, int col, int kColor, int kNext) {
        if (row != 0)
            if (color[row - 1][col] == kColor && viz[board[row - 1][col]] == 0 && kNext != 3)
                return false;
        if (col != 8)
            if (color[row][col + 1] == kColor && viz[board[row][col + 1]] == 0 && kNext != 4)
                return false;
        if (row != 8)
            if (color[row + 1][col] == kColor && viz[board[row + 1][col]] == 0 && kNext != 1)
                return false;
        if (col != 0)
            if (color[row][col - 1] == kColor && viz[board[row][col - 1]] == 0 && kNext != 2)
                return false;
        return true;
    }

    public static boolean shareColorWithNeighbour(int[][] color, int[][] board, int[] viz, int row, int col, int kColor) {
        if (col != 8) {
            if (color[row][col + 1] == 0 && viz[board[row][col + 1]] == 0 && checkNeighbourColors(color, board, viz, row, col + 1, kColor, 2)) {
                color[row][col + 1] = kColor;
                return true;
            }
        }
        if (row != 8) {
            if (color[row + 1][col] == 0 && viz[board[row + 1][col]] == 0 && checkNeighbourColors(color, board, viz, row + 1, col, kColor, 3)) {
                color[row + 1][col] = kColor;
                return true;
            }

        }
        if (row != 0) {
            if (color[row - 1][col] == 0 && viz[board[row - 1][col]] == 0 && checkNeighbourColors(color, board, viz, row - 1, col, kColor, 1)) {
                color[row - 1][col] = kColor;
                return true;
            }

        }
        if (col != 0) {
            if (color[row][col - 1] == 0 && viz[board[row][col - 1]] == 0 && checkNeighbourColors(color, board, viz, row, col - 1, kColor, 4)) {
                color[row][col - 1] = kColor;
                return true;
            }
        }
        return false;
    }

    public static int[][] groupColors(int[][] color) {
        int[][] board = new int[9][9];
        emptyBoard(board);
        int counter = 0;
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0) {
                    counter = counter + 1;
                    assignGroupColor(color, board, counter, i, j);
                }
            }
        return board;
    }

    public static void assignGroupColor(int[][] color, int[][] board, int counter, int row, int col) {

        board[row][col] = counter;

        if (row != 0) {
            if (color[row - 1][col] == color[row][col] && board[row - 1][col] == 0)
                assignGroupColor(color, board, counter, row - 1, col);
        }

        if (col != 0) {
            if (color[row][col - 1] == color[row][col] && board[row][col - 1] == 0)
                assignGroupColor(color, board, counter, row, col - 1);
        }

        if (col != 8) {
            if (color[row][col + 1] == color[row][col] && board[row][col + 1] == 0)
                assignGroupColor(color, board, counter, row, col + 1);
        }

        if (row != 8) {
            if (color[row + 1][col] == color[row][col] && board[row + 1][col] == 0)
                assignGroupColor(color, board, counter, row + 1, col);
        }

    }

    public static int getNumberOfGroups(int[][] groupsBoard) {
        int maxi = 0;
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                if (groupsBoard[i][j] > maxi)
                    maxi = groupsBoard[i][j];
        return maxi;
    }

    public static void emptyBoard(int[][] board) {
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                board[i][j] = 0;
    }

    public static int[][] createBoardCopy(int board[][]) {
        int[][] boardCopy = new int[9][9];
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                boardCopy[i][j] = board[i][j];
        return boardCopy;
    }

}
