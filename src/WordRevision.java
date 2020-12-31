import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.ArrayList;

public class WordRevision {
    public static void main(String[] args) {
        new welcomeScreen(new JFrame());
    }
}

abstract class GUIStage implements ActionListener {
    JFrame window;

    GUIStage (JFrame win, Dimension size, String title){
        window = win;
        window.setTitle(title);
        window.setSize(size);
        initialise();
        update();

    }

    abstract void initialise();

    void update(){
        window.revalidate();
        window.repaint();
        window.setVisible(true);
    }
}

class welcomeScreen extends GUIStage{
    JButton play;
    JButton credits;
    JLabel text;
    JPanel buttons;


    welcomeScreen(JFrame win) {
        super(win, new Dimension(300,300), "Welcome");
    }

    @Override
    void initialise() {
        buttons = new JPanel();

        play = new JButton("Start revising");
        credits = new JButton("credits");
        text = new JLabel("welcome to German revision");

        play.addActionListener(this);
        credits.addActionListener(this);

        buttons.setLayout(new GridLayout(1,2));
        buttons.add(play, 0);
        buttons.add(credits, 1);

        window.setLayout(new BorderLayout());
        window.add(text, BorderLayout.NORTH);
        window.add(buttons, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (play.equals(actionEvent.getSource())){
            window.removeAll();
            new playScreen(window);
        }
        else if (credits.equals(actionEvent.getSource())){
            window.removeAll();
            new creditsScreen(window);
        }

    }
}

class creditsScreen extends GUIStage{
    JLabel text;
    JButton welcome;

    creditsScreen(JFrame win) {
        super(win, new Dimension(600,600), "Credits");
    }

    @Override
    void initialise() {
        text = new JLabel("Made by oliver killane hastily while learning java to distract from german");
        welcome = new JButton("To Welcome screen.");

        welcome.addActionListener(this);

        window.setLayout(new BorderLayout());
        window.add(text, BorderLayout.NORTH);
        window.add(welcome, BorderLayout.SOUTH);

        window.setVisible(true);
        window.revalidate();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (welcome.equals(actionEvent.getSource())){
            new welcomeScreen(window);
        }
    }
}

class playScreen extends GUIStage{
    JLabel prompt;
    JLabel question;
    JPanel input;
    JTextField entry;
    JButton reveal;
    JButton check;
    JButton welcome;


    playScreen(JFrame win) {
        super(win, new Dimension(1200,600), "Play");
    }

    @Override
    void initialise() {
        prompt = new JLabel("this is the prompt");
        question = new JLabel("this is the question");

        reveal = new JButton("reveal");
        check = new JButton("check");
        welcome = new JButton("welcome");

        entry = new JTextField();

        reveal.addActionListener(this);
        check.addActionListener(this);
        welcome.addActionListener(this);

        input = new JPanel();
        input.setLayout(new GridLayout(1, 3));

        input.add(entry);
        input.add(check);
        input.add(reveal);

        window.setLayout(new BorderLayout());
        window.add(prompt, BorderLayout.NORTH);
        window.add(question, BorderLayout.CENTER);
        window.add(input, BorderLayout.SOUTH);

        window.setVisible(true);
        window.revalidate();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (welcome.equals(actionEvent.getSource())){
            new welcomeScreen(window);
        }
    }
}

class QuestionSet{
    String prompt;
    HashMap<String, ArrayList<String>> pairs;
}