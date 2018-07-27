package com.telkom.lutfi.mytelkomakses;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.telkom.lutfi.mytelkomakses.TeamLeaderGrupTeknisi.MenuGrupTeknisi;
import com.telkom.lutfi.mytelkomakses.TeamLeaderOrderTeknisi.ListOrder;
import com.telkom.lutfi.mytelkomakses.TeamLeaderOrderTeknisi.ListOrderPasangBaru;
import com.telkom.lutfi.mytelkomakses.TeamLeaderOrderTeknisi.MenuOrderTeknisi;
import com.telkom.lutfi.mytelkomakses.TeamLeaderTeknisi.TeknisiMenuActivity;

public class TeamLeaderActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_leader);

        mAuth=FirebaseAuth.getInstance();

        Toolbar toolbar =(Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        CardView listLeader = (CardView) findViewById(R.id.list_team_leader);
        listLeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeamLeaderActivity.this, ListTeamLeader.class);
                startActivity(intent);
            }
        });

        CardView Teknsi = (CardView) findViewById(R.id.teknis);
        Teknsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeamLeaderActivity.this, TeknisiMenuActivity.class);
                startActivity(intent);
            }
        });

        CardView maps = (CardView) findViewById(R.id.mapstek);
        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeamLeaderActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        CardView Order = (CardView) findViewById(R.id.ordertek);
        Order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeamLeaderActivity.this, MenuGrupTeknisi.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id= item.getItemId();

        switch (id){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, LoginActivity.class));

                break;
        }
        return true;
    }
}
