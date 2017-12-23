package com.example.asif.useractivityrecognition.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.asif.useractivityrecognition.Model.GeofencePosition;
import com.example.asif.useractivityrecognition.R;

import java.util.List;

/**
 * Created by Asif on 23/12/2017.
 */

public class GeofencePositionAdapter extends RecyclerView.Adapter<GeofencePositionAdapter.MyViewHolder> {

    private Context ctx;
    private List<GeofencePosition> mGeofencePositions;
    private LayoutInflater layoutInflater;

    public GeofencePositionAdapter(Context context, List<GeofencePosition> geofencePositions) {
        ctx = context;
        mGeofencePositions = geofencePositions;
        layoutInflater = LayoutInflater.from(ctx);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.row_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        GeofencePosition mGeofencePosition = mGeofencePositions.get(position);

        holder.textViewId.setText(mGeofencePosition.getGeofenceId());

        String textPosition = mGeofencePosition.getLatitude()+", "+mGeofencePosition.getLongitude();
        holder.textViewPosition.setText(textPosition);
    }

    @Override
    public int getItemCount() {
        return mGeofencePositions.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewId, textViewPosition;

        MyViewHolder(View itemView) {
            super(itemView);

            textViewId = itemView.findViewById(R.id.textViewId);
            textViewPosition = itemView.findViewById(R.id.textViewPosition);

        }
    }

}
