package com.intacta.sosviagens.Beans;

public class Comment extends Road{


    public Comment(String comment, String user, String data, String cortesy, String time, String opinion, String id) {
        this.comment = comment;
        this.user = user;
        this.data = data;
        this.cortesy = cortesy;
        this.time = time;
        this.opinion = opinion;
        this.id = id;
    }


    public Comment() {
     }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }



    private String comment;
    private String user;
    private String data;
    private String cortesy;

    public String getCortesy() {
        return cortesy;
    }

    public void setCortesy(String cortesy) {
        this.cortesy = cortesy;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String time;

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    private String opinion;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;



 }
