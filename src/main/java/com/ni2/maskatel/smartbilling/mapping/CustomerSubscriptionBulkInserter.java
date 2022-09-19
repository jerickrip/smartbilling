package com.ni2.maskatel.smartbilling.mapping;

import com.ni2.maskatel.smartbilling.model.CustomerSubscription;
import de.bytefish.pgbulkinsert.PgBulkInsert;
import org.postgresql.PGConnection;

import java.sql.SQLException;
import java.util.stream.Stream;

public class CustomerSubscriptionBulkInserter extends PgBulkInsert<CustomerSubscription> {
    public CustomerSubscriptionBulkInserter() {
        super("public", "customersubscription");

        mapString("note", CustomerSubscription::getNote);
        mapString("accnt_service_no", CustomerSubscription::getAccnt_service_no);
        mapString("service_start_date", CustomerSubscription::getService_start_date);
        mapString("mod_date", CustomerSubscription::getMod_date);
        mapString("location_id", CustomerSubscription::getLocation_id);
        mapString("recurr_bill_cycle_type_id", CustomerSubscription::getRecurr_bill_cycle_type_id);
        mapString("service_id", CustomerSubscription::getService_id);
        mapString("create_date", CustomerSubscription::getCreate_date);
        mapString("accnt_service_id", CustomerSubscription::getAccnt_service_id);
        mapString("accnt_id", CustomerSubscription::getAccnt_id);
        mapString("identifier5", CustomerSubscription::getIdentifier5);
        mapString("bill_start_date", CustomerSubscription::getBill_start_date);
        mapString("service_end_date", CustomerSubscription::getService_end_date);
        mapString("identifier1", CustomerSubscription::getIdentifier1);
        mapString("identifier2", CustomerSubscription::getIdentifier2);
        mapString("identifier3", CustomerSubscription::getIdentifier3);
        mapString("service_name", CustomerSubscription::getService_name);
        mapString("identifier4", CustomerSubscription::getIdentifier4);
        mapString("comp_id", CustomerSubscription::getComp_id);
        mapString("owner_sec_user_id", CustomerSubscription::getOwner_sec_user_id);
        mapString("create_username", CustomerSubscription::getCreate_username);
        mapString("usage_bill_cycle_type_id", CustomerSubscription::getUsage_bill_cycle_type_id);
        mapString("mod_username", CustomerSubscription::getMod_username);
        mapString("rn", CustomerSubscription::getRn);
        mapString("bill_end_date", CustomerSubscription::getBill_end_date);
        mapString("usage_identifier", CustomerSubscription::getUsage_identifier);
    }

    @Override
    public void saveAll(PGConnection connection, Stream<CustomerSubscription> entities) throws SQLException {
        super.saveAll(connection, entities);
    }
}
