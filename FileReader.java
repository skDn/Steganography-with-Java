//package src;
import java.io.*;
import java.util.*;

/**
 * This class reads a payload file
 *
 * @author Rose
 */
public class FileReader {

    private int count = 0;

    /**
     * A fileinputstream object to read in the bytes of the file
     */
    private FileInputStream fileInStream;

    /**
     * A string to hold the file name
     */
    private String fileName;
    /**
     * An int to hold the current byte
     */
    private int currentByte;

    /**
     * the current position within the current byte e.g. position 0 is the least significant bit
     */
    private int currentPos = 0;

    /**
     * a constant to hold the length of a byte
     */
    private final int byteLength = 8;

    /**
     * A file object to represent the file to be read
     */
    private File file;

    /**
     * List to hold the bits which represent the size of the file
     */
    private List<Integer> sbits;
    /**
     * List to hold the bits representing the extension
     */
    private List<Integer> extBits;

    /**
     * iterators for the lists which hold the bits relating to the size and extension
     */
    private Iterator<Integer> sBitsIt;
    private Iterator<Integer> extBitsIt;
    /**
     * A boolean indicating successful execution of all processes
     */
    private boolean success = true;

    /**
     * @param f, the file which has to be read in
     *           This constructor creates a file input stream of the file passed in
     *           and then calls the method to read each byte and split it into bits
     */
    public FileReader(String f) {

        //a shallow copy of the file passed in
        file = new File(f);
        //store the name of the file in a string
        fileName = f;
        //set up an input stream for the file
        setUpStream();

        //set up the extension and file size bits which will be accessed through the
        //"getNextBit" method to create a new payload which consists of the file
        //size, extension and the bits of the file
        populateSizeBits();
        populateExtensionBits();

        //set up the first byte
        try {
            getNextByte();
        } catch (IOException e) {
            System.err.println("The file " + fileName + " has no bytes," +
                    " please try again");

        }
    }

