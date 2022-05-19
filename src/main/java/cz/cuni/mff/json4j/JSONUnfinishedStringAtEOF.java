package cz.cuni.mff.json4j;

/**
 * An exception when the JSON ends with an unfinished String (missing the second quotechar).
 * A JSON ending with an unfinished String is either malformed or corrupted.
 * Note that this exception happens even if ANY String in the file is unfinished.
 */
public class JSONUnfinishedStringAtEOF extends Exception {
}
