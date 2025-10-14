package pck_GUIs;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Rescalar {

    public void escalarLabel(JLabel label, String rutaImg) {
        // Asegúrate de llamar a esto cuando label ya tenga tamaño (>0)
        int w = label.getWidth();
        int h = label.getHeight();
        if (w <= 0 || h <= 0) return;

        try {
            URL url = getClass().getResource(rutaImg);
            BufferedImage src = ImageIO.read(url);

            // Opcional: mantener proporción y calcular nuevo tamaño
            Dimension d = fit(src.getWidth(), src.getHeight(), w, h);
            BufferedImage dst = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2 = dst.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.drawImage(src, 0, 0, d.width, d.height, null);
            g2.dispose();

            label.setIcon(new ImageIcon(dst));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Dimension fit(int srcW, int srcH, int maxW, int maxH) {
        double r = Math.min(maxW / (double) srcW, maxH / (double) srcH);
        return new Dimension((int) Math.round(srcW * r), (int) Math.round(srcH * r));
    }
}
