package by.tokt.dao;

import by.tokt.entity.Film;

import java.util.List;


public interface DAO {
    //добавление нового фильма
    void addFilm(String name, String date, String director) throws Exceptions;
    //вывод всех фильмов
    List<Film> getAll() throws Exceptions;
}
