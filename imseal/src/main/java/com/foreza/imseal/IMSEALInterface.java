package com.foreza.imseal;

public interface IMSEALInterface {

    void initSuccess(int sessionId);
    void initFail(String errString);
    void eventLogSuccess();
    void eventLogFailure();


}
