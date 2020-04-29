package com.foreza.imseal;

public class EventModel {

    class AdRequestEventModel {
        public String session_id;
        public String time_start;
    }

     class AdErrorEventModel {
        public String time_error;
        public String err_message;
    }

     class AdLoadEventModel {
        public String time_load;
        public String success_message;
    }

}
