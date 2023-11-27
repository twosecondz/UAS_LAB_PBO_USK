import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Akun
 */
public class Akun {
    private String id;
    private String username;
    private String password;
    private static Map<String, Akun> akunDatabase = new HashMap<>();

    public Akun(String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public static void createAccount(String id, String username, String password) {
        Akun akun = new Akun(id, username, password);
        akunDatabase.put(username, akun);
        System.out.println("Akun berhasil dibuat.");

        // Menulis ulang database ke file setiap kali ada perubahan
        writeDatabaseToFile();
    }

    private static void writeDatabaseToFile() {
        try (FileWriter writer = new FileWriter("akun_database.txt")) {
            for (Akun akun : akunDatabase.values()) {
                writer.write(akun.getId() + "," + akun.getUsername() + "," + akun.getPassword() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Akun> getAkunDatabase() {
        return akunDatabase;
    }
}
