package Helper;

import javax.swing.*;
import java.awt.*;

public class UIHelper {

    public static void syncInternalFrameWithDesktop(JInternalFrame frame, JDesktopPane desktopPane, JPanel outerPanel, JPanel wrapperPanel) {
        Runnable resizeTask = () -> {
            int parentWidth = desktopPane.getWidth();
            int parentHeight = desktopPane.getHeight();

            frame.setBounds(0, 0, (int)(parentWidth * 0.98), (int)(parentHeight * 0.92));

            int outerWidth = (int) (parentWidth * 0.98);
            int outerHeight = (int) (parentHeight * 0.92);
            outerPanel.setPreferredSize(new Dimension(outerWidth, outerHeight));

            int wrapperWidth = (int) (outerWidth * 0.95);
            int wrapperHeight = (int) (outerHeight * 0.95);
            wrapperPanel.setPreferredSize(new Dimension(wrapperWidth, wrapperHeight));

            outerPanel.revalidate();
            outerPanel.repaint();
            wrapperPanel.revalidate();
            wrapperPanel.repaint();
        };

        desktopPane.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                resizeTask.run();
            }
        });

        SwingUtilities.invokeLater(resizeTask);
    }
}
