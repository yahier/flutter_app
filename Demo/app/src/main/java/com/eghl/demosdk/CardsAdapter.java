package com.eghl.demosdk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eghl.demosdk.models.Card;

import java.util.List;

/**
 * Created by Jezer Crespo on 3/1/2017.
 */

public class CardsAdapter extends BaseAdapter {

    private Context context;
    private List<Card> cardList;

    public CardsAdapter(Context context, List<Card> cardList) {
        this.context = context;
        this.cardList = cardList;
    }

    @Override
    public int getCount() {
        return cardList.size();
    }

    @Override
    public Object getItem(int position) {
        return cardList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return cardList.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.row_cards,parent,false);
        }
        TextView lastFour = (TextView) convertView.findViewById(R.id.lastFour);
        TextView brand = (TextView) convertView.findViewById(R.id.brand);

        lastFour.setText("***"+cardList.get(position).getLastFour());
        brand.setText(cardList.get(position).getBrandName());

        return convertView;
    }
}