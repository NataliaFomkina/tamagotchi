package org.example.ui;
import javax.swing.*;
//класс для удобной работы с блочным расположением
public class BoxLayoutUtils {
    //возвращает панель с установленым вертикальым блочным расположением
    public static JPanel createVerticalPanel(){
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        return p;
    }
    //возвращает панель с установленным горизонтальным блочным расположением
    public static JPanel createHorizontalPanel(){
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        return p;
    }
}
