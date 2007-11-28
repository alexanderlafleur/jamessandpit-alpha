package simple.easymock.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public final class SearchArea {
    private static final int NUMBER_OF_STATES = 8;

    /**
     * Utility constructor.
     */
    private SearchArea() {
    }

    //
    // Private constants
    //

    private static final int POSTCODE_LOWER_BOUND = 100;

    private static final int POSTCODE_UPPER_BOUND = 9999;

    private static final int RANGE_POSTCODE = 0;

    private static final int RANGE_BOOK = 0;

    private static final int RANGE_TOURISM = 0;

    private static final int RANGE_STATE = 0;

    private static final int ZONEKEY_NATIONAL = 0;

    private static final int PARAM_SEARCH_AREA_ALL_STATES = 0;

    //
    // Public constants
    //

    //
    // Private variables
    //

    //
    // Private methods
    //

    /**
     * Expands the zone list by applying the surrounding areas.
     * 
     * @param in_dataStoreMgr
     *            a Datastore manager to use to fetch lookups from
     * @param in_zoneList
     *            the zonelist to apply logic on
     * @return the expanded zone list
     */
    protected static ArrayList addSurroundingAreas(DataStoreManager in_dataStoreMgr, List<Integer> in_zoneList) {
        HashMap surroundingAreas;
        ArrayList<Integer> output, surroundPostcodeList;
        Integer thisZone, thisPostcode, newZoneKey;

        // Init from existing list
        output = new ArrayList<Integer>(in_zoneList);

        // Retrieve the surrounding areas map
        surroundingAreas = (HashMap) in_dataStoreMgr.getDataStoreItem(DataStoreManager.ITEM_SURROUNDING_AREAS);

        // Loop through the existing list of zones adding surrounding areas for
        // each entry
        for (int i = 0; i < in_zoneList.size(); i++) {
            thisZone = (Integer) in_zoneList.get(i);

            // If this zone is a postcode, add the surrounding
            if (isZoneKeyInRange(thisZone.intValue(), RANGE_POSTCODE)) {
                // Grab the surrounding zone list
                thisPostcode = new Integer(convertFromZoneKey(thisZone.intValue(), RANGE_POSTCODE));
                surroundPostcodeList = (ArrayList<Integer>) surroundingAreas.get(thisPostcode);
                if (surroundPostcodeList != null) {
                    // Add each item
                    for (int j = 0; j < surroundPostcodeList.size(); j++) {
                        newZoneKey = new Integer(convertToZoneKey(((Integer) surroundPostcodeList.get(j)).intValue(), RANGE_POSTCODE));
                        output.add(newZoneKey);
                    }
                }
            }
        }

        // Return
        return output;
    }

    /**
     * apply suburb expansion to a suburb.
     * 
     * @param in_dataStoreMgr
     *            a DataStoreManager used to fetch lookups
     * @param in_suburbClue
     *            the suburb to expand
     * @return the expanded suburb name
     */
    private static String applySuburbExpansion(DataStoreManager in_dataStoreMgr, String in_suburbClue) {
        HashMap locationMap;
        String location, postcodes, suburbClue;

        // Init
        suburbClue = in_suburbClue;

        // Grab the expansion map
        locationMap = (HashMap) in_dataStoreMgr.getDataStoreItem(DataStoreManager.ITEM_SUBURB_EXPANSION);

        // Loop through the set of locations text as necessary
        for (Iterator entryIter = locationMap.entrySet().iterator(); entryIter.hasNext();) {
            // Grab this location
            Map.Entry entry = (Map.Entry) entryIter.next();
            location = (String) entry.getKey();

            // Loop back if not in the user's clue (check the original for
            // speed)
            if (in_suburbClue.indexOf(location) < 0) {
                continue;
            }

            // We found it. Grab the set of postcodes for this location and
            // replace
            postcodes = (String) entry.getValue();
        }

        return suburbClue;
    }

    /**
     * Adds zoned areas (book for postcode, state for book, national).
     * 
     * @param in_dataStoreMgr
     *            a DataStoreManager used to fetch lookups
     * @param in_zoneList
     *            the initial set of zones
     * @return A list of the possible paid zones given the input zones
     */
    private static ArrayList determinePaidZones(DataStoreManager in_dataStoreMgr, ArrayList in_zoneList) {
        int zoneKey, zoneValue, newZoneValue;
        ArrayList output, newZoneList;
        HashMap postcodeToBook, bookToState;

        // Initialise to include all input zones to start
        output = new ArrayList(in_zoneList);

        // Grab the book to state and postcode to book maps
        bookToState = (HashMap) in_dataStoreMgr.getDataStoreItem(DataStoreManager.ITEM_BOOK_TO_STATE);
        postcodeToBook = (HashMap) in_dataStoreMgr.getDataStoreItem(DataStoreManager.ITEM_POSTCODE_TO_BOOK);
        HashMap postcodeToTourismId = (HashMap) in_dataStoreMgr.getDataStoreItem(DataStoreManager.POSTCODE_TO_TOURISM_ID);
        HashMap tourismIdToState = (HashMap) in_dataStoreMgr.getDataStoreItem(DataStoreManager.TOURISM_ID_TO_STATE);

        // Add the book zones which encompass the set of postcodes
        for (int i = output.size() - 1; i >= 0; i--) {
            // Grab this zone
            zoneKey = ((Integer) output.get(i)).intValue();

            // If its a postcode
            if (isZoneKeyInRange(zoneKey, RANGE_POSTCODE)) {
                // Find the set of books for this postcode
                zoneValue = convertFromZoneKey(zoneKey, RANGE_POSTCODE);
                newZoneList = (ArrayList) postcodeToBook.get(new Integer(zoneValue));
                if (newZoneList != null) {
                    // Add the books
                    for (int j = 0; j < newZoneList.size(); j++) {
                        newZoneValue = ((Integer) newZoneList.get(j)).intValue();
                        output.add(new Integer(convertToZoneKey(newZoneValue, RANGE_BOOK)));
                    }
                }
                newZoneList = (ArrayList) postcodeToTourismId.get(new Integer(zoneValue));

                if (newZoneList != null) {
                    // Add the books
                    for (int j = 0; j < newZoneList.size(); j++) {
                        newZoneValue = ((Integer) newZoneList.get(j)).intValue();
                        output.add(new Integer(convertToZoneKey(newZoneValue, RANGE_TOURISM)));
                    }
                }
            }
        }

        // Add the state zones which encompass the set of books
        for (int i = output.size() - 1; i >= 0; i--) {
            // Grab this zone
            zoneKey = ((Integer) output.get(i)).intValue();

            // If its a book
            if (isZoneKeyInRange(zoneKey, RANGE_BOOK)) {
                // Find the set of states for this book
                zoneValue = convertFromZoneKey(zoneKey, RANGE_BOOK);
                newZoneList = (ArrayList) bookToState.get(new Integer(zoneValue));
                if (newZoneList != null) {
                    // Add the states
                    for (int j = 0; j < newZoneList.size(); j++) {
                        newZoneValue = ((Integer) newZoneList.get(j)).intValue();
                        output.add(new Integer(convertToZoneKey(newZoneValue, RANGE_STATE)));
                    }
                }
            }
            // if it's a tourism zone
            if (isZoneKeyInRange(zoneKey, RANGE_TOURISM)) {
                // Find the set of states for this book
                zoneValue = convertFromZoneKey(zoneKey, RANGE_TOURISM);
                newZoneList = (ArrayList) tourismIdToState.get(new Integer(zoneValue));
                if (newZoneList != null) {
                    // Add the states
                    for (int j = 0; j < newZoneList.size(); j++) {
                        newZoneValue = ((Integer) newZoneList.get(j)).intValue();
                        output.add(new Integer(convertToZoneKey(newZoneValue, RANGE_STATE)));
                    }
                }
            }
        }

        // Deduplicate
        output = new ArrayList(new HashSet(output));

        // Add the national zone
        output.add(new Integer(ZONEKEY_NATIONAL));

        return output;
    }

    /**
     * Determine a set of physical zones for a search area.
     * 
     * @param in_dataStoreMgr
     *            A DataStoreManager used to do lookups
     * @param in_suburbClue
     *            the suburb part of the search area
     * @param in_bookId
     *            the book part of the search area
     * @param in_stateId
     *            the state part of the search area
     * @param in_regionId
     *            the region part of the search area
     * @return a List of zones which match the physical search area
     * @throws RequestException
     */
    private static ArrayList determinePhysicalZones(DataStoreManager in_dataStoreMgr, String in_suburbClue, Integer in_bookId, Integer in_stateId, Integer in_regionId)
            throws RequestException {
        ArrayList<Integer> zoneList, stateList, postcodeList, regionZoneList;
        String suburbClue;
        HashSet regionZoneLookup;
        Integer currentPostcode, currentPostcodeZonekey, bookId, stateId;

        bookId = in_bookId;
        stateId = in_stateId;

        // Init
        zoneList = new ArrayList<Integer>();

        // Preparse
        if ((stateId != null) && (stateId.intValue() == -1)) {
            stateId = null;
        }
        if ((bookId != null) && (bookId.intValue() == -1)) {
            bookId = null;
        }

        // Determine state list
        stateList = determineStateList(stateId);

        // Determine region list
        regionZoneList = determineRegionList(in_dataStoreMgr, in_regionId);
        regionZoneLookup = new HashSet(regionZoneList);

        // Apply the suburb expansion file
        suburbClue = in_suburbClue;
        if (suburbClue != null) {
            suburbClue = suburbClue.toLowerCase();
            suburbClue = applySuburbExpansion(in_dataStoreMgr, suburbClue);
        }

        // Check if the user has NOT specified a suburb/postcode
        if ("".equals(suburbClue)) {
            // We don't have a postcode - book OR state is the search area

            // unless region is the search area
            if (regionZoneList.size() == 0) {
                // Add the book
                if (bookId != null) {
                    zoneList.add(new Integer(convertToZoneKey(bookId.intValue(), RANGE_BOOK)));
                } else {
                    // Add the state(s)
                    for (int i = 0; i < stateList.size(); i++) {
                        zoneList.add(new Integer(convertToZoneKey(((Integer) stateList.get(i)).intValue(), RANGE_STATE)));
                    }
                }
            } else {
                // Add the region list
                zoneList.addAll(regionZoneList);
            }
        } else {
            // The search area is defined by the postcode, with the
            // book/state/region forming a boundary in which the postcode must
            // fall

            // Convert suburb clue to list of postcodes
            postcodeList = processSuburbClue(suburbClue, bookId, stateList, in_dataStoreMgr);

            // Convert postcode list to zonekeys and add to zone list
            for (int i = 0; i < postcodeList.size(); i++) {
                currentPostcode = (Integer) postcodeList.get(i);
                currentPostcodeZonekey = new Integer(convertToZoneKey(currentPostcode.intValue(), RANGE_POSTCODE));

                // if we have a search region
                if (regionZoneList.size() != 0) {
                    // only add the postcode zonekey if it exists in the regions
                    // list of zonekeys
                    if (regionZoneLookup.contains(currentPostcodeZonekey)) {
                        zoneList.add(currentPostcodeZonekey);
                    }
                } else // we don't have a search region, so no need to check if
                // the postcode is in the region simply add it
                {
                    zoneList.add(currentPostcodeZonekey);
                }
            }
        }

        // Deduplicate the zone list
        zoneList = new ArrayList(new HashSet(zoneList));

        // Verify we found at least some zones
        if (zoneList.size() == 0) {
            throw new RequestException("Unable to establish search area", RequestException.ERRORCODE_INVALID_SEARCH_AREA);
        }

        return zoneList;
    }

    /**
     * Determines the set of states for a given stateId. <p/> This deals with the special stateId "9" which is all states by adding states 1->8.
     * 
     * @param in_stateId
     *            the state to determine the list from
     * @return the list of states which match the state ID passed in
     */
    private static ArrayList<Integer> determineStateList(Integer in_stateId) {
        ArrayList<Integer> stateList;

        // Init
        stateList = new ArrayList<Integer>();

        // If we have a state clue
        if (in_stateId != null) {
            // Check if it is all states
            if (in_stateId.intValue() == PARAM_SEARCH_AREA_ALL_STATES) {
                // Add all states
                for (int i = 1; i <= NUMBER_OF_STATES; i++) {
                    stateList.add(new Integer(i));
                }
            } else {
                // Just add this state
                stateList.add(in_stateId);
            }
        }

        return stateList;
    }

    /**
     * Determines the set of zones for a given region id.
     * 
     * @param in_dataStoreMgr
     *            a DataStoreManager to grab lookup tables from.
     * @param in_regionId
     *            the region id to get zones for
     * @return the List of zones for the regionId passed in
     */
    private static ArrayList determineRegionList(DataStoreManager in_dataStoreMgr, Integer in_regionId) {
        ArrayList regionList = null;
        HashMap regionToZoneMap;

        // If we have a region id clue
        if (in_regionId != null) {
            // get the structure that we need from the datastore manager
            regionToZoneMap = (HashMap) in_dataStoreMgr.getDataStoreItem(DataStoreManager.ITEM_REGION_TO_ZONEKEY);

            // get the list of zonekeys for this region
            regionList = (ArrayList) regionToZoneMap.get(in_regionId);

        }

        if (regionList == null) {
            regionList = new ArrayList();
        }

        return regionList;
    }

    /**
     * Translates the suburb clue into a set of postcodes
     */
    private static ArrayList processSuburbClue(String in_suburb, Integer in_bookId, ArrayList in_stateList, DataStoreManager in_dataStoreMgr) throws RequestException {
        StringBuffer suburb, suburbClue;
        char thisChar;
        ArrayList postcodeList;
        StringTokenizer st;
        String token, finalClue;
        int postcodeValue;
        HashMap equateWords, postcodeToBook;
        Integer postcode;

        // Init
        postcodeList = new ArrayList();
        postcodeToBook = (HashMap) in_dataStoreMgr.getDataStoreItem(DataStoreManager.ITEM_POSTCODE_TO_BOOK);

        // Clean the suburb clue to just alphanumeric letters
        suburb = new StringBuffer(in_suburb.length());
        for (int i = 0; i < in_suburb.length(); i++) {
            thisChar = in_suburb.charAt(i);
            if (Character.isLetterOrDigit(thisChar)) {
                suburb.append(thisChar);
            } else {
                suburb.append(' ');
            }
        }
        suburb = new StringBuffer(suburb.toString().trim().toLowerCase());

        // Bail if no suburb clue
        if ("".equals(suburb.toString())) {
            return postcodeList;
        }

        // Split into suburb clue words and postcodes
        st = new StringTokenizer(suburb.toString());
        suburbClue = new StringBuffer();
        while (st.hasMoreTokens()) {
            // Grab this token
            token = st.nextToken();

            // If it looks like a postcode
            postcodeValue = Integer.parseInt(token);
            if ((postcodeValue >= POSTCODE_LOWER_BOUND) && (postcodeValue <= POSTCODE_UPPER_BOUND)) {
                // Add to the postcode list if its a known postcode (we can find
                // a book for it)
                postcode = new Integer(postcodeValue);
                if (postcodeToBook.get(postcode) != null) {
                    postcodeList.add(postcode);
                } else {
                }
            } else {
                // This token doesn't appear to be a postcode. Save it for the
                // text search later
                suburbClue.append(token + " ");
            }
        }

        // Apply the suburb equate words
        equateWords = (HashMap) in_dataStoreMgr.getDataStoreItem(DataStoreManager.ITEM_SUBURB_EQUATE);
        finalClue = suburbClue.toString();

        // Add the list of postcodes from the suburb text index

        // Removed postcode filtering code -- see comment of deprecated method
        // filterPostcodeList()

        // Ensure we found at least one postcode
        if (postcodeList.size() == 0) {
            throw new RequestException("Unable to determine postcodes for suburb clue", RequestException.ERRORCODE_INVALID_SUBURB_CLUE);
        }

        return postcodeList;
    }

    //
    // Public methods
    //

    /**
     * Converts a zonekey to a zone.
     * 
     * @param zoneKey
     *            input
     * @param zoneRange
     *            input range
     * @return the zone
     */
    public static int convertFromZoneKey(int zoneKey, int zoneRange) {
        return zoneKey - zoneRange;
    }

    /**
     * Converts a zone to a zonekey.
     * 
     * @param zone
     *            input
     * @param zoneRange
     *            input range
     * @return zonekey
     */
    public static int convertToZoneKey(int zone, int zoneRange) {
        return zone + zoneRange;
    }

    /**
     * Determines the set of zones for a given set of search area clues.
     * 
     * @param in_dataStoreMgr
     *            data store
     * @param in_suburbClue
     *            input clue
     * @param in_bookId
     *            book clue
     * @param in_stateId
     *            state id clue
     * @param in_regionId
     *            region clue
     * @param in_includeSurround
     *            whether to inlcude surrounding suburbs in the set
     * @param in_includeZoned
     *            whether to include zoned in the set
     * @return a SearchAreaZoneListBean with the list of zones in it
     * @throws RequestException
     *             in the event of an error
     */
    public static SearchAreaZoneListBean determineZones(DataStoreManager in_dataStoreMgr, String in_suburbClue, Integer in_bookId, Integer in_stateId, Integer in_regionId,
            Boolean in_includeSurround, Boolean in_includeZoned) throws RequestException {
        ArrayList physicalList, paidList, combinedList, savedPaidList;

        // Grab the physical zones
        physicalList = determinePhysicalZones(in_dataStoreMgr, in_suburbClue, in_bookId, in_stateId, in_regionId);
        combinedList = new ArrayList(physicalList);

        // Grab the paid zones only if not all states search

        paidList = null;
        savedPaidList = null;
        // if zoning is null, save a copy of the paid zonelist
        // if zoning is enabled, then simply set the paid and combined list
        if (in_includeZoned == null) {
            savedPaidList = determinePaidZones(in_dataStoreMgr, physicalList);
        } else if (in_includeZoned.booleanValue()) {
            paidList = determinePaidZones(in_dataStoreMgr, physicalList);
            combinedList = paidList;
        }

        // if surrounding suburbs is enabled, expand the search area
        if ((in_includeSurround != null) && (in_includeSurround.booleanValue())) {
            physicalList = addSurroundingAreas(in_dataStoreMgr, physicalList);
        }

        // todo check
        // do we need to de-dupe the physical zone list here again?
        // todo check

        return new SearchAreaZoneListBean(physicalList, paidList, combinedList, savedPaidList);
    }

    /**
     * Tests whether a zone is in a particular rank.
     * 
     * @param in_zoneKey
     *            input
     * @param in_zoneRange
     *            input range
     * @return true if the zonkey is in a particular rank, false if not
     */
    public static boolean isZoneKeyInRange(int in_zoneKey, int in_zoneRange) {
        int testVal;

        // Remove the zonekey value
        // todo wtf is this number
        final int checkValue = 100000;
        testVal = in_zoneKey / checkValue;
        testVal *= checkValue;

        // Check
        return (testVal == in_zoneRange);
    }

    /**
     * Converts a zone key list into indexable content.
     * 
     * @param in_zoneList
     *            input list
     * @return a string of the input list, fit for indexing
     */
    public static String zoneKeyListToString(int[] in_zoneList) {
        StringBuffer output;

        // Init
        output = new StringBuffer();

        for (int element : in_zoneList) {
            output.append(element).append(" ");
        }

        // return
        return output.toString().trim();
    }

    /**
     * Converts a zonekey list into XML.
     * 
     * @param in_zoneKeys
     *            input array
     * @return an xml representation of the input
     */
    public static String zoneKeyListToXml(int[] in_zoneKeys) {
        StringBuffer output;

        // Init
        output = new StringBuffer("<zoneKeyList>\n");

        for (int element : in_zoneKeys) {
            // Add zonekey
            output.append("<zoneKey>").append(element).append("</zoneKey>\n");
        }

        // Terminate and return
        output.append("</zoneKeyList>");
        return output.toString();
    }

    /**
     * Converts paid zone keys to a string.
     * 
     * @param in_zoneList
     *            input list
     * @return string representation of the input list
     */
    public static String zoneKeyPaidListToString(ArrayList in_zoneList) {
        StringBuffer output;

        // Init
        output = new StringBuffer();

        // Add the paid zones
        if (in_zoneList != null) {
            for (int i = 0; i < in_zoneList.size(); i++) {

            }
        }

        return output.toString().trim();
    }
}
