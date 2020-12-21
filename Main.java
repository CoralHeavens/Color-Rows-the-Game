package sample;
import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Main {

    public static int mX = 9;                   // Field Width
    public static int mY = 9;                   // Field Height
    public static int mC = 3;                   // Count of new squares every move
    public static int time, score = 0;          // Counter of time and score
    public static int sizeOfGraal = mX * mY;    // Size of Field
    public static int finish = 5;               // Free squares to finish

    public static void main(String[] args) {

        zWindow window = new zWindow();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setBounds(400, 200, 530, 620);
        window.setResizable(false);
        window.setTitle("Color Rows v0.1");
        window.setVisible(true);

    }
}

/*  *** JButton class with coordinates  *** */

class JButtonXY extends JButton {

    public int x, y;
    public Color bgColor;
    public boolean[] isReadyFrom = new boolean[2];

    public JButtonXY() {
        super();
    }
}

class zWindow extends JFrame {

    public Random random = new Random();
    public boolean isKeyPressed = false;
    public JButtonXY[][] Fifth_Holy_Graal_War = new JButtonXY[Main.mX][Main.mY];
    public JButtonXY[] Next = new JButtonXY[Main.mC];
    public JButtonXY Surrender = new JButtonXY(), Waves = new JButtonXY();
    public int Graal_x, Graal_y, color_number, freeSpace;
    public int[][] nextA = new int[2][Main.mC];
    public Color[] current_color = new Color[Main.mC];
    public Color Graal_Color;
    public boolean[][] erase = new boolean[Main.mX][Main.mY];

    /* ****  Constructor  **** */

    public zWindow() {

        for (int z = 0; z < Main.mX; z++) {
            for (int c = 0; c < Main.mY; c++) {
                erase[z][c] = false;
            }
        }
        setLayout(new FlowLayout());
        gridOptionsSet();
    }

    public void gridOptionsSet() {

        for (int x = 0; x < Main.mX; x++) {
            for (int y = 0; y < Main.mY; y++) {

                Fifth_Holy_Graal_War[x][y] = new JButtonXY();
                Fifth_Holy_Graal_War[x][y].setPreferredSize(new Dimension(50, 50));
                Fifth_Holy_Graal_War[x][y].setBackground(Color.WHITE);
                Fifth_Holy_Graal_War[x][y].bgColor = Color.WHITE;
                Fifth_Holy_Graal_War[x][y].x = x;
                Fifth_Holy_Graal_War[x][y].y = y;

                /*  ****    Action after button click   *** */

                int finalX = x;
                int finalY = y;
                Fifth_Holy_Graal_War[x][y].addActionListener(e -> {

                    if (isKeyPressed) {
                        pressingButtonAfter(Fifth_Holy_Graal_War[finalX][finalY]);
                    }
                    else {
                        pressingButtonBefore(Fifth_Holy_Graal_War[finalX][finalY]);
                    }

                });
                add(Fifth_Holy_Graal_War[x][y]);
            }
        }

        visualDetailsSet();
        firstGeneration();
    }

    public void visualDetailsSet() {

        // Count of moves
        Waves.addActionListener(e -> info());
        add(Waves);

        // Next squares
        for (int z = 0; z < Main.mC; z++) {
            Next[z] = new JButtonXY();
            Next[z].setPreferredSize(new Dimension(50, 50));
            add(Next[z]);
        }

        // Surrender button
        Surrender.addActionListener(e -> finishAlert());
        add(Surrender);
    }

    public void pressingButtonAfter(JButtonXY West_Graal_Battle) {

        if (West_Graal_Battle.x == Graal_x && West_Graal_Battle.y == Graal_y) {
            System.out.println("\nStep Back is unhonored!");
            Fifth_Holy_Graal_War[Graal_x][Graal_y].setBackground(Fifth_Holy_Graal_War[Graal_x][Graal_y].bgColor);
            isKeyPressed = false;
            return;
        }
        if (West_Graal_Battle.bgColor != Color.WHITE) {
            System.out.println("\nNo. Just No.");
            return;
        }
        isKeyPressed = false;

        drawFinalSquares();
        generateColor();
        generateNext(West_Graal_Battle.x, West_Graal_Battle.y);
        drawLesserSquares();

        West_Graal_Battle.setBackground(Graal_Color);
        West_Graal_Battle.bgColor = Graal_Color;
        Fifth_Holy_Graal_War[Graal_x][Graal_y].setBackground(Color.WHITE);
        Fifth_Holy_Graal_War[Graal_x][Graal_y].bgColor = Color.WHITE;
        System.out.println("\nAnother one bite the dust");

        Main.time++;

        checkClosest();
        checkFinish();
    }

    public void pressingButtonBefore(JButtonXY East_Graal_Battle) {

        if (East_Graal_Battle.bgColor == Color.WHITE) {
            System.out.println("\nFriendly fire is not permitted here");
            return;
        }
        isKeyPressed = true;
        Graal_x = East_Graal_Battle.x;
        Graal_y = East_Graal_Battle.y;
        Graal_Color = East_Graal_Battle.bgColor;
        East_Graal_Battle.setBackground(Color.GRAY);
        System.out.println("\nSpot has been chosen");
    }

