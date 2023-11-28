import java.util.HashMap;
import java.util.Map;

/**
 * Admin
 */
class Admin extends Akun {
    private String username;
    private String password;
    private static Map<String, Akun> akunDatabase = new HashMap<>();


    public Admin(String username, String password) {
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

    public void addProduk() {
        System.out.println("Berhasil menambahkan produk");
    }

    public void deleteProduk() {
        System.out.println("Berhasil menghapus produk");
    }

    public void editProduk() {
        System.out.println("Berhasil mengedit produk");
    }

    public void seeTransaction() {
        System.out.println("Berhasil melihat transaksi");
    }

    public void acceptTransaction() {
        System.out.println("Berhasil menerima transaksi");
    }
}