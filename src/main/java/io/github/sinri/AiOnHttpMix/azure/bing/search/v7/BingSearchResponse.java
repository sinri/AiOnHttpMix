package io.github.sinri.AiOnHttpMix.azure.bing.search.v7;

import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface BingSearchResponse extends UnmodifiableJsonifiableEntity {
    static BingSearchResponse wrap(int statusCode, Map<String, String> headers, String rawBody) {
        return new BingSearchResponseImpl(statusCode, headers, rawBody);
    }

    int getStatusCode();

    Map<String, String> getHeaders();

    String getRawBody();

    /**
     * @return Type hint, which is set to SearchResponse.
     */
    default String getType() {
        return readString("_type");
    }

    /**
     * @return The query string that Bing used for the request.
     */
    default QueryContext getQueryContext() {
        JsonObject entries = readJsonObject("queryContext");
        return QueryContext.wrap(entries);
    }

    /**
     * @return A list of webpages that are relevant to the search query.
     */
    default WebAnswer getWebPages() {
        JsonObject entries = readJsonObject("webPages");
        return WebAnswer.wrap(entries);
    }

    /**
     * @return The order that Bing suggests that you display the search results in.
     */
    default RankingResponse getRankingResponse() {
        JsonObject entries = readJsonObject("rankingResponse");
        return RankingResponse.wrap(entries);
    }

    interface QueryContext extends UnmodifiableJsonifiableEntity {
        static QueryContext wrap(JsonObject jsonObject) {
            return new QueryContextImpl(jsonObject);
        }

        /**
         * A Boolean value that indicates whether the specified query has adult intent.
         * The value is true if the query has adult intent.
         * If true, and the request's safeSearch query parameter is set to Strict, the response contains only news results, if applicable.
         */
        default Boolean getAdultIntent() {
            return readBoolean("adultIntent");
        }

        /**
         * The query string to use to force Bing to use the original string.
         * For example, if the query string is saling downwind, the override query string is +saling downwind. Remember to encode the query string, which results in %2Bsaling+downwind.
         * The object includes this field only if the original query string contains a spelling mistake.
         */
        default String getAlterationOverrideQuery() {
            return readString("alterationOverrideQuery");
        }

        /**
         * The query string that Bing used to perform the query.
         * Bing uses the altered query string if the original query string contained spelling mistakes. For example, if the query string is saling downwind, the altered query string is sailing downwind.
         * The object includes this field only if the original query string contains a spelling mistake.
         */
        default String getAlteredQuery() {
            return readString("alteredQuery");
        }

        /**
         * A Boolean value that indicates whether Bing requires the user's location to provide accurate results. If you specified the user's location by using the X-MSEdge-ClientIP and X-Search-Location headers, you can ignore this field.
         * For location aware queries, such as "today's weather" or "restaurants near me" that need the user's location to provide accurate results, this field is set to true.
         * For location aware queries that include the location (for example, "Seattle weather"), this field is set to false. This field is also set to false for queries that are not location aware, such as "best sellers."
         */
        default Boolean getAskUserForLocation() {
            return readBoolean("askUserForLocation");
        }

        /**
         * The query string as specified in the request.
         */
        default String getOriginalQuery() {
            return readString("originalQuery");
        }
    }

    interface WebAnswer extends UnmodifiableJsonifiableEntity {
        static WebAnswer wrap(JsonObject jsonObject) {
            return new WebAnswerImpl(jsonObject);
        }

        /**
         * @return An ID that uniquely identifies the web answer.
         * The object includes this field only if the Ranking answer suggests that you display all web results in a group. For more information about how to use the ID, see Ranking results.
         */
        default String getId() {
            return readString("id");
        }

        /**
         * @return A Boolean value that indicates whether the response excluded some results from the answer. If Bing excluded some results, the value is true.
         */
        default Boolean getSomeResultsRemoved() {
            return readBoolean("someResultsRemoved");
        }

        /**
         * @return The estimated number of webpages that are relevant to the query. Use this number along with the count and offset query parameters to page the results.
         */
        default Long getTotalEstimatedMatches() {
            return readLong("totalEstimatedMatches");
        }

        /**
         * @return A list of webpages that are relevant to the query.
         */
        default List<WebPage> getValue() {
            return Objects.requireNonNull(readJsonObjectArray("value"))
                    .stream()
                    .map(WebPage::wrap)
                    .toList();
        }

        /**
         * @return The URL to the Bing search results for the requested webpages.
         */
        default String getWebSearchUrl() {
            return readString("webSearchUrl");
        }
    }

    interface WebPage extends UnmodifiableJsonifiableEntity {
        static WebPage wrap(JsonObject jsonObject) {
            return new WebPageImpl(jsonObject);
        }

        /**
         * @return The last time that Bing crawled the webpage. The date is in the form, YYYY-MM-DDTHH:MM:SS. For example, 2015-04-13T05:23:39.
         */
        default String getDateLastCrawled() {
            return readString("dateLastCrawled");
        }

        /**
         * @return The time that webpage published. The date is in the form, YYYY-MM-DDTHH:MM:SS.
         * Example: 2015-04-13T05:23:39.
         */
        default String getDatePublished() {
            return readString("datePublished");
        }

        /**
         * @return The display version of the datePublished.
         */
        default String getDatePublishedDisplayText() {
            return readString("datePublishedDisplayText");
        }

        // contractualRules: A list of rules that you must adhere to if you display the answer.

        /**
         * @return A list of links to related content that Bing found in the website that contains this webpage.
         * The Webpage object in this context includes only the name and url fields and optionally the snippet field.
         */
        default List<WebPage> getDeepLinks() {
            return Objects.requireNonNull(readJsonObjectArray("deepLinks"))
                    .stream()
                    .map(WebPage::wrap)
                    .toList();
        }

        /**
         * @return The display URL of the webpage. The URL is meant for display purposes only and is not well formed.
         */
        default String getDisplayUrl() {
            return readString("displayUrl");
        }

        /**
         * @return An ID that uniquely identifies this webpage in the list of web results.
         * The object includes this field only if the Ranking answer specifies that you mix the webpages with the other search results. Each webpage contains an ID that matches an ID in the Ranking answer. For more information, see Ranking results.
         */
        default String getId() {
            return readString("id");
        }

        /**
         * @return A Boolean value that indicates whether the webpage contains adult content. If the webpage doesn't contain adult content, isFamilyFriendly is set to true.
         */
        default Boolean getIsFamilyFriendly() {
            return readBoolean("isFamilyFriendly");
        }

        /**
         * @return A Boolean value that indicates whether the user’s query is frequently used for navigation to different parts of the webpage’s domain. Is true if users navigate from this page to other parts of the website.
         */
        default Boolean getIsNavigational() {
            return readBoolean("isNavigational");
        }

        /**
         * @return A two-letter language code that identifies the language used by the webpage. For example, the language code is en for English.
         */
        default String getLanguage() {
            return readString("language");
        }

        /**
         * @return A notice that Bing provides if it thinks the webpage may cause a potential issue if the user clicks the url link. You should display the notice with high visibility next to the webpage link.
         */
        default Malware getMalware() {
            return Malware.wrap(readJsonObject("malware"));
        }

        /**
         * @return The name of the webpage.
         * Use this name along with url to create a hyperlink that when clicked takes the user to the webpage.
         */
        default String getName() {
            return readString("name");
        }

        /**
         * @return A list of search tags that the webpage owner specified on the webpage. The API returns only indexed search tags.
         * The name field of the MetaTag object contains the indexed search tag. Search tags begin with search.* (for example, search.assetId). The content field contains the tag's value.
         */
        default List<MetaTag> getSearchTags() {
            return Objects.requireNonNull(readJsonObjectArray("searchTags"))
                    .stream()
                    .map(MetaTag::wrap)
                    .toList();
        }

        /**
         * @return A snippet of text from the webpage that describes its contents.
         */
        default String getSnippet() {
            return readString("snippet");
        }

        /**
         * @return The URL to the webpage.
         * Use this URL along with name to create a hyperlink that when clicked takes the user to the webpage.
         */
        default String getUrl() {
            return readString("url");
        }
    }

    interface Malware extends UnmodifiableJsonifiableEntity {
        static Malware wrap(JsonObject jsonObject) {
            return new MalwareImpl(jsonObject);
        }

        /**
         * @return A URL to a webpage where the user may get more information about safely buying prescription medicine online.
         */
        default String getBeSafeRxUrl() {
            return readString("beSafeRxUrl");
        }

        /**
         * Possible values are:
         * NABP — Warns that the National Association of Boards of Pharmacy includes this pharmacy on its Not Recommended list.
         * Malware — Warns that the site may download malicious software that may harm the user’s device.
         * MaliciousPageLink — Warns that the site may contain links that could download malicious software that may harm the user’s device.
         * Phishing — Warns that the site could trick the user into disclosing financial, personal, or other sensitive information.
         *
         * @return The type of malware notice.
         */
        default String getMalwareWarningType() {
            return readString("malwareWarningType");
        }

        /**
         * @return A URL to a webpage where the user can get an explanation of the issue. For NABP notices, users can use this link to verify a pharmacy.
         */
        default String getWarningExplanationUrl() {
            return readString("warningExplanationUrl");
        }

        /**
         * @return A URL to a webpage where the user can get more information about the notice. For NABP notices, users can use this link to see the list of online sites that the board doesn’t recommend.
         */
        default String getWarningLetterUrl() {
            return readString("warningLetterUrl");
        }
    }

    interface MetaTag extends UnmodifiableJsonifiableEntity {
        static MetaTag wrap(JsonObject jsonObject) {
            return new MetaTagImpl(jsonObject);
        }

        /**
         * @return The name of the metadata.
         */
        default String getName() {
            return readString("name");
        }

        /**
         * @return The metadata.
         */
        default String getContent() {
            return readString("content");
        }
    }

    interface RankingResponse extends UnmodifiableJsonifiableEntity {
        static RankingResponse wrap(JsonObject jsonObject) {
            return new RankingResponseImpl(jsonObject);
        }

        /**
         * @return The search results to display in the mainline section of the search results page.
         */
        default RankingGroup getMainline() {
            return RankingGroup.wrap(readJsonObject("mainline"));
        }

        /**
         * @return The search results that should be afforded the most visible treatment (for example, displayed above the mainline and sidebar).
         */
        default RankingGroup getPole() {
            return RankingGroup.wrap(readJsonObject("pole"));
        }

        /**
         * @return The search results to display in the sidebar section of the search results page.
         */
        default RankingGroup getSidebar() {
            return RankingGroup.wrap(readJsonObject("sidebar"));
        }
    }

    interface RankingGroup extends UnmodifiableJsonifiableEntity {
        static RankingGroup wrap(JsonObject jsonObject) {
            return new RankingGroupImpl(jsonObject);
        }

        default List<RankingItem> getItems() {
            return Objects.requireNonNull(readJsonObjectArray("items"))
                    .stream()
                    .map(RankingItem::wrap)
                    .toList();
        }
    }

    interface RankingItem extends UnmodifiableJsonifiableEntity {
        static RankingItem wrap(JsonObject jsonObject) {
            return new RankingItemImpl(jsonObject);
        }

        /**
         * @return The answer that contains the item to display. For example, News.
         * Use the type to find the answer in the SearchResponse object. The type is the name of a field in the SearchResponse object.
         */
        default String getAnswerType() {
            return readString("answerType");
        }

        /**
         * @return A zero-based index of the item in the answer.
         * If the item does not include this field, display all items in the answer. For example, display all news articles in the News answer.
         */
        default Integer getResultIndex() {
            return readInteger("resultIndex");
        }

        /**
         * @return The ID that identifies either an answer to display or an item of an answer to display. If the ID identifies an answer, display all items of the answer.
         */
        default Identifiable getValue() {
            return Identifiable.wrap(readJsonObject("value"));
        }
    }

    interface Identifiable extends UnmodifiableJsonifiableEntity {
        static Identifiable wrap(JsonObject jsonObject) {
            return new IdentifiableImpl(jsonObject);
        }

        default String getId() {
            return readString("id");
        }
    }

    /*
    todo more
    computation:	The answer to a math expression or unit conversion expression.
    entities:	A list of entities that are relevant to the search query.
    images:	A list of images that are relevant to the search query.
    news:	A list of news articles that are relevant to the search query.
    places:	A list of places that are relevant to the search query.
    relatedSearches:	A list of related queries made by others.
    spellSuggestions:	The query string that likely represents the user's intent.
    timeZone:	The date and time of one or more geographic locations.
    translations:	The translation of a word or phrase in the query string to another language.
    videos:	A list of videos that are relevant to the search query.
     */

}