    public void generateColor() {

        /*  *** Generate colors for next squares    *** */

        for (int c = 0; c < Main.mC; c++) {
            color_number = random.nextInt(6);
            if (color_number == 0) { current_color[c] = Color.RED;}
            if (color_number == 1) { current_color[c] = Color.BLUE;}
            if (color_number == 2) { current_color[c] = Color.CYAN;}
            if (color_number == 3) { current_color[c] = Color.GREEN;}
            if (color_number == 4) { current_color[c] = Color.MAGENTA;}
            if (color_number == 5) { current_color[c] = Color.YELLOW;}
        }
    }

    public void generateNext(int not_x, int not_y) {

        /*  *** Generate spots for next squares   *** */

        for (int c = 0; c < Main.mC; c++) {
            nextA[0][c] = random.nextInt(Main.mX);
            nextA[1][c] = random.nextInt(Main.mY);
            if (Fifth_Holy_Graal_War[nextA[0][c]][nextA[1][c]].bgColor != Color.WHITE) {
                c--;
                continue;
            }
            if (Main.time != 0 && nextA[0][c] == Graal_x && nextA[1][c] == Graal_y) {
                c--;
                continue;
            }
            if (nextA[0][c] == not_x && nextA[1][c] == not_y) {
                c--;
                continue;
            }
            if (c > 0) {
                for (int z = c-1; z >= 0; z--) {
                    if (nextA[0][c] == nextA[0][z] && nextA[1][c] == nextA[1][z]) {
                        c--;
                    }
                }
            }
        }
    }

    public void drawLesserSquares() {

        for (int c = 0; c < Main.mC; c++) {
            Next[c].setBackground(current_color[c]);
            Next[c].bgColor = current_color[c];
            Next[c].x = nextA[0][c];
            Next[c].y = nextA[1][c];
        }
    }

    public void drawFinalSquares() {

        for (int z = 0; z < Main.mC; z++) {
            Fifth_Holy_Graal_War[Next[z].x][Next[z].y].setBackground(Next[z].bgColor);
            Fifth_Holy_Graal_War[Next[z].x][Next[z].y].bgColor = Next[z].bgColor;
        }
        System.out.println("\nNew enemies arrives...");
    }

    public void checkFinish() {

        freeSpace = 0;

        for (int z = 0; z < Main.mX; z++) {
            for (int c = 0; c < Main.mY; c++) {
                if (Fifth_Holy_Graal_War[z][c].bgColor != Color.WHITE) {
                    freeSpace++;
                }
            }
        }
        System.out.println("\nFree space left: " + (Main.sizeOfGraal - freeSpace));
        if (freeSpace > Main.sizeOfGraal-Main.finish) {

            finishAlert();
        }

        Waves.setText("Passed " + Main.time + " waves");
        Surrender.setText("Score: " + Main.score);
    }

    public void finishAlert() {

        this.setVisible(false);
        JOptionPane.showMessageDialog(null, "Game over! Your Score: " + Main.score);
        System.exit(0);
    }

    public void info() {

        JOptionPane.showMessageDialog(null, "Hi! To earn points you need to set\n" +
                    "squares in rows of three or more!\n" +
                    "Good luck!\nGame finishes when you leave <5\nfree squares!\nLeft button - info\n" +
                    "Right button - surrender\n\n" +
                    "Creator: CoralHeavens ♥" + "\n");
    }

    public void checkClosest() {

        for (int x = 0; x < Main.mX; x++) {
            for (int y = 0; y < Main.mY; y++) {
                if (Fifth_Holy_Graal_War[x][y].bgColor == Color.WHITE) {
                    continue;
                }
                if (x != Main.mX-1) {
                    scan(x, y, x+1, y, x-1, y, 0);
                }
                if (y != Main.mY-1) {
                    scan(x, y, x, y+1, x, y-1, 1);
                }
            }
        }
        executeJudgement();
    }

    public void scan(int x, int y, int x_add, int y_add, int x_sub, int y_sub, int side) {

        if (Fifth_Holy_Graal_War[x][y].bgColor == Fifth_Holy_Graal_War[x_add][y_add].bgColor) {
            if (Fifth_Holy_Graal_War[x][y].isReadyFrom[side]) {
                erase[x_sub][y_sub] = true;
                erase[x][y] = true;
                erase[x_add][y_add] = true;
            }
            Fifth_Holy_Graal_War[x_add][y_add].isReadyFrom[side] = true;
        }
    }

    public void executeJudgement() {

        for (int x = 0; x < Main.mX; x++) {
            for (int y = 0; y < Main.mY; y++) {
                if (erase[x][y]) {
                    Fifth_Holy_Graal_War[x][y].setBackground(Color.WHITE);
                    Fifth_Holy_Graal_War[x][y].bgColor = Color.WHITE;
                    erase[x][y] = false;
                    Main.score++;
                }
                Fifth_Holy_Graal_War[x][y].isReadyFrom[0] = false;
                Fifth_Holy_Graal_War[x][y].isReadyFrom[1] = false;
            }
        }
    }

    public void firstGeneration() {

        generateColor();
        generateNext(-1, -1);
        drawLesserSquares();
        drawFinalSquares();
        generateColor();
        generateNext(-1, -1);
        drawLesserSquares();
        checkClosest();
        checkFinish();
        info();
    }
}