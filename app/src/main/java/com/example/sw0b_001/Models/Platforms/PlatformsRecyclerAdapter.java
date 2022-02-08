package com.example.sw0b_001.Models.Platforms;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sw0b_001.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PlatformsRecyclerAdapter extends RecyclerView.Adapter<PlatformsRecyclerAdapter.ViewHolder> {

    public Context context;
    public List<Platform> platforms;
    public int customLayout;

    public PlatformsRecyclerAdapter(Context context, List<Platform> platforms, int layout){
        this.context = context;
        this.platforms = platforms;
        this.customLayout = layout;
    }

    public void update(List<Platform> platforms) {
        this.platforms = platforms;
        this.notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(this.customLayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Platform platform = this.platforms.get(position);
        holder.name.setText(platform.getName());
        holder.description.setText(platform.getDescription());
        holder.provider.setText(platform.getProvider());
        holder.type.setText(platform.getType());
        holder.image.setImageResource(platform.getImage());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent = Platforms.getIntent(context.getApplicationContext(), platform.getProvider(), platform.getName());
                Intent intent = Platform.getIntent(context.getApplicationContext(), platform.getName(), platform.getType());
//                Log.i("onclick" + platform.getName(), platform.getType());
                intent.putExtra("platform_id", platform.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.platforms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, description, provider, type;
        ImageView image;
        ConstraintLayout layout;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.subject);
            this.description = itemView.findViewById(R.id.subjectSub);
            this.provider = itemView.findViewById(R.id.bottomRight);
            this.image = itemView.findViewById(R.id.image);
            this.type = itemView.findViewById(R.id.topRight);
            this.layout = itemView.findViewById(R.id.email_thread_card_layout);
        }
    }
}
