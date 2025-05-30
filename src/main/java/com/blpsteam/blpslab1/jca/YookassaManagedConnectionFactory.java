package com.blpsteam.blpslab1.jca;

import jakarta.resource.ResourceException;
import jakarta.resource.spi.*;

import javax.security.auth.Subject;
import javax.transaction.xa.XAResource;
import java.io.PrintWriter;

@ConnectionDefinition(
        connectionFactory = YookassaConnectionFactory.class,
        connectionFactoryImpl = YookassaConnectionFactoryImpl.class,
        connection = YookassaConnection.class,
        connectionImpl = YookassaConnectionImpl.class
)
public class YookassaManagedConnectionFactory implements ManagedConnectionFactory {

    private String shopId;
    private String apiKey;

    @Override
    public Object createConnectionFactory(ConnectionManager cxManager) throws ResourceException {
        return new com.blpsteam.blpslab1.jca.YookassaConnectionFactoryImpl(this);
    }

    @Override
    public Object createConnectionFactory() throws ResourceException {
        return new com.blpsteam.blpslab1.jca.YookassaConnectionFactoryImpl(this);
    }

    @Override
    public ManagedConnection createManagedConnection(Subject subject, ConnectionRequestInfo cxRequestInfo) throws ResourceException {
        return new ManagedConnection() {
            private final YookassaConnection connection = new YookassaConnectionImpl(shopId, apiKey);

            @Override
            public Object getConnection(Subject subject, ConnectionRequestInfo cxRequestInfo) {
                return connection;
            }

            @Override public void destroy() {}
            @Override public void cleanup() {}
            @Override public void associateConnection(Object connection) {}

            @Override
            public void addConnectionEventListener(ConnectionEventListener connectionEventListener) {

            }

            @Override
            public void removeConnectionEventListener(ConnectionEventListener connectionEventListener) {

            }

            //            @Override public void addConnectionEventListener(javax.resource.spi.ConnectionEventListener listener) {}
//            @Override public void removeConnectionEventListener(javax.resource.spi.ConnectionEventListener listener) {}
            @Override public XAResource getXAResource() { return null; }

            @Override
            public LocalTransaction getLocalTransaction() throws ResourceException {
                return null;
            }

            @Override
            public ManagedConnectionMetaData getMetaData() throws ResourceException {
                return null;
            }

            @Override public java.io.PrintWriter getLogWriter() { return null; }
            @Override public void setLogWriter(PrintWriter out) {}
        };
    }

    @Override
    public ManagedConnection matchManagedConnections(java.util.Set set, Subject subject, ConnectionRequestInfo info) {
        return null;
    }

    // Getters/Setters for ra.xml
    public String getShopId() { return shopId; }
    public void setShopId(String shopId) { this.shopId = shopId; }
    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }

    @Override public PrintWriter getLogWriter() { return null; }
    @Override public void setLogWriter(PrintWriter out) {}
}
