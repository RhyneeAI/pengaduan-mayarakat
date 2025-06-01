package Helper;
import javax.swing.*;
import java.awt.*;

public class RoundedButton extends JButton {
    private final int cornerRadius = 13;

    public RoundedButton(String label) {
        super(label);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setFont(getFont().deriveFont(Font.BOLD, 14f));
        setMargin(new Insets(8, 16, 8, 16)); // top, left, bottom, right
    }

    @Override
    protected void paintComponent(Graphics g) {
        Color bg = getBackground();
        Color fg = getForeground();

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(bg);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        g2.setColor(fg);
        FontMetrics fm = g2.getFontMetrics();
        int stringWidth = fm.stringWidth(getText());
        int stringHeight = fm.getAscent();
        int x = (getWidth() - stringWidth) / 2;
        int y = (getHeight() + stringHeight) / 2 - 2;

        g2.drawString(getText(), x, y);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        // optional: border hilang
    }
}