    /**
     * setUpStream sets up a file input stream to read in the file passed in
     * to the constructor
     */
    private void setUpStream() {
        //set up a file input stream from the file passed in
        try {
            fileInStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {

            //error message as the user has selected a file which cannot be located
            System.err.println("The file " + fileName + " cannot be found, please" +
                    "try again");
            success = false;//fail
        }
    }


    /**
     * This method returns a string populated with the extension of the file
     */
    private String getExtension() {
        //set up the string
        String ext = "";
        //find the position of the .
        // System.out.println(fileName);
        int pos = fileName.lastIndexOf(".");

        //now create a substring from the . to the end
        ext = fileName.substring(pos);
        //return this string
        return ext;
    }

    /**
     * This method performs a check to see if the file has another byte,
     * it sets the current byte to that which is read in.
     *
     * @return a boolean indicating if there is another byte or not
     * @throws IOException
     */
    private boolean getNextByte() throws IOException {

        //read in the next byte and store it in the currentByte variable
        currentByte = fileInStream.read();

        //if the current byte is not -1, then it is not the end of the file,
        //hence return true
        if (currentByte != -1) {
            return true;
        }
        //if the current byte is -1, then it is the end of the file, return false
        else {
            //System.out.println("here");
            return false;
        }
    }

    /**
     * returns the value of the current byte
     */
    private int getCurrentByte() {
        return currentByte;
    }

    /**
     * A method which checks to see if there is a next bit to be returned
     *
     * @return a boolean indicating if there is a next bit to be read in from the new header information
     * (the size bits and extension bits) or from the file bits
     */
    public boolean hasNextBit() {
        boolean hasNext = false;
        //if there is another bit relating to the size to be hidden, return true
        if (sBitsIt.hasNext()) {
            hasNext = true;
        }
        //else, you've used all the bits relating to size, so check to see if there
        //are bits relating to the extension to be hidden
        else if (extBitsIt.hasNext()) {
            hasNext = true;
        }
        //otherwise, we're on to checking the bytes of the file, if the current position
        //indicates it's at the last bit of the current byte (position 7 since
        // you count from 0), then try to get the next byte
        //from the payload
        else if (currentPos > (byteLength - 1)) {
            //try to get the next byte
            try {
                //get the next byte
                if (getNextByte()) {
                    //reset currentPos
                    currentPos = 0;
                    //set the boolean hasNext to be true since the next byte has 8 bits
                    hasNext = true;
                }
                //if there is no next byte, return false
                else {
                    //System.out.println("here");
                    hasNext = false;
                }
            }
            //catch the IO exception
            catch (IOException e) {
                System.err.println("The file " + fileName + " has no more bits," +
                        " please try again");

                success = false;//fail
            }
        }
        /*
         * otherwise, set the boolean to true, can get the next bit since we're in a byte which has more bits
		 * to be read
		 */
        else {
            hasNext = true;
        }

        //return the boolean
        return hasNext;
    }

    /**
     * A method to get the next bit from the payload, should always be preceded by
     * 'hasNextBit'
     *
     * @return the next bit from the payload
     */
    public int getNextBit() {
        int bit = 0;
        //if there are more size bits to hide, then get the next size bit
        //---------
        if (sBitsIt.hasNext()) {
            bit = sBitsIt.next();
        }
        //otherwise, if there are more extension bits to hide, get the extension bit
        else if (extBitsIt.hasNext()) {
            bit = extBitsIt.next();
        }
        //otherwise, get the next bit from the current byte of the payload file.
        else {
            bit = getCurrentByte() >> currentPos;
            bit &= 0x1;
            currentPos++;
        }
        count++;
        return bit;
    }

    /**
     * a method to return the size of the file in terms of how many bits are in the file
     *
     * @return the size of the file in bits
     */
    public int getFileSize() {
        return (int) file.length() * byteLength;
    }

    //TODO YOU MUST FILL IN THIS METHOD

    /**
     * method to populate the list of bits relating to the size of the payload
     * 32 bits used to represent the size
     */
    private void populateSizeBits() {
    	//convert the size value into 32 bit long bit array
        int size = getFileSize();
        sbits = new ArrayList<Integer>(32);
        boolean[] bits = toBinary(size, 32);

        //fill the bits in the sizeBits Array
        for (int bit = 0; bit < 32; bit++) {
            if (bits[bit])
                sbits.add(1);
            else {
                sbits.add(0);
            }
        }
        sBitsIt = sbits.iterator();

    }

    /**
     * Convert the number to binary representation with base 32
     * USAGE: when populating the SizeBits array
     * @param int number[], int base
     */
    private static boolean[] toBinary(int number, int base) {
        final boolean[] ret = new boolean[base];
        for (int i = 0; i < base; i++) {
            ret[base - 1 - i] = (1 << i & number) != 0;
        }
        return ret;
    }

    //TODO YOU MUST FILL IN THIS METHOD

    /**
     * method to populate the list of bits relating to the extension of the payload
     * 64 bits used to represent the extension
     */
    private void populateExtensionBits() {
    	//get the extension string
        String extension = getExtension();

        try {
        	//convert to byte array with "UTF-8" encoding
            byte[] bytes = extension.getBytes("UTF-8");
            // convert to bits  lengts: bytes.lenght * 8
            boolean[] bits = byteArray2BitArray(bytes);
            //
            if (bits.length > 64) {
                throw new IllegalArgumentException("the extension is too big to fit in a 64 bit array");
            }
            //populate initially  the array with zeroes 
            extBits = new ArrayList<Integer>(Collections.nCopies(64, 0));
            //loop on the bits array
            for (int bit = 0; bit < bits.length; bit++) {
            	//if '1' change the bit from 0 to 1
            	//modify the extensionBits array in reversed order to cope wiht lengts,
            	//less than 64
                if (bits[bit])
                    extBits.set(64 - bits.length + bit, 1);
            }
            extBitsIt = extBits.iterator();
        }
        catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        }
    }

    /**
     * Convert a byte array to a boolean array. Bit 0 is represented with false,
     * Bit 1 is represented with 1
     * 
     * @param bytes byte[]
     * @return boolean[]
     */
    public static boolean[] byteArray2BitArray(byte[] bytes) {
    	//init bits array with zeroes
        boolean[] bits = new boolean[bytes.length * 8];
        //for each bit in the byte array
        for (int i = 0; i < bytes.length * 8; i++) {
        	//go through each bit set remember the curent bit and modify if 1
            if ((bytes[i / 8] & (1 << (7 - (i % 8)))) > 0)
                bits[i] = true;
        }
        return bits;
    }


    /**
     * An accessor method to get the boolean indicating if any errors were thrown
     *
     * @return success boolean
     */
    public boolean getSuccessBool() {
        return success;
    }

    /**
     * Accessor methods
     */
    public List<Integer> getSizeBits() {
        return sbits;

    }

    public List<Integer> getExtBits() {
        return extBits;
    }

}
