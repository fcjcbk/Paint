import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


interface Drawable {
    void Draw(Graphics2D g2);

    Rectangle getBounds();

    ETools getShape();

    void move(int dx, int dy);

    void scaleX(boolean isLeft, int dx);

    void scaleY(boolean isTop, int dy);

    void setStroke(BasicStroke stroke);

    void setColor(Color color);

    void scale(int rectangleX, int rectangleY, double scaleX, double scaleY, int ox, int oy, Drawable drawable);

    Drawable getClone();
}

public abstract class BasicShape implements Drawable {
    public boolean transparent;

    protected int x, y;

    protected Color color;
    protected Color fillColor;
    protected BasicStroke stroke;

    protected ETools shape;

    public BasicShape(int x, int y, Color color, BasicStroke stroke, ETools shape) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.stroke = stroke;
        this.shape = shape;
        this.fillColor = null;
        this.transparent = true;
    }

    public BasicShape(int x, int y, Color color, BasicStroke stroke, ETools shape, Color fillColor, boolean transparent) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.stroke = stroke;
        this.shape = shape;
        this.fillColor = fillColor;
        this.transparent = transparent;
    }

    @Override
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

    @Override
    public void setStroke(BasicStroke stroke) {
        this.stroke = stroke;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    public boolean getTransparency() {
        return this.transparent;
    }

    protected abstract void draw(Graphics2D g2);

    @Override
    public void Draw(Graphics2D g2) {
        g2.setColor(getColor());
        g2.setStroke(getStroke());
        draw(g2);
    }
}

class LineShape extends BasicShape {

    private int endX, endY;

    public LineShape(int x, int y, int endX, int endY, Color color, BasicStroke stroke) {
        super(x, y, color, stroke, ETools.LINE);
        this.endX = endX;
        this.endY = endY;
    }

    @Override
    public void move(int dx, int dy) {
        x += dx;
        y += dy;
        endX += dx;
        endY += dy;
    }

    @Override
    protected void draw(Graphics2D g2) {
        g2.drawLine(x, y, endX, endY);
    }

    @Override
    public Rectangle getBounds() {
        int nx = Math.min(x, endX);
        int ny = Math.min(y, endY);

        int nw = Math.abs(x - endX);
        int nh = Math.abs(y - endY);
        return new Rectangle(nx, ny, nw, nh);
    }


    public LineShape(LineShape rhs) {
        super(rhs.x, rhs.y, rhs.color, rhs.stroke, rhs.shape);
        this.endX = rhs.endX;
        this.endY = rhs.endY;
    }

    @Override
    public void scale(int rectangleX, int rectangleY, double scaleX, double scaleY, int ox, int oy, Drawable drawable) {
        LineShape originalLine = (LineShape) drawable;
        x = (int) ((originalLine.x - ox) * scaleX + rectangleX);
        y = (int) ((originalLine.y - oy) * scaleY + rectangleY);
        endX = (int) ((originalLine.endX - ox) * scaleX + rectangleX);
        endY = (int) ((originalLine.endY - oy) * scaleY + rectangleY);
    }

    @Override
    public Drawable getClone() {
        return new LineShape(this);
    }


    @Override
    public void scaleX(boolean isLeft, int dx) {
        Path2D.Double path = new Path2D.Double();
        path.moveTo(x, y);
        path.lineTo(endX, endY);

        double factor = isLeft ? 1 : -1;
        int width = (int) path.getBounds2D().getWidth();
        factor *= (double) (width + dx) / width;

        AffineTransform at = new AffineTransform();
        at.scale(factor, 1.0);

        Shape transformedShape = at.createTransformedShape(path);
        PathIterator iterator = transformedShape.getPathIterator(null);
        double[] coords = new double[6];
        iterator.currentSegment(coords);
        x = (int) coords[0];
        y = (int) coords[1];
        iterator.next();
        iterator.currentSegment(coords);
        endX = (int) coords[0];
        endY = (int) coords[1];
    }

