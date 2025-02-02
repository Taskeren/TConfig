package com.github.taskeren.config;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

public class ConfigCategory implements Map<String, Property> {
	private final String name;
	private String comment;
	private final ArrayList<ConfigCategory> children = new ArrayList<>();
	private final Map<String, Property> properties = new TreeMap<>();
	@SuppressWarnings("unused")
	private final int propNumber = 0;
	public final ConfigCategory parent;
	private boolean changed = false;
	private List<String> propertyOrder = null;

	public ConfigCategory(String name) {
		this(name, null);
	}

	public ConfigCategory(String name, ConfigCategory parent) {
		this.name = name;
		this.parent = parent;
		if(parent != null) {
			parent.children.add(this);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ConfigCategory) {
			ConfigCategory cat = (ConfigCategory) obj;
			return name.equals(cat.name) && children.equals(cat.children);
		}

		return false;
	}

	public String getName() {
		return name;
	}

	public String getQualifiedName() {
		return getQualifiedName(name, parent);
	}

	public static String getQualifiedName(String name, ConfigCategory parent) {
		return (parent == null ? name : parent.getQualifiedName() + Configuration.CATEGORY_SPLITTER + name);
	}

	public ConfigCategory getFirstParent() {
		return (parent == null ? this : parent.getFirstParent());
	}

	public boolean isChild() {
		return parent != null;
	}

	public Map<String, Property> getValues() {
		return ImmutableMap.copyOf(properties);
	}

	public List<Property> getOrderedValues() {
		if(this.propertyOrder != null) {
			ArrayList<Property> set = new ArrayList<>();
			for(String key : this.propertyOrder)
				if(properties.containsKey(key))
					set.add(properties.get(key));

			return ImmutableList.copyOf(set);
		} else
			return ImmutableList.copyOf(properties.values());
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getComment() {
		return this.comment;
	}

	public ConfigCategory setPropertyOrder(List<String> propertyOrder) {
		this.propertyOrder = propertyOrder;
		for(String s : properties.keySet())
			if(!propertyOrder.contains(s))
				propertyOrder.add(s);
		return this;
	}

	public List<String> getPropertyOrder() {
		return ImmutableList.copyOf(Objects.requireNonNullElseGet(this.propertyOrder, properties::keySet));
	}

	public boolean containsKey(String key) {
		return properties.containsKey(key);
	}

	public Property get(String key) {
		return properties.get(key);
	}

	private void write(BufferedWriter out, String... data) throws IOException {
		write(out, true, data);
	}

	private void write(BufferedWriter out, boolean newLine, String... data) throws IOException {
		for(String datum : data) {
			out.write(datum);
		}
		if(newLine)
			out.write(Configuration.NEW_LINE);
	}

	public void write(BufferedWriter out, int indent) throws IOException {
		String pad0 = getIndent(indent);
		String pad1 = getIndent(indent + 1);
		String pad2 = getIndent(indent + 2);

		if(comment != null && !comment.isEmpty()) {
			write(out, pad0, Configuration.COMMENT_SEPARATOR);
			write(out, pad0, "# ", name);
			write(out, pad0,
					"#--------------------------------------------------------------------------------------------------------#");
			Splitter splitter = Splitter.onPattern("\r?\n");

			for(String line : splitter.split(comment)) {
				write(out, pad0, "# ", line);
			}

			write(out, pad0, Configuration.COMMENT_SEPARATOR, Configuration.NEW_LINE);
		}

		String displayName = name;

		if(!Configuration.allowedProperties.matchesAllOf(name)) {
			displayName = '"' + name + '"';
		}

		write(out, pad0, displayName, " {");

		Property[] props = getOrderedValues().toArray(new Property[]{});

		for(int x = 0; x < props.length; x++) {
			Property prop = props[x];

			if(prop.getComment() != null && !prop.getComment().isEmpty()) {
				if(x != 0) {
					out.newLine();
				}

				Splitter splitter = Splitter.onPattern("\r?\n");
				for(String commentLine : splitter.split(prop.getComment())) {
					write(out, pad1, "# ", commentLine);
				}
			}

			String propName = prop.getName();

			if(!Configuration.allowedProperties.matchesAllOf(propName)) {
				propName = '"' + propName + '"';
			}

			if(prop.isList()) {
				char type = prop.getType().getID();

				write(out, pad1, String.valueOf(type), ":", propName, " <");

				for(String line : prop.getStringList()) {
					write(out, pad2, line);
				}

				write(out, pad1, " >");
			} else if(prop.getType() == null) {
				write(out, pad1, propName, "=", prop.getString());
			} else {
				char type = prop.getType().getID();
				write(out, pad1, String.valueOf(type), ":", propName, "=", prop.getString());
			}

			prop.resetChangedState();
		}

		if(children.size() > 0)
			out.newLine();

		for(ConfigCategory child : children) {
			child.write(out, indent + 1);
		}

		write(out, pad0, "}", Configuration.NEW_LINE);
	}

	private String getIndent(int indent) {
		return "    ".repeat(Math.max(0, indent));
	}

	public boolean hasChanged() {
		if(changed)
			return true;
		for(Property prop : properties.values()) {
			if(prop.hasChanged())
				return true;
		}
		return false;
	}

	void resetChangedState() {
		changed = false;
		for(Property prop : properties.values()) {
			prop.resetChangedState();
		}
	}

	// Map bouncer functions for compatibility with older mods, to be removed once
	// all mods stop using it.
	@Override
	public int size() {
		return properties.size();
	}

	@Override
	public boolean isEmpty() {
		return properties.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return properties.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return properties.containsValue(value);
	}

	@Override
	public Property get(Object key) {
		return properties.get(key);
	}

	@Override
	public Property put(String key, Property value) {
		changed = true;
		if(this.propertyOrder != null && !this.propertyOrder.contains(key))
			this.propertyOrder.add(key);
		return properties.put(key, value);
	}

	@Override
	public Property remove(Object key) {
		changed = true;
		return properties.remove(key);
	}

	@Override
	public void putAll(@NotNull Map<? extends String, ? extends Property> m) {
		changed = true;
		if(this.propertyOrder != null)
			for(String key : m.keySet())
				if(!this.propertyOrder.contains(key))
					this.propertyOrder.add(key);
		properties.putAll(m);
	}

	@Override
	public void clear() {
		changed = true;
		properties.clear();
	}

	@Override
	public Set<String> keySet() {
		return properties.keySet();
	}

	@Override
	public Collection<Property> values() {
		return properties.values();
	}

	@Override // Immutable copy, changes will NOT be reflected in this category
	public Set<java.util.Map.Entry<String, Property>> entrySet() {
		return ImmutableSet.copyOf(properties.entrySet());
	}

	public Set<ConfigCategory> getChildren() {
		return ImmutableSet.copyOf(children);
	}

	public void removeChild(ConfigCategory child) {
		if(children.contains(child)) {
			children.remove(child);
			changed = true;
		}
	}
}
