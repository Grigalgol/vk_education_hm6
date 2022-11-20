package dao;

import commons.JDBCCredentials;
import entity.InvoiceItem;
import generated.Tables;
import generated.tables.records.InvoiceItemRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class InvoiceItemDAO implements DAO<InvoiceItem> {
    private static final @NotNull JDBCCredentials CREDS = JDBCCredentials.DEFAULT;

    @Override
    public @NotNull InvoiceItem get(int id) throws SQLException {
        var connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password());
        var context = DSL.using(connection, SQLDialect.POSTGRES);
        InvoiceItemRecord record = context.fetchOne(Tables.INVOICE_ITEM, Tables.INVOICE_ITEM.ID.eq(id));
        connection.close();
        if (record != null)
            return new InvoiceItem(record.getId(), record.getIdInvoice(), record.getProduct(), record.getPrice(), record.getCount());
        return new InvoiceItem();
    }

    @Override
    public @NotNull List<@NotNull InvoiceItem> all() throws SQLException {
        var connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password());
        var context = DSL.using(connection, SQLDialect.POSTGRES);
        List<InvoiceItem> allInvoiceItems = new ArrayList<>();
        Result<InvoiceItemRecord> result = context.fetch(Tables.INVOICE_ITEM);
        for (var record : result) {
            allInvoiceItems.add(
                    new InvoiceItem(
                            record.getId(),
                            record.getIdInvoice(),
                            record.getProduct(),
                            record.getPrice(),
                            record.getCount()
                    )
            );
        }
        connection.close();
        return allInvoiceItems;
    }

    @Override
    public void save(@NotNull InvoiceItem entity) throws SQLException {
        var connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password());
        var context = DSL.using(connection, SQLDialect.POSTGRES);
        context.newRecord(Tables.INVOICE_ITEM)
                .setId(entity.getId())
                .setIdInvoice(entity.getIdInvoice())
                .setProduct(entity.getProductCode())
                .setPrice(entity.getPrice())
                .setCount(entity.getCount())
                .store();
        connection.close();
    }

    @Override
    public void update(@NotNull InvoiceItem entity) throws SQLException {
        var connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password());
        var context = DSL.using(connection, SQLDialect.POSTGRES);
        InvoiceItemRecord record = context.fetchOne(Tables.INVOICE_ITEM, Tables.INVOICE_ITEM.ID.eq(entity.getId()));
        if (record == null) throw new IllegalStateException("Invoice Item with id " + entity.getId() + " not found");
        record.setIdInvoice(entity.getIdInvoice())
                .setProduct(entity.getProductCode())
                .setPrice(entity.getPrice())
                .setCount(entity.getCount())
                .store();
        connection.close();
    }

    @Override
    public void delete(@NotNull InvoiceItem entity) throws SQLException {
        var connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password());
        var context = DSL.using(connection, SQLDialect.POSTGRES);
        context.delete(Tables.INVOICE_ITEM)
                .where(Tables.INVOICE_ITEM.ID.eq(entity.getId()))
                .execute();
        connection.close();
    }
}
