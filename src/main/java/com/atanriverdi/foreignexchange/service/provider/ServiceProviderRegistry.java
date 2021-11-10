package com.atanriverdi.foreignexchange.service.provider;

public interface ServiceProviderRegistry {

    ServiceProvider getServiceBean(String providerType);
}
