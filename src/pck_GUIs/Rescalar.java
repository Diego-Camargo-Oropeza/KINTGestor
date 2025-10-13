package pck_GUIs;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Rescalar {

    public void escalarLabel(JLabel label, String rutaImg) {
        label.setIcon(new ImageIcon(new ImageIcon(getClass().getResource(rutaImg)).getImage().getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_DEFAULT)));
    }
}
