/**
 * Invoice
 */
public class Invoice {
    public Transaksi transaksi;
    public Pembayaran pembayaran;

    abstract class Pembayaran {
        public String id;
    
        public Pembayaran(String id) {
            this.id = id;
        }
    
    }

    class QRIS extends Pembayaran {

        public QRIS(String id) {
            super(id);
    
        }
    
    }
    
    class Bank extends Pembayaran {
    
        public Bank(String id) {
            super(id);
        }
    
    }
    
    class COD extends Pembayaran {
    
        public COD(String id) {
            super(id);
        }
    
    }

}
