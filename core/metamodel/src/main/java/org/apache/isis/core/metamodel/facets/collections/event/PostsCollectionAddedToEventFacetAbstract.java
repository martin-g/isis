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

package org.apache.isis.core.metamodel.facets.collections.event;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.services.eventbus.CollectionAddedToEvent;
import org.apache.isis.applib.services.eventbus.PropertyChangedEvent;
import org.apache.isis.core.metamodel.facetapi.Facet;
import org.apache.isis.core.metamodel.facetapi.FacetHolder;
import org.apache.isis.core.metamodel.facets.SingleValueFacetAbstract;
import org.apache.isis.core.metamodel.facets.collections.modify.CollectionAddToFacet;


public abstract class PostsCollectionAddedToEventFacetAbstract 
    extends SingleValueFacetAbstract<Class<? extends CollectionAddedToEvent<?,?>>> 
    implements PostsCollectionAddedToEventFacet {

	public static Class<? extends Facet> type() {
	    
	    // the "primary" type is CollectionAddToFacet rather than PostsAddedToCollectionEventFacet
	    // so that this facet can wrap an existing (via setUnderlying).
	    
        //return PostsAddedToCollectionEventFacet.class;
	    return CollectionAddToFacet.class;
    }

    public PostsCollectionAddedToEventFacetAbstract(
            final Class<? extends CollectionAddedToEvent<?, ?>> changedEventType, 
            final FacetHolder holder) {
        super(type(), changedEventType, holder);
    }


}