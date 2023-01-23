package alex.common;

import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class AlexFile {
    public static ArrayList<String> readAsList(Resources resources, int rawFile) {
        ArrayList<String> list = new ArrayList<>();
        InputStream inputStream = resources.openRawResource(rawFile);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            while (reader.ready()) {
                list.add(reader.readLine());
            }
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
