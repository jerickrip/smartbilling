package com.ni2.maskatel.smartbilling.mapping;

import com.ni2.maskatel.smartbilling.model.CustomerProduct;
import de.bytefish.pgbulkinsert.PgBulkInsert;
import org.postgresql.PGConnection;

import java.sql.SQLException;
import java.util.stream.Stream;

public class CustomerProductBulkInserter extends PgBulkInsert<CustomerProduct> {
    public CustomerProductBulkInserter() {
        super("public", "customerproduct");

        mapString("accnt_prod_no", CustomerProduct::getAccnt_prod_no);
        mapString("mod_date", CustomerProduct::getMod_date);
        mapString("location_id", CustomerProduct::getLocation_id);
        mapString("recurr_bill_cycle_type_id", CustomerProduct::getRecurr_bill_cycle_type_id);
        mapString("price", CustomerProduct::getPrice);
        mapString("product_id", CustomerProduct::getProduct_id);
        mapString("field_data_row_id", CustomerProduct::getField_data_row_id);
        mapString("create_date", CustomerProduct::getCreate_date);
        mapString("accnt_service_id", CustomerProduct::getAccnt_service_id);
        mapString("accnt_id", CustomerProduct::getAccnt_id);
        mapString("effective_from", CustomerProduct::getEffective_from);
        mapString("identifier5", CustomerProduct::getIdentifier5);
        mapString("supp_accnt_id", CustomerProduct::getSupp_accnt_id);
        mapString("parent_accnt_product_id", CustomerProduct::getParent_accnt_product_id);
        mapString("bill_start_date", CustomerProduct::getBill_start_date);
        mapString("cost", CustomerProduct::getCost);
        mapString("identifier1", CustomerProduct::getIdentifier1);
        mapString("identifier2", CustomerProduct::getIdentifier2);
        mapString("identifier3", CustomerProduct::getIdentifier3);
        mapString("identifier4", CustomerProduct::getIdentifier4);
        mapString("acc_accounting_code_id", CustomerProduct::getAcc_accounting_code_id);
        mapString("accnt_product_id", CustomerProduct::getAccnt_product_id);
        mapString("effective_to", CustomerProduct::getEffective_to);
        mapString("imp_ctl_id", CustomerProduct::getImp_ctl_id);
        mapString("comp_id", CustomerProduct::getComp_id);
        mapString("product_name", CustomerProduct::getProduct_name);
        mapString("owner_sec_user_id", CustomerProduct::getOwner_sec_user_id);
        mapString("create_username", CustomerProduct::getCreate_username);
        mapString("qty", CustomerProduct::getQty);
        mapString("mod_username", CustomerProduct::getMod_username);
        mapString("rn", CustomerProduct::getRn);
        mapString("bill_end_date", CustomerProduct::getBill_end_date);
    }

    @Override
    public void saveAll(PGConnection connection, Stream<CustomerProduct> entities) throws SQLException {
        super.saveAll(connection, entities);
    }
}
