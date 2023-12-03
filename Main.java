import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Online Shopping with Java
 * Sistem perbelanjaan dibuat dengan menggunakan interface CLI (Command Line Interface)
 *
 * @author M. Syahidal Akbar Zas
 * @author Farhanul Khair
 * @author Nazwa Salsabila
 * @version 99.0
 * @since 2023-12-01
 * 
 * https://github.com/twosecondz/UAS_LAB_PBO_USK
 */

/**
 * Kelas Customer merupakan turunan dari kelas User dan mewakili pengguna dengan
 * peran customer
 * pada program shopping.
 */
class Customer extends User {
    List<Product> cart;
    List<Transaction> transactionHistory;
    Scanner scanner = new Scanner(System.in);

    /**
     * Konstruktor untuk membuat objek Customer dengan username dan password.
     *
     * @param username Username customer.
     * @param password Password customer.
     */
    Customer(String username, String password) {
        super(username, password);
        cart = new ArrayList<>();
        transactionHistory = new ArrayList<>();
        scanner = new Scanner(System.in);
    }

    /**
     * Menampilkan daftar produk kepada customer.
     *
     * @param productList Daftar produk yang tersedia.
     */
    void viewProducts(List<Product> productList) {
        Main.clearScreen();
        System.out.println("<".repeat(25) + "LIST BARANG " + ">".repeat(25));
        System.out.println();
        for (Product product : productList) {
            System.out.println("\nNama Barang : " + product.name);
            System.out.println("ID          : " + product.id);
            System.out.println("Harga       : Rp." + product.price);
        }
    }

    /**
     * Menambahkan produk ke dalam keranjang belanja.
     *
     * @param product Produk yang akan ditambahkan ke keranjang.
     */
    void addToCart(Product product) {
        cart.add(product);
        System.out.println(product.name + " telah ditambahkan ke keranjang.");
    }

    /**
     * Menampilkan isi keranjang belanja customer.
     */
    void viewCart() {
        Main.clearScreen();
        System.out.println("<".repeat(25) + " KERANJANG BELANJA " + ">".repeat(25));
        System.out.println();
        for (Product product : cart) {
            System.out.println("\nNama Barang : " + product.name);
            System.out.println("ID          : " + product.id);
            System.out.println("Harga       : Rp." + product.price);
        }
        System.out.println("\nTotal item di keranjang : " + cart.size());
    }

    /**
     * Melakukan proses checkout dengan memilih produk dari keranjang.
     *
     * @param payment         Metode pembayaran yang dipilih.
     * @param transactionList Daftar transaksi yang akan diperbarui.
     */
    void checkout(Payment payment, List<Transaction> transactionList) {
        if (cart.isEmpty()) {
            System.out.println("\nKeranjang belanja kosong. Tidak ada barang untuk checkout.");
            return;
        }

        System.out.println("<".repeat(15) + " KERANJANG BELANJA ANDA " + ">".repeat(15));
        System.out.println();
        viewCart();

        System.out.print("\nPilih produk untuk checkout (masukkan ID barang) : ");
        String selectedProductId = scanner.next();

        List<Product> selectedProducts = new ArrayList<>();

        while (!selectedProductId.equalsIgnoreCase("Y")) {
            Product selectedProduct = getProductById(cart, selectedProductId);

            if (selectedProduct != null) {
                selectedProducts.add(selectedProduct);
                System.out.println(selectedProduct.name + " telah ditambahkan ke dalam daftar checkout.");
            } else {
                System.out.println("\nProduk dengan ID " + selectedProductId + " tidak ada di keranjang");
            }

            System.out.println();
            System.out.print("Masukan id produk lain untuk CheckOut atau Selesai? (ketik Y) : ");
            selectedProductId = scanner.next();

        }

        if (selectedProducts.isEmpty()) {
            System.out.println("\nProduk untuk checkout belum dipilih");
            return;
        }

        Transaction transaction = new Transaction(this, new ArrayList<>(cart), payment);
        System.out.println("Total Harga : " + transaction.getTotalAmount());
        transactionList.add(transaction);
        transactionHistory.add(transaction);

        // Memanggil metode processPayment untuk mengeksekusi logika pembayaran
        transaction.processPayment();
        System.out.println("\nTransaksi berhasil !");

        // cetak resi
        transaction.cetakResi();
        cart.clear();
    }

