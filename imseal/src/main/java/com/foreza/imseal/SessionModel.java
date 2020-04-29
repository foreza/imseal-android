package com.foreza.imseal;

public class SessionModel {

    public class SessionPost {
        public String device_id;
        public String isActive;
        public String cellular;
        public String OS;
        public String request_ip;
        public String continent;
        public String country;
        public String city;
        public String date_created;
    }



    public class SessionResponse {
        public String session_id;
    }

}
