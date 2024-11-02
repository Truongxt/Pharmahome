package entity;


import java.time.LocalDate;

public class HoaDon {

	private String maHD;
	private LocalDate ngayLap;
	private double tongTien;
        private Voucher voucher;
        private KhachHang khachHang;
        private NhanVien nhanVien;
        

      

	public void tinhThanhTien() {
		throw new UnsupportedOperationException();
	}

	public void tinhTienThua() {
		throw new UnsupportedOperationException();
	}

	public void tinhThue() {
		throw new UnsupportedOperationException();
	}

	public void TinhTongTienTra() {
		throw new UnsupportedOperationException();
	}

    public HoaDon(String maHD, LocalDate ngayLap, double tongTien, Voucher voucher, KhachHang khachHang, NhanVien nhanVien) {
        this.maHD = maHD;
        this.ngayLap = ngayLap;
        this.tongTien = tongTien;
        this.voucher = voucher;
        this.khachHang = khachHang;
        this.nhanVien = nhanVien;
    }

    public HoaDon(String maHD) {
        this.maHD = maHD;
    }

    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public LocalDate getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(LocalDate ngayLap) {
        this.ngayLap = ngayLap;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public Voucher getVoucher() {
        return voucher;
    }

    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

}