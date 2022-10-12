package com.example.ramses4.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ramses4.R;
import com.example.ramses4.adapter.DragTileCallback;
import com.example.ramses4.game.RamsesBoard;
import com.example.ramses4.adapter.BoardViewAdapter;
import com.example.ramses4.databinding.ActivityGameBinding;
import com.example.ramses4.game.Board;
import com.example.ramses4.game.Player;
import com.example.ramses4.game.PositionManager;
import com.example.ramses4.game.Referee;
import com.example.ramses4.game.Settings;
import com.example.ramses4.shapes.Position;
import com.example.ramses4.utils.MusicHandler;
import com.example.ramses4.utils.VibratorHandler;

import java.util.ArrayList;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class GameActivity extends AppCompatActivity implements BoardViewAdapter.DragListener {

    private Board board = new Board(8, 6);
    private PositionManager positionManager;
    private RamsesBoard ramsesBoard;
    private Referee referee;
    private ItemTouchHelper itemTouchhelper;
    private BoardViewAdapter mBoardAdapter;
    private TextView playerName;
    private TextView playerScore;
    private ImageView targetPanel;
    private TextView targetPoints;
    private VibratorHandler vibratorHandler;
    private MusicHandler musicHandler;
    private ImageView silentModeImageView;

    private DragTileCallback dragTileCallback = new DragTileCallback() {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            Position positionStart = board.intToPosition(viewHolder.getAdapterPosition());
            positionManager.setCurrentPosition(positionStart);
            referee.checkTile(ramsesBoard.getHiddenTileDescription(board.positionToInt(positionManager.getCurrentPosition())), vibratorHandler);
            updateView();
            return true;
        }
    };

    private final View.OnLayoutChangeListener mLayoutChangeListener = new View.OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View view, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            double ratio = (double) view.getWidth() / (double) ((View)view.getParent()).getWidth();
            if (ratio > 0.79 && ratio < 0.81) {
                view.removeOnLayoutChangeListener(this);
                initBoardDisplay();
                updateView();
                //resizeLayout(view);
            }
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */

    private final View.OnTouchListener mTopTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    positionManager.goUp();
                    referee.checkTile(ramsesBoard.getHiddenTileDescription(board.positionToInt(positionManager.getCurrentPosition())), vibratorHandler);
                    updateView();
                    break;
                case MotionEvent.ACTION_UP:

                    view.performClick();
                    break;
                default:
                    break;
            }
            return false;
        }
    };

    private final View.OnTouchListener mBottomTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    positionManager.goDown();
                    referee.checkTile(ramsesBoard.getHiddenTileDescription(board.positionToInt(positionManager.getCurrentPosition())), vibratorHandler);
                    updateView();
                    break;
                case MotionEvent.ACTION_UP:

                    view.performClick();
                    break;
                default:
                    break;
            }
            return false;
        }
    };

    private final View.OnTouchListener mRightTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    positionManager.goRight();
                    referee.checkTile(ramsesBoard.getHiddenTileDescription(board.positionToInt(positionManager.getCurrentPosition())), vibratorHandler);
                    updateView();
                    break;
                case MotionEvent.ACTION_UP:

                    view.performClick();
                    break;
                default:
                    break;
            }
            return false;
        }
    };

    private final View.OnTouchListener mLeftTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    positionManager.goLeft();
                    referee.checkTile(ramsesBoard.getHiddenTileDescription(board.positionToInt(positionManager.getCurrentPosition())), vibratorHandler);
                    updateView();
                    break;
                case MotionEvent.ACTION_UP:

                    view.performClick();
                    break;
                default:
                    break;
            }
            return false;
        }
    };

    private final View.OnTouchListener mSoundModeTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (musicHandler.isSilentMode()) {
                        musicHandler.unmute();
                        silentModeImageView.setImageResource(android.R.drawable.ic_lock_silent_mode_off);
                    } else {
                        musicHandler.mute();
                        silentModeImageView.setImageResource(android.R.drawable.ic_lock_silent_mode);
                    }
                case MotionEvent.ACTION_UP:
                    view.performClick();
                    break;
                default:
                    break;
            }
            return false;
        }
    };

    private ActivityGameBinding binding;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vibratorHandler = new VibratorHandler(getApplicationContext());
        musicHandler = new MusicHandler(getApplicationContext());

        binding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        binding.topButton.setOnTouchListener(mTopTouchListener);
        binding.bottomButton.setOnTouchListener(mBottomTouchListener);
        binding.rightButton.setOnTouchListener(mRightTouchListener);
        binding.leftButton.setOnTouchListener(mLeftTouchListener);
        binding.musicSilentModeButton.setOnTouchListener(mSoundModeTouchListener);
        binding.boardScreenFramelayout.addOnLayoutChangeListener(mLayoutChangeListener);

        // Init game
        initGameFromSettings(new Settings(getIntent()));

    }

    private void initBoardDisplay() {
        ramsesBoard.updateBoard(positionManager);
        mBoardAdapter = new BoardViewAdapter(ramsesBoard.getTiles(), board, this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, ramsesBoard.getNbCols());
        RecyclerView recyclerView = findViewById(R.id.board_recyclerview);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mBoardAdapter);

        playerName = findViewById(R.id.player_name_textview);
        playerScore = findViewById(R.id.player_score_textview);

        targetPanel = findViewById(R.id.target_image_view);
        targetPoints = findViewById(R.id.target_points_textview);

        itemTouchhelper = new ItemTouchHelper(dragTileCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        silentModeImageView = findViewById(R.id.music_silent_mode_button);
        silentModeImageView.setImageResource(android.R.drawable.ic_lock_silent_mode_off);
    }

    private void updateTilePosition() {
        ramsesBoard.updateBoard(positionManager);
        mBoardAdapter.updateTiles(ramsesBoard.getTiles());
        mBoardAdapter.notifyDataSetChanged();
    }

    private void updateView() {
        playerName.setText(referee.getCurrentPlayer().getName());
        playerScore.setText(referee.getCurrentPlayer().getScoreAsString());

        targetPanel.setImageResource(referee.getCurrentTarget().getDrawableId());
        targetPoints.setText(referee.getCurrentTarget().getScoreAsString());

        LinearLayout sidepanel = findViewById(R.id.side_panel_linearlayout);
        sidepanel.invalidate();

        updateTilePosition();
    }

    private void initGameFromSettings(Settings settings) {

        // Difficulty
        if (settings.getDifficulty() == 0)
            board = new Board(6, 4);
        else if (settings.getDifficulty() == 1)
            board = new Board(8, 6);
        else
            board = new Board(10, 8);

        this.positionManager = new PositionManager(board, new Position(0, 0));


        // Players
        ArrayList<Player> players = new ArrayList<>();
        if (settings.getPlayers().size() == 0) {
            players.add(new Player("Player 1", 0));
            players.add(new Player("Player 2", 0));
        } else
            players = settings.getPlayers();

        this.referee = new Referee(players, board);
        this.ramsesBoard = new RamsesBoard(board, referee.getTreasures());

        // Controls
        if (settings.hasArrowControls())
            binding.controlsFramelayout.setVisibility(View.VISIBLE);
        else
            binding.controlsFramelayout.setVisibility(View.GONE);

        // Music
        this.musicHandler.startMusic();
    }

    @Override
    public void startDragRequest(BoardViewAdapter.RecyclerViewHolder viewHolder) {
        Position newPosition = board.intToPosition(viewHolder.getAdapterPosition());
        dragTileCallback.setHorizontalMovement(positionManager.isHorizontalMovement(newPosition));
        if (positionManager.isPositionAllowed(newPosition))
            itemTouchhelper.startDrag(viewHolder);
    }

    private void resizeLayout(View view) {
        int sideLength = Math.max(Math.min(view.getWidth() / board.getNbCols(), view.getHeight() / board.getNbRows()), 30);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = view.getWidth();
        layoutParams.height = ramsesBoard.getNbCols() * sideLength;
        view.setLayoutParams(layoutParams);
    }

    @Override
    public void onBackPressed() {
        musicHandler.stopMusic();
        setResult(RESULT_OK);
        finish();
    }
}