import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.JSONArray;

public class WordRevision {
    public static void main(String[] args) {
        new welcome();
    }
}

abstract class GUIStage{
    JFrame window;
    GUIStage (String name, Dimension size){
        window = new JFrame();
        window.setTitle(name);
        window.setSize(size);
        setWindow();
        window.setVisible(true);
    }
    abstract void setWindow();

    void addComponents(Component... components){
        for (Component comp : components){
            window.add(comp);
        }
    }
}

class welcome extends GUIStage{
    welcome() {
        super("Welcome to Revision.", new Dimension(600,600));
    }

    @Override
    void setWindow() {
        JLabel text = new JLabel("Welcome to the Revision Program.");
        JButton play = new JButton("Start a new revision session");
        play.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                window.dispose();
                new play("lol");
            }
        });
        JButton credits = new JButton("Go to the credits");
        credits.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                window.dispose();
                new credits();
            }
        });
        addComponents(text,play,credits);
        window.setLayout(new GridLayout(3,3));
    }
}

class credits extends GUIStage{
    credits(){
        super("Credits", new Dimension(300,300));
    }

    @Override
    void setWindow() {
        JLabel text = new JLabel("Made by Oliver Killane when he wanted to do german revision but was too bored to, you know, revise german.");

        JButton welret = new JButton("return to welcome screen");
        welret.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                window.dispose();
                new welcome();
            }
        });

        addComponents(text, welret);
        window.setLayout(new GridLayout(3,3));
    }
}

class play extends GUIStage implements ActionListener{
    ArrayList<Question> questions;
    JLabel prompt;
    JLabel question;
    JTextField answer;
    JButton welret;


    play(String path){
        super("Play", new Dimension(1500,800));
        //getjson(path);
    }
    @Override
    void setWindow() {
        prompt = new JLabel("");
        question = new JLabel("");
        answer = new JTextField();
        welret = new JButton("Return to Welcome");

        answer.addActionListener(this);
        welret.addActionListener(this);

        window.setLayout(new GridLayout(4,1));
        window.add(prompt);
        window.add(question);
        window.add(answer);
        window.add(welret);

        window.setVisible(true);
    }

    private void getjson(String path){
        try{
            JSONArray file = (JSONArray) JSONValue.parse(Files.readString(FileSystems.getDefault().getPath(path)));
            String prompt = (String)((JSONObject)file.get(0)).get("prompt");
            questions.add(new Question(prompt, "hey", new ArrayList<String>(){{add("lol");}}));
        }
        catch (IOException e){
            JOptionPane.showMessageDialog(window,
                    "Cannot get file",
                    "Error, file unopenable.",
                    JOptionPane.ERROR_MESSAGE);
            window.dispose();
            new welcome();
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (welret.equals(actionEvent.getSource())) {
            window.dispose();
            new welcome();
        }
    }
}

final class Question{
    String prompt;
    String question;
    ArrayList<String> answers;

    Question(String p, String q, ArrayList<String> ans){
        prompt = p;
        question = q;
        answers = ans;
    }
}