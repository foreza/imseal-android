package com.foreza.imseal;

public class LocationModel {
    public String ip;
    public String continent;
    public String country;
    public String city;
    public String latitude;
    public String longitude;
    public String region;
    public int completed_requests;
}


// Sample response from API:

/*

{
        "ip": "XXXXXXXX",
        "success": true,
        "type": "IPv4",
        "continent": "North America",
        "continent_code": "NA",
        "country": "United States",
        "country_code": "US",
        "country_flag": "https://cdn.ipwhois.io/flags/us.svg",
        "country_capital": "Washington",
        "country_phone": "+1",
        "country_neighbours": "CA,MX,CU",
        "region": "California",
        "city": "Santa Monica",
        "latitude": "",
        "longitude": "",
        "asn": "AS20001",
        "org": "Charter Communications",
        "isp": "Charter Communications",
        "timezone": "America/Los_Angeles",
        "timezone_name": "Pacific Standard Time",
        "timezone_dstOffset": "0",
        "timezone_gmtOffset": "-28800",
        "timezone_gmt": "GMT -8:00",
        "currency": "US Dollar",
        "currency_code": "USD",
        "currency_symbol": "$",
        "currency_rates": "1",
        "currency_plural": "US dollars",
        "completed_requests": 19
        }

        */
