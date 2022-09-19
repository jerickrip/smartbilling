package com.ni2.maskatel.smartbilling.utils;

import com.ni2.maskatel.smartbilling.mapping.*;
import com.ni2.maskatel.smartbilling.model.*;
import com.opencsv.CSVWriter;
import de.bytefish.pgbulkinsert.util.PostgreSqlUtils;
import org.json.simple.JSONObject;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtils {
    private static Map<String, JSONObject> customerMap;
    private static Map<String, JSONObject> custLocMap;
    private static Map<String, JSONObject> packageMap;
    private static Map<String, JSONObject> serviceMap;
    private static Map<String, JSONObject> subserviceMap;
    private static Map<String, JSONObject> miscchargeMap;
    private static Map<String, JSONObject> contactsMap;
    private static Map<String, JSONObject> addressMap;
    private static Map<String, JSONObject> srvlcMap;
    private static Map<String, String> paramaddr;
    private static List<Customer> customerlist = new ArrayList<>();
    private static List<CustomerLocation> customerlocationlist = new ArrayList<>();
    private static List<CustomerAddress> customeraddresslist = new ArrayList<>();
    private static List<CustomerSubscription> customersubscriptionlist = new ArrayList<>();
    private static List<CustomerProduct> customerproductlist = new ArrayList<>();
    private static List<CustomerSubservice> customersubservicelist = new ArrayList<>();
    private static List<CustomerMisc> customermisclist = new ArrayList<>();
    private static String[] suffixstr = new String[]{"AL","ALLEE","ALLEY","AUT","AV","AVE","AVENUE","BD","BOUL","CAR","CARREF","CERCLE","CH","CIR","COTE","COUR","COURS","CRES","CROIS","CRT","DR",
            "ESPL","GATE","ILE","IMP","ISLAND","LANE","MONTEE","PASS","PK","PL","PLACE","PLAT","PROM","PT","PVT","QUAI","RANG","RD","RG","RL","RIDGE","RLE","ROW","RT","RTE","RU","RUE",
            "SENT","TU","TSSE","VOIE"};
    private static List<String> suffixlist = Arrays.asList(suffixstr);
    private static String street="";
    private static String civic="";
    private static String suffix="";
    private static String nameaddress="";
    private static String orientation="";
    private static String city="";
    private static String zippc="";
    private static String sbmacth="";

    public static void setDataMaps(Map<String, Map> maps) {
        customerMap = maps.get("Customer");
        custLocMap = maps.get("CustomerLocations");
        packageMap = maps.get("CustomerSubscriptions");
        serviceMap = maps.get("CustomerProducts");
        subserviceMap = maps.get("CustomerSubCharges");
        miscchargeMap = maps.get("CustomerMiscCharges");
        contactsMap = maps.get("CustomerContacts");
        addressMap = maps.get("CustomerAddress");
        srvlcMap = maps.get("CustomerSubscriptions");
    }

    public static String extractValue(JSONObject obj, Object key) {
        if (null == obj) return null;
        return (null == obj.get(key)) ? "" : obj.get(key).toString();
    }

    public static void writeFile(String fileName, String className, Class c) {
        try {
            String[] headers;
            Map<String, JSONObject> map;
            Method method = null;
            Class[] params = new Class[2];
            params[0] = int.class;
            params[1] = String.class;

            switch (className) {
                case "Customer":
                    headers = customerHeaders();
                    map = customerMap;
                    method = c.getDeclaredMethod("customerRow", params);
                    break;
                case "CustomerLocations":
                    headers = genLocationsHeaders();
                    map = custLocMap;
                    method = c.getDeclaredMethod("genLocationRow", params);
                    break;
                case "CustomerSubscriptions":
                    headers = genPackageHeaders();
                    map = packageMap;
                    method = c.getDeclaredMethod("genPackageRow", params);
                    break;
                case "CustomerProducts":
                    headers = genServiceHeaders();
                    map = serviceMap;
                    method = c.getDeclaredMethod("genServiceRow", params);
                    break;
                case "CustomerSubCharges":
                    headers = genSubServiceHeaders();
                    map = subserviceMap;
                    method = c.getDeclaredMethod("genSubServiceRow", params);
                    break;
                case "CustomerMiscCharges":
                    headers = genMiscChargesHeaders();
                    map = miscchargeMap;
                    method = c.getDeclaredMethod("genMiscChargesRow", params);
                    break;
                case "CustomerContacts":
                    headers = gencontactsHeaders();
                    map = contactsMap;
                    method = c.getDeclaredMethod("gencontactsRow", params);
                    break;
                case "CustomerAddress":
                    headers = genAddressHeaders();
                    map = addressMap;
                    method = c.getDeclaredMethod("genAddressRow", params);
                    break;
                default:
                    return;
            }

            int ii = 0;
            int jj = 1;
            CSVWriter writer = null;
            writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(fileName + "_" + jj + ".csv"),"ISO-8859-1"), ',');
            // feed in your array (or convert your data to an array)
            writer.writeNext(headers);
            for (String entry : map.keySet()) {
                ii++;
                if(null!=entry){
                    writer.writeNext((String[]) method.invoke(null, ii, entry));
//                if (ii == 5000) {
//                    writer.flush();
//                    ii = 0;
//                    jj++;
//                    writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(fileName + "_" + jj + ".csv"),"ISO-8859-1"), ',');
//                    writer.writeNext(headers);
//                }
                }
            }
            writer.close();
//            writeDB(className);
        } catch (Exception e) {
            System.out.println("writeFile: " + e.getMessage());
            e.getCause().printStackTrace();
        }
    }

    public static void writeDB(String className) {
        try {
            Connection pgconnection;
            pgconnection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/pg_smartbilling", "postgres", "admin");
            switch (className) {
                case "Customer":
                    CustomerBulkInserter customerBulkInserter = new CustomerBulkInserter();
                    customerBulkInserter.saveAll(PostgreSqlUtils.getPGConnection(pgconnection), customerlist.stream());
                    break;
                case "CustomerLocations":
                    CustomerLocationBulkInserter customerlocationBulkInserter = new CustomerLocationBulkInserter();
                    customerlocationBulkInserter.saveAll(PostgreSqlUtils.getPGConnection(pgconnection), customerlocationlist.stream());
                    break;
                case "CustomerAddress":
                    CustomerAddressBulkInserter customeraddressBulkInserter = new CustomerAddressBulkInserter();
                    customeraddressBulkInserter.saveAll(PostgreSqlUtils.getPGConnection(pgconnection), customeraddresslist.stream());
                    break;
                case "CustomerSubscriptions":
                    CustomerSubscriptionBulkInserter customersubscriptionBulkInserter = new CustomerSubscriptionBulkInserter();
                    customersubscriptionBulkInserter.saveAll(PostgreSqlUtils.getPGConnection(pgconnection), customersubscriptionlist.stream());
                    break;
                case "CustomerProducts":
                    CustomerProductBulkInserter customerproductBulkInserter = new CustomerProductBulkInserter();
                    customerproductBulkInserter.saveAll(PostgreSqlUtils.getPGConnection(pgconnection), customerproductlist.stream());
                    break;
                case "CustomerSubCharges":
                    CustomerSubserviceBulkInserter customerSubserviceBulkInserter = new CustomerSubserviceBulkInserter();
                    customerSubserviceBulkInserter.saveAll(PostgreSqlUtils.getPGConnection(pgconnection), customersubservicelist.stream());
                    break;
                case "CustomerMiscCharges":
                    CustomerMiscBulkInsert customerMiscBulkInsert = new CustomerMiscBulkInsert();
                    customerMiscBulkInsert.saveAll(PostgreSqlUtils.getPGConnection(pgconnection), customermisclist.stream());
                    break;
                default:
                    return;
            }
            pgconnection.close();
        } catch (Exception e) {
            System.out.println("writeDB: " + e.getMessage());
            e.getCause().printStackTrace();
        }
    }

    private static String[] customerHeaders() {
        String[] values = new String[15];
        values[0] = "Category";
        values[1] = "T.Name";
        values[2] = "I.UniversalId";
        values[3] = "I.Account";
        values[4] = "I.Name";
        values[5] = "I.PhoneNumber";
        values[6] = "REL.1.RelationName";
        values[7] = "REL.1.Attribute.Responsibility";
        values[8] = "REL.1.Side";
        values[9] = "REL.1.Category";
        values[10] = "REL.1.Identifier";
        values[11] = "REL.2.RelationName";
//        values[12] = "REL.2.Attribute.Responsibility";
        values[12] = "REL.2.Side";
        values[13] = "REL.2.Category";
        values[14] = "REL.2.Identifier";
        return values;
    }

    private static String[] customerRow(int i, String key) {
        String[] values = new String[16];
        Customer c = new Customer();
        try {
            values[0] = "CUSTOMER";
            values[1] = "Client";
            values[2] = extractValue(customerMap.get(key), "accnt_id");
            values[3] = extractValue(customerMap.get(key), "accnt_no");
            values[4] = extractValue(customerMap.get(key), "accnt_name");
            values[5] = extractValue(customerMap.get(key), "phone");
            values[6] = "ResponsibleFor";
            values[7] = "Customer";
            values[8] = "A";
            values[9] = "SERVICE_PROVIDER";
            values[10] = "0";
            values[11] = "Consumes";
//            values[12] = "Site Client";
            values[12] = "Z";
            values[13] = "SERVICE_LOCATION";
            values[14] = extractValue(customerMap.get(key), "location_id");

            c.setAcc_invoice_template_id(extractValue(customerMap.get(key), "acc_invoice_template_id"));
            c.setAccnt_catg_id(extractValue(customerMap.get(key), "accnt_catg_id"));
            c.setAccnt_id(extractValue(customerMap.get(key), "accnt_id"));
            c.setAccnt_name(extractValue(customerMap.get(key), "accnt_name"));
            c.setAccnt_no(extractValue(customerMap.get(key), "accnt_no"));
            c.setAccnt_status_id(extractValue(customerMap.get(key), "accnt_status_id"));
            c.setAccnt_type_id(extractValue(customerMap.get(key), "accnt_type_id"));
            c.setBill_cycle_type_id(extractValue(customerMap.get(key), "bill_cycle_type_id"));
            c.setComp_dba_id(extractValue(customerMap.get(key), "comp_dba_id"));
            c.setComp_id(extractValue(customerMap.get(key), "comp_id"));
            c.setCrcy_code(extractValue(customerMap.get(key), "crcy_code"));
            c.setCreate_date(extractValue(customerMap.get(key), "create_date"));
            c.setCreate_username(extractValue(customerMap.get(key), "crea-datete_username"));
            c.setDeleted_date(extractValue(customerMap.get(key), "deleted"));
            c.setDeleted_username(extractValue(customerMap.get(key), "deleted_username"));
            c.setEmail(extractValue(customerMap.get(key), "email"));
            c.setExternal_id(extractValue(customerMap.get(key), "external_id"));
            c.setFax(extractValue(customerMap.get(key), "fax"));
            c.setField_data_row_id(extractValue(customerMap.get(key), "field_data_row_id"));
            c.setImp_ctl_id(extractValue(customerMap.get(key), "imp_ctl_id"));
            c.setInv_late_fee(extractValue(customerMap.get(key), "inv_late_fee"));
            c.setInv_mthd_type_id(extractValue(customerMap.get(key), "inv_mthd_type_id"));
            c.setInv_start_date(extractValue(customerMap.get(key), "inv_start_date"));
            c.setInv_term_id(extractValue(customerMap.get(key), "inv_term_id"));
            c.setLang_code(extractValue(customerMap.get(key), "lang_code"));
            c.setLocation_id(extractValue(customerMap.get(key), "location_id"));
            c.setMod_date(extractValue(customerMap.get(key), "mod_date"));
            c.setMod_username(extractValue(customerMap.get(key), "mod_username"));
            c.setOwner_department_id(extractValue(customerMap.get(key), "owner_department_id"));
            c.setOwner_sec_user_id(extractValue(customerMap.get(key), "owner_sec_user_id"));
            c.setParent_accnt_id(extractValue(customerMap.get(key), "parent_accnt_id"));
            c.setPhone(extractValue(customerMap.get(key), "phone"));
            c.setPhone_ext(extractValue(customerMap.get(key), "phone_ext"));
            c.setPortal_access(extractValue(customerMap.get(key), "portal_access"));
            c.setRn(extractValue(customerMap.get(key), "rn"));
            c.setSecurity_deposit(extractValue(customerMap.get(key), "security_deposit"));
            c.setSecurity_deposit_bal(extractValue(customerMap.get(key), "security_deposit_bal"));
            c.setSend_ntfy(extractValue(customerMap.get(key), "send_ntfy"));
            customerlist.add(c);
        } catch (Exception e) {
            System.out.println("customerrow: " + e.getMessage());
            e.getCause().printStackTrace();//e.printStackTrace();
        }
        return values;
    }

    private static String[] genPackageHeaders() {
        String[] values = new String[15];
        values[0] = "Category";
        values[1] = "T.Name";
        values[2] = "T.UniversalId";
        values[3] = "I.Name";
        values[4] = "I.UniversalId";
        values[5] = "I.Status";
        values[6] = "REL.1.RelationName";
        values[7] = "REL.1.Attribute.Responsibility";
        values[8] = "REL.1.Side";
        values[9] = "REL.1.Category";
        values[10] = "REL.1.Identifier";
        values[11] = "REL.2.RelationName";
        values[12] = "REL.2.Side";
        values[13] = "REL.2.Category";
        values[14] = "REL.2.Identifier";
        return values;
    }

    private static String[] genPackageRow(int i, String key) {
        String[] values = new String[15];
        CustomerSubscription s = new CustomerSubscription();
        try {
            values[0] = "SB_SUBSCRIPTION";
            values[1] = extractValue(packageMap.get(key), "service_name").replace("/", "-"); // T.Name
            values[2] = extractValue(packageMap.get(key), "service_id"); // T.UniversalId
            values[3] = extractValue(packageMap.get(key), "accnt_service_no"); // I.Name
            values[4] = extractValue(packageMap.get(key), "accnt_service_id"); // I.UniversalId
            values[5] = "In Service"; // Package Status
            values[6] = "ResponsibleFor";
            values[7] = "Customer";
            values[8] = "Z";
            values[9] = "CUSTOMER";
            values[10] = extractValue(packageMap.get(key), "accnt_id"); // Customer_id
            values[11] = "AllocatedTo";
            values[12] = "Z";
            values[13] = "SERVICE_LOCATION";
            values[14] = extractValue(packageMap.get(key), "location_id"); // location_id

            s.setNote(extractValue(packageMap.get(key), "note"));
            s.setAccnt_service_no(extractValue(packageMap.get(key), "accnt_service_no"));
            s.setService_start_date(extractValue(packageMap.get(key), "service_start_date"));
            s.setMod_date(extractValue(packageMap.get(key), "mod_date"));
            s.setLocation_id(extractValue(packageMap.get(key), "location_id"));
            s.setRecurr_bill_cycle_type_id(extractValue(packageMap.get(key), "recurr_bill_cycle_type_id"));
            s.setService_id(extractValue(packageMap.get(key), "service_id"));
            s.setCreate_date(extractValue(packageMap.get(key), "create_date"));
            s.setAccnt_service_id(extractValue(packageMap.get(key), "accnt_service_id"));
            s.setAccnt_id(extractValue(packageMap.get(key), "accnt_id"));
            s.setIdentifier5(extractValue(packageMap.get(key), "identifier5"));
            s.setBill_start_date(extractValue(packageMap.get(key), "bill_start_date"));
            s.setService_end_date(extractValue(packageMap.get(key), "service_end_date"));
            s.setIdentifier1(extractValue(packageMap.get(key), "identifier1"));
            s.setIdentifier2(extractValue(packageMap.get(key), "identifier2"));
            s.setIdentifier3(extractValue(packageMap.get(key), "identifier3"));
            s.setService_name(extractValue(packageMap.get(key), "service_name"));
            s.setIdentifier4(extractValue(packageMap.get(key), "identifier4"));
            s.setComp_id(extractValue(packageMap.get(key), "comp_id"));
            s.setOwner_sec_user_id(extractValue(packageMap.get(key), "owner_sec_user_id"));
            s.setCreate_username(extractValue(packageMap.get(key), "create_username"));
            s.setUsage_bill_cycle_type_id(extractValue(packageMap.get(key), "usage_bill_cycle_type_id"));
            s.setMod_username(extractValue(packageMap.get(key), "mod_username"));
            s.setRn(extractValue(packageMap.get(key), "rn"));
            s.setBill_end_date(extractValue(packageMap.get(key), "bill_end_date"));
            s.setUsage_identifier(extractValue(packageMap.get(key), "usage_identifier"));
            customersubscriptionlist.add(s);
        } catch (Exception e) {
            System.out.println("genPackageRow: " + e.getMessage());
            e.getCause().printStackTrace();//e.printStackTrace();
        }
        return values;
    }

    private static String[] genServiceHeaders() {
        String[] values = new String[12];
        values[0] = "Category";
        values[1] = "T.Name";
        values[2] = "T.UniversalId";
        values[3] = "I.Name";
        values[4] = "I.UniversalId";
        values[5] = "I.Status";
        values[6] = "I.ScheduledStartDate";
        values[7] = "I.ScheduledEndDate";
        values[8] = "REL.1.RelationName";
        values[9] = "REL.1.Side";
        values[10] = "REL.1.Category";
        values[11] = "REL.1.Identifier";
        return values;
    }

    private static String[] genServiceRow(int i, String key) {
        String[] values = new String[12];
        CustomerProduct p = new CustomerProduct();
        try {
            String service_id = extractValue(serviceMap.get(key), "accnt_product_id");
            String serv_pack_id = extractValue(serviceMap.get(key), "accnt_service_id");
            String pack_serv = "";
            pack_serv = (null != serv_pack_id) ? serv_pack_id : "";
            values[0] = "SB_PRODUCT";
            values[1] = extractValue(serviceMap.get(key), "product_name").replace("/", "-");
            values[2] = extractValue(serviceMap.get(key), "product_id");// T.UniversalId
            values[3] = extractValue(serviceMap.get(key), "accnt_prod_no"); // I.UniversalId
            values[4] = service_id; // I.UniversalId
            values[5] = "In Service"; // I.Status
            values[6] = extractValue(serviceMap.get(key), "bill_start_date"); //ScheduledStartDate
            values[7] = extractValue(serviceMap.get(key), "bill_end_date"); //ScheduledEndDate
            values[8] = "ServiceOf";
            values[9] = "Z";
            values[10] = "SB_SUBSCRIPTION";
            values[11] = pack_serv; // PackageId

            p.setAccnt_prod_no(extractValue(serviceMap.get(key), "accnt_prod_no"));
            p.setMod_date(extractValue(serviceMap.get(key), "mod_date"));
            p.setLocation_id(extractValue(serviceMap.get(key), "location_id"));
            p.setRecurr_bill_cycle_type_id(extractValue(serviceMap.get(key), "recurr_bill_cycle_type_id"));
            p.setPrice(extractValue(serviceMap.get(key), "price"));
            p.setProduct_id(extractValue(serviceMap.get(key), "product_id"));
            p.setField_data_row_id(extractValue(serviceMap.get(key), "field_data_row_id"));
            p.setCreate_date(extractValue(serviceMap.get(key), "create_date"));
            p.setAccnt_service_id(extractValue(serviceMap.get(key), "accnt_service_id"));
            p.setAccnt_id(extractValue(serviceMap.get(key), "accnt_id"));
            p.setEffective_from(extractValue(serviceMap.get(key), "effective_from"));
            p.setIdentifier5(extractValue(serviceMap.get(key), "identifier5"));
            p.setSupp_accnt_id(extractValue(serviceMap.get(key), "supp_accnt_id"));
            p.setParent_accnt_product_id(extractValue(serviceMap.get(key), "parent_accnt_product_id"));
            p.setBill_start_date(extractValue(serviceMap.get(key), "bill_start_date"));
            p.setCost(extractValue(serviceMap.get(key), "cost"));
            p.setIdentifier1(extractValue(serviceMap.get(key), "identifier1"));
            p.setIdentifier2(extractValue(serviceMap.get(key), "identifier2"));
            p.setIdentifier3(extractValue(serviceMap.get(key), "identifier3"));
            p.setIdentifier4(extractValue(serviceMap.get(key), "identifier4"));
            p.setAcc_accounting_code_id(extractValue(serviceMap.get(key), "acc_accounting_code_id"));
            p.setAccnt_product_id(extractValue(serviceMap.get(key), "accnt_product_id"));
            p.setEffective_to(extractValue(serviceMap.get(key), "effective_to"));
            p.setImp_ctl_id(extractValue(serviceMap.get(key), "imp_ctl_id"));
            p.setComp_id(extractValue(serviceMap.get(key), "comp_id"));
            p.setProduct_name(extractValue(serviceMap.get(key), "product_name"));
            p.setOwner_sec_user_id(extractValue(serviceMap.get(key), "owner_sec_user_id"));
            p.setCreate_username(extractValue(serviceMap.get(key), "create_username"));
            p.setQty(extractValue(serviceMap.get(key), "qty"));
            p.setMod_username(extractValue(serviceMap.get(key), "mod_username"));
            p.setRn(extractValue(serviceMap.get(key), "rn"));
            p.setBill_end_date(extractValue(serviceMap.get(key), "bill_end_date"));
            customerproductlist.add(p);
        } catch (Exception e) {
            System.out.println("genServiceRow: " + e.getMessage());
            e.getCause().printStackTrace();//e.printStackTrace();
        }
        return values;
    }

    private static String[] genLocationsHeaders() {
        String[] values = new String[16];
        values[0] = "Category";
        values[1] = "T.Name";
        values[2] = "I.UniversalId";
        values[3] = "I.Name";
        values[4] = "REL.1.RelationName";
        values[5] = "REL.1.Side";
        values[6] = "REL.1.Category";
        values[7] = "REL.1.Identifier";
        values[8] = "REL.2.RelationName";
        values[9] = "REL.2.Side";
        values[10] = "REL.2.Category";
        values[11] = "REL.2.Identifier";
        values[12] = "REL.3.RelationName";
        values[13] = "REL.3.Side";
        values[14] = "REL.3.Category";
        values[15] = "REL.3.Identifier";
        return values;
    }

    private static String[] genLocationRow(int i, String key) {
        CustomerLocation cl = new CustomerLocation();
        String[] values = new String[16];
        try {
            String loc_cust_id = extractValue(custLocMap.get(key), "accnt_id");
            String locserv = "";// = extractValue(custLocMap.get(key), "accnt_service_id");
            String addr_loc_id = "";
            String cvc_adr = extractValue(custLocMap.get(key), "addr_line1");
            String addr_id;
            String addr_accnt;
            addr_id = ((null == cvc_adr) || (cvc_adr.equals(""))) ? "" : nameaddress;
            addr_loc_id = extractValue(custLocMap.get(key), "location_id");
            for (String p_entry : srvlcMap.keySet()) {
                addr_accnt = extractValue(packageMap.get(p_entry), "location_id");
                if (addr_accnt.equals(addr_loc_id)) {
                    locserv = extractValue(packageMap.get(p_entry), "accnt_service_id");
//                srvlcMap.remove(p_entry);
                    break;
                }
            }
            values[0] = "SERVICE_LOCATION"; //Category
            values[1] = "Site Client"; //T.Name
            values[2] = extractValue(custLocMap.get(key), "location_id"); // I.UniversalId
            values[3] = extractValue(custLocMap.get(key), "loc_name"); // I.Name
            values[4] = "Consumes"; // REL.1.RelationName
            values[5] = "A"; // REL.1.Side
            values[6] = "CUSTOMER"; // REL.1.Category
            values[7] = extractValue(custLocMap.get(key), "accnt_id"); // REL.1.Identifier
            values[8] = "Localizes"; // REL.1.RelationName
            values[9] = "A"; // REL.1.Side
            values[10] = "CIVIC_ADDRESS"; // REL.1.Category
            values[11] = extractValue(custLocMap.get(key), "location_id"); // REL.1.Identifier
            values[12] = "AllocatedTo";
            values[13] = "Z";
            values[14] = "SB_SUBSCRIPTION";
            values[15] = locserv;

            cl.setZip(extractValue(custLocMap.get(key), "zip"));
            cl.setAddr_line1(extractValue(custLocMap.get(key), "addr_line1"));
            cl.setCity(extractValue(custLocMap.get(key), "city"));
            cl.setCustom_fields(extractValue(custLocMap.get(key), "custom_fields"));
            cl.setImp_ctl_id(extractValue(custLocMap.get(key), "imp_ctl_id"));
            cl.setFull_address(extractValue(custLocMap.get(key), "full_address"));
            cl.setComp_id(extractValue(custLocMap.get(key), "comp_id"));
            cl.setLocation_id(extractValue(custLocMap.get(key), "location_id"));
            cl.setCountry_state(extractValue(custLocMap.get(key), "country_state"));
            cl.setState_name(extractValue(custLocMap.get(key), "state_name"));
            cl.setAttention(extractValue(custLocMap.get(key), "attention"));
            cl.setLoc_name(extractValue(custLocMap.get(key), "loc_name"));
            cl.setAddr_line2(extractValue(custLocMap.get(key), "addr_line2"));
            cl.setRn(extractValue(custLocMap.get(key), "rn"));
            cl.setState_code(extractValue(custLocMap.get(key), "state_code"));
            cl.setCtry_name(extractValue(custLocMap.get(key), "ctry_name"));
            cl.setAccnt_id(extractValue(custLocMap.get(key), "accnt_id"));
            cl.setCtry_code_iso2(extractValue(custLocMap.get(key), "ctry_code_iso2"));
            customerlocationlist.add(cl);
        } catch (Exception e) {
            System.out.println("genLocationRow: " + e.getMessage());
            e.getCause().printStackTrace();//e.printStackTrace();
        }
        return values;
    }

    private static String[] genSubServiceHeaders() {
        String[] values = new String[11];
        values[0] = "Category";
        values[1] = "T.Name";
        values[2] = "I.UniversalId";
        values[3] = "I.Name";
        values[4] = "I.Status";
        values[5] = "I.ScheduledStartDate";
        values[6] = "I.ScheduledEndDate";
        values[7] = "REL.1.RelationName";
        values[8] = "REL.1.Side";
        values[9] = "REL.1.Category";
        values[10] = "REL.1.Identifier";

        return values;
    }

    private static String[] genSubServiceRow(int i, String key) {
        CustomerSubservice ss=new CustomerSubservice();
        String[] values = new String[11];
        try{
        values[0] = "BUSINESS_SERVICE";
        values[1] = "Sub Charges";//extractValue(packageMap.get(key), "product_name"); // T.Name
        values[2] = extractValue(subserviceMap.get(key), "accnt_product_id"); // I.Name
        values[3] = extractValue(subserviceMap.get(key), "accnt_prod_no"); // I.UniversalId
        values[4] = "In Service"; // I.Status
        values[5] = extractValue(subserviceMap.get(key), "bill_start_date");//""; //ScheduledStartDate
        values[6] = extractValue(subserviceMap.get(key), "bill_end_date");//""; //ScheduledEndDate
        values[7] = "ServiceOf";
        values[8] = "Z";
        values[9] = "PACKAGE";
        values[10] = extractValue(subserviceMap.get(key), "accnt_service_id"); // PackageId

        ss.setAccnt_prod_no(extractValue(subserviceMap.get(key), "accnt_prod_no"));
        ss.setMod_date(extractValue(subserviceMap.get(key), "mod_date"));
        ss.setLocation_id(extractValue(subserviceMap.get(key), "location_id"));
        ss.setRecurr_bill_cycle_type_id(extractValue(subserviceMap.get(key), "recurr_bill_cycle_type_id"));
        ss.setPrice(extractValue(subserviceMap.get(key), "price"));
        ss.setProduct_id(extractValue(subserviceMap.get(key), "product_id"));
        ss.setField_data_row_id(extractValue(subserviceMap.get(key), "field_data_row_id"));
        ss.setCreate_date(extractValue(subserviceMap.get(key), "create_date"));
        ss.setAccnt_service_id(extractValue(subserviceMap.get(key), "accnt_service_id"));
        ss.setAccnt_id(extractValue(subserviceMap.get(key), "accnt_id"));
        ss.setEffective_from(extractValue(subserviceMap.get(key), "effective_from"));
        ss.setIdentifier5(extractValue(subserviceMap.get(key), "identifier5"));
        ss.setSupp_accnt_id(extractValue(subserviceMap.get(key), "supp_accnt_id"));
        ss.setParent_accnt_product_id(extractValue(subserviceMap.get(key), "parent_accnt_product_id"));
        ss.setBill_start_date(extractValue(subserviceMap.get(key), "bill_start_date"));
        ss.setCost(extractValue(subserviceMap.get(key), "cost"));
        ss.setIdentifier1(extractValue(subserviceMap.get(key), "identifier1"));
        ss.setIdentifier2(extractValue(subserviceMap.get(key), "identifier2"));
        ss.setIdentifier3(extractValue(subserviceMap.get(key), "identifier3"));
        ss.setIdentifier4(extractValue(subserviceMap.get(key), "identifier4"));
        ss.setAcc_accounting_code_id(extractValue(subserviceMap.get(key), "acc_accounting_code_id"));
        ss.setAccnt_product_id(extractValue(subserviceMap.get(key), "accnt_product_id"));
        ss.setEffective_to(extractValue(subserviceMap.get(key), "effective_to"));
        ss.setImp_ctl_id(extractValue(subserviceMap.get(key), "imp_ctl_id"));
        ss.setComp_id(extractValue(subserviceMap.get(key), "comp_id"));
        ss.setProduct_name(extractValue(subserviceMap.get(key), "product_name"));
        ss.setOwner_sec_user_id(extractValue(subserviceMap.get(key), "owner_sec_user_id"));
        ss.setCreate_username(extractValue(subserviceMap.get(key), "create_username"));
        ss.setQty(extractValue(subserviceMap.get(key), "qty"));
        ss.setMod_username(extractValue(subserviceMap.get(key), "mod_username"));
        ss.setRn(extractValue(subserviceMap.get(key), "rn"));
        ss.setBill_end_date(extractValue(subserviceMap.get(key), "bill_end_date"));
//        System.out.println("subservice key "+key);
        customersubservicelist.add(ss);
        } catch (Exception e) {
            System.out.println("subservice: " + e.getMessage());
            e.getCause().printStackTrace();//e.printStackTrace();
        }
        return values;
    }

    private static String[] genMiscChargesHeaders() {
        String[] values = new String[11];
        values[0] = "Category";
        values[1] = "T.Name";
        values[2] = "I.Name";
        values[3] = "I.UniversalId";
        values[4] = "I.Status";
        values[5] = "I.ScheduledStartDate";
        values[6] = "I.ScheduledEndDate";
        values[7] = "REL.1.RelationName";
        values[8] = "REL.1.Side";
        values[9] = "REL.1.Category";
        values[10] = "REL.1.Identifier";
        return values;
    }

    private static String[] genMiscChargesRow(int i, String key) {
        CustomerMisc sm=new CustomerMisc();
        String[] values = new String[11];
        try{
        values[0] = "BUSINESS_SERVICE";
        values[1] = "Misc Charges";//extractValue(packageMap.get(key), "product_name"); // T.Name
        values[2] = extractValue(miscchargeMap.get(key), "accnt_prod_no"); // I.Name
        values[3] = extractValue(miscchargeMap.get(key), "accnt_product_id"); // I.UniversalId
        values[4] = "In Service"; // I.Status
        values[5] = extractValue(miscchargeMap.get(key), "bill_start_date");//""; //ScheduledStartDate
        values[6] = extractValue(miscchargeMap.get(key), "bill_end_date");//""; //ScheduledEndDate
        values[7] = "ServiceOf";
        values[8] = "Z";
        values[9] = "PACKAGE";
        values[10] = extractValue(miscchargeMap.get(key), "accnt_service_id"); // PackageId

        sm.setAccnt_prod_no(extractValue(miscchargeMap.get(key), "accnt_prod_no"));
        sm.setMod_date(extractValue(miscchargeMap.get(key), "mod_date"));
        sm.setLocation_id(extractValue(miscchargeMap.get(key), "location_id"));
        sm.setRecurr_bill_cycle_type_id(extractValue(miscchargeMap.get(key), "recurr_bill_cycle_type_id"));
        sm.setPrice(extractValue(miscchargeMap.get(key), "price"));
        sm.setProduct_id(extractValue(miscchargeMap.get(key), "product_id"));
        sm.setField_data_row_id(extractValue(miscchargeMap.get(key), "field_data_row_id"));
        sm.setCreate_date(extractValue(miscchargeMap.get(key), "create_date"));
        sm.setAccnt_service_id(extractValue(miscchargeMap.get(key), "accnt_service_id"));
        sm.setAccnt_id(extractValue(miscchargeMap.get(key), "accnt_id"));
        sm.setEffective_from(extractValue(miscchargeMap.get(key), "effective_from"));
        sm.setIdentifier5(extractValue(miscchargeMap.get(key), "identifier5"));
        sm.setSupp_accnt_id(extractValue(miscchargeMap.get(key), "supp_accnt_id"));
        sm.setParent_accnt_product_id(extractValue(miscchargeMap.get(key), "parent_accnt_product_id"));
        sm.setBill_start_date(extractValue(miscchargeMap.get(key), "bill_start_date"));
        sm.setCost(extractValue(miscchargeMap.get(key), "cost"));
        sm.setIdentifier1(extractValue(miscchargeMap.get(key), "identifier1"));
        sm.setIdentifier2(extractValue(miscchargeMap.get(key), "identifier2"));
        sm.setIdentifier3(extractValue(miscchargeMap.get(key), "identifier3"));
        sm.setIdentifier4(extractValue(miscchargeMap.get(key), "identifier4"));
        sm.setAcc_accounting_code_id(extractValue(miscchargeMap.get(key), "acc_accounting_code_id"));
        sm.setAccnt_product_id(extractValue(miscchargeMap.get(key), "accnt_product_id"));
        sm.setEffective_to(extractValue(miscchargeMap.get(key), "effective_to"));
        sm.setImp_ctl_id(extractValue(miscchargeMap.get(key), "imp_ctl_id"));
        sm.setComp_id(extractValue(miscchargeMap.get(key), "comp_id"));
        sm.setProduct_name(extractValue(miscchargeMap.get(key), "product_name"));
        sm.setOwner_sec_user_id(extractValue(miscchargeMap.get(key), "owner_sec_user_id"));
        sm.setCreate_username(extractValue(miscchargeMap.get(key), "create_username"));
        sm.setQty(extractValue(miscchargeMap.get(key), "qty"));
        sm.setMod_username(extractValue(miscchargeMap.get(key), "mod_username"));
        sm.setRn(extractValue(miscchargeMap.get(key), "rn"));
        sm.setBill_end_date(extractValue(miscchargeMap.get(key), "bill_end_date"));
//        System.out.println("misc key "+key);
        customermisclist.add(sm);
        } catch (Exception e) {
            System.out.println("misc: " + e.getMessage());
            e.getCause().printStackTrace();//e.printStackTrace();
        }
        return values;
    }

    private static String[] gencontactsHeaders() {
        String[] values = new String[9];
        values[0] = "Category";
        values[1] = "T.Name";
        values[2] = "I.UniversalId";
        values[3] = "I.Name";
        values[4] = "I.PhoneNumber";
        values[5] = "REL.1.RelationName";
        values[6] = "REL.1.Side";
        values[7] = "REL.1.Category";
        values[8] = "REL.1.Identifier";
        return values;
    }

    private static String[] gencontactsRow(int i, String key) {
        String[] values = new String[9];
        values[0] = "CONTACT_CUSTOMER";
        values[1] = "Contact_Customer";
        values[2] = extractValue(contactsMap.get(key), "accnt_id");
        values[3] = extractValue(contactsMap.get(key), "accnt_name");
        values[4] = extractValue(contactsMap.get(key), "phone");
        values[5] = "Contains";
        values[6] = "A";
        values[7] = "CUSTOMER_HR";
        values[8] = extractValue(contactsMap.get(key), "accnt_id");
        return values;
    }

    private static String[] genAddressHeaders() {
        String[] values = new String[18];
        values[0] = "Category";
        values[1] = "T.Name";
        values[2] = "I.Name";
        values[3] = "I.UniversalId";
        values[4] = "I.City";
        values[5] = "I.Country";
        values[6] = "I.StateProvince";
        values[7] = "I.Street";
        values[8] = "I.Civic";
        values[9] = "I.ZipPostalCode";
        values[10] = "I.Orientation";
        values[11] = "I.Suffix";
        values[12] = "I.Status";
        values[13] = "REL.1.RelationName";
        values[14] = "REL.1.Side";
        values[15] = "REL.1.Category";
        values[16] = "REL.1.Identifier";
        values[17] = "SB.Match";
//        values[14]="REL.2.RelationName";
//        values[15]="REL.2.Side";
//        values[16]="REL.2.Category";
//        values[17]="REL.2.Identifier";
        return values;
    }

    private static String[] genAddressRow(int genA, String key) {
        String[] values = new String[18];
        String addr="";
        String str;
        try {
            city=(null!=extractValue(addressMap.get(key), "city"))?extractValue(addressMap.get(key), "city").replace(" ",""):"";
            zippc=extractValue(addressMap.get(key), "zip").replace(" ","");
            addr = extractValue(addressMap.get(key), "addr_line1").toUpperCase();
            mod_iname(addr);
            values[0] = "CIVIC_ADDRESS";
            values[1] = "Service Address";//extractValue(addressMap.get(key), "loc_name"); // T.Name
            values[2] = nameaddress; // I.Name
            values[3] = extractValue(addressMap.get(key), "location_id");
            values[4] = extractValue(addressMap.get(key), "city");
            values[5] = extractValue(addressMap.get(key), "ctry_name");
            values[6] = extractValue(addressMap.get(key), "state_name");
            values[7] = street;
            values[8] = civic;
            values[9] = zippc;
            values[10] = orientation;
            values[11] = suffix;
            values[12] = "In Service";
//            values[10] = "AllocatedTo";
//            values[11] = "Z";
//            values[12] = "PACKAGE";
//            values[13] = pak_addr;
            values[13] = "Localizes";
            values[14] = "Z";
            values[15] = "SERVICE_LOCATION";//"ADDRESS";
            values[16] = extractValue(addressMap.get(key), "location_id");
            values[17] = sbmacth;

            CustomerAddress a = new CustomerAddress();
            a.setAddr_line1(extractValue(addressMap.get(key), "addr_line1"));
            a.setAddr_line2(extractValue(addressMap.get(key), "addr_line2"));
            a.setAttention(extractValue(addressMap.get(key), "attention"));
            a.setCity(extractValue(addressMap.get(key), "city"));
            a.setCtry_code(extractValue(addressMap.get(key), "ctry_code"));
            a.setLoc_name(extractValue(addressMap.get(key), "loc_name"));
            a.setState_code(extractValue(addressMap.get(key), "state_code"));
            a.setZip(extractValue(addressMap.get(key), "zip"));
            customeraddresslist.add(a);
        } catch (Exception e) {
            System.out.println("address: " + e.getMessage());
            e.getCause().printStackTrace();
        }
        return values;
    }

    private static void mod_iname(String iname) {
        if (iname.contains(" ST ")){
            iname=iname.replace(" ST "," SAINT-");
        }
        if (iname.contains(" ST-")){
            iname=iname.replace(" ST-"," SAINT-");
        }
        if (iname.contains(" STE ")){
            iname=iname.replace(" STE "," SAINTE-");
        }
        if (iname.contains(" STE-")){
            iname=iname.replace(" STE-"," SAINTE-");
        }
        if (iname.contains(" AVENUE ")){
            iname=iname.replace(" AVENUE "," AV ");
        }
        if (iname.contains(" IMPASSE ")){
            iname=iname.replace(" IMPASSE "," IMP ");
        }
        if (iname.contains(" BOULEVARD ")){
            iname=iname.replace(" BOULEVARD "," BOUL ");
        }
        if (iname.contains(" BOUL.")){
            iname=iname.replace(" BOUL.","");
        }
        if (iname.contains("C.P.")){
            iname=iname.replace("C.P."," CP ");
        }
        String[] addr = iname.split(" ");
        List<String> addrlist = Arrays.asList(addr);
        int adr1=0;
        int adr2=0;
        boolean flg01=false;
        boolean flg02=false;
        boolean flg03=false;
        street="";
        try {
            for (String al: addrlist) {
                adr1++;
                if (flg01==false){
                    for (String s : suffixlist) {
                        if (al.equals(s) && flg01==false) {
                            suffix=al;
                            flg01=true;
                            adr2=1;

                        }
                    }
                }

                if (!al.matches("[a-zA-Z]+") && flg02==false)  {
                    civic=al;
                    flg02=true;
                    adr2=2;
                }

                if(flg03==false){
                    if(al.equals("O")||al.equals("E")||al.equals("N") ||al.equals("S")){
                        orientation=al;
                        flg03=true;
                        adr2=3;
                    }
                }

                if((adr2!= 1)||(adr2!= 2)||(adr2!= 3)){
                    street=street+al;
                }
            }

            String city2=city.replace(" ","");
            String street2="";
            street2=street.replace(suffix,"");
            street=street2.replace(civic,"");
            nameaddress=civic+"-"+street;
            sbmacth=city2+suffix+street+civic+zippc+orientation;
        }catch (Exception e){
            System.out.println("mod_name: " + e.getMessage());
            e.getCause().printStackTrace();
        }
    }

}