    /**
     * Menampilkan opsi transfer bank kepada customer.
     */
    void displayBankOptions() {
        Main.clearScreen();
        System.out.println("<".repeat(25) + " TRANSFER BANK " + ">".repeat(25));
        System.out.println();
        System.out.println("=".repeat(35));
        System.out.println("|            Pilih Bank           |");
        System.out.println("=".repeat(35));
        System.out.println("| 1. BSI                          |");
        System.out.println("| 2. Bank Aceh                    |");
        System.out.println("| 3. Bank BRI                     |");
        System.out.println("| 4. Bank Nagari                  |");
        System.out.println("=".repeat(35));
        System.out.print("Masukkan pilihan (1/2/3/4) : ");
    }

    /**
     * Menampilkan riwayat belanja customer.
     */
    void viewHistory() {
        Main.clearScreen();
        System.out.println("<".repeat(15) + " RIWAYAT BELANJA " + ">".repeat(15));
        System.out.println();
        for (Transaction transaction : transactionHistory) {
            System.out.println("ID Transaksi : " + transaction.id);
            System.out.println("Jumlah Barang : " + transaction.products.size());
            System.out.println("Total Harga : " + transaction.totalAmount);
            System.out.println("Metode Pembayaran : " + transaction.payment);
        }
    }

    /**
     * Mendapatkan objek produk berdasarkan ID dari daftar produk.
     *
     * @param productList Daftar produk yang tersedia.
     * @param productId   ID produk yang dicari.
     * @return Objek Product jika ditemukan, atau null jika tidak ditemukan.
     */
    private Product getProductById(List<Product> productList, String productId) {
        return productList.stream()
                .filter(product -> product.id.equals(productId))
                .findFirst()
                .orElse(null);
    }
}

/**
 * Interface Authentication menyediakan metode untuk proses otentikasi,
 * khususnya untuk login dengan menggunakan username dan password.
 */
interface Authentication {

    // Memeriksa apakah kombinasi username dan password valid untuk proses login.
    boolean login(String username, String password);
}

/**
 * Kelas User merepresentasikan entitas pengguna dalam sistem.
 * Implementasi Authentication untuk proses otentikasi login.
 */
class User implements Authentication {
    String username; // Variable untuk menampung username user
    String password; // Variable untuk menampung password user

    // Konstruktor untuk membuat objek User dengan username dan password tertentu.
    User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Implementasi metode login dari antarmuka Authentication.
     * Memeriksa apakah kombinasi username dan password valid untuk login.
     */
    @Override
    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
}

/**
 * Kelas Admin adalah turunan dari kelas User dan digunakan untuk mengelola
 * barang
 * dan melihat daftar transaksi. Admin memiliki akses ke beberapa menu
 * pengelolaan
 * seperti menambah, melihat, mengedit, dan menghapus barang serta melihat
 * daftar transaksi.
 */
class Admin extends User {
    static Scanner scanner = new Scanner(System.in);

    // Konstruktor untuk membuat objek Admin dengan username dan password tertentu.
    Admin(String username, String password) {
        super(username, password);
    }

    /**
     * Metode untuk mengelola barang dan menu administrasi.
     *
     * @param productList     Daftar produk yang akan dikelola.
     * @param transactionList Daftar transaksi yang akan dilihat.
     */
    void manageProducts(List<Product> productList, List<Transaction> transactionList) {
        boolean isManagingProducts = true;

        while (isManagingProducts) {

            // Tampilan menu admin
            System.out.println();
            System.out.println("<".repeat(25) + " ADMIN MENU " + ">".repeat(25));
            System.out.println();
            System.out.println("=".repeat(36));
            System.out.println("|   Menu Admin                     |");
            System.out.println("=".repeat(36));
            System.out.println("| 1. Tambah Barang                 |");
            System.out.println("| 2. Lihat Barang                  |");
            System.out.println("| 3. Edit Barang                   |");
            System.out.println("| 4. Hapus Barang                  |");
            System.out.println("| 5. Lihat Transaksi               |");
            System.out.println("| 6. LogOut                        |");
            System.out.println("=".repeat(36));
            System.out.print("Pilih opsi: ");
            int option = scanner.nextInt();

            // Switch case untuk memproses pilihan user admin
            switch (option) {
                case 1:
                    addProduct(productList);
                    break;
                case 2:
                    viewProducts(productList);
                    break;
                case 3:
                    editProduct(productList);
                    break;
                case 4:
                    deleteProduct(productList);
                    break;
                case 5:
                    viewTransactions(transactionList);
                    break;
                case 6:
                    isManagingProducts = false;
                    break;
                default:
                    System.out.println("\nSilahkan pilih 1-6!.");
            }
        }
    }

