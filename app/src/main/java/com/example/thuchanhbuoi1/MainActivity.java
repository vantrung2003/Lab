package com.example.thuchanhbuoi1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thuchanhbuoi1.login.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {
    private static TextView tvView;
    private Button btnDem;
    private int counter = 0;
    private static final int MESSAGE_UPDATE_COUNTER = 101;
    private static final int MESSAGE_COUNT_DONE = 102;
    private MyHandler handler;
    FirebaseAuth auth;
    Button btnSignout;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(getApplicationContext(), "Xin chao the gioi", Toast.LENGTH_SHORT).show();
        init();
        listener();

        auth = FirebaseAuth.getInstance();
        btnSignout = findViewById(R.id.btnSignOut);
        user = auth.getCurrentUser();
        if (user == null){
            Intent i = new Intent(getApplicationContext(), Login.class);
            startActivity(i);
            finish();
        }
btnSignout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(getApplicationContext(), Login.class);
        startActivity(i);
        finish();
    }
});



    }

    protected void init() {
        tvView = findViewById(R.id.tvView);
        btnDem = findViewById(R.id.btnDem);
    }

    private void tangBoDem() {
        new CounterTask(this).execute();
    }

    private void listener() {
        handler = new MyHandler(this);
    }

    public void startCounting(View view) {
        btnDem.setEnabled(false);
        tangBoDem();
    }

    private void updateCounter(int count) {
        tvView.setText("Thread is running: " + count);
    }

    private void onCountingDone() {
        tvView.setText("DONE");
        btnDem.setEnabled(true);
    }

    private static class MyHandler extends Handler {
        private final WeakReference<MainActivity> activityRef;

        MyHandler(MainActivity activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message message) {
            MainActivity activity = activityRef.get();
            if (activity != null) {
                switch (message.what) {
                    case MESSAGE_UPDATE_COUNTER:
                        int count = message.arg1;
                        activity.updateCounter(count);
                        break;
                    case MESSAGE_COUNT_DONE:
                        activity.onCountingDone();
                        break;
                    default:
                        tvView.setText("Loi roi!!");
                        break;
                }
            }
        }
    }

    private static class CounterTask extends AsyncTask<Void, Integer, Void> {
        private WeakReference<MainActivity> activityRef;

        CounterTask(MainActivity activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i <= 10; i++) {
                publishProgress(i);
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            MainActivity activity = activityRef.get();
            if (activity != null) {
                Message message = Message.obtain();
                message.what = MESSAGE_UPDATE_COUNTER;
                message.arg1 = values[0];
                activity.handler.sendMessage(message);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            MainActivity activity = activityRef.get();
            if (activity != null) {
                activity.handler.sendEmptyMessage(MESSAGE_COUNT_DONE);
            }
        }
    }
}
