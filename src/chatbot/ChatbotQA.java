package chatbot;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ChatbotQA {
	int id;
	int priority = 0;
	String question, answer, answerWeb;
	String func = "question";

	public void setID(int id) {
		this.id = id;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public void setAnswerWeb(String answerWeb) {
		this.answerWeb = answerWeb;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public int getID() {
		return id;
	}

	public int getPriority() {
		return priority;
	}

	public String getAnswer() {
		return answer;
	}

	public String getAnswerWeb() {
		return answerWeb;
	}

	public String getQuestion() {
		return question;
	}

	public static String convertToJson(ArrayList<ChatbotQA> Item) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jsonStr = gson.toJson(Item);
		return jsonStr;
	}
}
