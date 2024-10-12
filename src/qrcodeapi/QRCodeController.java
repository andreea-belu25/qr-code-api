package qrcodeapi;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class QRCodeController {

    // Health Check Endpoint
    @GetMapping("/qrcode")
    public ResponseEntity<byte[]> getQRCodeImage(@RequestParam(required = false) Integer size,
                                                 @RequestParam(required = false) String type) throws IOException {
        // Validate presence of parameters
        if (size == null || type == null) {
            return ResponseEntity
                    .badRequest()
                    .body("{\"error\": \"Image size and type must be provided\"}".getBytes());
        }

        // Validate the size parameter
        if (size < 150 || size > 350) {
            return ResponseEntity
                    .badRequest()
                    .body("{\"error\": \"Image size must be between 150 and 350 pixels\"}".getBytes());
        }

        // Validate the image type parameter
        String imageType;
        switch (type.toLowerCase()) {
            case "png":
                imageType = "png";
                break;
            case "jpeg":
            case "jpg":
                imageType = "jpeg";
                break;
            case "gif":
                imageType = "gif";
                break;
            default:
                return ResponseEntity
                        .badRequest()
                        .body("{\"error\": \"Only png, jpeg and gif image types are supported\"}".getBytes());
        }

        // Create a SIZExSIZE pixel image filled with white color
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, size, size);

        // Serialize the BufferedImage and include it in the response body
        try (var baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, imageType, baos);
            byte[] bytes = baos.toByteArray();

            // Set the correct Content-Type based on the requested image type
            MediaType mediaType = switch (imageType) {
                case "png" -> MediaType.IMAGE_PNG;
                case "jpeg" -> MediaType.IMAGE_JPEG;
                case "gif" -> MediaType.IMAGE_GIF;
                default -> MediaType.APPLICATION_OCTET_STREAM; // Fallback, though it shouldn't reach here
            };

            return ResponseEntity
                    .ok()
                    .contentType(mediaType)
                    .body(bytes);
        } catch (IOException e) {
            // Return a 500 error if serialization fails
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Failed to generate image\"}".getBytes());
        }
    }
}
