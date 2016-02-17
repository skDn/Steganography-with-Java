package src;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

public class Test {

    protected static String fileExtension = ".bmp";

//    public static void main(String[] args) {
//
//        Steg st = new Steg();
//        String testImage = "image.bmp";
////        st.hideString("", testImage);
////        testImage = testImage.replaceFirst("[.][^.]+$", "");
////        System.out.println(st.extractString(testImage + "-modified" + fileExtension));
//
//        System.out.println(st.hideFile("Test.txt", "marbles.bmp"));
//        st.extractFile("marbles-modified.bmp");
////        st.hideFile("outFile.abcdefg", "image.bmp");
//
//
//    }
//

    
    public static void main(String args[]) throws IOException {

        int userInput = -1;
        String fileToUse;
        String fileOrStringToEncode;
        Scanner inputReader = new Scanner(System.in);
        Steg st = new Steg();

        do {

        
            //Getting input in int format
           
            System.out.println("Enter the bmp file you want to use: ");
            fileToUse = inputReader.nextLine();
            System.out.println("Enter the file  or string you want to include as payload: ");
            fileOrStringToEncode= inputReader.nextLine();
            System.out.println("Enter type of operation (1-hide String; 2-hide file) ");
            userInput = inputReader.nextInt();
            switch (userInput) {
         
                case 1:
                	st.hideString(fileOrStringToEncode, fileToUse);
                	fileToUse = fileToUse.replaceFirst("[.][^.]+$", "");
                   System.out.println("ARe String Equal ???   "+st.extractString(fileToUse + "-modified" + fileExtension).equals(fileOrStringToEncode));
                    break;
                case 2:
              
                    System.out.println(fileOrStringToEncode);
                    System.out.println(fileToUse);
                    String stegoFile = st.hideFile(fileOrStringToEncode , fileToUse);
                    String outputFile =	st.extractFile(stegoFile);
                    
                    File file1 = new File(fileToUse);
                    File file2 = new File(outputFile);
                    
                    System.out.println("Encoding Result or file Name:  "+ stegoFile);
                    System.out.println("Extraction Result or file Name:   "+outputFile);
                    System.out.println("Are Files Equal: "+sameContent(file1,file2));
                    System.out.println("=========================================");
                    break;
              

            }

        }

        while (userInput!=0);



    }
    
    private static boolean sameContent(File file1, File file2) throws IOException {
       return FileUtils.contentEquals(file1, file2);
    }



}