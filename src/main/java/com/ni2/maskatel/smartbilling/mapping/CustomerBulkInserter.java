package com.ni2.maskatel.smartbilling.mapping;

import com.ni2.maskatel.smartbilling.model.Customer;
import de.bytefish.pgbulkinsert.PgBulkInsert;
import org.postgresql.PGConnection;

import java.sql.SQLException;
import java.util.stream.Stream;

public class CustomerBulkInserter extends PgBulkInsert<Customer> {
    //    private static Map<String, String> mapString;
    public CustomerBulkInserter() {
        super("public", "customer");

        mapString("inv_start_date", Customer::getInv_start_date);
        mapString("acc_invoice_template_id", Customer::getAcc_invoice_template_id);
        mapString("mod_date", Customer::getMod_date);
        mapString("external_id", Customer::getExternal_id);
        mapString("location_id", Customer::getLocation_id);
        mapString("deleted_date", Customer::getDeleted_date);
        mapString("lang_code", Customer::getLang_code);
        mapString("owner_department_id", Customer::getOwner_department_id);
        mapString("accnt_type_id", Customer::getAccnt_type_id);
        mapString("field_data_row_id", Customer::getField_data_row_id);
        mapString("fax", Customer::getFax);
        mapString("inv_term_id", Customer::getInv_term_id);
        mapString("create_date", Customer::getCreate_date);
        mapString("accnt_id", Customer::getAccnt_id);
        mapString("bill_cycle_type_id", Customer::getBill_cycle_type_id);
        mapString("accnt_catg_id", Customer::getAccnt_catg_id);
        mapString("email", Customer::getEmail);
        mapString("security_deposit", Customer::getSecurity_deposit);
        mapString("send_ntfy", Customer::getSend_ntfy);
        mapString("imp_ctl_id", Customer::getImp_ctl_id);
        mapString("portal_access", Customer::getPortal_access);
        mapString("comp_id", Customer::getComp_id);
        mapString("crcy_code", Customer::getCrcy_code);
        mapString("accnt_name", Customer::getAccnt_name);
        mapString("owner_sec_user_id", Customer::getOwner_sec_user_id);
        mapString("inv_mthd_type_id", Customer::getInv_mthd_type_id);
        mapString("comp_dba_id", Customer::getComp_dba_id);
        mapString("create_username", Customer::getCreate_username);
        mapString("inv_late_fee", Customer::getInv_late_fee);
        mapString("phone", Customer::getPhone);
        mapString("accnt_status_id", Customer::getAccnt_status_id);
        mapString("accnt_no", Customer::getAccnt_no);
        mapString("mod_username", Customer::getMod_username);
        mapString("deleted_username", Customer::getDeleted_username);
        mapString("rn", Customer::getRn);
        mapString("security_deposit_bal", Customer::getSecurity_deposit);
        mapString("parent_accnt_id", Customer::getParent_accnt_id);
        mapString("phone_ext", Customer::getPhone_ext);
    }

    @Override
    public void saveAll(PGConnection connection, Stream<Customer> entities) throws SQLException {
        super.saveAll(connection, entities);
    }
}
