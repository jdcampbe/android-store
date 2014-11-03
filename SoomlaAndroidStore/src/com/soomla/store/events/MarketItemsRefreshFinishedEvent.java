/*
 * Copyright (C) 2012-2014 Soomla Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.soomla.store.events;


import com.soomla.store.domain.MarketItem;

import java.util.List;

/**
 * This event is fired when items associated with market are refreshed (prices, titles ...).
 */
public class MarketItemsRefreshFinishedEvent extends SoomlaEvent {

    /**
     * Constructor.
     *
     * @param marketItems
     */
    public MarketItemsRefreshFinishedEvent(List<MarketItem> marketItems) {
        this(marketItems, null);
    }

    public MarketItemsRefreshFinishedEvent(List<MarketItem> marketItems, Object sender) {
        super(sender);
        this.mMarketItems = marketItems;
    }


    /** Setters and Getters */

    public List<MarketItem> getMarketItems() {
        return mMarketItems;
    }


    /** Private Members */

    List<MarketItem> mMarketItems;
}
