import java.util.Scanner;
import java.util.Map;

public class Main {
    public Driver driverAkun;
    public Akun akun = new Akun();
    private Scanner input = new Scanner(System.in);

    public Main() {
        startMenu();
    }

    private void startMenu() {
        int pilStart;
        do {
            System.out.println();
            System.out.println("=".repeat(40));
            System.out.println("|   Selamat datang di Three Market !   |");
            System.out.println("=".repeat(40));
            System.out.println("| 1. Create Account                    |");
            System.out.println("| 2. Login                             |");
            System.out.println("| 3. Keluar                            |");
            System.out.println("=".repeat(40));
            System.out.print("Pilih opsi (1/2/3): ");
            pilStart = input.nextInt();

            if (pilStart == 1) {
                createAccount();
            } else if (pilStart == 2) {
                loginMenu();
            } else if (pilStart == 3) {
                System.out.println("\nTerima kasih sudah berkunjung ! ");
            } else {
                System.out.println("\nInput tidak valid. Silahkan coba lagi.");
            }
        } while (pilStart != 1 && pilStart != 2 && pilStart != 3);
    }

    private void loginMenu() {
        int pilLogin;
        do {
            System.out.println();
            System.out.println("=".repeat(22));
            System.out.println("|   Login sebagai    |");
            System.out.println("=".repeat(22));
            System.out.println("| 1. Admin           |");
            System.out.println("| 2. Customer        |");
            System.out.println("=".repeat(22));
            System.out.print("Pilih opsi (1/2): ");
            pilLogin = input.nextInt();

            if (pilLogin == 1 || pilLogin == 2) {
                login((pilLogin == 1) ? "Admin" : "Customer", (pilLogin == 1) ? "ID-Admin" : "ID-Customer");
            } else {
                System.out.println("\nOpsi tidak valid. Silahkan coba lagi.");
            }
        } while (pilLogin != 1 && pilLogin != 2);
    }

    private void login(String role, String idPrefix) {
        Map<String, String> akunDatabase = akun.getAkunDatabase();

        System.out.print("\nMasukkan username : ");
        String username = input.next();
        
        if (akunDatabase.containsKey(idPrefix + "-" + username)) {
            System.out.print("Masukkan password : ");
            String password = input.next();

            if (akun.login(username, password)) {
                System.out.println("\nLogin berhasil!");
            } else {
                System.out.println("\nLogin gagal. Cek kembali username dan password.");
            }
        } else {
            System.out.println("\nAkun belum dibuat. Silahkan buat akun terlebih dahulu");
            startMenu();
        }
    }

    private void createAccount() {
        int pilAcc;
        do {
            System.out.println();
            System.out.println("=".repeat(30));
            System.out.println("|   Create account sebagai   |");
            System.out.println("=".repeat(30));
            System.out.println("| 1. Admin                   |");
            System.out.println("| 2. Customer                |");
            System.out.println("=".repeat(30));
            System.out.print("Pilih opsi (1/2): ");
            pilAcc = input.nextInt();

            System.out.print("\nMasukkan username : ");
            String username = input.next();
            System.out.print("Masukkan password : ");
            String password = input.next();

            if (pilAcc == 1 || pilAcc == 2) {
                akun.createAccount((pilAcc == 1) ? "Admin" : "Customer", (pilAcc == 1) ? "ID-Admin" : "ID-Customer", username, password);
                startMenu();
            } else {
                System.out.println("\nOpsi tidak valid.");
            }
        } while (pilAcc != 1 && pilAcc != 2);
    }

    public static void main(String[] args) {
        new Main();
    }
}