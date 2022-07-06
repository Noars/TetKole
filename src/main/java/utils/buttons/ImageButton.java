package utils.buttons;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageButton {
    /**
     * Class for create a specific image that will fit with the button.
     *
     * @param url -> the path of the image we want to use
     * @return an image with a specific ratio
     */
    public static ImageView createButtonImageView(String url) {
        ImageView image = new ImageView(new Image(url));
        image.setPreserveRatio(true);
        image.setFitWidth(495. / 6);
        return image;
    }

}
