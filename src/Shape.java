import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;


interface Drawable {
    public void Draw(Graphics2D g2);
}

public abstract class Shape implements Drawable {
    public boolean transparent;

    protected int x, y;

    protected Color color;
    protected Color fillColor;
    protected BasicStroke stroke;

    protected ETools shape;

    public Shape(int x, int y, Color color, BasicStroke stroke, ETools shape) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.stroke = stroke;
        this.shape = shape;
        this.fillColor = null;
        this.transparent = true;
    }

    public Shape(int x, int y, Color color, BasicStroke stroke, ETools shape, Color fillColor, boolean transparent) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.stroke = stroke;
        this.shape = shape;
        this.fillColor = fillColor;
        this.transparent = transparent;
    }

    public ETools getShape() {
        return this.shape;
    }


    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }


    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Color getColor() {
        return this.color;
    }

    public Color getFillColor() {
        return this.fillColor;
    }

    public BasicStroke getStroke() {
        return this.stroke;
    }

    public boolean getTransparency() {
        return this.transparent;
    }

    protected abstract void draw(Graphics2D g2);

    public void Draw(Graphics2D g2) {
        g2.setColor(getColor());
        g2.setStroke(getStroke());
        draw(g2);
    }
}

class LineShape extends Shape {

    private int endX, endY;

    public LineShape(int x, int y, int endX, int endY, Color color, BasicStroke stroke) {
        super(x, y, color, stroke, ETools.LINE);
        this.endX = endX;
        this.endY = endY;
    }

    @Override
    protected void draw(Graphics2D g2) {
        g2.drawLine(x, y, endX, endY);
    }
}

class RectangleShape extends Shape {
    private int width, height;

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public RectangleShape(int x, int y, int width, int height, Color color, BasicStroke stroke) {
        super(x, y, color, stroke, ETools.RECTANGLE);
        this.width = width;
        this.height = height;
    }


    public RectangleShape(int x, int y, int width, int height, Color color, BasicStroke stroke, Color fillColor, boolean transparent) {
        super(x, y, color, stroke, ETools.RECTANGLE, fillColor, transparent);
        this.width = width;
        this.height = height;
    }


    @Override
    protected void draw(Graphics2D g2) {

        g2.drawRect(x, y, width, height);
        if (!transparent) {
            g2.setColor(getFillColor());
            g2.fillRect(x, y, width, height);
        }
    }
}

class ELLIPTICALShape extends Shape {
    private int width, height;

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public ELLIPTICALShape(int x, int y, int width, int height, Color color, BasicStroke stroke) {
        super(x, y, color, stroke, ETools.RECTANGLE);
        this.width = width;
        this.height = height;
    }


    public ELLIPTICALShape(int x, int y, int width, int height, Color color, BasicStroke stroke, Color fillColor, boolean transparent) {
        super(x, y, color, stroke, ETools.RECTANGLE, fillColor, transparent);
        this.width = width;
        this.height = height;
    }

    @Override
    protected void draw(Graphics2D g2) {

        g2.drawOval(x, y, width, height);
        if (!transparent) {
            g2.setColor(getFillColor());
            g2.fillOval(x, y, width, height);
        }
    }
}

class PentagonShape extends Shape {
    private int[] pointsX;
    private int[] pointsY;

    private int x2, y2;

    public PentagonShape(int x1, int y1, int x2, int y2, Color color, BasicStroke stroke) {
        super(x1, y1, color, stroke, ETools.PENTAGON);
        this.x2 = x2;
        this.y2 = y2;
        initPoints();
    }

    public PentagonShape(int x1, int y1, int x2, int y2, Color color, BasicStroke stroke, Color fillColor, boolean transparent) {
        super(x1, y1, color, stroke, ETools.PENTAGON, fillColor, transparent);
        this.x2 = x2;
        this.y2 = y2;
        initPoints();
    }

