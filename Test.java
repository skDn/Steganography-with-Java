import java.io.*;

public class Test {

    public static void main(String args[]) throws IOException {

        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        String userInput = "";
        String fileToUse;
        String fileOrStringToEncode;
        Steg st = new Steg();

        while (!userInput.equals("0")) {
            System.out.println("Enter 1 to hide string: ");
            System.out.println("Enter 2 to extract string: ");
            System.out.println("Enter 3 to hide file: ");
            System.out.println("Enter 4 to extract file: ");
            System.out.println("Enter 0 to exit: ");
            userInput = stdin.readLine();

            if (userInput.equals("1")) {
                System.out.println("Enter the bmp file you want to use: ");
                fileToUse = stdin.readLine();

                System.out.println("Enter the string you want to encode: ");
                fileOrStringToEncode = stdin.readLine();

                String output = st.hideString(fileOrStringToEncode, fileToUse);
                System.out.println(output);
            } else if (userInput.equals("2")) {
                System.out.println("Enter the bmp file you want to use for extracting string: ");
                fileToUse = stdin.readLine();
                System.out.println(st.extractString(fileToUse));
            } else if (userInput.equals("3")) {
                System.out.println("Enter the bmp file you want to use: ");
                fileToUse = stdin.readLine();

                System.out.println("Enter the file you want to encode: ");
                fileOrStringToEncode = stdin.readLine();
                System.out.println(st.hideFile(fileOrStringToEncode, fileToUse));
            } else if (userInput.equals("4")) {
                System.out.println("Enter the bmp file you want to use for extracting file: ");
                fileToUse = stdin.readLine();
                System.out.println(st.extractFile(fileToUse));

            }
            System.out.println();
        }
    }


}