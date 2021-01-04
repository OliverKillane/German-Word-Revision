import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

// Basic program, takes lists of questions and a prompt from a json file, uses a java swing UI to display them.
// Transition between stages done by keeping a single JFrame (window) that is passed between objects which run each
// stage of the GUI.

// Gson used to turn JSON into objects because its easier than going with JSON.simple.

public class WordRevision {
    public static void main(String[] args) {
        new welcomeScreen(new JFrame());
    }
}

// GUIStage runs main drawing functionality for each stage.
abstract class GUIStage implements ActionListener {
    JFrame window;

    GUIStage(JFrame win, Dimension size, String title) {
        window = win;
        window.setTitle(title);
        window.setSize(size);
        initialise();
        update();
    }

    abstract void initialise();

    void update() {
        window.revalidate();
        window.repaint();
        window.setVisible(true);
    }

    abstract void cleanscreen();

    void clean(Component... comps) {
        for (Component comp : comps) {
            window.remove(comp);
        }
    }
}

class welcomeScreen extends GUIStage {
    JButton play;
    JButton credits;
    JLabel text;
    JPanel buttons;

    welcomeScreen(JFrame win) {
        super(win, new Dimension(600, 600), "Welcome");
    }

    @Override
    void initialise() {
        buttons = new JPanel();

        play = new JButton("Start revising");
        credits = new JButton("credits");
        text = new JLabel("<html><font size=+8>welcome to german revision</font>", JLabel.CENTER);

        play.addActionListener(this);
        credits.addActionListener(this);

        buttons.setLayout(new GridLayout(1, 2));
        buttons.add(play, 0);
        buttons.add(credits, 1);

        window.setLayout(new BorderLayout());
        window.add(text, BorderLayout.NORTH);
        window.add(buttons, BorderLayout.SOUTH);
    }

    @Override
    void cleanscreen() {
        clean(play, credits, text, buttons);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (play.equals(actionEvent.getSource())) {
            cleanscreen();
            new fileSelectScreen(window);
        } else if (credits.equals(actionEvent.getSource())) {
            cleanscreen();
            new creditsScreen(window);
        }
    }
}

class fileSelectScreen extends GUIStage {
    JLabel text;
    JButton infinitives;
    JButton verbs;
    JButton vocab1;
    JButton choosefile;
    JButton welcome;

    fileSelectScreen(JFrame win) {
        super(win, new Dimension(600, 600), "Select File");
    }

    @Override
    void initialise() {
        text = new JLabel("Shortcuts");
        infinitives = new JButton("Infinitive verbs.");
        verbs = new JButton("Verb Conjugations");
        vocab1 = new JButton("Vocab list 1");
        choosefile = new JButton("Choose file");
        welcome = new JButton("Welcome screen");

        infinitives.addActionListener(this);
        verbs.addActionListener(this);
        vocab1.addActionListener(this);
        choosefile.addActionListener(this);
        welcome.addActionListener(this);

        window.setLayout(new GridLayout(6, 1));
        window.add(text);
        window.add(infinitives);
        window.add(verbs);
        window.add(vocab1);
        window.add(choosefile);
        window.add(welcome);
    }

