package com.example.ramses4.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.example.ramses4.R;
import com.example.ramses4.adapter.PlayerSettingsAdapter;
import com.example.ramses4.databinding.ActivitySettingsBinding;
import com.example.ramses4.game.Player;
import com.example.ramses4.game.Settings;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class SettingsActivity extends AppCompatActivity implements PlayerSettingsAdapter.CallbackListener {

    private Settings settings;

    CompoundButton.OnCheckedChangeListener mArrowControlsSwitchListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            settings.setHasArrowControls(isChecked);
        }
    };

    RadioGroup.OnCheckedChangeListener mDifficultyRadioGroupListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == R.id.easy_radiobutton)
                settings.setDifficulty(0);
            else if (checkedId == R.id.medium_radiobutton)
                settings.setDifficulty(1);
            else
                settings.setDifficulty(2);
        }
    };

    AdapterView.OnItemSelectedListener mMaxScoreSpinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String item = (String) parent.getItemAtPosition(position);
            if (item.equals("10"))
                settings.setMaxScore(10);
            if(item.equals("20"))
                settings.setMaxScore(20);
            if (item.equals("50"))
                settings.setMaxScore(50);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            settings.setMaxScore(20);
        }
    };

    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.playersSettingsRecyclerview.setNestedScrollingEnabled(false);
        binding.activateArrowControlsSwitch.setOnCheckedChangeListener(mArrowControlsSwitchListener);
        binding.difficultiesRadiobutton.setOnCheckedChangeListener(mDifficultyRadioGroupListener);
        binding.maxScoreSpinner.setOnItemSelectedListener(mMaxScoreSpinnerListener);
        binding.settingsOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        String[] maxScores = new String[]{"10", "20", "50"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, maxScores);
        binding.maxScoreSpinner.setAdapter(adapter);

        // Init game
        settings = new Settings(getIntent().getExtras());
    }

    @Override
    protected void onStart() {
        super.onStart();
        initPlayerDisplay();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK, settings.addToIntent(new Intent()));
        finish();
    }

    private void initPlayerDisplay() {
        binding.difficultiesRadiobutton.check(convertDifficultyId(settings.getDifficulty()));
        binding.activateArrowControlsSwitch.setChecked(settings.hasArrowControls());
        binding.maxScoreSpinner.setSelection(1);
        PlayerSettingsAdapter playerSettingsAdapter = new PlayerSettingsAdapter(settings.getPlayers(), this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        RecyclerView recyclerView = findViewById(R.id.players_settings_recyclerview);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(playerSettingsAdapter);
    }

    private int convertDifficultyId(int id) {
        if (id == 0)
            return R.id.easy_radiobutton;
        if (id == 1)
            return R.id.medium_radiobutton;
        return R.id.standard_radio_button;
    }

    @Override
    public void updatePlayers(ArrayList<Player> players) {
        settings.setPlayers(players.stream().filter(p -> !p.getName().equals("")).collect(Collectors.toCollection(ArrayList::new)));
    }
}