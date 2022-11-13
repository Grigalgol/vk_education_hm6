package dao;

import commons.JDBCCredentials;
import entity.Invoice;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InvoiceDAOTest {

    private static InvoiceDAO dao;
    private static final JDBCCredentials CREDS = JDBCCredentials.DEFAULT;

    @BeforeAll
    static void setUp() {
        try {
            Connection connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password());
            connection.setAutoCommit(false);
            dao = new InvoiceDAO(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    static void tearDown() {
        try {
            Connection connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password());
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void get() {
        Date date = new Date(122, 10, 5);
        Invoice invoice = new Invoice(1, date, 12345);
        assertEquals(invoice, dao.get(invoice.getId()));
    }

    @Test
    void all() throws ParseException {
        List<Invoice> list = new ArrayList<>();
        Date date1 = new Date(122, 10, 5);
        Date date2 = new Date(122, 10, 6);
        Date date3 = new Date(122, 10, 7);
        Date date4 = new Date(122, 0, 1);
        Date date5 = new Date(122, 0, 2);
        list.add(new Invoice(1, date1, 12345));
        list.add(new Invoice(2, date2, 12345));
        list.add(new Invoice(3, date3, 12345));
        list.add(new Invoice(4, date4, 55555));
        list.add(new Invoice(5, date5, 55555));
        assertEquals(list, dao.all());
    }

    @Test
    void save() {
        Date date = new Date(122, 10, 5);
        Invoice invoice = new Invoice(99, date, 12345);
        dao.save(invoice);
        assertEquals(invoice, dao.get(invoice.getId()));
        dao.delete(invoice);
    }

    @Test
    void update() {
        Date date = new Date(122, 10, 5);
        Invoice invoice = new Invoice(99, date, 12345);
        dao.save(invoice);
        invoice.setOrganizationSender(55555);
        dao.update(invoice);
        assertEquals(invoice, dao.get(invoice.getId()));
        dao.delete(invoice);
    }

    @Test
    void delete() {
        Date date = new Date(122, 10, 5);
        Invoice invoice = new Invoice(99, date, 12345);
        dao.save(invoice);
        dao.delete(invoice);
        assertNotEquals(invoice, dao.get(invoice.getId()));
        assertNull(dao.get(invoice.getId()).getDate());
    }

}
