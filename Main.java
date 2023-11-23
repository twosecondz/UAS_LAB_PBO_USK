import java.util.ArrayList;

class Akun {
    public String id;
}

class Admin {
}

class Customer {
    Keranjang keranjang;
    ArrayList<Invoice> invoiceSelesai;
}

abstract class Driver {
}

class AdminDriver {
    public Admin akun;
    public ListBarang listBarang;
    ArrayList<Transaksi> listTransaksi;
}

class CustomerDriver {
    Customer akun;
    Transaksi transaksi;
    ListBarang barang;
}

class Keranjang {
    ArrayList<Barang> barang;
}

class Barang {
}

class ListBarang {
    ArrayList<Barang> barang;
}

class Transaksi {
    Customer akun;
    ArrayList<Barang> barang;
}

class Invoice {
    Transaksi transaksi;
    Pembayaran pembayaran;
}

abstract class Pembayaran {
    String id;

    public abstract void QRIS();

    public abstract void Bank();

    public abstract void COD();
}

public class Main {
    Akun akun;
    Driver driverAkun;

    public void login() {

    }

    public static void main(String[] args) {

    }
}
