package dao;

import commons.JDBCCredentials;
import entity.Organization;
import generated.Tables;
import generated.tables.records.OrganizationRecord;
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

public class OrganizationDAO implements DAO<Organization> {

    private static final @NotNull JDBCCredentials CREDS = JDBCCredentials.DEFAULT;

    @Override
    public @NotNull Organization get(int id) throws SQLException {
        var connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password());
        var context = DSL.using(connection, SQLDialect.POSTGRES);
        OrganizationRecord record = context.fetchOne(Tables.ORGANIZATION, Tables.ORGANIZATION.INN.eq(id));
        if (record != null) return new Organization(record.getName(), record.getInn(), record.getPaymentAccount());
        connection.close();
        return new Organization();
    }

    @Override
    public @NotNull List<@NotNull Organization> all() throws SQLException {
        var connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password());
        var context = DSL.using(connection, SQLDialect.POSTGRES);
        List<Organization> allOrganizations = new ArrayList<>();
        Result<OrganizationRecord> result = context.fetch(Tables.ORGANIZATION);
        for (var record : result) {
            allOrganizations.add(new Organization(record.getName(), record.getInn(), record.getPaymentAccount()));
        }
        connection.close();
        return allOrganizations;
    }

    @Override
    public void save(@NotNull Organization entity) throws SQLException {
        var connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password());
        var context = DSL.using(connection, SQLDialect.POSTGRES);
        context.newRecord(Tables.ORGANIZATION)
                .setInn(entity.getINN())
                .setName(entity.getName())
                .setPaymentAccount(entity.getPaymentAccount())
                .store();
        connection.close();
    }

    @Override
    public void update(@NotNull Organization entity) throws SQLException {
        var connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password());
        var context = DSL.using(connection, SQLDialect.POSTGRES);
        OrganizationRecord record = context.fetchOne(Tables.ORGANIZATION, Tables.ORGANIZATION.INN.eq(entity.getINN()));
        if (record == null) throw new IllegalStateException("Organization with id " + entity.getINN() + " not found");
        record.setName(entity.getName()).setPaymentAccount(entity.getPaymentAccount()).store();
        connection.close();
    }

    @Override
    public void delete(@NotNull Organization entity) throws SQLException {
        var connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password());
        var context = DSL.using(connection, SQLDialect.POSTGRES);
        context.delete(Tables.ORGANIZATION)
                .where(Tables.ORGANIZATION.INN.eq(entity.getINN()))
                .execute();
        connection.close();
    }
}
