package io.github.hidroh.calendar.adapter;

/**
 * Created by singgih on 10/20/2017.
 */

public class Customer {

    private String cust_id;
    private String cust_name;
    private String cust_st;
    private String cust_phone;
    private String cust_address;
    private String cust_desc;

    private int is_draft;

    public Customer(){

    }

    public Customer(String cust_id,String cust_name, String cust_st, String cust_phone, String cust_address, String cust_desc, int is_draft) {

        this.cust_id = cust_id;
        this.cust_name = cust_name;
        this.cust_st = cust_st;
        this.cust_phone = cust_phone;
        this.cust_address = cust_address;
        this.cust_desc = cust_desc;

        this.is_draft = is_draft;

    }

    public String getCust_id() {
        return cust_id;
    }

    public void setCust_id(String cust_id) {
        this.cust_id = cust_id;
    }

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public String getCust_st() {
        return cust_st;
    }

    public void setCust_st(String cust_st) {
        this.cust_st = cust_st;
    }

    public String getCust_phone() {
        return cust_phone;
    }

    public void setCust_phone(String cust_phone) {
        this.cust_phone = cust_phone;
    }

    public String getCust_address() {
        return cust_address;
    }

    public void setCust_address(String cust_address) {
        this.cust_address = cust_address;
    }

    public String getCust_desc() {
        return cust_desc;
    }

    public void setCust_desc(String cust_desc) {
        this.cust_desc = cust_desc;
    }

    public int getIs_draft() {
        return is_draft;
    }

    public void setIs_draft(int is_draft) {
        this.is_draft = is_draft;
    }

}
