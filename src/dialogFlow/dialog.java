package dialogFlow;

import java.util.Scanner;

import JDBC.mySql;

public class dialog {

	public static void main(String[] args) {
		mySql test = new mySql();
		Scanner scanner = new Scanner(System.in);
		String question = null;
		System.out.print("yes");
		question = scanner.nextLine();

		while (!question.equals("end")) {
			// System.out.println(question);
			test.SelectTable(question);
			System.out.print("yes");
			question = scanner.nextLine();
		}
	}

}