    @Override
    public void scaleY(boolean isTop, int dy) {
        Path2D.Double path = new Path2D.Double();
        path.moveTo(x, y);
        path.lineTo(endX, endY);

        double factor = isTop ? 1 : -1;
        int height = (int) path.getBounds2D().getHeight();
        factor *= (double) (height + dy) / height;

        AffineTransform at = new AffineTransform();
        at.scale(1, factor);

        Shape transformedShape = at.createTransformedShape(path);
        PathIterator iterator = transformedShape.getPathIterator(null);
        double[] coords = new double[6];
        iterator.currentSegment(coords);
        x = (int) coords[0];
        y = (int) coords[1];
        iterator.next();
        iterator.currentSegment(coords);
        endX = (int) coords[0];
        endY = (int) coords[1];
    }
}

class RectangleShape extends BasicShape {
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

    public RectangleShape(RectangleShape rhs) {
        super(rhs.x, rhs.y, rhs.color, rhs.stroke, ETools.RECTANGLE);
        this.width = rhs.width;
        this.height = rhs.height;
    }


    public RectangleShape(int x, int y, int width, int height, Color color, BasicStroke stroke, Color fillColor, boolean transparent) {
        super(x, y, color, stroke, ETools.RECTANGLE, fillColor, transparent);
        this.width = width;
        this.height = height;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    @Override
    protected void draw(Graphics2D g2) {

        g2.drawRect(x, y, width, height);
        if (!transparent) {
            g2.setColor(getFillColor());
            g2.fillRect(x, y, width, height);
        }
    }

    @Override
    public void move(int dx, int dy) {
        x += dx;
        y += dy;
    }

//    @Override
//    public void scale(double factor) {
//        width = (int)(width * factor);
//        height = (int)(height * factor);
//    }

    @Override
    public void scaleX(boolean isLeft, int dx) {
        width += dx;

        if (isLeft) {
            x = x - dx;
        }
    }

    @Override
    public void scaleY(boolean isTop, int dy) {
        height += dy;
        if (isTop) {
            y -= dy;
        }
    }

    @Override
    public void scale(int rectangleX, int rectangleY, double scaleX, double scaleY, int ox, int oy, Drawable drawable) {
        RectangleShape original = (RectangleShape) drawable;
        x = (int) ((original.x - ox) * scaleX + rectangleX);
        y = (int) ((original.y - oy) * scaleY + rectangleY);

        width = (int) (original.width * scaleX);
        height = (int) (original.height * scaleY);
    }

    @Override
    public Drawable getClone() {
        return new RectangleShape(this);
    }
}

class EllipticalShape extends BasicShape {
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

    public EllipticalShape(int x, int y, int width, int height, Color color, BasicStroke stroke) {
        super(x, y, color, stroke, ETools.RECTANGLE);
        this.width = width;
        this.height = height;
    }


    public EllipticalShape(int x, int y, int width, int height, Color color, BasicStroke stroke, Color fillColor, boolean transparent) {
        super(x, y, color, stroke, ETools.RECTANGLE, fillColor, transparent);
        this.width = width;
        this.height = height;
    }

    public EllipticalShape(EllipticalShape rhs) {
        super(rhs.x, rhs.y, rhs.color, rhs.stroke, ETools.RECTANGLE);
        this.width = rhs.width;
        this.height = rhs.height;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    @Override
    protected void draw(Graphics2D g2) {

        g2.drawOval(x, y, width, height);
        if (!transparent) {
            g2.setColor(getFillColor());
            g2.fillOval(x, y, width, height);
        }
    }

    @Override
    public void move(int dx, int dy) {
        x += dx;
        y += dy;
    }

//    @Override
//    public void scale(double factor) {
//        width = (int)(width * factor);
//        height = (int)(height * factor);
//    }

    @Override
    public void scaleX(boolean isLeft, int dx) {
        width += dx;

        if (isLeft) {
            x = x - dx;
        }
    }

    @Override
    public void scaleY(boolean isTop, int dy) {
        height += dy;
        if (isTop) {
            y -= dy;
        }
    }

    @Override
    public void scale(int rectangleX, int rectangleY, double scaleX, double scaleY, int ox, int oy, Drawable drawable) {
        EllipticalShape original = (EllipticalShape) drawable;
        x = (int) ((original.x - ox) * scaleX + rectangleX);
        y = (int) ((original.y - oy) * scaleY + rectangleY);

        width = (int) (original.width * scaleX);
        height = (int) (original.height * scaleY);
    }

    @Override
    public Drawable getClone() {
        return new EllipticalShape(this);
    }
}

class PentagonShape extends BasicShape {
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

