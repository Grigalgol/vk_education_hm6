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
    private final @NotNull Connection connection;
    private final @NotNull DSLContext context;

    public InvoiceItemDAO(@NotNull Connection connection) {
        this.connection = connection;
        this.context = DSL.using(connection, SQLDialect.POSTGRES);
    }

    public InvoiceItemDAO() {
        try {
            this.connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password());
            this.context = DSL.using(connection, SQLDialect.POSTGRES);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull InvoiceItem get(int id) {
        InvoiceItemRecord record = context.fetchOne(Tables.INVOICE_ITEM, Tables.INVOICE_ITEM.ID.eq(id));
        if (record != null)
            return new InvoiceItem(record.getId(), record.getIdInvoice(), record.getProduct(), record.getPrice(), record.getCount());
        return new InvoiceItem();
    }

    @Override
    public @NotNull List<@NotNull InvoiceItem> all() throws ParseException {
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
        return allInvoiceItems;
    }

    @Override
    public void save(@NotNull InvoiceItem entity) {
        context.newRecord(Tables.INVOICE_ITEM)
                .setId(entity.getId())
                .setIdInvoice(entity.getIdInvoice())
                .setProduct(entity.getProductCode())
                .setPrice(entity.getPrice())
                .setCount(entity.getCount())
                .store();
    }

    @Override
    public void update(@NotNull InvoiceItem entity) {
        InvoiceItemRecord record = context.fetchOne(Tables.INVOICE_ITEM, Tables.INVOICE_ITEM.ID.eq(entity.getId()));
        if (record == null) throw new IllegalStateException("Invoice Item with id " + entity.getId() + " not found");
        record.setIdInvoice(entity.getIdInvoice())
                .setProduct(entity.getProductCode())
                .setPrice(entity.getPrice())
                .setCount(entity.getCount())
                .store();
    }

    @Override
    public void delete(@NotNull InvoiceItem entity) {
        context.delete(Tables.INVOICE_ITEM)
                .where(Tables.INVOICE_ITEM.ID.eq(entity.getId()))
                .execute();
    }
}
