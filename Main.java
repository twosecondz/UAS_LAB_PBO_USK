import java.util.Map;
import java.util.Scanner;

public class Main {
    private Scanner input = new Scanner(System.in);

    public Main() {
        startMenu();
    }

    private void startMenu() {
        System.out.println();
        System.out.println("*".repeat(27));
        System.out.println("| WELCOME TO THREE MARKET |");
        System.out.println("*".repeat(27));
        System.out.println("| 1. Login                |");
        System.out.println("| 2. Create Account       |");
        System.out.println("*".repeat(27));
        System.out.print("Pilih opsi (1/2) : ");
        int option = input.nextInt();

        if (option == 1) {
            loginMenu();
        } else if (option == 2) {
            createAccount();
            startMenu();
        } else {
            System.out.println("Opsi tidak valid.");
        }
    }

    private void loginMenu() {
        Map<String, Akun> akunDatabase = Akun.getAkunDatabase();

        System.out.print("\nMasukkan username untuk login : ");
        String username = input.next();

        if (akunDatabase.containsKey(username)) {
            System.out.println("\n\tLOGIN");
            System.out.print("Password : ");
            String password = input.next();

            if (isValidLogin(username, password)) {
                System.out.println("\nSelamat datang, " + username + "!");
                // Lanjutkan dengan langkah-langkah selanjutnya setelah login
                // Contoh: displayMainMenu(role);
            } else {
                System.out.println("\nLogin gagal. Password tidak valid.");
            }
        } else {
            System.out.println("\nAkun belum dibuat. Silakan buat akun terlebih dahulu !");
            // Tambahkan opsi untuk membuat akun atau kembali ke menu utama
            System.out.println();
            System.out.println("*".repeat(28));            
            System.out.println("| 1. Create Account        |");
            System.out.println("| 2. Kembali ke Menu Utama |");
            System.out.println("*".repeat(28));        
            System.out.print("Pilih opsi (1/2) : ");
            int option = input.nextInt();

            if (option == 1) {
                createAccount();
                startMenu();
            } else if (option == 2) {
                startMenu();
            } else {
                System.out.println("Opsi tidak valid.");
            }
        }
    }

    private boolean isValidLogin(String username, String password) {
        Map<String, Akun> akunDatabase = Akun.getAkunDatabase();

        if (akunDatabase.containsKey(username)) {
            Akun akun = akunDatabase.get(username);
            return akun.getPassword().equals(password);
        }

        return false;
    }

    private void createAccount() {
        System.out.println("\n\tCREATE ACCOUNT");
        System.out.print("Username : ");
        String uname = input.next();
        System.out.print("Password : ");
        String pass = input.next();

        // Untuk sementara, beri ID default
        String id = "ID-" + System.currentTimeMillis();

        Akun.createAccount(id, uname, pass);
    }

    public static void main(String[] args) {
        new Main();
    }
}
