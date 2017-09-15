import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StageDatabase {

	static final String JDBC_DRIVER = "oracle.jdbc.xa.client.OracleXADataSource";
	static final String DB_URL = "jdbc:oracle:thin:@(DESCRIPTION="+
						"(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)"+ 
						"(HOST="+"tgnet1.fptest.fairpointtest.com"+")(PORT="+"1614"+")))"+
						"(CONNECT_DATA=(SID="+"ortgnet1"+")(SERVER=DEDICATED)))";

	static final String USER = "STG";
	static final String PASS = "W3lC0me_2016";
	static Connection conn = null;

	public static int getCount(char flag, int company) throws SQLException {

		Statement stmt = null;
		int count = 0;
		try {
			// STEP 1: Register JDBC driver
			Class.forName(JDBC_DRIVER);

			// STEP 2: Open a connection
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// STEP 3: Execute a query

			stmt = conn.createStatement();
			String sql = "SELECT count(*) as count FROM stg_met_ld_cust_record where processed_customer_ind = '" + flag
					+ "' and company_id = '" + company + "'";
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				count = rs.getInt("count");
			}

			stmt.close();
			conn.close();
		} catch (

		Exception e) {
			e.printStackTrace();
		} finally {
		}

		return count;
	}
}
