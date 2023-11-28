package by.tokt.dao;

public class Exceptions extends Exception{

    //вывод ошибки по умолчанию
    public Exceptions() {
        super();
    }

    //вывод переданной ошибки
    public Exceptions(String message) {
        super(message);
    }

    //сообщение + объект исключения
    public Exceptions(String message, Throwable cause) {
        super(message, cause);
    }

    //объект исключения
    public Exceptions(Throwable cause) {
        super(cause);
    }

}
