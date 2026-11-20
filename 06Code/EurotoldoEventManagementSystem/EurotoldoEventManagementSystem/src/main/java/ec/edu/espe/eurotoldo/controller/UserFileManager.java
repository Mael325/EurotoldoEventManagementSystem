package ec.edu.espe.eurotoldo.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ec.edu.espe.eurotoldo.model.User;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 *
 * @author Juan Alvarez, Anthony Arévalo, Dylan Cumbajin, MosQ, @ESPE
 */
public class UserFileManager {

    private static final String FILE_NAME = "users.json";
    private final Gson gson;

    public UserFileManager() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public boolean saveUsers(ArrayList<User> users) {
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            gson.toJson(users, writer);
            return true;
        } catch (IOException e) {
            System.out.println("Error al guardar usuarios en " + FILE_NAME + ": " + e.getMessage());
            return false;
        }
    }

    public ArrayList<User> loadUsers() {
        File file = new File(FILE_NAME);
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(file)) {
            Type listType = new TypeToken<ArrayList<User>>() {}.getType();
            ArrayList<User> loadedUsers = gson.fromJson(reader, listType);
            return (loadedUsers != null) ? loadedUsers : new ArrayList<>();
        } catch (IOException e) {
            System.out.println("Error al cargar usuarios desde " + FILE_NAME + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
}