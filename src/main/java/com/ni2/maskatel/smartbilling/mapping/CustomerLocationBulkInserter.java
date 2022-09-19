package com.ni2.maskatel.smartbilling.mapping;

import com.ni2.maskatel.smartbilling.model.CustomerLocation;
import de.bytefish.pgbulkinsert.PgBulkInsert;
import org.postgresql.PGConnection;

import java.sql.SQLException;
import java.util.stream.Stream;

public class CustomerLocationBulkInserter extends PgBulkInsert<CustomerLocation> {
    public CustomerLocationBulkInserter() {
        super("public", "customerlocation");
        mapString("zip", CustomerLocation::getZip);
        mapString("addr_line1", CustomerLocation::getAddr_line1);
        mapString("city", CustomerLocation::getCity);
        mapString("custom_fields", CustomerLocation::getCustom_fields);
        mapString("imp_ctl_id", CustomerLocation::getImp_ctl_id);
        mapString("full_address", CustomerLocation::getFull_address);
        mapString("comp_id", CustomerLocation::getComp_id);
        mapString("location_id", CustomerLocation::getLocation_id);
        mapString("country_state", CustomerLocation::getCountry_state);
        mapString("state_name", CustomerLocation::getState_name);
        mapString("attention", CustomerLocation::getAttention);
        mapString("loc_name", CustomerLocation::getLoc_name);
        mapString("addr_line2", CustomerLocation::getAddr_line2);
        mapString("rn", CustomerLocation::getRn);
        mapString("state_code", CustomerLocation::getState_code);
        mapString("ctry_name", CustomerLocation::getCtry_name);
        mapString("accnt_id", CustomerLocation::getAccnt_id);
        mapString("ctry_code_iso2", CustomerLocation::getCtry_code_iso2);
    }

    @Override
    public void saveAll(PGConnection connection, Stream<CustomerLocation> entities) throws SQLException {
        super.saveAll(connection, entities);
    }
}