    @Override
    void cleanscreen() {
        clean(text, infinitives, verbs, vocab1, choosefile, welcome);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (welcome.equals(actionEvent.getSource())) {
            cleanscreen();
            new welcomeScreen(window);
        } else if (infinitives.equals(actionEvent.getSource())) {
            //infinitives relative path
        } else if (verbs.equals(actionEvent.getSource())) {
            //verbs relative path
        } else if (vocab1.equals(actionEvent.getSource())) {
            //vocab1 relative path
        } else if (choosefile.equals(actionEvent.getSource())) {
            try {
                QuestionSet qset = getQSet(getFile());
                cleanscreen();
                System.out.println("hey");
                new playScreen(window, qset);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    QuestionSet getQSet(String path) throws IOException {
        String source = Files.readString(Paths.get(path));
        Gson gson = new Gson();
        return gson.fromJson(source, QuestionSet.class);
    }

    String getFile() throws Exception {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(window);
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        } else {
            // make this better, derive a new exception class you fool
            throw new Exception("No file selected");
        }
    }
}

class creditsScreen extends GUIStage {
    JLabel text;
    JButton welcome;

    creditsScreen(JFrame win) {
        super(win, new Dimension(600, 600), "Credits");
    }

    @Override
    void initialise() {
        text = new JLabel("Made by oliver killane hastily while learning java to distract from german, also by the master of java Jordan Hall who blessed this code with his debugging genius.");
        welcome = new JButton("To Welcome screen.");

        welcome.addActionListener(this);

        window.setLayout(new BorderLayout());
        window.add(text, BorderLayout.NORTH);
        window.add(welcome, BorderLayout.SOUTH);
    }

    @Override
    void cleanscreen() {
        clean(text, welcome);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (welcome.equals(actionEvent.getSource())) {
            cleanscreen();
            new welcomeScreen(window);
        }
    }
}

class playScreen extends GUIStage {
    JLabel prompt;
    JLabel question;
    JPanel input;
    JPanel buttons;
    JTextField entry;
    JButton reveal;
    JButton check;
    JButton welcome;

    QuestionSet qset;
    Question current;
    int qsize;

    Random randgen;


    playScreen(JFrame win, QuestionSet qs) {
        super(win, new Dimension(1200, 600), "Play");
        // Cannot reach here
        qset = qs;
        qsize = qs.questions.size();
        randgen = new Random();

        prompt.setText(qset.prompt);

        updateQuestion();
    }

    @Override
    void initialise() {
        prompt = new JLabel("",JLabel.CENTER);
        question = new JLabel("",JLabel.CENTER);

        reveal = new JButton("reveal");
        check = new JButton("check");
        welcome = new JButton("welcome");

        entry = new JTextField();
        entry.addActionListener(this);

        reveal.addActionListener(this);
        check.addActionListener(this);
        welcome.addActionListener(this);

        input = new JPanel();
        input.setLayout(new GridLayout(1, 2));

        buttons = new JPanel();
        buttons.setLayout(new GridLayout(3,1));

        buttons.add(check);
        buttons.add(reveal);
        buttons.add(welcome);

        input.add(entry);
        input.add(buttons);

        window.setLayout(new BorderLayout());
        window.add(prompt, BorderLayout.NORTH);
        window.add(question, BorderLayout.CENTER);
        window.add(input, BorderLayout.SOUTH);

        window.setVisible(true);
        window.revalidate();
    }

    @Override
    void cleanscreen() {
        clean(prompt, question, input, entry, reveal, check, welcome);
    }


    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (welcome.equals(actionEvent.getSource())) {

            Object[] options = {"Yes, please",
                                "No way!"};
            int selected = JOptionPane.showOptionDialog(window,
                    "Are you sure you want to quit?",
                    "Exit revision session",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,     //do not use a custom Icon
                    options,  //the titles of buttons
                    options[0]); //default button title
            if (selected == 0){
                cleanscreen();
                new welcomeScreen(window);
            }
        }

        if (reveal.equals(actionEvent.getSource())) {
            revealQuestion();
        }

        if (check.equals(actionEvent.getSource())) {
            checkQuestion();
        }
        else if (entry.equals(actionEvent.getSource())){
            checkQuestion();
        }
    }

    void updateQuestion() {
        current = qset.questions.get(randgen.nextInt(qsize));

        question.setText("<html><font size=+8>"+ current.question +"</font>");
        entry.setText("");

        update();
    }

    void checkQuestion() {
        if (current.answers.contains(entry.getText().toLowerCase().trim())) {
            updateQuestion();
        } else {
            JOptionPane.showMessageDialog(window, "Incorrect");
        }
    }

    void revealQuestion() {
        entry.setText(current.answers.get(0));
    }
}

class Question {
    String question;
    ArrayList<String> answers;
}

class QuestionSet {
    String prompt;
    ArrayList<Question> questions;
}