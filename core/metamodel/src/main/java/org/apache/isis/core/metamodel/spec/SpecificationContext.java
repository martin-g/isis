/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.isis.core.metamodel.spec;

import org.apache.isis.core.commons.authentication.AuthenticationSessionProvider;
import org.apache.isis.core.metamodel.adapter.ServicesProvider;

public class SpecificationContext {

    private final AuthenticationSessionProvider authenticationSessionProvider;
    private final ServicesProvider servicesProvider;
    private final ObjectInstantiator objectInstantiator;
    private final SpecificationLookup specificationLookup;

    public SpecificationContext(final AuthenticationSessionProvider authenticationSessionProvider, final ServicesProvider servicesProvider, final ObjectInstantiator objectInstantiator, final SpecificationLookup specificationLookup) {
        this.authenticationSessionProvider = authenticationSessionProvider;
        this.servicesProvider = servicesProvider;
        this.objectInstantiator = objectInstantiator;
        this.specificationLookup = specificationLookup;
    }

    public AuthenticationSessionProvider getAuthenticationSessionProvider() {
        return authenticationSessionProvider;
    }

    public ServicesProvider getServicesProvider() {
        return servicesProvider;
    }

    public ObjectInstantiator getObjectInstantiator() {
        return objectInstantiator;
    }

    public SpecificationLookup getSpecificationLookup() {
        return specificationLookup;
    }
}