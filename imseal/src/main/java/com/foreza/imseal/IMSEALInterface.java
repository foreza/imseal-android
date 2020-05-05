package com.foreza.imseal;

public interface IMSEALInterface {

    void initSuccess(String sessionId);
    void initFail(String errString);
    void eventLogSuccess();
    void eventLogFailure();


}
