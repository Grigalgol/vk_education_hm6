package dao;

import commons.JDBCCredentials;
import entity.Product;
import generated.Tables;
import generated.tables.records.ProductRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO implements DAO<Product> {

    private static final @NotNull JDBCCredentials CREDS = JDBCCredentials.DEFAULT;
    private final @NotNull Connection connection;
    private final @NotNull DSLContext context;

    public ProductDAO(@NotNull Connection connection) {
        this.connection = connection;
        this.context = DSL.using(connection, SQLDialect.POSTGRES);
    }

    public ProductDAO() {
        try {
            this.connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password());
            this.context = DSL.using(connection, SQLDialect.POSTGRES);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull Product get(int id) {
        ProductRecord record = context.fetchOne(Tables.PRODUCT, Tables.PRODUCT.INTERNAL_CODE.eq(id));
        if (record != null) return new Product(record.getName(), record.getInternalCode());
        return new Product();
    }

    @Override
    public @NotNull List<@NotNull Product> all() {
        List<Product> allProducts = new ArrayList<>();
        Result<ProductRecord> result = context.fetch(Tables.PRODUCT);
        for (var record : result) {
            allProducts.add(new Product(record.getName(), record.getInternalCode()));
        }
        return allProducts;
    }

    @Override
    public void save(@NotNull Product entity) {
        context.newRecord(Tables.PRODUCT).setName(entity.getName()).setInternalCode(entity.getInternalCode()).store();
    }

    @Override
    public void update(@NotNull Product entity) {
        ProductRecord record = context.fetchOne(Tables.PRODUCT, Tables.PRODUCT.INTERNAL_CODE.eq(entity.getInternalCode()));
        if (record == null)
            throw new IllegalStateException("Product with id " + entity.getInternalCode() + " not found");
        record.setName(entity.getName()).store();
    }

    @Override
    public void delete(@NotNull Product entity) {
        context.delete(Tables.PRODUCT)
                .where(Tables.PRODUCT.INTERNAL_CODE.eq(entity.getInternalCode()))
                .execute();
    }
}
