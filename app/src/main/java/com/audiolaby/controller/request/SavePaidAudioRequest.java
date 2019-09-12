package com.audiolaby.controller.request;


public class SavePaidAudioRequest {

    private String post_id;
    private String packageName;
    private String orderId;
    private String purchaseToken;
    private String os;


    public SavePaidAudioRequest(String os,String post_id, String packageName, String orderId, String purchaseToken) {
        this.os = os;
        this.post_id = post_id;
        this.packageName = packageName;
        this.orderId = orderId;
        this.purchaseToken = purchaseToken;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPurchaseToken() {
        return purchaseToken;
    }

    public void setPurchaseToken(String purchaseToken) {
        this.purchaseToken = purchaseToken;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }
}
