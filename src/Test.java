import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class Test {

    protected static String fileExtension = ".bmp";

    public static void main(String[] args) {

        Steg st = new Steg();
        String testImage = "image.bmp";
//        st.hideString("", testImage);
//        testImage = testImage.replaceFirst("[.][^.]+$", "");
//        System.out.println(st.extractString(testImage + "-modified" + fileExtension));

        System.out.println(st.hideFile("Cyber.iml", "marbles.bmp"));
        st.extractFile("marbles-modified.bmp");
//        st.hideFile("outFile.abcdefg", "image.bmp");


    }


    /*
    public static void main(String args[]) throws IOException {

        int userInput = -1;
        String fileToUse;
        String fileToEncode;
        Scanner inputReader = new Scanner(System.in);

        do {

            System.out.println("Enter your choice of command: ");
            //Getting input in int format
            userInput = inputReader.nextInt();

            switch (userInput) {
                case 1:
                    System.out.println("hello");
                    break;
                case 2:
                    System.out.println("Enter the bmp file you want to use: ");
                    fileToUse = inputReader.nextLine();
                    System.out.println("Enter the String you want to encode: ");
                    fileToUse = inputReader.nextLine();

            }

        }

        while (userInput!=0);



    }
*/


}