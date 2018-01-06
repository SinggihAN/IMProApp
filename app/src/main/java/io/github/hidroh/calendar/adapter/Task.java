package io.github.hidroh.calendar.adapter;

/**
 * Created by singgih on 11/6/2017.
 */

public class Task {

    private String task_id;
    private String task_name;
    private String task_priority;
    private String task_type;
    private String task_status;
    private String task_due;
    private String task_outcome;
    private String task_percent;
    private String task_desc;
    private String cust_id;
    private String opp_id;
    private String lead_id;
    private String user_id;
    private String contact_id;
    private int status;

    public Task(){

    }

    public Task(
            String task_id,
            String task_name,
            String task_priority,
            String task_type,
            String task_status,
            String task_due,
            String task_outcome,
            String task_percent,
            String task_desc,
            String cust_id,
            String opp_id,
            String lead_id,
            String user_id,
            String contact_id,
            int status
    ) {

        this.task_id = task_id;
        this.task_name = task_name;
        this.task_priority = task_priority;
        this.task_type = task_type;
        this.task_status = task_status;
        this.task_due = task_due;
        this.task_outcome = task_outcome;
        this.task_percent = task_percent;
        this.task_desc = task_desc;
        this.cust_id = cust_id;
        this.opp_id = opp_id;
        this.lead_id = lead_id;
        this.user_id = user_id;
        this.contact_id = contact_id;
        this.status = status;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getTask_priority() {
        return task_priority;
    }

    public void setTask_priority(String task_priority) {
        this.task_priority = task_priority;
    }

    public String getTask_type() {
        return task_type;
    }

    public void setTask_type(String task_type) {
        this.task_type = task_type;
    }

    public String getTask_status() {
        return task_status;
    }

    public void setTask_status(String task_status) {
        this.task_status = task_status;
    }

    public String getTask_due() {
        return task_due;
    }

    public void setTask_due(String task_due) {
        this.task_due = task_due;
    }

    public String getTask_outcome() {
        return task_outcome;
    }

    public void setTask_outcome(String task_outcome) {
        this.task_outcome = task_outcome;
    }

    public String getTask_percent() {
        return task_percent;
    }

    public void setTask_percent(String task_percent) {
        this.task_percent = task_percent;
    }

    public String getTask_desc() {
        return task_desc;
    }

    public void setTask_desc(String task_desc) {
        this.task_desc = task_desc;
    }

    public String getCust_id() {
        return cust_id;
    }

    public void setCust_id(String cust_id) {
        this.cust_id = cust_id;
    }

    public String getOpp_id() {
        return opp_id;
    }

    public void setOpp_id(String opp_id) {
        this.opp_id = opp_id;
    }

    public String getLead_id() {
        return lead_id;
    }

    public void setLead_id(String lead_id) {
        this.lead_id = lead_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getContact_id() {
        return contact_id;
    }

    public void setContact_id(String contact_id) {
        this.contact_id = contact_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