    public PentagonShape(PentagonShape rhs) {
        super(rhs.x, rhs.y, rhs.color, rhs.stroke, ETools.PENTAGON);
        this.x2 = rhs.x2;
        this.y2 = rhs.y2;
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
    public Rectangle getBounds() {
        int minX = Arrays.stream(pointsX).min().getAsInt();
        int minY = Arrays.stream(pointsY).min().getAsInt();
        int maxX = Arrays.stream(pointsX).max().getAsInt();
        int maxY = Arrays.stream(pointsY).max().getAsInt();

        return new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }


    @Override
    protected void draw(Graphics2D g2) {
        g2.drawPolygon(pointsX, pointsY, 5);
        if (!transparent) {
            g2.setColor(getFillColor());
            g2.fillPolygon(pointsX, pointsY, 5);
        }
    }

    @Override
    public void move(int dx, int dy) {
        for (int i = 0; i < pointsX.length; i++) {
            pointsX[i] += dx;
        }

        for (int i = 0; i < pointsY.length; i++) {
            pointsY[i] += dy;
        }
    }


    @Override
    public void scale(int rectangleX, int rectangleY, double scaleX, double scaleY, int ox, int oy, Drawable drawable) {
        PentagonShape original = (PentagonShape) drawable;

        x = (int) ((original.x - ox) * scaleX + rectangleX);
        y = (int) ((original.y - oy) * scaleY + rectangleY);

        for (int i = 0; i < pointsX.length; i++) {
            pointsX[i] = (int) ((original.pointsX[i] - ox) * scaleX + rectangleX);
        }

        for (int i = 0; i < pointsY.length; i++) {
            pointsY[i] = (int) ((original.pointsY[i] - oy) * scaleY + rectangleY);
        }
    }


    @Override
    public void scaleX(boolean isLeft, int dx) {
        int centerX = 0;

        for (int i = 0; i < pointsX.length; i++) {
            centerX += pointsX[i];
        }

        centerX /= pointsX.length;

        for (int i = 0; i < pointsX.length; i++) {
            if (isLeft && pointsX[i] < centerX || !isLeft && pointsX[i] > centerX) {
                pointsX[i] = centerX + dx;
            }
        }

    }

    @Override
    public void scaleY(boolean isTop, int dy) {
        int centerY = 0;

        for (int i = 0; i < pointsY.length; i++) {
            centerY += pointsY[i];
        }

        centerY /= pointsY.length;

        for (int i = 0; i < pointsY.length; i++) {
            if (isTop && pointsY[i] < centerY || !isTop && pointsY[i] > centerY) {
                pointsY[i] = centerY + dy;
            }
        }

    }

    @Override
    public Drawable getClone() {
        return new PentagonShape(this);
    }
}

class HexagonShape extends BasicShape {
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

