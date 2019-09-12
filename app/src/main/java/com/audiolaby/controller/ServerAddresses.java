package com.audiolaby.controller;

public enum ServerAddresses {


    // BASE_URL(BuildConfig.ENV),
    BASE_URL("http://audiolaby.dev-fnode.com"),
    //BASE_URL("http://dev-audiolaby.dev-fnode.com"),
    BASE_APP("/api"),
    BASE_REST(BASE_URL.getValue().concat(BASE_APP.getValue()).concat("/")
    );

    private String value;

    private ServerAddresses(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }


}


