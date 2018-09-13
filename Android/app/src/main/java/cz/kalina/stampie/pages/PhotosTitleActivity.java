package cz.kalina.stampie.pages;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import cz.kalina.stampie.R;
import cz.kalina.stampie.MainActivity;

public class PhotosTitleActivity extends FragmentActivity {

    private TextView titleView = null;

    private Button saveBtn = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos_title);

        titleView = (TextView)findViewById(R.id.PhotosTitleText);
        saveBtn = (Button)findViewById(R.id.PhotosTitleSaveBtn);
        saveBtn.setOnClickListener(saveButtonListener);
    }

    private OnClickListener saveButtonListener = new OnClickListener() {
        public void onClick(View v) {

            try {

                if (titleView.getText().toString().length() == 0) {

                    MainActivity.reportUserMessage(PhotosTitleActivity.this, "Error", "Photo title is required");
                    return;
                }

                Bundle bundle = new Bundle();
                bundle.putString("Title", (titleView != null) ? titleView.getText().toString() : "{no title}");

                Intent mIntent = new Intent();
                mIntent.putExtras(bundle);

                setResult(RESULT_OK, mIntent);
                finish();
                finish();

            } catch (Exception e) {
                MainActivity.reportError(PhotosTitleActivity.this, "Error occured when saving new photo", e.getMessage());
            }
        }
    };
}
