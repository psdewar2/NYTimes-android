# Week 2 - NYTimes

NYTimes is an article search application for Android.

Submitted by: Peyt S. Dewar II

Time spent:

Completed user stories:

 * [x] Required: Users can enter a search query that will display a grid of news articles using the thumbnail and headline from the New York Times Search API.
 * [x] Required: Users can click on settings which allows selection of advanced search options to filter results.
 * [x] Required: Users can configure advanced search filters such as begin date, news desk values, and sort order.
 * [x] Required: Subsequent searches will have any filters applied to the search results.
 * [x] Required: Users can tap on any article in results to view the contents in an embedded browser.
 * [x] Required: Users can scroll down infinitely to continue loading more news articles. The maximum number of articles is limited by the API search.
 * [] Optional: Robust error handling is added and network connectivity is checked. 
 * [x] Optional: ActionBar SearchView is used for searching instead of EditText.
 * [] Optional: Users can share a link to their friends or email it to themselves.
 * [x] Optional: SettingsActivity is replaced with a lightweight modal overlay.
 * [] Optional: UI is improved through styling and coloring.
 * [] Stretch: RecyclerView is used with the StaggeredGridLayoutManager to improve displaying the grid of image results.
 * [] Stretch: RecyclerView is made heterogenous for different news articles that only have text or have text with thumbnails.
 * [x] Stretch: ButterKnife annotation library is applied to reduce view boilerplate.
 * [x] Stretch: Parceler is used to make Article objects parcelable instead of serializable.
 * [] Stretch: All icon drawables and other static image assets are replaced with vector drawables where appropriate.
 * [] Stretch: Data binding support module is leveraged to bind data into one or more activity layout templates.
 * [] Stretch: Picasso is replaced with Glide for more efficient image rendering.
 * [] Stretch: Retrolambda expressions are used to cleanup event handling blocks.
 * [] Stretch: GSON is leveraged to streamline the parsing of JSON data.
 * [] Stretch: Android Async HTTP is replaced with Retrofit.

## Video Walkthrough 

![Video Walkthrough](__.gif)

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Notes

## License

    Copyright [2016] [Peyt S. Dewar II]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
