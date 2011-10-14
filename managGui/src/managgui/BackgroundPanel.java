import java.awt.*;
import javax.swing.*;

public class BackgroundPanel extends JPanel {

    public BackgroundPanel(String b) {
        super(true);
        setOpaque(false);
        icon = new ImageIcon(b);
        background = icon.getImage();
    }

    public void paintComponent(Graphics g) {
        g.drawImage(background, 0, 0, this);
    }

    protected ImageIcon icon;
    protected Image background;

}
