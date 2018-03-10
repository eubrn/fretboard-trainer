package de.eugenbernwald.fretboardtrainer.ui;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Colors {

    private static Random rnd = new Random();
    private static final List<Color> BG_COLORS =
            Arrays.asList(
                    Color.rgb(0x91,0,0),
                    Color.rgb(0xff,0xba,0x08),
                    Color.rgb(0,0x92,0x46),
                    Color.rgb(0x3f,0x88,0xc5),
                    Color.rgb(0x03,0x2b,0x43));


    public static Color getNextColor(Color current){
        List<Color> colors = BG_COLORS
                .stream()
                .filter(bg -> current == null || !current.equals(bg))
                .collect(Collectors.toList());

        Collections.shuffle(colors);

        return colors.get(rnd.nextInt(colors.size()));
    }

    public static Background toBackground(Color color){
        return new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY));
    }
}
