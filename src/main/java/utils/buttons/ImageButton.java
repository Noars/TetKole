package utils.buttons;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageButton {

    public static ImageView createButtonImageView(String url) {
        ImageView image = new ImageView(new Image(url));
        image.setPreserveRatio(true);
        image.setFitWidth(495. / 6);
        return image;
    }

}
