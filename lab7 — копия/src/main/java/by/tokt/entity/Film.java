package by.tokt.entity;

import java.util.Comparator;

public class Film implements Comparable<Film>{
    private int id;
    private String name;
    private String date;
    private String director;

    public Film(int id, String name, String date, String director) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.director = director;
    }

    public Film(String name, String date, String director) {
        this.name = name;
        this.date = date;
        this.director = director;
    }

    public Film() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    //переопределение для сравнения по имени. меньше 0, если текущий меньше otherFilm.name
    @Override
    public int compareTo(Film otherFilm) {
        return this.name.compareTo(otherFilm.name);
    }

    //сравнение по режиссерам
    public static Comparator<Film> DirectorComparator = Comparator.comparing(Film::getDirector);
}
