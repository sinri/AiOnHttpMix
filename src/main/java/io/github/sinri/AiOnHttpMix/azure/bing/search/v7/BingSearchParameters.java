package io.github.sinri.AiOnHttpMix.azure.bing.search.v7;

import io.github.sinri.keel.core.json.JsonifiableEntity;
import io.vertx.core.json.JsonObject;

/**
 * @see <a href="https://learn.microsoft.com/en-us/bing/search-apis/bing-web-search/reference/query-parameters">Docs</a>
 */
public interface BingSearchParameters extends JsonifiableEntity<BingSearchParameters> {
    static BingSearchParameters create() {
        return new BingSearchParametersImpl();
    }

    static BingSearchParameters create(String q) {
        return new BingSearchParametersImpl().setQ(q);
    }

    static BingSearchParameters wrap(JsonObject jsonObject) {
        return new BingSearchParametersImpl(jsonObject);
    }

    /**
     * The number of answers that you want the response to include.
     * The answers that Bing returns are based on ranking.
     * For example, if Bing returns webpages, images, videos, and relatedSearches for a request and you set this parameter to two (2),
     * the response includes webpages and images.
     * If you included the responseFilter query parameter in the same request and set it to webpages and news, the response would include only webpages.
     * For information about promoting a ranked answer into the response, see the promote query parameter.
     */
    default Integer getAnswerCount() {
        return readInteger("answerCount");
    }

    /**
     * @param answerCount 返回报文里想要的回答数量。
     */
    default BingSearchParameters setAnswerCount(Integer answerCount) {
        this.toJsonObject().put("answerCount", answerCount);
        return this;
    }

    /**
     * A 2-character country code of the country where the results come from.
     * For a list of possible values, see Market codes.
     *
     * @see <a href="https://learn.microsoft.com/en-us/bing/search-apis/bing-web-search/reference/market-codes#country-codes">Market codes</a>
     * If you set this parameter, you must also specify the Accept-Language header.
     * @see <a href="https://learn.microsoft.com/en-us/bing/search-apis/bing-web-search/reference/headers#acceptlanguage">Accept-Language</a>
     * Bing uses the first supported language it finds in the specified languages and combines it with the country code to determine the market to return results for.
     * If the languages list does not include a supported language, Bing finds the closest language and market that supports the request.
     * Or, Bing may use an aggregated or default market for the results.
     * To know which market Bing used, get the BingAPIs-Market header in the response.
     * @see <a href="https://learn.microsoft.com/en-us/bing/search-apis/bing-web-search/reference/headers#market-header">BingAPIs-Market</a>
     * Use this query parameter and the Accept-Language header only if you specify multiple languages.
     * Otherwise, you should use the mkt and setLang query parameters.
     * This parameter and the mkt query parameter are mutually exclusive — do not specify both.
     */
    default String getCc() {
        return readString("cc");
    }

    /**
     * @param cc Country Code in 2 chars.
     * @see <a href="https://learn.microsoft.com/en-us/bing/search-apis/bing-web-search/reference/market-codes#country-codes">Market codes</a>
     */
    default BingSearchParameters setCc(String cc) {
        this.toJsonObject().put("cc", cc);
        return this;
    }

    /**
     * The number of search results to return in the response.
     * The default is 10 and the maximum value is 50.
     * The actual number delivered may be less than requested.
     * Use this parameter along with the offset parameter to page results.
     * For more information, see Paging results.
     * For example, if your user interface displays 10 search results per page, set count to 10 and offset to 0 to get the first page of results.
     * For each subsequent page, increment offset by 10 (for example, 0, 10, 20).
     * It is possible for multiple pages to include some overlap in results.
     * This parameter affects only webpage results and has no impact on the number of results that Bing returns for other answers in the search results such as images or videos.
     */
    default Integer getCount() {
        return readInteger("count");
    }

    /**
     * @param count 搜索候选结果数量
     */
    default BingSearchParameters setCount(Integer count) {
        this.toJsonObject().put("count", count);
        return this;
    }


    /**
     * Filter search results by the following case-insensitive age values:
     * `Day` — Return webpages that Bing discovered within the last 24 hours.
     * `Week` — Return webpages that Bing discovered within the last 7 days.
     * `Month` — Return webpages that Bing discovered within the last 30 days.
     * To get articles discovered by Bing during a specific timeframe,
     * specify a date range in the form, {@code YYYY-MM-DD..YYYY-MM-DD}.
     * For example, {@code &freshness=2019-02-01..2019-05-30}.
     * To limit the results to a single date, set this parameter to a specific date.
     * For example, {@code &freshness=2019-02-04}.
     */
    default String getFreshness() {
        return readString("freshness");
    }

