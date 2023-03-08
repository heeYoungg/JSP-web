
package user;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;

public class UserDAO {

  private static Connection conn; // 데이터 베이스에 접근하게 해주는 객체
  private static PreparedStatement pstmt; //
  private static ResultSet rs;// 어떠한 정보를 담을수 있는 객체


  public UserDAO() {
    try {
        String dbURL = "jdbc:mysql://localhost:3306/BBS?serverTimezone=Asia/Seoul";
        String dbID = "hy";
        String dbPassword = "0328";
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
        
    }
    catch (Exception e) {
      System.out.println(e.toString());
    }
  }

  public int login(String userID, String userPassword) {
    String SQL = "SELECT userPassword FROM USER WHERE userID = ? ";
    try {
      pstmt = conn.prepareStatement(SQL);
      pstmt.setString(1, userID);
      rs = pstmt.executeQuery();
      if (rs.next()) {
        if (rs.getString(1).equals(userPassword))
          return 1; //로그인 성공 메인jsp 로 이동
        else 
          return 0; // 비밀번호 오류
      }
      return -1; // 아이디오류
    } catch (Exception e) {
      System.out.println(e.toString());
    }
    return -2; // db오류
  }
  
  public int join(User user) {
    String SQL = "INSERT INTO USER VALUES(?, ?, ?, ?, ?)";
    try {
       pstmt = conn.prepareStatement(SQL);
       pstmt.setString(1, user.getUserID());
       pstmt.setString(2, user.getUserPassword());
       pstmt.setString(3, user.getUserName());
       pstmt.setString(4, user.getUserGender());
       pstmt.setString(5, user.getUserEmail());
       return pstmt.executeUpdate();
    }catch(Exception e) {
      System.out.println(e.toString());
    }
    return -1; //db오류
  }
}
