import javax.swing.*;
import java.awt.*;

public class drawingComponent extends JComponent {

    public drawingComponent() {
        repaint();
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.GREEN);
        g2.fillRect(50, 50, 100, 100);
    }
}
