package com.thesis.dont.loyaltypointuser.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointuser.R;
import com.thesis.dont.loyaltypointuser.models.CardModel;
import com.thesis.dont.loyaltypointuser.models.Event;
import com.thesis.dont.loyaltypointuser.models.Global;
import com.thesis.dont.loyaltypointuser.models.Shop;

import java.util.ArrayList;

import butterknife.ButterKnife;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.view.CardGridView;

public class CardEventsFragment extends Fragment {
    private static final String ARG_POSITION = "position";
    private static final String ARG_CARDID = "card_id";
    public static final String EVENT_OBJECT = "event_obj";
    public static final String SHOP_ID = "shop_id";

    //    @InjectView(R.id.textView)

    private CardGridView mListView;
    CardGridArrayAdapter mAdapter;
    Activity mParentActivity;

    SwipeRefreshLayout mSwipeLayout;
    View rootView;

    private int position;
    private com.thesis.dont.loyaltypointuser.models.Card mCard;

    public CardEventsFragment() {
    }

    public CardEventsFragment(int position, com.thesis.dont.loyaltypointuser.models.Card mCard) {
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        b.putParcelable(ARG_CARDID, mCard);
        this.setArguments(b);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
        mCard = getArguments().getParcelable(ARG_CARDID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_card_events, container, false);
        ButterKnife.inject(this, rootView);
        ViewCompat.setElevation(rootView, 50);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mParentActivity = getActivity();
        mAdapter = new CardGridArrayAdapter(mParentActivity, new ArrayList<Card>());
        mListView = (CardGridView) rootView.findViewById(R.id.listEvents);
        mListView.setAdapter(mAdapter);

        mSwipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        mSwipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doRefresh();
            }
        });

        doRefresh();
    }

    public void doRefresh() {
        mSwipeLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(true);
                getListEvents();
            }
        });
    }

    public class EventCard extends Card {

        protected TextView eventNameTv, eventDateTv, eventPointTv, eventShopNameTv;
        protected ImageView eventImgIv;
        protected Event event;
        protected ArrayList<Shop> listShops = new ArrayList<Shop>();

        protected String eventName, eventDate, eventPoint, eventImg, eventShopName;

        public EventCard(Context context) {
            super(context, R.layout.card_event_list_row);
        }

        public EventCard(Context context, int innerLayout) {
            super(context, innerLayout);
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {

            //Populate the inner elements

            eventNameTv = (TextView) view.findViewById(R.id.eventName);
            eventNameTv.setText(eventName);

            eventDateTv = (TextView) view.findViewById(R.id.eventDate);
            //eventDateTv.setText(eventDate);
            eventDateTv.setText(Helper.setTextStyleItalic(eventDate));

            eventPointTv = (TextView) view.findViewById(R.id.eventPoint);
            eventPointTv.setText(eventPoint);

            eventShopNameTv = (TextView) view.findViewById(R.id.eventShopName);
            eventShopNameTv.setText(eventShopName);
            //eventPointTv.setTextColor(Color.rgb(0, 100, 0));

            eventImgIv = (ImageView) view.findViewById(R.id.eventImg);
            if (eventImg == null || eventImg.equals(""))
                eventImg = null;
            Picasso.with(mParentActivity).load(eventImg).placeholder(R.drawable.ic_about).into(eventImgIv);
        }

    }



    public void getListEvents() {
        CardModel.getListEvents(mCard.getId(), new CardModel.OnGetListEventResult() {
            @Override
            public void onSuccess(final ArrayList<Event> listEvents, final ArrayList<ArrayList<Shop>> listShops) {
                mParentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.clear();
                        for (int i = 0; i < listEvents.size(); i++) {

                            EventCard card = new EventCard(mParentActivity);

                            //Only for test, use different titles and ratings
                            card.eventName = listEvents.get(i).getName();
                            card.eventDate = listEvents.get(i).getTime_start() + " - " + listEvents.get(i).getTime_end();
                            card.eventPoint = String.valueOf(listEvents.get(i).getPoint()) + " points";
                            card.eventImg = listEvents.get(i).getImage();
                            card.event = listEvents.get(i);
                            card.listShops = listShops.get(i);
                            String shopListName = "";
                            for(int j = 0; j<listShops.get(i).size();j++){
                                if(j == (listShops.get(i).size() -1)){
                                    shopListName = shopListName + listShops.get(i).get(j).getName();
                                }else {
                                    shopListName = shopListName + listShops.get(i).get(j).getName() + ", ";
                                }
                            }
                            card.eventShopName = shopListName;
                            card.setOnClickListener(new Card.OnCardClickListener() {
                                @Override
                                public void onClick(Card card, View view) {
                                    Intent i = new Intent(mParentActivity, EventDetailActivity.class);
                                    i.putExtra(EVENT_OBJECT, ((EventCard) card).event);
                                    i.putParcelableArrayListExtra(Global.SHOP_ARRAY_OBJECT, ((EventCard) card).listShops);
                                    startActivity(i);
                                }
                            });

                            mAdapter.add(card);
                        }
                        mAdapter.notifyDataSetChanged();
                        mSwipeLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onError(final String error) {
                mParentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Get listEvents không thành công
                        mSwipeLayout.setRefreshing(false);
                        Toast.makeText(mParentActivity, error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