    /**
     * @param freshness 搜索被Bing在指定日期范围内的目标。日期格式为{@code YYYY-MM-DD}。开始日期和结束日期之间以`..`分隔，如果为同一日则直接填一个日期。
     */
    default BingSearchParameters setFreshness(String freshness) {
        this.toJsonObject().put("freshness", freshness);
        return this;
    }

    // mkt

    /**
     * The market where the results come from.
     * Typically, mkt is the country where the user is making the request from.
     * However, it could be a different country if the user is not located in a country where Bing delivers results.
     * The market must be in the form {@code <language>-<country/region>}.
     * For example, en-US.
     * The string is case insensitive.
     * For a list of possible market values, see Market codes.
     *
     * @see <a href="https://learn.microsoft.com/en-us/bing/search-apis/bing-web-search/reference/market-codes">Market codes</a>
     * NOTE:
     * If known, you are encouraged to always specify the market.
     * Specifying the market helps Bing route the request and return an appropriate and optimal response.
     * If you specify a market that is not listed in Market codes, Bing uses a best fit market code based on an internal mapping that is subject to change.
     * To know which market Bing used, get the BingAPIs-Market header in the response.
     * @see <a href="https://learn.microsoft.com/en-us/bing/search-apis/bing-web-search/reference/headers#market-header">BingAPIs-Market</a>
     * This parameter and the cc query parameter are mutually exclusive — do not specify both.
     */
    default String getMkt() {
        return readString("mkt");
    }

    /**
     * @param mkt 搜索市场。和cc犯冲的参数。
     */
    default BingSearchParameters setMkt(String mkt) {
        this.toJsonObject().put("mkt", mkt);
        return this;
    }

    /**
     * The zero-based offset that indicates the number of search results to skip before returning results.
     * The default is 0. The offset should be less than {@code (totalEstimatedMatches - count)}.
     * Use this parameter along with the count parameter to page results.
     * For example, if your user interface displays 10 search results per page, set count to 10 and offset to 0 to get the first page of results.
     * For each subsequent page, increment offset by 10 (for example, 0, 10, 20).
     * It is possible for multiple pages to include some overlap in results.
     */
    default Integer getOffset() {
        return readInteger("offset");
    }

    /**
     * @param offset 默认为0。
     */
    default BingSearchParameters setOffset(Integer offset) {
        this.toJsonObject().put("offset", offset);
        return this;
    }

    /**
     * A comma-delimited list of answers that you want the response to include regardless of their ranking.
     * For example, if you set answerCount) to two (2) so Bing returns the top two ranked answers, but you also want the response to include news, you'd set promote to news.
     * If the top ranked answers are webpages, images, videos, and relatedSearches, the response includes webpages and images because news is not a ranked answer.
     * But if you set promote to video, Bing would promote the video answer into the response and return webpages, images, and videos.
     * The answers that you want to promote do not count against the answerCount limit.
     * For example, if the ranked answers are news, images, and videos, and you set answerCount to 1 and promote to news, the response contains news and images.
     * Or, if the ranked answers are videos, images, and news, the response contains videos and news.
     * The following are the possible values:
     * Computation
     * Entities
     * Images
     * News
     * RelatedSearches
     * SpellSuggestions
     * TimeZone
     * Videos
     * Webpages
     * NOTE: Use only if you specify the answerCount parameter.
     */
    default String getPromote() {
        return readString("promote");
    }

    /**
     * The following are the possible values:
     * Computation
     * Entities
     * Images
     * News
     * RelatedSearches
     * SpellSuggestions
     * TimeZone
     * Videos
     * Webpages
     *
     * @param promote 英文逗号分隔的回答类型取值。
     */
    default BingSearchParameters setPromote(String promote) {
        this.toJsonObject().put("promote", promote);
        return this;
    }

    /**
     * The user's search query term. The term may not be empty.
     * The term may contain Bing Advanced Operators.
     * For example, to limit results to a specific domain, use the `site:` operator ({@code q=fishing+site:fishing.contoso.com}).
     * Note that the results may contain results from other sites depending on the number of relevant results found on the specified site.
     */
    default String getQ() {
        return readString("q");
    }

    default BingSearchParameters setQ(String q) {
        this.toJsonObject().put("q", q);
        return this;
    }

    /**
     * A comma-delimited list of answers to include in the response.
     * If you do not specify this parameter, the response includes all search answers for which there's relevant data.
     * The following are the possible filter values:
     * Computation
     * Entities
     * Images
     * News
     * Places
     * RelatedSearches
     * SpellSuggestions
     * TimeZone
     * Translations
     * Videos
     * Webpages
     * If you want to exclude specific types of content, such as images, from the response, you can exclude them by prefixing a hyphen (minus) to the responseFilter value.
     * For example, {@code &responseFilter=-images}.
     * While this filter can be used to get a single answer type, you should always use the answer-specific endpoint (if exists) so as to get richer results. For example, to receive only images, send the request to one of the Image Search API endpoints.
     * To include answers that would otherwise be excluded because of ranking, see the promote query parameter.
     */
    default String getResponseFilter() {
        return readString("responseFilter");
    }

