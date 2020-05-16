package com.foreza.imseal;

public interface IMSEALEventListener {

    void initSuccess(int sessionId);
    void initFail(String errString);
    void startEventLogSuccess();
    void startEventLogFail();
    void eventLogSuccess();
    void eventLogFailure();


}
