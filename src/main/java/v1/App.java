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
    
    // Url para imagem do emoji que vai ser colocado na frente da face detectada
    private static final String URL = "https://cdn.shopify.com/s/files/1/1061/1924/products/Thinking_Face_Emoji_large.png?v=1571606036";

    public static void main(String[] args) throws VideoCaptureException {
        
        // Instancia uma tela de vídeo 
        Video<MBFImage> video = new VideoCapture(860, 640);
        
        // Cria tela de vídeo a partir da Web Cam do Usuário
        VideoDisplay<MBFImage> display = VideoDisplay.createVideoDisplay(video);

        // Cria um listener que identifica frames nos vídeos e ativa processamento no frame
        display.addVideoListener(
                new VideoDisplayListener<MBFImage>() {
                    
                    // Método para mudança e processamento de frame antes da atualização
                    public void beforeUpdate(MBFImage frame) {
                        
                        // Instancia o algorítmo HaarCascade para identificação de Face
                        FaceDetector<DetectedFace, FImage> fd = new HaarCascadeDetector(40);
                       
                        // Instancia lista para cada face detectada pela webcam
                        List<DetectedFace> faces = fd.detectFaces(Transforms.calculateIntensity(frame));

                        // Para cada face detectada
                        for (DetectedFace face : faces) {
                            try {
                                // Carrega imagem do Emoji
                                MBFImage image = ImageUtilities.readMBF(new File("emoji.jpg"));
                                
                                // captura coordenada X do centro da face
                                int x = Math.round(face.getBounds().x);
                                
                                // Captura coordenada y do ponto mais alto do centro da face (altura)
                                int y = Math.round(face.getBounds().y);
                                
                                // Coloca imagem do emoji nas coordenadas caputuradas.
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
