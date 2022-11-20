package com.example.ramses4.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ramses4.R;
import com.example.ramses4.adapter.DragTileCallback;
import com.example.ramses4.game.GameBuilder;
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

    // Full screen
    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler(Looper.myLooper());
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar
            if (Build.VERSION.SDK_INT >= 30) {
                mContentView.getWindowInsetsController().hide(
                        WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
            } else {
                // Note that some of these constants are new as of API 16 (Jelly Bean)
                // and API 19 (KitKat). It is safe to use them, as they are inlined
                // at compile-time and do nothing on earlier devices.
                mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            }
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            //mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    private Board board = new Board(8, 6);
    private PositionManager positionManager;
    private GameBuilder gameBuilder;
    private RamsesBoard ramsesBoard;
    private Referee referee;
    private Settings settings;
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
            referee.checkPlayerMove(ramsesBoard.getHiddenTileDescription(board.positionToInt(positionManager.getCurrentPosition())), vibratorHandler);
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
                    referee.checkPlayerMove(ramsesBoard.getHiddenTileDescription(board.positionToInt(positionManager.getCurrentPosition())), vibratorHandler);
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
                    referee.checkPlayerMove(ramsesBoard.getHiddenTileDescription(board.positionToInt(positionManager.getCurrentPosition())), vibratorHandler);
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
                    referee.checkPlayerMove(ramsesBoard.getHiddenTileDescription(board.positionToInt(positionManager.getCurrentPosition())), vibratorHandler);
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
                    referee.checkPlayerMove(ramsesBoard.getHiddenTileDescription(board.positionToInt(positionManager.getCurrentPosition())), vibratorHandler);
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

        mVisible = true;
        //mControlsView = binding.fullscreenContentControls;
        mContentView = binding.boardScreenFramelayout;

        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

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
        settings = new Settings(getIntent());
        initGameFromSettings();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void show() {
        // Show the system bar
        if (Build.VERSION.SDK_INT >= 30) {
            mContentView.getWindowInsetsController().show(
                    WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
        } else {
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
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

        if (referee.getWinner() != null) {
            // TODO : Display fragment

            targetPanel.setImageResource(android.R.color.transparent);
            targetPoints.setText("");

            new AlertDialog.Builder(this)
                .setTitle("Winner !!")
                .setMessage(referee.getWinner().getName() + " won with " + referee.getWinner().getScore() + " points ! \nStart a new game ?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        initGameFromSettings();
                        initBoardDisplay();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

        } else {
            targetPanel.setImageResource(referee.getCurrentTarget().getDrawableId());
            targetPoints.setText(referee.getCurrentTarget().getScoreAsString());
        }

        LinearLayout sidepanel = findViewById(R.id.side_panel_linearlayout);
        sidepanel.invalidate();

        updateTilePosition();
    }

    private void initGameFromSettings() {

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
            for(Player p : settings.getPlayers())
                players.add(p.clone());

        gameBuilder = new GameBuilder(board);
        gameBuilder.buid();
        referee = new Referee(
                board,
                players,
                gameBuilder.getTargets(),
                gameBuilder.getTreasures(),
                settings.getMaxScore()
        );
        ramsesBoard = new RamsesBoard(board, referee.getTreasures());

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