    public HexagonShape(HexagonShape rhs) {
        super(rhs.x, rhs.y, rhs.color, rhs.stroke, ETools.PENTAGON);
        this.x2 = rhs.x2;
        this.y2 = rhs.y2;
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
    public Rectangle getBounds() {
        int minX = Arrays.stream(pointsX).min().getAsInt();
        int minY = Arrays.stream(pointsY).min().getAsInt();
        int maxX = Arrays.stream(pointsX).max().getAsInt();
        int maxY = Arrays.stream(pointsY).max().getAsInt();

        return new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }

    @Override
    protected void draw(Graphics2D g2) {
        g2.drawPolygon(pointsX, pointsY, 6);
        if (!transparent) {
            g2.setColor(getFillColor());
            g2.fillPolygon(pointsX, pointsY, 6);
        }
    }

    @Override
    public void move(int dx, int dy) {
        for (int i = 0; i < pointsX.length; i++) {
            pointsX[i] += dx;
        }

        for (int i = 0; i < pointsY.length; i++) {
            pointsY[i] += dy;
        }
    }

    @Override
    public void scaleX(boolean isLeft, int dx) {
        int centerX = 0;

        for (int i = 0; i < pointsX.length; i++) {
            centerX += pointsX[i];
        }

        centerX /= pointsX.length;

        for (int i = 0; i < pointsX.length; i++) {
            if (isLeft && pointsX[i] < centerX || !isLeft && pointsX[i] > centerX) {
                pointsX[i] = centerX + dx;
            }
        }

    }

    @Override
    public void scaleY(boolean isTop, int dy) {
        int centerY = 0;

        for (int i = 0; i < pointsY.length; i++) {
            centerY += pointsY[i];
        }

        centerY /= pointsY.length;

        for (int i = 0; i < pointsY.length; i++) {
            if (isTop && pointsY[i] < centerY || !isTop && pointsY[i] > centerY) {
                pointsY[i] = centerY + dy;
            }
        }

    }

    @Override
    public void scale(int rectangleX, int rectangleY, double scaleX, double scaleY, int ox, int oy, Drawable drawable) {
        HexagonShape original = (HexagonShape) drawable;

        x = (int) ((original.x - ox) * scaleX + rectangleX);
        y = (int) ((original.y - oy) * scaleY + rectangleY);

        for (int i = 0; i < pointsX.length; i++) {
            pointsX[i] = (int) ((original.pointsX[i] - ox) * scaleX + rectangleX);
        }

        for (int i = 0; i < pointsY.length; i++) {
            pointsY[i] = (int) ((original.pointsY[i] - oy) * scaleY + rectangleY);
        }
    }


    @Override
    public Drawable getClone() {
        try {
            return (Drawable) this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}

class TriangleShape extends BasicShape {
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

    public TriangleShape(TriangleShape rhs) {
        super(rhs.x, rhs.y, rhs.color, rhs.stroke, ETools.PENTAGON);
        this.x2 = rhs.x2;
        this.y2 = rhs.y2;
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
    public Rectangle getBounds() {
        int minX = Arrays.stream(pointsX).min().getAsInt();
        int minY = Arrays.stream(pointsY).min().getAsInt();
        int maxX = Arrays.stream(pointsX).max().getAsInt();
        int maxY = Arrays.stream(pointsY).max().getAsInt();

        return new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }

    @Override
    protected void draw(Graphics2D g2) {
        g2.drawPolygon(pointsX, pointsY, 3);
        if (!transparent) {
            g2.setColor(getFillColor());
            g2.fillPolygon(pointsX, pointsY, 3);
        }
    }

    @Override
    public void move(int dx, int dy) {
        for (int i = 0; i < pointsX.length; i++) {
            pointsX[i] += dx;
        }

        for (int i = 0; i < pointsY.length; i++) {
            pointsY[i] += dy;
        }
    }

    @Override
    public void scaleX(boolean isLeft, int dx) {
        int centerX = 0;

        for (int i = 0; i < pointsX.length; i++) {
            centerX += pointsX[i];
        }

        centerX /= pointsX.length;

        for (int i = 0; i < pointsX.length; i++) {
            if (isLeft && pointsX[i] < centerX || !isLeft && pointsX[i] > centerX) {
                pointsX[i] = centerX + dx;
            }
        }

    }

    @Override
    public void scaleY(boolean isTop, int dy) {
        int centerY = 0;

        for (int i = 0; i < pointsY.length; i++) {
            centerY += pointsY[i];
        }

        centerY /= pointsY.length;

        for (int i = 0; i < pointsY.length; i++) {
            if (isTop && pointsY[i] < centerY || !isTop && pointsY[i] > centerY) {
                pointsY[i] = centerY + dy;
            }
        }

    }

    @Override
    public void scale(int rectangleX, int rectangleY, double scaleX, double scaleY, int ox, int oy, Drawable drawable) {
        TriangleShape original = (TriangleShape) drawable;

        x = (int) ((original.x - ox) * scaleX + rectangleX);
        y = (int) ((original.y - oy) * scaleY + rectangleY);

        for (int i = 0; i < pointsX.length; i++) {
            pointsX[i] = (int) ((original.pointsX[i] - ox) * scaleX + rectangleX);
        }

        for (int i = 0; i < pointsY.length; i++) {
            pointsY[i] = (int) ((original.pointsY[i] - oy) * scaleY + rectangleY);
        }
    }

    @Override
    public Drawable getClone() {
        return new TriangleShape(this);
    }
}

class TextShape extends BasicShape {
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

    @Override
    public Rectangle getBounds() {
        // Todo: 此处增加 text 的实现
        return new Rectangle(x, y, 0, 0);
    }

    @Override
    public void move(int dx, int dy) {
        x += dx;
        y += dy;
    }


    @Override
    public void scaleX(boolean isLeft, int dx) {

    }

    @Override
    public void scaleY(boolean isTop, int dy) {

    }

    @Override
    public void scale(int rectangleX, int rectangleY, double scaleX, double scaleY, int ox, int oy, Drawable drawable) {
    }


    @Override
    public Drawable getClone() {
        try {
            return (Drawable) this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}

class Pencil implements Drawable {
    private ArrayList<LineShape> lines = new ArrayList<>();

    public Pencil() {
    }

    public Pencil(Pencil pencil) {
        lines = new ArrayList<>();
        for (LineShape line : pencil.lines) {
            lines.add((LineShape) line.getClone());
        }

    }

    @Override
    public ETools getShape() {
        return ETools.PENCIL;
    }

    @Override
    public void Draw(Graphics2D g2) {
        for (LineShape line : lines) {
            line.Draw(g2);
        }
    }

    @Override
    public Rectangle getBounds() {
        Rectangle bounds = null;

        for (LineShape line : lines) {
            if (bounds == null) {
                bounds = line.getBounds();
            } else {
                bounds = bounds.union(line.getBounds());
            }
        }
        return bounds;
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

    @Override
    public void move(int dx, int dy) {
        for (LineShape line : lines) {
            line.move(dx, dy);
        }
    }

    @Override
    public void scaleX(boolean isLeft, int dx) {
        for (LineShape line : lines) {
            line.scaleX(isLeft, dx);
        }
    }

    @Override
    public void scaleY(boolean isTop, int dy) {
        for (LineShape line : lines) {
            line.scaleY(isTop, dy);
        }

    }

    @Override
    public void setStroke(BasicStroke stroke) {
        for (LineShape d : lines) {
            d.setStroke(stroke);
        }
    }

    @Override
    public void setColor(Color color) {
        for (LineShape l : lines) {
            l.setColor(color);
        }
    }

    @Override
    public void scale(int rectangleX, int rectangleY, double scaleX, double scaleY, int ox, int oy, Drawable drawable) {
        Pencil original = (Pencil) drawable;
        for (int i = 0; i < lines.size(); i++) {
            LineShape line = lines.get(i);
            line.scale(rectangleX, rectangleY, scaleX, scaleY, ox, oy, original.lines.get(i));
        }
    }

    @Override
    public Drawable getClone() {
        return new Pencil(this);
    }
}


class Polygon implements Drawable {
    private ArrayList<Integer> pointsX;
    private ArrayList<Integer> pointsY;
    private boolean isFinish = false;
    private Color color;
    private BasicStroke stroke;

    private int previewPointX = 0;
    private int previewPointY = 0;

    public Polygon(Color color, BasicStroke stroke) {
        pointsX = new ArrayList<>();
        pointsY = new ArrayList<>();

        this.color = color;
        this.stroke = stroke;
    }

    public Polygon(Polygon polygon) {
        pointsX = new ArrayList<>();
        pointsY = new ArrayList<>();

        this.color = polygon.color;
        this.stroke = polygon.stroke;

        for (int i = 0; i < polygon.pointsX.size(); i++) {
            pointsX.add(polygon.pointsX.get(i));
            pointsY.add(polygon.pointsY.get(i));
        }
    }

    @Override
    public ETools getShape() {
        return ETools.POLYGON;
    }

    @Override
    public Rectangle getBounds() {
        int minX = Collections.min(pointsX);
        int minY = Collections.min(pointsY);
        int maxX = Collections.max(pointsX);
        int maxY = Collections.max(pointsY);

        return new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }


    @Override
    public void setStroke(BasicStroke stroke) {
        this.stroke = stroke;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void Draw(Graphics2D g2) {
        g2.setColor(color);
        g2.setStroke(stroke);
        if (isFinish) {
            int[] px = pointsX.stream().mapToInt(i -> i).toArray();
            int[] py = pointsY.stream().mapToInt(i -> i).toArray();
            g2.drawPolygon(px, py, pointsX.size());
            return;
        }

        for (int i = 1; i < pointsX.size(); i++) {
            g2.drawLine(pointsX.get(i - 1), pointsY.get(i - 1), pointsX.get(i), pointsY.get(i));
        }
        g2.drawLine(pointsX.get(size() - 1), pointsY.get(size() - 1), previewPointX, previewPointY);
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void addPoint(int x, int y) {
        pointsX.add(x);
        pointsY.add(y);
        if (size() == 1) {
            previewPointX = pointsX.get(pointsX.size() - 1);
            previewPointY = pointsY.get(pointsY.size() - 1);
        }
    }

    public void popPoint() {
        pointsX.remove(pointsX.size() - 1);
        pointsY.remove(pointsY.size() - 1);
    }

    public int size() {
        return pointsX.size();
    }

    public void setPreviewPoint(int x, int y) {
        previewPointX = x;
        previewPointY = y;
    }

    @Override
    public void move(int dx, int dy) {
        pointsX.replaceAll(integer -> integer + dx);

        pointsY.replaceAll(integer -> integer + dy);
    }

    @Override
    public void scaleX(boolean isLeft, int dx) {
        int centerX = 0;

        for (int i = 0; i < pointsX.size(); i++) {
            centerX += pointsX.get(i);
        }

        centerX /= pointsX.size();

        for (int i = 0; i < pointsX.size(); i++) {
            if (isLeft && pointsX.get(i) < centerX || !isLeft && pointsX.get(i) > centerX) {
                pointsX.set(i, centerX + dx);
            }
        }
    }

    @Override
    public void scaleY(boolean isTop, int dy) {
        int centerY = 0;

        for (int i = 0; i < pointsY.size(); i++) {
            centerY += pointsY.get(i);
        }

        centerY /= pointsY.size();

        for (int i = 0; i < pointsY.size(); i++) {
            if (isTop && pointsY.get(i) < centerY || !isTop && pointsY.get(i) > centerY) {
                pointsY.set(i, centerY + dy);
            }
        }
    }

    @Override
    public void scale(int rectangleX, int rectangleY, double scaleX, double scaleY, int ox, int oy, Drawable drawable) {
        Polygon original = (Polygon) drawable;

        for (int i = 0; i < pointsX.size(); i++) {
            pointsX.set(i, (int) ((original.pointsX.get(i) - ox) * scaleX + rectangleX));
        }

        for (int i = 0; i < pointsY.size(); i++) {
            pointsY.set(i, (int) ((original.pointsY.get(i) - oy) * scaleY + rectangleY));
        }
    }


    @Override
    public Drawable getClone() {
        return new Polygon(this);
    }
}

class Selected implements Drawable {
    private ArrayList<Drawable> shapes = new ArrayList<>();
    private RectangleShape bounds = null;
    private boolean isMoving = false;
    private Direction direction = null;
    private Selected copy = null;

    @Override
    public ETools getShape() {
        return ETools.SELECT;
    }

    public Selected(RectangleShape rectangleShape, ArrayList<Drawable> graphics) {
        this.bounds = rectangleShape;
        Rectangle b = rectangleShape.getBounds();
        for (Drawable drawable : graphics) {
            if (b.contains(drawable.getBounds())) {
                shapes.add(drawable);
            }
        }
    }

    public Selected(Selected rhs) {
        shapes = new ArrayList<>();
        for (int i = 0; i < rhs.shapes.size(); i++) {
            shapes.add(rhs.shapes.get(i).getClone());
        }

        bounds = new RectangleShape(rhs.bounds);
        isMoving = rhs.isMoving;
        direction = rhs.direction;

    }

    @Override
    public void Draw(Graphics2D g2) {
        bounds.Draw(g2);
    }

    @Override
    public Rectangle getBounds() {
        return bounds.getBounds();
    }

    @Override
    public void move(int dx, int dy) {
        bounds.move(dx, dy);
        for (Drawable drawable : shapes) {
            drawable.move(dx, dy);
        }
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
        if (this.direction != null) {
            this.copy = new Selected(this);
        } else {
            this.copy = null;
        }
    }

    public Direction getDirection() {
        return direction;
    }

    public int getSelectedSize() {
        return shapes.size();
    }

    @Override
    public void scaleX(boolean isLeft, int dx) {
        for (Drawable d : shapes) {
            d.scaleX(isLeft, dx);
        }
        bounds.scaleX(isLeft, dx);
    }

    @Override
    public void scaleY(boolean isTop, int dy) {
        for (Drawable d : shapes) {
            d.scaleY(isTop, dy);
        }
        bounds.scaleY(isTop, dy);
    }

    @Override
    public void setStroke(BasicStroke stroke) {
        for (Drawable d : shapes) {
            d.setStroke(stroke);
        }
    }

    @Override
    public void setColor(Color color) {
        for (Drawable d : shapes) {
            d.setColor(color);
        }
    }

    public void delete(ArrayList<Drawable> graphics) {
        for (Drawable d : graphics) {
            if (shapes.contains(d)) {
                graphics.remove(d);
            }
        }
    }

    public int getX() {
        return bounds.getX();
    }

    public int getY() {
        return bounds.getY();
    }

    public int getHeight() {
        return bounds.getHeight();
    }

    public int getWidth() {
        return bounds.getWidth();
    }

    @Override
    public void scale(int rectangleX, int rectangleY, double scaleX, double scaleY, int ox, int oy, Drawable drawable) {
        double newWidth = rectangleX - bounds.getX();
        double newHeight = rectangleY - bounds.getY();

        scaleX = newWidth / copy.bounds.getWidth();
        scaleY = newHeight / copy.bounds.getHeight();

//        scaleX = newWidth / bounds.getWidth();
//        scaleY = newHeight / bounds.getHeight();

        bounds.setWidth((int) newWidth);
        bounds.setHeight((int) newHeight);

        for (int i = 0; i < shapes.size(); i++) {
            Drawable d = copy.shapes.get(i);
            shapes.get(i).scale(bounds.getX(), bounds.getY(), scaleX, scaleY, copy.getX(), copy.getY(), d);
        }
    }

    public Selected getCopy() {
        return copy;
    }

    @Override
    public Drawable getClone() {
        try {
            return (Drawable) this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}

enum Direction {
    LEFT,
    RIGHT,
    UP,
    DOWN
}
