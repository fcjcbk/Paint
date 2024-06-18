import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class MenuItemActionListener implements ActionListener {
    /*
    Name:
    newFile,
    openFile,
    saveFile,
    full,
    half,
    quash,
    recover
    * */
    private void openFile(File file) {
        try {
            StartUp.mainWindow.getDrawPanel().setImage(ImageIO.read(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage makePanel(JPanel panel) {
        int w = panel.getWidth();
        int h = panel.getHeight();
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        panel.print(g);
        return bi;
    }

    private void saveFile(File f) throws IOException {
        BufferedImage im = makePanel(StartUp.mainWindow.getDrawPanel());
        ImageIO.write(im, "png", f);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String name = ((JMenuItem) e.getSource()).getName();
        switch (name) {
            case "newFile":
                StartUp.mainWindow.createNewDrawPanel();
                break;

            case "openFile":
                JFileChooser fileChooserOpen = new JFileChooser(new File("."));
                fileChooserOpen.setFileFilter(new FileNameExtensionFilter("图片", "jpg", "png"));
                if (fileChooserOpen.showOpenDialog(StartUp.mainWindow) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooserOpen.getSelectedFile();
                    openFile(file);
                }
                break;

            case "saveFile":
                JFileChooser fileChooserSave = new JFileChooser(new File("."));
                fileChooserSave.setFileFilter(new FileNameExtensionFilter("图片", "jpg", "png"));
                if (fileChooserSave.showSaveDialog(StartUp.mainWindow) == JFileChooser.APPROVE_OPTION) {
                    File file = new File(fileChooserSave.getSelectedFile() + ".png");
                    try {
                        saveFile(file);
                    } catch (IOException ioE) {
                        ioE.printStackTrace();
                    }
                }
                break;

            case "full":
                // Add implementation if needed
                break;

            case "half":
                // Add implementation if needed
                break;

            case "quash":
                StartUp.mainWindow.getDrawPanel().quash();
                break;

            case "recover":
                StartUp.mainWindow.getDrawPanel().recover();
                break;

            case "delete":
                System.out.println("delete");
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        // Update Swing components here
                        StartUp.mainWindow.getDrawPanel().delete();
                    }
                });
                break;
            case "copy":
                StartUp.mainWindow.getDrawPanel().copy();
                break;
            case "paste":
                StartUp.mainWindow.getDrawPanel().paste();
                break;
            default:
                return;
        }
        System.out.println(e.toString());
    }
}
