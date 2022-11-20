package dao;

import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

public interface DAO<T> {

    @NotNull T get(int id) throws SQLException;

    @NotNull List< @NotNull T> all() throws ParseException, SQLException;

    void save(@NotNull T entity) throws SQLException;

    void update(@NotNull T entity) throws SQLException;

    void delete(@NotNull T entity) throws SQLException;
}
