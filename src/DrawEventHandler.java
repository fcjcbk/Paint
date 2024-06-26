import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Stack;

public class DrawEventHandler extends JPanel implements MouseListener, MouseMotionListener {

    public static boolean isInCanvas = false;
    private int x1, y1, x2, y2; // 当前坐标与上一次操作的坐标
    private int currentX, currentY; // 实时坐标
    private boolean dragged = false; // 是否拖动
    private Color currentColor;
    private Color lastColor;
    private boolean transparent;
    private int grouped;
    private BasicStroke stroke = new BasicStroke((float) 2);
    private ArrayList<Drawable> graphics; // 当前画板上的图形
    private ArrayList<Drawable> copyBuffer; // 复制图形的缓冲区
    private int copyX, copyY; // 复制图形的坐标

    private Stack<Drawable> removed; // 回收站，暂存被删除的图形
    private Drawable currentState; // 当前正在操作的图形
    private BufferedImage canvas;
    private Graphics2D graphics2D; // 画图
    private ETools activeTool; // 当前工具
    private TextDialog textDialog;

    public DrawEventHandler(int x, int y) {
        setSize(x, y);
        Dimension temp = new Dimension(x, y);
        setMaximumSize(temp);
        setMinimumSize(temp);
        setBorder(BorderFactory.createLineBorder(Color.lightGray));
        setBackground(Color.white);

        addMouseListener(this);
        addMouseMotionListener(this);
        requestFocus();
        activeTool = ETools.PENCIL;
        currentColor = Color.BLACK;
        lastColor = Color.WHITE;
        textDialog = new TextDialog(StartUp.mainWindow);

        this.graphics = new ArrayList<>();
        this.removed = new Stack<Drawable>();
        this.grouped = 1;
        this.transparent = true;
    }

    public DrawEventHandler() {
        setSize(1100, 550);
        Dimension temp = new Dimension(1100, 550);
        setMaximumSize(temp);
        setMinimumSize(temp);
        setBorder(BorderFactory.createLineBorder(Color.lightGray));
        setBackground(Color.white);

        addMouseListener(this);
        addMouseMotionListener(this);
        requestFocus();
        activeTool = ETools.PENCIL;
        //System.out.println("create");
        currentColor = Color.BLACK;
        lastColor = Color.WHITE;
        textDialog = new TextDialog(StartUp.mainWindow);

        this.graphics = new ArrayList<>();
        this.removed = new Stack<Drawable>();
        this.grouped = 1;
        this.transparent = true;
    }


    public void changeDrawPanelSize(int width, int height) {
        graphics.clear();
        removed.clear();
        currentState = null;

        Dimension temp = new Dimension(width, height);
        setSize(temp);
        setMaximumSize(temp);
        setMinimumSize(temp);

        repaint();
        StartUp.mainWindow.setCanvasSizeLabel(width, height);
    }

