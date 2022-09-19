package com.ni2.maskatel.smartbilling.mapping;

import com.ni2.maskatel.smartbilling.model.CustomerAddress;
import de.bytefish.pgbulkinsert.PgBulkInsert;
import org.postgresql.PGConnection;

import java.sql.SQLException;
import java.util.stream.Stream;

public class CustomerAddressBulkInserter extends PgBulkInsert<CustomerAddress> {
    public CustomerAddressBulkInserter() {
        super("public", "customeraddress");
        mapString("loc_name", CustomerAddress::getLoc_name);
        mapString("attention", CustomerAddress::getAttention);
        mapString("addr_line1", CustomerAddress::getAddr_line1);
        mapString("addr_line2", CustomerAddress::getAddr_line2);
        mapString("city", CustomerAddress::getCity);
        mapString("state_code", CustomerAddress::getState_code);
        mapString("ctry_code", CustomerAddress::getCtry_code);
        mapString("zip", CustomerAddress::getZip);
    }

    @Override
    public void saveAll(PGConnection connection, Stream<CustomerAddress> entities) throws SQLException {
        super.saveAll(connection, entities);
    }
}
