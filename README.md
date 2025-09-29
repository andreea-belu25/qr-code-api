Tools:
--
Java, SpringBoot

Utility: 
--
This Java code defines a REST API using Spring Boot to generate and return a simple white square image (which could be a placeholder for a QR code)
         of a specified size and format (PNG, JPEG, or GIF)

Development:
--

- Application.java 
	- The Main class of the application.

- QRCodeController.java
	- Spring imports are used for handling HTTP requests and responses.
    - @RequestMapping("/api"): Maps all URLs starting with /api to methods in this controller.

    - QR Code Image Endpoint
      - @GetMapping("/qrcode"): Maps the /api/qrcode endpoint to this method. It responds to HTTP GET requests.
      - @RequestParam(required = false): Marks size and type as optional query parameters that the client can pass (e.g., /api/qrcode?size=200&type=png).
      - The method returns a ResponseEntity<byte[]>, which contains the image as a byte array, along with HTTP status and headers.

	- Parameter Validation
      - If the size or the type is not provided
      - If the size is not between the given dimensions
      - If the type of the image is not png, jpg/jpeg or gif
      => a 400 Bad Request is sent

    - Image Creation
		- A BufferedImage of the specified size is created with a white background.
		- Graphics2D is used to "draw" onto the image. In this case, the entire image is filled with white.

    - Image Serialization
	    - The image is written to a ByteArrayOutputStream and the resulting byte array is used as the response body.

    - Media Type Mapping
	    - The content type of the response is set based on the image format.

    - Returning the response

  	- Error Handling
	    - If an error occurs during image creation or serialization, the method returns a 500 Internal Server Error response with an error message.
