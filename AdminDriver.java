import java.util.ArrayList;

/**
 * AdminDriver
 */
class AdminDriver {
    public Admin akun;
    public ListBarang listBarang;
    public ArrayList<Transaksi> listTransaksi;
    
    public AdminDriver() {
        akun.addProduk();
        akun.deleteProduk();
        akun.editProduk();
        akun.seeTransaction();
        akun.acceptTransaction();
    }
}