/*
 * This file is generated by jOOQ.
 */
package generated.tables.pojos;


import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class InvoiceItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Integer id;
    private final Integer price;
    private final Integer product;
    private final Integer count;
    private final Integer idInvoice;

    public InvoiceItem(InvoiceItem value) {
        this.id = value.id;
        this.price = value.price;
        this.product = value.product;
        this.count = value.count;
        this.idInvoice = value.idInvoice;
    }

    public InvoiceItem(
        Integer id,
        Integer price,
        Integer product,
        Integer count,
        Integer idInvoice
    ) {
        this.id = id;
        this.price = price;
        this.product = product;
        this.count = count;
        this.idInvoice = idInvoice;
    }

    /**
     * Getter for <code>public.invoice_item.id</code>.
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * Getter for <code>public.invoice_item.price</code>.
     */
    public Integer getPrice() {
        return this.price;
    }

    /**
     * Getter for <code>public.invoice_item.product</code>.
     */
    public Integer getProduct() {
        return this.product;
    }

    /**
     * Getter for <code>public.invoice_item.count</code>.
     */
    public Integer getCount() {
        return this.count;
    }

    /**
     * Getter for <code>public.invoice_item.id_invoice</code>.
     */
    public Integer getIdInvoice() {
        return this.idInvoice;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("InvoiceItem (");

        sb.append(id);
        sb.append(", ").append(price);
        sb.append(", ").append(product);
        sb.append(", ").append(count);
        sb.append(", ").append(idInvoice);

        sb.append(")");
        return sb.toString();
    }
}