    private void initPoints() {
        pointsX = new int[]{
                x + x2 / 2,
                x,
                x + (int) (x2 / 4.3),
                x + (int) (x2 * 3.3 / 4.3),
                x + x2
        };
        pointsY = new int[]{
                y,
                y + (int) (((Math.sqrt(3) - 0.9) / 2) * y2),
                y + y2,
                y + y2,
                y + (int) (((Math.sqrt(3) - 0.9) / 2) * y2)
        };

    }


    @Override
    protected void draw(Graphics2D g2) {
        g2.drawPolygon(pointsX, pointsY, 5);
        if (!transparent) {
            g2.setColor(getFillColor());
            g2.fillPolygon(pointsX, pointsY, 5);
        }
    }
}

class HexagonShape extends Shape {
    private int[] pointsX;
    private int[] pointsY;

    private int x2, y2;


    public HexagonShape(int x1, int y1, int x2, int y2, Color color, BasicStroke stroke) {
        super(x1, y1, color, stroke, ETools.PENTAGON);
        this.x2 = x2;
        this.y2 = y2;
        initPoints();
    }

    public HexagonShape(int x1, int y1, int x2, int y2, Color color, BasicStroke stroke, Color fillColor, boolean transparent) {
        super(x1, y1, color, stroke, ETools.PENTAGON, fillColor, transparent);
        this.x2 = x2;
        this.y2 = y2;
        initPoints();
    }


    private void initPoints() {
        pointsX = new int[]{
                x + x2 / 4,
                x,
                x + x2 / 4,
                x + x2 * 3 / 4,
                x + x2,
                x + x2 * 3 / 4
        };
        pointsY = new int[]{
                y,
                y + y2 / 2,
                y + y2,
                y + y2,
                y + y2 / 2,
                y
        };
    }

    @Override
    protected void draw(Graphics2D g2) {
        g2.drawPolygon(pointsX, pointsY, 6);
        if (!transparent) {
            g2.setColor(getFillColor());
            g2.fillPolygon(pointsX, pointsY, 6);
        }
    }
}

class TriangleShape extends Shape {
    private int[] pointsX;
    private int[] pointsY;

    private int x2, y2;


    public TriangleShape(int x1, int y1, int x2, int y2, Color color, BasicStroke stroke) {
        super(x1, y1, color, stroke, ETools.PENTAGON);
        this.x2 = x2;
        this.y2 = y2;
        initPoints();
    }

    public TriangleShape(int x1, int y1, int x2, int y2, Color color, BasicStroke stroke, Color fillColor, boolean transparent) {
        super(x1, y1, color, stroke, ETools.PENTAGON, fillColor, transparent);
        this.x2 = x2;
        this.y2 = y2;
        initPoints();
    }

    private void initPoints() {
        pointsX = new int[]{
                x + x2 / 2,
                x,
                x + x2
        };
        pointsY = new int[]{
                y,
                y + y2,
                y + y2
        };
    }

    @Override
    protected void draw(Graphics2D g2) {
        g2.drawPolygon(pointsX, pointsY, 3);
        if (!transparent) {
            g2.setColor(getFillColor());
            g2.fillPolygon(pointsX, pointsY, 3);
        }
    }
}

class TextShape extends Shape {
    private Font font;
    private int fontSize;
    private String message;

    public TextShape(int x, int y, Color color, BasicStroke stroke, Font font, int fontSize, String message) {
        super(x, y, color, stroke, ETools.TEXT);
        this.font = font;
        this.fontSize = fontSize;
        this.message = message;
    }

    @Override
    protected void draw(Graphics2D g2) {
        g2.setFont(font);
        g2.drawString(message, x, y);
    }
}

class Pencil implements Drawable {
    private ArrayList<LineShape> lines = new ArrayList<>();

    @Override
    public void Draw(Graphics2D g2) {
        for (LineShape line : lines) {
            line.Draw(g2);
        }
    }

    public void addLine(LineShape line) {
        lines.add(line);
    }

    public void popLine() {
        lines.remove(lines.size() - 1);
    }

    public void clear() {
        lines.clear();
    }

    public int getLines() {
        return lines.size();
    }
}