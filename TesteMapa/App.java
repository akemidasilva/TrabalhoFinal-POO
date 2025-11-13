import javax.swing.*;
import java.awt.Dimension; // You don't need all of App.java's variables here

public class App extends JFrame {
    public static void main(String[] args) throws Exception {

        JFrame frame = new JFrame("Bad Ice-Cream");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        // 1. Create the game panel
        BadIceCream badIceCreamGame = new BadIceCream();

        // 2. Add the panel to the frame
        frame.add(badIceCreamGame);

        // 3. Pack the frame to fit the panel's preferred size
        frame.pack();

        // 4. Center the frame on the screen
        frame.setLocationRelativeTo(null);

        // 5. Make the frame visible *LAST*
        frame.setVisible(true);
    }
}