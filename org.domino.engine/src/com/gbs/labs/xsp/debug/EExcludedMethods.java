package com.gbs.labs.xsp.debug;

public enum EExcludedMethods {
	// TODO: it would be safer, albeit less exciting, if we were inclusive rather than exclusive
	// on what properties we reflected. However, this requires access to the underlying
	// class/property registry that's buried in IBM's packages somewhere.
	// When we know where, we'll likely use it, but until then, we'll just slide down
	// the razor's edge and pick which getters we want to ignore.
	// BOOYAH!!!

	getClass, // called explicitly
	getFacets, // called explicitly
	getData, // called explicitly
	getChildren, // called explicitly
	getTagAttributes, // called explicitly
	getFacetsAndChildren, // suppressed because we call Facets and Children explicitly

	getComponent, // causes infinite loops (and is kinda silly)
	getParent, // causes infinite loops (ditto)
	getDataContainers, // generates JSON errors.
	// Think this contains all the data containers for the whole view in one group,
	// which will prevent them from being reflected in their local containers.
	getDataCache,

	// The following are almost comical. If you include a view, they will cause the serialization
	// of massive amounts of data. It's a good case study in why we should use
	// an inclusion list instead of exclusion.
	// However, it's a whole lot of fun to see just how much reflected information
	// will get sent back. :-)
	getColumnValues,
	getColumnValuesEx,
	getColumns,

	getDataContainer,
	getDataObject,
	getDataContexts,

	getRenderer,
	getRenderKit, // may not cause errors, but reflecting it is probably going to generate a LOT of content!
	getRendererComponent,

	// getRendersChildren,
	// getRendererType,
	// getSelectedIds,
	getGlobalObject, // generates JSON errors
	getTransientMap, // generates JSON errors
	// getLocale, // handled explicitly now to avoid ridiculous loops
	// isTransient,
	// isEnableModifiedFlag,
	// getAttributes,
	// getValue,
	// getView, //Left in there, but it's never going to work because the damn thing is always recycled()
	// getDocument, //ditto
	// getViewScope,
	// getNavigationRules, // generates JSON errors
	// getBeforeRenderResponse,
	// getAfterRenderResponse,
	// getAfterPageLoad,
	// getBeforePageLoad,
	// getValueChangeListeners,
	// getActionListeners,
	isRendering,
	getVars, // not sure why this generates problems.
	// It might have been an empty iterator problem as is therefore safe.
	// getResources, // generates JSON errors
	// getPropertyMap, // generates JSON errors
	// getValidators,

	findScriptCollector, // doesn't hurt anything, just isn't useful

	// BEGIN KNOWN TO CAUSE DRAMATIC SIDE-EFFECTS
	gotoFirstPage,
	gotoPreviousPage,
	gotoNextPage,
	gotoLastPage,
	createPagerState,
	refresh, // can you imagine how expensive that would be? :-)
	getFirst,
	getNext,
	getPrevious,
	getLast,
	notify,
	wait,
	notifyAll,
	finalize,
	find,
	children,
	clone,
	save,
	createUniqueId,
	EMPTY_ITERATOR
	// END KNOWN TO CAUSE DRAMATIC SIDE-EFFECTS
}
