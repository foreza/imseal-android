package com.foreza.imseal.models;

import java.util.Date;

public class AdEventModel {

    public static final String DEFAULT_REASON_STRING = "No reason provided";

    public class IMSEALAdRequestEvent {
        int session_id;
        Date timestamp;
//
//        public IMSEALAdRequestEvent(int session_id) {
//            timestamp = new Date();
//            this.session_id = session_id;
//        }

    }

    public class IMSEALAdResponseEvent {
        public String event_id;
    }

    public class IMSEALAdLoadEvent {
        final int type = 1;
        int event_id;
        Date timestamp;

        IMSEALAdLoadEvent (int event_id){
            timestamp = new Date();
            this.event_id = event_id;
        }

    }

    public class IMSEALAdNoFillEvent {
        final int type = 2;
        int event_id;
        Date timestamp;
        String reason_string;


        IMSEALAdNoFillEvent (int event_id){
            timestamp = new Date();
            this.event_id = event_id;
            reason_string = "no reason provided";
        }

        IMSEALAdNoFillEvent (int event_id, String reason_string){
            timestamp = new Date();
            this.event_id = event_id;
            this.reason_string = reason_string;
        }

    }



}
