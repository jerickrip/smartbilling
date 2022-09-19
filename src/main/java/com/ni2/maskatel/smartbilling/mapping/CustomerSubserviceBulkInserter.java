package com.ni2.maskatel.smartbilling.mapping;

import com.ni2.maskatel.smartbilling.model.CustomerSubservice;
import de.bytefish.pgbulkinsert.PgBulkInsert;
import org.postgresql.PGConnection;

import java.sql.SQLException;
import java.util.stream.Stream;

public class CustomerSubserviceBulkInserter extends PgBulkInsert<CustomerSubservice> {
    public CustomerSubserviceBulkInserter() {
        super("public", "customersubservice");

        mapString("accnt_prod_no",CustomerSubservice::getAccnt_prod_no);
        mapString("mod_date",CustomerSubservice::getMod_date);
        mapString("location_id",CustomerSubservice::getLocation_id);
        mapString("recurr_bill_cycle_type_id",CustomerSubservice::getRecurr_bill_cycle_type_id);
        mapString("price",CustomerSubservice::getPrice);
        mapString("product_id",CustomerSubservice::getProduct_id);
        mapString("field_data_row_id",CustomerSubservice::getField_data_row_id);
        mapString("create_date",CustomerSubservice::getCreate_date);
        mapString("accnt_service_id",CustomerSubservice::getAccnt_service_id);
        mapString("accnt_id",CustomerSubservice::getAccnt_id);
        mapString("effective_from",CustomerSubservice::getEffective_from);
        mapString("identifier5",CustomerSubservice::getIdentifier5);
        mapString("supp_accnt_id",CustomerSubservice::getSupp_accnt_id);
        mapString("parent_accnt_product_id",CustomerSubservice::getParent_accnt_product_id);
        mapString("bill_start_date",CustomerSubservice::getBill_start_date);
        mapString("cost",CustomerSubservice::getCost);
        mapString("identifier1",CustomerSubservice::getIdentifier1);
        mapString("identifier2",CustomerSubservice::getIdentifier2);
        mapString("identifier3",CustomerSubservice::getIdentifier2);
        mapString("identifier4",CustomerSubservice::getIdentifier4);
        mapString("acc_accounting_code_id",CustomerSubservice::getAcc_accounting_code_id);
        mapString("accnt_product_id",CustomerSubservice::getAccnt_product_id);
        mapString("effective_to",CustomerSubservice::getEffective_to);
        mapString("imp_ctl_id",CustomerSubservice::getImp_ctl_id);
        mapString("comp_id",CustomerSubservice::getComp_id);
        mapString("product_name",CustomerSubservice::getProduct_name);
        mapString("owner_sec_user_id",CustomerSubservice::getOwner_sec_user_id);
        mapString("create_username",CustomerSubservice::getCreate_username);
        mapString("qty",CustomerSubservice::getQty);
        mapString("mod_username",CustomerSubservice::getMod_username);
        mapString("rn",CustomerSubservice::getRn);
        mapString("bill_end_date",CustomerSubservice::getBill_end_date);
    }
    
    @Override
    public void saveAll(PGConnection connection, Stream<CustomerSubservice> entities) throws SQLException {
        super.saveAll(connection, entities);
    }}
