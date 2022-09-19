package com.ni2.maskatel.smartbilling.mapping;

import com.ni2.maskatel.smartbilling.model.CustomerMisc;
import de.bytefish.pgbulkinsert.PgBulkInsert;
import org.postgresql.PGConnection;

import java.sql.SQLException;
import java.util.stream.Stream;

public class CustomerMiscBulkInsert extends PgBulkInsert<CustomerMisc> {
    public CustomerMiscBulkInsert(){
        super("public", "customermisc");

        mapString("accnt_prod_no", CustomerMisc::getAccnt_prod_no);
        mapString("mod_date",CustomerMisc::getMod_date);
        mapString("location_id",CustomerMisc::getLocation_id);
        mapString("recurr_bill_cycle_type_id",CustomerMisc::getRecurr_bill_cycle_type_id);
        mapString("price",CustomerMisc::getPrice);
        mapString("product_id",CustomerMisc::getProduct_id);
        mapString("field_data_row_id",CustomerMisc::getField_data_row_id);
        mapString("create_date",CustomerMisc::getCreate_date);
        mapString("accnt_service_id",CustomerMisc::getAccnt_service_id);
        mapString("accnt_id",CustomerMisc::getAccnt_id);
        mapString("effective_from",CustomerMisc::getEffective_from);
        mapString("identifier5",CustomerMisc::getIdentifier5);
        mapString("supp_accnt_id",CustomerMisc::getSupp_accnt_id);
        mapString("parent_accnt_product_id",CustomerMisc::getParent_accnt_product_id);
        mapString("bill_start_date",CustomerMisc::getBill_start_date);
        mapString("cost",CustomerMisc::getCost);
        mapString("identifier1",CustomerMisc::getIdentifier1);
        mapString("identifier2",CustomerMisc::getIdentifier2);
        mapString("identifier3",CustomerMisc::getIdentifier2);
        mapString("identifier4",CustomerMisc::getIdentifier4);
        mapString("acc_accounting_code_id",CustomerMisc::getAcc_accounting_code_id);
        mapString("accnt_product_id",CustomerMisc::getAccnt_product_id);
        mapString("effective_to",CustomerMisc::getEffective_to);
        mapString("imp_ctl_id",CustomerMisc::getImp_ctl_id);
        mapString("comp_id",CustomerMisc::getComp_id);
        mapString("product_name",CustomerMisc::getProduct_name);
        mapString("owner_sec_user_id",CustomerMisc::getOwner_sec_user_id);
        mapString("create_username",CustomerMisc::getCreate_username);
        mapString("qty",CustomerMisc::getQty);
        mapString("mod_username",CustomerMisc::getMod_username);
        mapString("rn",CustomerMisc::getRn);
        mapString("bill_end_date",CustomerMisc::getBill_end_date);
    }

    @Override
    public void saveAll(PGConnection connection, Stream<CustomerMisc> entities) throws SQLException {
        super.saveAll(connection, entities);
    }
}
