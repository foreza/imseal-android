package com.foreza.imseal;

public class EndpointConfigs {
    private String _location_api_url = "http://free.ipwhois.io/";
    private String _seal_api_url = "";

    public EndpointConfigs(){ }

    public String getLocationAPIEndpoint() {
        return _location_api_url;
    }
    public String getSEALAPIEndpoint() {
        return _seal_api_url;
    }
}

