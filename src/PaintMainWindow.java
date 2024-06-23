import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class PaintMainWindow extends JFrame implements ActionListener {
    private JPanel paintMainPanel;
    private JMenuBar mainMenu;
    private JPanel toolPanel;
    private JPanel drawBackPanel;
    private JButton pencilButton;
    private JButton bucketButton;
    private JButton eraserButton;
    private JButton strawButton;
    private JButton textButton;
    private JButton brushesButton;
    private JButton lineButton;
    private JButton triangleButton;
    private JButton arcButton;
    private JButton ellipticalButton;
    private JButton pentagonButton;
    private JButton hexagonButton;
    private JButton magnifyingButton;
    private JPanel mainTool;
    private JPanel shapeTools;
    private JButton blackButton;
    private JButton garyButton;
    private JButton lightGaryButton;
    private JButton darkRedButton;
    private JButton pinkButton;
    private JButton citrusColorButton;
    private JButton redButton;
    private JButton waxyYellowButton;
    private JButton mistyColorButton;
    private JButton orangeButton;
    private JButton lightGreenButton;
    private JButton blueButton;
    private JButton lightYellowButton;
    private JButton ultramarineButton;
    private JButton lightPurpleButton;
    private JPanel colorPanel;
    private JButton paletteButton;
    private JPanel brushPanel;
    private JButton rectangleButton;
    private JButton selectButton;
    private JPanel select;
    private JPanel statusPanel;
    private JSpinner sizeSpinner;
    private JPanel sizePanel;
    private JButton nowColorButton;
    private JButton lastColorButton;
    private JLabel mousePosLabel;
    private JLabel canvasSizeLabel;
    private JPanel drawPanel;
    private JRadioButton fillButton;
    private JMenu file, view, edit;
    private JMenuItem openFile, newFile, saveFile;
    private JMenuItem full, half;
    private JMenuItem quash, recover, delete, copy, paste;
    private DrawPanelListener drawPanelListener = new DrawPanelListener();
    private MenuItemActionListener menuItemActionListener = new MenuItemActionListener();

    private Map<JButton, ETools> toolMap = new HashMap<>();
    private Map<JButton, Color> colorMap = new HashMap<>();

    public PaintMainWindow() {
        super("画图");
        //System.out.println("second");
        setIconImage(new ImageIcon("assets/Logo.png").getImage());
        setSize(1200, 800);
        setLocation(500, 400);
        Initialize();
        add(paintMainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension temp = drawPanel.getSize();
        setCanvasSizeLabel(temp.width, temp.height);
        setMousePosLabel(0, 0);
        setVisible(true);
        brushesButton.requestFocus();
    }


    private void Initialize() {
        Font font = new Font("Microsoft YaHei UI", Font.PLAIN, 14);

        file = new JMenu("文件");
        view = new JMenu("视图");
        edit = new JMenu("编辑");

        file.setFont(font);
        view.setFont(font);
        edit.setFont(font);

        newFile = new JMenuItem("新建", new ImageIcon("assets/newFile (Icon).png"));
        openFile = new JMenuItem("打开", new ImageIcon("assets/open (Icon).png"));
        saveFile = new JMenuItem("保存", new ImageIcon("assets/save (Icon).png"));
        newFile.setFont(font);
        openFile.setFont(font);
        saveFile.setFont(font);
        newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        newFile.addActionListener(menuItemActionListener);
        openFile.addActionListener(menuItemActionListener);
        saveFile.addActionListener(menuItemActionListener);
        newFile.setName("newFile");
        openFile.setName("openFile");
        saveFile.setName("saveFile");
        file.add(newFile);
        file.add(openFile);
        file.add(saveFile);

        full = new JMenuItem("100%");
        half = new JMenuItem("50%");
        full.addActionListener(menuItemActionListener);
        half.addActionListener(menuItemActionListener);
        full.setName("full");
        half.setName("half");
        view.add(full);
        view.add(half);

        quash = new JMenuItem("撤销", new ImageIcon("assets/quash (Icon).png"));
        recover = new JMenuItem("恢复", new ImageIcon("assets/recover (Icon).png"));
        delete = new JMenuItem("删除", new ImageIcon("assets/quash (Icon).png"));
        copy = new JMenuItem("复制", new ImageIcon("assets/paste (Icon).png"));
        paste = new JMenuItem("粘贴", new ImageIcon("assets/paste (Icon).png"));

        quash.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
        recover.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
        delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0));
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));

        quash.addActionListener(menuItemActionListener);
        recover.addActionListener(menuItemActionListener);
        delete.addActionListener(menuItemActionListener);
        copy.addActionListener(menuItemActionListener);
        delete.addActionListener(menuItemActionListener);
        paste.addActionListener(menuItemActionListener);

        quash.setName("quash");
        recover.setName("recover");
        delete.setName("delete");
        copy.setName("copy");
        paste.setName("paste");

        edit.add(quash);
        edit.add(recover);
        edit.add(delete);
        edit.add(copy);
        edit.add(paste);

        mainMenu.add(file);
        mainMenu.add(view);
        mainMenu.add(edit);

        sizeSpinner.setModel(new SpinnerNumberModel(2, 1, 10, 1));
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) sizeSpinner.getEditor();
        editor.getTextField().setEditable(false);
        editor.getTextField().setFocusable(false);

        pencilButton.setIcon(new ImageIcon("assets/pencil (Icon).png"));
        eraserButton.setIcon(new ImageIcon("assets/eraser (Icon).png"));
        bucketButton.setIcon(new ImageIcon("assets/bucket (Icon).png"));
        strawButton.setIcon(new ImageIcon("assets/straw (Icon).png"));
        textButton.setIcon(new ImageIcon("assets/text (Icon).png"));
        magnifyingButton.setIcon(new ImageIcon("assets/magnifying (Icon).png"));
        brushesButton.setIcon(new ImageIcon("assets/paint (Icon).png"));
        lineButton.setIcon(new ImageIcon("assets/line (Icon).png"));
        arcButton.setIcon(new ImageIcon("assets/arc (Icon).png"));
        ellipticalButton.setIcon(new ImageIcon("assets/elliptical (Icon).png"));
        hexagonButton.setIcon((new ImageIcon("assets/hexagon (Icon).png")));
        pentagonButton.setIcon(new ImageIcon("assets/pentagon (Icon).png"));
        triangleButton.setIcon(new ImageIcon("assets/triangle (Icon).png"));
        rectangleButton.setIcon(new ImageIcon("assets/rectangle (Icon).png"));
        selectButton.setIcon(new ImageIcon("assets/constituency (Icon).png"));
        paletteButton.setIcon(new ImageIcon("assets/color (Icon).png"));

        fillButton.addActionListener(this);

        pencilButton.addActionListener(this);
        eraserButton.addActionListener(this);
        bucketButton.addActionListener(this);
        strawButton.addActionListener(this);
        textButton.addActionListener(this);
        magnifyingButton.addActionListener(this);
        brushesButton.addActionListener(this);
        lineButton.addActionListener(this);
        arcButton.addActionListener(this);
        ellipticalButton.addActionListener(this);
        hexagonButton.addActionListener(this);
        pentagonButton.addActionListener(this);
        triangleButton.addActionListener(this);
        rectangleButton.addActionListener(this);
        selectButton.addActionListener(this);
        paletteButton.addActionListener(this);

        blackButton.addActionListener(this);
        garyButton.addActionListener(this);
        lightGaryButton.addActionListener(this);
        darkRedButton.addActionListener(this);
        pinkButton.addActionListener(this);
        citrusColorButton.addActionListener(this);
        redButton.addActionListener(this);
        waxyYellowButton.addActionListener(this);
        mistyColorButton.addActionListener(this);
        orangeButton.addActionListener(this);
        lightGreenButton.addActionListener(this);
        blueButton.addActionListener(this);
        lightYellowButton.addActionListener(this);
        ultramarineButton.addActionListener(this);
        lightPurpleButton.addActionListener(this);

        nowColorButton.addActionListener(this);
        lastColorButton.addActionListener(this);

        initMap();

        sizeSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                StartUp.mainWindow.getDrawPanel().setThickness((int) sizeSpinner.getValue());
            }
        });
    }

    public DrawPanelListener getDrawPanel() {
        return (DrawPanelListener) drawPanel;
    }

    public void setCanvasSizeLabel(int x, int y) {
        canvasSizeLabel.setIcon(new ImageIcon("assets/canvas (Icon).png"));
        canvasSizeLabel.setText(x + ", " + y + "px");
    }

    public void setMousePosLabel(int x, int y) {
        if (DrawPanelListener.isInCanvas) {
            mousePosLabel.setIcon(new ImageIcon("assets/cursor (Icon).png"));
            mousePosLabel.setText(x + ", " + y + "px");
        } else {
            mousePosLabel.setIcon(new ImageIcon("assets/cursor (Icon).png"));
            mousePosLabel.setText("");
        }
    }

    private void showColorChooser() {
        Color newColor = JColorChooser.showDialog(this, "拾色器", new Color(121, 203, 96));
        if (newColor != null) {
            setLastColor(((DrawPanelListener) drawPanel).getCurrentColor());
            StartUp.mainWindow.getDrawPanel().setColor(newColor);
            setCurrentColor(((DrawPanelListener) drawPanel).getCurrentColor());
        }
    }

    public void createNewDrawPanel() {
        JFrame newFileFrame = new JFrame();
        newFileFrame.setTitle("新建画布");
        newFileFrame.setSize(366, 168);
        newFileFrame.setPreferredSize(new Dimension(366, 168));
        newFileFrame.setLayout(null);
        newFileFrame.setResizable(false);
        newFileFrame.pack();

        newFileFrame.setLocation(500, 400);

        newFileFrame.setVisible(true);

        JTextField width = new JTextField();
        width.setSize(100, 25);
        width.setLocation(100, 25);

        JLabel widthLabel = new JLabel("宽度 (px):");
        widthLabel.setSize(75, 25);
        widthLabel.setLocation(25, 25);

        JLabel heightLabel = new JLabel("高度 (px):");
        heightLabel.setSize(75, 25);
        heightLabel.setLocation(25, 75);

        JTextField height = new JTextField();
        height.setLocation(100, 75);
        height.setSize(100, 25);

        JButton okay = new JButton("新建");
        okay.setSize(75, 25);
        okay.setLocation(250, 25);
        okay.addActionListener(new ActionListener() {
                                   public void actionPerformed(ActionEvent e) {
                                       try {
                                           int newDrawPanelWidth = Integer.parseInt(width.getText());
                                           int newDrawPanelHeight = Integer.parseInt(height.getText());
                                           newFileFrame.dispose();

                                           ((DrawPanelListener) drawPanel).changeDrawPanelSize(newDrawPanelWidth, newDrawPanelHeight);
                                       } catch (NumberFormatException nfe) {
                                           JOptionPane.showMessageDialog(null,
                                                   "输入错误，请输入整数",
                                                   "ERROR",
                                                   JOptionPane.ERROR_MESSAGE);
                                       }
                                   }
                               }
        );

        JButton cancel = new JButton("取消");
        cancel.setSize(75, 25);
        cancel.setLocation(250, 75);
        cancel.addActionListener(new ActionListener() {
                                     public void actionPerformed(ActionEvent e) {
                                         newFileFrame.dispose();
                                     }
                                 }
        );
        ArrayList<Component> focusOrderList = new ArrayList<Component>();
        focusOrderList.add(heightLabel);
        focusOrderList.add(widthLabel);
        focusOrderList.add(width);
        focusOrderList.add(height);
        focusOrderList.add(okay);
        focusOrderList.add(cancel);

        Vector<Component> vector = new Vector<Component>();
        vector.add(heightLabel);
        vector.add(widthLabel);
        vector.add(width);
        vector.add(height);
        vector.add(okay);
        vector.add(cancel);
        newFileFrame.setFocusTraversalPolicy(new NewFileDialogFocusTraversalPolicy(vector));
        newFileFrame.add(heightLabel);
        newFileFrame.add(widthLabel);
        newFileFrame.add(width);
        newFileFrame.add(height);
        newFileFrame.add(okay);
        newFileFrame.add(cancel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (toolMap.containsKey(source)) {
            ((DrawPanelListener) drawPanel).setTool(toolMap.get(source));
        } else if (colorMap.containsKey(source)) {
            setLastColor(((DrawPanelListener) drawPanel).getCurrentColor());
            ((DrawPanelListener) drawPanel).setColor(colorMap.get(source));
            setCurrentColor(((DrawPanelListener) drawPanel).getCurrentColor());
        } else if (source == lastColorButton) {
            setLastColor(((DrawPanelListener) drawPanel).getCurrentColor());
            ((DrawPanelListener) drawPanel).setColor(lastColorButton.getBackground());
            setCurrentColor(((DrawPanelListener) drawPanel).getCurrentColor());
        } else if(source == nowColorButton) {
            setLastColor(((DrawPanelListener) drawPanel).getCurrentColor());
            ((DrawPanelListener) drawPanel).setColor(nowColorButton.getBackground());
            setCurrentColor(((DrawPanelListener) drawPanel).getCurrentColor());
        } else if (source == paletteButton) {
            showColorChooser();
        } else if (source == fillButton) {
            StartUp.mainWindow.getDrawPanel().setTransparency(!fillButton.isSelected());
        } else if (source == selectButton) {
            ((DrawPanelListener) drawPanel).setTool(ETools.SELECT);
        }
    }

    private void initMap() {
        toolMap.put(pencilButton, ETools.PENCIL);
        toolMap.put(eraserButton, ETools.ERASER);
        toolMap.put(bucketButton, ETools.BUCKET);
        toolMap.put(strawButton, ETools.STRAW);
        toolMap.put(textButton, ETools.TEXT);
        toolMap.put(brushesButton, ETools.PENCIL);
        toolMap.put(lineButton, ETools.LINE);
        toolMap.put(arcButton, ETools.POLYGON);
        toolMap.put(ellipticalButton, ETools.ELLIPTICAL);
        toolMap.put(hexagonButton, ETools.HEXAGON);
        toolMap.put(pentagonButton, ETools.PENTAGON);
        toolMap.put(triangleButton, ETools.TRIANGLE);
        toolMap.put(rectangleButton, ETools.RECTANGLE);

        colorMap.put(blackButton, SpecialColor.black);
        colorMap.put(garyButton, SpecialColor.gary);
        colorMap.put(lightGaryButton, SpecialColor.lightGary);
        colorMap.put(darkRedButton, SpecialColor.darkRed);
        colorMap.put(pinkButton, SpecialColor.pink);
        colorMap.put(citrusColorButton, SpecialColor.citrusColor);
        colorMap.put(redButton, SpecialColor.red);
        colorMap.put(waxyYellowButton, SpecialColor.waxyYellow);
        colorMap.put(mistyColorButton, SpecialColor.mistyColor);
        colorMap.put(orangeButton, SpecialColor.orange);
        colorMap.put(lightGreenButton, SpecialColor.lightGreen);
        colorMap.put(blueButton, SpecialColor.blue);
        colorMap.put(lightYellowButton, SpecialColor.lightYellow);
        colorMap.put(ultramarineButton, SpecialColor.ultramarine);
        colorMap.put(lightPurpleButton, SpecialColor.lightPurple);
    }

    public void actionPerformed1(ActionEvent e) {
        if (e.getSource() == paletteButton) {
            showColorChooser();
        } else if (e.getSource() == pencilButton) {
            ((DrawPanelListener) drawPanel).setTool(ETools.PENCIL);
        } else if (e.getSource() == eraserButton) {
            ((DrawPanelListener) drawPanel).setTool(ETools.ERASER);
        } else if (e.getSource() == bucketButton) {
            ((DrawPanelListener) drawPanel).setTool(ETools.BUCKET);
        } else if (e.getSource() == textButton) {
            ((DrawPanelListener) drawPanel).setTool(ETools.TEXT);
        } else if (e.getSource() == magnifyingButton) {
            ((DrawPanelListener) drawPanel).setTool(ETools.MAGNIFYING);
        } else if (e.getSource() == strawButton) {
            ((DrawPanelListener) drawPanel).setTool(ETools.STRAW);
        } else if (e.getSource() == brushesButton) {
            // TODO
            ((DrawPanelListener) drawPanel).setTool(ETools.PENCIL);
        } else if (e.getSource() == lineButton) {
            ((DrawPanelListener) drawPanel).setTool(ETools.LINE);
        } else if (e.getSource() == arcButton) {
            ((DrawPanelListener) drawPanel).setTool(ETools.POLYGON);
        } else if (e.getSource() == ellipticalButton) {
            ((DrawPanelListener) drawPanel).setTool(ETools.ELLIPTICAL);
        } else if (e.getSource() == hexagonButton) {
            ((DrawPanelListener) drawPanel).setTool(ETools.HEXAGON);
        } else if (e.getSource() == pentagonButton) {
            ((DrawPanelListener) drawPanel).setTool(ETools.PENTAGON);
        } else if (e.getSource() == triangleButton) {
            ((DrawPanelListener) drawPanel).setTool(ETools.TRIANGLE);
        } else if (e.getSource() == rectangleButton) {
            ((DrawPanelListener) drawPanel).setTool(ETools.RECTANGLE);
        } else if (e.getSource() == blackButton) {
            setLastColor(((DrawPanelListener) drawPanel).getCurrentColor());
            ((DrawPanelListener) drawPanel).setColor(SpecialColor.black);
            setCurrentColor(((DrawPanelListener) drawPanel).getCurrentColor());
        } else if (e.getSource() == garyButton) {
            setLastColor(((DrawPanelListener) drawPanel).getCurrentColor());
            ((DrawPanelListener) drawPanel).setColor(SpecialColor.gary);
            setCurrentColor(((DrawPanelListener) drawPanel).getCurrentColor());
        } else if (e.getSource() == lightGaryButton) {
            setLastColor(((DrawPanelListener) drawPanel).getCurrentColor());
            ((DrawPanelListener) drawPanel).setColor(SpecialColor.lightGary);
            setCurrentColor(((DrawPanelListener) drawPanel).getCurrentColor());
        } else if (e.getSource() == darkRedButton) {
            setLastColor(((DrawPanelListener) drawPanel).getCurrentColor());
            ((DrawPanelListener) drawPanel).setColor(SpecialColor.darkRed);
            setCurrentColor(((DrawPanelListener) drawPanel).getCurrentColor());
        } else if (e.getSource() == pinkButton) {
            setLastColor(((DrawPanelListener) drawPanel).getCurrentColor());
            ((DrawPanelListener) drawPanel).setColor(SpecialColor.pink);
            setCurrentColor(((DrawPanelListener) drawPanel).getCurrentColor());
        } else if (e.getSource() == citrusColorButton) {
            setLastColor(((DrawPanelListener) drawPanel).getCurrentColor());
            ((DrawPanelListener) drawPanel).setColor(SpecialColor.citrusColor);
            setCurrentColor(((DrawPanelListener) drawPanel).getCurrentColor());
        } else if (e.getSource() == redButton) {
            setLastColor(((DrawPanelListener) drawPanel).getCurrentColor());
            ((DrawPanelListener) drawPanel).setColor(SpecialColor.red);
            setCurrentColor(((DrawPanelListener) drawPanel).getCurrentColor());
        } else if (e.getSource() == redButton) {
            setLastColor(((DrawPanelListener) drawPanel).getCurrentColor());
            ((DrawPanelListener) drawPanel).setColor(SpecialColor.red);
            setCurrentColor(((DrawPanelListener) drawPanel).getCurrentColor());
        } else if (e.getSource() == waxyYellowButton) {
            setLastColor(((DrawPanelListener) drawPanel).getCurrentColor());
            ((DrawPanelListener) drawPanel).setColor(SpecialColor.waxyYellow);
            setCurrentColor(((DrawPanelListener) drawPanel).getCurrentColor());
        } else if (e.getSource() == mistyColorButton) {
            setLastColor(((DrawPanelListener) drawPanel).getCurrentColor());
            ((DrawPanelListener) drawPanel).setColor(SpecialColor.mistyColor);
            setCurrentColor(((DrawPanelListener) drawPanel).getCurrentColor());
        } else if (e.getSource() == orangeButton) {
            setLastColor(((DrawPanelListener) drawPanel).getCurrentColor());
            ((DrawPanelListener) drawPanel).setColor(SpecialColor.orange);
            setCurrentColor(((DrawPanelListener) drawPanel).getCurrentColor());
        } else if (e.getSource() == lightGreenButton) {
            setLastColor(((DrawPanelListener) drawPanel).getCurrentColor());
            ((DrawPanelListener) drawPanel).setColor(SpecialColor.lightGreen);
            setCurrentColor(((DrawPanelListener) drawPanel).getCurrentColor());
        } else if (e.getSource() == blueButton) {
            setLastColor(((DrawPanelListener) drawPanel).getCurrentColor());
            ((DrawPanelListener) drawPanel).setColor(SpecialColor.blue);
            setCurrentColor(((DrawPanelListener) drawPanel).getCurrentColor());
        } else if (e.getSource() == lightYellowButton) {
            setLastColor(((DrawPanelListener) drawPanel).getCurrentColor());
            ((DrawPanelListener) drawPanel).setColor(SpecialColor.lightYellow);
            setCurrentColor(((DrawPanelListener) drawPanel).getCurrentColor());
        } else if (e.getSource() == ultramarineButton) {
            setLastColor(((DrawPanelListener) drawPanel).getCurrentColor());
            ((DrawPanelListener) drawPanel).setColor(SpecialColor.ultramarine);
            setCurrentColor(((DrawPanelListener) drawPanel).getCurrentColor());
        } else if (e.getSource() == lightPurpleButton) {
            setLastColor(((DrawPanelListener) drawPanel).getCurrentColor());
            ((DrawPanelListener) drawPanel).setColor(SpecialColor.lightPurple);
            setCurrentColor(((DrawPanelListener) drawPanel).getCurrentColor());
        } else if (e.getSource() == lastColorButton) {
            setLastColor(((DrawPanelListener) drawPanel).getCurrentColor());
            ((DrawPanelListener) drawPanel).setColor(lastColorButton.getBackground());
            setCurrentColor(((DrawPanelListener) drawPanel).getCurrentColor());
        } else if (e.getSource() == nowColorButton) {
            setLastColor(((DrawPanelListener) drawPanel).getCurrentColor());
            ((DrawPanelListener) drawPanel).setColor(nowColorButton.getBackground());
            setCurrentColor(((DrawPanelListener) drawPanel).getCurrentColor());
        } else if (e.getSource() == fillButton) {
            if (fillButton.isSelected()) {
                StartUp.mainWindow.getDrawPanel().setTransparency(false);
            } else {
                StartUp.mainWindow.getDrawPanel().setTransparency(true);
            }
        } else if (e.getSource() == selectButton) {
            ((DrawPanelListener) drawPanel).setTool(ETools.SELECT);
        }
    }

    public void setLastColor(Color lastColor) {
        lastColorButton.setBackground(lastColor);
    }

    public void setCurrentColor(Color currentColor) {
        nowColorButton.setBackground(currentColor);
    }

    private void createUIComponents() {
        drawPanel = new DrawPanelListener();
    }

    public JPanel getDrawBackPanel() {
        return drawBackPanel;
    }
}