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

package org.apache.isis.core.progmodel.facets.collections.accessor;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.isis.core.metamodel.facetapi.Facet;
import org.apache.isis.core.metamodel.facetapi.FacetHolder;
import org.apache.isis.core.metamodel.facetapi.FacetUtil;
import org.apache.isis.core.metamodel.facetapi.FeatureType;
import org.apache.isis.core.metamodel.facetapi.MethodRemover;
import org.apache.isis.core.metamodel.methodutils.MethodScope;
import org.apache.isis.core.progmodel.facets.MethodPrefixConstants;
import org.apache.isis.core.progmodel.facets.PropertyOrCollectionIdentifyingFacetFactoryAbstract;

public class CollectionAccessorFacetFactory extends PropertyOrCollectionIdentifyingFacetFactoryAbstract {

    private static final String[] PREFIXES = { MethodPrefixConstants.GET_PREFIX };

    public CollectionAccessorFacetFactory() {
        super(FeatureType.COLLECTIONS_ONLY, PREFIXES);
    }

    @Override
    public void process(final ProcessMethodContext processMethodContext) {

        attachAccessorFacetForAccessorMethod(processMethodContext);
    }

    private void attachAccessorFacetForAccessorMethod(final ProcessMethodContext processMethodContext) {
        final Method accessorMethod = processMethodContext.getMethod();
        processMethodContext.removeMethod(accessorMethod);

        final FacetHolder holder = processMethodContext.getFacetHolder();
        final Facet facet = new CollectionAccessorFacetViaAccessor(accessorMethod, holder);
        FacetUtil.addFacet(facet);
    }

    // ///////////////////////////////////////////////////////////////
    // PropertyOrCollectionIdentifyingFacetFactory impl.
    // ///////////////////////////////////////////////////////////////

    @Override
    public boolean isPropertyOrCollectionAccessorCandidate(final Method method) {
        return method.getName().startsWith(MethodPrefixConstants.GET_PREFIX);
    }

    @Override
    public boolean isCollectionAccessor(final Method method) {
        if (!isPropertyOrCollectionAccessorCandidate(method)) {
            return false;
        }
        final Class<?> methodReturnType = method.getReturnType();
        return isCollectionOrArray(methodReturnType);
    }

    /**
     * The method way well represent a reference property, but this facet
     * factory does not have any opinion on the matter.
     */
    @Override
    public boolean isPropertyAccessor(final Method method) {
        return false;
    }

    /**
     * The method way well represent a value property, but this facet factory
     * does not have any opinion on the matter.
     */
    public boolean isValuePropertyAccessor(final Method method) {
        return false;
    }

    @Override
    public void findAndRemoveCollectionAccessors(final MethodRemover methodRemover, final List<Method> methodListToAppendTo) {
        final Class<?>[] collectionClasses = getCollectionTypeRepository().getCollectionType();
        for (final Class<?> returnType : collectionClasses) {
            final List<Method> list = methodRemover.removeMethods(MethodScope.OBJECT, MethodPrefixConstants.GET_PREFIX, returnType, false, 0);
            methodListToAppendTo.addAll(list);
        }
    }

    @Override
    public void findAndRemovePropertyAccessors(final MethodRemover methodRemover, final List<Method> methodListToAppendTo) {
        // does nothing
    }

}
