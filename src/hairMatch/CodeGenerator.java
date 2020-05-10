package hairMatch;

public class CodeGenerator {

	public static String getRandomCode(int n) {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++) {
			int type;
			type = (int) ((Math.random() * 7) % 3);

			if (type == 1) // numbers
				sb.append((int) (Math.random() * 10));
			else if (type == 2) // upper case letters
				sb.append((char) (((Math.random() * 26) + 65)));
			else // Lower case letters
				sb.append(((char) ((Math.random() * 26) + 97)));
		}
		return sb.toString();
	}
}
