package dao;

import entity.ChiTietHoaDon;
import entity.Thuoc;
import entity.HoaDon;
import connect.ConnectDB;
import entity.ThangVaDoanhThu;
import entity.ThuocVaLuotBan;
import java.sql.*;
import java.util.ArrayList;
import entity.ThuocvaDoanhThu;
import java.time.LocalDate;
import java.time.YearMonth;

public class ChiTietHoaDon_DAO {
    
    // Phương thức tạo mới một đối tượng ChiTietHoaDon
    public Boolean create(ChiTietHoaDon chiTiet) {
        int n = 0;
        
        try {
            PreparedStatement ps = ConnectDB.conn.prepareStatement("INSERT INTO ChiTietHoaDon VALUES (?,?,?,?)");
            ps.setInt(1, chiTiet.getSoLuong());
            ps.setDouble(2, chiTiet.getDonGia());
            ps.setString(3, chiTiet.getThuoc().getMaThuoc());  // Giả sử Thuoc có phương thức getMaThuoc()
            ps.setString(4, chiTiet.getHoaDon().getMaHD()); // Giả sử HoaDon có phương thức getMaHoaDon()
            n = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    // Phương thức lấy tất cả các đối tượng ChiTietHoaDon
    public ArrayList<ChiTietHoaDon> getAllChiTietHoaDon() {
        ArrayList<ChiTietHoaDon> list = new ArrayList<>();
        
        try {
            ConnectDB.connect();
            String sql = "SELECT * FROM ChiTietHoaDon";
            PreparedStatement ps = ConnectDB.conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                int soLuong = rs.getInt("soLuong");
                double donGia = rs.getDouble("donGia");
                
                String maThuoc = rs.getString("maThuoc");
                String maHoaDon = rs.getString("maHoaDon");
                
                Thuoc thuoc = new Thuoc_DAO().getThuoc(maThuoc);  // Lấy thông tin thuốc
                HoaDon hoaDon = new HoaDon_DAO().getHoaDon(maHoaDon); // Lấy thông tin hóa đơn
                
                ChiTietHoaDon chiTiet = new ChiTietHoaDon(soLuong, donGia, thuoc, hoaDon);
                list.add(chiTiet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Phương thức lấy một đối tượng ChiTietHoaDon theo mã thuốc và mã hóa đơn
    public ChiTietHoaDon getChiTietHoaDon(String maThuoc, String maHoaDon) {
        try {
            PreparedStatement ps = ConnectDB.conn.prepareStatement("SELECT * FROM ChiTietHoaDon WHERE maThuoc = ? AND maHoaDon = ?");
            ps.setString(1, maThuoc);
            ps.setString(2, maHoaDon);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int soLuong = rs.getInt("soLuong");
                double donGia = rs.getDouble("donGia");
                
                Thuoc thuoc = new Thuoc_DAO().getThuoc(maThuoc);
                HoaDon hoaDon = new HoaDon_DAO().getHoaDon(maHoaDon);
                
                return new ChiTietHoaDon(soLuong, donGia, thuoc, hoaDon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Phương thức cập nhật thông tin một đối tượng ChiTietHoaDon
    public boolean suaChiTietHoaDon(String maThuoc, String maHoaDon, ChiTietHoaDon newChiTiet) {
        int n = 0;
        try {
            PreparedStatement ps = ConnectDB.conn.prepareStatement(
                "UPDATE ChiTietHoaDon SET soLuong = ?, donGia = ? WHERE maThuoc = ? AND maHoaDon = ?");
            ps.setInt(1, newChiTiet.getSoLuong());
            ps.setDouble(2, newChiTiet.getDonGia());
            ps.setString(3, maThuoc);
            ps.setString(4, maHoaDon);
            n = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    // Phương thức xóa một đối tượng ChiTietHoaDon
    public boolean deleteChiTietHoaDon(String maThuoc, String maHoaDon) {
        int n = 0;
        try {
            PreparedStatement ps = ConnectDB.conn.prepareStatement("DELETE FROM ChiTietHoaDon WHERE maThuoc = ? AND maHoaDon = ?");
            ps.setString(1, maThuoc);
            ps.setString(2, maHoaDon);
            n = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    // Phương thức lấy số lượng chi tiết hóa đơn
    public int getSize() {
        int n = 0;
        try {
            PreparedStatement ps = ConnectDB.conn.prepareStatement("SELECT COUNT(*) AS total FROM ChiTietHoaDon");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                n = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n;
    }
   public ArrayList<ThuocvaDoanhThu> getTop10ThuocCoDoanhThuCaoNhat() {
    ArrayList<ThuocvaDoanhThu> topThuocList = new ArrayList<>();
    
    try {
        String sql = "SELECT TOP 10 maThuoc, SUM(soLuong * donGia) AS doanhThu " +
                     "FROM ChiTietHoaDon " +
                     "GROUP BY maThuoc " +
                     "ORDER BY doanhThu DESC";
        
        PreparedStatement ps = ConnectDB.conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        
        while (rs.next()) {
            String maThuoc = rs.getString("maThuoc");
            double doanhThu = rs.getDouble("doanhThu");
            
            Thuoc thuoc = new Thuoc_DAO().getThuoc(maThuoc);
            ThuocvaDoanhThu thuocDoanhThu = new ThuocvaDoanhThu(thuoc, doanhThu);
            topThuocList.add(thuocDoanhThu);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    return topThuocList;
}

public double getDoanhThu(String maThuoc) {
    double doanhThu = 0;
    
    try {
        String sql = "SELECT SUM(soLuong * donGia) AS doanhThu " +
                     "FROM ChiTietHoaDon " +
                     "WHERE maThuoc = ? " + // Thêm dấu cộng
                     "GROUP BY maThuoc " +
                     "ORDER BY doanhThu DESC";
                     
        PreparedStatement ps = ConnectDB.conn.prepareStatement(sql);
        ps.setString(1, maThuoc); // Sử dụng setString nếu maThuoc là String
        ResultSet rs = ps.executeQuery(); // Gọi executeQuery() sau khi đặt tham số

        while (rs.next()) {
            
            doanhThu = rs.getDouble("doanhThu");
            
           
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    System.out.println(doanhThu);
    return doanhThu;
}

public ChiTietHoaDon getChiTietHoaDonTheoMa(String maHoaDon) {
        try {
            PreparedStatement ps = ConnectDB.conn.prepareStatement("SELECT * FROM ChiTietHoaDon WHERE maHD = ?");
            
            ps.setString(1, maHoaDon);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int soLuong = rs.getInt("soLuong");
                double donGia = rs.getDouble("donGia");
                
                
                HoaDon hoaDon = new HoaDon_DAO().getHoaDon(maHoaDon);
                
                return new ChiTietHoaDon(hoaDon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
public int getsoLuongBan(String maThuoc) {
    int soLuong = 0;
    
    try {
        String sql = "SELECT  sum(soLuong) as soLuong FROM ChiTietHoaDon WHERE maThuoc = ? ";
                     
        PreparedStatement ps = ConnectDB.conn.prepareStatement(sql);
        ps.setString(1, maThuoc); // Sử dụng setString nếu maThuoc là String
        ResultSet rs = ps.executeQuery(); // Gọi executeQuery() sau khi đặt tham số

        while (rs.next()) {
            
            soLuong = rs.getInt("soLuong");
            
           
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    System.out.println(soLuong);
    return soLuong;
}
    public ArrayList<ChiTietHoaDon> getTop10ThuocCoLuotBan(int month, int year){
        ArrayList<HoaDon> arrHoaDon= new ArrayList<HoaDon>();
        HoaDon_DAO hoaDon_DAO= new HoaDon_DAO();
        arrHoaDon= hoaDon_DAO.getHoaDonTrongThang(month, year);
        ArrayList<ChiTietHoaDon> arrCTHD= new ArrayList<ChiTietHoaDon>();
        for (HoaDon hoaDon : arrHoaDon) {
            arrCTHD.add(getChiTietHoaDonTheoMa(hoaDon.getMaHD()));
        }
        
        
        
        
        return arrCTHD;
}
public ArrayList<ThuocVaLuotBan> getThuocCoLuotBanCaoNhatTrongThang(int thang, int nam) {
    ArrayList<ThuocVaLuotBan> topThuocList = new ArrayList<>();
    
    try {
        // Tính toán ngày đầu và ngày cuối của tháng
        YearMonth yearMonth = YearMonth.of(nam, thang);
        LocalDate firstDayOfMonth = yearMonth.atDay(1);
        LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();

        // Lấy danh sách số lượng bán của từng loại thuốc trong tháng cụ thể
        String sql = "SELECT c.maThuoc, SUM(c.soLuong) AS soLuong " +
                     "FROM HoaDon h " +
                     "JOIN ChiTietHoaDon c ON h.maHD = c.maHD " +
                     "WHERE h.ngayLap BETWEEN ? AND ? " + 
                     "GROUP BY c.maThuoc " + 
                     "ORDER BY soLuong DESC";  // Sắp xếp theo số lượng bán giảm dần
        
        PreparedStatement ps = ConnectDB.conn.prepareStatement(sql);
        ps.setDate(1, java.sql.Date.valueOf(firstDayOfMonth)); // Ngày đầu tháng
        ps.setDate(2, java.sql.Date.valueOf(lastDayOfMonth));   // Ngày cuối tháng
        
        ResultSet rs = ps.executeQuery();
        
        while (rs.next()) {
            String maThuoc = rs.getString("maThuoc");
            int luotBan = rs.getInt("soLuong");
            
            // Lấy thông tin thuốc
            Thuoc thuoc = new Thuoc_DAO().getThuoc(maThuoc);
            ThuocVaLuotBan thuocLuotBan = new ThuocVaLuotBan(thuoc, luotBan);
            topThuocList.add(thuocLuotBan);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    return topThuocList;
}

public ArrayList<ThuocvaDoanhThu> getThuocCoDoanhThuCaoNhatTrongThang(int thang, int nam) {
    ArrayList<ThuocvaDoanhThu> topThuocList = new ArrayList<>();

    try {
        // Tính toán ngày đầu và ngày cuối của tháng
        YearMonth yearMonth = YearMonth.of(nam, thang);
        LocalDate firstDayOfMonth = yearMonth.atDay(1);
        LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();

        // Lấy danh sách doanh thu của từng loại thuốc trong tháng cụ thể
        String sql = "SELECT c.maThuoc, SUM(c.soLuong * c.donGia) AS doanhThu " +
                     "FROM HoaDon h " +
                     "JOIN ChiTietHoaDon c ON h.maHD = c.maHD " +
                     "WHERE h.ngayLap BETWEEN ? AND ? " + 
                     "GROUP BY c.maThuoc " + 
                     "ORDER BY doanhThu DESC";  // Sắp xếp theo doanh thu giảm dần

        PreparedStatement ps = ConnectDB.conn.prepareStatement(sql);
        ps.setDate(1, java.sql.Date.valueOf(firstDayOfMonth)); // Ngày đầu tháng
        ps.setDate(2, java.sql.Date.valueOf(lastDayOfMonth));   // Ngày cuối tháng

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String maThuoc = rs.getString("maThuoc");
            double doanhThu = rs.getDouble("doanhThu");

            // Lấy thông tin thuốc
            Thuoc thuoc = new Thuoc_DAO().getThuoc(maThuoc);
            ThuocvaDoanhThu thuocDoanhThu = new ThuocvaDoanhThu(thuoc, doanhThu);
            topThuocList.add(thuocDoanhThu);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return topThuocList;
}




    public static void main(String[] args) throws SQLException {
        ConnectDB.connect();
        System.out.println(new ChiTietHoaDon_DAO());
    }
}
