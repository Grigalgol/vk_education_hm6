package dao;

import commons.JDBCCredentials;
import entity.Invoice;
import generated.Tables;
import generated.tables.records.InvoiceRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO implements DAO<Invoice> {
    private static final @NotNull JDBCCredentials CREDS = JDBCCredentials.DEFAULT;

    @Override
    public @NotNull Invoice get(int id) {
        try (var connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password())) {
            var context = DSL.using(connection, SQLDialect.POSTGRES);
            InvoiceRecord record = context.fetchOne(Tables.INVOICE, Tables.INVOICE.ID.eq(id));
            if (record != null) {
                try {
                    return new Invoice(record.getId(),
                            new SimpleDateFormat("yyyy-MM-dd").parse(record.getDate().toString()),
                            record.getOrganizationSender());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
            return new Invoice();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull List<@NotNull Invoice> all() throws ParseException {
        try (var connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password())) {
            var context = DSL.using(connection, SQLDialect.POSTGRES);
            List<Invoice> allInvoices = new ArrayList<>();
            Result<InvoiceRecord> result = context.fetch(Tables.INVOICE);
            for (var record : result) {
                allInvoices.add(
                        new Invoice(
                                record.getId(),
                                new SimpleDateFormat("yyyy-MM-dd").parse(record.getDate().toString()),
                                record.getOrganizationSender()
                        )
                );
            }
            return allInvoices;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void save(@NotNull Invoice entity) {
        try (var connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password())) {
            var context = DSL.using(connection, SQLDialect.POSTGRES);
            context.newRecord(Tables.INVOICE)
                    .setId(entity.getId())
                    .setDate(LocalDate.ofInstant(entity.getDate().toInstant(), ZoneId.systemDefault()))
                    .setOrganizationSender(entity.getOrganizationSender())
                    .store();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void update(@NotNull Invoice entity) {
        try (var connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password())) {
            var context = DSL.using(connection, SQLDialect.POSTGRES);
            InvoiceRecord record = context.fetchOne(Tables.INVOICE, Tables.INVOICE.ID.eq(entity.getId()));
            if (record == null) throw new IllegalStateException("Invoice with id " + entity.getId() + " not found");
            record.setDate(LocalDate.ofInstant(entity.getDate().toInstant(), ZoneId.systemDefault()))
                    .setOrganizationSender(entity.getOrganizationSender())
                    .store();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(@NotNull Invoice entity) {
        try (var connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password())) {
            var context = DSL.using(connection, SQLDialect.POSTGRES);
            context.delete(Tables.INVOICE)
                    .where(Tables.INVOICE.ID.eq(entity.getId()))
                    .execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
