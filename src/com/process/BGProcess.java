package com.process;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLRecoverableException;
import java.sql.Statement;
import java.util.Date;

public class BGProcess implements Runnable {

	@Override
	public void run() {

		System.out.println("Starting BGP thread.....");

		Connection con = null;
		Statement stmt1 = null;
		Statement stmt2 = null;
		Statement stmt3 = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;

		System.out.println("Connecting to database...");

		try {
			
			Class.forName("oracle.jdbc.xa.client.OracleXADataSource");
			String url  = "jdbc:oracle:thin:@(DESCRIPTION="+
						"(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)"+ 
						"(HOST="+"172.24.80.215"+")(PORT="+"1680"+")))"+
						"(CONNECT_DATA=(SID="+"ORMSM6T6"+")(SERVER=DEDICATED)))";
			con = DriverManager.getConnection(url,"ASAP","kh1$w1ft");

			stmt1 = con.createStatement();
			stmt2 = con.createStatement();
			stmt3 = con.createStatement();
			stmt3.executeUpdate("UPDATE JOB_QUEUE "
					+ "SET job_queue_status = '"+ LoadProperties.STP_STATUS + "' WHERE job_queue_status = '"+ LoadProperties.REA_STATUS + "'");
			while (true) {

				System.out.println("\nReading data from database - " + new Date());

				rs1 = stmt1.executeQuery(
						"select count(job_queue_status) rowCount from JOB_QUEUE where job_queue_status = '"+ LoadProperties.STP_STATUS + "'");

				if (null != rs1) {
					while (rs1.next()) {

						int rowCount = rs1.getInt("rowCount");
						System.out.println("STP status records: " + rowCount);

						if (rowCount > 0) {

							rs2 = stmt2.executeQuery(
									"SELECT count(job_queue_status) As rowCount FROM JOB_QUEUE where job_queue_status = '"+ LoadProperties.REA_STATUS + "'");

							if (null != rs2) {
								while (rs2.next()) {
									int count = rs2.getInt("rowCount");

									if (count < LoadProperties.PERCENT) {
										System.out.println("REA status records: "+ count + ". Count is less than 20%.. ");
										System.out.println("Updating the status from STP to REA");

										stmt3.executeUpdate("UPDATE JOB_QUEUE "
												+ "SET job_queue_status = '"+ LoadProperties.REA_STATUS + "' WHERE job_queue_status = '"+ LoadProperties.STP_STATUS + "' and rownum <="
												+ LoadProperties.RECORD_LIMIT);
										
										if(rowCount < LoadProperties.RECORD_LIMIT)
											System.out.println(rowCount + " records updated");
										else
											System.out.println(LoadProperties.RECORD_LIMIT + " records updated");
										

									}

								}
							}
						} else {
							System.out.println("\nNo STP records");
							Thread.currentThread().interrupt();
							System.out.println("BGProcess stopped");
						}
					}

				} else {
					System.out.println("\nNo STP records");
					Thread.currentThread().interrupt();
					System.out.println("BGProcess stopped");
				}
				
				if(Thread.currentThread().isInterrupted())
					return;
				else{
					System.out.println("\nWaiting for 2 mins.............");
					Thread.sleep(1000);
				}

			}
		} catch (SQLRecoverableException se) {
			System.out.println("Coundn't establish database connection");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				if (null != rs1 && null != rs2) {
					rs1.close();
					rs2.close();
				}
				if (null != con) {
					con.close();
				}
				if (null != stmt1 && null != stmt2 && null != stmt3) {
					stmt1.close();
					stmt2.close();
					stmt3.close();
				}
			} catch (Exception e) {
			}

		}
	}

}
