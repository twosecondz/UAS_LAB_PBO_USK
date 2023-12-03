import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Customer extends User {
    List<Product> cart;
    List<Transaction> transactionHistory;
    Scanner scanner = new Scanner(System.in);

    Customer(String username, String password) {
        super(username, password);
        cart = new ArrayList<>();
        transactionHistory = new ArrayList<>();
        scanner = new Scanner(System.in);
    }

    void viewProducts(List<Product> productList) {
        System.out.println("List Barang:");
        for (Product product : productList) {
            System.out.println("- " + product.name + " (ID: " + product.id + ", Price: " + product.price + ")");
        }
    }

    void addToCart(Product product) {
        cart.add(product);
        System.out.println(product.name + " ditambahkan ke dalam keranjang.");
    }

    void viewCart() {
        System.out.println("Keranjang Belanja:");
        for (Product product : cart) {
            System.out.println("- " + product.name + " (ID: " + product.id + ", Price: " + product.price + ")");
        }
        System.out.println("Total Items in Cart: " + cart.size());
    }

    void checkout(Payment payment, List<Transaction> transactionList) {
        if (cart.isEmpty()) {
            System.out.println("Keranjang belanja kosong. Tidak ada barang untuk checkout.");
            return;
        }

        System.out.println("Keranjang Belanja Anda");
        viewCart();

        System.out
                .print("Pilih produk untuk checkout (masukkan ID atau ketik 'selesai' untuk menyelesaikan checkout): ");
        String selectedProductId = scanner.next();

        List<Product> selectedProducts = new ArrayList<>();

        while (!selectedProductId.equalsIgnoreCase("selesai")) {
            Product selectedProduct = getProductById(cart, selectedProductId);

            if (selectedProduct != null) {
                selectedProducts.add(selectedProduct);
                System.out.println(selectedProduct.name + " ditambahkan ke dalam daftar checkout.");
            } else {
                System.out.println("Produk dengan ID " + selectedProductId + " tidak ditemukan dalam keranjang.");
            }

            System.out.print("Pilih produk lain atau ketik 'selesai': ");
            selectedProductId = scanner.next();
        }

        if (selectedProducts.isEmpty()) {
            System.out.println("Tidak ada produk yang dipilih untuk checkout.");
            return;
        }

        Transaction transaction = new Transaction(this, new ArrayList<>(cart), payment);
        System.out.println("Total Harga: " + transaction.getTotalAmount());
        transactionList.add(transaction);
        transactionHistory.add(transaction);
        // Memanggil metode processPayment untuk mengeksekusi logika pembayaran
        transaction.processPayment();
        System.out.println("Transaksi berhasil!");
        transaction.cetakResi(); // Menambah pemanggilan cetakResi()
        cart.clear();
    }

    void displayBankOptions() {
        System.out.println("Pilih Bank:");
        System.out.println("1. BSI");
        System.out.println("2. Mandiri");
        System.out.println("3. Bank Aceh");
        System.out.println("4. BCA");
        System.out.print("Masukkan pilihan (1/2/3/4): ");
    }

    void viewHistory() {
        System.out.println("History Belanja:");
        for (Transaction transaction : transactionHistory) {
            System.out.println("ID Transaksi: " + transaction.id);
            System.out.println("Jumlah Barang: " + transaction.products.size());
            System.out.println("Total Harga: " + transaction.totalAmount);
            System.out.println("Metode Pembayaran: " + transaction.payment);
            System.out.println("--------------");
        }
    }

    private Product getProductById(List<Product> productList, String productId) {
        return productList.stream()
                .filter(product -> product.id.equals(productId))
                .findFirst()
                .orElse(null);
    }
}

interface Authentication {
    boolean login(String username, String password);
}

class User implements Authentication {
    String username;
    String password;

    User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
}

class Admin extends User {
    static Scanner scanner = new Scanner(System.in);

    Admin(String username, String password) {
        super(username, password);
    }

    void addProduct(List<Product> productList) {
        System.out.print("Enter product ID: ");
        String productId = scanner.next();
        scanner.nextLine();
        System.out.print("Enter product name: ");
        String productName = scanner.nextLine();
        System.out.print("Enter product price: ");
        int productPrice = scanner.nextInt();

        Product newProduct = new Product(productId, productName, productPrice);
        productList.add(newProduct);
        System.out.println("Product added successfully!");
    }

