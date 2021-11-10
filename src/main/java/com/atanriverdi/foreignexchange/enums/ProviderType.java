package com.atanriverdi.foreignexchange.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public @Getter
@AllArgsConstructor
enum ProviderType {

    FRANKFURTER("com.atanriverdi.foreignexchange.service.impl.FrankfurterServiceProvider"),
    FOREXIO("com.atanriverdi.foreignexchange.service.impl.FrankfurterServiceProvider.ForexioServiceProvider"),
    DEFAULT("com.atanriverdi.foreignexchange.service.impl.FrankfurterServiceProvider.DefaultProvider");

    String value;
}
