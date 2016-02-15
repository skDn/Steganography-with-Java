import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

class Steg {


	/**
	 * A constant to hold the number of bits per byte
	 */
	private final int byteLength = 8;

	/**
	 * A constant to hold the number of bits used to store the size of the file extracted
	 */
	protected final int sizeBitsLength = 32;
	/**
	 * A constant to hold the number of bits used to store the extension of the file extracted
	 */
	protected final int extBitsLength = 64;

	protected final int imageDataBitsLenght = 54;

	protected final String outputFileExtension = "bmp";


	/**
	 * Default constructor to create a steg object, doesn't do anything - so we actually don't need to declare it explicitly. Oh well.
	 */

	public Steg() {

	}

	/**
	 * A method for hiding a string in an uncompressed image file such as a .bmp or .png
	 * You can assume a .bmp will be used
	 *
	 * @param cover_filename - the filename of the cover image as a string
	 * @param payload        - the string which should be hidden in the cover image.
	 * @return a string which either contains 'Fail' or the name of the stego image which has been
	 * written out as a result of the successful hiding operation.
	 * You can assume that the images are all in the same directory as the java files
	 */
	//TODO you must write this method
	public String hideString(String payload, String cover_filename) {

		byte[] imageBytes;
		byte[] payloadBytes = payload.getBytes();
		//byte[] stringSize = new byte[]{(byte) (payloadBytes.length)};
		int[] stringSize = getStringByteArrayLengthToIntArray(payloadBytes.length);

		try {
			BufferedImage img = getBufferedImage(cover_filename);
			imageBytes = getByteArrayFromBufferedImage(img);

			if ((payloadBytes.length + sizeBitsLength + imageDataBitsLenght) > imageBytes.length) {
				throw new IllegalArgumentException("File not long enough!");
			}

			// swap 32 bits after the first 54 with the size of the string
			int i = imageDataBitsLenght;
			for (int j = 0; i < imageDataBitsLenght + sizeBitsLength; i++, j++) {
				imageBytes[i] = (byte) swapLsb(stringSize[j], imageBytes[i]);
			}
			for (int j = 0; j < payloadBytes.length; j++) {
				int biteToAdd = payloadBytes[j];

				for (int bit = byteLength - 1; bit >= 0; bit--, i++) {
					int b = getBitWithIndex(biteToAdd, bit);
					imageBytes[i] = (byte) swapLsb(b, imageBytes[i]);
				}
			}

			String filename = generateFileName(cover_filename);
			createImageFileFromBufferedImage(img, filename);
			return filename;


		} catch (IOException e) {
			return "Fail";
		}
	}

	private void createImageFileFromBufferedImage(BufferedImage img, String filename) throws IOException {
		File outputfile = new File(filename);
		ImageIO.write(img, outputFileExtension, outputfile); // Write the Buffered Image into an output file
	}

	private String generateFileName(String originalName) {
		String fileName = originalName.replaceFirst("[.][^.]+$", "");
		return fileName + "-modified" + "." + outputFileExtension;
	}

	private int getBitWithIndex(int biteToAdd, int bit) {
		return (biteToAdd >>> bit) & 1;
	}
	//TODO you must write this method

	/**
	 * The extractString method should extract a string which has been hidden in the stegoimage
	 *
	 * @param stego_image name of the stego image
	 * @return a string which contains either the message which has been extracted or 'Fail' which indicates the extraction
	 * was unsuccessful
	 */
	public String extractString(String stego_image) {
		byte[] decode;
		byte[] imageBytes;

		try {
			BufferedImage img = getBufferedImage(stego_image);
			imageBytes = getByteArrayFromBufferedImage(img);
			decode = decodeHiddenString(imageBytes);
			return (new String(decode));
		} catch (Exception e) {
			return "";
		}

	}

	private BufferedImage getBufferedImage(String stego_image) throws IOException {
		File file = new File(stego_image);
		return ImageIO.read(file);
	}

	//TODO you must write this method

