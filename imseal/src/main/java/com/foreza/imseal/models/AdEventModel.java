package com.foreza.imseal.models;

import java.util.Date;

public class AdEventModel {

    public class IMSEALAdRequestEvent {
        int session_id;
        Date timestamp;
    }

    public class IMSEALAdResponseEvent {
        public String event_id;
    }

    public class IMSEALAdLoadEvent {
        final int type = 1;
        int event_id;
        Date timestamp;
    }

    public class IMSEALAdNoFillEvent {
        final int type = 2;
        int event_id;
        Date timestamp;
        String reason_string;
    }



}
