import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 * Customer
 */
class Customer extends Akun {
    public Keranjang keranjang;
    public ArrayList<Invoice>invoiceSelesai;
    private static Map<String, Akun> akunDatabase = new HashMap<>();

    private String username;
    private String password;

    public Customer(String id, String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean login(String inputUsername, String inputPassword) {
        return username.equals(inputUsername) && password.equals(inputPassword);
    }
}
