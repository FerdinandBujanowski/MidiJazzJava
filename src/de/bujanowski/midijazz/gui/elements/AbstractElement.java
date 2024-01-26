package de.bujanowski.midijazz.gui.elements;
import javafx.scene.canvas.Canvas;

import java.awt.*;

public abstract class AbstractElement implements IElement {

    private double[] bounds;
    protected int x, y, width, height;

    public AbstractElement(double[] bounds) {
        this.setBounds(bounds);
    }

    public double[] getBounds() {
        return this.bounds;
    }
    public void setBounds(double[] bounds) {
        this.bounds = bounds;
    }

    public void resize(Canvas canvas) {
        this.x = (int)Math.round(canvas.getWidth() * this.bounds[0]);
        this.y = (int)Math.round(canvas.getHeight() * this.bounds[1]);
        this.width = (int)Math.round(canvas.getWidth() * this.bounds[2]);
        this.height = (int)Math.round(canvas.getHeight() * this.bounds[3]);
    }
}
