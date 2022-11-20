package dao;

import entity.Invoice;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InvoiceDAOTest {

    private static InvoiceDAO dao;

    @BeforeAll
    static void setUp() {
        dao = new InvoiceDAO();
    }


    @Test
    void get() throws SQLException {
        Date date = new java.sql.Date(122, 10, 5);
        Invoice invoice = new Invoice(1, date, 12345);
        assertEquals(invoice, dao.get(invoice.getId()));
    }

    @Test
    void all() throws ParseException {
        List<Invoice> list = new ArrayList<>();
        Date date1 = new java.sql.Date(122, 10, 5);
        Date date2 = new java.sql.Date(122, 10, 6);
        Date date3 = new java.sql.Date(122, 10, 7);
        Date date4 = new java.sql.Date(122, 0, 1);
        Date date5 = new java.sql.Date(122, 0, 2);
        list.add(new Invoice(1, date1, 12345));
        list.add(new Invoice(2, date2, 12345));
        list.add(new Invoice(3, date3, 12345));
        list.add(new Invoice(4, date4, 55555));
        list.add(new Invoice(5, date5, 55555));
        list.add(new Invoice(6, date5, 98765));
        list.add(new Invoice(7, date5, 56789));
        list.add(new Invoice(8, date5, 66666));
        list.add(new Invoice(9, date5, 77777));
        list.add(new Invoice(10, date5, 88888));
        list.add(new Invoice(11, date5, 54645));
        list.add(new Invoice(12, date5, 54123));
        assertEquals(list, dao.all());
    }

    @Test
    void save() throws SQLException {
        Date date = new Date(122, 10, 5);
        Invoice invoice = new Invoice(99, date, 12345);
        dao.save(invoice);
        assertEquals(invoice, dao.get(invoice.getId()));
        dao.delete(invoice);
    }

    @Test
    void update() throws SQLException {
        Date date1 = new Date(122, 10, 5);
        Invoice invoice = new Invoice(99, date1, 12345);
        dao.save(invoice);
        invoice.setOrganizationSender(55555);
        dao.update(invoice);
        assertEquals(invoice, dao.get(invoice.getId()));
        dao.delete(invoice);
    }

    @Test
    void delete() throws SQLException {
        Date date1 = new Date(122, 10, 5);
        Invoice invoice = new Invoice(99, date1, 12345);
        dao.save(invoice);
        dao.delete(invoice);
        assertNotEquals(invoice, dao.get(invoice.getId()));
        assertNull(dao.get(invoice.getId()).getDate());
    }

}