    @Override
    public void paintComponent(Graphics g) {
        //System.out.println(activeTool.toString() + "pc");
        Dimension dimension = StartUp.mainWindow.getDrawPanel().getSize();
        if (canvas == null) {
            canvas = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_ARGB);
            graphics2D = canvas.createGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            clear();
        }
        g.drawImage(canvas, 0, 0, null);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (Drawable s : graphics) {
            s.Draw(g2);
        }
        if (currentState != null) {
            currentState.Draw(g2);
        }

    }

    public void setTool(ETools tool) {
        this.activeTool = tool;
    }

    public void setImage(BufferedImage image) {
        graphics2D.dispose();
        this.setInkPanel(image.getWidth(), image.getHeight());
        canvas = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        graphics2D = canvas.createGraphics();
        graphics2D.drawImage(image, 0, 0, null);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    public void setInkPanel(int width, int height) {
        canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        graphics2D = canvas.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        this.setSize(width - 3, height - 3);
        this.setPreferredSize(new Dimension(width - 3, height - 3));
        clear();

    }

    public void clear() {
        Dimension dimension = StartUp.mainWindow.getDrawPanel().getSize();
        graphics2D.setPaint(Color.white);
        graphics2D.fillRect(0, 0, dimension.width, dimension.height);
        graphics.clear();
        removed.removeAllElements();
        StartUp.mainWindow.getDrawPanel().repaint();
        graphics2D.setColor(currentColor);
    }


    public void setColor(Color c) {
        lastColor = currentColor;
        currentColor = c;
        graphics2D.setColor(c);

        if (currentState != null && currentState.getShape() == ETools.SELECT) {
            Selected s = (Selected) currentState;
            s.setColor(c);
            repaint();
        }
    }

    public void setThickness(float f) {
        stroke = new BasicStroke(f);
        graphics2D.setStroke(stroke);

        if (currentState != null && currentState.getShape() == ETools.SELECT) {
            Selected s = (Selected) currentState;
            s.setStroke(stroke);
            repaint();
        }
    }

    public void setTransparency(Boolean b) {
        this.transparent = b;
    }

    public void floodFill(Point2D.Double point, Color fillColor) {
//        Color targetColor = new Color(canvas.getRGB((int) point.getX(), (int) point.getY()));
//        Queue<Point2D.Double> queue = new LinkedList<Point2D.Double>();
//        queue.add(point);
//        if (!targetColor.equals(fillColor)) ;
//        while (!queue.isEmpty()) {
//            Point2D.Double p = queue.remove();
//
//            if ((int) p.getX() >= 0 && (int) p.getX() < canvas.getWidth() &&
//                    (int) p.getY() >= 0 && (int) p.getY() < canvas.getHeight())
//                if (canvas.getRGB((int) p.getX(), (int) p.getY()) == targetColor.getRGB()) {
//                    canvas.setRGB((int) p.getX(), (int) p.getY(), fillColor.getRGB());
//                    queue.add(new Point2D.Double(p.getX() - 1, p.getY()));
//                    queue.add(new Point2D.Double(p.getX() + 1, p.getY()));
//                    queue.add(new Point2D.Double(p.getX(), p.getY() - 1));
//                    queue.add(new Point2D.Double(p.getX(), p.getY() + 1));
//                }
//        }
    }

    private Drawable popGraphics() {
        Drawable d = graphics.get(graphics.size() - 1);
        graphics.remove(graphics.size() - 1);
        return d;
    }

    private void pushGraphics(Drawable d) {
        graphics.add(d);
    }

    public void quash() {
        if (graphics.size() <= 0) {
            return;
        }

        Drawable lastRemoved = popGraphics();
        removed.push(lastRemoved);
        repaint();
    }

    public void recover() {
        pushGraphics(removed.pop());
        repaint();
    }

    public void delete() {
        if (currentState == null || activeTool != ETools.SELECT || currentState.getShape() != ETools.SELECT) {
            return;
        }
        Selected s = (Selected) currentState;
        s.delete(graphics);
        currentState = null;
        repaint();
    }

    public void copy() {
        if (currentState == null || activeTool != ETools.SELECT || currentState.getShape() != ETools.SELECT) {
            return;
        }
        Selected s = (Selected) currentState;

        copyBuffer = s.getShapesCopy();
        copyX = s.getX() + s.getWidth() / 2;
        copyY = s.getY() + s.getHeight() / 2;
    }

    public void paste() {
        if (copyBuffer == null) {
            return;
        }
        for (Drawable d : copyBuffer) {
            Drawable cloneD = d.getClone();
            cloneD.move(currentX - copyX, currentY - copyY);
            graphics.add(cloneD);

        }
        repaint();

    }


    public Color getCurrentColor() {
        return currentColor;
    }

    private int calcDistance(Dimension a, Dimension b) {
        return (int) Math.sqrt((a.width - b.width) * (a.width - b.width)
                + (a.height - b.height) * (a.height - b.height));
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (activeTool == ETools.STRAW) {
            try {
                int x = e.getX(), y = e.getY();
                x += StartUp.mainWindow.getLocation().x;
                y += StartUp.mainWindow.getLocation().y;
                x += StartUp.mainWindow.getDrawBackPanel().getLocation().x;
                y += StartUp.mainWindow.getDrawBackPanel().getLocation().y;
                x += getLocation().x;
                y += getLocation().y;
                x += 8;
                y += 31;
                //
                System.out.println("isHere");
                Robot robot = new Robot();
                System.out.println(x + " " + y);
                Color color = robot.getPixelColor(x, y);
                System.out.println(color.toString());
                StartUp.mainWindow.setLastColor(getCurrentColor());
                setColor(color);
                StartUp.mainWindow.setCurrentColor(getCurrentColor());
            } catch (AWTException ex) {
                ex.printStackTrace();
            }
        } else if (activeTool == ETools.POLYGON) {
            if (currentState == null) {
                Polygon p = new Polygon(currentColor, stroke);
                p.addPoint(x1, y1);
                currentState = p;
                repaint();
                return;
            }

            Polygon p = (Polygon) currentState;
            p.addPoint(x1, y1);
            p.setPreviewPoint(x1, y1);

            if (e.getClickCount() == 2 && p.size() > 2) {
                p.setFinish(true);
                pushGraphics(p);
                currentState = null;
            }
            repaint();

        } else if (activeTool == ETools.SELECT) {
            currentState = null;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        x1 = e.getX();
        y1 = e.getY();
        switch (activeTool) {
            case PENCIL:
                currentState = new Pencil();
                break;
            case SELECT:
                if (currentState == null || currentState.getShape() != ETools.SELECT) {
                    return;
                }
                Rectangle bounds = currentState.getBounds();
                Selected s = (Selected) currentState;

                int tolerance = 1;
                if (s.getBounds().getMaxX() - e.getX() <= 10 && s.getBounds().getMaxY() - e.getY() <= 10) {
                    s.setDirection(Direction.RIGHT);
                } else if (Math.abs(x1 - bounds.x) <= tolerance && y1 >= bounds.y && y1 <= bounds.y + bounds.height) {
                    s.setDirection(Direction.LEFT);
                } else if (Math.abs(x1 - (bounds.x + bounds.width)) <= tolerance && y1 >= bounds.y && y1 <= bounds.y + bounds.height) {
                    s.setDirection(Direction.RIGHT);
                } else if (Math.abs(y1 - bounds.y) <= tolerance && x1 >= bounds.x && x1 <= bounds.x + bounds.width) {
                    s.setDirection(Direction.UP);
                } else if (Math.abs(y1 - (bounds.y + bounds.height)) <= tolerance && x1 >= bounds.x && x1 <= bounds.x + bounds.width) {
                    s.setDirection(Direction.DOWN);
                } else if (x1 >= bounds.x && x1 < bounds.x + bounds.width && y1 >= bounds.y && y1 < bounds.y + bounds.height) {
                    s.setMoving(true);
                }
                break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (currentState != null && activeTool != ETools.POLYGON && activeTool != ETools.SELECT) {
            pushGraphics(currentState);
            currentState = null;
        } else if (activeTool == ETools.TEXT) {
            int result = textDialog.showCustomDialog(StartUp.mainWindow);
            if (result == TextDialog.APPLY_OPTION) {
                pushGraphics(new TextShape(x1, y1, currentColor, stroke,
                        textDialog.getFont(), textDialog.getInputSize(), textDialog.getText()));
            }
        } else if (activeTool == ETools.BUCKET) {
            floodFill(new Point2D.Double(x1, y1), currentColor);
        } else if (activeTool == ETools.SELECT) {
            if (currentState != null) {
                if (currentState.getShape() == ETools.SELECT) {
                    Selected s = (Selected) currentState;
                    s.setMoving(false);
                    s.setDirection(null);
                } else {
                    Selected selected = new Selected((RectangleShape) currentState, graphics);
                    currentState = selected;
                    if (selected.getSelectedSize() == 0) {
                        currentState = null;
                    }
                }
            }

        }
        if (dragged) {
            grouped++;
            dragged = false;
        }
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        isInCanvas = true;

        Cursor cursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
        setCursor(cursor);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        isInCanvas = false;
        StartUp.mainWindow.setMousePosLabel(0, 0);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        //System.out.println(grouped);
        StartUp.mainWindow.setMousePosLabel(e.getX(), e.getY());

        Color primary = currentColor;
        Color secondary = lastColor;
        if (SwingUtilities.isRightMouseButton(e)) {
            primary = secondary;
            secondary = currentColor;
        }
        x2 = e.getX();
        y2 = e.getY();
        this.dragged = true;
        //System.out.println(activeTool.toString())

        switch (activeTool) {
            case ERASER:
                pushGraphics(new LineShape(x1, y1, x2, y2, Color.white, stroke));
                StartUp.mainWindow.getDrawPanel().repaint();
                x1 = x2;
                y1 = y2;
                break;
            case PENCIL:
                // Todo: 需要对 pencil 做特殊定义
                Pencil p = (Pencil) currentState;
                p.addLine(new LineShape(x1, y1, x2, y2, primary, stroke));
                StartUp.mainWindow.getDrawPanel().repaint();
                x1 = x2;
                y1 = y2;
                break;
            case LINE:
                currentState = new LineShape(x1, y1, x2, y2, primary, stroke);
                StartUp.mainWindow.getDrawPanel().repaint();
                break;
            case RECTANGLE:
                RectangleShape rectangleShape = null;
                if (x1 < x2 && y1 < y2) {
                    rectangleShape = new RectangleShape(x1, y1, x2 - x1, y2 - y1, primary, stroke, secondary, transparent);
                } else if (x2 < x1 && y1 < y2) {
                    rectangleShape = new RectangleShape(x2, y1, x1 - x2, y2 - y1, primary, stroke, secondary, transparent);
                } else if (x1 < x2 && y2 < y1) {
                    rectangleShape = new RectangleShape(x1, y2, x2 - x1, y1 - y2, primary, stroke, secondary, transparent);
                } else if (x2 < x1 && y2 < y1) {
                    rectangleShape = new RectangleShape(x2, y2, x1 - x2, y1 - y2, primary, stroke, secondary, transparent);
                }

                if (rectangleShape != null && e.isShiftDown()) {
                    rectangleShape.setHeight(rectangleShape.getWidth());
                }
                currentState = rectangleShape;
                StartUp.mainWindow.getDrawPanel().repaint();
                break;
            case SELECT:
                if (currentState != null && currentState.getShape() == ETools.SELECT) {
                    Selected s = (Selected) currentState;
                    if (s.isMoving()) {
                        s.move(x2 - x1, y2 - y1);
                        x1 = x2;
                        y1 = y2;
                        StartUp.mainWindow.getDrawPanel().repaint();
                        return;
                    } else if (s.getDirection() != null) {
                        switch (s.getDirection()) {
                            case LEFT:
                                s.scale(x2, y2, 0, 0, 0, 0, null);
                                break;
                            case RIGHT:
                                if (e.isShiftDown()) {
                                    s.scale(x2, x2 - x1 + y1, 0, 0, 0, 0, null);

                                } else {
                                    s.scale(x2, y2, 0, 0, 0, 0, null);
                                }
                                break;
                            case UP:
                                s.scale(x2, y2, 0, 0, 0, 0, null);
                                break;
                            case DOWN:
                                s.scale(x2, y2, 0, 0, 0, 0, null);
                                break;
                        }
                        StartUp.mainWindow.getDrawPanel().repaint();
                        return;
                    }
                }
                RectangleShape selectShape = null;
                float[] dashPattern = {10, 10};  // 10 pixel line, 10 pixel space
                BasicStroke dashedStroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0);
                if (x1 < x2 && y1 < y2) {
                    selectShape = new RectangleShape(x1, y1, x2 - x1, y2 - y1, Color.BLACK, dashedStroke, secondary, true);
                } else if (x2 < x1 && y1 < y2) {
                    selectShape = new RectangleShape(x2, y1, x1 - x2, y2 - y1, Color.BLACK, dashedStroke, secondary, true);
                } else if (x1 < x2 && y2 < y1) {
                    selectShape = new RectangleShape(x1, y2, x2 - x1, y1 - y2, Color.BLACK, dashedStroke, secondary, true);
                } else if (x2 < x1 && y2 < y1) {
                    selectShape = new RectangleShape(x2, y2, x1 - x2, y1 - y2, Color.BLACK, dashedStroke, secondary, true);
                } else  {
                    break;
                }

                currentState = selectShape;
                StartUp.mainWindow.getDrawPanel().repaint();
                break;
            case ELLIPTICAL:
                EllipticalShape ellipticalShape = null;
                if (x1 < x2 && y1 < y2) {
                    ellipticalShape = new EllipticalShape(x1, y1, x2 - x1, y2 - y1, primary, stroke, secondary, transparent);
                } else if (x2 < x1 && y1 < y2) {
                    ellipticalShape = new EllipticalShape(x2, y1, x1 - x2, y2 - y1, primary, stroke, secondary, transparent);
                } else if (x1 < x2 && y2 < y1) {
                    ellipticalShape = new EllipticalShape(x1, y2, x2 - x1, y1 - y2, primary, stroke, secondary, transparent);
                } else if (x2 < x1 && y2 < y1) {
                    ellipticalShape = new EllipticalShape(x2, y2, x1 - x2, y1 - y2, primary, stroke, secondary, transparent);
                }
                if (ellipticalShape != null && e.isShiftDown()) {
                    ellipticalShape.setHeight(ellipticalShape.getWidth());
                }

                currentState = ellipticalShape;
                StartUp.mainWindow.getDrawPanel().repaint();
                break;
            case PENTAGON:
                PentagonShape pentagonShape = null;

                if (x1 < x2 && y1 < y2) {
                    pentagonShape = new PentagonShape(x1, y1, x2 - x1, y2 - y1, primary, stroke, secondary, transparent);
                } else if (x2 < x1 && y1 < y2) {
                    pentagonShape = new PentagonShape(x2, y1, x1 - x2, y2 - y1, primary, stroke, secondary, transparent);
                } else if (x1 < x2 && y2 < y1) {
                    pentagonShape = new PentagonShape(x1, y2, x2 - x1, y1 - y2, primary, stroke, secondary, transparent);
                } else if (x2 < x1 && y2 < y1) {
                    pentagonShape = new PentagonShape(x2, y2, x1 - x2, y1 - y2, primary, stroke, secondary, transparent);
                }
                currentState = pentagonShape;
                StartUp.mainWindow.getDrawPanel().repaint();
                break;
            case HEXAGON:
                HexagonShape hexagonShape = null;

                if (x1 < x2 && y1 < y2) {
                    hexagonShape = new HexagonShape(x1, y1, x2 - x1, y2 - y1, primary, stroke, secondary, transparent);
                } else if (x2 < x1 && y1 < y2) {
                    hexagonShape = new HexagonShape(x2, y1, x1 - x2, y2 - y1, primary, stroke, secondary, transparent);
                } else if (x1 < x2 && y2 < y1) {
                    hexagonShape = new HexagonShape(x1, y2, x2 - x1, y1 - y2, primary, stroke, secondary, transparent);
                } else if (x2 < x1 && y2 < y1) {
                    hexagonShape = new HexagonShape(x2, y2, x1 - x2, y1 - y2, primary, stroke, secondary, transparent);
                }
                currentState = hexagonShape;
                StartUp.mainWindow.getDrawPanel().repaint();
                break;
            case TRIANGLE:
                TriangleShape triangleShape = null;
                if (x1 < x2 && y1 < y2) {
                    triangleShape = new TriangleShape(x1, y1, x2 - x1, y2 - y1, primary, stroke, secondary, transparent);
                } else if (x2 < x1 && y1 < y2) {
                    triangleShape = new TriangleShape(x2, y1, x1 - x2, y2 - y1, primary, stroke, secondary, transparent);
                } else if (x1 < x2 && y2 < y1) {
                    triangleShape = new TriangleShape(x1, y2, x2 - x1, y1 - y2, primary, stroke, secondary, transparent);
                } else if (x2 < x1 && y2 < y1) {
                    triangleShape = new TriangleShape(x2, y2, x1 - x2, y1 - y2, primary, stroke, secondary, transparent);
                }
                currentState = triangleShape;
                StartUp.mainWindow.getDrawPanel().repaint();
                break;
        }

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        StartUp.mainWindow.setMousePosLabel(e.getX(), e.getY());
        currentX = e.getX();
        currentY = e.getY();
        if (activeTool == ETools.POLYGON && currentState != null) {
            Polygon p = (Polygon) currentState;
            p.setPreviewPoint(e.getX(), e.getY());
            repaint();
        }

    }
}
