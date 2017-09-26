package io.github.hidroh.calendar.adapter;


public class Name {
    private String id_tasks;
    private String subject_tasks;
    private String status_tasks;
    private String tanggal_tasks;
    private String waktu_tasks;

    private String outcome_tasks;
    private String customers_tasks;
    private String type_tasks;
    private String description_tasks;

    private int status;

    public Name(){

    }

    public Name(String id_tasks,String subject_tasks, String status_tasks, String tanggal_tasks, String waktu_tasks,
                String outcome_tasks, String customers_tasks, String type_tasks,String description_tasks, int status) {

        this.id_tasks = id_tasks;
        this.subject_tasks = subject_tasks;
        this.status_tasks = status_tasks;
        this.tanggal_tasks = tanggal_tasks;
        this.waktu_tasks = waktu_tasks;

        this.outcome_tasks = outcome_tasks;
        this.customers_tasks = customers_tasks;
        this.type_tasks = type_tasks;
        this.description_tasks = description_tasks;

        this.status = status;
    }

    public String getId_tasks() {
        return id_tasks;
    }
    public void setId_tasks(String id_tasks) {
        this.id_tasks = id_tasks;
    }


    public String getSubject_tasks() {
        return subject_tasks;
    }
    public void setSubject_tasks(String subject_tasks) {
        this.subject_tasks = subject_tasks;
    }


    public String getStatus_tasks() {
        return status_tasks;
    }
    public void setStatus_tasks(String status_tasks) {
        this.status_tasks = status_tasks;
    }


    public String getTanggal_tasks() {
        return tanggal_tasks;
    }
    public void setTanggal_tasks(String tanggal_tasks) {
        this.tanggal_tasks = tanggal_tasks;
    }


    public String getWaktu_tasks() {
        return waktu_tasks;
    }
    public void setWaktu_tasks(String waktu_tasks) {
        this.waktu_tasks = waktu_tasks;
    }


    public String getOutcome_tasks() {
        return outcome_tasks;
    }
    public void setOutcome_tasks(String outcome_tasks) {
        this.outcome_tasks = outcome_tasks;
    }


    public String getCustomers_tasks() {
        return customers_tasks;
    }
    public void setCustomers_tasks(String customers_tasks) {
        this.customers_tasks = customers_tasks;
    }


    public String getType_tasks() {
        return type_tasks;
    }
    public void setType_tasks(String type_tasks) {
        this.type_tasks = type_tasks;
    }


    public String getDescription_tasks() {
        return description_tasks;
    }
    public void setDescription_tasks(String description_tasks) {
        this.description_tasks = description_tasks;
    }

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }


    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return id_tasks;
    }

}