    void manageProducts(List<Product> productList, List<Transaction> transactionList) {
        boolean isManagingProducts = true;

        while (isManagingProducts) {
            System.out.println("\nMenu Admin - Manage Products:");
            System.out.println("1. Add Product");
            System.out.println("2. View Products");
            System.out.println("3. Edit Product");
            System.out.println("4. Delete Product");
            System.out.println("5. View Transactions");
            System.out.println("6. Back to Main Menu");
            System.out.print("Choose option (1/2/3/4/5/6): ");
            int option = scanner.nextInt();

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
                    System.out.println("Invalid option.");
            }
        }
    }

    private void viewProducts(List<Product> productList) {
        System.out.println("List of Products:");
        for (Product product : productList) {
            System.out.println("- " + product.name + " (ID: " + product.id + ", Price: " + product.price + ")");
        }
    }

    private void editProduct(List<Product> productList) {
        System.out.print("Enter product ID to edit: ");
        String productId = scanner.next();
        scanner.nextLine();
        Product productToEdit = getProductById(productList, productId);

        if (productToEdit != null) {
            System.out.print("Enter new product name: ");
            String newProductName = scanner.nextLine();
            System.out.print("Enter new product price: ");
            int newProductPrice = scanner.nextInt();

            productToEdit.name = newProductName;
            productToEdit.price = newProductPrice;

            System.out.println("Product edited successfully!");
        } else {
            System.out.println("Product not found.");
        }
    }

    private void deleteProduct(List<Product> productList) {
        System.out.print("Enter product ID to delete: ");
        String productId = scanner.next();
        Product productToDelete = getProductById(productList, productId);

        if (productToDelete != null) {
            productList.remove(productToDelete);
            System.out.println("Product deleted successfully!");
        } else {
            System.out.println("Product not found.");
        }
    }

    private Product getProductById(List<Product> productList, String productId) {
        return productList.stream()
                .filter(product -> product.id.equals(productId))
                .findFirst()
                .orElse(null);
    }

    private void viewTransactions(List<Transaction> transactionList) {
        System.out.println("List of Transactions:");
        for (Transaction transaction : transactionList) {
            System.out.println("Transaction ID: " + transaction.id);
            System.out.println("User: " + transaction.user.username);
            System.out.println("Payment Method: " + transaction.payment);
            System.out.println("--------------");
        }
    }
}

class Product {
    String id;
    String name;
    int price;

    Product(String id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}

class Transaction {
    static int counter = 1;
    int id;
    User user;
    List<Product> products;
    Payment payment;
    int totalAmount;

    int getTotalAmount() {
        return totalAmount;
    }

    Transaction(User user, List<Product> products, Payment payment) {
        this.id = counter++;
        this.user = user;
        this.products = products;
        this.payment = payment;
        this.totalAmount = calculateTotalAmount();
    }

    private int calculateTotalAmount() {
        return products.stream().mapToInt(product -> product.price).sum();
    }

    void processPayment() {
        // Memanggil metode processPayment pada objek Payment
        payment.processPayment();
    }

    void cetakResi() {
        System.out.println("Resi Transaksi #" + id);
        System.out.println("----------------------------");
        System.out.println("User: " + user.username);
        System.out.println("Metode Pembayaran: " + payment);
        System.out.println("----------------------------");
        System.out.println("List Barang:");

        for (Product product : products) {
            System.out.println("- " + product.name + " (ID: " + product.id + ", Price: " + product.price + ")");
        }

        System.out.println("----------------------------");
        System.out.println("Total Harga: " + totalAmount);
        System.out.println("----------------------------");
        System.out.println("<<<< Terima kasih telah berbelanja di ICESCAPE >>>>");
    }
}

abstract class Payment {
    abstract void processPayment();
}

class QRIS extends Payment {
    @Override
    void processPayment() {
        System.out.println("Memproses pembayaran menggunakan QRIS...");
        // Implementasi logika pembayaran QRIS
    }

    public String toString() {
        return "QRIS";
    }
}

class Bank extends Payment {
    String bankName;

    Bank(String bankName) {
        this.bankName = bankName;
    }

    @Override
    void processPayment() {
        System.out.println("Memproses pembayaran melalui Bank " + bankName + "...");
        // Implementasi logika pembayaran Bank
    }

    public String toString() {
        return "Bank " + bankName;
    }
}

class COD extends Payment {
    @Override
    void processPayment() {
        System.out.println("Memproses pembayaran tunai (Cash on Delivery/COD)...");
        // Implementasi logika pembayaran COD
    }

    public String toString() {
        return "COD";
    }

}
public class Main {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        List<Product> productList = new ArrayList<>();
        List<Transaction> transactionList = new ArrayList<>();
        List<Customer> customerList = new ArrayList<>(); // Tambahkan list untuk menyimpan Customer
        Admin admin = new Admin("admin", "adminpass");
        Customer customer = new Customer("user", "userpass");

        productList.add(new Product("p1", "Nugget 500 Gr", 50000));
        productList.add(new Product("p2", "Dimsum 50 pcs", 120000));
        productList.add(new Product("p3", "Gyoza 10 pcs", 30000));
        productList.add(new Product("p4", "Cheese Dumpling 500 Gr", 35000));
        productList.add(new Product("p5", "Kentang Crincle 1 Kg", 50000));
        productList.add(new Product("p6", "Chicken Wings 500 Gr", 70000));
        productList.add(new Product("p7", "Empek-Empek 5 pcs", 20000));
        productList.add(new Product("p8", "Bakso Ayam 500 Gr", 40000));
        productList.add(new Product("p9", "Smoked Beef 250 Gr", 50000));
        productList.add(new Product("p10", "Mixed Vegetables 500 Gr", 33000));
        productList.add(new Product("p11", "Sosis Ayam 1 Kg", 50000));
        productList.add(new Product("p12", "Kebab Coklat 10 pcs", 50000));
        productList.add(new Product("p13", "Tortila 5 pcs", 15000));
        productList.add(new Product("p14", "Kulit Lumpia 50 Lbr", 8000));
        productList.add(new Product("p15", "American Risoles 10 pcs", 35000));

