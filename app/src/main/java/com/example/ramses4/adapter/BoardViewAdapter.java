package com.example.ramses4.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ramses4.R;
import com.example.ramses4.game.Board;
import com.example.ramses4.shapes.Tile;

import java.util.ArrayList;

public class BoardViewAdapter extends RecyclerView.Adapter<BoardViewAdapter.RecyclerViewHolder> {

    private ArrayList<Tile> tileList;
    private Board board;
    private final DragListener mDragListener;

    public BoardViewAdapter(ArrayList<Tile> tileList, Board board, DragListener dragListener) {
        this.tileList = tileList;
        this.board = board;
        this.mDragListener = dragListener;

    }

    public void updateTiles(ArrayList<Tile> tiles) {
        this.tileList = tiles;
    }

    @NonNull
    @Override
    public BoardViewAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_layout, parent, false);
        View root = view.getRootView();
        ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
        View grandParent = (View)parent.getParent();
        int sideLength = Math.max(Math.min(grandParent.getWidth() / board.getNbCols(), grandParent.getHeight() / board.getNbRows()), 30);
        layoutParams.width = sideLength;
        layoutParams.height = sideLength;
        root.setLayoutParams(layoutParams);
        return new RecyclerViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull BoardViewAdapter.RecyclerViewHolder holder, int position) {
        Tile tile = tileList.get(position);
        holder.foregroundImageView.setImageResource(tile.getDrawableId());
        holder.foregroundImageView.setRotation(tile.getRotation());
        holder.foregroundImageView.setBackgroundResource(tile.getBackgroundColorId());
        holder.foregroundImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN)
                    mDragListener.startDragRequest(holder);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return tileList.size();
    }

    public interface DragListener {
        void startDragRequest(RecyclerViewHolder viewHolder);
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private ImageView foregroundImageView;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            foregroundImageView = itemView.findViewById(R.id.tile_image_view);
        }

    }
}
