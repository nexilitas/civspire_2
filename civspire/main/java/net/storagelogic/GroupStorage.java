package net.storagelogic;

import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.invoke.Group;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class GroupStorage {
    private static final File file = new File("plugins/Civspire/groups.json");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Type GROUP_LIST_TYPE = new TypeToken<List<Group>>() {}.getType();

    public static void saveGroups(Collection<Group> groups) {
        try (Writer writer = new FileWriter(file)) {
            gson.toJson(groups, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Group> loadGroups() {
        if (!file.exists()) return new ArrayList<>();
        try (Reader reader = new FileReader(file)) {
            return gson.fromJson(reader, GROUP_LIST_TYPE);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}