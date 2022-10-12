package com.example.ramses4.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ramses4.R;
import com.example.ramses4.game.Player;

import java.util.ArrayList;

public class PlayerSettingsAdapter extends RecyclerView.Adapter<PlayerSettingsAdapter.RecyclerViewHolder> {

    private ArrayList<Player> players;
    private final CallbackListener mCallbackListener;

    public PlayerSettingsAdapter(ArrayList<Player> players, CallbackListener mCallbackListener) {
        this.players = players;
        this.players.add(0, new Player("", 0));
        this.mCallbackListener = mCallbackListener;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_player_item, parent, false);
        return new PlayerSettingsAdapter.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        Player player = players.get(position);

        if (position == 0)  {
            holder.setHolderAsEntry();
            holder.imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (players.size() <= 4 && !holder.editText.getText().toString().equals(""))
                        players.add(new Player(holder.editText.getText().toString(), 0));

                    mCallbackListener.updatePlayers(players);
                    notifyDataSetChanged();
                }
            });
        } else {
            holder.setHolderAsItem(player);
            holder.imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    players.remove(holder.getAdapterPosition());
                    mCallbackListener.updatePlayers(players);
                    notifyDataSetChanged();
                }
            });
        }
    }

    public interface CallbackListener {
        void updatePlayers(ArrayList<Player> players);
    }


    @Override
    public int getItemCount() {
        return players.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private EditText editText;
        private TextView textView;
        private ImageButton imageButton;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            editText = itemView.findViewById(R.id.playername_settings_editText);
            textView = itemView.findViewById(R.id.playername_settings_textview);
            imageButton = itemView.findViewById(R.id.player_action_button);
        }

        public void setHolderAsItem(Player player) {
            textView.setVisibility(View.VISIBLE);
            String playerTag = getAdapterPosition() + ". " + player.getName();
            textView.setText(playerTag);
            editText.setVisibility(View.GONE);
            imageButton.setImageResource(android.R.drawable.ic_menu_delete);
        }

        public void setHolderAsEntry() {
            textView.setVisibility(View.GONE);
            editText.setVisibility(View.VISIBLE);
            editText.setText("");
            editText.clearFocus();
            editText.setFocusableInTouchMode(true);
            imageButton.setImageResource(android.R.drawable.ic_menu_add);
        }
    }
}
