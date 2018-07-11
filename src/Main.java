import org.json.JSONException;

public class Main {

	public static void main(String[] args) throws Exception {
		String threadID = args[0];
		String file_directory = args[1];
		Email test = new Email(threadID, file_directory);
		System.out.println(test.getContent());
//		System.out.println(test.getID());
//		System.out.println(test.getLabels());
//		System.out.println(test.getSubject());
//		System.out.println(test.getFromName());
//		System.out.println(test.getFromEmail());
//		System.out.println(test.getHeaders());
	}
}
