/**
 * CustomerDriver
 */
class CustomerDriver {
    public Customer akun;
    public Transaksi transaksi;
    public ListBarang barang;

    public CustomerDriver() {
        akun = new Customer(null, null, null);
        transaksi = new Transaksi();
        barang = new ListBarang();
        
        
    }
}
