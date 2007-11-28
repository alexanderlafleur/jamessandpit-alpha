package simple.easymock.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.easymock.classextension.EasyMock;

public class SearchAreaTestCase extends TestCase {

    public void testDetermineZone() throws RequestException {
        String suburb = "200";
        String region = "1";
        int zone1 = 2;
        // int postcode = 3;
        int book1 = 4;
        Boolean includeSurround = Boolean.TRUE;
        Integer bookId = new Integer(2);
        Integer stateId = new Integer(3);
        Boolean includeZoned = Boolean.FALSE;

        Map itemSuburbExpansion = createHashMap(region, new int[] {});
        Map itemRegionToZone = createHashMap(region, new int[] { zone1 });
        Map itemPostcodeToBook = createHashMap(new Integer(suburb), new int[] { book1 });
        Map itemSurroundingAreas = createHashMap(new Integer(suburb), new int[] { book1 });
        Map itemSuburbEquate = createHashMap(new Integer(suburb), new int[] { Integer.parseInt(suburb) });

        DataStoreManager mgr = EasyMock.createStrictMock(DataStoreManager.class);

        org.easymock.EasyMock.expect(mgr.getDataStoreItem(DataStoreManager.ITEM_REGION_TO_ZONEKEY)).andReturn(itemRegionToZone);

        org.easymock.EasyMock.expect(mgr.getDataStoreItem(DataStoreManager.ITEM_SUBURB_EXPANSION)).andReturn(itemSuburbExpansion);

        org.easymock.EasyMock.expect(mgr.getDataStoreItem(DataStoreManager.ITEM_POSTCODE_TO_BOOK)).andReturn(itemPostcodeToBook);

        org.easymock.EasyMock.expect(mgr.getDataStoreItem(DataStoreManager.ITEM_SUBURB_EQUATE)).andReturn(itemSuburbEquate);

        org.easymock.EasyMock.expect(mgr.getDataStoreItem(DataStoreManager.ITEM_SURROUNDING_AREAS)).andReturn(itemSurroundingAreas);

        EasyMock.replay(mgr);

        SearchArea.determineZones(mgr, String.valueOf(suburb), bookId, stateId, new Integer(region), includeSurround, includeZoned);

        EasyMock.verify(mgr);

    }

    private Map createHashMap(Object key, int values[]) {
        Map map = new HashMap();

        ArrayList<Integer> arrayList = new ArrayList<Integer>();

        for (int v : values) {
            arrayList.add(new Integer(v));
        }

        map.put(key, arrayList);

        return map;
    }

    // DataStoreManager mgr = new DataStoreManager();
    // mgr.addDataStoreItem(DataStoreManager.ITEM_SUBURB_EXPANSION,
    // createHashMap(region, new int[] {}));
    //
    // mgr.addDataStoreItem(DataStoreManager.ITEM_REGION_TO_ZONEKEY,
    // createHashMap(region, new int[] { zone1 }));
    //
    // mgr.addDataStoreItem(DataStoreManager.ITEM_POSTCODE_TO_BOOK,
    // createHashMap(new Integer(suburb), new int[] { book1 }));
    //
    // mgr.addDataStoreItem(DataStoreManager.ITEM_SURROUNDING_AREAS,
    // createHashMap(new Integer(suburb), new int[] { book1 }));
}