	/**
	 * The hideFile method hides any file (so long as there's enough capacity in the image file) in a cover image
	 *
	 * @param file_payload - the name of the file to be hidden, you can assume it is in the same directory as the program
	 * @param cover_image  - the name of the cover image file, you can assume it is in the same directory as the program
	 * @return String - either 'Fail' to indicate an error in the hiding process, or the name of the stego image written out as a
	 * result of the successful hiding process
	 */
	public String hideFile(String file_payload, String cover_image) {

		try {
			//cover_image
			BufferedImage img = getBufferedImage(cover_image);
			byte[] imageBytes = getByteArrayFromBufferedImage(img);

			FileReader reader = new FileReader(file_payload);
			int nextBit;
			int i = imageDataBitsLenght;

			while(reader.hasNextBit()){
				nextBit = reader.getNextBit();
				imageBytes[i] = (byte) swapLsb(nextBit, imageBytes[i]);
				i++;
			}
			String filename = generateFileName(cover_image);
			createImageFileFromBufferedImage(img, filename);	
			
			return filename;
		}
		catch(Exception e){
			e.printStackTrace();
			return "Fail";
		}
		
	}

		//TODO you must write this method

		/**
		 * The extractFile method hides any file (so long as there's enough capacity in the image file) in a cover image
		 *
		 * @param stego_image - the name of the file to be hidden, you can assume it is in the same directory as the program
		 * @return String - either 'Fail' to indicate an error in the extraction process, or the name of the file written out as a
		 * result of the successful extraction process
		 */
		public String extractFile(String stego_image) {
			byte[] decode;
			byte[] imageBytes;

			try {
				BufferedImage img = getBufferedImage(stego_image);
				imageBytes = getByteArrayFromBufferedImage(img);
				decode = decodeHiddenFile(imageBytes);
				return (new String(decode));
			} catch (Exception e) {
				return "";
			}

		}

		//TODO you must write this method

		/**
		 * This method swaps the least significant bit with a bit from the filereader
		 *
		 * @param bitToHide - the bit which is to replace the lsb of the byte of the image
		 * @param byt       - the current byte
		 * @return the altered byte
		 */
		public int swapLsb(int bitToHide, int byt) {
			return ((byt >> 1) << 1) | bitToHide;
		}

		private byte[] getByteArrayFromBufferedImage(BufferedImage image) {
			WritableRaster raster = image.getRaster();
			DataBufferByte buffer = (DataBufferByte) raster.getDataBuffer();
			return buffer.getData();

		}

		private int[] getStringByteArrayLengthToIntArray(int stringLenght) {
			int[] size = new int[sizeBitsLength];
			for (int i = sizeBitsLength - 1, j = 0; i >= 0; i--, j++)
				size[i] = (stringLenght >> j) & 1;
			return size;
		}

		private byte[] decodeHiddenString(byte[] image) {
			int length = 0;
			//loop through 54 to 86 bytes of data to determine text length
			int i = imageDataBitsLenght;
			for (; i < imageDataBitsLenght + sizeBitsLength; i++) {
				length = retrieveBitFromImage(image, length, i);
			}
	
			byte[] result = new byte[length];

			//loop through each byte of text
			for (int j = 0; j < result.length; j++) {
				//loop through each bit within a byte of text
				for (int bit = 0; bit < byteLength; bit++, i++) {
					//assign bit: [(new byte value) << 1] OR [(text byte) AND 1]
					result[j] = (byte) retrieveBitFromImage(image, result[j], i);
				}
			}
			return result;
		}
		
		private byte[] decodeHiddenFile(byte[] image) {
			int length = 0;
			//loop through 54 to 86 bytes of data to determine text length
			int i = imageDataBitsLenght;
			for (; i < imageDataBitsLenght + sizeBitsLength; i++) {
				length = retrieveBitFromImage(image, length, i);
			    System.out.println(length);	
			}
		


			return new byte[10];
		}

		private int retrieveBitFromImage(byte[] image, int result, int i) {
			result = (result << 1) | (image[i] & 1);
			return result;
		}
		
	
		


	}