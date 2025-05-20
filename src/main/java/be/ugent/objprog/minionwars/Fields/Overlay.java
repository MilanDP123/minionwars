package be.ugent.objprog.minionwars.Fields;

import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;

public class Overlay extends Hexagon {
    private Field field;

    public Overlay(int x, int y) {
        super(x, y);
        getStyleClass().add("overlay");
        setStrokeType(StrokeType.INSIDE);
        setStroke(Color.BLACK);
        setStrokeWidth(4.0);
    }

    public void setField(Field field) {
        this.field = field;
        setOnMouseClicked(field::mouseClicked);
        setOnMouseEntered(field::mouseEntered);
        if (field.getHomeBase() == 1) {
            color("yellow");
        } else {
            color("red");
        }
    }

    public void clearColors() {
        getStyleClass().clear();
        getStyleClass().add("overlay");
        if (field.hasMinion()) {
            field.colorBorder();
        }
    }

    public void color(String color) {
        clearColors();
        getStyleClass().add("overlay-" + color);
    }

    public void clearBorderColor() {
        getStyleClass().removeIf(string -> string.startsWith("overlay-border-"));
    }

    public void setBorderColor(String color) {
        clearBorderColor();
        getStyleClass().add("overlay-border-" + color);
    }
}