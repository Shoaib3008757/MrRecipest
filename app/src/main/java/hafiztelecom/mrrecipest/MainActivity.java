package hafiztelecom.mrrecipest;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

import hafiztelecom.mrrecipest.util.IabBroadcastReceiver;
import hafiztelecom.mrrecipest.util.IabHelper;
import hafiztelecom.mrrecipest.util.IabResult;
import hafiztelecom.mrrecipest.util.Inventory;
import hafiztelecom.mrrecipest.util.Purchase;

public class MainActivity extends AppCompatActivity implements IabBroadcastReceiver.IabBroadcastListener{

    long currentPosition = -1;

    int ifBought = -1;


    static final Integer[] imgid = {R.drawable.behira_korma,R.drawable.chicken_r,R.drawable.chiken_tikka_r,
                                    R.drawable.fry_paratha, R.drawable.gogapa, R.drawable.mutton_biryani,
                                    R.drawable.mutton_sindhi_biryani, R.drawable.seekh_kabab, R.drawable.recipe1,
                                    R.drawable.tandori_tika};


    ListView image_list;
    ImageAdapter imageAdapter;


    //app-billig varibales
    //inApp purchase ID for Beard
    static final String SKU_3_RECIPE = "recipe3"; //id for fry paratha recipe
    static final String SKU_4_RECIPE = "recipe4"; //id for gol gappa recipe
    static final String SKU_5_RECIPE = "recipe5"; //id for mutton biryani recipe
    static final String SKU_6_RECIPE = "recipe6"; //id for mutton sindhi biryani recipe
    static final String SKU_7_RECIPE = "recipe7"; //id for seek kabab recipe
    static final String SKU_8_RECIPE = "recipe8"; //id for mutton recipe
    static final String SKU_9_RECIPE = "recipe9"; //id for allo methi recipe


    static final String TAG = "MAINACTIVITY";


    IabHelper mHelper;
    String devPayLoad = "";
    private static final int IAPCODE = 10001;
    static final int RC_REQUEST = 10001;



    boolean mSubscribedToInfiniteGas = false;
    // Provides purchase notification while this app is running
    IabBroadcastReceiver mBroadcastReceiver;

    SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image_list = (ListView) findViewById(R.id.list_view);
        imageAdapter = new ImageAdapter(this, imgid);
        image_list.setAdapter(imageAdapter);


        listViewClickLister();


        //caling fucntion for in-app
        keyInitialization();
        startUpLab();
        loadData();





    }//end of onCreate

    public void listViewClickLister(){

        image_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                long position = image_list.getItemIdAtPosition(i);
                Log.e("TAG", "Curren Item Id is " + position);


                if (position==0) {

                    showRecipeProcedure(position);
                }
                if (position==1) {

                    showRecipeProcedure(position);
                }
                if (position==2) {

                    showRecipeProcedure(position);
                }
                if (position==3) {

                    currentPosition = position;

                    if (ifBought==3){
                        showRecipeProcedure(position);
                    }else {

                        buyRecipe3();
                    }

                    //showRecipeProcedure(position);
                }
                if (position==4) {

                    currentPosition = position;

                    if (ifBought==4){
                        showRecipeProcedure(position);
                    }else {

                        buyRecipe4();
                    }

                    //showRecipeProcedure(position);
                }
                if (position==5) {

                    currentPosition = position;

                    if (ifBought==5){
                        showRecipeProcedure(position);
                    }else {

                        buyRecipe5();
                    }

                    //showRecipeProcedure(position);
                }
                if (position==6) {

                    currentPosition = position;

                    if (ifBought==6){
                        showRecipeProcedure(position);
                    }else {

                        buyRecipe6();
                    }


                    //showRecipeProcedure(position);
                }
                if (position==7) {

                    currentPosition = position;

                    if (ifBought==7){
                        showRecipeProcedure(position);
                    }else {

                        buyRecipe7();
                    }

                    //showRecipeProcedure(position);
                }
                if (position==8) {

                    currentPosition = position;

                    if (ifBought==8){
                        showRecipeProcedure(position);
                    }else {

                        buyRecipe8();
                    }

                    //showRecipeProcedure(position);
                }
                if (position==9) {

                    currentPosition = position;

                    if (ifBought==9){
                        showRecipeProcedure(position);
                    }else {

                        buyRecipe9();
                    }

                    //showRecipeProcedure(position);
                }

            }
        });
    }//end of listView Click

    public void showRecipeProcedure(long position){
        Intent veiwRecipeMethod = new Intent(MainActivity.this, ViewRecipe.class);
        veiwRecipeMethod.putExtra("id", position);
        startActivity(veiwRecipeMethod);
        Toast.makeText(MainActivity.this, "Please Wait...", Toast.LENGTH_SHORT).show();
    }//end of showRecipeProdcedure


    //starting in app billing <code>   ***************************** Developer Shoaib Anwar *****************************
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == IAPCODE )
        {
            android.util.Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
            if (mHelper == null) return;

            // Pass on the activity result to the helper for handling
            if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {




                super.onActivityResult(requestCode, resultCode, data);
            }
            else {
                android.util.Log.d(TAG, "onActivityResult handled by IABUtil.");
            }

        }

    }//end of onActivityResult




    @Override
    protected void onDestroy() {
        super.onDestroy();

        inAppBilling_onDestroy();

    }


    private static final char[] payloadSymbols = new char[36];

    static {
        for (int idx = 0; idx < 10; ++idx)
            payloadSymbols[idx] = (char) ('0' + idx);
        for (int idx = 10; idx < 36; ++idx)
            payloadSymbols[idx] = (char) ('a' + idx - 10);
    }


