package AppService;

import AppModel.TableActive;
import AppModel.TableBooking;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

public class SaverAndLoader {

    public static String getFile(String fileName) {
        return Paths.get(System.getProperty("user.dir"), "save", fileName).toString();
    }

    public static void settingGetFrom(String file) {
        Gson gson = new Gson();
        FileReader reader;
        try {
            if (file.contentEquals("settings.json")) {
                reader = new FileReader(getFile(file));
                SettingManager.setInstance(gson.fromJson(reader, SettingManager.class));
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    static TableActive[] tableGetFromAct() {
        Gson gson = new Gson();
        FileReader reader;
        try {
            reader = new FileReader(getFile("tableActives.json"));
            return gson.fromJson(reader, TableActive[].class);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    static TableBooking[] tableGetFromBook() {
        Gson gson = new Gson();
        FileReader reader;
        try {
            reader = new FileReader(getFile("tableBookings.json"));
            return gson.fromJson(reader, TableBooking[].class);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveTo(Object object, String file) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        FileWriter writer;
        try {
            writer = new FileWriter(getFile(file));
            gson.toJson(object, writer);
            writer.flush();
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
