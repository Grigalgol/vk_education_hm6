/*
 * This file is generated by jOOQ.
 */
package generated.tables.records;


import generated.tables.Invoice;

import java.time.LocalDate;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class InvoiceRecord extends UpdatableRecordImpl<InvoiceRecord> implements Record3<Integer, LocalDate, Integer> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.invoice.id</code>.
     */
    public InvoiceRecord setId(Integer value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.invoice.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>public.invoice.date</code>.
     */
    public InvoiceRecord setDate(LocalDate value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.invoice.date</code>.
     */
    public LocalDate getDate() {
        return (LocalDate) get(1);
    }

    /**
     * Setter for <code>public.invoice.organization_sender</code>.
     */
    public InvoiceRecord setOrganizationSender(Integer value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.invoice.organization_sender</code>.
     */
    public Integer getOrganizationSender() {
        return (Integer) get(2);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row3<Integer, LocalDate, Integer> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    @Override
    public Row3<Integer, LocalDate, Integer> valuesRow() {
        return (Row3) super.valuesRow();
    }

    @Override
    public Field<Integer> field1() {
        return Invoice.INVOICE.ID;
    }

    @Override
    public Field<LocalDate> field2() {
        return Invoice.INVOICE.DATE;
    }

    @Override
    public Field<Integer> field3() {
        return Invoice.INVOICE.ORGANIZATION_SENDER;
    }

    @Override
    public Integer component1() {
        return getId();
    }

    @Override
    public LocalDate component2() {
        return getDate();
    }

    @Override
    public Integer component3() {
        return getOrganizationSender();
    }

    @Override
    public Integer value1() {
        return getId();
    }

    @Override
    public LocalDate value2() {
        return getDate();
    }

    @Override
    public Integer value3() {
        return getOrganizationSender();
    }

    @Override
    public InvoiceRecord value1(Integer value) {
        setId(value);
        return this;
    }

    @Override
    public InvoiceRecord value2(LocalDate value) {
        setDate(value);
        return this;
    }

    @Override
    public InvoiceRecord value3(Integer value) {
        setOrganizationSender(value);
        return this;
    }

    @Override
    public InvoiceRecord values(Integer value1, LocalDate value2, Integer value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached InvoiceRecord
     */
    public InvoiceRecord() {
        super(Invoice.INVOICE);
    }

    /**
     * Create a detached, initialised InvoiceRecord
     */
    public InvoiceRecord(Integer id, LocalDate date, Integer organizationSender) {
        super(Invoice.INVOICE);

        setId(id);
        setDate(date);
        setOrganizationSender(organizationSender);
    }

    /**
     * Create a detached, initialised InvoiceRecord
     */
    public InvoiceRecord(generated.tables.pojos.Invoice value) {
        super(Invoice.INVOICE);

        if (value != null) {
            setId(value.getId());
            setDate(value.getDate());
            setOrganizationSender(value.getOrganizationSender());
        }
    }
}
