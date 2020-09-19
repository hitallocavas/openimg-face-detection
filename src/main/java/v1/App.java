package v1;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.ColourSpace;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.colour.Transforms;
import org.openimaj.image.processing.convolution.FGaussianConvolve;
import org.openimaj.image.processing.edges.CannyEdgeDetector;
import org.openimaj.image.processing.edges.SUSANEdgeDetector;
import org.openimaj.image.processing.face.detection.DetectedFace;
import org.openimaj.image.processing.face.detection.FaceDetector;
import org.openimaj.image.processing.face.detection.HaarCascadeDetector;
import org.openimaj.image.typography.hershey.HersheyFont;
import org.openimaj.video.Video;
import org.openimaj.video.VideoDisplay;
import org.openimaj.video.VideoDisplayListener;
import org.openimaj.video.capture.VideoCapture;
import org.openimaj.video.capture.VideoCaptureException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * OpenIMAJ Hello world!
 */
public class App {
    private static final String URL = "https://cdn.shopify.com/s/files/1/1061/1924/products/Thinking_Face_Emoji_large.png?v=1571606036";

    public static void main(String[] args) throws VideoCaptureException {
        Video<MBFImage> video = new VideoCapture(860, 640);
        VideoDisplay<MBFImage> display = VideoDisplay.createVideoDisplay(video);

        display.addVideoListener(
                new VideoDisplayListener<MBFImage>() {
                    public void beforeUpdate(MBFImage frame) {
                        FaceDetector<DetectedFace, FImage> fd = new HaarCascadeDetector(40);
                        List<DetectedFace> faces = fd.detectFaces(Transforms.calculateIntensity(frame));

                        for (DetectedFace face : faces) {
                            try {
                                MBFImage image = ImageUtilities.readMBF(new File("emoji.jpg"));
                                int x = Math.round(face.getBounds().x);
                                int y = Math.round(face.getBounds().y);
                                frame.drawImage(image, x, y);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            ;
                        }
                    }

                    public void afterUpdate(VideoDisplay<MBFImage> display) {
                    }
                });

    }
}
