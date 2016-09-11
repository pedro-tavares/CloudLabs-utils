package com.cloudlabs.server.dehibernator;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.collection.internal.PersistentBag;
import org.hibernate.collection.internal.PersistentList;
import org.hibernate.collection.internal.PersistentMap;
import org.hibernate.collection.internal.PersistentSet;
import org.hibernate.collection.internal.PersistentSortedSet;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cloudlabs.server.dehibernator.util.ReflectionUtils;

/**
 * Thread-safety: this class in not thread-safe.
 * Adapted from https://code.google.com/p/dehibernator/ by Andrey Minogin
 *
 * @author pedro-tavares
 * @since Jan 2015
 * Adapted all collection types to org.hibernate.collection.internal
 * Changed to perform as Spring @Service   
 */
@Service("dehibernateService")
public class DehibernateService {

	private static Logger LOG = LoggerFactory.getLogger(DehibernateService.class);

	private IdentityHashMap<Object, Object> processed = new IdentityHashMap<Object, Object>();

	@SuppressWarnings("unchecked")
	public <T> T clean(T object) {
		LOG.debug("Cleaning: " + (object != null ? object.getClass() : null));
		return (T) doClean(object);
	}

	@SuppressWarnings("unchecked")
	private Object doClean(Object dirty) {
		LOG.debug("Do clean: " + (dirty != null ? dirty.getClass() : null));

		if (dirty == null)
			return null;

		if (processed.containsKey(dirty)) {
			LOG.debug("Object already cleaned, skipping.");

			return processed.get(dirty);
		}

		if (isPrimitive(dirty)) {
			LOG.debug("Object is primitive, skipping.");

			return dirty;
		}

		if (dirty instanceof PersistentList) {
			LOG.debug("Object is a PersistentList");

			PersistentList dirtyList = (PersistentList) dirty;
			List<Object> cleanList = new ArrayList<Object>();
			processed.put(dirtyList, cleanList); 
			if (dirtyList.wasInitialized()) {
				for (Object value : dirtyList) {
					cleanList.add(doClean(value));
				}
			}
			return cleanList;
		}

		if (dirty instanceof PersistentBag) {
			LOG.debug("Object is a PersistentBag");

			PersistentBag dirtyList = (PersistentBag) dirty;
			List<Object> cleanList = new ArrayList<Object>();
			processed.put(dirtyList, cleanList);
			if (dirtyList.wasInitialized()) {
				for (Object value : dirtyList) {
					cleanList.add(doClean(value));
				}
			}
			return cleanList;
		}

		if (dirty instanceof PersistentSortedSet) {
			LOG.debug("Object is a PersistentSortedSet");

			PersistentSortedSet dirtySet = (PersistentSortedSet) dirty;
			Set<Object> cleanSet = new TreeSet<Object>();
			processed.put(dirtySet, cleanSet);
			if (dirtySet.wasInitialized()) {
				for (Object value : dirtySet) {
					cleanSet.add(doClean(value));
				}
			}
			return cleanSet;
		}
		
		if (dirty instanceof PersistentSet) {
			LOG.debug("Object is a PersistentSet");

			PersistentSet dirtySet = (PersistentSet) dirty;
			Set<Object> cleanSet = new HashSet<Object>();
			processed.put(dirtySet, cleanSet);
			if (dirtySet.wasInitialized()) {
				for (Object value : dirtySet) {
					cleanSet.add(doClean(value));
				}
			}
			return cleanSet;
		}

		
		if (dirty instanceof PersistentMap) {
			LOG.debug("Object is a PersistentMap");

			PersistentMap dirtyMap = (PersistentMap) dirty;
			Map<Object, Object> cleanMap = new LinkedHashMap<Object, Object>();
			processed.put(dirtyMap, cleanMap);
			if (dirtyMap.wasInitialized()) {
				for (Object key : dirtyMap.keySet()) {
					Object value = dirtyMap.get(key);
					cleanMap.put(doClean(key), doClean(value));
				}
			}
			return cleanMap;
		}

		if (dirty instanceof List) {
			LOG.debug("Object is a List");

			List<Object> dirtyList = (List<Object>) dirty;
			List<Object> cleanList = new ArrayList<Object>();
			processed.put(dirtyList, cleanList);
			for (Object value : dirtyList) {
				cleanList.add(doClean(value));
			}
			return cleanList;
		}

		if (dirty instanceof LinkedHashMap) {
			LOG.debug("Object is a LinkedHashMap");

			Map<Object, Object> dirtyMap = (Map<Object, Object>) dirty;
			Map<Object, Object> cleanMap = new LinkedHashMap<Object, Object>();
			processed.put(dirtyMap, cleanMap);
			for (Object key : dirtyMap.keySet()) {
				Object value = dirtyMap.get(key);
				cleanMap.put(doClean(key), doClean(value));
			}
			return cleanMap;
		}

		if (dirty instanceof HashMap) {
			LOG.debug("Object is a HashMap");

			Map<Object, Object> dirtyMap = (Map<Object, Object>) dirty;
			Map<Object, Object> cleanMap = new HashMap<Object, Object>();
			processed.put(dirtyMap, cleanMap);
			for (Object key : dirtyMap.keySet()) {
				Object value = dirtyMap.get(key);
				cleanMap.put(doClean(key), doClean(value));
			}
			return cleanMap;
		}

		if (dirty instanceof LinkedHashSet<?>) {
			LOG.debug("Object is a LinkedHashSet");

			Set<Object> dirtySet = (LinkedHashSet<Object>) dirty;
			Set<Object> cleanSet = new LinkedHashSet<Object>();
			processed.put(dirtySet, cleanSet);
			for (Object value : dirtySet) {
				cleanSet.add(doClean(value));
			}
			return cleanSet;
		}

		if (dirty instanceof HashSet<?>) {
			LOG.debug("Object is a HashSet");

			Set<Object> dirtySet = (HashSet<Object>) dirty;
			Set<Object> cleanSet = new HashSet<Object>();
			processed.put(dirtySet, cleanSet);
			for (Object value : dirtySet) {
				cleanSet.add(doClean(value));
			}
			return cleanSet;
		}

		if (dirty instanceof TreeSet<?>) {
			LOG.debug("Object is a TreeSet");

			Set<Object> dirtySet = (TreeSet<Object>) dirty;
			Set<Object> cleanSet = new TreeSet<Object>();
			processed.put(dirtySet, cleanSet);
			for (Object value : dirtySet) {
				cleanSet.add(doClean(value));
			}
			return cleanSet;
		}

		if (dirty instanceof HibernateProxy) {
			LOG.debug("Object is a HibernateProxy");

			HibernateProxy proxy = (HibernateProxy) dirty;
			LazyInitializer lazyInitializer = proxy.getHibernateLazyInitializer();
			if (lazyInitializer.isUninitialized()) {
				LOG.debug("It is uninitialized, skipping");

				processed.put(dirty, null);
				return null;
			} else {
				LOG.debug("It is initialized, getting implementation");

				dirty = lazyInitializer.getImplementation();
			}
		}

		processed.put(dirty, dirty);
		for (String property : ReflectionUtils.getProperties(dirty)) {
			LOG.debug("setting property {} of {}", property, dirty);

			Object value = ReflectionUtils.get(dirty, property);
			try {
				ReflectionUtils.setIfPossible(dirty, property, doClean(value));
			} catch (Exception e) {
				LOG.error("setting property {} of {}: " + e.getMessage(), property, dirty);
			}
		}
		return dirty;
	}

	public static boolean isPrimitive(Object object) {
		if (object != null) {
			if (object instanceof Boolean || object instanceof Character || object instanceof Byte
					|| object instanceof Short || object instanceof Integer || object instanceof Long
					|| object instanceof Float || object instanceof Double) {
				return true;
			}
			if (object instanceof String) {
				return true;
			}
			if (object instanceof Date) {
				return true;
			}
			if (object instanceof Enum) {
				return true;
			}
			Class<? extends Object> xClass = object.getClass();
			if (xClass.isPrimitive()) {
				return true;
			}
		} 
		return false;
	}

}
