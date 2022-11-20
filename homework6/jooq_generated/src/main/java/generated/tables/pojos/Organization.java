/*
 * This file is generated by jOOQ.
 */
package generated.tables.pojos;


import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Organization implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String  name;
    private final Integer inn;
    private final String  paymentAccount;

    public Organization(Organization value) {
        this.name = value.name;
        this.inn = value.inn;
        this.paymentAccount = value.paymentAccount;
    }

    public Organization(
        String  name,
        Integer inn,
        String  paymentAccount
    ) {
        this.name = name;
        this.inn = inn;
        this.paymentAccount = paymentAccount;
    }

    /**
     * Getter for <code>public.organization.name</code>.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for <code>public.organization.inn</code>.
     */
    public Integer getInn() {
        return this.inn;
    }

    /**
     * Getter for <code>public.organization.payment_account</code>.
     */
    public String getPaymentAccount() {
        return this.paymentAccount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Organization (");

        sb.append(name);
        sb.append(", ").append(inn);
        sb.append(", ").append(paymentAccount);

        sb.append(")");
        return sb.toString();
    }
}
