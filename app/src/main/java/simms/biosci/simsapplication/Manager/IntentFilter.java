package simms.biosci.simsapplication.Manager;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PatternMatcher;
import android.util.AndroidException;
import android.util.Printer;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by User 2 on 6/22/2017.
 */

public class IntentFilter implements Parcelable {
    public static final Creator<IntentFilter> CREATOR = null;
    public static final int MATCH_ADJUSTMENT_MASK = 65535;
    public static final int MATCH_ADJUSTMENT_NORMAL = 32768;
    public static final int MATCH_CATEGORY_EMPTY = 1048576;
    public static final int MATCH_CATEGORY_HOST = 3145728;
    public static final int MATCH_CATEGORY_MASK = 268369920;
    public static final int MATCH_CATEGORY_PATH = 5242880;
    public static final int MATCH_CATEGORY_PORT = 4194304;
    public static final int MATCH_CATEGORY_SCHEME = 2097152;
    public static final int MATCH_CATEGORY_SCHEME_SPECIFIC_PART = 5767168;
    public static final int MATCH_CATEGORY_TYPE = 6291456;
    public static final int NO_MATCH_ACTION = -3;
    public static final int NO_MATCH_CATEGORY = -4;
    public static final int NO_MATCH_DATA = -2;
    public static final int NO_MATCH_TYPE = -1;
    public static final int SYSTEM_HIGH_PRIORITY = 1000;
    public static final int SYSTEM_LOW_PRIORITY = -1000;

    public IntentFilter() {
        throw new RuntimeException("Stub!");
    }

    public IntentFilter(String action) {
        throw new RuntimeException("Stub!");
    }

    public IntentFilter(String action, String dataType) throws IntentFilter.MalformedMimeTypeException {
        throw new RuntimeException("Stub!");
    }

    public IntentFilter(IntentFilter o) {
        throw new RuntimeException("Stub!");
    }

    public static IntentFilter create(String action, String dataType) {
        throw new RuntimeException("Stub!");
    }

    public final void setPriority(int priority) {
        throw new RuntimeException("Stub!");
    }

    public final int getPriority() {
        throw new RuntimeException("Stub!");
    }

    public final void addAction(String action) {
        throw new RuntimeException("Stub!");
    }

    public final int countActions() {
        throw new RuntimeException("Stub!");
    }

    public final String getAction(int index) {
        throw new RuntimeException("Stub!");
    }

    public final boolean hasAction(String action) {
        throw new RuntimeException("Stub!");
    }

    public final boolean matchAction(String action) {
        throw new RuntimeException("Stub!");
    }

    public final Iterator<String> actionsIterator() {
        throw new RuntimeException("Stub!");
    }

    public final void addDataType(String type) throws IntentFilter.MalformedMimeTypeException {
        throw new RuntimeException("Stub!");
    }

    public final boolean hasDataType(String type) {
        throw new RuntimeException("Stub!");
    }

    public final int countDataTypes() {
        throw new RuntimeException("Stub!");
    }

    public final String getDataType(int index) {
        throw new RuntimeException("Stub!");
    }

    public final Iterator<String> typesIterator() {
        throw new RuntimeException("Stub!");
    }

    public final void addDataScheme(String scheme) {
        throw new RuntimeException("Stub!");
    }

    public final int countDataSchemes() {
        throw new RuntimeException("Stub!");
    }

    public final String getDataScheme(int index) {
        throw new RuntimeException("Stub!");
    }

    public final boolean hasDataScheme(String scheme) {
        throw new RuntimeException("Stub!");
    }

    public final Iterator<String> schemesIterator() {
        throw new RuntimeException("Stub!");
    }

    public final void addDataSchemeSpecificPart(String ssp, int type) {
        throw new RuntimeException("Stub!");
    }

    public final int countDataSchemeSpecificParts() {
        throw new RuntimeException("Stub!");
    }

    public final PatternMatcher getDataSchemeSpecificPart(int index) {
        throw new RuntimeException("Stub!");
    }

    public final boolean hasDataSchemeSpecificPart(String data) {
        throw new RuntimeException("Stub!");
    }

    public final Iterator<PatternMatcher> schemeSpecificPartsIterator() {
        throw new RuntimeException("Stub!");
    }

    public final void addDataAuthority(String host, String port) {
        throw new RuntimeException("Stub!");
    }

    public final int countDataAuthorities() {
        throw new RuntimeException("Stub!");
    }

    public final IntentFilter.AuthorityEntry getDataAuthority(int index) {
        throw new RuntimeException("Stub!");
    }

    public final boolean hasDataAuthority(Uri data) {
        throw new RuntimeException("Stub!");
    }

    public final Iterator<IntentFilter.AuthorityEntry> authoritiesIterator() {
        throw new RuntimeException("Stub!");
    }

    public final void addDataPath(String path, int type) {
        throw new RuntimeException("Stub!");
    }

    public final int countDataPaths() {
        throw new RuntimeException("Stub!");
    }

    public final PatternMatcher getDataPath(int index) {
        throw new RuntimeException("Stub!");
    }

    public final boolean hasDataPath(String data) {
        throw new RuntimeException("Stub!");
    }

    public final Iterator<PatternMatcher> pathsIterator() {
        throw new RuntimeException("Stub!");
    }

    public final int matchDataAuthority(Uri data) {
        throw new RuntimeException("Stub!");
    }

    public final int matchData(String type, String scheme, Uri data) {
        throw new RuntimeException("Stub!");
    }

    public final void addCategory(String category) {
        throw new RuntimeException("Stub!");
    }

    public final int countCategories() {
        throw new RuntimeException("Stub!");
    }

    public final String getCategory(int index) {
        throw new RuntimeException("Stub!");
    }

    public final boolean hasCategory(String category) {
        throw new RuntimeException("Stub!");
    }

    public final Iterator<String> categoriesIterator() {
        throw new RuntimeException("Stub!");
    }

    public final String matchCategories(Set<String> categories) {
        throw new RuntimeException("Stub!");
    }

    public final int match(ContentResolver resolver, Intent intent, boolean resolve, String logTag) {
        throw new RuntimeException("Stub!");
    }

    public final int match(String action, String type, String scheme, Uri data, Set<String> categories, String logTag) {
        throw new RuntimeException("Stub!");
    }

    public void writeToXml(XmlSerializer serializer) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public void readFromXml(XmlPullParser parser) throws XmlPullParserException, IOException {
        throw new RuntimeException("Stub!");
    }

    public void dump(Printer du, String prefix) {
        throw new RuntimeException("Stub!");
    }

    public final int describeContents() {
        throw new RuntimeException("Stub!");
    }

    public final void writeToParcel(Parcel dest, int flags) {
        throw new RuntimeException("Stub!");
    }

    public static final class AuthorityEntry {
        public AuthorityEntry(String host, String port) {
            throw new RuntimeException("Stub!");
        }

        public String getHost() {
            throw new RuntimeException("Stub!");
        }

        public int getPort() {
            throw new RuntimeException("Stub!");
        }

        public int match(Uri data) {
            throw new RuntimeException("Stub!");
        }
    }

    public static class MalformedMimeTypeException extends AndroidException {
        public MalformedMimeTypeException() {
            throw new RuntimeException("Stub!");
        }

        public MalformedMimeTypeException(String name) {
            throw new RuntimeException("Stub!");
        }
    }
}
