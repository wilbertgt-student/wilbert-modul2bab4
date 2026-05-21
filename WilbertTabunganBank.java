import java.util.Scanner;

class Pelanggan {
    private String nomorPelanggan;
    private String nama;
    private double saldo;
    private String pin;
    private boolean terblokir;
    private int percobaan;

    private static final double saldoMinimum = 10000;

    public Pelanggan(String nomorPelanggan, String nama, double saldo, String pin) {
        this.nomorPelanggan = nomorPelanggan;
        this.nama = nama;
        this.saldo = saldo;
        this.pin = pin;
        this.terblokir = false;
        this.percobaan = 0;
    }

    public String getNomorPelanggan() {
        return nomorPelanggan;
    }

    public String getNama() {
        return nama;
    }

    public double getSaldo() {
        return saldo;
    }

    public boolean isTerblokir() {
        return terblokir;
    }

    public String getJenisRekening() {
        String kode = nomorPelanggan.substring(0, 2);

        if (kode.equals("38")) {
            return "Silver";
        } else if (kode.equals("56")) {
            return "Gold";
        } else if (kode.equals("74")) {
            return "Platinum";
        } else {
            return "Tidak ada";
        }
    }

    public boolean autentikasi(String inputPin) {
        if (terblokir) {
            System.out.println("Akun anda telah diblokir selamanya");
            return false;
        }

        if (inputPin.equals(pin)) {
            percobaan = 0;
            return true;
        } else {
            percobaan++;

            if (percobaan >= 3) {
                terblokir = true;
                System.out.println("PIN salah 3 kali. Akun diblokir");
            } else {
                System.out.println("PIN salah! Sisa percobaan anda: " + (3 - percobaan));
            }

            return false;
        }
    }

    public void beli(double jumlah) {

        if (terblokir) {
            System.out.println("Akun diblokir dan transaksi gagal");
            return;
        }

        double cashback = 0;
        String jenis = getJenisRekening();

        if (jenis.equals("Silver")) {

            if (jumlah > 1000000) {
                cashback = jumlah * 0.05;
            }

        } else if (jenis.equals("Gold")) {

            if (jumlah > 1000000) {
                cashback = jumlah * 0.07;
            } else {
                cashback = jumlah * 0.02;
            }

        } else if (jenis.equals("Platinum")) {

            if (jumlah > 1000000) {
                cashback = jumlah * 0.10;
            } else {
                cashback = jumlah * 0.05;
            }
        }

        double saldoAkhir = saldo - jumlah + cashback;

        if (saldoAkhir < saldoMinimum) {

            System.out.println("Transaksi gagal");
            System.out.println("Saldo tidak mencukupi saldo minimum");

        } else {

            saldo = saldoAkhir;

            System.out.println("Pembelian berhasil!");
            System.out.println("Jenis rekening : " + jenis);
            System.out.printf("Total belanja  : Rp%.0f%n", jumlah);
            System.out.printf("Cashback       : Rp%.0f%n", cashback);
            System.out.printf("Saldo akhir    : Rp%.0f%n", saldo);
        }
    }

    public void topUp(double jumlah) {

        if (terblokir) {
            System.out.println("Akun diblokir");
            return;
        }

        if (jumlah <= 0) {
            System.out.println("Jumlah top up harus lebih dari 0");
            return;
        }

        saldo += jumlah;

        System.out.println("Top up berhasil");
        System.out.printf("Saldo sekarang : Rp%.0f%n", saldo);
    }

    public void infoPelanggan() {

        System.out.println("\n===== INFO PELANGGAN =====");
        System.out.println("Nomor Pelanggan : " + nomorPelanggan);
        System.out.println("Nama            : " + nama);
        System.out.println("Jenis Rekening  : " + getJenisRekening());
        System.out.printf("Saldo           : Rp%.0f%n", saldo);

        if (terblokir) {
            System.out.println("Status          : Terblokir");
        } else {
            System.out.println("Status          : Aktif");
        }

        System.out.println("==========================");
    }
}

public class WilbertTabunganBank {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Pelanggan[] daftarPelanggan = {
                new Pelanggan("3867421890", "PelangganSilver", 0, "3267"),
                new Pelanggan("5642032678", "PelangganGold", 0, "2189"),
                new Pelanggan("7441320671", "PelangganPlatinum", 0, "1100")
        };

        System.out.println("=== Selamat datang di Swalayan Tiny ===");

        System.out.print("Masukkan nomor pelanggan: ");
        String nomorInput = sc.nextLine();

        Pelanggan pelanggan = null;

        for (int i = 0; i < daftarPelanggan.length; i++) {

            if (daftarPelanggan[i].getNomorPelanggan().equals(nomorInput)) {
                pelanggan = daftarPelanggan[i];
                break;
            }
        }

        if (pelanggan == null) {
            System.out.println("Nomor pelanggan tidak ditemukan");
            sc.close();
            return;
        }

        boolean loginBerhasil = false;

        for (int i = 0; i < 3; i++) {

            System.out.print("Masukkan PIN: ");
            String pinInput = sc.nextLine();

            if (pelanggan.autentikasi(pinInput)) {
                loginBerhasil = true;
                break;
            }

            if (pelanggan.isTerblokir()) {
                break;
            }
        }

        if (!loginBerhasil) {
            System.out.println("Autentikasi gagal");
            sc.close();
            return;
        }

        int pilihan;

        do {

            System.out.println("\n===== MENU =====");
            System.out.println("1. Pembelian");
            System.out.println("2. Top Up");
            System.out.println("3. Info Pelanggan");
            System.out.println("4. Keluar");
            System.out.print("Pilih menu: ");

            pilihan = Integer.parseInt(sc.nextLine());

            switch (pilihan) {

                case 1:

                    System.out.print("Masukkan jumlah pembelian: Rp");
                    double beli = Double.parseDouble(sc.nextLine());

                    pelanggan.beli(beli);

                    break;

                case 2:

                    System.out.print("Masukkan jumlah top up: Rp");
                    double topup = Double.parseDouble(sc.nextLine());

                    pelanggan.topUp(topup);

                    break;

                case 3:

                    pelanggan.infoPelanggan();

                    break;

                case 4:

                    System.out.println("Terima kasih telah menggunakan program");
                    break;

                default:

                    System.out.println("Menu tidak ada");
            }

        } while (pilihan != 4);

        sc.close();
    }
}