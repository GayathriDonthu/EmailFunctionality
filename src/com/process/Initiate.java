package com.process;

public class Initiate {

	public static void main(String[] args) {
		System.out.println("Migration Process Started");
		LoadProperties.loadProperties();

		if (null != args && args.length > 0) {
			if (args.length > 2) {
				String category = args[0];
				String companyId = args[1];
				String records = args[2];
				if (category.trim().equals("EUL")) {
					InitiateEULMigration iem = new InitiateEULMigration(companyId, records);
					iem.init();
				} else if (category.trim().equals("ACCOUNTS")) {
					InitiateAccountMigration iam = new InitiateAccountMigration(companyId, records);
					iam.init();
				}
			} else if (args.length > 1) {
				String category = args[0];
				String companyId = args[1];
				if (category.trim().equals("EUL")) {
					InitiateEULMigration iem = new InitiateEULMigration(companyId, null);
					iem.init();
				} else if (category.trim().equals("ACCOUNTS")) {
					InitiateAccountMigration iam = new InitiateAccountMigration(companyId, null);
					iam.init();
				} else if (category.trim().equals("BGP")) {
					System.out.println(
							"Given Category as BGP. Stopping " + Thread.currentThread().getName() + " method......");
					Thread.currentThread().interrupt();
					if (Thread.currentThread().isInterrupted()) {
						System.out.println(Thread.currentThread().getName() + " method ended");
						BGProcess bgProcess = new BGProcess();
						Thread t = new Thread(bgProcess);
						t.start();
					} else
						System.out.println("Main method still running");

				}
			} else {

			}
		} else {
			System.out.println("no process specified, choose from below options");
			System.out.println("EUL");
			System.out.println("ACCOUNTS");
		}
		System.out.println("Migration Process End");
	}

}
