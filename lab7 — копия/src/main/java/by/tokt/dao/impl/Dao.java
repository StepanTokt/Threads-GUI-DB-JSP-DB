package by.tokt.dao.impl;

import by.tokt.dao.Connection;
import by.tokt.dao.DAO;
import by.tokt.dao.Exceptions;
import by.tokt.entity.Film;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Dao implements DAO {



    @Override
    public void addFilm(String name, String date, String director) throws Exceptions {
        try (java.sql.Connection connection = Connection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO films (name, date, director) VALUES (?, ?, ?)")) {

            //установка параметров в строку выше
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, date);
            preparedStatement.setString(3, director);

            preparedStatement.executeUpdate(); //выполнение запроса для вставки
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exceptions("Ошибка");
        }
    }

    @Override
    public List<Film> getAll() throws Exceptions {
        List<Film> films = new ArrayList<>();

        try (java.sql.Connection connection = Connection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM films");
             ResultSet resultSet = preparedStatement.executeQuery()) //возвращает выборку
        {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String date = resultSet.getString("date");
                String director = resultSet.getString("director");

                Film film = new Film(id, name, date, director);
                films.add(film);
            }

            return films;
        } catch (SQLException e) {
            throw new Exceptions("Error fetching all films", e);
        }
    }
}
