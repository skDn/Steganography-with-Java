public class Test {

    protected static String fileExtension = ".bmp";

    public static void main(String[] args) {

        Steg st = new Steg();
//        String testImage = "image.bmp";
//        st.hideString("", testImage);
//        testImage = testImage.replaceFirst("[.][^.]+$", "");
//        System.out.println(st.extractString(testImage + "-modified" + fileExtension));


        st.hideFile("Test.txt", "image.bmp");
        st.extractFile("image-modified.bmp");
        st.hideFile("outFile.txt", "image.bmp");


    }

    public static int byteToInt(byte[] bytes, int length) {
        int val = 0;
        if (length > 4) throw new RuntimeException("Too big to fit in int");
        for (int i = 0; i < length; i++) {
            val = val << 8;
            val = val | (bytes[i] & 0xFF);
        }
        return val;
    }

    private static int[] getStringSizeToIntArray(String message) {

        int[] size = new int[32];
        int stringLenght = message.length();
        for (int i = 32 - 1, j = 0; i >= 0; i--, j++) {
            size[i] = (stringLenght >> j) & 1;
//            System.out.println(size[i]);
        }

        return size;
    }

}