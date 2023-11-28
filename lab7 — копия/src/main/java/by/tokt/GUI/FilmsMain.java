package by.tokt.GUI;

import by.tokt.dao.DAO;
import by.tokt.dao.Exceptions;
import by.tokt.dao.impl.Dao;
import by.tokt.entity.Film;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;

public class FilmsMain extends JFrame {

    private JTextField titleField;
    private JTextField releaseDateField;
    private JTextField directorField;
    private JTextArea filmListArea;
    private DAO filmDao = new Dao();

    public FilmsMain() {
        super("Киношка");

        titleField = new JTextField(20);
        releaseDateField = new JTextField(20);
        directorField = new JTextField(20);

        JButton addButton = createButton("Add", e -> addFilm());
        JButton sortTitleButton = createButton("Sort by name", e -> sortFilmsByTitle());
        JButton sortDirectorButton = createButton("Sort by director", e -> sortFilmsByDirector());

        filmListArea = new JTextArea(10, 30);
        filmListArea.setEditable(false);
        filmListArea.setForeground(Color.BLACK); // Устанавливаем черный цвет текста
        filmListArea.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(sortTitleButton);
        inputPanel.add(sortDirectorButton);
        inputPanel.add(new JLabel("Films title:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("At the cinema:"));
        inputPanel.add(releaseDateField);
        inputPanel.add(new JLabel("Director:"));
        inputPanel.add(directorField);
        inputPanel.add(addButton);

        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.add(new JLabel("Films List:"), BorderLayout.NORTH);
        listPanel.add(new JScrollPane(filmListArea), BorderLayout.CENTER);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(listPanel, BorderLayout.CENTER);

        add(mainPanel);

        loadFilmList();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        button.setBackground(new Color(50, 205, 50)); // Зеленый цвет
        button.setFont(new Font("Arial", Font.BOLD, 14));
        return button;
    }

    private void addFilm() {
        String title = titleField.getText().trim();
        String releaseDate = releaseDateField.getText().trim();
        String director = directorField.getText().trim();

        if (title.isEmpty() || releaseDate.isEmpty() || director.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please, put data in fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            filmDao.addFilm(title, releaseDate, director);

            titleField.setText("");
            releaseDateField.setText("");
            directorField.setText("");

            loadFilmList();
        } catch (Exceptions e) {
            throw new RuntimeException(e);
        }
    }

    private void loadFilmList() {
        try {
            List<Film> films = filmDao.getAll();
            updateFilmListArea(films);
        } catch (Exceptions e) {
            e.printStackTrace();
        }
    }

    private void sortFilmsByTitle() {
        try {
            List<Film> films = filmDao.getAll();
            Collections.sort(films);
            updateFilmListArea(films);
        } catch (Exceptions e) {
            e.printStackTrace();
        }
    }

    private void sortFilmsByDirector() {
        try {
            List<Film> films = filmDao.getAll();
            films.sort(Film.DirectorComparator);
            updateFilmListArea(films);
        } catch (Exceptions e) {
            e.printStackTrace();
        }
    }

    private void updateFilmListArea(List<Film> films) {
        //для удобного склеивания строк
        StringBuilder filmsStringBuilder = new StringBuilder();

        for (Film film : films) {
            filmsStringBuilder.append("ID: ").append(film.getId())
                    .append(", Title: ").append(film.getName())
                    .append(", YOU CAN watch it from: ").append(film.getDate())
                    .append(", Director: ").append(film.getDirector())
                    .append("\n");
        }

        filmListArea.setText(filmsStringBuilder.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FilmsMain::new);
    }
}
