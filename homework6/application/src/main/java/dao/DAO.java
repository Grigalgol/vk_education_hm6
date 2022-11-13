package dao;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.util.List;

public interface DAO<T> {

    @NotNull T get(int id);

    @NotNull List< @NotNull T> all() throws ParseException;

    void save(@NotNull T entity);

    void update(@NotNull T entity);

    void delete(@NotNull T entity);
}
