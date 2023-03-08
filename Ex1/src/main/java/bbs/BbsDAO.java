package bbs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.DriverManager;

public class BbsDAO {

  private static Connection conn; // 데이터 베이스에 접근하게 해주는 객체
  private static PreparedStatement pstmt; //
  private static ResultSet rs;// 어떠한 정보를 담을수 있는 객체

  public BbsDAO() {
    try {
      String dbURL = "jdbc:mysql://localhost:3306/BBS?serverTimezone=Asia/Seoul";
      String dbID = "hy";
      String dbPassword = "0328";
      Class.forName("com.mysql.cj.jdbc.Driver");
      conn = DriverManager.getConnection(dbURL, dbID, dbPassword);

    } catch (Exception e) {
      System.out.println(e.toString());
    }
  }

  public String getDate() {
    String SQL = "SELECT NOW()";
    try {
      PreparedStatement pstmt = conn.prepareStatement(SQL);
      rs = pstmt.executeQuery();
      if (rs.next()) {
        return rs.getString(1);
      }
    } catch (Exception e) {
      System.out.println(e.toString());
    }
    return "";
  }

  public int getNext() {
    String SQL = "SELECT bbsID FROM BBS ORDER BY bbsID DESC ";
    try {
      PreparedStatement pstmt = conn.prepareStatement(SQL);
      rs = pstmt.executeQuery();
      if (rs.next()) {
        return rs.getInt(1) + 1;
      }
      return 1;
    } catch (Exception e) {
      System.out.println(e.toString());
    }
    return -1;
  }

  public int write(String bbsTitle, String userID, String bbsContent) {
    String SQL = "INSERT INTO BBS VALUES(?, ?, ?, ?, ?, ?)";
    try {
      PreparedStatement pstmt = conn.prepareStatement(SQL);
      pstmt.setInt(1, getNext());
      pstmt.setString(2, bbsTitle);
      pstmt.setString(3, bbsContent);
      pstmt.setString(4, userID);
      pstmt.setString(5, getDate());
      pstmt.setInt(6, 1);
      return pstmt.executeUpdate();
    } catch (Exception e) {
      System.out.println(e.toString());
    }
    return -1;
  }

  public ArrayList<Bbs> getList(int pageNumber) {
    String SQL =
        "SELECT * FROM BBS WHERE bbsID < ?   and bbsAvailable = 1 ORDER bY bbsID DESC LIMIT 10";
    ArrayList<Bbs> list = new ArrayList<Bbs>();
    try {
      PreparedStatement pstmt = conn.prepareStatement(SQL);
      pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        Bbs bbs = new Bbs();
        bbs.setBbsID(rs.getInt(1));
        bbs.setBbsTitle(rs.getString(2));
        bbs.setBbsContent(rs.getString(3));
        bbs.setUserID(rs.getString(4));
        bbs.setBbsDate(rs.getString(5));
        bbs.setBbsAvailable(rs.getInt(6));
        list.add(bbs);
      }
    } catch (Exception e) {
      System.out.println(e.toString());
    }
    return list;
  }

  public boolean nextPage(int pageNumber) {
    String SQL = "SELECT * FROM BBS WHERE bbsID < ? and bbsAvailable = 1";
    try {
      PreparedStatement pstmt = conn.prepareStatement(SQL);
      pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);
      rs = pstmt.executeQuery();
      if (rs.next()) {
        return true;
      }
    } catch (Exception e) {
      System.out.println(e.toString());
    }
    return false;
  }

  public Bbs getBbs(int bbsID) {
    String SQL = "SELECT * FROM BBS WHERE bbsID=?";
    try {
      PreparedStatement pstmt = conn.prepareStatement(SQL);
      pstmt.setInt(1, bbsID);
      rs = pstmt.executeQuery();
      if (rs.next()) {
        Bbs bbs = new Bbs();
        bbs.setBbsID(rs.getInt(1));
        bbs.setBbsTitle(rs.getString(2));
        bbs.setBbsContent(rs.getString(3));
        bbs.setUserID(rs.getString(4));
        bbs.setBbsDate(rs.getString(5));
        bbs.setBbsAvailable(rs.getInt(6));
        return bbs;
      }
    } catch (Exception e) {
      System.out.println(e.toString());
    }
    return null;
  }
    public int update(int bbsID, String bbsTitle, String bbsContent) {
      String SQL = "UPDATE BBS SET bbsTitle =?, bbsContent = ? WHERE bbsID = ?";
      try {
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, bbsTitle);
        pstmt.setString(2, bbsContent);
        pstmt.setInt(3, bbsID);
        return pstmt.executeUpdate();
      } catch (Exception e) {
        System.out.println(e.toString());
      }
      return -1;
    }
    public int delete(int bbsID) {
      String SQL = "UPDATE BBS SET bbsAvailable = 0 WHERE bbsID = ?";
      try {
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setInt(1, bbsID);
        return pstmt.executeUpdate();
      } catch (Exception e) {
        System.out.println(e.toString());
      }
      return -1;
    }
    
}


