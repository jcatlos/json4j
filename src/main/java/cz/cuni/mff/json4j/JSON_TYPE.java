package cz.cuni.mff.json4j;

/**
 * Represents a JSON type.
 * As of RFC-8259, a JSON value can be either a String, Number, JSON Object,
 * JSON Array, a Boolean ('true' / 'false') or 'null' (quotes are used to)
 * signify the precise token used for representation.
 */
public enum JSON_TYPE {STRING, NUMBER, BOOLEAN, OBJECT, ARRAY, NULL}
