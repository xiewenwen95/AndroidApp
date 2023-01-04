package iss.team6.thememorygame;

public class Card {
    public String ImageName;
    public int ImageId;

    public Card() {
    }

    public Card(String imageName, int imageId) {
        ImageName = imageName;
        ImageId = imageId;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }

    public int getImageId() {
        return ImageId;
    }

    public void setImageId(int imageId) {
        ImageId = imageId;
    }
}
