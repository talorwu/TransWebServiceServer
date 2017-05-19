
/**
 * TransWebServiceIOExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.7.3  Built on : May 30, 2016 (04:08:57 BST)
 */

package com.trans.service;

public class TransWebServiceIOExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1476431721725L;
    
    private com.trans.service.TransWebServiceStub.TransWebServiceIOException faultMessage;

    
        public TransWebServiceIOExceptionException() {
            super("TransWebServiceIOExceptionException");
        }

        public TransWebServiceIOExceptionException(java.lang.String s) {
           super(s);
        }

        public TransWebServiceIOExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public TransWebServiceIOExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(com.trans.service.TransWebServiceStub.TransWebServiceIOException msg){
       faultMessage = msg;
    }
    
    public com.trans.service.TransWebServiceStub.TransWebServiceIOException getFaultMessage(){
       return faultMessage;
    }
}
    