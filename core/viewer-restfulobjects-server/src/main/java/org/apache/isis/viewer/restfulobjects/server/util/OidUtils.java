/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.apache.isis.viewer.restfulobjects.server.util;

import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.core.metamodel.adapter.oid.Oid;
import org.apache.isis.core.metamodel.adapter.oid.OidMarshaller;
import org.apache.isis.core.metamodel.adapter.oid.RootOidDefault;
import org.apache.isis.core.metamodel.facets.object.viewmodel.ViewModelFacet;
import org.apache.isis.core.metamodel.spec.ObjectSpecId;
import org.apache.isis.core.metamodel.spec.ObjectSpecification;
import org.apache.isis.core.runtime.persistence.ObjectNotFoundException;
import org.apache.isis.core.runtime.persistence.PojoRecreationException;
import org.apache.isis.core.runtime.system.context.IsisContext;
import org.apache.isis.core.runtime.system.persistence.PersistenceSession;
import org.apache.isis.viewer.restfulobjects.rendering.RendererContext;

public final class OidUtils {

    private OidUtils() {
    }

    /**
     * @return {@code null} if not persistent and not a view model.
     */
    public static ObjectAdapter getObjectAdapterElseNull(
            final RendererContext rendererContext,
            final String domainType, final String instanceIdEncoded) {
        final String instanceIdUnencoded = UrlDecoderUtils.urlDecode(instanceIdEncoded);
        String oidStrUnencoded = getOidMarshaller().joinAsOid(domainType, instanceIdUnencoded);
        return getObjectAdapter(rendererContext, oidStrUnencoded);
    }

    /**
     * see {@link #getObjectAdapterElseNull(org.apache.isis.viewer.restfulobjects.rendering.RendererContext, String, String)}
     */
    public static ObjectAdapter getObjectAdapterElseNull(
            final RendererContext rendererContext,
            final String oidStrEncoded) {
        String oidStrUnencoded = UrlDecoderUtils.urlDecode(oidStrEncoded);
        return getObjectAdapter(rendererContext, oidStrUnencoded);
    }

    private static ObjectAdapter getObjectAdapter(
            final RendererContext rendererContext,
            final String oidStrUnencoded) {
        RootOidDefault rootOid = RootOidDefault.deString(oidStrUnencoded, getOidMarshaller());
        final ObjectSpecId specId = rootOid.getObjectSpecId();

        final ObjectSpecification spec = IsisContext.getSpecificationLoader().lookupBySpecId(specId);
        if(spec == null) {
            // eg "NONEXISTENT:123"
            return null;
        }

        // TODO: the logic to figure out which PersistenceSession API to call should be pushed down into PersistenceSession itself.
        final PersistenceSession persistenceSession = rendererContext.getPersistenceSession();

        if(spec.containsFacet(ViewModelFacet.class)) {

            // this is a hack; the RO viewer when rendering the URL for the view model loses the "view model" indicator
            // ("*") from the specId, meaning that the marshalling logic above in RootOidDefault.deString() creates an
            // oid in the wrong state.  The code below checks for this and recreates the oid with the current state of 'view model'
            if(!rootOid.isViewModel()) {
                rootOid = new RootOidDefault(rootOid.getObjectSpecId(), rootOid.getIdentifier(), Oid.State.VIEWMODEL);
            }

            try {
                return persistenceSession.getAdapterManager().adapterFor(rootOid);
            } catch(final ObjectNotFoundException ex) {
                return null;
            } catch(final PojoRecreationException ex) {
                return null;
            }
        } else {
            try {
                ObjectAdapter objectAdapter = persistenceSession.loadObject(rootOid);
                return objectAdapter.isTransient() ? null : objectAdapter;
            } catch(final ObjectNotFoundException ex) {
                return null;
            }
        }
    }

    private static OidMarshaller getOidMarshaller() {
		return new OidMarshaller();
	}

}