    default BingSearchParameters setResponseFilter(String responseFilter) {
        this.toJsonObject().put("responseFilter", responseFilter);
        return this;
    }

    /**
     * Used to filter webpages, images, and videos for adult content.
     * The following are the possible filter values:
     * Off — Returns content with adult text and images but not adult videos.
     * Moderate — Returns webpages with adult text, but not adult images or videos.
     * Strict — Does not return adult text, images, or videos.
     * The default is Moderate.
     * NOTE: For video results, if safeSearch is set to Off, Bing ignores it and uses Moderate.
     * NOTE: If the request comes from a market that Bing's adult policy requires that safeSearch be set to Strict, Bing ignores the safeSearch value and uses Strict.
     * NOTE: If you use the site: query operator, there is a chance that the response may contain adult content regardless of what the safeSearch query parameter is set to. Use site: only if you are aware of the content on the site and your scenario supports the possibility of adult content.
     */
    default String getSafeSearch() {
        return readString("safeSearch");
    }

    default BingSearchParameters setSafeSearch(String safeSearch) {
        this.toJsonObject().put("safeSearch", safeSearch);
        return this;
    }

    /**
     * The language to use for user interface strings.
     * You may specify the language using either a 2-letter or 4-letter code.
     * Using 4-letter codes is preferred.
     * For a list of supported language codes, see Bing supported languages.
     * Bing loads the localized strings if setlang contains a valid 2-letter neutral culture code (fr) or a valid 4-letter specific culture code (fr-ca).
     * For example, for fr-ca, Bing loads the fr neutral culture code strings.
     * If setlang is not valid (for example, zh) or Bing doesn’t support the language (for example, af, af-na), Bing defaults to en (English).
     * To specify the 2-letter code, set this parameter to an ISO 639-1 language code.
     * To specify the 4-letter code, use the form {@code <language>-<country/region>} where {@code <language>} is an ISO 639-1 language code (neutral culture) and {@code <country/region>} is an ISO 3166 country/region (specific culture) code.
     * For example, use en-US for United States English.
     * Although optional, you should always specify the language.
     * Typically, you set setLang to the same language specified by mkt unless the user wants the user interface strings displayed in a different language.
     * This parameter and the Accept-Language header are mutually exclusive — do not specify both.
     * A user interface string is a string that's used as a label in a user interface.
     * There are few user interface strings in the JSON response objects.
     * Also, any links to Bing.com properties in the response objects use the specified language.
     */
    default String getSetLang() {
        return readString("setLang");
    }

    default BingSearchParameters setSetLang(String setLang) {
        this.toJsonObject().put("setLang", setLang);
        return this;
    }

    /**
     * A Boolean value that determines whether display strings in the results should contain decoration markers such as hit highlighting characters.
     * If true, the strings may include markers. The default is false.
     * To specify whether to use Unicode characters or HTML tags as the markers, see the textFormat query parameter.
     * For information about hit highlighting, see Hit highlighting.
     */
    default Boolean getTextDecorations() {
        return readBoolean("textDecorations");
    }

    default BingSearchParameters setTextDecorations(Boolean textDecorations) {
        this.toJsonObject().put("textDecorations", textDecorations);
        return this;
    }

    /**
     * <p>
     * The type of markers to use for text decorations (see the textDecorations query parameter).
     * The following are the possible values:
     * </p>
     * <p>
     * `Raw` — Use Unicode characters to mark content that needs special formatting.
     * The Unicode characters are in the range E000 through E019.
     * For example, Bing uses E000 and E001 to mark the beginning and end of query terms for hit highlighting.
     * </p>
     * <p>
     * `HTML` — Use HTML tags to mark content that needs special formatting.
     * For example, use {@code <b>} tags to highlight query terms in display strings.
     * </p>
     * <p>
     * The default is Raw.
     * </p>
     * <p>
     * For a list of markers and information about processing strings with the embedded Unicode characters, see Hit highlighting.
     * </p>
     * <p>
     * For display strings that contain escapable HTML characters such as {@code <}, {@code >}, and {@code &},
     * if textFormat is set to HTML, Bing escapes the characters as appropriate (for example, {@code <} is escaped to {@code &lt;}).
     * </p>
     */
    default String getTextFormat() {
        return readString("textFormat");
    }

    default BingSearchParameters setTextFormat(String textFormat) {
        this.toJsonObject().put("textFormat", textFormat);
        return this;
    }
}
