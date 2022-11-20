import commons.JDBCCredentials;
import entity.Organization;
import entity.Product;
import org.jetbrains.annotations.NotNull;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static generated.Tables.*;

public class ReportManager {
    private final @NotNull Connection connection;
    private final @NotNull DSLContext context;
    private static final @NotNull JDBCCredentials CREDS = JDBCCredentials.DEFAULT;

    public ReportManager(@NotNull Connection connection) {
        this.connection = connection;
        this.context = DSL.using(connection, SQLDialect.POSTGRES);
    }

    public ReportManager() {
        try {
            this.connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password());
            this.context = DSL.using(connection, SQLDialect.POSTGRES);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Выбрать первые 10 поставщиков по количеству поставленного товара
    public @NotNull List<@NotNull Organization> getFirstTenOrganizationByDeliveredProduct() {
        List<Organization> organizations = new ArrayList<>();
        var record = context.select(ORGANIZATION.NAME, ORGANIZATION.INN, ORGANIZATION.PAYMENT_ACCOUNT)
                .from(ORGANIZATION)
                .join(INVOICE).on(ORGANIZATION.INN.eq(INVOICE.ORGANIZATION_SENDER))
                .join(INVOICE_ITEM).on(INVOICE.ID.eq(INVOICE_ITEM.ID_INVOICE))
                .groupBy(ORGANIZATION.INN)
                .orderBy(DSL.sum(INVOICE_ITEM.COUNT).desc())
                .limit(10);
        for (var res : record) {
            organizations.add(new Organization(res.value1(), res.value2(), res.value3()));
        }
        return organizations;
    }

    //Выбрать поставщиков с суммой поставленного товара выше указанного количества
    //(товар и его количество должны допускать множественное указание).
    //Я до конца не понял что значит уточнение в скобках,но я решил сделать реализацию через OR (хотя можно понять и как AND)
    public @NotNull List<@NotNull Organization> getOrganizationWithSumDeliveredProductIsMoreCount(Map<Integer, Integer> map) {
        ArrayList<Organization> result = new ArrayList<>();
        var recordSelectQuery= context.selectQuery();
        recordSelectQuery.addSelect(ORGANIZATION.INN, ORGANIZATION.NAME, ORGANIZATION.PAYMENT_ACCOUNT, INVOICE_ITEM.PRODUCT, DSL.sum(INVOICE_ITEM.COUNT));
        recordSelectQuery.addFrom(ORGANIZATION);
        recordSelectQuery.addJoin(INVOICE,ORGANIZATION.INN.eq(INVOICE.ORGANIZATION_SENDER));
        recordSelectQuery.addJoin(INVOICE_ITEM, INVOICE.ID.eq(INVOICE_ITEM.ID_INVOICE));
        boolean isFirst = true;
        Condition condition = null;
        for (var entry: map.entrySet()){
            if (isFirst) {
                condition=INVOICE_ITEM.PRODUCT.eq(entry.getKey());
                isFirst = false;
            }
            else condition= condition.or(INVOICE_ITEM.PRODUCT.eq(entry.getKey()));
        }
        recordSelectQuery.addConditions(condition);
        recordSelectQuery.addGroupBy(ORGANIZATION.INN,INVOICE_ITEM.PRODUCT);
        for (var record: recordSelectQuery.fetch()){
            Organization organization = new Organization(record.get(ORGANIZATION.NAME), record.get(ORGANIZATION.INN),  record.get(ORGANIZATION.PAYMENT_ACCOUNT));
            int productId = record.getValue(INVOICE_ITEM.PRODUCT);
            Integer code =  map.keySet().stream().filter(productCode -> productId == productCode).findFirst().orElse(null);
            int productSumCount = record.getValue(DSL.sum(INVOICE_ITEM.COUNT)).intValue();
            if (productSumCount >= map.get(code) && !result.contains(organization))
                result.add(organization);
        }
        return result;
    }


    //За каждый день для каждого товара рассчитать количество и сумму полученного товара
    // в указанном периоде, посчитать итоги за период
    public @NotNull Map<LocalDate, List<Map<Product, Map<String, Integer>>>> getCountAndSumProductByDayInPeriod(Date start, Date end) {
        Map<LocalDate, List<Map<Product, Map<String, Integer>>>> map = new HashMap<>();
        LocalDate s = LocalDate.ofInstant(start.toInstant(), ZoneId.systemDefault());
        LocalDate e = LocalDate.ofInstant(end.toInstant(), ZoneId.systemDefault());
        Map<Product, Integer> totalCount = new HashMap<>();
        Map<Product, Integer> totalSum = new HashMap<>();

        var record = context
                .select(INVOICE.DATE, PRODUCT.NAME, PRODUCT.INTERNAL_CODE, DSL.sum(INVOICE_ITEM.COUNT), DSL.sum(INVOICE_ITEM.PRICE))
                .from(PRODUCT)
                .join(INVOICE_ITEM).on(INVOICE_ITEM.PRODUCT.eq(PRODUCT.INTERNAL_CODE))
                .join(INVOICE).on(INVOICE_ITEM.ID_INVOICE.eq(INVOICE.ID))
                .where(INVOICE.DATE.between(s, e))
                .groupBy(INVOICE.DATE, PRODUCT.NAME, PRODUCT.INTERNAL_CODE);

        for (var res : record) {
            LocalDate date = res.value1();
            Product product = new Product(res.value2(), res.value3());
            if (!totalCount.containsKey(product)) {
                totalCount.put(product, 0);
            }
            if (!totalSum.containsKey(product)) {
                totalSum.put(product, 0);
            }
            String count = "count";
            String sum = "sum";
            Integer countInt = res.value4().intValue();
            Integer sumInt = res.value5().intValue();
            Integer currentCount = totalCount.get(product) + countInt;
            Integer currentSum = totalSum.get(product) + sumInt;
            totalCount.put(product, currentCount);
            totalSum.put(product, currentSum);
            if (!map.containsKey(date)) {
                map.put(date, new ArrayList<>());
            }
            Map<String, Integer> mapCountAndSum = new HashMap<>();
            mapCountAndSum.put(count, countInt);
            mapCountAndSum.put(sum, sumInt);

            Map<Product, Map<String, Integer>> productMap = new HashMap<>();
            productMap.put(product, mapCountAndSum);
            map.get(date).add(productMap);
        }
        System.out.println("Total result of count: ");
        System.out.println(totalCount);
        System.out.println("Total result of price: ");
        System.out.println(totalSum);
        return map;
    }


    //Рассчитать среднюю цену полученного товара за период
    public double getAveragePrice(int product, Date start, Date end) {
        LocalDate s = LocalDate.ofInstant(start.toInstant(), ZoneId.systemDefault());
        LocalDate e = LocalDate.ofInstant(end.toInstant(), ZoneId.systemDefault());

        var record = context
                .select(DSL.avg(INVOICE_ITEM.PRICE))
                .from(INVOICE_ITEM)
                .join(INVOICE).on(INVOICE_ITEM.ID_INVOICE.eq(INVOICE.ID))
                .where(INVOICE.DATE.between(s, e))
                .groupBy(INVOICE_ITEM.PRODUCT)
                .having(INVOICE_ITEM.PRODUCT.eq(product));

        for (var res : record) {
            return res.value1().doubleValue();
        }
        return 0;
    }


    //Вывести список товаров, поставленных организациями за период.
    //Если организация товары не поставляла, то она все равно должна быть отражена в списке.
    public @NotNull Map<Organization, List<Product>> getListOfProductDeliveredByOrgFOrPeriod(Date start, Date end) {
        Map<Organization, List<Product>> map = new HashMap<>();
        LocalDate s = LocalDate.ofInstant(start.toInstant(), ZoneId.systemDefault());
        LocalDate e = LocalDate.ofInstant(end.toInstant(), ZoneId.systemDefault());

        var record = context
                .select(ORGANIZATION.NAME, ORGANIZATION.INN, ORGANIZATION.PAYMENT_ACCOUNT, PRODUCT.NAME, PRODUCT.INTERNAL_CODE)
                .from(ORGANIZATION)
                .leftJoin(INVOICE).on(INVOICE.ORGANIZATION_SENDER.eq(ORGANIZATION.INN).and((INVOICE.DATE.between(s, e))))
                .leftJoin(INVOICE_ITEM).on(INVOICE_ITEM.ID_INVOICE.eq(INVOICE.ID))
                .leftJoin(PRODUCT).on(PRODUCT.INTERNAL_CODE.eq(INVOICE_ITEM.PRODUCT));

        for (var result : record) {
            Organization organization = new Organization(
                    result.value1(),
                    result.value2(),
                    result.value3()
            );
            Product product = null;
            if (result.value4() != null) {
                product = new Product(
                        result.value4(),
                        result.value5()
                );
            }
            if (!map.containsKey(organization)) {
                map.put(organization, new ArrayList<>());
            }
            if (product != null) {
                map.get(organization).add(product);
            }
        }

        return map;
    }
}
