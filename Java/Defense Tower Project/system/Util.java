package finalproject.system;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.TextArea;

public class Util {
    final static int BUTTON_PRE_WIDTH = 200;
    final static int BUTTON_PRE_HEIGHT = 200;

    public static Button createButton(String name, int width, int height, int x, int y) {
        Button b = new Button(name);
        b.setTranslateX(x);
        b.setTranslateY(y);
        b.setPrefSize(width, height);
        b.setMaxSize(1000,500);
        return b;
    }

    public static Button createButton(String name, int x, int y) {
        Button b = new Button(name);
        b.setTranslateX(x);
        b.setTranslateY(y);
        b.setPrefSize(BUTTON_PRE_WIDTH, BUTTON_PRE_HEIGHT);
        b.setMaxSize(1000,500);
        return b;
    }


}
