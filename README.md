*This project is a part of The [SOOMLA](http://www.soom.la) Framework, which is a series of open source initiatives with a joint goal to help mobile game developers do more together. SOOMLA encourages better game design, economy modeling, social engagement, and faster development.*

Haven't you ever wanted an in-app purchase one liner that looks like this!?

```Java
StoreInventory.buy("[itemId]");
```

## android-store

*SOOMLA's Store Module for Android*

**December 3rd, 2014**:
Migrating Amazon IAP from v1.0 to v2.0. [Read more about IAP v2.0](https://developer.amazon.com/public/apis/earn/in-app-purchasing/docs-v2/migrate-iapv1-apps-to-iapv2)

**September 15th, 2014**:
`NonConsumableItem` class was removed.
To create a non-consumable item in your `IStoreAssetes` implementation, use `LifeTimeVG` with `PurchaseType` of `PurchaseWithMarket`.

**June 20th, 2014**: v3.4.1 presents support for "Soomla Core" (which separates some objects common to ALL SOOMLA's modules). It also removed the usage of SOOM_SEC. [Getting Started](https://github.com/soomla/android-store#getting-started) has changed! see [CHANGELOG](changelog.md).

**May 25th, 2014**: Amazon billing service is now our second billing service implementation. See [Billing Services](#whats-next-selecting-a-billing-service) for details.

**April 1st, 2014**: We've added the option to refresh market items details from the IAB Service (default is Google Play). You can call 'refreshInventory(true)' from SoomlaStore when you want and all your PurchasableItems that has a PurchaseType of PurchaseWithMarket will update the values of: MarketPrice, MarketTitle, MarketDescription. android-store automatically running the operation when you initialize SoomlaStore.


The current virtual economy model is called **modelV3**. Want to learn more about it? Try these:
* [Economy Model Objects](https://github.com/soomla/android-store/wiki/Economy-Model-Objects)
* [Handling Store Operations](https://github.com/soomla/android-store/wiki/Handling-Store-Operations)

android-store is the Android flavor of SOOMLA's Store Module.

Check out our [Wiki] (https://github.com/soomla/android-store/wiki) for more information about the project and how to use it better.

## Download

####Pre baked jars:

[android-store v3.6.4](http://library.soom.la/fetch/android-store/3.6.4?cf=github)

## Getting Started

* Before doing anything, SOOMLA recommends that you go through [Android In-app Billing](http://developer.android.com/guide/google/play/billing/index.html) and [Amazon In App Purchasing](https://developer.amazon.com/public/apis/earn/in-app-purchasing).

1. Add the jars from the downloaded zip to your project.

2. Make the following changes to your AndroidManifest.xml:

  Set `SoomlaApp` as the main Application by placing it in the `application` tag:

    ```xml
    <application ...
                 android:name="com.soomla.SoomlaApp">
    ```

3. Initialize **Soomla** with a secret that you chose to encrypt the user data. (For those who came from older versions, this should be the same as the old "customSec"):

    ```Java
     Soomla.initialize("[YOUR CUSTOM GAME SECRET HERE]");
    ```
    > The secret is your encryption secret for data saved in the DB.

4. Create your own implementation of _IStoreAssets_ in order to describe your specific game's assets ([example](https://github.com/soomla/android-store/blob/master/SoomlaAndroidExample/src/com/soomla/example/MuffinRushAssets.java)). Initialize _SoomlaStore_ with the class you just created:

    ```Java
     SoomlaStore.getInstance().initialize(new YourStoreAssetsImplementation());
    ```

    > Initialize `SoomlaStore` ONLY ONCE when your application loads.

5. Refer to the [next section](https://github.com/soomla/android-store#whats-next-selecting-a-billing-service) for information of selecting your Billing Service and setting it up.

And that's it ! You have storage and in-app purchasing capabilities... ALL-IN-ONE.


## What's next? Selecting a Billing Service

android-store can be used on all Android based devices meaning that you might want to use IAP with different billing services.

We've created two billing services for you: Google Play and Amazon (according to your demand).

The billing service is automatically started and stopped for every operation you're running on `SoomlaStore` (`buyWithMarket`, `restoreTransactions` ...).

Be careful with that. Don't leave the service running in the background without closing it.

You must select a billing service for android-store to work properly. The integration of a billing service is very easy:

#### [Google Play](https://github.com/soomla/android-store-google-play)

1. Add `AndroidStoreGooglePlay.jar` from the folder `billing-services/google-play` to your project.
2. Make the following changes in `AndroidManifest.xml`:

  Add the following permission (for Google Play):

  ```xml
  <uses-permission android:name="com.android.vending.BILLING" />
  ```

  Add the `IabActivity` to your `application` element, the plugin will spawn a transparent activity to make purchases. Also, you need to tell us what plugin you're using so add a meta-data tag for that:

  ```xml
  <activity android:name="com.soomla.store.billing.google.GooglePlayIabService$IabActivity"
            android:theme="@android:style/Theme.Translucent.NoTitleBar.Fullscreen"/>
  <meta-data android:name="billing.service" android:value="google.GooglePlayIabService" />
  ```

3. After you initialize `SoomlaStore`, let the plugin know your public key from the dev console:

  ```Java
  GooglePlayIabService.getInstance().setPublicKey("[YOUR PUBLIC KEY FROM THE MARKET]");
  ```

4. If you want to allow the test purchases, all you need to do is tell that to the plugin:

  ```Java
  GooglePlayIabService.AllowAndroidTestPurchases = true;
  ```

For Google Play, We recommend that you open the IAB Service and keep it open in the background in cases where you have an in-game storefront. This is how you do that:

When you open the store, call:  
  ```Java
  SoomlaStore.getInstance().startIabServiceInBg();
  ```

When the store is closed, call:  
  ```Java
  SoomlaStore.getInstance().stopIabServiceInBg();
  ```

#### [Amazon](https://github.com/soomla/android-store-amazon)

1. Add `in-app-purchasing-2.0.1.jar` and `AndroidStoreAmazon.jar` from the folder `billing-services/amazon` to your project.

2. Make the following changes in `AndroidManifest.xml`:

  Add Amazon's `ResponseReceiver` to your `application` element. Also, you need to tell us what plugin you're using so add a meta-data tag for that:

  ```xml
  <receiver android:name = "com.amazon.device.iap.ResponseReceiver" >
    <intent-filter>
        <action android:name = "com.amazon.inapp.purchasing.NOTIFY"
                android:permission = "com.amazon.inapp.purchasing.Permission.NOTIFY" />
    </intent-filter>
  </receiver>
  <meta-data android:name="billing.service" android:value="amazon.AmazonIabService" />
  ```

## Important read: In App Purchasing.


When we implemented modelV3, we were thinking about ways people buy things inside apps. We figured many ways you can let your users purchase stuff in your game and we designed the new modelV3 to support 2 of them: PurchaseWithMarket and PurchaseWithVirtualItem.

**PurchaseWithMarket** is a PurchaseType that allows users to purchase a VirtualItem with Google Play.  
**PurchaseWithVirtualItem** is a PurchaseType that lets your users purchase a VirtualItem with a different VirtualItem. For Example: Buying 1 Sword with 100 Gems.

In order to define the way your various virtual items (Goods, Coins ...) are purchased, you'll need to create your implementation of IStoreAsset (the same one from step 4 in the "Getting Started" above).

Here is an example:

Lets say you have a _VirtualCurrencyPack_ you call `TEN_COINS_PACK` and a _VirtualCurrency_ you call `COIN_CURRENCY`:

```Java
VirtualCurrencyPack TEN_COINS_PACK = new VirtualCurrencyPack(
        "10 Coins",                                     // name
        "A pack of 10 coins",                           // description
        "10_coins",                                     // item id
        10,                                             // number of currencies in the pack
        COIN_CURRENCY_ITEM_ID,                          // the currency associated with this pack
        new PurchaseWithMarket("com.soomla.ten_coin_pack", 1.99));
```

Now you can use _StoreInventory_ to buy your new VirtualCurrencyPack:

```Java
StoreInventory.buy(TEN_COINS_PACK.getItemId());
```

And that's it! android-store knows how to contact Google Play for you and will redirect your users to their purchasing system to complete the transaction.
Don't forget to define your _IStoreEventHandler_ in order to get the events of successful or failed purchases (see [Event Handling](https://github.com/soomla/android-store#event-handling)).


## Debugging

In order to debug android-store, set `SoomlaConfig.logDebug` to `true`. This will print all of _android-store's_ debugging messages to logcat.

## Storage & Meta-Data


When you initialize _SoomlaStore_, it automatically initializes two other classes: _StorageManager_ and _StoreInfo_. _StorageManager_ is the father of all storage related instances in your game. Use it to access the balances of virtual currencies and virtual goods (usually, using their itemIds). _StoreInfo_ is the mother of all meta data information about your specific game. It is initialized with your implementation of `IStoreAssets` and you can use it to retrieve information about your specific game.  
We've also added _StoreInventory_ which is a utility class to help you do store related operations even easier.

The on-device storage is encrypted and kept in a SQLite database. SOOMLA is preparing a cloud-based storage service that will allow this SQLite to be synced to a cloud-based repository that you'll define.

**Example Usages**

* Give the user 10 pieces of a virtual currency with itemId "currency_coin":

    ```Java
    StoreInventory.giveVirtualItem("currency_coin", 10);
    ```

* Take 10 virtual goods with itemId "green_hat":

    ```Java
    StoreInventory.takeVirtualItem("green_hat", 10);
    ```

* Get the current balance of a virtual good with itemId "green_hat" (here we decided to show you the 'long' way. you can also use StoreInventory):

    ```Java
    VirtualGood greenHat = (VirtualGood)StoreInfo.getVirtualItem("green_hat");
    int greenHatsBalance = StorageManager.getVirtualGoodsStorage().getBalance(greenHat);
    ```

## Security


If you want to protect your game from 'bad people' (and who doesn't?!), you might want to follow some guidelines:

+ SOOMLA keeps the game's data in an encrypted database. In order to encrypt your data, SOOMLA generates a private key out of several parts of information. The Soomla Secret (before v3.4.1 is was called customSec) is one of them. SOOMLA recommends that you provide this value when initializing `SoomlaStore` and before you release your game. BE CAREFUL: You can change this value once! If you try to change it again, old data from the database will become unavailable.
+ Following Google's recommendation, SOOMLA also recommends that you split your public key and construct it on runtime or even use bit manipulation on it in order to hide it. The key itself is not secret information but if someone replaces it, your application might get fake messages that might harm it.

## Event Handling


For event handling, we use Square's great open-source project [otto](http://square.github.com/otto/). In ordered to be notified of store related events, you can register for specific events and create your game-specific behavior to handle them.

> Your behavior is an addition to the default behavior implemented by SOOMLA. You don't replace SOOMLA's behavior.

In order to register for events:

1. In the class that should receive the event create a function with the annotation '@Subscribe'. Example:

    ```Java
    @Subscribe
    public void onMarketPurchaseEvent(MarketPurchaseEvent marketPurchaseEvent) {
        ...
    }
    ```

2. You'll also have to register your class in the event bus (and unregister when needed):

   ```Java
   BusProvider.getInstance().register(this);
   ```

   ```Java
   BusProvider.getInstance().unregister(this);
   ```

> If your class is an Activity, register in 'onResume' and unregister in 'onPause'

You can find a full event handler example [here](https://github.com/soomla/android-store/blob/master/SoomlaAndroidExample/src/com/soomla/example/ExampleEventHandler.java).

[List of events](https://github.com/soomla/android-store/tree/master/SoomlaAndroidStore/src/com/soomla/store/events)

[Full documentation and explanation of otto](http://square.github.com/otto/)

Contribution
---
SOOMLA appreciates code contributions! You are more than welcome to extend the capabilities of SOOMLA.

Fork -> Clone -> Implement -> Add documentation -> Test -> Pull-Request.

IMPORTANT: If you would like to contribute, please follow our [Documentation Guidelines](https://github.com/soomla/android-store/blob/master/documentation.md). Clear, consistent comments will make our code easy to understand.

## SOOMLA, Elsewhere ...

+ [Framework Website](http://www.soom.la/)
+ [Knowledge Base](http://know.soom.la/)


<a href="https://www.facebook.com/pages/The-SOOMLA-Project/389643294427376"><img src="http://know.soom.la/img/tutorial_img/social/Facebook.png"></a><a href="https://twitter.com/Soomla"><img src="http://know.soom.la/img/tutorial_img/social/Twitter.png"></a><a href="https://plus.google.com/+SoomLa/posts"><img src="http://know.soom.la/img/tutorial_img/social/GoogleP.png"></a><a href ="https://www.youtube.com/channel/UCR1-D9GdSRRLD0fiEDkpeyg"><img src="http://know.soom.la/img/tutorial_img/social/Youtube.png"></a>

## License

Apache License. Copyright (c) 2012-2014 SOOMLA. http://project.soom.la
+ http://opensource.org/licenses/Apache-2.0
