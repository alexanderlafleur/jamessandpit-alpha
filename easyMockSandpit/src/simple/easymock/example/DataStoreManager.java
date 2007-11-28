package simple.easymock.example;

import java.util.HashMap;

public class DataStoreManager {

    public static final String ITEM_SURROUNDING_AREAS = "ITEM_SURROUNDING_AREAS";

    public static final String ITEM_SUBURB_EXPANSION = "ITEM_SUBURB_EXPANSION";

    public static final String ITEM_BOOK_TO_STATE = "ITEM_BOOK_TO_STATE";

    public static final String ITEM_POSTCODE_TO_BOOK = "ITEM_POSTCODE_TO_BOOK";

    public static final String POSTCODE_TO_TOURISM_ID = "POSTCODE_TO_TOURISM_ID";

    public static final String TOURISM_ID_TO_STATE = "TOURISM_ID_TO_STATE";

    public static final String ITEM_REGION_TO_ZONEKEY = "ITEM_REGION_TO_ZONEKEY";

    public static final String ITEM_SUBURB_EQUATE = "ITEM_SUBURB_EQUATE";

    private HashMap data = new HashMap();

    public Object getDataStoreItem(String key) {
        return data.get(key);
    }

    public void addDataStoreItem(String key, Object value) {
        data.put(key, value);
    }
}