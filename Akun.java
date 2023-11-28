import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Akun
 */
public class Akun {
    private String id;
    private Admin admin;
    private Map<String, String> akunDatabase = new HashMap<>();

    public Map<String, String> getAkunDatabase() {
        return akunDatabase;
    }

    public void createAccount(String role, String idPrefix, String username, String password) {
        String id = idPrefix + "-" + username;
        akunDatabase.put(id, password);
        System.out.println("\nAkun berhasil dibuat!");
        System.out.println("ID Anda: " + id);
    }

    private void saveToFileAdmin(String id, String password, String username) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("Database/akunAdmin.txt", true))) {
            writer.println(id + "," + password + "," + username);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Gagal menyimpan akun ke dalam file.");
        }
    }

    public boolean login(String username, String password) {
        for (Map.Entry<String, String> entry : akunDatabase.entrySet()) {
            if (entry.getKey().endsWith("-" + username) && entry.getValue().equals(password)) {
                return true;
            }
        }
        return false;
    }
}