    /**
     * Metode untuk menambahkan barang baru ke dalam daftar produk.
     *
     * @param productList Daftar produk yang akan diperbarui dengan barang baru.
     */
    void addProduct(List<Product> productList) {
        Main.clearScreen();
        System.out.println("<".repeat(15) + " PENAMBAHAN BARANG " + ">".repeat(15));
        System.out.println();
        System.out.print("Masukkan ID barang    : ");
        String productId = scanner.next();
        scanner.nextLine();
        System.out.print("Masukkan nama barang  : ");
        String productName = scanner.nextLine();
        System.out.print("Masukkan harga barang : ");
        int productPrice = scanner.nextInt();

        Product newProduct = new Product(productId, productName, productPrice);
        productList.add(newProduct);
        System.out.println("\nBarang berhasil ditambahkan!");
    }

    /**
     * Metode untuk melihat daftar produk.
     *
     * @param productList Daftar produk yang akan ditampilkan.
     */
    private void viewProducts(List<Product> productList) {
        Main.clearScreen();
        System.out.println();
        System.out.println("<".repeat(15) + " LIST BARANG " + ">".repeat(15));
        for (Product product : productList) {
            System.out.println("\nNama Barang : " + product.name);
            System.out.println("ID          : " + product.id);
            System.out.println("Harga       : Rp." + product.price);
        }
    }

    /**
     * Metode untuk mengedit barang dalam daftar produk.
     *
     * @param productList Daftar produk yang akan diperbarui setelah pengeditan.
     */
    private void editProduct(List<Product> productList) {
        Main.clearScreen();

        System.out.println("<".repeat(15) + " PENGEDITAN BARANG " + ">".repeat(15));
        System.out.println();
        System.out.print("Masukkan ID barang untuk edit barang : ");
        String productId = scanner.next();
        scanner.nextLine();
        Product productToEdit = getProductById(productList, productId);

        // Permisalan jika user meng input id barang yang barangnya tidak ada dan ada
        if (productToEdit != null) {
            System.out.print("Masukkan nama barang baru : ");
            String newProductName = scanner.nextLine();
            System.out.print("Masukkan harga barang baru : ");
            int newProductPrice = scanner.nextInt();

            productToEdit.name = newProductName;
            productToEdit.price = newProductPrice;

            System.out.println("\nBarang berhasil di edit!");
        } else {
            System.out.println("\nBarang tidak ada!");
        }
    }

    /**
     * Metode untuk menghapus barang dari daftar produk.
     *
     * @param productList Daftar produk yang akan diperbarui setelah penghapusan.
     */
    private void deleteProduct(List<Product> productList) {
        Main.clearScreen();

        System.out.println("<".repeat(15) + " PENGHAPUSAN BARANG " + ">".repeat(15));
        System.out.println();
        System.out.print("Masukkan ID barang untuk hapus barang : ");
        String productId = scanner.next();
        Product productToDelete = getProductById(productList, productId);

        // Permisalan jika user meng input id barang yang barangnya tidak ada dan ada
        if (productToDelete != null) {
            productList.remove(productToDelete);
            System.out.println("\nBarang berhasil dihapus !");
        } else {
            System.out.println("\nBarang tidak ditemukan.");
        }
    }

