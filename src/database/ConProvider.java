package database;
import java.sql.*;
public class ConProvider {

	public static Connection getConnection(){
	Connection con=null;
	try{
		Class.forName("oracle.jdbc.driver.OracleDriver");
		con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","neetu");
	}catch(Exception e){System.out.println(e);}
	return con;
    }
}
