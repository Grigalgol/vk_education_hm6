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
    private final @NotNull Connection connection;
    private final @NotNull DSLContext context;

    public OrganizationDAO(@NotNull Connection connection) {
        this.connection = connection;
        this.context = DSL.using(connection, SQLDialect.POSTGRES);
    }

    public OrganizationDAO() {
        try {
            this.connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password());
            this.context = DSL.using(connection, SQLDialect.POSTGRES);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull Organization get(int id) {
        OrganizationRecord record = context.fetchOne(Tables.ORGANIZATION, Tables.ORGANIZATION.INN.eq(id));
        if (record != null) return new Organization(record.getName(), record.getInn(), record.getPaymentAccount());
        return new Organization();
    }

    @Override
    public @NotNull List<@NotNull Organization> all() {
        List<Organization> allOrganizations = new ArrayList<>();
        Result<OrganizationRecord> result = context.fetch(Tables.ORGANIZATION);
        for (var record : result) {
            allOrganizations.add(new Organization(record.getName(), record.getInn(), record.getPaymentAccount()));
        }
        return allOrganizations;
    }

    @Override
    public void save(@NotNull Organization entity) {
        context.newRecord(Tables.ORGANIZATION)
                .setInn(entity.getINN())
                .setName(entity.getName())
                .setPaymentAccount(entity.getPaymentAccount())
                .store();
    }

    @Override
    public void update(@NotNull Organization entity) {
        OrganizationRecord record = context.fetchOne(Tables.ORGANIZATION, Tables.ORGANIZATION.INN.eq(entity.getINN()));
        if (record == null) throw new IllegalStateException("Organization with id " + entity.getINN() + " not found");
        record.setName(entity.getName()).setPaymentAccount(entity.getPaymentAccount()).store();
    }

    @Override
    public void delete(@NotNull Organization entity) {
        context.delete(Tables.ORGANIZATION)
                .where(Tables.ORGANIZATION.INN.eq(entity.getINN()))
                .execute();
    }
}
