    package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Event;
import com.thesis.dont.loyaltypointadmin.models.EventModel;
import com.thesis.dont.loyaltypointadmin.models.Global;
import com.thesis.dont.loyaltypointadmin.models.Shop;

import java.util.ArrayList;
import java.util.Calendar;

public class CardEditEventActivity extends ActionBarActivity implements DatePickerDialog.OnDateSetListener {

    private static final int SELECT_PHOTO = 100;
    private static final int SCAN_BARCODE = 49374;

    ButtonFlat dateStartButton, dateEndButton;
    ButtonRectangle nextButton, cancelBtn;
    ImageView iconChooser;
    EditText eventName, description;
    boolean isStartDatePicker = true, isScanBarCode = false;
    ProgressDialog mDialog;
    Event oldEvent;
    CreateEvent1Fragment createEvent1Fragment;
    CreateEvent2Fragment createEvent2Fragment;
    static Picasso mPicasso;
    private String cardId;
    Bitmap eventLogo = null;
    boolean isChangeEventImage = false;
    ArrayList<Shop> listShop = new ArrayList<Shop>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_edit_event);

        mDialog = new ProgressDialog(this);
        mDialog.setTitle("Updating event info");
        mDialog.setMessage("Please wait...");
        mDialog.setCancelable(false);
        mPicasso = Picasso.with(this);

        Intent i = getIntent();
        oldEvent = (Event) i.getParcelableExtra(ShopEventsFragment.EVENT_OBJECT);
        listShop = i.getParcelableArrayListExtra(Global.SHOP_LIST_OBJECT);
        cardId = i.getStringExtra(Global.CARD_ID);
        Calendar c = Calendar.getInstance();
        /** create value to list and add event change frangment**/
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.eventCategory, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Create new fragment and transaction
        if (oldEvent.getType() == 1) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            createEvent2Fragment = new CreateEvent2Fragment();
            fragmentTransaction.replace(R.id.fragment_container, createEvent2Fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            createEvent1Fragment = new CreateEvent1Fragment();
            fragmentTransaction.replace(R.id.fragment_container, createEvent1Fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        /****/
        eventName = (EditText) findViewById(R.id.eventname);
        eventName.setText(oldEvent.getName());
        description = (EditText) findViewById(R.id.descriptionEdtText);
        description.setText(oldEvent.getDescription());
        dateStartButton = (ButtonFlat) findViewById(R.id.startDateButton);
        dateStartButton.setText("start date\n" + oldEvent.getTime_start());
        dateEndButton = (ButtonFlat) findViewById(R.id.endDateButton);
        dateEndButton.setText("end date\n" + oldEvent.getTime_end());
        nextButton = (ButtonRectangle) findViewById(R.id.nextEventBtn);
        cancelBtn = (ButtonRectangle) findViewById(R.id.cancelBtn);
        iconChooser = (ImageView) findViewById(R.id.eventLogo);
        iconChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CardEditEventActivity.this, CropImageActivity.class);
                i.putExtra(CropImageActivity.ASPECT_RATIO_X, 1);
                i.putExtra(CropImageActivity.ASPECT_RATIO_Y, 1);
                startActivityForResult(i, SELECT_PHOTO);
            }
        });

        mPicasso.load(oldEvent.getImage()).placeholder(R.drawable.ic_award).into(iconChooser);

        //Choose day by show the DatePickerFragment
        dateStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment("EditEventActivity");
                newFragment.show(getSupportFragmentManager(), "datePicker");
                isStartDatePicker = true;
            }
        });
        //Choose day by show the DatePickerFragment
        dateEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment("EditEventActivity");
                newFragment.show(getSupportFragmentManager(), "datePicker");
                isStartDatePicker = false;
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseEventCreateFragment fragment = (BaseEventCreateFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);

                if (eventName.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Event name is empty!", Toast.LENGTH_LONG).show();
                } else if (!fragment.isFilledIn().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), fragment.isFilledIn(), Toast.LENGTH_LONG).show();
                } else {

                    Event event = new Event(oldEvent.getId(), oldEvent.getType(), eventName.getText().toString(),
                            dateStartButton.getText().toString().substring(11), dateEndButton.getText().toString().substring(9),
                            description.getText().toString(), fragment.getBarCode(), fragment.getGoodsName(), Float.parseFloat(fragment.getRatio()),
                            Integer.parseInt(fragment.getNumber()), Integer.parseInt(fragment.getPoint()), "");
                    Intent i = new Intent(CardEditEventActivity.this, CardShopListApplyEventActivity.class);
                    i.putExtra(Global.EVENT_OBJECT, event);
                    i.putExtra(Global.CARD_ID, cardId);
                    i.putParcelableArrayListExtra(Global.SHOP_LIST_OBJECT, listShop);
                    i.putExtra(Global.EVENT_LIST_TYPE, Global.CARD_EDIT_EVENT_LIST);
                    Global.tempActivity = CardEditEventActivity.this;
                    if(isChangeEventImage){
                        Global.tempBitmap = eventLogo;
                    }
                    startActivity(i);
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //call back when pick date
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if (isStartDatePicker) {
            dateStartButton.setText("start date\n" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
        } else {
            dateEndButton.setText("end date\n" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (oldEvent.getType() == 1) {
            createEvent2Fragment.setRatio(String.valueOf(oldEvent.getRatio()));
        } else {
            createEvent1Fragment.setPoint(String.valueOf(oldEvent.getPoint()));
            if(!isScanBarCode) {
                createEvent1Fragment.setBarCode(oldEvent.getBarcode());
            }
            isScanBarCode = false;
            createEvent1Fragment.setGoodsName(oldEvent.getGoods_name());
            createEvent1Fragment.setNumber(String.valueOf(oldEvent.getNumber()));
        }

    }

    //call back when scan bar code successfully
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case SELECT_PHOTO: {
                if(resultCode == RESULT_OK){
                    byte[] imageByteArray = data.getByteArrayExtra(CropImageActivity.CROPPED_IMAGE);

                    // không nén ảnh
                    eventLogo = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
                    iconChooser.setImageBitmap(eventLogo);

                    isChangeEventImage = true;
                }
                break;
            }
            case SCAN_BARCODE: {
                IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                createEvent1Fragment.setBarCode(scanningResult.getContents());
                isScanBarCode = true;
            }
        }
    }

    @Override
    public void onBackPressed() {

        finish();
    }
}
