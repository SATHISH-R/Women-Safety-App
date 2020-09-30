package com.rsr.womensafety;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {


    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> mobile = new ArrayList<>();

    Context context;

    public RecyclerAdapter(Context context1, ArrayList<String> Nname, ArrayList<String> Mmobile){

        context = context1;
        name = Nname;
        mobile = Mmobile;

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView Name;
        public TextView Mobile;
        public RelativeLayout delete;

        public ViewHolder(View itemView) {
            super(itemView);

            delete = itemView.findViewById(R.id.delete);

            Name =
                    (TextView) itemView.findViewById(R.id.card_name);
            Mobile =
                    (TextView) itemView.findViewById(R.id.card_phone);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Snackbar.make(v, "Name: " + name.get(position),
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();


                }
            });

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_alert, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.Name.setText(name.get(i).toString());
        viewHolder.Mobile.setText(mobile.get(i).toString());

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position = i;

                ArrayList<String> Name = new ArrayList<String>();
                ArrayList<String> Mobile = new ArrayList<String>();

                SessionManager sessionManager = new SessionManager(context);

                Name = sessionManager.getArrayList("userName");
                Mobile = sessionManager.getArrayList("userMobile");

                Name.remove(name.get(position));
                Mobile.remove(mobile.get(position));

                sessionManager.saveArrayList(Name, "userName");
                sessionManager.saveArrayList(Mobile, "userMobile");

                Intent intent = new Intent(context, AddAlert.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);


            }
        });

    }

    @Override
    public int getItemCount() {
        return name.size();
    }


}