        boolean isLoggedIn = true;

        while (isLoggedIn) {
            System.out.println("<<< Selamat datang di ICESCAPE Shopping System >>>");
            System.out.println("1. Login sebagai Admin");
            System.out.println("2. Login sebagai Customer");
            System.out.println("3. Buat Akun Customer");
            System.out.println("4. Keluar dari aplikasi");
            System.out.print("Pilih opsi (1/2/3/4): ");
            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    isLoggedIn = adminLogin(admin, productList, transactionList);
                    break;
                case 2:
                    isLoggedIn = customerLogin(customer, productList, transactionList);
                    break;
                case 3:
                    createAccount(customerList, productList, transactionList);
                    break;
                case 4:
                    isLoggedIn = false;
                    System.out.println("Logging out");
                    break;
                default:
                    System.out.println("Opsi tidak valid.");
            }
        }
    }

    private static boolean adminLogin(Admin admin, List<Product> productList, List<Transaction> transactionList) {
        boolean isLoggedIn = false;

        do {
            System.out.print("Enter username: ");
            String username = scanner.next();
            System.out.print("Enter password: ");
            String password = scanner.next();

            if (admin.login(username, password)) {
                admin.manageProducts(productList, transactionList);
                isLoggedIn = true;
            } else {
                System.out.println("Login gagal. Username atau password salah.");
            }
        } while (!isLoggedIn);

        return isLoggedIn;
    }

    private static boolean customerLogin(Customer customer, List<Product> productList,
            List<Transaction> transactionList) {
        boolean isLoggedIn = false;

        do {
            System.out.print("Enter username: ");
            String username = scanner.next();
            System.out.print("Enter password: ");
            String password = scanner.next();

            if (customer.login(username, password)) {
                customerMenu(customer, productList, transactionList);
                isLoggedIn = true;
            } else {
                System.out.println("Login gagal. Username atau password salah.");
            }
        } while (!isLoggedIn);

        return isLoggedIn;
    }

    private static void createAccount(List<Customer> customerList, List<Product> productList,
            List<Transaction> transactionList) {
        System.out.println("Membuat Akun Baru:");

        System.out.print("Enter username: ");
        String username = scanner.next();
        System.out.print("Enter password: ");
        String password = scanner.next();

        Customer newCustomer = new Customer(username, password);
        customerList.add(newCustomer);

        System.out.println("Akun berhasil dibuat. Silakan login untuk melanjutkan.");
        customerLogin(newCustomer, productList, transactionList);
    }

    private static void customerMenu(Customer customer, List<Product> productList, List<Transaction> transactionList) {
        boolean isLoggedIn = true;

        // Membuat objek pembayaran default
        Payment payment = new QRIS();

        while (isLoggedIn) {
            System.out.println("\nMenu Customer:");
            System.out.println("1. Lihat List Barang");
            System.out.println("2. Masukkan Barang ke Keranjang");
            System.out.println("3. Lihat Keranjang");
            System.out.println("4. Checkout");
            System.out.println("5. Lihat History Belanja");
            System.out.println("6. Kembali ke menu utama");
            System.out.print("Pilih opsi (1/2/3/4/5/6): ");
            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    customer.viewProducts(productList);
                    break;
                case 2:
                    System.out.print("Enter product ID to add to cart: ");
                    String productId = scanner.next();
                    Product selectedProduct = productList.stream()
                            .filter(product -> product.id.equals(productId))
                            .findFirst()
                            .orElse(null);

                    if (selectedProduct != null) {
                        customer.addToCart(selectedProduct);
                    } else {
                        System.out.println("Product not found.");
                    }
                    break;
                case 3:
                    customer.viewCart();
                    break;
                case 4:
                    // Pilihan metode pembayaran
                    System.out.println("Pilih metode pembayaran:");
                    System.out.println("1. QRIS");
                    System.out.println("2. Bank");
                    System.out.println("3. COD");
                    System.out.print("Masukkan pilihan (1/2/3): ");
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
                                    payment = new Bank("Mandiri");
                                    break;
                                case 3:
                                    payment = new Bank("Bank Aceh");
                                    break;
                                case 4:
                                    payment = new Bank("BCA");
                                    break;
                                default:
                                    System.out.println("Pilihan bank tidak valid.");
                                    return;
                            }
                            break;
                        case 3:
                            payment = new COD();
                            break;
                        default:
                            System.out.println("Pilihan metode pembayaran tidak valid.");
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
                    System.out.println("Logging out..");
                    break;
                default:
                    System.out.println("Opsi tidak valid.");
            }
        }
    }
}