//
//inappbilling start
//




    public void inAppBilling_onDestroy()
    {

        // very important:
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }
        if (mHelper != null) try {
            mHelper.dispose();
            mHelper.disposeWhenFinished();
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
        mHelper = null;
    }





    // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            android.util.Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isFailure()) {
                complain("Error purchasing: " + result);
                setWaitScreen(false);
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                complain("Error purchasing. Authenticity verification failed.");
                setWaitScreen(false);
                return;
            }

            android.util.Log.d(TAG, "Purchase successful.");

            //*********************************** Method for Reciepes *******************
            //***************recipe 3*****************
            if (currentPosition == 3) {
                if (purchase.getSku().equals(SKU_3_RECIPE)) {
                    // bought 1/4 tank of gas. So consume it.
                    android.util.Log.d(TAG, "Purchase is Star Goggle. Starting Goggle consumption.");
                    try {
                        mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming Goggle. Another async operation in progress.");
                        setWaitScreen(false);
                        return;
                    }
                }
            }//end of recipe 3

            //***************recipe 4*****************
            if (currentPosition == 4) {
                if (purchase.getSku().equals(SKU_4_RECIPE)) {
                    // bought 1/4 tank of gas. So consume it.
                    android.util.Log.d(TAG, "Purchase is Star Goggle. Starting Goggle consumption.");
                    try {
                        mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming Goggle. Another async operation in progress.");
                        setWaitScreen(false);
                        return;
                    }
                }
            }//end of recipe 4

            //***************recipe 5*****************
            if (currentPosition == 5) {
                if (purchase.getSku().equals(SKU_5_RECIPE)) {
                    // bought 1/4 tank of gas. So consume it.
                    android.util.Log.d(TAG, "Purchase is Star Goggle. Starting Goggle consumption.");
                    try {
                        mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming Goggle. Another async operation in progress.");
                        setWaitScreen(false);
                        return;
                    }
                }
            }//end of recipe 5

            //***************recipe 6*****************
            if (currentPosition == 6) {
                if (purchase.getSku().equals(SKU_6_RECIPE)) {
                    // bought 1/4 tank of gas. So consume it.
                    android.util.Log.d(TAG, "Purchase is Star Goggle. Starting Goggle consumption.");
                    try {
                        mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming Goggle. Another async operation in progress.");
                        setWaitScreen(false);
                        return;
                    }
                }
            }//end of recipe 6

            //***************recipe 7*****************
            if (currentPosition == 7) {
                if (purchase.getSku().equals(SKU_7_RECIPE)) {
                    // bought 1/4 tank of gas. So consume it.
                    android.util.Log.d(TAG, "Purchase is Star Goggle. Starting Goggle consumption.");
                    try {
                        mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming Goggle. Another async operation in progress.");
                        setWaitScreen(false);
                        return;
                    }
                }
            }//end of recipe 7

            //***************recipe 8*****************
            if (currentPosition == 8) {
                if (purchase.getSku().equals(SKU_8_RECIPE)) {
                    // bought 1/4 tank of gas. So consume it.
                    android.util.Log.d(TAG, "Purchase is Star Goggle. Starting Goggle consumption.");
                    try {
                        mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming Goggle. Another async operation in progress.");
                        setWaitScreen(false);
                        return;
                    }
                }
            }//end of recipe 8

            //***************recipe 9*****************
            if (currentPosition == 9) {
                if (purchase.getSku().equals(SKU_9_RECIPE)) {
                    // bought 1/4 tank of gas. So consume it.
                    android.util.Log.d(TAG, "Purchase is Star Goggle. Starting Goggle consumption.");
                    try {
                        mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming Goggle. Another async operation in progress.");
                        setWaitScreen(false);
                        return;
                    }
                }
            }//end of recipe 9



            //********************************************* Ranglerz Team (Shoaib Anwar) **********************************

        }
    };



    @Override
    public void receivedBroadcast() {
// Received a broadcast notification that the inventory of items has changed
        android.util.Log.d(TAG, "Received broadcast notification. Querying inventory.");
        try {
            mHelper.queryInventoryAsync(mGotInventoryListener);
        } catch (IabHelper.IabAsyncInProgressException e) {
            android.util.Log.e(TAG, "Error querying inventory. Another async operation in progress.");
        }
    }




    public class RandomString {

       /*
        * static { for (int idx = 0; idx < 10; ++idx) symbols[idx] = (char)
        * ('0' + idx); for (int idx = 10; idx < 36; ++idx) symbols[idx] =
        * (char) ('a' + idx - 10); }
        */


        private final Random random = new Random();

        private final char[] buf;

        public RandomString(int length) {
            if (length < 1)
                throw new IllegalArgumentException("length < 1: " + length);
            buf = new char[length];
        }

        public String nextString() {
            for (int idx = 0; idx < buf.length; ++idx)
                buf[idx] = payloadSymbols[random.nextInt(payloadSymbols.length)];
            return new String(buf);
        }

    }



    public void keyInitialization(){
        String base64EncodedPublicKey = "put they for recipe here from googel console";

        // Some sanity checks to see if the developer (that's you!) really followed the
        // instructions to run this sample (don't put these checks on your app!)
        if (base64EncodedPublicKey.contains("CONSTRUCT_YOUR")) {
            throw new RuntimeException("Please put your app's public key in MainActivity.java. See README.");
        }
        if (getPackageName().startsWith("com.example")) {
            throw new RuntimeException("Please change the sample's package name! See README.");
        }

        // Create the helper, passing it our context and the public key to verify signatures with
        android.util.Log.d(TAG, "Creating IAB helper.");
        mHelper = new IabHelper(this, base64EncodedPublicKey);

        // enable debug logging (for a production application, you should set this to false).
        mHelper.enableDebugLogging(true);



    }//end of keyInitialization

    public void startUpLab(){
        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        android.util.Log.d(TAG, "Starting setup.");

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                android.util.Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    complain("Problem setting up in-app billing: " + result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                mBroadcastReceiver = new IabBroadcastReceiver(MainActivity.this);
                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                registerReceiver(mBroadcastReceiver, broadcastFilter);

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                android.util.Log.d(TAG, "Setup successful. Querying inventory.");
                try {
                    mHelper.queryInventoryAsync(mGotInventoryListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    complain("Error querying inventory. Another async operation in progress.");
                }
            }
        });


    }//end of startup lab


    // Listener that's called when we finish querying the items and subscriptions we own
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            android.util.Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
                complain("Failed to query inventory: " + result);
                return;
            }

            android.util.Log.d(TAG, "Query inventory was successful.");

            // Check for gas delivery -- if we own gas, we should fill up the tank immediately

            //************************************** for Recipe Perchase ********************************

            //************for Recipe 3 perchase*********************
            if (currentPosition==3){
                Purchase gasPurchase = inventory.getPurchase(SKU_3_RECIPE);
                android.util.Log.d(TAG, "Inventory Purchase " + gasPurchase);

                if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
                    android.util.Log.d(TAG, "We have gas. Consuming it.");
                    try {
                        mHelper.consumeAsync(inventory.getPurchase(SKU_3_RECIPE), mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming gas. Another async operation in progress.");
                    }
                    return;
                }
            }//end for Recipe 3 perchase


            //************for Recipe 4 perchase*********************
            if (currentPosition==4){
                Purchase gasPurchase = inventory.getPurchase(SKU_4_RECIPE);
                android.util.Log.d(TAG, "Inventory Purchase " + gasPurchase);

                if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
                    android.util.Log.d(TAG, "We have gas. Consuming it.");
                    try {
                        mHelper.consumeAsync(inventory.getPurchase(SKU_4_RECIPE), mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming gas. Another async operation in progress.");
                    }
                    return;
                }
            }//end for Recipe 4 perchase

            //************for Recipe 5 perchase*********************
            if (currentPosition==5){
                Purchase gasPurchase = inventory.getPurchase(SKU_5_RECIPE);
                android.util.Log.d(TAG, "Inventory Purchase " + gasPurchase);

                if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
                    android.util.Log.d(TAG, "We have gas. Consuming it.");
                    try {
                        mHelper.consumeAsync(inventory.getPurchase(SKU_5_RECIPE), mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming gas. Another async operation in progress.");
                    }
                    return;
                }
            }//end for Recipe 5 perchase

            //************for Recipe 6 perchase*********************
            if (currentPosition==6){
                Purchase gasPurchase = inventory.getPurchase(SKU_6_RECIPE);
                android.util.Log.d(TAG, "Inventory Purchase " + gasPurchase);

                if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
                    android.util.Log.d(TAG, "We have gas. Consuming it.");
                    try {
                        mHelper.consumeAsync(inventory.getPurchase(SKU_6_RECIPE), mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming gas. Another async operation in progress.");
                    }
                    return;
                }
            }//end for Recipe 6 perchase

            //************for Recipe 7 perchase*********************
            if (currentPosition==7){
                Purchase gasPurchase = inventory.getPurchase(SKU_7_RECIPE);
                android.util.Log.d(TAG, "Inventory Purchase " + gasPurchase);

                if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
                    android.util.Log.d(TAG, "We have gas. Consuming it.");
                    try {
                        mHelper.consumeAsync(inventory.getPurchase(SKU_7_RECIPE), mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming gas. Another async operation in progress.");
                    }
                    return;
                }
            }//end for Recipe 7 perchase

            //************for Recipe 8 perchase*********************
            if (currentPosition==8){
                Purchase gasPurchase = inventory.getPurchase(SKU_8_RECIPE);
                android.util.Log.d(TAG, "Inventory Purchase " + gasPurchase);

                if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
                    android.util.Log.d(TAG, "We have gas. Consuming it.");
                    try {
                        mHelper.consumeAsync(inventory.getPurchase(SKU_8_RECIPE), mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming gas. Another async operation in progress.");
                    }
                    return;
                }
            }//end for Recipe 8 perchase

            //************for Recipe 9 perchase*********************
            if (currentPosition==9){
                Purchase gasPurchase = inventory.getPurchase(SKU_9_RECIPE);
                android.util.Log.d(TAG, "Inventory Purchase " + gasPurchase);

                if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
                    android.util.Log.d(TAG, "We have gas. Consuming it.");
                    try {
                        mHelper.consumeAsync(inventory.getPurchase(SKU_9_RECIPE), mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming gas. Another async operation in progress.");
                    }
                    return;
                }
            }//end for Recipe 9 perchase


            //************************************** end for Recipe Perchase ********************************

            setWaitScreen(false);
            android.util.Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };

    // Called when consumption is complete
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            android.util.Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            // We know this is the "gas" sku because it's the only one we consume,
            // so we don't check which sku was consumed. If you have more than one
            // sku, you probably should check...
            if (result.isSuccess()) {
                // successfully consumed, so we apply the effects of the item in our
                // game world's logic, which in our case means filling the gas tank a bit
                android.util.Log.d(TAG, "Consumption successful. Provisioning.");
                //mTank = mTank == TANK_MAX ? TANK_MAX : mTank + 1;
                //saveData();
                alert("You have successfully buy glass");

                onSuccessMobileBought();




                //alert("You filled 1/4 tank. Your tank is now " + String.valueOf(mTank) + "/4 full!");
            }
            else {
                complain("Error while consuming: " + result);
            }
            //updateUi();

            setWaitScreen(false);
            android.util.Log.d(TAG, "End consumption flow.");
        }
    };


    void complain(String message) {
        android.util.Log.e(TAG, "**** TrivialDrive Error: " + message);
        alert("Error: " + message);
    }


    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        android.util.Log.d(TAG, "Showing alert dialog: " + message);
        bld.setCancelable(false);
        bld.create().show();
    }


    //************************************ Starts for methods for buy Recipe x ************************************

    //buy recipe 3
    public void buyRecipe3(){
        if (mSubscribedToInfiniteGas) {
            complain("No need! You're subscribed to infinite gas. Isn't that awesome?");
            return;
        }


        // launch the gas purchase UI flow.
        // We will be notified of completion via mPurchaseFinishedListener
        setWaitScreen(true);
        android.util.Log.d(TAG, "Launching purchase flow for gas.");

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";
        android.util.Log.e("testTag", "Buy Call");
        RandomString randomString = new RandomString(36);
        devPayLoad = randomString.nextString();
        payload = devPayLoad;

        try {
            mHelper.launchPurchaseFlow(this, SKU_3_RECIPE, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error launching purchase flow. Another async operation in progress.");
            setWaitScreen(false);
        }
    }//end of buyRecipe3


    //buy recipe 4
    public void buyRecipe4(){
        if (mSubscribedToInfiniteGas) {
            complain("No need! You're subscribed to infinite gas. Isn't that awesome?");
            return;
        }


        // launch the gas purchase UI flow.
        // We will be notified of completion via mPurchaseFinishedListener
        setWaitScreen(true);
        android.util.Log.d(TAG, "Launching purchase flow for gas.");

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";
        android.util.Log.e("testTag", "Buy Call");
        RandomString randomString = new RandomString(36);
        devPayLoad = randomString.nextString();
        payload = devPayLoad;

        try {
            mHelper.launchPurchaseFlow(this, SKU_4_RECIPE, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error launching purchase flow. Another async operation in progress.");
            setWaitScreen(false);
        }
    }//end of buyRecipe4

    //buy recipe 5
    public void buyRecipe5(){
        if (mSubscribedToInfiniteGas) {
            complain("No need! You're subscribed to infinite gas. Isn't that awesome?");
            return;
        }


        // launch the gas purchase UI flow.
        // We will be notified of completion via mPurchaseFinishedListener
        setWaitScreen(true);
        android.util.Log.d(TAG, "Launching purchase flow for gas.");

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";
        android.util.Log.e("testTag", "Buy Call");
        RandomString randomString = new RandomString(36);
        devPayLoad = randomString.nextString();
        payload = devPayLoad;

        try {
            mHelper.launchPurchaseFlow(this, SKU_5_RECIPE, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error launching purchase flow. Another async operation in progress.");
            setWaitScreen(false);
        }
    }//end of buyRecipe5

    //buy recipe 6
    public void buyRecipe6(){
        if (mSubscribedToInfiniteGas) {
            complain("No need! You're subscribed to infinite gas. Isn't that awesome?");
            return;
        }


        // launch the gas purchase UI flow.
        // We will be notified of completion via mPurchaseFinishedListener
        setWaitScreen(true);
        android.util.Log.d(TAG, "Launching purchase flow for gas.");

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";
        android.util.Log.e("testTag", "Buy Call");
        RandomString randomString = new RandomString(36);
        devPayLoad = randomString.nextString();
        payload = devPayLoad;

        try {
            mHelper.launchPurchaseFlow(this, SKU_6_RECIPE, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error launching purchase flow. Another async operation in progress.");
            setWaitScreen(false);
        }
    }//end of buyRecipe6

    //buy recipe 7
    public void buyRecipe7(){
        if (mSubscribedToInfiniteGas) {
            complain("No need! You're subscribed to infinite gas. Isn't that awesome?");
            return;
        }


        // launch the gas purchase UI flow.
        // We will be notified of completion via mPurchaseFinishedListener
        setWaitScreen(true);
        android.util.Log.d(TAG, "Launching purchase flow for gas.");

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";
        android.util.Log.e("testTag", "Buy Call");
        RandomString randomString = new RandomString(36);
        devPayLoad = randomString.nextString();
        payload = devPayLoad;

        try {
            mHelper.launchPurchaseFlow(this, SKU_7_RECIPE, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error launching purchase flow. Another async operation in progress.");
            setWaitScreen(false);
        }
    }//end of buyRecipe7

    //buy recipe 8
    public void buyRecipe8(){
        if (mSubscribedToInfiniteGas) {
            complain("No need! You're subscribed to infinite gas. Isn't that awesome?");
            return;
        }


        // launch the gas purchase UI flow.
        // We will be notified of completion via mPurchaseFinishedListener
        setWaitScreen(true);
        android.util.Log.d(TAG, "Launching purchase flow for gas.");

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";
        android.util.Log.e("testTag", "Buy Call");
        RandomString randomString = new RandomString(36);
        devPayLoad = randomString.nextString();
        payload = devPayLoad;

        try {
            mHelper.launchPurchaseFlow(this, SKU_8_RECIPE, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error launching purchase flow. Another async operation in progress.");
            setWaitScreen(false);
        }
    }//end of buyRecipe8

    //buy recipe 9
    public void buyRecipe9(){
        if (mSubscribedToInfiniteGas) {
            complain("No need! You're subscribed to infinite gas. Isn't that awesome?");
            return;
        }


        // launch the gas purchase UI flow.
        // We will be notified of completion via mPurchaseFinishedListener
        setWaitScreen(true);
        android.util.Log.d(TAG, "Launching purchase flow for gas.");

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";
        android.util.Log.e("testTag", "Buy Call");
        RandomString randomString = new RandomString(36);
        devPayLoad = randomString.nextString();
        payload = devPayLoad;

        try {
            mHelper.launchPurchaseFlow(this, SKU_9_RECIPE, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error launching purchase flow. Another async operation in progress.");
            setWaitScreen(false);
        }
    }//end of buyRecipe9



//********************************** End for methods Buy Recipe *********************************


    // Enables or disables the "please wait" screen.
    void setWaitScreen(boolean set) {

        Toast.makeText(getApplicationContext(), "Please Wait...", Toast.LENGTH_SHORT).show();

		/*findViewById(R.id.screen_main).setVisibility(set ? View.GONE : View.VISIBLE);
		findViewById(R.id.screen_wait).setVisibility(set ? View.VISIBLE : View.GONE);*/
    }

    /** Verifies the developer payload of a purchase. */
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();



        return true;
    }




    @Override
    protected void onResume() {
        super.onResume();

        loadData();

    }

    //hinding views when bought invertory successfull
    public void onSuccessMobileBought(){
        sharedPreferences = getSharedPreferences("recipe", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

       //start
        if (currentPosition==3){
            //additing values to sharepreferences
            editor.putInt("recipe3", 3);//int value 1 is for stargoggle

            showRecipeProcedure(3);



        }//end
        if (currentPosition==4){
            //additing values to sharepreferences
            editor.putInt("recipe4", 4);//int value 1 is for stargoggle
            showRecipeProcedure(4);

        }//end
        if (currentPosition==5){
            //additing values to sharepreferences
            editor.putInt("recipe5", 5);//int value 1 is for stargoggle

        }//end
        if (currentPosition==6){
            //additing values to sharepreferences
            editor.putInt("recipe6", 6);//int value 1 is for stargoggle
            showRecipeProcedure(6);

        }//end
        if (currentPosition==7){
            //additing values to sharepreferences
            editor.putInt("recipe7", 7);//int value 1 is for stargoggle
            showRecipeProcedure(7);

        }//end
        if (currentPosition==8){
            //additing values to sharepreferences
            editor.putInt("recipe8", 8);//int value 1 is for stargoggle
            showRecipeProcedure(8);

        }//end
        if (currentPosition==9){
            //additing values to sharepreferences
            editor.putInt("recipe9", 9);//int value 1 is for stargoggle

            showRecipeProcedure(9);

        }//end


        editor.commit();




    }//end of on Successfully recipe bought

    public void loadData(){
        sharedPreferences = getSharedPreferences("recipe", 0);

        if(sharedPreferences!=null) {
            int boughtValue3 = sharedPreferences.getInt("recipe3", 0);//defualt values is zero for all
            int boughtValue4 = sharedPreferences.getInt("recipe4", 0);//defualt values is zero for all
            int boughtValue5 = sharedPreferences.getInt("recipe5", 0);//defualt values is zero for all
            int boughtValue6 = sharedPreferences.getInt("recipe6", 0);//defualt values is zero for all
            int boughtValue7 = sharedPreferences.getInt("recipe7", 0);//defualt values is zero for all
            int boughtValue8 = sharedPreferences.getInt("recipe8", 0);//defualt values is zero for all
            int boughtValue9 = sharedPreferences.getInt("recipe9", 0);//defualt values is zero for all



            if (boughtValue3==3){

                ifBought = 3;
            }

            if (boughtValue4==4){

                ifBought = 4;

            }

            if (boughtValue5==5){

                ifBought = 5;

            }

            if (boughtValue6==6){

                ifBought = 6;

            }

            if (boughtValue7==7){

                ifBought = 7;

            }

            if (boughtValue8==8){

                ifBought = 8;

            }

            if (boughtValue9==9){

                ifBought = 9;

            }

        }//end of check for sharepreference


    }//end of loadData



}