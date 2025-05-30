package com.blpsteam.blpslab1.jca;

import jakarta.resource.spi.ManagedConnectionFactory;

import java.io.Serializable;

public class YookassaConnectionFactoryImpl implements YookassaConnectionFactory, Serializable {

    private final YookassaConnection connection;

    public YookassaConnectionFactoryImpl(ManagedConnectionFactory mcf) {
        if (mcf instanceof YookassaManagedConnectionFactory) {
            YookassaManagedConnectionFactory factory = (YookassaManagedConnectionFactory) mcf;
            this.connection = new YookassaConnectionImpl(factory.getShopId(), factory.getApiKey());
        } else {
            throw new IllegalArgumentException("Expected YookassaManagedConnectionFactory");
        }
    }

    @Override
    public YookassaConnection getConnection() {
        return connection;
    }
}