    /**
     * Metode untuk mendapatkan objek Product berdasarkan ID dari daftar produk.
     *
     * @param productList Daftar produk yang akan dicari.
     * @param productId   ID produk yang akan dicari.
     * @return Objek Product yang sesuai dengan ID atau null jika tidak ditemukan.
     */
    private Product getProductById(List<Product> productList, String productId) {
        return productList.stream()
                .filter(product -> product.id.equals(productId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Metode untuk melihat daftar transaksi.
     *
     * @param transactionList Daftar transaksi yang akan ditampilkan.
     */
    private void viewTransactions(List<Transaction> transactionList) {
        System.out.println();
        System.out.println("<".repeat(10) + " DAFTAR TRANSAKSI " + ">".repeat(10));
        for (Transaction transaction : transactionList) {
            System.out.println("\nID Transaksi : " + transaction.id);
            System.out.println("User : " + transaction.user.username);
            System.out.println("Metode Bayar : " + transaction.payment);
        }
    }
}

/**
 * Kelas Product merepresentasikan barang yang dapat dibeli atau dikelola dalam
 * sistem.
 * Setiap produk memiliki ID unik, nama, dan harga.
 */
class Product {
    String id; // Variable id unik untuk mengidentifikasi barang
    String name; // Variable nama barang
    int price; // Variable harga barang

    // Konstruktor untuk membuat objek Product dengan ID, nama, dan harga tertentu.
    Product(String id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}

/**
 * Kelas Transaction merepresentasikan transaksi pembelian barang oleh pengguna.
 * Setiap transaksi memiliki ID unik, pengguna yang melakukan transaksi, daftar
 * barang,
 * metode pembayaran, dan total jumlah pembelian.
 */
class Transaction {
    User user; // Variable user untuk yang melakukan transaksi
    List<Product> products; // Variable daftar barang yang dibeli dalam transaksi
    Payment payment; // Variable metode pembayaran yang digunakan dalam transaksi

    static int counter = 1; // Variable statis untuk menghitung jumlah transaksi dan id unik
    int id; // Variable id untuk mengidentifikasi transaksi
    int totalAmount; // Variable total jumlah pembelian dalam transaksi

    // Mendapatkan total jumlah pembelian dalam transaksi.
    int getTotalAmount() {
        return totalAmount;
    }

    // Konstruktor untuk membuat objek Transaction dengan pengguna, daftar barang,
    // dan metode pembayaran tertentu.
    Transaction(User user, List<Product> products, Payment payment) {
        this.id = counter++;
        this.user = user;
        this.products = products;
        this.payment = payment;
        this.totalAmount = calculateTotalAmount();
    }

    /**
     * Menghitung total jumlah pembelian berdasarkan daftar barang dalam transaksi.
     * 
     * @return Total jumlah pembelian.
     */
    private int calculateTotalAmount() {
        return products.stream().mapToInt(product -> product.price).sum();
    }

    // Memproses pembayaran menggunakan metode pembayaran yang telah dipilih.
    void processPayment() {
        // Memanggil metode processPayment pada objek Payment
        payment.processPayment();
    }

    // Mencetak struk pembayaran transaksi dengan detail pengguna, daftar barang,
    // total jumlah pembelian, dan pesan terima kasih.
    void cetakResi() {
        Main.clearScreen();
        System.out.println();
        System.out.println("<".repeat(10) + " STRUK PEMBAYARAN " + ">".repeat(10));
        System.out.println();
        System.out.println("Resi Transaksi #" + id);
        System.out.println("-".repeat(35));
        System.out.println("User: " + user.username);
        System.out.println("Metode Pembayaran: " + payment);
        System.out.println("-".repeat(35));
        System.out.println("List Barang:");

        for (Product product : products) {
            System.out.println("\nNama Barang : " + product.name);
            System.out.println("ID          : " + product.id);
            System.out.println("Harga       : Rp." + product.price);
        }

        System.out.println("-".repeat(35));
        System.out.println("Total Harga: " + totalAmount);
        System.out.println("-".repeat(35));
        System.out.println("<".repeat(25) + "Terima kasih telah berbelanja di ICESCAPE" + ">".repeat(25));
        System.out.println();
    }
}

/**
 * Kelas abstrak Payment merepresentasikan suatu metode pembayaran yang dapat
 * diimplementasikan
 * oleh kelas-kelas turunannya. Metode pembayaran umumnya memiliki proses
 * pembayaran yang berbeda-beda.
 * Kelas ini dirancang untuk memberikan kerangka dasar untuk implementasi metode
 * pembayaran.
 */
abstract class Payment {

    /**
     * Metode abstrak yang harus diimplementasikan oleh kelas turunan.
     * Metode ini digunakan untuk menjalankan proses pembayaran sesuai dengan aturan
     * metode pembayaran tertentu.
     */
    abstract void processPayment();
}

/**
 * Kelas QRIS merupakan turunan dari kelas Payment dan mengimplementasikan
 * metode pembayaran
 * menggunakan QRIS.
 * Metode pembayaran ini hanya menampilkan pesan bahwa pembayaran sedang
 * diproses menggunakan QRIS.
 */
class QRIS extends Payment {

    /**
     * Implementasi metode processPayment untuk melakukan pembayaran menggunakan
     * QRIS.
     * Metode ini hanya mencetak pesan bahwa pembayaran sedang diproses menggunakan
     * QRIS.
     */
    @Override
    void processPayment() {
        System.out.println("\n'Anda membayar dengan QRIS'");
    }

    // Metode override dari toString untuk memberikan representasi string dari objek
    // QRIS.
    public String toString() {
        return "QRIS";
    }
}

/**
 * Kelas Bank merupakan turunan dari kelas Payment dan menyediakan metode
 * pembayaran melalui suatu bank.
 * Kelas ini memperoleh nama bank pada saat pembuatan objek dan menggunakan nama
 * tersebut
 * dalam proses pembayaran dan representasi string.
 */
class Bank extends Payment {
    String bankName; // Variable untuk nama bank yang digunakan untuk pembayaran

    // Konstruktor untuk membuat objek Bank dengan menyimpan nama bank.
    Bank(String bankName) {
        this.bankName = bankName;
    }

    /**
     * Implementasi metode processPayment untuk melakukan pembayaran melalui bank.
     * Metode ini mencetak pesan bahwa pembayaran sedang diproses melalui bank
     * tertentu.
     */
    @Override
    void processPayment() {
        System.out.println("\n'Anda membayar dengan " + bankName);
    }

    // Metode override dari toString untuk memberikan representasi string dari objek
    // Bank.
    public String toString() {
        return "Bank " + bankName;
    }
}

/**
 * Kelas COD (Cash on Delivery) merupakan turunan dari kelas Payment dan
 * menyediakan metode pembayaran tunai.
 * Kelas ini mengimplementasikan proses pembayaran tunai dan memberikan
 * representasi string "COD".
 */
class COD extends Payment {

    /**
     * Implementasi metode processPayment untuk melakukan pembayaran tunai (Cash on
     * Delivery/COD).
     * Metode ini mencetak pesan bahwa pembayaran sedang diproses secara tunai.
     */
    @Override
    void processPayment() {
        System.out.println("\n'Anda COD'");
    }

    // Metode override dari toString untuk memberikan representasi string dari objek
    // COD.
    public String toString() {
        return "COD";
    }
}

/**
 * Kelas utama yang menjalankan aplikasi ICESCAPE Shopping System.
 * Digunakan untuk menangani login, manajemen pengguna, dan interaksi dengan
 * produk serta transaksi.
 */
public class Main {
    static Scanner scanner = new Scanner(System.in);

    // Fungsi untuk menangani login dan menu utama aplikasi.
    public void login() {
        List<Product> productList = new ArrayList<>();
        List<Transaction> transactionList = new ArrayList<>();
        List<Customer> customerList = new ArrayList<>();

        Admin admin = new Admin("admin", "admin12345");
        Customer customer = new Customer("user", "user12345");

        // Menambahkan data barang
        productList.add(new Product("A01", "Paracetamol", 2000));
        productList.add(new Product("A02", "Ibu Profen", 2600));
        productList.add(new Product("A03", "Aspirin", 20000));
        productList.add(new Product("A04", "Cetirizine", 19000));
        productList.add(new Product("A05", "Loratadine", 3000));
        productList.add(new Product("B01", "Sucralfate", 22963));
        productList.add(new Product("B02", "Amoxcilin", 7761));
        productList.add(new Product("B03", "Multivitamin", 47000));
        productList.add(new Product("B04", "Folid Acid", 12500));
        productList.add(new Product("B05", "Calcium Syrup", 52500));
        productList.add(new Product("C01", "Omeprazole", 21900));
        productList.add(new Product("C02", "Esomeprazole", 90000));
        productList.add(new Product("C03", "Raberparzol", 1400));
        productList.add(new Product("C04", "Cephalexin", 6800));
        productList.add(new Product("C05", "Clindamycin", 11935));
        productList.add(new Product("D01", "Hydrocortisone", 3921));
        productList.add(new Product("D02", "Clotrimazole", 21000));
        productList.add(new Product("D03", "Diclofenac", 12451));
        productList.add(new Product("D04", "Betamethasone", 3716));
        productList.add(new Product("D05", "Neomycin Ointment", 20900));

        boolean isLoggedIn = true;

        while (isLoggedIn) {

            // Tampilan menu awal
            clearScreen();
            System.out.println();
            System.out.println("<".repeat(25) + " Selamat datang di Online Apotek Nazwa dkk " + ">".repeat(25));
            System.out.println();
            System.out.println("=".repeat(35));
            System.out.println("|               Menu              |");
            System.out.println("=".repeat(35));
            System.out.println("| 1. Login Customer               |");
            System.out.println("| 2. Login Admin                  |");
            System.out.println("| 3. Buat Akun Customer           |");
            System.out.println("| 4. LogOut                       |");
            System.out.println("=".repeat(35));
            System.out.print("Pilih: ");
            int option = scanner.nextInt();

            // Switch case untuk memproses pilihan user
            switch (option) {
                case 1:
                    clearScreen();
                    isLoggedIn = customerLogin(customer, productList, transactionList);
                    break;
                case 2:
                    clearScreen();
                    isLoggedIn = adminLogin(admin, productList, transactionList);
                    break;
                case 3:
                    clearScreen();
                    createAccount(customerList, productList, transactionList);
                    break;
                case 4:
                    isLoggedIn = false;
                    System.out.println("\nLogging out");
                    break;
                default:
                    System.out.println("\nOpsi tidak valid.");
            }
        }
    }

    // Fungsi untuk melakukan login sebagai admin.
    private static boolean adminLogin(Admin admin, List<Product> productList, List<Transaction> transactionList) {
        boolean isLoggedIn = false;

        do {
            System.out.println();
            System.out.println("<".repeat(25) + " LOGGIN AS ADMIN " + ">".repeat(25));
            System.out.println();
            System.out.print("Enter username : ");
            String username = scanner.next();
            System.out.print("Enter password : ");
            String password = scanner.next();

            // Mengecek kebenaran username dan password admin
            if (admin.login(username, password)) {
                clearScreen();
                admin.manageProducts(productList, transactionList);
                isLoggedIn = true;
            } else {
                System.out.println("\nLogin gagal!. Username atau password salah!.");
            }
        } while (!isLoggedIn);

        return isLoggedIn;
    }

    // Fungsi untuk melakukan login sebagai customer.
    private static boolean customerLogin(Customer customer, List<Product> productList,
            List<Transaction> transactionList) {
        boolean isLoggedIn = false;

        do {
            System.out.println();
            System.out.println("<".repeat(25) + " LOGGIN AS CUSTOMER " + ">".repeat(25));
            System.out.println();
            System.out.print("Enter username : ");
            String username = scanner.next();
            System.out.print("Enter password : ");
            String password = scanner.next();

            // Mengecek kebenaran username dan password customer
            if (customer.login(username, password)) {
                clearScreen();
                customerMenu(customer, productList, transactionList);
                isLoggedIn = true;
            } else {
                System.out.println("\nLogin gagal!. Username atau password salah!.");
            }
        } while (!isLoggedIn);

        return isLoggedIn;
    }

    /**
     * Fungsi untuk membuat akun pelanggan baru dan menambahkannya ke dalam daftar
     * pelanggan.
     * Setelah membuat akun, fungsi ini secara otomatis melakukan login menggunakan
     * akun baru tersebut.
     */
    private static void createAccount(List<Customer> customerList, List<Product> productList,
            List<Transaction> transactionList) {

        clearScreen();
        System.out.println();
        System.out.println("<".repeat(10) + " Create new account " + ">".repeat(10));
        System.out.println();
        System.out.print("Enter username : ");
        String username = scanner.next();
        System.out.print("Enter password : ");
        String password = scanner.next();

        Customer newCustomer = new Customer(username, password);
        customerList.add(newCustomer);

        System.out.println("\nAkun berhasil dibuat. Silakan login untuk melanjutkan.");
        customerLogin(newCustomer, productList, transactionList);
    }

    /**
     * Fungsi untuk menampilkan menu utama dan mengelola interaksi pelanggan dengan
     * aplikasi.
     * Pengguna dapat melihat daftar barang, menambahkan barang ke keranjang,
     * melihat keranjang,
     * melakukan proses checkout, melihat riwayat belanja, dan kembali ke menu
     * utama.
     */
    private static void customerMenu(Customer customer, List<Product> productList, List<Transaction> transactionList) {
        boolean isLoggedIn = true;

        // Membuat objek pembayaran default
        Payment payment = new QRIS();

        while (isLoggedIn) {

            // Tamppilan menu customer
            System.out.println();
            System.out.println("<".repeat(25) + " MENU CUSTOMER " + ">".repeat(25));
            System.out.println();
            System.out.println("=".repeat(35));
            System.out.println("|          Menu Customer          |");
            System.out.println("=".repeat(35));
            System.out.println("| 1. List barang                  |");
            System.out.println("| 2. Masukkan barang ke keranjang |");
            System.out.println("| 3. Lihat keranjang              |");
            System.out.println("| 4. CheckOut                     |");
            System.out.println("| 5. History belanja              |");
            System.out.println("| 6. LogOut                       |");
            System.out.println("=".repeat(35));
            System.out.print("Pilih: ");
            int option = scanner.nextInt();

            // Switch case untuk memproses pilihan user
            switch (option) {
                case 1:
                    customer.viewProducts(productList);
                    break;
                case 2:
                    clearScreen();
                    customer.viewProducts(productList);
                    System.out.println();
                    System.out.println("<".repeat(25) + " ADD BARANG KE KERANJANG " + ">".repeat(25));
                    System.out.println();
                    System.out.print("Masukkan ID barang untuk menambahkannya ke keranjang : ");
                    String productId = scanner.next();
                    Product selectedProduct = productList.stream()
                            .filter(product -> product.id.equals(productId))
                            .findFirst()
                            .orElse(null);

                    if (selectedProduct != null) {
                        customer.addToCart(selectedProduct);
                    } else {
                        System.out.println("\nBarang tidak ditemukan.");
                    }
                    break;
                case 3:
                    customer.viewCart();
                    break;
                case 4:
                    // Pilihan metode pembayaran
                    clearScreen();
                    System.out.println("<".repeat(25) + " CHECKOUT " + ">".repeat(25));
                    System.out.println();
                    System.out.println("=".repeat(35));
                    System.out.println("|     Pilih metode pembayaran     |");
                    System.out.println("=".repeat(35));
                    System.out.println("| 1. QRIS                         |");
                    System.out.println("| 2. Bank                         |");
                    System.out.println("| 3. COD                          |");
                    System.out.println("=".repeat(35));
                    System.out.print("Pilih: ");
                    int paymentOption = scanner.nextInt();

                    // Set objek pembayaran berdasarkan pilihan
                    switch (paymentOption) {
                        case 1:
                            payment = new QRIS();
                            break;
                        case 2:
                            customer.displayBankOptions();
                            int bankOption = scanner.nextInt();

                            switch (bankOption) {
                                case 1:
                                    payment = new Bank("BSI");
                                    break;
                                case 2:
                                    payment = new Bank("Bank Aceh");
                                    break;
                                case 3:
                                    payment = new Bank("Bank BRI");
                                    break;
                                case 4:
                                    payment = new Bank("Bank Nagari");
                                    break;
                                default:
                                    System.out.println("\nPilihan bank tidak valid.");
                                    return;
                            }
                            break;
                        case 3:
                            payment = new COD();
                            break;
                        default:
                            System.out.println("\nPilihan metode pembayaran tidak valid.");
                            break;
                    }

                    // Melakukan proses checkout dengan objek pembayaran yang telah dipilih
                    customer.checkout(payment, transactionList);
                    break;
                case 5:
                    customer.viewHistory();
                    break;
                case 6:
                    isLoggedIn = false;
                    System.out.println("\nLogging out!!!! Terimakasih sudah berbelanja!");
                    break;
                default:
                    System.out.println("\nOpsi tidak valid.");
            }
        }
    }

    // Method untuk membersihkan layar ketika di enter {fix error}
    public static void clearScreen() {
        try {
            // Membuat objek ProcessBuilder untuk menjalankan perintah shell
            // Perintah shell yang digunakan bergantung pada sistem operasi
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (IOException | InterruptedException e) {
            // Menangani eksepsi yang mungkin terjadi
            e.printStackTrace();
        }
    }

    // Metode utama yang memulai aplikasi dan memanggil fungsi login.
    public static void main(String[] args) {
        Main main = new Main();
        main.login();
    }

}
