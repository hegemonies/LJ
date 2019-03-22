package LJ;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Helper {
    public static String getDataFromFIle(String fileName) {
        File file = new File(fileName);

        if (!file.exists()) {
            System.out.println("Error: Input file not exist");
            return null;
        }

        String result = null;

        try (FileReader fileReader = new FileReader(fileName)) {
            char[] buffer = new char[(int)file.length()];
            fileReader.read(buffer);
            result = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
